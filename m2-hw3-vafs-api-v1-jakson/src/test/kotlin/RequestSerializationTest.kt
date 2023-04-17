package ru.beeline.vafs.api.v1

import ru.beeline.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestSerializationTest {
    private val request = RuleUpdateRequest(
        requestId = "123-987",
        debug = RuleDebug(
            mode = RuleRequestDebugMode.STUB,
            stub = RuleRequestDebugStubs.BAD_DESCRIPTION
        ),
        rule = RuleUpdateObject(
            description = "rule description",
            priority = 1000,
            listForNumberA = listOf("79995551111"),
            typeOperationA = TypeOperation.EXCLUDE,
            listForNumberB = listOf("79995551113", "79995551112"),
            typeOperationB = TypeOperation.INCLUDE,
            typeOperationCount = TypeOperationCount.EQUAL,
            targetCount = 3000,
            valueIsTrue = false,
            typeOperationAB = TypeOperationBool.AND,
            typeOperationABCount = TypeOperationBool.OR
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"description\":\\s*\"rule description\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badDescription\""))
        assertContains(json, Regex("\"requestType\":\\s*\"update\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as RuleUpdateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"requestId": "123-987"}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, RuleUpdateRequest::class.java)

        assertEquals("123-987", obj.requestId)
    }
}
