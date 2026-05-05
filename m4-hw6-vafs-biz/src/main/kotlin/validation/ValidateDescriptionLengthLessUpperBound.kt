package ru.beeline.vafs.biz.validation

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.helpers.errorValidation
import ru.beeline.vafs.common.helpers.fail
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.worker

private const val MAX_DESCRIPTION_LENGTH = 256

fun ICorChainDsl<VafsContext>.validateDescriptionLengthLessUpperBound(title: String) = worker {
    this.title = title
    on { ruleValidating.description.length > MAX_DESCRIPTION_LENGTH }
    handle {
        fail(
            errorValidation(
                field = "description",
                violationCode = "tooLong",
                description = "field must not exceed $MAX_DESCRIPTION_LENGTH characters"
            )
        )
    }
}
