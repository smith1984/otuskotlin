package ru.beeline.vafs.biz.auth

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.beeline.vafs.biz.VafsRuleProcessor
import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.VafsCorSettings
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.common.permissions.VafsRulePermissionOperator
import ru.beeline.vafs.common.permissions.VafsPrincipalModel
import ru.beeline.vafs.common.permissions.VafsUserGroups
import ru.beeline.vafs.repo.inmemory.RuleRepoInMemory
import ru.beeline.vafs.stub.VafsRuleStub
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class RuleCrudAuthTest {
    @Test
    fun createSuccessTest() = runTest {
        val userId = VafsUserId("123")
        val repo = RuleRepoInMemory()
        val processor = VafsRuleProcessor(
            settings = VafsCorSettings(
                repoTest = repo
            )
        )
        val context = VafsContext(
            workMode = VafsWorkMode.TEST,
            ruleRequest = VafsRuleStub.prepareResult {
                permissionsOperator.clear()
                id = VafsRuleId.NONE
            },
            command = VafsCommand.CREATE,
            principal = VafsPrincipalModel(
                id = userId,
                groups = setOf(
                    VafsUserGroups.USER,
                    VafsUserGroups.TEST,
                )
            )
        )

        processor.exec(context)
        assertEquals(VafsState.FINISHING, context.state)
        with(context.ruleResponse) {
            assertTrue { id.asString().isNotBlank() }
            assertContains(permissionsOperator, VafsRulePermissionOperator.READ)
            assertContains(permissionsOperator, VafsRulePermissionOperator.UPDATE)
            assertContains(permissionsOperator, VafsRulePermissionOperator.DELETE)
        }
    }

    @Test
    fun readSuccessTest() = runTest {
        val ruleObj = VafsRuleStub.get()
        val userId = ruleObj.userId
        val ruleId = ruleObj.id
        val repo = RuleRepoInMemory(initObjects = listOf(ruleObj))
        val processor = VafsRuleProcessor(
            settings = VafsCorSettings(
                repoTest = repo
            )
        )
        val context = VafsContext(
            command = VafsCommand.READ,
            workMode = VafsWorkMode.TEST,
            ruleRequest = VafsRule(id = ruleId),
            principal = VafsPrincipalModel(
                id = userId,
                groups = setOf(
                    VafsUserGroups.USER,
                    VafsUserGroups.TEST,
                )
            )
        )
        processor.exec(context)
        assertEquals(VafsState.FINISHING, context.state)
        with(context.ruleResponse) {
            assertTrue { id.asString().isNotBlank() }
            assertContains(permissionsOperator, VafsRulePermissionOperator.READ)
            assertContains(permissionsOperator, VafsRulePermissionOperator.UPDATE)
            assertContains(permissionsOperator, VafsRulePermissionOperator.DELETE)
        }
    }

}
