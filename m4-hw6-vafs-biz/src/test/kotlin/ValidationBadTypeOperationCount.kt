package ru.beeline.vafs.biz

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.stub.VafsRuleStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = VafsRuleStub.get()

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTypeOperationCountCorrect(command: VafsCommand, processor: VafsRuleProcessor) = runTest {
    val ctx = VafsContext(
        command = command,
        state = VafsState.NONE,
        workMode = VafsWorkMode.TEST,
        ruleRequest = stub
    )
    processor.exec(ctx)
    println(ctx.errors)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(VafsState.FAILING, ctx.state)
    assertEquals(VafsTypeOperationCount.LESS, ctx.ruleValidated.typeOperationCount)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTypeOperationCountEmpty(command: VafsCommand, processor: VafsRuleProcessor) = runTest {
    val ctx = VafsContext(
        command = command,
        state = VafsState.NONE,
        workMode = VafsWorkMode.TEST,
        ruleRequest = stub.copy(
            typeOperationCount = VafsTypeOperationCount.NONE,
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(VafsState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("typeOperationCount", error?.field)
    assertContains(error?.message ?: "", "empty")
}
