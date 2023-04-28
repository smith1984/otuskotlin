package ru.beeline.vafs.biz.validation

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.helpers.errorValidation
import ru.beeline.vafs.common.helpers.fail
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.worker

fun ICorChainDsl<VafsContext>.validateDescriptionHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { ruleValidating.description.isNotEmpty() && ! ruleValidating.description.contains(regExp) }
    handle {
        fail(
            errorValidation(
            field = "description",
            violationCode = "noContent",
            description = "field must contain letters"
        )
        )
    }
}
