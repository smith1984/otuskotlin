package ru.beeline.vafs.biz.validation

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.helpers.errorValidation
import ru.beeline.vafs.common.helpers.fail
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.worker


fun ICorChainDsl<VafsContext>.validateListNumberANotEmpty(title: String) = worker {
    this.title = title
    on { ruleValidating.listForNumberA.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "listForNumberA",
                violationCode = "empty",
                description = "field must not contain empty list"
            )
        )
    }
}

fun ICorChainDsl<VafsContext>.validateListNumberBNotEmpty(title: String) = worker {
    this.title = title
    on { ruleValidating.listForNumberB.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "listForNumberB",
                violationCode = "empty",
                description = "field must not contain empty list"
            )
        )
    }
}
