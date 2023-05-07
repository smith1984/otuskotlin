package ru.beeline.vafs.common.repo

import ru.beeline.vafs.common.models.VafsError
import ru.beeline.vafs.common.models.VafsRule

data class DbRulesResponse(
    override val data: List<VafsRule>?,
    override val isSuccess: Boolean,
    override val errors: List<VafsError> = emptyList(),
): IDbResponse<List<VafsRule>> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbRulesResponse(emptyList(), true)
        fun success(result: List<VafsRule>) = DbRulesResponse(result, true)
        fun error(errors: List<VafsError>) = DbRulesResponse(null, false, errors)
        fun error(error: VafsError) = DbRulesResponse(null, false, listOf(error))
    }
}
