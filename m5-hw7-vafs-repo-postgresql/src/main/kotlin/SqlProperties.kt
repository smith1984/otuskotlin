package ru.beeline.vafs.repo.posgresql

open class SqlProperties(
    val url: String = "jdbc:postgresql://localhost:5432/vafs",
    val user: String = "vafs",
    val password: String = "vafs",
    val schema: String = "backoffice",
    val clearTables: Boolean = false,
)