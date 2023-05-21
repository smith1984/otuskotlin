package ru.beeline.vafs.ktor.ru.beeline.vafs.ktor.config

import io.ktor.server.application.*
import io.ktor.server.config.*
import ru.beeline.vafs.repo.posgresql.SqlProperties

fun Application.getSqlProperties(environment: ApplicationEnvironment): SqlProperties {
    return SqlProperties(
        url = environment.config.property("sql.url").getString(),
        user = environment.config.property("sql.user").getString(),
        password = environment.config.property("sql.password").getString(),
    )
}

data class PostgresConfig(
    val url: String = "jdbc:postgresql://localhost:5432/vafs",
    val user: String = "backoffice",
    val password: String = "vafs",
    val schema: String = "vafs",
) {
    constructor(config: ApplicationConfig): this(
        url = config.property("$PATH.url").getString(),
        user = config.property("$PATH.user").getString(),
        password = config.property("$PATH.password").getString(),
        schema = config.property("$PATH.schema").getString(),
    )

    companion object {
        const val PATH = "vafs.repository.psql"
    }
}