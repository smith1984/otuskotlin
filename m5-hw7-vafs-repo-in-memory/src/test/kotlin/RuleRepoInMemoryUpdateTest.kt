package ru.beeline.vafs.repo.inmemory

import ru.beeline.vafs.common.repo.IRuleRepository
import ru.beeline.vafs.repository.test.RepoRuleUpdateTest


class RuleRepoInMemoryUpdateTest : RepoRuleUpdateTest() {
    override val repo: IRuleRepository = RuleRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}
