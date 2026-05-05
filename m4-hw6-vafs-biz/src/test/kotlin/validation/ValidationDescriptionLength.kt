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
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private const val MAX_DESCRIPTION_LENGTH = 256

@OptIn(ExperimentalCoroutinesApi::class)
fun validationDescriptionLengthAtUpperBound(command: VafsCommand, processor: VafsRuleProcessor, stub: VafsRule) = runTest {
    val rightAtBound = "a".repeat(MAX_DESCRIPTION_LENGTH)
    val ctx = VafsContext(
        command = command,
        state = VafsState.NONE,
        workMode = VafsWorkMode.TEST,
        ruleRequest = stub.copy(description = rightAtBound),
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
    assertEquals(rightAtBound, ctx.ruleValidated.description)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationDescriptionLengthOverUpperBound(command: VafsCommand, processor: VafsRuleProcessor, stub: VafsRule) = runTest {
    val tooLong = "a".repeat(MAX_DESCRIPTION_LENGTH + 1)
    val ctx = VafsContext(
        command = command,
        state = VafsState.NONE,
        workMode = VafsWorkMode.TEST,
        ruleRequest = stub.copy(description = tooLong),
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
    assertEquals("description", error?.field)
    assertEquals("validation-description-tooLong", error?.code)
    assertContains(error?.message ?: "", "$MAX_DESCRIPTION_LENGTH")
}
