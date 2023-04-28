package ru.beeline.vafs.biz.groups

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.VafsCommand
import ru.beeline.vafs.common.models.VafsState
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.chain

fun ICorChainDsl<VafsContext>.operation(title: String, command: VafsCommand, block: ICorChainDsl<VafsContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { this.command == command && state == VafsState.RUNNING }
}
