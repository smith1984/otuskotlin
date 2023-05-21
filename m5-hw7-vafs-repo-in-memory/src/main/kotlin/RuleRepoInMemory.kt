package ru.beeline.vafs.repo.inmemory

import io.github.reactivecircus.cache4k.Cache
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.common.repo.*
import ru.beeline.vafs.repo.inmemory.model.RuleEntity
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import java.util.UUID
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.beeline.vafs.common.helpers.errorRepoConcurrency

class  RuleRepoInMemory(
    initObjects: List<VafsRule> = emptyList(),
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { UUID.randomUUID().toString() },
) : IRuleRepository {

    private val cache = Cache.Builder<String, RuleEntity>()
        .expireAfterWrite(ttl)
        .build()

    private val mutex: Mutex = Mutex()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(rule: VafsRule) {
        val entity = RuleEntity(rule)
        if (entity.id == null) {
            return
        }
        cache.put(entity.id, entity)
    }

    override suspend fun createRule(rq: DbRuleRequest): DbRuleResponse {
        val key = randomUuid()
        val rule = rq.rule.copy(id = VafsRuleId(key))
        val entity = RuleEntity(rule)
        cache.put(key, entity)
        return DbRuleResponse(
            data = rule,
            isSuccess = true,
        )
    }

    override suspend fun readRule(rq: DbRuleIdRequest): DbRuleResponse {
        val key = rq.id.takeIf { it != VafsRuleId.NONE }?.asString() ?: return resultErrorEmptyId
        return cache.get(key)
            ?.let {
                DbRuleResponse(
                    data = it.toInternal(),
                    isSuccess = true,
                )
            } ?: resultErrorNotFound
    }

    private suspend fun doUpdate(key: String, oldLock: String, okBlock: (oldRule: RuleEntity) -> DbRuleResponse): DbRuleResponse = mutex.withLock {
        val oldRule = cache.get(key)
        when {
            oldRule == null -> resultErrorNotFound
            oldRule.lock != oldLock -> DbRuleResponse(
                data = oldRule.toInternal(),
                isSuccess = false,
                errors = listOf(errorRepoConcurrency(VafsRuleLock(oldLock), oldRule.lock?.let { VafsRuleLock(it) }))
            )

            else -> okBlock(oldRule)
        }
    }

    override suspend fun updateRule(rq: DbRuleRequest): DbRuleResponse {
        val key = rq.rule.id.takeIf { it != VafsRuleId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.rule.lock.takeIf { it != VafsRuleLock.NONE }?.asString() ?: return resultErrorEmptyLock
        val newRule = rq.rule.copy()
        val entity = RuleEntity(newRule)
        return doUpdate(key, oldLock) {
            cache.put(key, entity)
            DbRuleResponse(
                data = newRule,
                isSuccess = true,
            )
        }
    }

    override suspend fun deleteRule(rq: DbRuleIdRequest): DbRuleResponse {
        val key = rq.id.takeIf { it != VafsRuleId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.lock.takeIf { it != VafsRuleLock.NONE }?.asString() ?: return resultErrorEmptyLock
        return doUpdate(key, oldLock) {oldRule ->
            cache.invalidate(key)
            DbRuleResponse(
                data = oldRule.toInternal(),
                isSuccess = true,
            )
        }
    }

    override suspend fun searchRule(rq: DbRuleFilterRequest): DbRulesResponse {
        val result = cache.asMap().asSequence()
            .filter { entry ->
                rq.userId.takeIf { it != VafsUserId.NONE }?.let {
                    it.asString() == entry.value.userId
                } ?: true
            }
            .filter { entry ->
                rq.descriptionFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.description?.contains(it) ?: false
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        return DbRulesResponse(
            data = result,
            isSuccess = true
        )
    }

    companion object {
        val resultErrorEmptyId = DbRuleResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                VafsError(
                    code = "id-empty",
                    group = "validation",
                    field = "id",
                    message = "Id must not be null or blank"
                )
            )
        )
        val resultErrorEmptyLock = DbRuleResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                VafsError(
                    code = "lock-empty",
                    group = "validation",
                    field = "lock",
                    message = "Lock must not be null or blank"
                )
            )
        )
        val resultErrorNotFound = DbRuleResponse(
            isSuccess = false,
            data = null,
            errors = listOf(
                VafsError(
                    code = "not-found",
                    field = "id",
                    message = "Not Found"
                )
            )
        )
    }
}
