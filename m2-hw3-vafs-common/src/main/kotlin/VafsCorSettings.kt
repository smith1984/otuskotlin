package ru.beeline.vafs.common

import ru.beeline.vafs.common.repo.IRuleRepository
import ru.beeline.vafs.logging.common.LoggerProvider

data class VafsCorSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
    val repoStub: IRuleRepository = IRuleRepository.NONE,
    val repoTest: IRuleRepository = IRuleRepository.NONE,
    val repoProd: IRuleRepository = IRuleRepository.NONE,
) {
    companion object {
        val NONE = VafsCorSettings()
    }
}
