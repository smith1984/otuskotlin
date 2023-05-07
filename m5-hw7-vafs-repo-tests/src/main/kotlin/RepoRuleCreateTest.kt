package ru.beeline.vafs.repository.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.common.repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoRuleCreateTest {
    abstract val repo: IRuleRepository

    protected open val lockNew: VafsRuleLock = VafsRuleLock("20000000-0000-0000-0000-000000000002")
    
    private val createObj = VafsRule(
        description = "create rule description",
        userId = VafsUserId("operator-123"),
        priority = 1000,
        listForNumberA = listOf("79995551111"),
        typeOperationA = VafsTypeOperationList.EXCLUDE,
        listForNumberB = listOf("79995551113", "79995551112"),
        typeOperationB = VafsTypeOperationList.EXCLUDE,
        typeOperationCount = VafsTypeOperationCount.LESS,
        targetCount = 3000,
        valueIsTrue = false,
        typeOperationAB = VafsTypeOperationBool.AND,
        typeOperationABCount = VafsTypeOperationBool.AND,
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createRule(DbRuleRequest(createObj))
        val expected = createObj.copy(id = result.data?.id ?: VafsRuleId.NONE)
        assertEquals(true, result.isSuccess)
        assertEquals(expected.userId, result.data?.userId)
        assertEquals(expected.description, result.data?.description)
        assertEquals(expected.priority, result.data?.priority)
        assertEquals(expected.listForNumberA, result.data?.listForNumberA)
        assertEquals(expected.typeOperationA, result.data?.typeOperationA)
        assertEquals(expected.listForNumberB, result.data?.listForNumberB)
        assertEquals(expected.typeOperationB, result.data?.typeOperationB)
        assertEquals(expected.typeOperationCount, result.data?.typeOperationCount)
        assertEquals(expected.targetCount, result.data?.targetCount)
        assertEquals(expected.valueIsTrue, result.data?.valueIsTrue)
        assertEquals(expected.typeOperationAB, result.data?.typeOperationAB)
        assertEquals(expected.typeOperationABCount, result.data?.typeOperationABCount)
        assertNotEquals(VafsRuleId.NONE, result.data?.id)
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitRules("create") {
        override val initObjects: List<VafsRule> = emptyList()
    }
}
