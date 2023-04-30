package ru.beeline.vafs.biz

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.common.stubs.VafsStubs
import ru.beeline.vafs.stub.VafsRuleStub
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class RuleReadStubTest {

    private val processor = VafsRuleProcessor()
    private val id = VafsRuleId("123987")

    @Test
    fun read() = runTest {

        val ctx = VafsContext(
            command = VafsCommand.READ,
            state = VafsState.NONE,
            workMode = VafsWorkMode.STUB,
            stubCase = VafsStubs.SUCCESS,
            ruleRequest = VafsRule(
                id = id,
            ),
        )
        processor.exec(ctx)
        with (VafsRuleStub.get()) {
            assertEquals(id, ctx.ruleResponse.id)
            assertEquals(description, ctx.ruleResponse.description)
            assertEquals(priority, ctx.ruleResponse.priority)
            assertEquals(listForNumberA, ctx.ruleResponse.listForNumberA)
            assertEquals(typeOperationA, ctx.ruleResponse.typeOperationA)
            assertEquals(listForNumberB, ctx.ruleResponse.listForNumberB)
            assertEquals(typeOperationB, ctx.ruleResponse.typeOperationB)
            assertEquals(typeOperationCount, ctx.ruleResponse.typeOperationCount)
            assertEquals(targetCount, ctx.ruleResponse.targetCount)
            assertEquals(valueIsTrue, ctx.ruleResponse.valueIsTrue)
            assertEquals(typeOperationAB, ctx.ruleResponse.typeOperationAB)
            assertEquals(typeOperationABCount, ctx.ruleResponse.typeOperationABCount)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = VafsContext(
            command = VafsCommand.READ,
            state = VafsState.NONE,
            workMode = VafsWorkMode.STUB,
            stubCase = VafsStubs.BAD_ID,
            ruleRequest = VafsRule(),
        )
        processor.exec(ctx)
        assertEquals(VafsRule(), ctx.ruleResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = VafsContext(
            command = VafsCommand.READ,
            state = VafsState.NONE,
            workMode = VafsWorkMode.STUB,
            stubCase = VafsStubs.DB_ERROR,
            ruleRequest = VafsRule(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(VafsRule(), ctx.ruleResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = VafsContext(
            command = VafsCommand.READ,
            state = VafsState.NONE,
            workMode = VafsWorkMode.STUB,
            stubCase = VafsStubs.BAD_DESCRIPTION,
            ruleRequest = VafsRule(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(VafsRule(), ctx.ruleResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
