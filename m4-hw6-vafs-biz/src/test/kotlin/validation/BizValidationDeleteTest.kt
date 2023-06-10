package ru.beeline.vafs.biz.validation

import ru.beeline.vafs.biz.VafsRuleProcessor
import ru.beeline.vafs.common.VafsCorSettings
import ru.beeline.vafs.common.models.VafsCommand
import ru.beeline.vafs.repository.stub.RuleRepoStub
import ru.beeline.vafs.stub.VafsRuleStub
import validation.validationLockCorrect
import validation.validationLockEmpty
import validation.validationLockFormat
import validation.validationLockTrim
import kotlin.test.Test

class BizValidationDeleteTest {

    private val command = VafsCommand.DELETE
    private val settings by lazy {
        VafsCorSettings(
            repoTest = RuleRepoStub()
        )
    }
    private val processor by lazy { VafsRuleProcessor(settings) }

    private val stub = VafsRuleStub.get()

    @Test fun correctId() = validationIdCorrect(command, processor, stub)
    @Test fun trimId() = validationIdTrim(command, processor, stub)
    @Test fun emptyId() = validationIdEmpty(command, processor, stub)
    @Test fun badFormatId() = validationIdFormat(command, processor, stub)

    @Test fun correctLock() = validationLockCorrect(command, processor, stub)
    @Test fun trimLock() = validationLockTrim(command, processor, stub)
    @Test fun emptyLock() = validationLockEmpty(command, processor, stub)
    @Test fun badFormatLock() = validationLockFormat(command, processor, stub)

}

