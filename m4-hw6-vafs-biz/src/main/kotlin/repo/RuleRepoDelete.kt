package ru.beeline.vafs.biz.repo

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.VafsState
import ru.beeline.vafs.common.repo.DbRuleIdRequest
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.worker

fun ICorChainDsl<VafsContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление правила из БД по ID"
    on { state == VafsState.RUNNING }
    handle {
        val request = DbRuleIdRequest(ruleRepoPrepare)
        val result = ruleRepo.deleteRule(request)
        if (!result.isSuccess) {
            state = VafsState.FAILING
            errors.addAll(result.errors)
        }
        ruleRepoDone = ruleRepoRead
    }
}
