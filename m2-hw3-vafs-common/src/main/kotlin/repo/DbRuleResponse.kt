package ru.beeline.vafs.common.repo

import ru.beeline.vafs.common.models.VafsError
import ru.beeline.vafs.common.models.VafsRule

data class DbRuleResponse(
    override val data: VafsRule?,
    override val isSuccess: Boolean,
    override val errors: List<VafsError> = emptyList()
): IDbResponse<VafsRule> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbRuleResponse(null, true)
        fun success(result: VafsRule) = DbRuleResponse(result, true)
        fun error(errors: List<VafsError>) = DbRuleResponse(null, false, errors)
        fun error(error: VafsError) = DbRuleResponse(null, false, listOf(error))
    }
}
