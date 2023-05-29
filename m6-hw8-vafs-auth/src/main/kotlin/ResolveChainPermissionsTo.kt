package ru.beeline.vafs.auth

import ru.beeline.vafs.common.permissions.VafsUserGroups
import ru.beeline.vafs.common.permissions.VafsUserPermissions

fun resolveChainPermissions(
    groups: Iterable<VafsUserGroups>,
) = mutableSetOf<VafsUserPermissions>()
    .apply {
        addAll(groups.flatMap { groupPermissionsAdmits[it] ?: emptySet() })
        removeAll(groups.flatMap { groupPermissionsDenys[it] ?: emptySet() }.toSet())
    }
    .toSet()

private val groupPermissionsAdmits = mapOf(
    VafsUserGroups.USER to setOf(
        VafsUserPermissions.READ_OWN,
        VafsUserPermissions.READ_PUBLIC,
        VafsUserPermissions.CREATE_OWN,
        VafsUserPermissions.UPDATE_OWN,
        VafsUserPermissions.DELETE_OWN,
    ),
    VafsUserGroups.MODERATOR_VAFS to setOf(),
    VafsUserGroups.ADMIN_RULE to setOf(),
    VafsUserGroups.TEST to setOf(),
    VafsUserGroups.BAN_RULE to setOf(),
)

private val groupPermissionsDenys = mapOf(
    VafsUserGroups.USER to setOf(),
    VafsUserGroups.MODERATOR_VAFS to setOf(),
    VafsUserGroups.ADMIN_RULE to setOf(),
    VafsUserGroups.TEST to setOf(),
    VafsUserGroups.BAN_RULE to setOf(
        VafsUserPermissions.UPDATE_OWN,
        VafsUserPermissions.CREATE_OWN,
    ),
)
