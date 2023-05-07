package ru.beeline.vafs.repository.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.common.repo.*
import kotlin.test.Test
import kotlin.test.assertEquals


@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoRuleUpdateTest {
    abstract val repo: IRuleRepository
    protected open val updateSucc = initObjects[0]
    protected val updateConc = initObjects[1]
    private val updateIdNotFound = VafsRuleId("rule-repo-update-not-found")
    protected val lockBad = VafsRuleLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = VafsRuleLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSucc by lazy {
        VafsRule(
            id = updateSucc.id,
            description = "update object description",
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
            lock = initObjects.first().lock,
        )
    }

    private val reqUpdateNotFound = VafsRule(
        id = updateIdNotFound,
        description = "update object not found description",
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
        lock = initObjects.first().lock,
    )

    private val reqUpdateConc = VafsRule(
        id = updateConc.id,
        description = "update object not found description",
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
        lock = lockBad,
    )

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateRule(DbRuleRequest(reqUpdateSucc))
        assertEquals(true, result.isSuccess)
        assertEquals(reqUpdateSucc.id, result.data?.id)
        assertEquals(reqUpdateSucc.description, result.data?.description)
        assertEquals(reqUpdateSucc.priority, result.data?.priority)
        assertEquals(reqUpdateSucc.listForNumberA, result.data?.listForNumberA)
        assertEquals(reqUpdateSucc.typeOperationA, result.data?.typeOperationA)
        assertEquals(reqUpdateSucc.listForNumberB, result.data?.listForNumberB)
        assertEquals(reqUpdateSucc.typeOperationB, result.data?.typeOperationB)
        assertEquals(reqUpdateSucc.typeOperationCount, result.data?.typeOperationCount)
        assertEquals(reqUpdateSucc.targetCount, result.data?.targetCount)
        assertEquals(reqUpdateSucc.valueIsTrue, result.data?.valueIsTrue)
        assertEquals(reqUpdateSucc.typeOperationAB, result.data?.typeOperationAB)
        assertEquals(reqUpdateSucc.typeOperationABCount, result.data?.typeOperationABCount)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateRule(DbRuleRequest(reqUpdateNotFound))
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runTest {
        val result = repo.updateRule(DbRuleRequest(reqUpdateConc))
        assertEquals(false, result.isSuccess)
        val error = result.errors.find { it.code == "concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object : BaseInitRules("update") {
        override val initObjects: List<VafsRule> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}
