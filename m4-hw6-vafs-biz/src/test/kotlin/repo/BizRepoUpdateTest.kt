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

@OptIn(ExperimentalCoroutinesApi::class)
class BizRepoUpdateTest {

    private val userId = VafsUserId("operator-321")
    private val uuid = VafsRuleId("10000000-0000-0000-0000-000000000001")
    private val command = VafsCommand.UPDATE
    private val initRule = VafsRule(
        id = uuid,
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
    )
    private val repo by lazy { RuleRepositoryMock(
        invokeReadRule = {
            DbRuleResponse(
                isSuccess = true,
                data = initRule,
            )
        },
        invokeUpdateRule = {
            DbRuleResponse(
                isSuccess = true,
                data = VafsRule(
                    id = uuid,
                    description = "new rule description",
                    priority = 999,
                    listForNumberA = listOf("79995551112"),
                    typeOperationA = VafsTypeOperationList.INCLUDE,
                    listForNumberB = listOf("79995551115", "79995551117"),
                    typeOperationB = VafsTypeOperationList.INCLUDE,
                    typeOperationCount = VafsTypeOperationCount.EQUAL,
                    targetCount = 3001,
                    valueIsTrue = true,
                    typeOperationAB = VafsTypeOperationBool.XOR,
                    typeOperationABCount = VafsTypeOperationBool.OR,
                )
            )
        }
    ) }
    private val settings by lazy {
        VafsCorSettings(
            repoTest = repo
        )
    }
    private val processor by lazy { VafsRuleProcessor(settings) }

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val ruleToUpdate = VafsRule(
            id = uuid,
            description = "new rule description",
            priority = 999,
            listForNumberA = listOf("79995551112"),
            typeOperationA = VafsTypeOperationList.INCLUDE,
            listForNumberB = listOf("79995551115", "79995551117"),
            typeOperationB = VafsTypeOperationList.INCLUDE,
            typeOperationCount = VafsTypeOperationCount.EQUAL,
            targetCount = 3001,
            valueIsTrue = true,
            typeOperationAB = VafsTypeOperationBool.XOR,
            typeOperationABCount = VafsTypeOperationBool.OR,
            lock = VafsRuleLock("123-234-abc-ABC"),
        )
        val ctx = VafsContext(
            command = command,
            state = VafsState.NONE,
            workMode = VafsWorkMode.TEST,
            ruleRequest = ruleToUpdate,
        )
        processor.exec(ctx)
        assertEquals(VafsState.FINISHING, ctx.state)
        assertEquals(ruleToUpdate.id, ctx.ruleResponse.id)
        assertEquals(ruleToUpdate.description, ctx.ruleResponse.description)
        assertEquals(ruleToUpdate.priority, ctx.ruleResponse.priority)
        assertEquals(ruleToUpdate.listForNumberA, ctx.ruleResponse.listForNumberA)
        assertEquals(ruleToUpdate.listForNumberB, ctx.ruleResponse.listForNumberB)
        assertEquals(ruleToUpdate.typeOperationA, ctx.ruleResponse.typeOperationA)
        assertEquals(ruleToUpdate.typeOperationB, ctx.ruleResponse.typeOperationB)
        assertEquals(ruleToUpdate.typeOperationCount, ctx.ruleResponse.typeOperationCount)
        assertEquals(ruleToUpdate.typeOperationAB, ctx.ruleResponse.typeOperationAB)
        assertEquals(ruleToUpdate.typeOperationABCount, ctx.ruleResponse.typeOperationABCount)
        assertEquals(ruleToUpdate.targetCount, ctx.ruleResponse.targetCount)
        assertEquals(ruleToUpdate.valueIsTrue, ctx.ruleResponse.valueIsTrue)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}
