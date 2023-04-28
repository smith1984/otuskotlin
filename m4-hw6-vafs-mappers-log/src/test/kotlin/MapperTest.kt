package ru.beeline.api.logs.mapper

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperTest {

    @Test
    fun fromContext() {
        val context = VafsContext(
            requestId = VafsRequestId("1234-9876"),
            command = VafsCommand.READ,
            ruleResponse = VafsRule(
                description = "rule description",
                priority = 1000,
                listForNumberA = listOf("79995551111"),
                typeOperationA = VafsTypeOperationList.EXCLUDE,
                listForNumberB = listOf("79995551113", "79995551112"),
                typeOperationB = VafsTypeOperationList.INCLUDE,
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

        val log = context.toLog("test-id")

        assertEquals("test-id", log.logId)
        assertEquals("vafs", log.source)
        assertEquals("1234-9876", log.rule?.requestId)
        val error = log.errors?.firstOrNull()
        assertEquals("wrong description", error?.message)
        assertEquals("ERROR", error?.level)
    }
}
