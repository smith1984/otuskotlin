package ru.beeline.vafs.repo.inmemory

import ru.beeline.vafs.common.repo.IRuleRepository
import ru.beeline.vafs.repository.test.RepoRuleReadTest

class RuleRepoInMemoryReadTest: RepoRuleReadTest() {
    override val repo: IRuleRepository = RuleRepoInMemory(
        initObjects = initObjects
    )
}
