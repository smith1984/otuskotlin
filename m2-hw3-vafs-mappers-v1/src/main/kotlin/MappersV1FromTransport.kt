package ru.beeline.vafs.mappers

import ru.beeline.api.v1.models.*
import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.common.models.VafsWorkMode
import ru.beeline.vafs.common.stubs.VafsStubs
import ru.beeline.vafs.mappers.exceptions.UnknownRequestClass

fun VafsContext.fromTransport(request: IRequest) = when (request) {
    is RuleCreateRequest -> fromTransport(request)
    is RuleReadRequest -> fromTransport(request)
    is RuleUpdateRequest -> fromTransport(request)
    is RuleDeleteRequest -> fromTransport(request)
    is RuleSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toRuleId() = this?.let { VafsRuleId(it) } ?: VafsRuleId.NONE
private fun String?.toRuleWithId() = VafsRule(id = this.toRuleId())
private fun String?.toRuleLock() = this?.let { VafsRuleLock(it) } ?: VafsRuleLock.NONE
private fun IRequest?.requestId() = this?.requestId?.let { VafsRequestId(it) } ?: VafsRequestId.NONE

private fun RuleDebug?.transportToWorkMode(): VafsWorkMode = when (this?.mode) {
    RuleRequestDebugMode.PROD -> VafsWorkMode.PROD
    RuleRequestDebugMode.TEST -> VafsWorkMode.TEST
    RuleRequestDebugMode.STUB -> VafsWorkMode.STUB
    null -> VafsWorkMode.PROD
}

private fun RuleDebug?.transportToStubCase(): VafsStubs = when (this?.stub) {
    RuleRequestDebugStubs.SUCCESS -> VafsStubs.SUCCESS
    RuleRequestDebugStubs.NOT_FOUND -> VafsStubs.NOT_FOUND
    RuleRequestDebugStubs.BAD_ID -> VafsStubs.BAD_ID
    RuleRequestDebugStubs.BAD_DESCRIPTION -> VafsStubs.BAD_DESCRIPTION
    RuleRequestDebugStubs.BAD_PRIORITY -> VafsStubs.BAD_PRIORITY
    RuleRequestDebugStubs.BAD_LIST_FOR_NUMBER_A -> VafsStubs.BAD_LIST_FOR_NUMBER_A
    RuleRequestDebugStubs.BAD_TYPE_OPERATION_A -> VafsStubs.BAD_TYPE_OPERATION_A
    RuleRequestDebugStubs.BAD_LIST_FOR_NUMBER_B -> VafsStubs.BAD_LIST_FOR_NUMBER_B
    RuleRequestDebugStubs.BAD_TYPE_OPERATION_B -> VafsStubs.BAD_TYPE_OPERATION_B
    RuleRequestDebugStubs.BAD_TYPE_OPERATION_COUNT -> VafsStubs.BAD_TYPE_OPERATION_COUNT
    RuleRequestDebugStubs.BAD_TARGET_COUNT -> VafsStubs.BAD_TARGET_COUNT
    RuleRequestDebugStubs.BAD_VALUE_IS_TRUE -> VafsStubs.BAD_VALUE_IS_TRUE
    RuleRequestDebugStubs.BAD_TYPE_OPERATION_AB -> VafsStubs.BAD_TYPE_OPERATION_AB
    RuleRequestDebugStubs.BAD_TYPE_OPERATION_AB_COUNT -> VafsStubs.BAD_TYPE_OPERATION_AB_COUNT
    RuleRequestDebugStubs.CANNOT_DELETE -> VafsStubs.CANNOT_DELETE
    RuleRequestDebugStubs.BAD_SEARCH_STRING -> VafsStubs.BAD_SEARCH_STRING
    null -> VafsStubs.NONE
}

fun VafsContext.fromTransport(request: RuleCreateRequest) {
    command = VafsCommand.CREATE
    requestId = request.requestId()
    ruleRequest = request.rule?.toInternal() ?: VafsRule()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun VafsContext.fromTransport(request: RuleReadRequest) {
    command = VafsCommand.READ
    requestId = request.requestId()
    ruleRequest = request.rule.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun RuleReadObject?.toInternal(): VafsRule = if (this != null) {
    VafsRule(id = id.toRuleId())
} else {
    VafsRule.NONE
}

fun VafsContext.fromTransport(request: RuleUpdateRequest) {
    command = VafsCommand.UPDATE
    requestId = request.requestId()
    ruleRequest = request.rule?.toInternal() ?: VafsRule()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun VafsContext.fromTransport(request: RuleDeleteRequest) {
    command = VafsCommand.DELETE
    requestId = request.requestId()
    ruleRequest = request.rule.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun RuleDeleteObject?.toInternal(): VafsRule = if (this != null) {
    VafsRule(
        id = id.toRuleId(),
        lock = lock.toRuleLock(),
    )
} else {
    VafsRule.NONE
}

fun VafsContext.fromTransport(request: RuleSearchRequest) {
    command = VafsCommand.SEARCH
    requestId = request.requestId()
    ruleFilterRequest = request.ruleFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun RuleSearchFilter?.toInternal(): VafsRuleFilter = VafsRuleFilter(
    searchString = this?.searchString ?: ""
)

private fun RuleCreateObject.toInternal(): VafsRule = VafsRule(
    description = this.description ?: "",
    priority = this.priority ?: 0,
    listForNumberA = this.listForNumberA ?: listOf(),
    typeOperationA = this.typeOperationA.fromTransport(),
    listForNumberB = this.listForNumberB ?: listOf(),
    typeOperationB = this.typeOperationB.fromTransport(),
    typeOperationCount = this.typeOperationCount.fromTransport(),
    targetCount = this.targetCount ?: 0,
    valueIsTrue = this.valueIsTrue ?: false,
    typeOperationAB = this.typeOperationAB.fromTransport(),
    typeOperationABCount = this.typeOperationABCount.fromTransport()
)

private fun TypeOperation?.fromTransport(): VafsTypeOperationList = when (this) {
    TypeOperation.INCLUDE -> VafsTypeOperationList.INCLUDE
    TypeOperation.EXCLUDE -> VafsTypeOperationList.EXCLUDE
    null -> VafsTypeOperationList.NONE
}

private fun TypeOperationBool?.fromTransport(): VafsTypeOperationBool = when (this) {
    TypeOperationBool.OR -> VafsTypeOperationBool.OR
    TypeOperationBool.XOR -> VafsTypeOperationBool.XOR
    TypeOperationBool.AND -> VafsTypeOperationBool.AND
    TypeOperationBool.NOT -> VafsTypeOperationBool.NOT
    null -> VafsTypeOperationBool.NONE
}

private fun TypeOperationCount?.fromTransport(): VafsTypeOperationCount = when (this) {
    TypeOperationCount.EQUAL -> VafsTypeOperationCount.EQUAL
    TypeOperationCount.LESS_THAN -> VafsTypeOperationCount.LESS
    TypeOperationCount.LESS_THAN_EQUAL -> VafsTypeOperationCount.LESS_EQUAL
    TypeOperationCount.GREATER_THAN -> VafsTypeOperationCount.MORE
    TypeOperationCount.GREATER_THAN_EQUAL -> VafsTypeOperationCount.MORE_EQUAL
    null -> VafsTypeOperationCount.NONE
}
private fun RuleUpdateObject.toInternal(): VafsRule = VafsRule(
    id = this.id.toRuleId(),
    description = this.description ?: "",
    priority = this.priority ?: 0,
    listForNumberA = this.listForNumberA ?: listOf(),
    typeOperationA = this.typeOperationA.fromTransport(),
    listForNumberB = this.listForNumberB ?: listOf(),
    typeOperationB = this.typeOperationB.fromTransport(),
    typeOperationCount = this.typeOperationCount.fromTransport(),
    targetCount = this.targetCount ?: 0,
    valueIsTrue = this.valueIsTrue ?: false,
    typeOperationAB = this.typeOperationAB.fromTransport(),
    typeOperationABCount = this.typeOperationABCount.fromTransport(),
    lock = lock.toRuleLock(),
)


