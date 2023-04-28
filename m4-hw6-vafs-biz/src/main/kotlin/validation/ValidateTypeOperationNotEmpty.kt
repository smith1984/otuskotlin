package ru.beeline.vafs.biz.validation

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.helpers.errorValidation
import ru.beeline.vafs.common.helpers.fail
import ru.beeline.vafs.common.models.VafsTypeOperationBool
import ru.beeline.vafs.common.models.VafsTypeOperationCount
import ru.beeline.vafs.common.models.VafsTypeOperationList
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.worker


fun ICorChainDsl<VafsContext>.validateTypeOperationANotEmpty(title: String) = worker {
    this.title = title
    on { ruleValidating.typeOperationA == VafsTypeOperationList.NONE }
    handle {
        fail(
            errorValidation(
                field = "typeOperationA",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}

fun ICorChainDsl<VafsContext>.validateTypeOperationBNotEmpty(title: String) = worker {
    this.title = title
    on { ruleValidating.typeOperationB == VafsTypeOperationList.NONE }
    handle {
        fail(
            errorValidation(
                field = "typeOperationB",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}

fun ICorChainDsl<VafsContext>.validateTypeOperationCountNotEmpty(title: String) = worker {
    this.title = title
    on { ruleValidating.typeOperationCount == VafsTypeOperationCount.NONE }
    handle {
        fail(
            errorValidation(
                field = "typeOperationCount",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}

fun ICorChainDsl<VafsContext>.validateTypeOperationABNotEmpty(title: String) = worker {
    this.title = title
    on { ruleValidating.typeOperationAB == VafsTypeOperationBool.NONE }
    handle {
        fail(
            errorValidation(
                field = "typeOperationAB",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}

fun ICorChainDsl<VafsContext>.validateTypeOperationABCountNotEmpty(title: String) = worker {
    this.title = title
    on { ruleValidating.typeOperationABCount == VafsTypeOperationBool.NONE }
    handle {
        fail(
            errorValidation(
                field = "typeOperationABCount",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}