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
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
fun validationListNumberACorrect(command: VafsCommand, processor: VafsRuleProcessor, stub: VafsRule) = runTest {
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
    assertEquals(listOf("79995551111"), ctx.ruleValidated.listForNumberA)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationListNumberATrim(command: VafsCommand, processor: VafsRuleProcessor, stub: VafsRule) = runTest {
    val ctx = VafsContext(
        command = command,
        state = VafsState.NONE,
        workMode = VafsWorkMode.TEST,
        ruleRequest = stub.copy(
            listForNumberA = listOf("\n\t   79995551111\t\n")
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
    assertEquals(0, ctx.errors.size)
    assertNotEquals(VafsState.FAILING, ctx.state)
    assertEquals(listOf("79995551111"), ctx.ruleValidated.listForNumberA)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationListNumberAEmpty(command: VafsCommand, processor: VafsRuleProcessor, stub: VafsRule) = runTest {
    val ctx = VafsContext(
        command = command,
        state = VafsState.NONE,
        workMode = VafsWorkMode.TEST,
        ruleRequest = stub.copy(
            listForNumberA = listOf()
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
    assertEquals("listForNumberA", error?.field)
    assertContains(error?.message ?: "", "empty list")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationListNumberASymbols(command: VafsCommand, processor: VafsRuleProcessor, stub: VafsRule) = runTest {
    val ctx = VafsContext(
        command = command,
        state = VafsState.NONE,
        workMode = VafsWorkMode.TEST,
        ruleRequest = stub.copy(
            listForNumberA = listOf("79995551111", "+859", "asdf", "@!$")
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
    assertEquals("listForNumberA", error?.field)
    assertContains(error?.message ?: "", "asdf")
    assertContains(error?.message ?: "", "@!\$")
    assertFalse(error?.message?.contains("79995551111") ?: true)
    assertFalse(error?.message?.contains("+859") ?: true)
}
