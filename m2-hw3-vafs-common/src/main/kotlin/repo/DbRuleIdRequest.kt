package ru.beeline.vafs.common.repo

import ru.beeline.vafs.common.models.VafsRule
import ru.beeline.vafs.common.models.VafsRuleId
import ru.beeline.vafs.common.models.VafsRuleLock

data class DbRuleIdRequest(
    val id: VafsRuleId,
    val lock: VafsRuleLock = VafsRuleLock.NONE,
) {
    constructor(rule: VafsRule): this(rule.id, rule.lock)
}
