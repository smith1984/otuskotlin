package ru.beeline.vafs.repo.posgresql

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.beeline.vafs.common.helpers.errorRepoConcurrency
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.common.repo.*
import ru.beeline.vafs.repo.posgresql.ListNumberTable.value
import ru.beeline.vafs.repo.posgresql.RuleTable.listForNumberA
import ru.beeline.vafs.repo.posgresql.RuleTable.listForNumberB
import ru.beeline.vafs.repo.posgresql.RuleTable.userId
import ru.beeline.vafs.repo.posgresql.UserTable.name
import java.sql.SQLException
import java.util.UUID

private const val notFoundCode = "not-found"

class RuleRepoPostgresql(
    properties: SqlProperties,
    initObjects: Collection<VafsRule> = emptyList(),
    val randomUuid: () -> String = { UUID.randomUUID().toString() },
) : IRuleRepository {
    private val db by lazy { SqlConnector(properties).database(RuleTable, UserTable, ListNumberTable) }

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(item: VafsRule): DbRuleResponse {
        return safeTransaction({

            val uuidListA = UUID.randomUUID().toString()
            val uuidListB = UUID.randomUUID().toString()

            UserTable.insertIgnore {
                it[id] = UUID.randomUUID().toString()
                it[name] = item.userId.asString()
            }

            val (realUserId, realUserName) =
                UserTable.select(UserTable.name eq item.userId.asString()).single()
                    .let { resultRow -> Pair(resultRow[UserTable.id], resultRow[UserTable.name]) }


            val resListA = ListNumberTable.batchInsert(item.listForNumberA) { value ->
                this[ListNumberTable.id] = uuidListA
                this[ListNumberTable.value] = value
            }.toList().map { resultRow -> resultRow[value] }

            val resListB = ListNumberTable.batchInsert(item.listForNumberB) { value ->
                this[ListNumberTable.id] = uuidListB
                this[ListNumberTable.value] = value
            }.toList().map { resultRow -> resultRow[value] }

            val resRule = RuleTable.insert {
                it[id] = item.id.asString()
                it[description] = item.description
                it[userId] = realUserId
                it[priority] = item.priority
                it[listForNumberA] = uuidListA
                it[typeOperationA] = item.typeOperationA
                it[listForNumberB] = uuidListB
                it[typeOperationB] = item.typeOperationB
                it[typeOperationCount] = item.typeOperationCount
                it[targetCount] = item.targetCount
                it[valueIsTrue] = item.valueIsTrue
                it[typeOperationAB] = item.typeOperationAB
                it[typeOperationABCount] = item.typeOperationABCount
                it[lock] = item.lock.asString()
            }

            DbRuleResponse(RuleTable.from(resRule, resListA, resListB, realUserName), true)
        }, {
            DbRuleResponse(
                data = null,
                isSuccess = false,
                errors = listOf(VafsError(message = message ?: localizedMessage))
            )
        })
    }

    private fun read(recordRule: ResultRow): VafsRule {
        val listA = ListNumberTable.select(ListNumberTable.id eq recordRule[listForNumberA]).toList()
            .map { resultRow -> resultRow[value] }
        val listB = ListNumberTable.select(ListNumberTable.id eq recordRule[listForNumberB]).toList()
            .map { resultRow -> resultRow[value] }
        val user =
            UserTable.select(UserTable.id eq recordRule[userId]).single().let { resultRow -> resultRow[name] }

        return RuleTable.from(recordRule, listA, listB, user)
    }

    override suspend fun createRule(rq: DbRuleRequest): DbRuleResponse {
        val rule = rq.rule.copy(
            lock = VafsRuleLock(randomUuid()),
            id = rq.rule.id.takeIf { it != VafsRuleId.NONE } ?: VafsRuleId(randomUuid()))
        return save(rule)
    }

    override suspend fun readRule(rq: DbRuleIdRequest): DbRuleResponse {
        return safeTransaction({
            val recordRule = RuleTable.select { RuleTable.id eq rq.id.asString() }.single()
            val local = read(recordRule)

            DbRuleResponse(local, true)
        }, {
            val err = when (this) {
                is NoSuchElementException -> VafsError(field = "id", message = "Not Found", code = notFoundCode)
                is IllegalArgumentException -> VafsError(message = "More than one element with the same id")
                else -> VafsError(message = localizedMessage)
            }
            DbRuleResponse(data = null, isSuccess = false, errors = listOf(err))
        })
    }


    override suspend fun deleteRule(rq: DbRuleIdRequest): DbRuleResponse {
        val key = rq.id.takeIf { it != VafsRuleId.NONE }?.asString() ?: return resultErrorEmptyId

        return safeTransaction({
            val recordRule = RuleTable.select { RuleTable.id eq key }.singleOrNull()
            val local = recordRule?.let {
                read(it)
            } ?: return@safeTransaction resultErrorNotFound

            if (local.lock == rq.lock) {
                ListNumberTable.deleteWhere { ListNumberTable.id eq recordRule[listForNumberA] }
                ListNumberTable.deleteWhere { ListNumberTable.id eq recordRule[listForNumberB] }
                RuleTable.deleteWhere { RuleTable.id eq rq.id.asString() }
                DbRuleResponse(data = local, isSuccess = true)
            } else {
                resultErrorConcurrent(rq.lock.asString(), local)
            }
        }, {
            DbRuleResponse(
                data = null,
                isSuccess = false,
                errors = listOf(VafsError(field = "id", message = "Not Found"))
            )
        })
    }

    override suspend fun searchRule(rq: DbRuleFilterRequest): DbRulesResponse {
        return safeTransaction({
            val condition = Op.build {
                buildList {
                    add(Op.TRUE)
                    if (rq.userId != VafsUserId.NONE) {
                        add(UserTable.name eq rq.userId.asString())
                    }
                    if (rq.descriptionFilter.isNotBlank()) {
                        add((RuleTable.description like "%${rq.descriptionFilter}%"))
                    }
                }.reduce { a, b -> a and b }
            }

            val results = (RuleTable.join(
                UserTable,
                JoinType.INNER,
                additionalConstraint = { RuleTable.userId eq UserTable.id })).select(condition)

            val data = results.map { resultRow ->
                val listA = ListNumberTable.select(ListNumberTable.id eq resultRow[listForNumberA]).toList()
                    .map { rr -> rr[value] }
                val listB = ListNumberTable.select(ListNumberTable.id eq resultRow[listForNumberB]).toList()
                    .map { rr -> rr[value] }
                val user = resultRow[UserTable.name]
                RuleTable.from(resultRow, listA, listB, user)
            }
            DbRulesResponse(data = data, isSuccess = true)
        }, {
            DbRulesResponse(data = emptyList(), isSuccess = false, listOf(VafsError(message = localizedMessage)))
        })
    }

    override suspend fun updateRule(rq: DbRuleRequest): DbRuleResponse {
        val key = rq.rule.id.takeIf { it != VafsRuleId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.rule.lock.takeIf { it != VafsRuleLock.NONE }?.asString()
        val newRule = rq.rule.copy(lock = VafsRuleLock(randomUuid()))

        return safeTransaction({

            val recordRule = RuleTable.select { RuleTable.id eq key }.singleOrNull()
            val local = recordRule?.let {
                read(it)
            } ?: return@safeTransaction resultErrorNotFound

            val uuidListA = recordRule.let { resultRow -> resultRow[listForNumberA] }
            val uuidListB = recordRule.let { resultRow -> resultRow[listForNumberB] }

            return@safeTransaction when (oldLock) {
                null, local.lock.asString() -> update(newRule, uuidListA, uuidListB)
                else -> resultErrorConcurrent(local.lock.asString(), local)
            }
        }, {
            DbRuleResponse(
                data = rq.rule,
                isSuccess = false,
                errors = listOf(VafsError(field = "id", message = "Not Found", code = notFoundCode))
            )
        })

    }

    private fun updateList(uuid: String, lst: List<String>): List<String> {
        val oldList =
            ListNumberTable.select(ListNumberTable.id eq uuid).toList().map { resultRows -> resultRows[value] }

        val lstForInsert = lst.minus(oldList)
        val lstForDelete = oldList.minus(lst)

        fun condition(value: String) = Op.build {
            buildList {
                add(Op.TRUE)
                add(ListNumberTable.id eq uuid)
                add(ListNumberTable.value eq value)
            }.reduce{a,b -> a and b}
        }

        lstForDelete.forEach{value ->
            ListNumberTable.deleteWhere { condition(value) }
        }

        ListNumberTable.batchInsert(lstForInsert) { value ->
            this[ListNumberTable.id] = uuid
            this[ListNumberTable.value] = value
        }

        return ListNumberTable.select {
            ListNumberTable.id eq uuid
        }.toList().map { resultRow -> resultRow[value] }
    }

    private fun update(newRule: VafsRule, uuidListA: String, uuidListB: String): DbRuleResponse {
        UserTable.insertIgnore {
            it[id] = UUID.randomUUID().toString()
            it[name] = newRule.userId.asString()
        }

        val (realUserId, realUserName) =
            UserTable.select(UserTable.name eq newRule.userId.asString()).single()
                .let { resultRow -> Pair(resultRow[UserTable.id], resultRow[UserTable.name]) }

        val listA = updateList(uuidListA, newRule.listForNumberA)
        val listB = updateList(uuidListB, newRule.listForNumberB)

        RuleTable.update({ RuleTable.id.eq(newRule.id.asString()) }) {
            it[description] = newRule.description
            it[userId] = realUserId
            it[priority] = newRule.priority
            it[typeOperationA] = newRule.typeOperationA
            it[typeOperationB] = newRule.typeOperationB
            it[typeOperationCount] = newRule.typeOperationCount
            it[targetCount] = newRule.targetCount
            it[valueIsTrue] = newRule.valueIsTrue
            it[typeOperationAB] = newRule.typeOperationAB
            it[typeOperationABCount] = newRule.typeOperationABCount
            it[lock] = newRule.lock.asString()
        }
        val result = RuleTable.select { RuleTable.id.eq(newRule.id.asString()) }.single()

        return DbRuleResponse(data = RuleTable.from(result, listA, listB, realUserName), isSuccess = true)
    }

    private fun <T> safeTransaction(statement: Transaction.() -> T, handleException: Throwable.() -> T): T {
        return try {
            transaction(db, statement)
        } catch (e: SQLException) {
            throw e
        } catch (e: Throwable) {
            return handleException(e)
        }
    }

    companion object {
        val resultErrorEmptyId = DbRuleResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                VafsError(
                    field = "id",
                    message = "Id must not be null or blank"
                )
            )
        )

        fun resultErrorConcurrent(lock: String, ad: VafsRule?) = DbRuleResponse(
            data = ad,
            isSuccess = false,
            errors = listOf(
                errorRepoConcurrency(VafsRuleLock(lock), ad?.lock?.let { VafsRuleLock(it.asString()) }
                )
            ))

        val resultErrorNotFound = DbRuleResponse(
            isSuccess = false,
            data = null,
            errors = listOf(
                VafsError(
                    field = "id",
                    message = "Not Found",
                    code = notFoundCode
                )
            )
        )
    }
}