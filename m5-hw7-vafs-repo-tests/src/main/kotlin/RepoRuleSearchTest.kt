package ru.beeline.vafs.repository.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.common.repo.*
import kotlin.test.Test
import kotlin.test.assertEquals


@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoRuleSearchTest {
    abstract val repo: IRuleRepository

    protected open val initializedObjects: List<VafsRule> = initObjects

    @Test
    fun searchUser() = runRepoTest {
        val result = repo.searchRule(DbRuleFilterRequest(userId = searchUserId))
        assertEquals(true, result.isSuccess)
        val expected = listOf(initializedObjects[1], initializedObjects[2]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data?.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }


    companion object: BaseInitRules("search") {

        val searchUserId = VafsUserId("operator-124")
        override val initObjects: List<VafsRule> = listOf(
            createInitTestModel("rule1"),
            createInitTestModel("rule2", userId = searchUserId),
            createInitTestModel("rule3", userId = searchUserId),
        )
    }
}
