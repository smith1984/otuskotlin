package ru.beeline.vafs.common

import kotlinx.datetime.Instant
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.common.permissions.VafsPrincipalModel
import ru.beeline.vafs.common.permissions.VafsUserPermissions
import ru.beeline.vafs.common.repo.IRuleRepository
import ru.beeline.vafs.common.stubs.VafsStubs

data class VafsContext(
    var command: VafsCommand = VafsCommand.NONE,
    var state: VafsState = VafsState.NONE,
    val errors: MutableList<VafsError> = mutableListOf(),
    var settings: VafsCorSettings = VafsCorSettings.NONE,

    var workMode: VafsWorkMode = VafsWorkMode.PROD,
    var stubCase: VafsStubs = VafsStubs.NONE,

    var principal: VafsPrincipalModel = VafsPrincipalModel.NONE,
    val permissionsChain: MutableSet<VafsUserPermissions> = mutableSetOf(),
    var permitted: Boolean = false,

    var ruleRepo: IRuleRepository = IRuleRepository.NONE,
    var ruleRepoRead: VafsRule = VafsRule(),
    var ruleRepoPrepare: VafsRule = VafsRule(),
    var ruleRepoDone: VafsRule = VafsRule(),
    var rulesRepoDone: MutableList<VafsRule> = mutableListOf(),

    var requestId: VafsRequestId = VafsRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var ruleRequest: VafsRule = VafsRule(),
    var ruleFilterRequest: VafsRuleFilter = VafsRuleFilter(),

    var ruleValidating: VafsRule = VafsRule(),
    var ruleFilterValidating: VafsRuleFilter = VafsRuleFilter(),

    var ruleValidated: VafsRule = VafsRule(),
    var ruleFilterValidated: VafsRuleFilter = VafsRuleFilter(),

    var ruleResponse: VafsRule = VafsRule(),
    var rulesResponse: MutableList<VafsRule> = mutableListOf(),
)
