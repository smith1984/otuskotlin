package ru.beeline.vafs.ktor

import com.auth0.jwt.JWT
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.locations.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.slf4j.event.Level
import ru.beeline.vafs.api.v1.apiV1Mapper
import ru.beeline.vafs.ktor.base.resolveAlgorithm
import ru.beeline.vafs.ktor.config.KtorAuthConfig.Companion.GROUPS_CLAIM
import ru.beeline.vafs.ktor.plugins.initAppSettings
import ru.beeline.vafs.ktor.v1.v1Rule
import ru.beeline.vafs.ktor.v1.wsHandlerV1
import ru.beeline.vafs.logging.logback.LogWrapperLogback


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

private val clazz = Application::module::class.qualifiedName ?: "Application"
@Suppress("unused")
fun Application.module(appSettings: VafsAppSettings = initAppSettings()) {

    install(CallLogging) {
        level = Level.INFO
        val lgr = appSettings
            .corSettings
            .loggerProvider
            .logger(clazz) as? LogWrapperLogback
        lgr?.logger?.also { logger = it }
    }

    install(Routing)

    install(CachingHeaders)
    install(DefaultHeaders)
    install(AutoHeadResponse)

    install(Authentication) {
        jwt("auth-jwt") {
            val authConfig = appSettings.auth
            realm = authConfig.realm

            verifier {
                val algorithm = it.resolveAlgorithm(authConfig)

                JWT
                    .require(algorithm)
                    .withAudience(authConfig.audience)
                    .withIssuer(authConfig.issuer)
                    .build()
            }
            validate { jwtCredential: JWTCredential ->
                when {
                    jwtCredential.payload.getClaim(GROUPS_CLAIM).asList(String::class.java).isNullOrEmpty() -> {
                        this@module.log.error("Groups claim must not be empty in JWT token")
                        null
                    }

                    else -> JWTPrincipal(jwtCredential.payload)
                }
            }
        }
    }

    install(WebSockets)

    install(CORS) {
        allowMethod(HttpMethod.Post)
        anyHost()
    }

    install(ContentNegotiation) {
        jackson {
            setConfig(apiV1Mapper.serializationConfig)
            setConfig(apiV1Mapper.deserializationConfig)
        }
    }

    @Suppress("OPT_IN_USAGE")
    install(Locations)

    routing {
        get("/") {
            call.respondText("Hello, world!")
        }

        openAPI(path="openapi/rule/v1", swaggerFile = "./specs/specs-rule-v1.yaml") {
        }

        swaggerUI(path = "swagger/rule/log", swaggerFile = "./specs/specs-rule-log.yaml")

        route("v1") {
            authenticate("auth-jwt") {
                v1Rule(appSettings)
            }
        }

        webSocket("/v1/ws") {
            wsHandlerV1()
        }
    }
}
