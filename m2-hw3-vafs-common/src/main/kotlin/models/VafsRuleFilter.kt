package ru.beeline.vafs.common.models

data class VafsRuleFilter(
    var searchString: String = "",
    var ownerId: VafsRuleId = VafsRuleId.NONE,
)
