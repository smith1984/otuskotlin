package ru.beeline.vafs.biz.permissions

import ru.beeline.vafs.auth.resolveChainPermissions
import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.VafsState
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.worker


fun ICorChainDsl<VafsContext>.chainPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление прав доступа для групп пользователей"

    on { state == VafsState.RUNNING }

    handle {
        permissionsChain.addAll(resolveChainPermissions(principal.groups))
    }
}
