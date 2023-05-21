package ru.beeline.vafs.repo.posgresql

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.InsertStatement
import ru.beeline.vafs.common.models.*

object RuleTable : Table("rule") {
    val id = varchar("id", 50)
    val description = varchar("description", 4000)
    val userId = varchar("user_id", 50)
    val priority = integer("priority")
    val listForNumberA = varchar("list_for_number_a", 50)
    val typeOperationA = enumeration<VafsTypeOperationList>("type_operation_a")
    val listForNumberB = varchar("list_for_number_b", 50)
    val typeOperationB = enumeration<VafsTypeOperationList>("type_operation_b")
    val typeOperationCount = enumeration<VafsTypeOperationCount>("type_operation_count")
    val targetCount = integer("target_count")
    val valueIsTrue = bool("value_is_true")
    val typeOperationAB = enumeration<VafsTypeOperationBool>("type_operation_a_b")
    val typeOperationABCount = enumeration<VafsTypeOperationBool>("type_operation_ab_count")
    val lock = varchar("lock", 50)

    override val primaryKey = PrimaryKey(id)

    fun from(resRule: InsertStatement<Number>, resListA: List<String>, resListB: List<String>, user: String) = VafsRule(
        id = VafsRuleId(resRule[id].toString()),
        description = resRule[description],
        userId = VafsUserId(user),
        priority = resRule[priority],
        listForNumberA = resListA,
        typeOperationA = resRule[typeOperationA],
        listForNumberB = resListB,
        typeOperationB = resRule[typeOperationB],
        typeOperationCount = resRule[typeOperationCount],
        targetCount = resRule[targetCount],
        valueIsTrue = resRule[valueIsTrue],
        typeOperationAB = resRule[typeOperationAB],
        typeOperationABCount = resRule[typeOperationABCount],
        lock = VafsRuleLock(resRule[lock])
    )

    fun from(resRule: ResultRow, resListA: List<String>, resListB: List<String>, user: String) = VafsRule(
        id = VafsRuleId(resRule[id].toString()),
        description = resRule[description],
        userId = VafsUserId(user),
        priority = resRule[priority],
        listForNumberA = resListA,
        typeOperationA = resRule[typeOperationA],
        listForNumberB = resListB,
        typeOperationB = resRule[typeOperationB],
        typeOperationCount = resRule[typeOperationCount],
        targetCount = resRule[targetCount],
        valueIsTrue = resRule[valueIsTrue],
        typeOperationAB = resRule[typeOperationAB],
        typeOperationABCount = resRule[typeOperationABCount],
        lock = VafsRuleLock(resRule[lock])
    )
}

object UserTable : Table("user") {
    val id = varchar("id", 50)
    val name = varchar("name", 50).uniqueIndex()

    override val primaryKey = PrimaryKey(id)
}

object ListNumberTable : Table("list_number") {
    val id = varchar("id", 50)
    val value = varchar("value", 15)
}
