package ru.beeline.vafs.biz

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.common.stubs.VafsStubs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

@OptIn(ExperimentalCoroutinesApi::class)
class RuleSearchStubTest {

    private val processor = VafsRuleProcessor()
    val filter = VafsRuleFilter(searchString = "rule")

    @Test
    fun read() = runTest {

        val ctx = VafsContext(
            command = VafsCommand.SEARCH,
            state = VafsState.NONE,
            workMode = VafsWorkMode.STUB,
            stubCase = VafsStubs.SUCCESS,
            ruleFilterRequest = filter,
        )
        processor.exec(ctx)
        assertTrue(ctx.rulesResponse.size > 1)
        val first = ctx.rulesResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.description.contains(filter.searchString))
    }

    @Test
    fun badId() = runTest {
        val ctx = VafsContext(
            command = VafsCommand.SEARCH,
            state = VafsState.NONE,
            workMode = VafsWorkMode.STUB,
            stubCase = VafsStubs.BAD_ID,
            ruleFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(VafsRule(), ctx.ruleResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = VafsContext(
            command = VafsCommand.SEARCH,
            state = VafsState.NONE,
            workMode = VafsWorkMode.STUB,
            stubCase = VafsStubs.DB_ERROR,
            ruleFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(VafsRule(), ctx.ruleResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = VafsContext(
            command = VafsCommand.SEARCH,
            state = VafsState.NONE,
            workMode = VafsWorkMode.STUB,
            stubCase = VafsStubs.BAD_PRIORITY,
            ruleFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(VafsRule(), ctx.ruleResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
