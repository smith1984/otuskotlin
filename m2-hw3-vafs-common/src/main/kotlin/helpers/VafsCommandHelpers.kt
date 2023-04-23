package ru.beeline.vafs.common.helpers

import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.VafsCommand

fun VafsContext.isUpdatableCommand() =
    this.command in listOf(VafsCommand.CREATE, VafsCommand.UPDATE, VafsCommand.DELETE)
