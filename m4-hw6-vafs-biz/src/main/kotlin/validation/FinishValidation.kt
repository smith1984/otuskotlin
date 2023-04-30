package ru.beeline.vafs.biz.validation

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.VafsState
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.worker


fun ICorChainDsl<VafsContext>.finishRuleValidation(title: String) = worker {
    this.title = title
    on { state == VafsState.RUNNING }
    handle {
        ruleValidated = ruleValidating
    }
}

fun ICorChainDsl<VafsContext>.finishRuleFilterValidation(title: String) = worker {
    this.title = title
    on { state == VafsState.RUNNING }
    handle {
        ruleFilterValidated = ruleFilterValidating
    }
}
