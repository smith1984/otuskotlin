package ru.beeline.vafs.biz.permissions

import ru.beeline.vafs.auth.checkPermitted
import ru.beeline.vafs.auth.resolveRelationsTo
import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.helpers.fail
import ru.beeline.vafs.common.models.VafsError
import ru.beeline.vafs.common.models.VafsState
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.chain
import ru.beeline.vafs.cor.worker

fun ICorChainDsl<VafsContext>.accessValidation(title: String) = chain {
    this.title = title
    description = "Вычисление прав доступа по группе принципала и таблице прав доступа"
    on { state == VafsState.RUNNING }
    worker("Вычисление отношения правила к принципалу") {
        val tmpRelations = ruleRepoRead.resolveRelationsTo(principal)
        ruleRepoRead.principalRelations.clear()
        ruleRepoRead.principalRelations.addAll(tmpRelations)
    }
    worker("Вычисление доступа к правилу") {
        permitted = checkPermitted(command, ruleRepoRead.principalRelations, permissionsChain)
    }
    worker {
        this.title = "Валидация прав доступа"
        description = "Проверка наличия прав для выполнения операции"
        on { !permitted }
        handle {
            fail(VafsError(message = "User is not allowed to perform this operation"))
        }
    }
}

