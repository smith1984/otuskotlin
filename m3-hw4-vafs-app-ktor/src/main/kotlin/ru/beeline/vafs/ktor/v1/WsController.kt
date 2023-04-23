package ru.beeline.vafs.ktor.v1

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import ru.beeline.api.v1.models.IRequest
import ru.beeline.vafs.api.v1.apiV1Mapper
import ru.beeline.vafs.common.*
import ru.beeline.vafs.common.helpers.addError
import ru.beeline.vafs.common.helpers.asVafsError
import ru.beeline.vafs.common.helpers.isUpdatableCommand
import ru.beeline.vafs.mappers.*
import ru.beeline.vafs.stub.VafsRuleStub
import java.util.*


val sessions = Collections.synchronizedSet<WebSocketSession>(LinkedHashSet())

suspend fun WebSocketSession.wsHandlerV1() {
    sessions.add(this)

    val ctx = VafsContext()
    val init = apiV1Mapper.writeValueAsString(ctx.toTransportInit())
    outgoing.send(Frame.Text(init))

    incoming.receiveAsFlow().mapNotNull { it ->
        val frame = it as? Frame.Text ?: return@mapNotNull

        val jsonStr = frame.readText()
        val context = VafsContext()

        try {
            val request = apiV1Mapper.readValue<IRequest>(jsonStr)
            context.fromTransport(request)
            context.ruleResponse = VafsRuleStub.get()

            val result = apiV1Mapper.writeValueAsString(context.toTransportRule())

            if (context.isUpdatableCommand()) {
                sessions.forEach {
                    it.send(Frame.Text(result))
                }
            } else {
                outgoing.send(Frame.Text(result))
            }
        } catch (_: ClosedReceiveChannelException) {
            sessions.remove(this)
        } catch (t: Throwable) {
            context.addError(t.asVafsError())

            val result = apiV1Mapper.writeValueAsString(context.toTransportInit())
            outgoing.send(Frame.Text(result))
        }
    }.collect()
}
