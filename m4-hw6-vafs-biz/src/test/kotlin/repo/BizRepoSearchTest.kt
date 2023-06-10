package ru.beeline.vafs.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.beeline.vafs.repository.test.RuleRepositoryMock
import ru.beeline.vafs.biz.VafsRuleProcessor
import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.VafsCorSettings
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.common.permissions.VafsPrincipalModel
import ru.beeline.vafs.common.permissions.VafsUserGroups
import ru.beeline.vafs.common.repo.DbRulesResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoSearchTest {

    private val userId = VafsUserId("operator-321")
    private val uuid = VafsRuleId("10000000-0000-0000-0000-000000000001")
    private val command = VafsCommand.SEARCH
    private val initRule = VafsRule(
        id = uuid,
        description = "rule description",
        priority = 1000,
        listForNumberA = listOf("79995551111"),
        typeOperationA = VafsTypeOperationList.EXCLUDE,
        listForNumberB = listOf("79995551113", "79995551112"),
        typeOperationB = VafsTypeOperationList.EXCLUDE,
        typeOperationCount = VafsTypeOperationCount.LESS,
        targetCount = 3000,
        valueIsTrue = false,
        typeOperationAB = VafsTypeOperationBool.AND,
        typeOperationABCount = VafsTypeOperationBool.AND,
        userId = userId
    )
    private val repo by lazy { RuleRepositoryMock(
        invokeSearchRule = {
            DbRulesResponse(
                isSuccess = true,
                data = listOf(initRule),
            )
        }
    ) }
    private val settings by lazy {
        VafsCorSettings(
            repoTest = repo
        )
    }
    private val processor by lazy { VafsRuleProcessor(settings) }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun repoSearchSuccessTest() = runTest {
        val ctx = VafsContext(
            command = command,
            state = VafsState.NONE,
            workMode = VafsWorkMode.TEST,
            ruleFilterRequest = VafsRuleFilter(
                searchString = "description",
            ),
            principal = VafsPrincipalModel(
                id = userId,
                groups = setOf(
                    VafsUserGroups.USER,
                    VafsUserGroups.TEST,
                )
            ),
        )
        processor.exec(ctx)
        assertEquals(VafsState.FINISHING, ctx.state)
        assertEquals(1, ctx.rulesResponse.size)
    }
}
