package ru.beeline.vafs.repository.test

import ru.beeline.vafs.common.models.*

abstract class BaseInitRules(val op: String): IInitObjects<VafsRule> {

    open val lockOld: VafsRuleLock = VafsRuleLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: VafsRuleLock = VafsRuleLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        userId: VafsUserId = VafsUserId("operator-123"),
        lock: VafsRuleLock = lockOld,
    ) = VafsRule(
        id = VafsRuleId("rule-repo-$op-$suf"),
        description = "$suf rule description",
        priority = 1000,
        listForNumberA = listOf("79995551111"),
        typeOperationA = VafsTypeOperationList.EXCLUDE,
        listForNumberB = listOf("79995551113", "79995551112"),
        typeOperationB = VafsTypeOperationList.EXCLUDE,
        typeOperationCount = VafsTypeOperationCount.LESS,
        targetCount = 3000,
        valueIsTrue = false,
        typeOperationAB = VafsTypeOperationBool.AND,
        typeOperationABCount = VafsTypeOperationBool.AND,
        userId = userId,
        lock = lock,
    )
}
