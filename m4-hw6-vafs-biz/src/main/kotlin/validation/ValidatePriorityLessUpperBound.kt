package ru.beeline.vafs.biz.validation

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.helpers.errorValidation
import ru.beeline.vafs.common.helpers.fail
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.worker


fun ICorChainDsl<VafsContext>.validatePriorityLessUpperBound(title: String) = worker {
    this.title = title
    on { ruleValidating.priority > 1000 }
    handle {
        fail(
            errorValidation(
                field = "priority",
                violationCode = "aboveValue",
                description = "field must not exceed the upper bound"
            )
        )
    }
}
