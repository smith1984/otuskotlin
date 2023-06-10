package ru.beeline.vafs.biz.permissions

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.VafsState
import ru.beeline.vafs.common.models.VafsSearchPermissions
import ru.beeline.vafs.common.permissions.VafsUserPermissions
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.chain
import ru.beeline.vafs.cor.worker

fun ICorChainDsl<VafsContext>.searchTypes(title: String) = chain {
    this.title = title
    description = "Добавление ограничений в поисковый запрос согласно правам доступа и др. политикам"
    on { state == VafsState.RUNNING }
    worker("Определение типа поиска") {
        val tmpPermissions = setOfNotNull(
            VafsSearchPermissions.OWN.takeIf { permissionsChain.contains(VafsUserPermissions.SEARCH_OWN) },
            VafsSearchPermissions.PUBLIC.takeIf { permissionsChain.contains(VafsUserPermissions.SEARCH_PUBLIC) },
            VafsSearchPermissions.REGISTERED.takeIf { permissionsChain.contains(VafsUserPermissions.SEARCH_REGISTERED) },
        )
        ruleFilterValidated.searchPermissions.clear()
        ruleFilterValidated.searchPermissions.addAll(tmpPermissions)
    }
}
