package ru.beeline.vafs.biz.groups

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.VafsState
import ru.beeline.vafs.common.models.VafsWorkMode
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.chain

fun ICorChainDsl<VafsContext>.stubs(title: String, block: ICorChainDsl<VafsContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == VafsWorkMode.STUB && state == VafsState.RUNNING }
}
