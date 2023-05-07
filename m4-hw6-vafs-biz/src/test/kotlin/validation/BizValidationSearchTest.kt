package ru.beeline.vafs.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.beeline.vafs.biz.VafsRuleProcessor
import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.VafsCorSettings
import ru.beeline.vafs.common.models.VafsCommand
import ru.beeline.vafs.common.models.VafsRuleFilter
import ru.beeline.vafs.common.models.VafsState
import ru.beeline.vafs.common.models.VafsWorkMode
import ru.beeline.vafs.repository.stub.RuleRepoStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationSearchTest {

    private val command = VafsCommand.SEARCH
    private val settings by lazy {
        VafsCorSettings(
            repoTest = RuleRepoStub()
        )
    }
    private val processor by lazy { VafsRuleProcessor(settings) }

    @Test
    fun correctEmpty() = runTest {
        val ctx = VafsContext(
            command = command,
            state = VafsState.NONE,
            workMode = VafsWorkMode.TEST,
            ruleFilterRequest = VafsRuleFilter()
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(VafsState.FAILING, ctx.state)
    }
}

