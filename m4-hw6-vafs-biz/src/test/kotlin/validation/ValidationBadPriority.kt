package ru.beeline.vafs.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.beeline.vafs.biz.VafsRuleProcessor
import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.VafsCommand
import ru.beeline.vafs.common.models.VafsRule
import ru.beeline.vafs.common.models.VafsState
import ru.beeline.vafs.common.models.VafsWorkMode
import ru.beeline.vafs.common.permissions.VafsPrincipalModel
import ru.beeline.vafs.common.permissions.VafsUserGroups
import ru.beeline.vafs.stub.VafsRuleStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
fun validationPriorityCorrect(command: VafsCommand, processor: VafsRuleProcessor, stub: VafsRule) = runTest {
    val ctx = VafsContext(
        command = command,
        state = VafsState.NONE,
        workMode = VafsWorkMode.TEST,
        ruleRequest = stub,
        principal = VafsPrincipalModel(
            id = stub.userId,
            groups = setOf(
                VafsUserGroups.USER,
                VafsUserGroups.TEST,
            )
        ),
    )
    processor.exec(ctx)
    
    assertEquals(0, ctx.errors.size)
    assertNotEquals(VafsState.FAILING, ctx.state)
    assertEquals(1000, ctx.ruleValidated.priority)
}


@OptIn(ExperimentalCoroutinesApi::class)
fun validationPriorityEmpty(command: VafsCommand, processor: VafsRuleProcessor, stub: VafsRule) = runTest {
    val ctx = VafsContext(
        command = command,
        state = VafsState.NONE,
        workMode = VafsWorkMode.TEST,
        ruleRequest = stub.copy(
            priority = 0
        ),
        principal = VafsPrincipalModel(
            id = stub.userId,
            groups = setOf(
                VafsUserGroups.USER,
                VafsUserGroups.TEST,
            )
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(VafsState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("priority", error?.field)
    assertContains(error?.message ?: "", "not equal zero")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationPriorityMoreUpperBound(command: VafsCommand, processor: VafsRuleProcessor, stub: VafsRule) = runTest {
    val ctx = VafsContext(
        command = command,
        state = VafsState.NONE,
        workMode = VafsWorkMode.TEST,
        ruleRequest = stub.copy(
            priority = 10000
        ),
        principal = VafsPrincipalModel(
            id = stub.userId,
            groups = setOf(
                VafsUserGroups.USER,
                VafsUserGroups.TEST,
            )
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(VafsState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("priority", error?.field)
    assertContains(error?.message ?: "", "upper bound")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationPriorityLessLowerBound(command: VafsCommand, processor: VafsRuleProcessor, stub: VafsRule) = runTest {
    val ctx = VafsContext(
        command = command,
        state = VafsState.NONE,
        workMode = VafsWorkMode.TEST,
        ruleRequest = stub.copy(
            priority = -100
        ),
        principal = VafsPrincipalModel(
            id = stub.userId,
            groups = setOf(
                VafsUserGroups.USER,
                VafsUserGroups.TEST,
            )
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(VafsState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("priority", error?.field)
    assertContains(error?.message ?: "", "lower bound")
}
