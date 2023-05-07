package ru.beeline.vafs.repo.posgresql

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class SqlConnector(
    private val properties: SqlProperties,
    private val databaseConfig: DatabaseConfig = DatabaseConfig { },
) {

    private val driver: String = properties.url.let { url ->
        when {
            url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
            else -> error("Cannot parse database type from url: $url.`jdbc:postgresql://` are supported only.")
        }
    }

    fun database(vararg tables: Table): Database {
        val db = Database.connect(
            properties.url, driver, properties.user, properties.password,
            databaseConfig = databaseConfig,
            setupConnection = { connection ->
                connection.schema = properties.schema
            }
        )

        transaction(db) {
            if (properties.clearTables) {
                tables.forEach { t -> t.deleteAll() }
            }
        }

        return db
    }
}