package ru.beeline.vafs.common

import kotlinx.datetime.Instant
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.common.stubs.VafsStubs

data class VafsContext(
    var command: VafsCommand = VafsCommand.NONE,
    var state: VafsState = VafsState.NONE,
    val errors: MutableList<VafsError> = mutableListOf(),

    var workMode: VafsWorkMode = VafsWorkMode.PROD,
    var stubCase: VafsStubs = VafsStubs.NONE,

    var requestId: VafsRequestId = VafsRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var ruleRequest: VafsRule = VafsRule(),
    var ruleFilterRequest: VafsRuleFilter = VafsRuleFilter(),
    var ruleResponse: VafsRule = VafsRule(),
    var rulesResponse: MutableList<VafsRule> = mutableListOf(),
)
