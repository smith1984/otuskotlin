package ru.beeline.vafs.ktor

import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.slf4j.event.Level
import ru.beeline.vafs.api.v1.apiV1Mapper
import ru.beeline.vafs.ktor.plugins.initAppSettings
import ru.beeline.vafs.ktor.v1.v1Rule
import ru.beeline.vafs.ktor.v1.wsHandlerV1
import ru.beeline.vafs.logging.logback.LogWrapperLogback
import java.time.Duration

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

private val clazz = Application::module::class.qualifiedName ?: "Application"
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

        route("v1") {
            v1Rule(appSettings)
        }

        webSocket("/v1/ws") {
            wsHandlerV1()
        }
    }
}
