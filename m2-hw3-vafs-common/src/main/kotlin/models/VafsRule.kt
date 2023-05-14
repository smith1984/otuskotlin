package ru.beeline.vafs.common.models

data class VafsRule(
    var id: VafsRuleId = VafsRuleId.NONE,
    var userId: VafsUserId = VafsUserId.NONE,
    var description: String = "",
    var priority: Int = 0,
    var listForNumberA: List<String> = listOf(),
    var typeOperationA: VafsTypeOperationList = VafsTypeOperationList.NONE,
    var listForNumberB: List<String> = listOf(),
    var typeOperationB: VafsTypeOperationList = VafsTypeOperationList.NONE,
    var typeOperationCount: VafsTypeOperationCount = VafsTypeOperationCount.NONE,
    var targetCount: Int = 0,
    var valueIsTrue: Boolean = false,
    var typeOperationAB: VafsTypeOperationBool = VafsTypeOperationBool.NONE,
    var typeOperationABCount: VafsTypeOperationBool = VafsTypeOperationBool.NONE,
    var lock: VafsRuleLock = VafsRuleLock.NONE,
    val permissionsOperator: MutableSet<VafsRulePermissionOperator> = mutableSetOf()
) {
    fun isEmpty() = this == NONE

    companion object {
        val NONE = VafsRule()
    }
}