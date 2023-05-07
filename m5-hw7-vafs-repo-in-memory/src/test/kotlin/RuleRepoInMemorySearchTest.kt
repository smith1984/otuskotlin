package ru.beeline.vafs.repo.inmemory

import ru.beeline.vafs.common.repo.IRuleRepository
import ru.beeline.vafs.repository.test.RepoRuleSearchTest


class RuleRepoInMemorySearchTest : RepoRuleSearchTest() {
    override val repo: IRuleRepository = RuleRepoInMemory(
        initObjects = initObjects
    )
}
