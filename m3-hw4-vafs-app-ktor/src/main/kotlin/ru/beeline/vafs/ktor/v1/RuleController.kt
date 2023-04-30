package ru.beeline.vafs.ktor.v1

import io.ktor.server.application.*
import ru.beeline.api.v1.models.*
import ru.beeline.vafs.common.models.VafsCommand
import ru.beeline.vafs.ktor.VafsAppSettings
import ru.beeline.vafs.logging.common.ILogWrapper

suspend fun ApplicationCall.createRule(appSettings: VafsAppSettings, logger: ILogWrapper) =
    processV1<RuleCreateRequest, RuleCreateResponse>(appSettings, logger, "rule-create", VafsCommand.CREATE)

suspend fun ApplicationCall.readRule(appSettings: VafsAppSettings, logger: ILogWrapper) =
    processV1<RuleReadRequest, RuleReadResponse>(appSettings, logger, "rule-read", VafsCommand.READ)

suspend fun ApplicationCall.updateRule(appSettings: VafsAppSettings, logger: ILogWrapper) =
    processV1<RuleUpdateRequest, RuleUpdateResponse>(appSettings, logger, "rule-update", VafsCommand.UPDATE)

suspend fun ApplicationCall.deleteRule(appSettings: VafsAppSettings, logger: ILogWrapper) =
    processV1<RuleDeleteRequest, RuleDeleteResponse>(appSettings, logger, "rule-delete", VafsCommand.DELETE)

suspend fun ApplicationCall.searchRule(appSettings: VafsAppSettings,logger: ILogWrapper) =
    processV1<RuleSearchRequest, RuleSearchResponse>(appSettings, logger, "rule-search", VafsCommand.SEARCH)