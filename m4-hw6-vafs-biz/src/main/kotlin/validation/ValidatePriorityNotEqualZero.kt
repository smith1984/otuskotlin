package ru.beeline.vafs.biz.validation

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.helpers.errorValidation
import ru.beeline.vafs.common.helpers.fail
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.worker


fun ICorChainDsl<VafsContext>.validatePriorityNotEqualZero(title: String) = worker {
    this.title = title
    on { ruleValidating.priority == 0 }
    handle {
        fail(
            errorValidation(
                field = "priority",
                violationCode = "zeroValue",
                description = "field must not equal zero"
            )
        )
    }
}
