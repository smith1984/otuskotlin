package ru.beeline.vafs.biz.validation

import ru.beeline.vafs.biz.VafsRuleProcessor
import ru.beeline.vafs.common.VafsCorSettings
import ru.beeline.vafs.common.models.VafsCommand
import ru.beeline.vafs.repository.stub.RuleRepoStub
import ru.beeline.vafs.stub.VafsRuleStub
import kotlin.test.Test

class BizValidationReadTest {

    private val command = VafsCommand.READ
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

}

