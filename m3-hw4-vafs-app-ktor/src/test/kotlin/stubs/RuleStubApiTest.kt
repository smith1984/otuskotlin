package ru.beeline.vafs.ktor.stubs

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.call.*

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import org.junit.Test
import ru.beeline.api.v1.models.*
import kotlin.test.assertEquals

class V1RuleStubApiTest {
    @Test
    fun create() = testApplication {
        val client = myClient()

        val response = client.post("/v1/ad/create") {
            val requestObj = RuleCreateRequest(
                requestId = "12345-987654",
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
                debug = RuleDebug(
                    mode = RuleRequestDebugMode.STUB,
                    stub = RuleRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<RuleCreateResponse>()
        println(responseObj)
        assertEquals(200, response.status.value)
        assertEquals("123987", responseObj.rule?.id)
    }

    @Test
    fun read() = testApplication {
        val client = myClient()

        val response = client.post("/v1/ad/read") {
            val requestObj = RuleReadRequest(
                requestId = "12345-987654",
                rule = RuleReadObject("123987"),
                debug = RuleDebug(
                    mode = RuleRequestDebugMode.STUB,
                    stub = RuleRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<RuleReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals("123987", responseObj.rule?.id)
    }

    @Test
    fun update() = testApplication {
        val client = myClient()

        val response = client.post("/v1/ad/update") {
            val requestObj = RuleUpdateRequest(
                requestId = "12345-987654",
                rule = RuleUpdateObject(
                    description = "rule description update 2023.04.04",
                    priority = 10,
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
                debug = RuleDebug(
                    mode = RuleRequestDebugMode.STUB,
                    stub = RuleRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<RuleUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("123987", responseObj.rule?.id)
    }

    @Test
    fun delete() = testApplication {
        val client = myClient()

        val response = client.post("/v1/ad/delete") {
            val requestObj = RuleDeleteRequest(
                requestId = "12345-987654",
                rule = RuleDeleteObject(
                    id = "123987",
                ),
                debug = RuleDebug(
                    mode = RuleRequestDebugMode.STUB,
                    stub = RuleRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<RuleDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals("123987", responseObj.rule?.id)
    }

    @Test
    fun search() = testApplication {
        val client = myClient()

        val response = client.post("/v1/ad/search") {
            val requestObj = RuleSearchRequest(
                requestId = "12345-987654",
                adFilter = RuleSearchFilter(),
                debug = RuleDebug(
                    mode = RuleRequestDebugMode.STUB,
                    stub = RuleRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<RuleSearchResponse>()
        assertEquals(200, response.status.value)
        assertEquals("rule-123-01", responseObj.rules?.first()?.id)
    }

    private fun ApplicationTestBuilder.myClient() = createClient {
        install(ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                enable(SerializationFeature.INDENT_OUTPUT)
                writerWithDefaultPrettyPrinter()
            }
        }
    }
}