package ru.beeline.vafs.ktor.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.beeline.vafs.ktor.VafsAppSettings

fun Route.v1Rule(appSettings: VafsAppSettings) {

    val loggerRule = appSettings.corSettings.loggerProvider.logger(Route::v1Rule::class)

    route("rule") {
        post("create") {
            call.createRule(appSettings, loggerRule)
        }
        post("read") {
            call.readRule(appSettings, loggerRule)
        }
        post("update") {
            call.updateRule(appSettings, loggerRule)
        }
        post("delete") {
            call.deleteRule(appSettings, loggerRule)
        }
        post("search") {
            call.searchRule(appSettings, loggerRule)
        }
    }
}

