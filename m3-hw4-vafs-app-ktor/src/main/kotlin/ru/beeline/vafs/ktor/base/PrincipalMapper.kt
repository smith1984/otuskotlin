package ru.beeline.vafs.ktor.base

import io.ktor.server.auth.jwt.*
import ru.beeline.vafs.ktor.config.KtorAuthConfig.Companion.F_NAME_CLAIM
import ru.beeline.vafs.ktor.config.KtorAuthConfig.Companion.GROUPS_CLAIM
import ru.beeline.vafs.ktor.config.KtorAuthConfig.Companion.ID_CLAIM
import ru.beeline.vafs.ktor.config.KtorAuthConfig.Companion.L_NAME_CLAIM
import ru.beeline.vafs.ktor.config.KtorAuthConfig.Companion.M_NAME_CLAIM
import ru.beeline.vafs.common.permissions.VafsPrincipalModel
import ru.beeline.vafs.common.permissions.VafsUserGroups
import ru.beeline.vafs.common.models.VafsUserId

fun JWTPrincipal?.toModel() = this?.run {
    VafsPrincipalModel(
        id = payload.getClaim(ID_CLAIM).asString()?.let { VafsUserId(it) } ?: VafsUserId.NONE,
        fname = payload.getClaim(F_NAME_CLAIM).asString() ?: "",
        mname = payload.getClaim(M_NAME_CLAIM).asString() ?: "",
        lname = payload.getClaim(L_NAME_CLAIM).asString() ?: "",
        groups = payload
            .getClaim(GROUPS_CLAIM)
            ?.asList(String::class.java)
            ?.mapNotNull {
                when(it) {
                    "USER" -> VafsUserGroups.USER
                    else -> null
                }
            }?.toSet() ?: emptySet()
    )
} ?: VafsPrincipalModel.NONE
