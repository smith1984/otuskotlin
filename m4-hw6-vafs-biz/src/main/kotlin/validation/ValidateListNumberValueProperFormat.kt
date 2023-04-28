package ru.beeline.vafs.biz.validation

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.helpers.errorValidation
import ru.beeline.vafs.common.helpers.fail
import ru.beeline.vafs.common.models.VafsRuleId
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.worker


fun ICorChainDsl<VafsContext>.validateListNumberAValueProperFormat(title: String) = worker {
    this.title = title

    val regExp = Regex("^[0-9+*?-]+$")
    on { ruleValidating.listForNumberA.map { it.matches(regExp) }.contains(false) }
    handle {
        val encodedId = ruleValidating.listForNumberA.filter { !it.matches(regExp) }.joinToString(separator = ", ")
        fail(
            errorValidation(
                field = "listForNumberA",
                violationCode = "badFormat",
                description = "value $encodedId must contain only digit and symbols +*?-"
            )
        )
    }
}

fun ICorChainDsl<VafsContext>.validateListNumberBValueProperFormat(title: String) = worker {
    this.title = title

    val regExp = Regex("^[0-9+*?-]+$")
    on { ruleValidating.listForNumberB.map { it.matches(regExp) }.contains(false) }
    handle {
        val encodedId = ruleValidating.listForNumberB.filter { !it.matches(regExp) }.joinToString(separator = ", ")
        fail(
            errorValidation(
                field = "listForNumberB",
                violationCode = "badFormat",
                description = "value $encodedId must contain only digit and symbols +*?-"
            )
        )
    }
}
