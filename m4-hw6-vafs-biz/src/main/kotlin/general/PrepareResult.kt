package ru.beeline.vafs.biz.general

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.VafsState
import ru.beeline.vafs.common.models.VafsWorkMode
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.worker

fun ICorChainDsl<VafsContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != VafsWorkMode.STUB }
    handle {
        ruleResponse = ruleRepoDone
        rulesResponse = rulesRepoDone
        state = when (val st = state) {
            VafsState.RUNNING -> VafsState.FINISHING
            else -> st
        }
    }
}
