package ru.beeline.vafs.biz

import ru.beeline.vafs.common.models.VafsCommand
import kotlin.test.Test

class BizValidationReadTest {

    private val command = VafsCommand.READ
    private val processor by lazy { VafsRuleProcessor() }

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)

}

