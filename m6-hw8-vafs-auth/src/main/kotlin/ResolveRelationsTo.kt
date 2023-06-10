package ru.beeline.vafs.auth

import ru.beeline.vafs.common.models.VafsRule
import ru.beeline.vafs.common.models.VafsRuleId
import ru.beeline.vafs.common.permissions.VafsPrincipalModel
import ru.beeline.vafs.common.permissions.VafsPrincipalRelations

fun VafsRule.resolveRelationsTo(principal: VafsPrincipalModel): Set<VafsPrincipalRelations> = setOfNotNull(
    VafsPrincipalRelations.NONE,
    VafsPrincipalRelations.NEW.takeIf { id == VafsRuleId.NONE },
    VafsPrincipalRelations.OWN.takeIf { principal.id == userId },
)
