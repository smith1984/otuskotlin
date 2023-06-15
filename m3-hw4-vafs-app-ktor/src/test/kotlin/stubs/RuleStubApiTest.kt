package ru.beeline.vafs.ktor.stubs

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.call.*

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.junit.Test
import ru.beeline.api.v1.models.*
import ru.beeline.vafs.common.VafsCorSettings
import ru.beeline.vafs.ktor.VafsAppSettings
import ru.beeline.vafs.ktor.config.KtorAuthConfig
import ru.beeline.vafs.ktor.module
import ru.otus.otuskotlin.marketplace.app.ru.otus.otuskotlin.marketplace.auth.addAuth
import kotlin.test.assertEquals

class V1RuleStubApiTest {
    @Test
    fun create() = testApplication {

        environment {
            config = ApplicationConfig("application_without_module.yaml")
        }

        application {
            module(VafsAppSettings(corSettings = VafsCorSettings(), auth = KtorAuthConfig.TEST))
        }

        val client = myClient()

        val response = client.post("/v1/rule/create") {
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
            addAuth(config = KtorAuthConfig.TEST)
            setBody(requestObj)
        }
        val responseObj = response.body<RuleCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("123987", responseObj.rule?.id)
    }

    @Test
    fun read() = testApplication {

        environment {
            config = ApplicationConfig("application_without_module.yaml")
        }

        application {
            module(VafsAppSettings(corSettings = VafsCorSettings(), auth = KtorAuthConfig.TEST))
        }

        val client = myClient()

        val response = client.post("/v1/rule/read") {
            val requestObj = RuleReadRequest(
                requestId = "12345-987654",
                rule = RuleReadObject("123987"),
                debug = RuleDebug(
                    mode = RuleRequestDebugMode.STUB,
                    stub = RuleRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            addAuth(config = KtorAuthConfig.TEST)
            setBody(requestObj)
        }
        val responseObj = response.body<RuleReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals("123987", responseObj.rule?.id)
    }

    @Test
    fun update() = testApplication {

        environment {
            config = ApplicationConfig("application_without_module.yaml")
        }

        application {
            module(VafsAppSettings(corSettings = VafsCorSettings(), auth = KtorAuthConfig.TEST))
        }

        val client = myClient()

        val response = client.post("/v1/rule/update") {
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
            addAuth(config = KtorAuthConfig.TEST)
            setBody(requestObj)
        }
        val responseObj = response.body<RuleUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("123987", responseObj.rule?.id)
    }

    @Test
    fun delete() = testApplication {

        environment {
            config = ApplicationConfig("application_without_module.yaml")
        }

        application {
            module(VafsAppSettings(corSettings = VafsCorSettings(), auth = KtorAuthConfig.TEST))
        }

        val client = myClient()

        val response = client.post("/v1/rule/delete") {
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
            addAuth(config = KtorAuthConfig.TEST)
            setBody(requestObj)
        }
        val responseObj = response.body<RuleDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals("123987", responseObj.rule?.id)
    }

    @Test
    fun search() = testApplication {

        environment {
            config = ApplicationConfig("application_without_module.yaml")
        }

        application {
            module(VafsAppSettings(corSettings = VafsCorSettings(), auth = KtorAuthConfig.TEST))
        }

        val client = myClient()

        val response = client.post("/v1/rule/search") {
            val requestObj = RuleSearchRequest(
                requestId = "12345-987654",
                ruleFilter = RuleSearchFilter(),
                debug = RuleDebug(
                    mode = RuleRequestDebugMode.STUB,
                    stub = RuleRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            addAuth(config = KtorAuthConfig.TEST)
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
