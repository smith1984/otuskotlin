package ru.beeline.vafs.biz.validation

import ru.beeline.vafs.biz.VafsRuleProcessor
import ru.beeline.vafs.common.VafsCorSettings
import ru.beeline.vafs.common.models.VafsCommand
import ru.beeline.vafs.repository.stub.RuleRepoStub
import validation.validationLockCorrect
import validation.validationLockEmpty
import validation.validationLockFormat
import validation.validationLockTrim
import kotlin.test.Test

class BizValidationUpdateTest {

    private val command = VafsCommand.UPDATE
    private val settings by lazy {
        VafsCorSettings(
            repoTest = RuleRepoStub()
        )
    }
    private val processor by lazy { VafsRuleProcessor(settings) }

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)


    @Test fun correctDescription() = validationDescriptionCorrect(command, processor)
    @Test fun trimDescription() = validationDescriptionTrim(command, processor)
    @Test fun emptyDescription() = validationDescriptionEmpty(command, processor)
    @Test fun badSymbolsDescription() = validationDescriptionSymbols(command, processor)

    @Test fun correctPriority() = validationPriorityCorrect(command, processor)
    @Test fun emptyPriority() = validationPriorityEmpty(command, processor)
    @Test fun priorityLessLowerBound() = validationPriorityLessLowerBound(command, processor)
    @Test fun priorityMoreUpperBound() = validationPriorityMoreUpperBound(command, processor)

    @Test fun correctListNumberA() = validationListNumberACorrect(command, processor)
    @Test fun trimValueListNumberA() = validationListNumberATrim(command, processor)
    @Test fun emptyListNumberA() = validationListNumberAEmpty(command, processor)
    @Test fun badSymbolsValueListNumberA() = validationListNumberASymbols(command, processor)

    @Test fun correctListNumberB() = validationListNumberBCorrect(command, processor)
    @Test fun trimValueListNumberB() = validationListNumberBTrim(command, processor)
    @Test fun emptyListNumberB() = validationListNumberBEmpty(command, processor)
    @Test fun badSymbolsValueListNumberB() = validationListNumberBSymbols(command, processor)

    @Test fun correctTypeOperationA() = validationTypeOperationACorrect(command, processor)
    @Test fun emptyTypeOperationA() = validationTypeOperationAEmpty(command, processor)

    @Test fun correctTypeOperationB() = validationTypeOperationBCorrect(command, processor)
    @Test fun emptyTypeOperationB() = validationTypeOperationBEmpty(command, processor)

    @Test fun correctTypeOperationAB() = validationTypeOperationABCorrect(command, processor)
    @Test fun emptyTypeOperationAB() = validationTypeOperationABEmpty(command, processor)

    @Test fun correctTypeOperationABCount() = validationTypeOperationABCountCorrect(command, processor)
    @Test fun emptyTypeOperationABCount() = validationTypeOperationABCountEmpty(command, processor)


    @Test fun correctTypeOperationCount() = validationTypeOperationCountCorrect(command, processor)
    @Test fun emptyTypeOperationCount() = validationTypeOperationCountEmpty(command, processor)

    @Test fun correctTargetCount() = validationTargetCountCorrect(command, processor)
    @Test fun emptyTargetCount() = validationTargetCountEmpty(command, processor)
    @Test fun targetCountMoreUpperBound() = validationTargetCountLessLowerBound(command, processor)

    @Test fun correctLock() = validationLockCorrect(command, processor)
    @Test fun trimLock() = validationLockTrim(command, processor)
    @Test fun emptyLock() = validationLockEmpty(command, processor)
    @Test fun badFormatLock() = validationLockFormat(command, processor)

}

