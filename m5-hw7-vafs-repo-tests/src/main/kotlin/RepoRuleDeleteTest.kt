package ru.beeline.vafs.repository.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.common.repo.*
import kotlin.test.Test
import kotlin.test.assertEquals


@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoRuleDeleteTest {
    abstract val repo: IRuleRepository

    @Test
    fun deleteSuccess() = runRepoTest {
        val result = repo.deleteRule(DbRuleIdRequest(successId, lock = lockOld))

        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readRule(DbRuleIdRequest(notFoundId))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun deleteConcurrency() = runTest {
        val result = repo.deleteRule(DbRuleIdRequest(concurrencyId, lock = lockBad))

        assertEquals(false, result.isSuccess)
        val error = result.errors.find { it.code == "concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(lockOld, result.data?.lock)
    }

    companion object : BaseInitRules("delete") {
        override val initObjects: List<VafsRule> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )

        val successId = VafsRuleId(initObjects[0].id.asString())
        val notFoundId = VafsRuleId("rule-repo-delete-notFound")
        val concurrencyId = VafsRuleId(initObjects[1].id.asString())
    }
}
