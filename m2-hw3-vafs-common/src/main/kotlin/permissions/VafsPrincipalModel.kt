package ru.beeline.vafs.common.permissions

import ru.beeline.vafs.common.models.VafsUserId

data class VafsPrincipalModel(
    val id: VafsUserId = VafsUserId.NONE,
    val fname: String = "",
    val mname: String = "",
    val lname: String = "",
    val groups: Set<VafsUserGroups> = emptySet()
) {
    companion object {
        val NONE = VafsPrincipalModel()
    }
}
