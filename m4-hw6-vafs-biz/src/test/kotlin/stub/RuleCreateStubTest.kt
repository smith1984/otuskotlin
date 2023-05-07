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
class RuleCreateStubTest {

    private val processor = VafsRuleProcessor()

    private val id = VafsRuleId("123987")
    private val description = "rule description"
    private val priority = 1000
    private val listForNumberA = listOf("79995551111")
    private val typeOperationA = VafsTypeOperationList.EXCLUDE
    private val listForNumberB = listOf("79995551113", "79995551112")
    private val typeOperationB = VafsTypeOperationList.EXCLUDE
    private val typeOperationCount = VafsTypeOperationCount.LESS
    private val targetCount = 3000
    private val valueIsTrue = false
    private val typeOperationAB = VafsTypeOperationBool.AND
    private val typeOperationABCount = VafsTypeOperationBool.AND

    @Test
    fun create() = runTest {

        val ctx = VafsContext(
            command = VafsCommand.CREATE,
            state = VafsState.NONE,
            workMode = VafsWorkMode.STUB,
            stubCase = VafsStubs.SUCCESS,
            ruleRequest = VafsRule(
                id = id,
                description = description,
                priority = priority,
                listForNumberA = listForNumberA,
                typeOperationA = typeOperationA,
                listForNumberB = listForNumberB,
                typeOperationB = typeOperationB,
                typeOperationCount = typeOperationCount,
                targetCount = targetCount,
                valueIsTrue = valueIsTrue,
                typeOperationAB = typeOperationAB,
                typeOperationABCount = typeOperationABCount,
            ),
        )
        processor.exec(ctx)
        assertEquals(VafsRuleStub.get().id, ctx.ruleResponse.id)
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

    @Test
    fun badDescription() = runTest {
        val ctx = VafsContext(
            command = VafsCommand.CREATE,
            state = VafsState.NONE,
            workMode = VafsWorkMode.STUB,
            stubCase = VafsStubs.BAD_DESCRIPTION,
            ruleRequest = VafsRule(
                id = id,
                description = description,
                priority = priority,
                listForNumberA = listForNumberA,
                typeOperationA = typeOperationA,
                listForNumberB = listForNumberB,
                typeOperationB = typeOperationB,
                typeOperationCount = typeOperationCount,
                targetCount = targetCount,
                valueIsTrue = valueIsTrue,
                typeOperationAB = typeOperationAB,
                typeOperationABCount = typeOperationABCount,
            ),
        )
        processor.exec(ctx)
        assertEquals(VafsRule(), ctx.ruleResponse)
        assertEquals("description", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badPriority() = runTest {
        val ctx = VafsContext(
            command = VafsCommand.CREATE,
            state = VafsState.NONE,
            workMode = VafsWorkMode.STUB,
            stubCase = VafsStubs.BAD_PRIORITY,
            ruleRequest = VafsRule(
                id = id,
                description = description,
                priority = priority,
                listForNumberA = listForNumberA,
                typeOperationA = typeOperationA,
                listForNumberB = listForNumberB,
                typeOperationB = typeOperationB,
                typeOperationCount = typeOperationCount,
                targetCount = targetCount,
                valueIsTrue = valueIsTrue,
                typeOperationAB = typeOperationAB,
                typeOperationABCount = typeOperationABCount,
            ),
        )
        processor.exec(ctx)
        assertEquals(VafsRule(), ctx.ruleResponse)
        assertEquals("priority", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badTypeOperationABCount() = runTest {
        val ctx = VafsContext(
            command = VafsCommand.CREATE,
            state = VafsState.NONE,
            workMode = VafsWorkMode.STUB,
            stubCase = VafsStubs.BAD_TYPE_OPERATION_AB_COUNT,
            ruleRequest = VafsRule(
                id = id,
                description = description,
                priority = priority,
                listForNumberA = listForNumberA,
                typeOperationA = typeOperationA,
                listForNumberB = listForNumberB,
                typeOperationB = typeOperationB,
                typeOperationCount = typeOperationCount,
                targetCount = targetCount,
                valueIsTrue = valueIsTrue,
                typeOperationAB = typeOperationAB,
                typeOperationABCount = typeOperationABCount,
            ),
        )
        processor.exec(ctx)
        assertEquals(VafsRule(), ctx.ruleResponse)
        assertEquals("typeOperationABCount", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badTypeOperationAB() = runTest {
        val ctx = VafsContext(
            command = VafsCommand.CREATE,
            state = VafsState.NONE,
            workMode = VafsWorkMode.STUB,
            stubCase = VafsStubs.BAD_TYPE_OPERATION_AB,
            ruleRequest = VafsRule(
                id = id,
                description = description,
                priority = priority,
                listForNumberA = listForNumberA,
                typeOperationA = typeOperationA,
                listForNumberB = listForNumberB,
                typeOperationB = typeOperationB,
                typeOperationCount = typeOperationCount,
                targetCount = targetCount,
                valueIsTrue = valueIsTrue,
                typeOperationAB = typeOperationAB,
                typeOperationABCount = typeOperationABCount,
            ),
        )
        processor.exec(ctx)
        assertEquals(VafsRule(), ctx.ruleResponse)
        assertEquals("typeOperationAB", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badValueIsTrue() = runTest {
        val ctx = VafsContext(
            command = VafsCommand.CREATE,
            state = VafsState.NONE,
            workMode = VafsWorkMode.STUB,
            stubCase = VafsStubs.BAD_VALUE_IS_TRUE,
            ruleRequest = VafsRule(
                id = id,
                description = description,
                priority = priority,
                listForNumberA = listForNumberA,
                typeOperationA = typeOperationA,
                listForNumberB = listForNumberB,
                typeOperationB = typeOperationB,
                typeOperationCount = typeOperationCount,
                targetCount = targetCount,
                valueIsTrue = valueIsTrue,
                typeOperationAB = typeOperationAB,
                typeOperationABCount = typeOperationABCount,
            ),
        )
        processor.exec(ctx)
        assertEquals(VafsRule(), ctx.ruleResponse)
        assertEquals("valueIsTrue", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badTargetCount() = runTest {
        val ctx = VafsContext(
            command = VafsCommand.CREATE,
            state = VafsState.NONE,
            workMode = VafsWorkMode.STUB,
            stubCase = VafsStubs.BAD_TARGET_COUNT,
            ruleRequest = VafsRule(
                id = id,
                description = description,
                priority = priority,
                listForNumberA = listForNumberA,
                typeOperationA = typeOperationA,
                listForNumberB = listForNumberB,
                typeOperationB = typeOperationB,
                typeOperationCount = typeOperationCount,
                targetCount = targetCount,
                valueIsTrue = valueIsTrue,
                typeOperationAB = typeOperationAB,
                typeOperationABCount = typeOperationABCount,
            ),
        )
        processor.exec(ctx)
        assertEquals(VafsRule(), ctx.ruleResponse)
        assertEquals("targetCount", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badTypeOperationCount() = runTest {
        val ctx = VafsContext(
            command = VafsCommand.CREATE,
            state = VafsState.NONE,
            workMode = VafsWorkMode.STUB,
            stubCase = VafsStubs.BAD_TYPE_OPERATION_COUNT,
            ruleRequest = VafsRule(
                id = id,
                description = description,
                priority = priority,
                listForNumberA = listForNumberA,
                typeOperationA = typeOperationA,
                listForNumberB = listForNumberB,
                typeOperationB = typeOperationB,
                typeOperationCount = typeOperationCount,
                targetCount = targetCount,
                valueIsTrue = valueIsTrue,
                typeOperationAB = typeOperationAB,
                typeOperationABCount = typeOperationABCount,
            ),
        )
        processor.exec(ctx)
        assertEquals(VafsRule(), ctx.ruleResponse)
        assertEquals("typeOperationCount", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badTypeOperationB() = runTest {
        val ctx = VafsContext(
            command = VafsCommand.CREATE,
            state = VafsState.NONE,
            workMode = VafsWorkMode.STUB,
            stubCase = VafsStubs.BAD_TYPE_OPERATION_B,
            ruleRequest = VafsRule(
                id = id,
                description = description,
                priority = priority,
                listForNumberA = listForNumberA,
                typeOperationA = typeOperationA,
                listForNumberB = listForNumberB,
                typeOperationB = typeOperationB,
                typeOperationCount = typeOperationCount,
                targetCount = targetCount,
                valueIsTrue = valueIsTrue,
                typeOperationAB = typeOperationAB,
                typeOperationABCount = typeOperationABCount,
            ),
        )
        processor.exec(ctx)
        
        assertEquals(VafsRule(), ctx.ruleResponse)
        assertEquals("typeOperationB", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badTypeOperationA() = runTest {
        val ctx = VafsContext(
            command = VafsCommand.CREATE,
            state = VafsState.NONE,
            workMode = VafsWorkMode.STUB,
            stubCase = VafsStubs.BAD_TYPE_OPERATION_A,
            ruleRequest = VafsRule(
                id = id,
                description = description,
                priority = priority,
                listForNumberA = listForNumberA,
                typeOperationA = typeOperationA,
                listForNumberB = listForNumberB,
                typeOperationB = typeOperationB,
                typeOperationCount = typeOperationCount,
                targetCount = targetCount,
                valueIsTrue = valueIsTrue,
                typeOperationAB = typeOperationAB,
                typeOperationABCount = typeOperationABCount,
            ),
        )
        processor.exec(ctx)
        assertEquals(VafsRule(), ctx.ruleResponse)
        assertEquals("typeOperationA", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badListForNumberB() = runTest {
        val ctx = VafsContext(
            command = VafsCommand.CREATE,
            state = VafsState.NONE,
            workMode = VafsWorkMode.STUB,
            stubCase = VafsStubs.BAD_LIST_FOR_NUMBER_B,
            ruleRequest = VafsRule(
                id = id,
                description = description,
                priority = priority,
                listForNumberA = listForNumberA,
                typeOperationA = typeOperationA,
                listForNumberB = listForNumberB,
                typeOperationB = typeOperationB,
                typeOperationCount = typeOperationCount,
                targetCount = targetCount,
                valueIsTrue = valueIsTrue,
                typeOperationAB = typeOperationAB,
                typeOperationABCount = typeOperationABCount,
            ),
        )
        processor.exec(ctx)
        assertEquals(VafsRule(), ctx.ruleResponse)
        assertEquals("listForNumberB", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badListForNumberA() = runTest {
        val ctx = VafsContext(
            command = VafsCommand.CREATE,
            state = VafsState.NONE,
            workMode = VafsWorkMode.STUB,
            stubCase = VafsStubs.BAD_LIST_FOR_NUMBER_A,
            ruleRequest = VafsRule(
                id = id,
                description = description,
                priority = priority,
                listForNumberA = listForNumberA,
                typeOperationA = typeOperationA,
                listForNumberB = listForNumberB,
                typeOperationB = typeOperationB,
                typeOperationCount = typeOperationCount,
                targetCount = targetCount,
                valueIsTrue = valueIsTrue,
                typeOperationAB = typeOperationAB,
                typeOperationABCount = typeOperationABCount,
            ),
        )
        processor.exec(ctx)
        assertEquals(VafsRule(), ctx.ruleResponse)
        assertEquals("listForNumberA", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }


    @Test
    fun databaseError() = runTest {
        val ctx = VafsContext(
            command = VafsCommand.CREATE,
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
            command = VafsCommand.CREATE,
            state = VafsState.NONE,
            workMode = VafsWorkMode.STUB,
            stubCase = VafsStubs.BAD_ID,
            ruleRequest = VafsRule(
                id = id,
                description = description,
                priority = priority,
                listForNumberA = listForNumberA,
                typeOperationA = typeOperationA,
                listForNumberB = listForNumberB,
                typeOperationB = typeOperationB,
                typeOperationCount = typeOperationCount,
                targetCount = targetCount,
                valueIsTrue = valueIsTrue,
                typeOperationAB = typeOperationAB,
                typeOperationABCount = typeOperationABCount,
            ),
        )
        processor.exec(ctx)
        assertEquals(VafsRule(), ctx.ruleResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
