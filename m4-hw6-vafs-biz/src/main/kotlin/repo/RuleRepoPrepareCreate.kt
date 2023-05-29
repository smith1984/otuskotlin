package ru.beeline.vafs.biz.repo

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.VafsState
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.worker

fun ICorChainDsl<VafsContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == VafsState.RUNNING }
    handle {
        ruleRepoRead = ruleValidated.copy()
        ruleRepoPrepare = ruleRepoRead
        ruleRepoRead.userId = principal.id
    }
}
