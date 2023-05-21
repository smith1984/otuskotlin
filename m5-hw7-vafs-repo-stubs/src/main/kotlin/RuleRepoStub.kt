package ru.beeline.vafs.repository.stub

import ru.beeline.vafs.common.repo.*
import ru.beeline.vafs.stub.VafsRuleStub

class RuleRepoStub() : IRuleRepository {
    override suspend fun createRule(rq: DbRuleRequest): DbRuleResponse {
        return DbRuleResponse(
            data = VafsRuleStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun readRule(rq: DbRuleIdRequest): DbRuleResponse {
        return DbRuleResponse(
            data = VafsRuleStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun updateRule(rq: DbRuleRequest): DbRuleResponse {
        return DbRuleResponse(
            data = VafsRuleStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun deleteRule(rq: DbRuleIdRequest): DbRuleResponse {
        return DbRuleResponse(
            data = VafsRuleStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun searchRule(rq: DbRuleFilterRequest): DbRulesResponse {
        return DbRulesResponse(
            data = VafsRuleStub.prepareSearchList(filter = ""),
            isSuccess = true,
        )
    }
}
