package ru.beeline.vafs.api.v1


import ru.beeline.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseSerializationTest {
    private val response = RuleReadResponse(
        requestId = "123-987",
        rule = RuleResponseObject(
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
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"description\":\\s*\"rule description\""))
        assertContains(json, Regex("\"responseType\":\\s*\"read\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as RuleReadResponse

        assertEquals(response, obj)
    }
}
