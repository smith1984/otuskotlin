package ru.beeline.vafs.biz.validation

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.VafsState
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.chain


fun ICorChainDsl<VafsContext>.validation(block: ICorChainDsl<VafsContext>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == VafsState.RUNNING }
}
