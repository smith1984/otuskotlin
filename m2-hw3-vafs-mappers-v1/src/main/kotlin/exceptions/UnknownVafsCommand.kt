package ru.beeline.vafs.mappers.exceptions

import ru.beeline.vafs.common.models.VafsCommand

class UnknownVafsCommand(command: VafsCommand) : Throwable("Wrong command $command at mapping toTransport stage")
