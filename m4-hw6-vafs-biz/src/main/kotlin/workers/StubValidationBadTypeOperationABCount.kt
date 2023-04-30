package ru.beeline.vafs.biz.workers

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.VafsError
import ru.beeline.vafs.common.models.VafsState
import ru.beeline.vafs.common.stubs.VafsStubs
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.worker

fun ICorChainDsl<VafsContext>.stubValidationBadTypeOperationABCount(title: String) = worker {
    this.title = title
    on { stubCase == VafsStubs.BAD_TYPE_OPERATION_AB_COUNT && state == VafsState.RUNNING }
    handle {
        state = VafsState.FAILING
        this.errors.add(
            VafsError(
                group = "validation",
                code = "validation-typeOperationABCount",
                field = "typeOperationABCount",
                message = "Wrong typeOperationABCount field"
            )
        )
    }
}
