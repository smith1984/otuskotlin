package ru.beeline.vafs.biz

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.beeline.vafs.common.models.VafsCommand
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationDeleteTest {

    private val command = VafsCommand.DELETE
    private val processor by lazy { VafsRuleProcessor() }

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)


}

