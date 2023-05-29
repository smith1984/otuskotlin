package ru.beeline.vafs.ktor.config

import io.ktor.server.config.*

data class KtorAuthConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String,
    val clientId: String,
    val certUrl: String? = null,
) {
    constructor(config: ApplicationConfig): this(
        secret = config.propertyOrNull("$PATH.secret")?.getString() ?: "",
        issuer = config.property("$PATH.issuer").getString(),
        audience = config.property("$PATH.audience").getString(),
        realm = config.property("$PATH.realm").getString(),
        clientId = config.property("$PATH.clientId").getString(),
        certUrl = config.propertyOrNull("$PATH.certUrl")?.getString(),
    )

    companion object {
        const val PATH = "jwt"
        const val ID_CLAIM = "sub"
        const val GROUPS_CLAIM = "groups"
        const val F_NAME_CLAIM = "fname"
        const val M_NAME_CLAIM = "mname"
        const val L_NAME_CLAIM = "lname"

        val TEST = KtorAuthConfig(
            secret = "secret",
            issuer = "beeline",
            audience = "rule-users",
            realm = "vafs-rules",
            clientId = "beeline-vafs-service",
        )

        val NONE = KtorAuthConfig(
            secret = "",
            issuer = "",
            audience = "",
            realm = "",
            clientId = "",
        )
    }
}
