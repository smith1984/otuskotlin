package ru.beeline.vafs.common.models

data class VafsRuleFilter(
    var searchString: String = "",
    var userId: VafsUserId = VafsUserId.NONE,
    val searchPermissions: MutableSet<VafsSearchPermissions> = mutableSetOf(),
    )
