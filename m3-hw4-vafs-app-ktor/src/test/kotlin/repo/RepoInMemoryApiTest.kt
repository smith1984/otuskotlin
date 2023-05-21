package ru.beeline.vafs.ktor.repo

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.junit.Test
import ru.beeline.api.v1.models.*
import ru.beeline.vafs.common.VafsCorSettings
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.ktor.VafsAppSettings
import ru.beeline.vafs.ktor.module
import ru.beeline.vafs.repo.inmemory.RuleRepoInMemory
import ru.beeline.vafs.stub.VafsRuleStub
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class RepoInMemoryApiTest {
    private val uuidOld = "10000000-0000-0000-0000-000000000001"
    private val uuidNew = "10000000-0000-0000-0000-000000000002"
    private val initRule = VafsRuleStub.prepareResult {
        id = VafsRuleId(uuidOld)
        description = "rule description"
        priority = 999
        listForNumberA = listOf("79995551112")
        typeOperationA = VafsTypeOperationList.INCLUDE
        listForNumberB = listOf("79995551115", "79995551117")
        typeOperationB = VafsTypeOperationList.INCLUDE
        typeOperationCount = VafsTypeOperationCount.EQUAL
        targetCount = 3001
        valueIsTrue = true
        typeOperationAB = VafsTypeOperationBool.XOR
        typeOperationABCount = VafsTypeOperationBool.OR
        lock = VafsRuleLock(uuidOld)
    }

    @Test
    fun create() = testApplication {
        val repo = RuleRepoInMemory(initObjects = listOf(initRule), randomUuid = { uuidNew })

        environment {
            config = ApplicationConfig("application_without_module.yaml")
        }

        application {
            module(VafsAppSettings(corSettings = VafsCorSettings(repoTest = repo)))
        }

        val client = myClient()

        val createRule = RuleCreateObject(
            description = "rule description",
            priority = 999,
            listForNumberA = listOf("79995551112"),
            typeOperationA = TypeOperation.INCLUDE,
            listForNumberB = listOf("79995551115", "79995551117"),
            typeOperationB = TypeOperation.INCLUDE,
            typeOperationCount = TypeOperationCount.EQUAL,
            targetCount = 3001,
            valueIsTrue = true,
            typeOperationAB = TypeOperationBool.XOR,
            typeOperationABCount = TypeOperationBool.OR,
        )

        val response = client.post("/v1/rule/create") {
            val requestObj = RuleCreateRequest(
                requestId = "12345",
                rule = createRule,
                debug = RuleDebug(
                    mode = RuleRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<RuleCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(uuidNew, responseObj.rule?.id)
        assertEquals(createRule.description, responseObj.rule?.description)
        assertEquals(createRule.priority, responseObj.rule?.priority)
        assertEquals(createRule.listForNumberA, responseObj.rule?.listForNumberA)
        assertEquals(createRule.listForNumberB, responseObj.rule?.listForNumberB)
        assertEquals(createRule.typeOperationA, responseObj.rule?.typeOperationA)
        assertEquals(createRule.typeOperationB, responseObj.rule?.typeOperationB)
        assertEquals(createRule.typeOperationCount, responseObj.rule?.typeOperationCount)
        assertEquals(createRule.typeOperationAB, responseObj.rule?.typeOperationAB)
        assertEquals(createRule.typeOperationABCount, responseObj.rule?.typeOperationABCount)
        assertEquals(createRule.targetCount, responseObj.rule?.targetCount)
        assertEquals(createRule.valueIsTrue, responseObj.rule?.valueIsTrue)
    }

    @Test
    fun read() = testApplication {
        val repo = RuleRepoInMemory(initObjects = listOf(initRule), randomUuid = { uuidNew })

        environment {
            config = ApplicationConfig("application_without_module.yaml")
        }

        application {
            module(VafsAppSettings(corSettings = VafsCorSettings(repoTest = repo)))
        }

        val client = myClient()

        val response = client.post("/v1/rule/read") {
            val requestObj = RuleReadRequest(
                requestId = "12345",
                rule = RuleReadObject(uuidOld),
                debug = RuleDebug(
                    mode = RuleRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<RuleReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals(uuidOld, responseObj.rule?.id)
    }

    @Test
    fun update() = testApplication {
        val repo = RuleRepoInMemory(initObjects = listOf(initRule), randomUuid = { uuidNew })

        environment {
            config = ApplicationConfig("application_without_module.yaml")
        }

        application {
            module(VafsAppSettings(corSettings = VafsCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val ruleUpdate = RuleUpdateObject(
            id = uuidOld,
            description = "rule description update",
            priority = 995,
            listForNumberA = listOf("79995551112"),
            typeOperationA = TypeOperation.INCLUDE,
            listForNumberB = listOf("79995551115", "79995551117"),
            typeOperationB = TypeOperation.INCLUDE,
            typeOperationCount = TypeOperationCount.EQUAL,
            targetCount = 3001,
            valueIsTrue = true,
            typeOperationAB = TypeOperationBool.XOR,
            typeOperationABCount = TypeOperationBool.OR,
            lock = initRule.lock.asString(),
        )

        val response = client.post("/v1/rule/update") {
            val requestObj = RuleUpdateRequest(
                requestId = "12345",
                rule = ruleUpdate,
                debug = RuleDebug(
                    mode = RuleRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<RuleUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(ruleUpdate.id, responseObj.rule?.id)
        assertEquals(ruleUpdate.description, responseObj.rule?.description)
        assertEquals(ruleUpdate.priority, responseObj.rule?.priority)
        assertEquals(ruleUpdate.listForNumberA, responseObj.rule?.listForNumberA)
        assertEquals(ruleUpdate.listForNumberB, responseObj.rule?.listForNumberB)
        assertEquals(ruleUpdate.typeOperationA, responseObj.rule?.typeOperationA)
        assertEquals(ruleUpdate.typeOperationB, responseObj.rule?.typeOperationB)
        assertEquals(ruleUpdate.typeOperationCount, responseObj.rule?.typeOperationCount)
        assertEquals(ruleUpdate.typeOperationAB, responseObj.rule?.typeOperationAB)
        assertEquals(ruleUpdate.typeOperationABCount, responseObj.rule?.typeOperationABCount)
        assertEquals(ruleUpdate.targetCount, responseObj.rule?.targetCount)
        assertEquals(ruleUpdate.valueIsTrue, responseObj.rule?.valueIsTrue)
    }

    @Test
    fun delete() = testApplication {
        val repo = RuleRepoInMemory(initObjects = listOf(initRule), randomUuid = { uuidNew })

        environment {
            config = ApplicationConfig("application_without_module.yaml")
        }

        application {
            module(VafsAppSettings(corSettings = VafsCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val response = client.post("/v1/rule/delete") {
            val requestObj = RuleDeleteRequest(
                requestId = "12345",
                rule = RuleDeleteObject(
                    id = uuidOld,
                    lock = initRule.lock.asString(),
                ),
                debug = RuleDebug(
                    mode = RuleRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<RuleDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals(uuidOld, responseObj.rule?.id)
    }

    @Test
    fun search() = testApplication {
        val repo = RuleRepoInMemory(initObjects = listOf(initRule), randomUuid = { uuidNew })

        environment {
            config = ApplicationConfig("application_without_module.yaml")
        }

        application {
            module(VafsAppSettings(corSettings = VafsCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val response = client.post("/v1/rule/search") {
            val requestObj = RuleSearchRequest(
                requestId = "12345",
                ruleFilter = RuleSearchFilter(),
                debug = RuleDebug(
                    mode = RuleRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<RuleSearchResponse>()
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.rules?.size)
        assertEquals(uuidOld, responseObj.rules?.first()?.id)
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
