package ru.beeline.vafs.biz.workers

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.VafsState
import ru.beeline.vafs.common.stubs.VafsStubs
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.worker
import ru.beeline.vafs.stub.VafsRuleStub

fun ICorChainDsl<VafsContext>.stubSearchSuccess(title: String) = worker {
    this.title = title
    on { stubCase == VafsStubs.SUCCESS && state == VafsState.RUNNING }
    handle {
        state = VafsState.FINISHING
        rulesResponse.addAll(VafsRuleStub.prepareSearchList(ruleFilterRequest.searchString))
    }
}
