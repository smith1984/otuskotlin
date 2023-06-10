package ru.beeline.vafs.auth

import ru.beeline.vafs.common.models.VafsCommand
import ru.beeline.vafs.common.permissions.VafsPrincipalRelations
import ru.beeline.vafs.common.permissions.VafsUserPermissions

fun checkPermitted(
    command: VafsCommand,
    relations: Iterable<VafsPrincipalRelations>,
    permissions: Iterable<VafsUserPermissions>,
) =
    relations.asSequence().flatMap { relation ->
        permissions.map { permission ->
            AccessTableConditions(
                command = command,
                permission = permission,
                relation = relation,
            )
        }
    }.any {
        accessTable[it] != null
    }

private data class AccessTableConditions(
    val command: VafsCommand,
    val permission: VafsUserPermissions,
    val relation: VafsPrincipalRelations
)

private val accessTable = mapOf(
    // Create
    AccessTableConditions(
        command = VafsCommand.CREATE,
        permission = VafsUserPermissions.CREATE_OWN,
        relation = VafsPrincipalRelations.NEW,
    ) to true,

    // Read
    AccessTableConditions(
        command = VafsCommand.READ,
        permission = VafsUserPermissions.READ_OWN,
        relation = VafsPrincipalRelations.OWN,
    ) to true,
    AccessTableConditions(
        command = VafsCommand.READ,
        permission = VafsUserPermissions.READ_PUBLIC,
        relation = VafsPrincipalRelations.PUBLIC,
    ) to true,

    // Update
    AccessTableConditions(
        command = VafsCommand.UPDATE,
        permission = VafsUserPermissions.UPDATE_OWN,
        relation = VafsPrincipalRelations.OWN,
    ) to true,

    // Delete
    AccessTableConditions(
        command = VafsCommand.DELETE,
        permission = VafsUserPermissions.DELETE_OWN,
        relation = VafsPrincipalRelations.OWN,
    ) to true,
)
