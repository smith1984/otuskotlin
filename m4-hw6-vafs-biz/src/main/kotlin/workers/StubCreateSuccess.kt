package ru.beeline.vafs.biz.workers

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.common.stubs.VafsStubs
import ru.beeline.vafs.cor.ICorChainDsl
import ru.beeline.vafs.cor.worker
import ru.beeline.vafs.stub.VafsRuleStub

fun ICorChainDsl<VafsContext>.stubCreateSuccess(title: String) = worker {
    this.title = title
    on { stubCase == VafsStubs.SUCCESS && state == VafsState.RUNNING }
    handle {
        state = VafsState.FINISHING
        val stub = VafsRuleStub.prepareResult {
            ruleRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
            ruleRequest.userId.takeIf { it != VafsUserId.NONE }?.also { this.userId = it }
            ruleRequest.priority.takeIf { it != 0 }?.also { this.priority = it }
            ruleRequest.listForNumberA.takeIf { it.isNotEmpty() }?.also { this.listForNumberA = it }
            ruleRequest.typeOperationA.takeIf { it == VafsTypeOperationList.NONE }?.also { this.typeOperationA = it }
            ruleRequest.listForNumberB.takeIf { it.isNotEmpty() }?.also { this.listForNumberB = it }
            ruleRequest.typeOperationB.takeIf { it == VafsTypeOperationList.NONE }?.also { this.typeOperationB = it }
            ruleRequest.typeOperationCount.takeIf { it == VafsTypeOperationCount.NONE }?.also { this.typeOperationCount = it }
            ruleRequest.targetCount.takeIf { it != 0 }?.also { this.targetCount = it }
            ruleRequest.valueIsTrue.takeIf { !it }?.also { this.valueIsTrue = it }
            ruleRequest.typeOperationAB.takeIf { it == VafsTypeOperationBool.NONE }?.also { this.typeOperationAB = it }
            ruleRequest.typeOperationABCount.takeIf { it == VafsTypeOperationBool.NONE }?.also { this.typeOperationABCount = it }
        }
        ruleResponse = stub
    }
}
