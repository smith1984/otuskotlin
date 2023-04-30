package ru.beeline.vafs.common

import ru.beeline.vafs.logging.common.LoggerProvider

data class VafsCorSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
) {
    companion object {
        val NONE = VafsCorSettings()
    }
}
