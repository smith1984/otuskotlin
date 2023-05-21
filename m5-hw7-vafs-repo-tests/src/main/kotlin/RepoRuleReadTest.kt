package ru.beeline.vafs.repository.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.common.repo.*
import kotlin.test.Test
import kotlin.test.assertEquals


@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoRuleReadTest {
    abstract val repo: IRuleRepository
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readRule(DbRuleIdRequest(readSucc.id))
        assertEquals(true, result.isSuccess)
        assertEquals(readSucc, result.data)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readRule(DbRuleIdRequest(notFoundId))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitRules("read") {
        override val initObjects: List<VafsRule> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = VafsRuleId("rule-repo-read-notFound")

    }
}
