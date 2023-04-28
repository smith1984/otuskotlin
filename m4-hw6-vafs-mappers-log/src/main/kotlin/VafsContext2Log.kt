package ru.beeline.api.logs.mapper

import kotlinx.datetime.Clock
import ru.beeline.api.models.*
import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.*

fun VafsContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "vafs",
    rule = toVafsLog(),
    errors = errors.map { it.toLog() },
)

fun VafsContext.toVafsLog():VafsLogModel? {
    val ruleNone = VafsRule()
    return VafsLogModel(
        requestId = requestId.takeIf { it != VafsRequestId.NONE }?.asString(),
        requestRule = ruleRequest.takeIf { it != ruleNone }?.toLog(),
        responseRule = ruleResponse.takeIf { it != ruleNone }?.toLog(),
        responseRules = rulesResponse.takeIf { it.isNotEmpty() }?.filter { it != ruleNone }?.map { it.toLog() },
        requestFilter = ruleFilterRequest.takeIf { it != VafsRuleFilter() }?.toLog(),
    ).takeIf { it != VafsLogModel() }
}

private fun VafsRuleFilter.toLog() = RuleFilterLog(
    searchString = searchString.takeIf { it.isNotBlank() },
    userId = userId.takeIf { it != VafsUserId.NONE }?.asString(),
)

fun VafsError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

fun VafsRule.toLog() = RuleLog(
    id = id.takeIf { it != VafsRuleId.NONE }?.asString(),
    description = description.takeIf { it.isNotBlank() },
    priority = priority.takeIf { it != 0 },
    listForNumberA = listForNumberA.takeIf { it.isNotEmpty() },
    typeOperationA = typeOperationA.toLog(),
    listForNumberB = listForNumberB.takeIf { it.isNotEmpty() },
    typeOperationB = typeOperationB.toLog(),
    typeOperationCount = typeOperationCount.toLog(),
    targetCount = targetCount.takeIf { it != 0 },
    valueIsTrue = valueIsTrue.takeIf { !it },
    typeOperationAB = typeOperationAB.toLog(),
    typeOperationABCount = typeOperationABCount.toLog(),
    userId = userId.takeIf { it != VafsUserId.NONE }?.asString(),
    permissions = permissionsOperator.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
)

private fun VafsTypeOperationList.toLog(): String? = when (this) {
    VafsTypeOperationList.INCLUDE -> "INCLUDE"
    VafsTypeOperationList.EXCLUDE -> "EXCLUDE"
    VafsTypeOperationList.NONE -> null
}

private fun VafsTypeOperationCount.toLog(): String? = when (this) {
    VafsTypeOperationCount.MORE -> "GREATER_THAN"
    VafsTypeOperationCount.MORE_EQUAL -> "GREATER_THAN_EQUAL"
    VafsTypeOperationCount.EQUAL -> "EQUAL"
    VafsTypeOperationCount.LESS -> "LESS_THAN"
    VafsTypeOperationCount.LESS_EQUAL -> "LESS_THAN_EQUAL"
    VafsTypeOperationCount.NONE -> null
}

private fun VafsTypeOperationBool.toLog(): String? = when (this) {
    VafsTypeOperationBool.OR -> "OR"
    VafsTypeOperationBool.XOR -> "XOR"
    VafsTypeOperationBool.AND -> "AND"
    VafsTypeOperationBool.NOT -> "NOT"
    VafsTypeOperationBool.NONE -> null
}