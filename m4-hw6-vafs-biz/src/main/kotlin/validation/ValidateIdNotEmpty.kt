package ru.beeline.vafs.biz.validation

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.helpers.errorValidation
import ru.beeline.vafs.common.helpers.fail
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.worker


fun ICorChainDsl<VafsContext>.validateIdNotEmpty(title: String) = worker {
    this.title = title
    on { ruleValidating.id.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "id",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
