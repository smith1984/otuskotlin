package ru.beeline.vafs.auth

import ru.beeline.vafs.common.permissions.VafsRulePermissionOperator
import ru.beeline.vafs.common.permissions.VafsPrincipalRelations
import ru.beeline.vafs.common.permissions.VafsUserPermissions

fun resolveFrontPermissions(
    permissions: Iterable<VafsUserPermissions>,
    relations: Iterable<VafsPrincipalRelations>,
) = mutableSetOf<VafsRulePermissionOperator>()
    .apply {
        for (permission in permissions) {
            for (relation in relations) {
                accessTable[permission]?.get(relation)?.let { this@apply.add(it) }
            }
        }
    }
    .toSet()

private val accessTable = mapOf(
    // READ
    VafsUserPermissions.READ_OWN to mapOf(
        VafsPrincipalRelations.OWN to VafsRulePermissionOperator.READ
    ),
    VafsUserPermissions.READ_GROUP to mapOf(
        VafsPrincipalRelations.GROUP to VafsRulePermissionOperator.READ
    ),
    VafsUserPermissions.READ_PUBLIC to mapOf(
        VafsPrincipalRelations.PUBLIC to VafsRulePermissionOperator.READ
    ),
    VafsUserPermissions.READ_CANDIDATE to mapOf(
        VafsPrincipalRelations.MODERATABLE to VafsRulePermissionOperator.READ
    ),

    // UPDATE
    VafsUserPermissions.UPDATE_OWN to mapOf(
        VafsPrincipalRelations.OWN to VafsRulePermissionOperator.UPDATE
    ),
    VafsUserPermissions.UPDATE_PUBLIC to mapOf(
        VafsPrincipalRelations.MODERATABLE to VafsRulePermissionOperator.UPDATE
    ),
    VafsUserPermissions.UPDATE_CANDIDATE to mapOf(
        VafsPrincipalRelations.MODERATABLE to VafsRulePermissionOperator.UPDATE
    ),

    // DELETE
    VafsUserPermissions.DELETE_OWN to mapOf(
        VafsPrincipalRelations.OWN to VafsRulePermissionOperator.DELETE
    ),
    VafsUserPermissions.DELETE_PUBLIC to mapOf(
        VafsPrincipalRelations.MODERATABLE to VafsRulePermissionOperator.DELETE
    ),
    VafsUserPermissions.DELETE_CANDIDATE to mapOf(
        VafsPrincipalRelations.MODERATABLE to VafsRulePermissionOperator.DELETE
    ),
)
