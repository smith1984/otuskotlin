package ru.beeline.vafs.biz.repo

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.VafsState
import ru.beeline.vafs.common.repo.DbRuleFilterRequest
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.worker

fun ICorChainDsl<VafsContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск правил в БД по фильтру"
    on { state == VafsState.RUNNING }
    handle {
        val request = DbRuleFilterRequest(
            descriptionFilter = ruleFilterValidated.searchString,
            userId = ruleFilterValidated.userId,
        )
        val result = ruleRepo.searchRule(request)
        val resultRules = result.data
        if (result.isSuccess && resultRules != null) {
            rulesRepoDone = resultRules.toMutableList()
        } else {
            state = VafsState.FAILING
            errors.addAll(result.errors)
        }
    }
}
