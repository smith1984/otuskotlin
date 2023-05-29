package ru.beeline.vafs.ktor

import ru.beeline.vafs.biz.VafsRuleProcessor
import ru.beeline.vafs.common.VafsCorSettings
import ru.beeline.vafs.ktor.config.KtorAuthConfig

data class VafsAppSettings(
    val corSettings: VafsCorSettings,
    val processor: VafsRuleProcessor = VafsRuleProcessor(settings = corSettings),
    val auth: KtorAuthConfig = KtorAuthConfig.NONE,
    )
