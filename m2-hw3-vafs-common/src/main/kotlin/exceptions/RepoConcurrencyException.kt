package ru.beeline.vafs.common.exceptions

import ru.beeline.vafs.common.models.VafsRuleLock

class RepoConcurrencyException(expectedLock: VafsRuleLock, actualLock: VafsRuleLock?): RuntimeException(
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)
