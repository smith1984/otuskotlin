package ru.beeline.vafs.biz.workers

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.helpers.fail
import ru.beeline.vafs.common.models.VafsError
import ru.beeline.vafs.common.models.VafsState
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.worker

fun ICorChainDsl<VafsContext>.stubNoCase(title: String) = worker {
    this.title = title
    on { state == VafsState.RUNNING }
    handle {
        fail(
            VafsError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}
