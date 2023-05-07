package ru.beeline.vafs.repository.test

import ru.beeline.vafs.common.repo.*

class RuleRepositoryMock(
    private val invokeCreateRule: (DbRuleRequest) -> DbRuleResponse = { DbRuleResponse.MOCK_SUCCESS_EMPTY },
    private val invokeReadRule: (DbRuleIdRequest) -> DbRuleResponse = { DbRuleResponse.MOCK_SUCCESS_EMPTY },
    private val invokeUpdateRule: (DbRuleRequest) -> DbRuleResponse = { DbRuleResponse.MOCK_SUCCESS_EMPTY },
    private val invokeDeleteRule: (DbRuleIdRequest) -> DbRuleResponse = { DbRuleResponse.MOCK_SUCCESS_EMPTY },
    private val invokeSearchRule: (DbRuleFilterRequest) -> DbRulesResponse = { DbRulesResponse.MOCK_SUCCESS_EMPTY },
): IRuleRepository {
    override suspend fun createRule(rq: DbRuleRequest): DbRuleResponse {
        return invokeCreateRule(rq)
    }

    override suspend fun readRule(rq: DbRuleIdRequest): DbRuleResponse {
        return invokeReadRule(rq)
    }

    override suspend fun updateRule(rq: DbRuleRequest): DbRuleResponse {
        return invokeUpdateRule(rq)
    }

    override suspend fun deleteRule(rq: DbRuleIdRequest): DbRuleResponse {
        return invokeDeleteRule(rq)
    }

    override suspend fun searchRule(rq: DbRuleFilterRequest): DbRulesResponse {
        return invokeSearchRule(rq)
    }
}
