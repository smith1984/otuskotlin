package ru.beeline.vafs.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.beeline.vafs.biz.VafsRuleProcessor
import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.stub.VafsRuleStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = VafsRuleStub.get()

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTypeOperationABCorrect(command: VafsCommand, processor: VafsRuleProcessor) = runTest {
    val ctx = VafsContext(
        command = command,
        state = VafsState.NONE,
        workMode = VafsWorkMode.TEST,
        ruleRequest = stub
    )
    processor.exec(ctx)
    
    assertEquals(0, ctx.errors.size)
    assertNotEquals(VafsState.FAILING, ctx.state)
    assertEquals(VafsTypeOperationBool.AND, ctx.ruleValidated.typeOperationAB)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTypeOperationABEmpty(command: VafsCommand, processor: VafsRuleProcessor) = runTest {
    val ctx = VafsContext(
        command = command,
        state = VafsState.NONE,
        workMode = VafsWorkMode.TEST,
        ruleRequest = stub.copy(
            typeOperationAB = VafsTypeOperationBool.NONE,
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(VafsState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("typeOperationAB", error?.field)
    assertContains(error?.message ?: "", "empty")
}
