package ru.beeline.vafs.ktor.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.v1Rule() {
    route("ad") {
        post("create") {
            call.createRule()
        }
        post("read") {
            call.readRule()
        }
        post("update") {
            call.updateRule()
        }
        post("delete") {
            call.deleteRule()
        }
        post("search") {
            call.searchRule()
        }
    }
}

