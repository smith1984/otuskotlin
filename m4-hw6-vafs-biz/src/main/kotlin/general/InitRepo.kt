package ru.beeline.vafs.biz.general

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.helpers.errorAdministration
import ru.beeline.vafs.common.helpers.fail
import ru.beeline.vafs.common.models.VafsWorkMode
import ru.beeline.vafs.common.repo.IRuleRepository
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.worker


fun ICorChainDsl<VafsContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от запрошенного режима работы        
    """.trimIndent()
    handle {
        ruleRepo = when {
            workMode == VafsWorkMode.TEST -> settings.repoTest
            workMode == VafsWorkMode.STUB -> settings.repoStub
            else -> settings.repoProd
        }
        if (workMode != VafsWorkMode.STUB && ruleRepo == IRuleRepository.NONE) fail(
            errorAdministration(
                field = "repo",
                violationCode = "dbNotConfigured",
                description = "The database is unconfigured for chosen workmode ($workMode). " +
                        "Please, contact the administrator staff"
            )
        )
    }
}
