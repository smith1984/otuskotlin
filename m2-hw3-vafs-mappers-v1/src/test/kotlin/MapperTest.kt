package ru.beeline.vafs.mappers

import org.junit.Test
import ru.beeline.api.v1.models.*
import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.common.stubs.VafsStubs
import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun fromTransport() {
        val req = RuleCreateRequest(
            requestId = "123-987",
            debug = RuleDebug(
                mode = RuleRequestDebugMode.STUB,
                stub = RuleRequestDebugStubs.SUCCESS,
            ),
            rule = RuleCreateObject(
                description = "rule description",
                priority = 1000,
                listForNumberA = listOf("79995551111"),
                typeOperationA = TypeOperation.EXCLUDE,
                listForNumberB = listOf("79995551113", "79995551112"),
                typeOperationB = TypeOperation.INCLUDE,
                typeOperationCount = TypeOperationCount.LESS_THAN,
                targetCount = 3000,
                valueIsTrue = false,
                typeOperationAB = TypeOperationBool.AND,
                typeOperationABCount = TypeOperationBool.OR
            ),
        )

        val context = VafsContext()
        context.fromTransport(req)

        assertEquals(VafsStubs.SUCCESS, context.stubCase)
        assertEquals(VafsWorkMode.STUB, context.workMode)
        assertEquals("rule description", context.ruleRequest.description)
        assertEquals(1000, context.ruleRequest.priority)
        assertEquals(listOf("79995551113", "79995551112"), context.ruleRequest.listForNumberB)
        assertEquals(VafsTypeOperationCount.LESS, context.ruleRequest.typeOperationCount)
        assertEquals(VafsTypeOperationBool.AND, context.ruleRequest.typeOperationAB)
        assertEquals(VafsTypeOperationList.EXCLUDE, context.ruleRequest.typeOperationA)

    }

    @Test
    fun toTransport() {
        val context = VafsContext(
            requestId = VafsRequestId("123-987"),
            command = VafsCommand.UPDATE,
            ruleResponse = VafsRule(
                description = "rule description",
                priority = 1000,
                listForNumberA = listOf("79995551111"),
                typeOperationA = VafsTypeOperationList.INCLUDE,
                listForNumberB = listOf("79995551113", "79995551112"),
                typeOperationB = VafsTypeOperationList.EXCLUDE,
                typeOperationCount = VafsTypeOperationCount.LESS,
                targetCount = 3000,
                valueIsTrue = false,
                typeOperationAB = VafsTypeOperationBool.AND,
                typeOperationABCount = VafsTypeOperationBool.OR
            ),
            errors = mutableListOf(
                VafsError(
                    code = "err",
                    group = "request",
                    field = "description",
                    message = "wrong description",
                )
            ),
            state = VafsState.RUNNING,
        )

        val req = context.toTransportRule() as RuleUpdateResponse

        assertEquals("123-987", req.requestId)
        assertEquals("rule description", req.rule?.description)
        assertEquals(3000, req.rule?.targetCount)
        assertEquals(listOf("79995551113", "79995551112"), req.rule?.listForNumberB)
        assertEquals(TypeOperationCount.LESS_THAN, req.rule?.typeOperationCount)
        assertEquals(TypeOperationBool.AND, req.rule?.typeOperationAB)
        assertEquals(TypeOperation.INCLUDE, req.rule?.typeOperationA)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("description", req.errors?.firstOrNull()?.field)
        assertEquals("wrong description", req.errors?.firstOrNull()?.message)
    }
}
