package ru.beeline.vafs.common.helpers

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.exceptions.RepoConcurrencyException
import ru.beeline.vafs.common.models.VafsError
import ru.beeline.vafs.common.models.VafsRuleLock
import ru.beeline.vafs.common.models.VafsState

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

fun VafsContext.fail(error: VafsError) {
    addError(error)
    state = VafsState.FAILING
}

fun errorValidation(
    field: String,
    violationCode: String,
    description: String,
    level: VafsError.Level = VafsError.Level.ERROR,
) = VafsError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

fun errorAdministration(
    field: String = "",
    violationCode: String,
    description: String,
    exception: Exception? = null,
    level: VafsError.Level = VafsError.Level.ERROR,
) = VafsError(
    field = field,
    code = "administration-$violationCode",
    group = "administration",
    message = "Microservice management error: $description",
    level = level,
    exception = exception,
)

fun errorRepoConcurrency(
    expectedLock: VafsRuleLock,
    actualLock: VafsRuleLock?,
    exception: Exception? = null,
) = VafsError(
    field = "lock",
    code = "concurrency",
    group = "repo",
    message = "The object has been changed concurrently by another user or process",
    exception = exception ?: RepoConcurrencyException(expectedLock, actualLock),
)
