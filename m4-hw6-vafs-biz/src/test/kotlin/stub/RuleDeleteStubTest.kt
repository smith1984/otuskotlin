package ru.beeline.vafs.biz.stub

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.beeline.vafs.biz.VafsRuleProcessor
import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.common.stubs.VafsStubs
import ru.beeline.vafs.stub.VafsRuleStub
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class RuleDeleteStubTest {

    private val processor = VafsRuleProcessor()
    private val id = VafsRuleId("123987")

    @Test
    fun delete() = runTest {

        val ctx = VafsContext(
            command = VafsCommand.DELETE,
            state = VafsState.NONE,
            workMode = VafsWorkMode.STUB,
            stubCase = VafsStubs.SUCCESS,
            ruleRequest = VafsRule(
                id = id,
            ),
        )
        processor.exec(ctx)

        val stub = VafsRuleStub.get()

        assertEquals(stub.id, ctx.ruleResponse.id)
        assertEquals(stub.description, ctx.ruleResponse.description)
        assertEquals(stub.priority, ctx.ruleResponse.priority)
        assertEquals(stub.listForNumberA, ctx.ruleResponse.listForNumberA)
        assertEquals(stub.typeOperationA, ctx.ruleResponse.typeOperationA)
        assertEquals(stub.listForNumberB, ctx.ruleResponse.listForNumberB)
        assertEquals(stub.typeOperationB, ctx.ruleResponse.typeOperationB)
        assertEquals(stub.typeOperationCount, ctx.ruleResponse.typeOperationCount)
        assertEquals(stub.targetCount, ctx.ruleResponse.targetCount)
        assertEquals(stub.valueIsTrue, ctx.ruleResponse.valueIsTrue)
        assertEquals(stub.typeOperationAB, ctx.ruleResponse.typeOperationAB)
        assertEquals(stub.typeOperationABCount, ctx.ruleResponse.typeOperationABCount)
    }

    @Test
    fun badId() = runTest {
        val ctx = VafsContext(
            command = VafsCommand.DELETE,
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
            command = VafsCommand.DELETE,
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
            command = VafsCommand.DELETE,
            state = VafsState.NONE,
            workMode = VafsWorkMode.STUB,
            stubCase = VafsStubs.BAD_TYPE_OPERATION_B,
            ruleRequest = VafsRule(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(VafsRule(), ctx.ruleResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
