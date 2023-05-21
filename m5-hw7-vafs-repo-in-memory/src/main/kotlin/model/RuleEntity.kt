package ru.beeline.vafs.repo.inmemory.model

import ru.beeline.vafs.common.models.*

data class RuleEntity(
    val id: String? = null,
    val description: String? = null,
    val userId: String? = null,
    val priority: Int? = null,
    val listForNumberA: List<String>? = null,
    val typeOperationA: String? = null,
    val listForNumberB: List<String>? = null,
    val typeOperationB: String? = null,
    val typeOperationCount: String? = null,
    val targetCount: Int? = null,
    val valueIsTrue: Boolean = false,
    val typeOperationAB: String? = null,
    val typeOperationABCount: String? = null,
    val lock: String? = null,
) {
    constructor(model: VafsRule): this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        description = model.description.takeIf { it.isNotBlank() },
        userId = model.userId.asString().takeIf { it.isNotBlank() },
        priority = model.priority.takeIf { it != 0 },
        listForNumberA = model.listForNumberA.takeIf { it.isNotEmpty() },
        typeOperationA = model.typeOperationA.toString().takeIf { it != "NONE" },
        listForNumberB = model.listForNumberB.takeIf { it.isNotEmpty() },
        typeOperationB = model.typeOperationB.toString().takeIf { it != "NONE" },
        typeOperationCount = model.typeOperationCount.toString().takeIf { it != "NONE" },
        targetCount = model.targetCount.takeIf { it != 0 },
        valueIsTrue = model.valueIsTrue,
        typeOperationAB = model.typeOperationAB.toString().takeIf { it != "NONE" },
        typeOperationABCount = model.typeOperationABCount.toString().takeIf { it != "NONE" },
        lock = model.lock.asString().takeIf { it.isNotBlank() }
    )

    fun toInternal() = VafsRule(
        id = id?.let { VafsRuleId(it) }?: VafsRuleId.NONE,
        description = description?: "",
        userId = userId?.let { VafsUserId(it) }?: VafsUserId.NONE,
        priority = priority?: 0,
        listForNumberA = listForNumberA?: listOf(),
        typeOperationA = typeOperationA?.fromStringToVafsTypeOperationList() ?: VafsTypeOperationList.NONE,
        listForNumberB = listForNumberB?: listOf(),
        typeOperationB = typeOperationB?.fromStringToVafsTypeOperationList() ?: VafsTypeOperationList.NONE,
        typeOperationCount = typeOperationCount?.fromStringToVafsTypeOperationCount() ?: VafsTypeOperationCount.NONE,
        targetCount =targetCount?: 0,
        valueIsTrue = valueIsTrue,
        typeOperationAB = typeOperationAB?.fromStringToVafsTypeOperationBool() ?: VafsTypeOperationBool.NONE,
        typeOperationABCount = typeOperationABCount?.fromStringToVafsTypeOperationBool() ?: VafsTypeOperationBool.NONE,
        lock = lock?.let { VafsRuleLock(it) } ?: VafsRuleLock.NONE,

        )
}

private fun String.fromStringToVafsTypeOperationList(): VafsTypeOperationList? = when (this) {
    "INCLUDE" -> VafsTypeOperationList.INCLUDE
    "EXCLUDE" -> VafsTypeOperationList.EXCLUDE
    else -> null
}

private fun String.fromStringToVafsTypeOperationCount(): VafsTypeOperationCount? = when (this) {
    "MORE" -> VafsTypeOperationCount.MORE
    "MORE_EQUAL" -> VafsTypeOperationCount.MORE_EQUAL
    "EQUAL" -> VafsTypeOperationCount.EQUAL
    "LESS" -> VafsTypeOperationCount.LESS
    "LESS_EQUAL" -> VafsTypeOperationCount.LESS_EQUAL
    else -> null
}

private fun String.fromStringToVafsTypeOperationBool(): VafsTypeOperationBool? = when (this) {
    "OR" -> VafsTypeOperationBool.OR
    "XOR" -> VafsTypeOperationBool.XOR
    "AND" -> VafsTypeOperationBool.AND
    "NOT" -> VafsTypeOperationBool.NOT
    else -> null
}