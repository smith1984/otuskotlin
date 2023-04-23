package ru.beeline.vafs.stub

import ru.beeline.vafs.common.models.*

object VafsRuleStubSample {
    val RULE_BASE: VafsRule
        get() = VafsRule(
            id = VafsRuleId("123987"),
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
            userId = VafsUserId("operator-1"),

            permissionsOperator = mutableSetOf(
                VafsRulePermissionOperator.READ,
                VafsRulePermissionOperator.UPDATE,
                VafsRulePermissionOperator.DELETE,
            )
        )

    val RULE_PERMISSION_READ = RULE_BASE.copy(
        permissionsOperator = mutableSetOf(
            VafsRulePermissionOperator.READ,
        )
    )
}