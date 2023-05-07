package ru.beeline.vafs.common.models


@JvmInline
value class VafsRuleLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = VafsRuleLock("")
    }
}
