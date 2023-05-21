package ru.beeline.vafs.repository.test

import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.resource.DirectoryResourceAccessor
import org.testcontainers.containers.PostgreSQLContainer
import ru.beeline.vafs.common.models.VafsRule
import ru.beeline.vafs.repo.posgresql.RuleRepoPostgresql
import ru.beeline.vafs.repo.posgresql.SqlProperties
import java.time.Duration
import java.util.UUID

import java.io.File


class PostgresContainer : PostgreSQLContainer<PostgresContainer>("postgres:15.2")

object SqlTestCompanion {
    private const val USER = "vafs"
    private const val PASS = "vafs"
    private const val DB = "vafs"

    private val container by lazy {
        PostgresContainer().apply {
            withUsername(USER)
            withPassword(PASS)
            withDatabaseName(DB)
            withStartupTimeout(Duration.ofSeconds(300L))
            start()
        }
    }

    private val url by lazy { container.jdbcUrl }

    private val driver by lazy { container.driverClassName }

    private fun migrationDDL() {
        val resourceAccessor = DirectoryResourceAccessor(File("../infra/volumes/postgres/liquidbase"))

        val database = DatabaseFactory.getInstance().openDatabase(
            url,
            USER,
            PASS,
            driver,
            null, null, null, resourceAccessor
        )
        val lq = Liquibase("changelog.yaml", resourceAccessor, database)

        lq.update()
    }

    fun repoUnderTestContainer(
        initObjects: Collection<VafsRule>,
        randomUuid: () -> String = { UUID.randomUUID().toString() },
    ): RuleRepoPostgresql {
        migrationDDL()
        return RuleRepoPostgresql(SqlProperties(url, USER, PASS, clearTables = true), initObjects = initObjects, randomUuid = randomUuid)
    }
}
