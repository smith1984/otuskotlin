package ru.beeline.vafs.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.beeline.vafs.biz.VafsRuleProcessor
import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.VafsCommand
import ru.beeline.vafs.common.models.VafsState
import ru.beeline.vafs.common.models.VafsWorkMode
import ru.beeline.vafs.stub.VafsRuleStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = VafsRuleStub.get()

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTargetCountCorrect(command: VafsCommand, processor: VafsRuleProcessor) = runTest {
    val ctx = VafsContext(
        command = command,
        state = VafsState.NONE,
        workMode = VafsWorkMode.TEST,
        ruleRequest = stub
    )
    processor.exec(ctx)
    
    assertEquals(0, ctx.errors.size)
    assertNotEquals(VafsState.FAILING, ctx.state)
    assertEquals(3000, ctx.ruleValidated.targetCount)
}


@OptIn(ExperimentalCoroutinesApi::class)
fun validationTargetCountEmpty(command: VafsCommand, processor: VafsRuleProcessor) = runTest {
    val ctx = VafsContext(
        command = command,
        state = VafsState.NONE,
        workMode = VafsWorkMode.TEST,
        ruleRequest = stub.copy(
            targetCount = 0
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(VafsState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("targetCount", error?.field)
    assertContains(error?.message ?: "", "not equal zero")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTargetCountLessLowerBound(command: VafsCommand, processor: VafsRuleProcessor) = runTest {
    val ctx = VafsContext(
        command = command,
        state = VafsState.NONE,
        workMode = VafsWorkMode.TEST,
        ruleRequest = stub.copy(
            targetCount = -1
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(VafsState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("targetCount", error?.field)
    assertContains(error?.message ?: "", "lower bound")
}
