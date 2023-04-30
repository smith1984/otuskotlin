package ru.beeline.vafs.biz.validation

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.helpers.errorValidation
import ru.beeline.vafs.common.helpers.fail
import ru.beeline.vafs.common.models.VafsRuleId
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.worker


fun ICorChainDsl<VafsContext>.validateIdProperFormat(title: String) = worker {
    this.title = title

    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { ruleValidating.id != VafsRuleId.NONE && ! ruleValidating.id.asString().matches(regExp) }
    handle {
        val encodedId = ruleValidating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
            field = "id",
            violationCode = "badFormat",
            description = "value $encodedId must contain only letters and numbers"
        )
        )
    }
}
