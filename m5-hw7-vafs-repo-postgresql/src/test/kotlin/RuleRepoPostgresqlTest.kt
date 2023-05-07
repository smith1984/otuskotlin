package ru.beeline.vafs.repo.posgresql

import ru.beeline.vafs.common.repo.IRuleRepository
import ru.beeline.vafs.repository.test.*

class RepoRuleSQLCreateTest : RepoRuleCreateTest() {
    override val repo: IRuleRepository = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { lockNew.asString() },
    )
}


class RepoRuleSQLDeleteTest : RepoRuleDeleteTest() {
    override val repo: IRuleRepository = SqlTestCompanion.repoUnderTestContainer(initObjects)
}


class RepoRuleSQLReadTest : RepoRuleReadTest() {
    override val repo: IRuleRepository = SqlTestCompanion.repoUnderTestContainer(initObjects)
}


class RepoRuleSQLSearchTest : RepoRuleSearchTest() {
    override val repo: IRuleRepository = SqlTestCompanion.repoUnderTestContainer(initObjects)
}


class RepoRuleSQLUpdateTest : RepoRuleUpdateTest() {
    override val repo: IRuleRepository = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { lockNew.asString() },
    )
}
