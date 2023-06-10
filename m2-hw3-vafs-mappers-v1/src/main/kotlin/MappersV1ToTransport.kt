package ru.beeline.vafs.mappers

import ru.beeline.api.v1.models.*
import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.common.permissions.VafsRulePermissionOperator
import ru.beeline.vafs.mappers.exceptions.UnknownVafsCommand

fun VafsContext.toTransportRule(): IResponse = when (val cmd = command) {
    VafsCommand.CREATE -> toTransportCreate()
    VafsCommand.READ -> toTransportRead()
    VafsCommand.UPDATE -> toTransportUpdate()
    VafsCommand.DELETE -> toTransportDelete()
    VafsCommand.SEARCH -> toTransportSearch()
    VafsCommand.NONE -> throw UnknownVafsCommand(cmd)
}

fun VafsContext.toTransportCreate() = RuleCreateResponse(
    responseType = "create",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == VafsState.FINISHING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    rule = ruleResponse.toTransportRule()
)

fun VafsContext.toTransportRead() = RuleReadResponse(
    responseType = "read",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == VafsState.FINISHING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    rule = ruleResponse.toTransportRule()
)

fun VafsContext.toTransportUpdate() = RuleUpdateResponse(
    responseType = "update",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == VafsState.FINISHING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    rule = ruleResponse.toTransportRule()
)

fun VafsContext.toTransportDelete() = RuleDeleteResponse(
    responseType = "delete",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == VafsState.FINISHING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    rule = ruleResponse.toTransportRule()
)

fun VafsContext.toTransportSearch() = RuleSearchResponse(
    responseType = "search",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == VafsState.FINISHING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    rules = rulesResponse.toTransportRule()
)

fun List<VafsRule>.toTransportRule(): List<RuleResponseObject>? = this
    .map { it.toTransportRule() }
    .toList()
    .takeIf { it.isNotEmpty() }

fun VafsContext.toTransportInit() = RuleInitResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (errors.isEmpty()) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
)

private fun VafsRule.toTransportRule(): RuleResponseObject = RuleResponseObject(
    id = id.takeIf { it != VafsRuleId.NONE }?.asString(),
    description = description.takeIf { it.isNotBlank() },
    userId = userId.takeIf { it != VafsUserId.NONE }?.asString(),
    permissions = permissionsOperator.toTransportRule(),
    priority = priority.takeIf { it != 0 },
    listForNumberA = listForNumberA.takeIf { it.isNotEmpty() },
    typeOperationA = typeOperationA.toTransportRule(),
    listForNumberB = listForNumberB.takeIf { it.isNotEmpty() },
    typeOperationB = typeOperationB.toTransportRule(),
    typeOperationCount = typeOperationCount.toTransportRule(),
    targetCount = targetCount.takeIf { it != 0 },
    valueIsTrue = valueIsTrue,
    typeOperationAB = typeOperationAB.toTransportRule(),
    typeOperationABCount = typeOperationABCount.toTransportRule(),
    lock = lock.takeIf { it != VafsRuleLock.NONE }?.asString(),
)

private fun Set<VafsRulePermissionOperator>.toTransportRule(): Set<RulePermissions>? = this
    .map { it.toTransportRule() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun VafsRulePermissionOperator.toTransportRule() = when (this) {
    VafsRulePermissionOperator.READ -> RulePermissions.READ
    VafsRulePermissionOperator.UPDATE -> RulePermissions.UPDATE
    VafsRulePermissionOperator.DELETE -> RulePermissions.DELETE
    VafsRulePermissionOperator.CREATE -> RulePermissions.CREATE

}

private fun VafsTypeOperationList.toTransportRule(): TypeOperation? = when (this) {
    VafsTypeOperationList.INCLUDE -> TypeOperation.INCLUDE
    VafsTypeOperationList.EXCLUDE -> TypeOperation.EXCLUDE
    VafsTypeOperationList.NONE -> null
}

private fun VafsTypeOperationCount.toTransportRule(): TypeOperationCount? = when (this) {
    VafsTypeOperationCount.MORE -> TypeOperationCount.GREATER_THAN
    VafsTypeOperationCount.MORE_EQUAL -> TypeOperationCount.GREATER_THAN_EQUAL
    VafsTypeOperationCount.EQUAL -> TypeOperationCount.EQUAL
    VafsTypeOperationCount.LESS -> TypeOperationCount.LESS_THAN
    VafsTypeOperationCount.LESS_EQUAL -> TypeOperationCount.LESS_THAN_EQUAL
    VafsTypeOperationCount.NONE -> null
}

private fun VafsTypeOperationBool.toTransportRule(): TypeOperationBool? = when (this) {
    VafsTypeOperationBool.OR -> TypeOperationBool.OR
    VafsTypeOperationBool.XOR -> TypeOperationBool.XOR
    VafsTypeOperationBool.AND -> TypeOperationBool.AND
    VafsTypeOperationBool.NOT -> TypeOperationBool.NOT
    VafsTypeOperationBool.NONE -> null
}

private fun List<VafsError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportRule() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun VafsError.toTransportRule() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)
