package ru.beeline.vafs.repo.inmemory

import ru.beeline.vafs.repository.test.RepoRuleCreateTest

class RuleRepoInMemoryCreateTest : RepoRuleCreateTest() {
    override val repo = RuleRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}