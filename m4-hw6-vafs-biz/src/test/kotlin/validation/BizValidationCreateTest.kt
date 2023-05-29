package ru.beeline.vafs.biz.validation

import ru.beeline.vafs.biz.VafsRuleProcessor
import ru.beeline.vafs.common.VafsCorSettings
import ru.beeline.vafs.common.models.VafsCommand
import ru.beeline.vafs.repository.stub.RuleRepoStub
import ru.beeline.vafs.stub.VafsRuleStub
import kotlin.test.Test

class BizValidationCreateTest {

    private val command = VafsCommand.CREATE
    private val settings by lazy {
        VafsCorSettings(
            repoTest = RuleRepoStub()
        )
    }
    private val processor by lazy { VafsRuleProcessor(settings) }

    private val stub = VafsRuleStub.getNew()

    @Test fun correctDescription() = validationDescriptionCorrect(command, processor, stub)
    @Test fun trimDescription() = validationDescriptionTrim(command, processor, stub)
    @Test fun emptyDescription() = validationDescriptionEmpty(command, processor, stub)
    @Test fun badSymbolsDescription() = validationDescriptionSymbols(command, processor, stub)

    @Test fun correctPriority() = validationPriorityCorrect(command, processor, stub)
    @Test fun emptyPriority() = validationPriorityEmpty(command, processor, stub)
    @Test fun priorityLessLowerBound() = validationPriorityLessLowerBound(command, processor, stub)
    @Test fun priorityMoreUpperBound() = validationPriorityMoreUpperBound(command, processor, stub)

    @Test fun correctListNumberA() = validationListNumberACorrect(command, processor, stub)
    @Test fun trimValueListNumberA() = validationListNumberATrim(command, processor, stub)
    @Test fun emptyListNumberA() = validationListNumberAEmpty(command, processor, stub)
    @Test fun badSymbolsValueListNumberA() = validationListNumberASymbols(command, processor, stub)

    @Test fun correctListNumberB() = validationListNumberBCorrect(command, processor, stub)
    @Test fun trimValueListNumberB() = validationListNumberBTrim(command, processor, stub)
    @Test fun emptyListNumberB() = validationListNumberBEmpty(command, processor, stub)
    @Test fun badSymbolsValueListNumberB() = validationListNumberBSymbols(command, processor, stub)

    @Test fun correctTypeOperationA() = validationTypeOperationACorrect(command, processor, stub)
    @Test fun emptyTypeOperationA() = validationTypeOperationAEmpty(command, processor, stub)

    @Test fun correctTypeOperationB() = validationTypeOperationBCorrect(command, processor, stub)
    @Test fun emptyTypeOperationB() = validationTypeOperationBEmpty(command, processor, stub)

    @Test fun correctTypeOperationAB() = validationTypeOperationABCorrect(command, processor, stub)
    @Test fun emptyTypeOperationAB() = validationTypeOperationABEmpty(command, processor, stub)

    @Test fun correctTypeOperationABCount() = validationTypeOperationABCountCorrect(command, processor, stub)
    @Test fun emptyTypeOperationABCount() = validationTypeOperationABCountEmpty(command, processor, stub)


    @Test fun correctTypeOperationCount() = validationTypeOperationCountCorrect(command, processor, stub)
    @Test fun emptyTypeOperationCount() = validationTypeOperationCountEmpty(command, processor, stub)

    @Test fun correctTargetCount() = validationTargetCountCorrect(command, processor, stub)
    @Test fun emptyTargetCount() = validationTargetCountEmpty(command, processor, stub)
    @Test fun targetCountMoreUpperBound() = validationTargetCountLessLowerBound(command, processor, stub)
}

