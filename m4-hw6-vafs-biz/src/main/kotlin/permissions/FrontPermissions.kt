package ru.beeline.vafs.biz.permissions

import ru.beeline.vafs.auth.resolveFrontPermissions
import ru.beeline.vafs.auth.resolveRelationsTo
import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.VafsState
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.worker

fun ICorChainDsl<VafsContext>.frontPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление разрешений пользователей для фронтенда"

    on { state == VafsState.RUNNING }

    handle {
        ruleRepoDone.permissionsOperator.addAll(
            resolveFrontPermissions(
                permissionsChain,
                ruleRepoDone.resolveRelationsTo(principal)
            )
        )

        for (rule in rulesRepoDone) {
            rule.permissionsOperator.addAll(
                resolveFrontPermissions(
                    permissionsChain,
                    rule.resolveRelationsTo(principal)
                )
            )
        }
    }
}
