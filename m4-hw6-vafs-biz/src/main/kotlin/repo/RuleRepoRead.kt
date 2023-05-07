package ru.beeline.vafs.biz.repo

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.VafsState
import ru.beeline.vafs.common.repo.DbRuleIdRequest
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.worker

fun ICorChainDsl<VafsContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение правила из БД"
    on { state == VafsState.RUNNING }
    handle {
        val request = DbRuleIdRequest(ruleValidated)
        val result = ruleRepo.readRule(request)
        val resultAd = result.data
        if (result.isSuccess && resultAd != null) {
            ruleRepoRead = resultAd
        } else {
            state = VafsState.FAILING
            errors.addAll(result.errors)
        }
    }
}
