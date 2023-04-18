package ru.beeline.vafs.ktor.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.beeline.api.v1.models.*
import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.mappers.*
import ru.beeline.vafs.stub.VafsRuleStub

suspend fun ApplicationCall.createRule() {
    val request = receive<RuleCreateRequest>()
    val context = VafsContext()
    context.fromTransport(request)
    context.ruleResponse = VafsRuleStub.get()
    respond(context.toTransportCreate())
}

suspend fun ApplicationCall.readRule() {
    val request = receive<RuleReadRequest>()
    val context = VafsContext()
    context.fromTransport(request)
    context.ruleResponse = VafsRuleStub.get()
    respond(context.toTransportRead())
}

suspend fun ApplicationCall.updateRule() {
    val request = receive<RuleUpdateRequest>()
    val context = VafsContext()
    context.fromTransport(request)
    context.ruleResponse = VafsRuleStub.get()
    respond(context.toTransportUpdate())
}

suspend fun ApplicationCall.deleteRule() {
    val request = receive<RuleDeleteRequest>()
    val context = VafsContext()
    context.fromTransport(request)
    context.ruleResponse = VafsRuleStub.get()
    respond(context.toTransportDelete())
}

suspend fun ApplicationCall.searchRule() {
    val request = receive<RuleSearchRequest>()
    val context = VafsContext()
    context.fromTransport(request)
    context.rulesResponse.addAll(VafsRuleStub.prepareSearchList("Интервокинг"))
    respond(context.toTransportSearch())
}
