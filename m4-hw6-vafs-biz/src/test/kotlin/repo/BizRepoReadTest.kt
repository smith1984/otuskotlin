package ru.beeline.vafs.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.beeline.vafs.repository.test.RuleRepositoryMock
import ru.beeline.vafs.biz.VafsRuleProcessor
import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.VafsCorSettings
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.common.repo.DbRuleResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoReadTest {

    private val userId = VafsUserId("operator-321")
    private val command = VafsCommand.READ
    private val initRule = VafsRule(
        userId = userId,
        id = VafsRuleId("10000000-0000-0000-0000-000000000001"),
        description = "rule description",
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
    private val repo by lazy {
        RuleRepositoryMock(
            invokeReadRule = {
                DbRuleResponse(
                    isSuccess = true,
                    data = initRule,
                )
            }
        )
    }
    private val settings by lazy {
        VafsCorSettings(
            repoTest = repo
        )
    }
    private val processor by lazy { VafsRuleProcessor(settings) }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun repoReadSuccessTest() = runTest {
        val ctx = VafsContext(
            command = command,
            state = VafsState.NONE,
            workMode = VafsWorkMode.TEST,
            ruleRequest = VafsRule(
                id = VafsRuleId("10000000-0000-0000-0000-000000000001"),
            ),
        )
        processor.exec(ctx)
        assertEquals(VafsState.FINISHING, ctx.state)
        assertEquals(initRule.id, ctx.ruleResponse.id)
        assertEquals("rule description", ctx.ruleResponse.description)
        assertEquals(1000, ctx.ruleResponse.priority)
        assertEquals(listOf("79995551111"), ctx.ruleResponse.listForNumberA)
        assertEquals(listOf("79995551113", "79995551112"), ctx.ruleResponse.listForNumberB)
        assertEquals(VafsTypeOperationList.EXCLUDE, ctx.ruleResponse.typeOperationA)
        assertEquals(VafsTypeOperationList.EXCLUDE, ctx.ruleResponse.typeOperationB)
        assertEquals(VafsTypeOperationCount.LESS, ctx.ruleResponse.typeOperationCount)
        assertEquals(VafsTypeOperationBool.AND, ctx.ruleResponse.typeOperationAB)
        assertEquals(VafsTypeOperationBool.AND, ctx.ruleResponse.typeOperationABCount)
        assertEquals(3000, ctx.ruleResponse.targetCount)
        assertEquals(false, ctx.ruleResponse.valueIsTrue)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun repoReadNotFoundTest() = repoNotFoundTest(command)
}
