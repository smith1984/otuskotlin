package ru.beeline.vafs.biz.repo

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.VafsState
import ru.beeline.vafs.common.repo.DbRuleRequest
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.worker

fun ICorChainDsl<VafsContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Добавление правила в БД"
    on { state == VafsState.RUNNING }
    handle {
        val request = DbRuleRequest(ruleRepoPrepare)
        val result = ruleRepo.createRule(request)
        val resultRule = result.data
        if (result.isSuccess && resultRule != null) {
            ruleRepoDone = resultRule
        } else {
            state = VafsState.FAILING
            errors.addAll(result.errors)
        }
    }
}
