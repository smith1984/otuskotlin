package ru.beeline.vafs.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class VafsUserId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = VafsUserId("")
    }
}
