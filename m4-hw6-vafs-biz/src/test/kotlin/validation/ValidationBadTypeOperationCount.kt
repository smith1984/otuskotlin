package ru.beeline.vafs.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.beeline.vafs.biz.VafsRuleProcessor
import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.common.permissions.VafsPrincipalModel
import ru.beeline.vafs.common.permissions.VafsUserGroups
import ru.beeline.vafs.stub.VafsRuleStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTypeOperationCountCorrect(command: VafsCommand, processor: VafsRuleProcessor, stub: VafsRule) = runTest {
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
    assertEquals(VafsTypeOperationCount.LESS, ctx.ruleValidated.typeOperationCount)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTypeOperationCountEmpty(command: VafsCommand, processor: VafsRuleProcessor, stub: VafsRule) = runTest {
    val ctx = VafsContext(
        command = command,
        state = VafsState.NONE,
        workMode = VafsWorkMode.TEST,
        ruleRequest = stub.copy(
            typeOperationCount = VafsTypeOperationCount.NONE,
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
    assertEquals("typeOperationCount", error?.field)
    assertContains(error?.message ?: "", "empty")
}
