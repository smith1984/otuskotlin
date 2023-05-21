package ru.beeline.vafs.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.beeline.vafs.repository.test.RuleRepositoryMock
import ru.beeline.vafs.biz.VafsRuleProcessor
import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.VafsCorSettings
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.common.repo.DbRuleResponse
import kotlin.test.assertEquals

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
)
private val repo = RuleRepositoryMock(
        invokeReadRule = {
            if (it.id == initRule.id) {
                DbRuleResponse(
                    isSuccess = true,
                    data = initRule,
                )
            } else DbRuleResponse(
                isSuccess = false,
                data = null,
                errors = listOf(VafsError(message = "Not found", field = "id"))
            )
        }
    )
private val settings by lazy {
    VafsCorSettings(
        repoTest = repo
    )
}
private val processor by lazy { VafsRuleProcessor(settings) }

@OptIn(ExperimentalCoroutinesApi::class)
fun repoNotFoundTest(command: VafsCommand) = runTest {
    val ctx = VafsContext(
        command = command,
        state = VafsState.NONE,
        workMode = VafsWorkMode.TEST,
        ruleRequest = VafsRule(
            id = VafsRuleId("10000000-0000-0000-0000-000000000002"),
            description = "rule description",
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
        ),
    )
    processor.exec(ctx)
    assertEquals(VafsState.FAILING, ctx.state)
    assertEquals(VafsRule(), ctx.ruleResponse)
    assertEquals(1, ctx.errors.size)
    assertEquals("id", ctx.errors.first().field)
}
