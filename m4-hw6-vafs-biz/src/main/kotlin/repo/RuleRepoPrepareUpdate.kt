package ru.beeline.vafs.biz.repo

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.VafsState
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.worker

fun ICorChainDsl<VafsContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == VafsState.RUNNING }
    handle {
        ruleRepoPrepare = ruleRepoRead.deepCopy().apply {
            description = ruleValidated.description
            priority = ruleValidated.priority
            listForNumberA = ruleValidated.listForNumberA
            typeOperationA = ruleValidated.typeOperationA
            listForNumberB = ruleValidated.listForNumberB
            typeOperationB = ruleValidated.typeOperationB
            typeOperationCount = ruleValidated.typeOperationCount
            targetCount = ruleValidated.targetCount
            valueIsTrue = ruleValidated.valueIsTrue
            typeOperationAB = ruleValidated.typeOperationAB
            typeOperationABCount = ruleValidated.typeOperationABCount
        }
    }
}
