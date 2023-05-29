package ru.beeline.vafs.ktor.plugins

import io.ktor.server.application.*
import ru.beeline.vafs.biz.VafsRuleProcessor
import ru.beeline.vafs.common.VafsCorSettings
import ru.beeline.vafs.ktor.VafsAppSettings
import ru.beeline.vafs.ktor.config.KtorAuthConfig
import ru.beeline.vafs.logging.common.LoggerProvider
import ru.beeline.vafs.logging.logback.loggerLogback
import ru.beeline.vafs.repository.stub.RuleRepoStub

fun Application.initAppSettings(): VafsAppSettings {

    val corSettings = VafsCorSettings(
        loggerProvider = getLoggerProviderConf(),
        repoTest = getDatabaseConf(RuleDbType.TEST),
        repoProd = getDatabaseConf(RuleDbType.PROD),
        repoStub = RuleRepoStub(),
    )
    return VafsAppSettings(
        corSettings = corSettings,
        processor = VafsRuleProcessor(corSettings),
        auth = KtorAuthConfig(environment.config)
    )
}

fun getLoggerProviderConf(): LoggerProvider = LoggerProvider {
    loggerLogback(it)
}
