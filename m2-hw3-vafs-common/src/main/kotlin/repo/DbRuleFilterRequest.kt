package ru.beeline.vafs.common.repo

import ru.beeline.vafs.common.models.VafsUserId


data class DbRuleFilterRequest(
    val descriptionFilter: String = "",
    val userId: VafsUserId = VafsUserId.NONE,
)
