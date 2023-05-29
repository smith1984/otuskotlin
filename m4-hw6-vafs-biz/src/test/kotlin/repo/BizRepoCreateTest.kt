package ru.beeline.vafs.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.beeline.vafs.biz.VafsRuleProcessor
import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.VafsCorSettings
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.common.permissions.VafsPrincipalModel
import ru.beeline.vafs.common.permissions.VafsUserGroups
import ru.beeline.vafs.common.repo.DbRuleResponse
import ru.beeline.vafs.repository.test.RuleRepositoryMock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizRepoCreateTest {

    private val userId = VafsUserId("operator-321")
    private val command = VafsCommand.CREATE
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val repo = RuleRepositoryMock(
        invokeCreateRule = {
            DbRuleResponse(
                isSuccess = true,
                data = VafsRule(
                    id = VafsRuleId(uuid),
                    description = it.rule.description,
                    userId = userId,
                    priority = it.rule.priority,
                    listForNumberA = it.rule.listForNumberA,
                    typeOperationA = it.rule.typeOperationA,
                    listForNumberB = it.rule.listForNumberB,
                    typeOperationB = it.rule.typeOperationB,
                    typeOperationCount = it.rule.typeOperationCount,
                    targetCount = it.rule.targetCount,
                    valueIsTrue = it.rule.valueIsTrue,
                    typeOperationAB = it.rule.typeOperationAB,
                    typeOperationABCount = it.rule.typeOperationABCount,
                )
            )
        }
    )
    private val settings = VafsCorSettings(
        repoTest = repo
    )
    private val processor = VafsRuleProcessor(settings)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun repoCreateSuccessTest() = runTest {
        val ctx = VafsContext(
            command = command,
            state = VafsState.NONE,
            workMode = VafsWorkMode.TEST,
            ruleRequest = VafsRule(
                description = "create rule description",
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
            ),
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
        assertNotEquals(VafsRuleId.NONE, ctx.ruleResponse.id)
        assertEquals("create rule description", ctx.ruleResponse.description)
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
}
