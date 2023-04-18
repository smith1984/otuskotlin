package ru.beeline.vafs.common.helpers

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.VafsError

fun Throwable.asVafsError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = VafsError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

fun VafsContext.addError(vararg error: VafsError) = errors.addAll(error)
