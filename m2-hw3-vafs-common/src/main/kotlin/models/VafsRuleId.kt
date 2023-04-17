package ru.beeline.vafs.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class VafsRuleId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = VafsRuleId("")
    }
}
