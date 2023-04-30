package ru.beeline.vafs.biz.workers

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.VafsState
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.worker


fun ICorChainDsl<VafsContext>.initStatus(title: String) = worker() {
    this.title = title
    on { state == VafsState.NONE }
    handle { state = VafsState.RUNNING }
}
