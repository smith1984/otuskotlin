package ru.beeline.vafs.ktor.auth

import io.ktor.client.request.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.junit.Test
import ru.beeline.vafs.common.VafsCorSettings
import ru.beeline.vafs.ktor.VafsAppSettings
import ru.beeline.vafs.ktor.config.KtorAuthConfig
import ru.beeline.vafs.ktor.module
import ru.otus.otuskotlin.marketplace.app.ru.otus.otuskotlin.marketplace.auth.addAuth
import kotlin.test.assertEquals

class AuthTest {
    @Test
    fun invalidAudience() = testApplication {
        environment {
            config = ApplicationConfig("application_without_module.yaml")
        }

        application {
            module(VafsAppSettings(corSettings = VafsCorSettings(), auth = KtorAuthConfig.TEST))
        }

        val response = client.post("/v1/rule/create") {
            addAuth(config = KtorAuthConfig.TEST.copy(audience = "invalid"))
        }
        assertEquals(401, response.status.value)
    }
}
