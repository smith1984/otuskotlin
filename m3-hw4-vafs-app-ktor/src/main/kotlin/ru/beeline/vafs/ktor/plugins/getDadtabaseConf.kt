package ru.beeline.vafs.ktor.plugins

import io.ktor.server.application.*

import ru.beeline.vafs.repo.posgresql.RuleRepoPostgresql
import ru.beeline.vafs.repo.posgresql.SqlProperties
import ru.beeline.vafs.common.repo.IRuleRepository
import ru.beeline.vafs.ktor.config.PostgresConfig
import ru.beeline.vafs.repo.inmemory.RuleRepoInMemory
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

enum class RuleDbType(val confName: String) {
    PROD("prod"), TEST("test")
}

fun Application.getDatabaseConf(type: RuleDbType): IRuleRepository {
    val dbSettingPath = "vafs.repository.${type.confName}"
    val dbSetting = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()
    return when (dbSetting) {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory()
        "postgres", "postgresql", "pg", "sql", "psql" -> initPostgres()
        else -> throw IllegalArgumentException(
            "$dbSettingPath must be set in application.yml to one of: " +
                    "'inmemory', 'postgres'"
        )
    }
}

private fun Application.initPostgres(): IRuleRepository {
    val config = PostgresConfig(environment.config)
    return RuleRepoPostgresql(
        properties = SqlProperties(
            url = config.url,
            user = config.user,
            password = config.password,
            schema = config.schema,
        )
    )
}

private fun Application.initInMemory(): IRuleRepository {
    val ttlSetting = environment.config.propertyOrNull("db.prod")?.getString()?.let {
        Duration.parse(it)
    }
    return RuleRepoInMemory(ttl = ttlSetting ?: 10.minutes)
}
