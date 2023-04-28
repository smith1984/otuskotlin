package ru.beeline.vafs.ktor

import ru.beeline.vafs.biz.VafsRuleProcessor
import ru.beeline.vafs.common.VafsCorSettings

data class VafsAppSettings(
    val corSettings: VafsCorSettings,
    val processor: VafsRuleProcessor,
)
