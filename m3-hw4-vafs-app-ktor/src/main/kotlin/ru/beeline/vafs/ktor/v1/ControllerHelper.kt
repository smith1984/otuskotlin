package ru.beeline.vafs.ktor.v1

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.datetime.Clock
import ru.beeline.api.logs.mapper.toLog
import ru.beeline.api.v1.models.IRequest
import ru.beeline.api.v1.models.IResponse
import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.helpers.asVafsError
import ru.beeline.vafs.common.models.VafsCommand
import ru.beeline.vafs.common.models.VafsState
import ru.beeline.vafs.ktor.VafsAppSettings
import ru.beeline.vafs.ktor.base.toModel
import ru.beeline.vafs.logging.common.ILogWrapper
import ru.beeline.vafs.mappers.fromTransport
import ru.beeline.vafs.mappers.toTransportRule

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processV1(
    appSettings: VafsAppSettings,
    logger: ILogWrapper,
    logId: String,
    command: VafsCommand? = null,
) {
    val ctx = VafsContext(
        timeStart = Clock.System.now(),
    )
    val processor = appSettings.processor
    try {
        logger.doWithLogging(id = logId) {
            ctx.principal = principal<JWTPrincipal>().toModel()
            val request = receive<Q>()
            ctx.fromTransport(request)
            logger.info(
                msg = "$command request is got",
                data = ctx.toLog("${logId}-got")
            )
            processor.exec(ctx)
            logger.info(
                msg = "$command request is handled",
                data = ctx.toLog("${logId}-handled")
            )
            respond(ctx.toTransportRule())
        }
    } catch (e: Throwable) {
        logger.doWithLogging(id = "${logId}-failure") {
            command?.also { ctx.command = it }
            logger.error(
                msg = "$command handling failed",
            )
            ctx.state = VafsState.FAILING
            ctx.errors.add(e.asVafsError())
            processor.exec(ctx)
            respond(ctx.toTransportRule())
        }
    }
}
