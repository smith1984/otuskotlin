package ru.beeline.vafs.ktor.stubs

import io.ktor.client.plugins.websocket.*
import io.ktor.server.testing.*
import io.ktor.websocket.*
import kotlinx.coroutines.withTimeout
import ru.beeline.api.v1.models.*
import ru.beeline.vafs.api.v1.apiV1Mapper
import ru.beeline.vafs.ktor.module
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class RuleStubWebsocketTest {

    @Test
    fun createStub() {
        val request = RuleCreateRequest(
            requestId = "1234-9876",
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

        testMethod<IResponse>(request) {
            assertEquals("1234-9876", it.requestId)
        }
    }

        @Test
        fun readStub() {
            val request = RuleReadRequest(
                requestId = "1234-9876",
                rule = RuleReadObject("12349876"),
                debug = RuleDebug(
                    mode = RuleRequestDebugMode.STUB,
                    stub = RuleRequestDebugStubs.SUCCESS
                )
            )

            testMethod<IResponse>(request) {
                assertEquals("1234-9876", it.requestId)
            }
        }

        @Test
        fun updateStub() {
            val request = RuleUpdateRequest(
                requestId = "1234-9876",
                rule = RuleUpdateObject(
                    id = "",
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

            testMethod<IResponse>(request) {
                assertEquals("1234-9876", it.requestId)
            }
        }

        @Test
        fun deleteStub() {
            val request = RuleDeleteRequest(
                requestId = "1234-9876",
                rule = RuleDeleteObject(
                    id = "12349876",
                ),
                debug = RuleDebug(
                    mode = RuleRequestDebugMode.STUB,
                    stub = RuleRequestDebugStubs.SUCCESS
                )
            )

            testMethod<IResponse>(request) {
                assertEquals("1234-9876", it.requestId)
            }
        }

        @Test
        fun searchStub() {
            val request = RuleSearchRequest(
                requestId = "1234-9876",
                ruleFilter = RuleSearchFilter(),
                debug = RuleDebug(
                    mode = RuleRequestDebugMode.STUB,
                    stub = RuleRequestDebugStubs.SUCCESS
                )
            )

            testMethod<IResponse>(request) {
                assertEquals("1234-9876", it.requestId)
            }
        }
    
    private inline fun <reified T> testMethod(
        request: Any,
        crossinline assertBlock: (T) -> Unit,
    ) = testApplication {

        val client = createClient {
            install(WebSockets)
        }

        client.webSocket("/v1/ws") {
            withTimeout(3000) {
                val incame = incoming.receive() as Frame.Text
                val response = apiV1Mapper.readValue(incame.readText(), T::class.java)
                assertIs<RuleInitResponse>(response)
            }
            send(Frame.Text(apiV1Mapper.writeValueAsString(request)))
            withTimeout(3000) {
                val incame = incoming.receive() as Frame.Text
                val response = apiV1Mapper.readValue(incame.readText(), T::class.java)

                assertBlock(response)
            }
        }
    }
}
