package ru.beeline.vafs.stub


import ru.beeline.vafs.common.models.VafsRule
import ru.beeline.vafs.common.models.VafsRuleId
import ru.beeline.vafs.stub.VafsRuleStubSample.RULE_BASE


object VafsRuleStub {
    fun get(): VafsRule = RULE_BASE.copy()
    
    fun prepareSearchList(filter: String) = listOf(
        generateVafsRule(get(), "rule-123-01", filter),
        generateVafsRule(get(), "rule-123-02", filter),
        generateVafsRule(get(), "rule-123-03", filter),
        generateVafsRule(get(), "rule-123-04", filter),
        generateVafsRule(get(), "rule-123-05", filter),
        generateVafsRule(get(), "rule-123-06", filter),
    )

    fun prepareResult(block: VafsRule.() -> Unit): VafsRule = get().apply(block)
    private fun generateVafsRule(base: VafsRule, id: String, filter: String) = base.copy(
        id = VafsRuleId(id),
        description = "description $filter $id",
    )

}
