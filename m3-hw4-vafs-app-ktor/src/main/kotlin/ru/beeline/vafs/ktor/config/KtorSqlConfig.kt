package ru.beeline.vafs.ktor.ru.beeline.vafs.ktor.config

import io.ktor.server.application.*
import ru.beeline.vafs.repo.posgresql.SqlProperties

fun Application.getSqlProperties(environment: ApplicationEnvironment): SqlProperties {
    return SqlProperties(
        url = environment.config.property("sql.url").getString(),
        user = environment.config.property("sql.user").getString(),
        password = environment.config.property("sql.password").getString(),
    )
}