package ru.beeline.vafs.ktor.plugins

import io.ktor.server.application.*
import ru.beeline.vafs.biz.VafsRuleProcessor
import ru.beeline.vafs.common.VafsCorSettings
import ru.beeline.vafs.ktor.VafsAppSettings
import ru.beeline.vafs.logging.common.LoggerProvider
import ru.beeline.vafs.logging.logback.loggerLogback

fun Application.initAppSettings(): VafsAppSettings = VafsAppSettings(
    corSettings = VafsCorSettings(
        loggerProvider = getLoggerProviderConf()
    ),
    processor = VafsRuleProcessor(),
)
fun getLoggerProviderConf(): LoggerProvider = LoggerProvider {
    loggerLogback(it)
}