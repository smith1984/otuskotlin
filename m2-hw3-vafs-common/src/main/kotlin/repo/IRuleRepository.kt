package ru.beeline.vafs.common.repo

interface IRuleRepository {
    suspend fun createRule(rq: DbRuleRequest): DbRuleResponse
    suspend fun readRule(rq: DbRuleIdRequest): DbRuleResponse
    suspend fun updateRule(rq: DbRuleRequest): DbRuleResponse
    suspend fun deleteRule(rq: DbRuleIdRequest): DbRuleResponse
    suspend fun searchRule(rq: DbRuleFilterRequest): DbRulesResponse
    companion object {
        val NONE = object : IRuleRepository {
            override suspend fun createRule(rq: DbRuleRequest): DbRuleResponse {
                TODO("Not yet implemented")
            }

            override suspend fun readRule(rq: DbRuleIdRequest): DbRuleResponse {
                TODO("Not yet implemented")
            }

            override suspend fun updateRule(rq: DbRuleRequest): DbRuleResponse {
                TODO("Not yet implemented")
            }

            override suspend fun deleteRule(rq: DbRuleIdRequest): DbRuleResponse {
                TODO("Not yet implemented")
            }

            override suspend fun searchRule(rq: DbRuleFilterRequest): DbRulesResponse {
                TODO("Not yet implemented")
            }
        }
    }
}
