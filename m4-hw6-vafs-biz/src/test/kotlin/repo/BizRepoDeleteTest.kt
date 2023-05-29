package ru.beeline.vafs.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.beeline.vafs.repository.test.RuleRepositoryMock
import ru.beeline.vafs.biz.VafsRuleProcessor
import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.VafsCorSettings
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.common.permissions.VafsPrincipalModel
import ru.beeline.vafs.common.permissions.VafsUserGroups
import ru.beeline.vafs.common.repo.DbRuleResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class BizRepoDeleteTest {

    private val userId = VafsUserId("operator-321")
    private val command = VafsCommand.DELETE
    private val initRule = VafsRule(
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
        userId = userId,
        lock = VafsRuleLock("123-234-abc-ABC"),
    )
    private val repo by lazy {
        RuleRepositoryMock(
            invokeReadRule = {
               DbRuleResponse(
                   isSuccess = true,
                   data = initRule,
               )
            },
            invokeDeleteRule = {
                if (it.id == initRule.id)
                    DbRuleResponse(
                        isSuccess = true,
                        data = initRule
                    )
                else DbRuleResponse(isSuccess = false, data = null)
            }
        )
    }
    private val settings by lazy {
        VafsCorSettings(
            repoTest = repo
        )
    }
    private val processor by lazy { VafsRuleProcessor(settings) }

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val adToUpdate = VafsRule(
            id = VafsRuleId("10000000-0000-0000-0000-000000000001"),
            lock = VafsRuleLock("123-234-abc-ABC"),
        )
        val ctx = VafsContext(
            command = command,
            state = VafsState.NONE,
            workMode = VafsWorkMode.TEST,
            ruleRequest = adToUpdate,
            principal = VafsPrincipalModel(
                id = userId,
                groups = setOf(
                    VafsUserGroups.USER,
                    VafsUserGroups.TEST,
                )
            ),
        )
        processor.exec(ctx)
        assertEquals(VafsState.FINISHING, ctx.state)
        assertTrue { ctx.errors.isEmpty() }
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

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)
}
