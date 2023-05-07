package ru.beeline.vafs.common.repo

import ru.beeline.vafs.common.models.VafsError


interface IDbResponse<T> {
    val data: T?
    val isSuccess: Boolean
    val errors: List<VafsError>
}
