package ru.beeline.vafs.biz

import ru.beeline.vafs.biz.general.initRepo
import ru.beeline.vafs.biz.general.prepareResult
import ru.beeline.vafs.biz.groups.*
import ru.beeline.vafs.biz.permissions.accessValidation
import ru.beeline.vafs.biz.permissions.chainPermissions
import ru.beeline.vafs.biz.permissions.frontPermissions
import ru.beeline.vafs.biz.permissions.searchTypes
import ru.beeline.vafs.biz.repo.*
import ru.beeline.vafs.biz.validation.*
import ru.beeline.vafs.biz.workers.*
import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.VafsCorSettings
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.cor.*

class VafsRuleProcessor(private val settings: VafsCorSettings = VafsCorSettings()) {
    suspend fun exec(ctx: VafsContext) = BusinessChain.exec(ctx.apply { settings = this@VafsRuleProcessor.settings })

    companion object {
        private val BusinessChain = rootChain<VafsContext> {
            initStatus("Инициализация статуса")
            initRepo("Инициализация репозитория")

            operation("Создание правила", VafsCommand.CREATE) {
                stubs("Обработка стабов") {
                    stubCreateSuccess("Имитация успешной обработки")
                    stubValidationBadDescription("Имитация ошибки валидации описания")
                    stubValidationBadPriority("Имитация ошибки валидации приоритета")
                    stubValidationBadListForNumberA("Имитация ошибки валидации списка для номера A")
                    stubValidationBadTypeOperationA("Имитация ошибки валидации типа операции к списку номера A")
                    stubValidationBadListForNumberB("Имитация ошибки валидации списка для номера B")
                    stubValidationBadTypeOperationB("Имитация ошибки валидации типа операции к списку номера B")
                    stubValidationBadTypeOperationCount("Имитация ошибки валидации типа операции к счетчику правила")
                    stubValidationBadTargetCount("Имитация ошибки валидации целевого значения счётчика")
                    stubValidationBadValueIsTrue("Имитация ошибки валидации булевого значения применяемого к счётчику")
                    stubValidationBadTypeOperationAB("Имитация ошибки валидации типа операции между списками A и B")
                    stubValidationBadTypeOperationABCount("Имитация ошибки валидации типа операции между списками и счётчиком")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в ruleValidating") { ruleValidating = ruleRequest.copy() }
                    worker("Очистка описания") { ruleValidating.description = ruleValidating.description.trim() }
                    worker("Очистка значение в списке номеров A") {
                        ruleValidating.listForNumberA =
                            ruleValidating.listForNumberA.map { it.trim() }.filter { it.isNotEmpty() }
                    }
                    worker("Очистка значение в списке номеров B") {
                        ruleValidating.listForNumberB =
                            ruleValidating.listForNumberB.map { it.trim() }.filter { it.isNotEmpty() }
                    }

                    validateDescriptionNotEmpty("Проверка, что описание не пусто")
                    validateDescriptionHasContent("Проверка символов")
                    validatePriorityNotEqualZero("Проверка, что приоритет не равен 0")
                    validatePriorityLessUpperBound("Проверка, что приоритет меньше верхней границы")
                    validatePriorityMoreLowerBound("Проверка, что приоритет больше нижней границы")
                    validateListNumberANotEmpty("Что список для номера A не пустой")
                    validateListNumberBNotEmpty("Что список для номера B не пустой")
                    validateListNumberAValueProperFormat("Проверка элементов списка A на телефонный формат")
                    validateListNumberBValueProperFormat("Проверка элементов списка B на телефонный формат")
                    validateTypeOperationANotEmpty("Проверка типа операции для списка A на None")
                    validateTypeOperationBNotEmpty("Проверка типа операции для списка B на None")
                    validateTypeOperationCountNotEmpty("Проверка типа операции для счётчика на None")
                    validateTypeOperationABNotEmpty("Проверка типа булевой операции для списков A,B на None")
                    validateTypeOperationABCountNotEmpty("Проверка типа булевой операции для списков A,B и счётчика на None")
                    validateTargetCountMoreLowerBound("Проверка, что значение счётчика не равно 0")
                    validateTargetCountNotEqualZero("Проверка, что значение счётчика больше нижней границы")

                    finishRuleValidation("Завершение проверок")
                }
                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика сохранения"
                    repoPrepareCreate("Подготовка объекта для сохранения")
                    accessValidation("Вычисление прав доступа")
                    repoCreate("Создание правила в БД")
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
            }
            operation("Получить правило", VafsCommand.READ) {
                stubs("Обработка стабов") {
                    stubReadSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с BД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в ruleValidating") { ruleValidating = ruleRequest.copy() }
                    worker("Очистка id") { ruleValidating.id = VafsRuleId(ruleValidating.id.asString().trim()) }

                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")

                    finishRuleValidation("Успешное завершение процедуры валидации")
                }
                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика чтения"
                    repoRead("Чтение правила из БД")
                    accessValidation("Вычисление прав доступа")
                    worker {
                        title = "Подготовка ответа для Read"
                        on { state == VafsState.RUNNING }
                        handle { ruleRepoDone = ruleRepoRead }
                    }
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
            }
            operation("Изменить правило", VafsCommand.UPDATE) {
                stubs("Обработка стабов") {
                    stubUpdateSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubValidationBadDescription("Имитация ошибки валидации описания")
                    stubValidationBadPriority("Имитация ошибки валидации приоритета")
                    stubValidationBadListForNumberA("Имитация ошибки валидации списка для номера A")
                    stubValidationBadTypeOperationA("Имитация ошибки валидации типа операции к списку номера A")
                    stubValidationBadListForNumberB("Имитация ошибки валидации списка для номера B")
                    stubValidationBadTypeOperationB("Имитация ошибки валидации типа операции к списку номера B")
                    stubValidationBadTypeOperationCount("Имитация ошибки валидации типа операции к счетчику правила")
                    stubValidationBadTargetCount("Имитация ошибки валидации целевого значения счётчика")
                    stubValidationBadValueIsTrue("Имитация ошибки валидации булевого значения применяемого к счётчику")
                    stubValidationBadTypeOperationAB("Имитация ошибки валидации типа операции между списками A и B")
                    stubValidationBadTypeOperationABCount("Имитация ошибки валидации типа операции между списками и счётчиком")
                    stubDbError("Имитация ошибки работы с BД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в ruleValidating") { ruleValidating = ruleRequest.copy() }
                    worker("Очистка id") { ruleValidating.id = VafsRuleId(ruleValidating.id.asString().trim()) }
                    worker("Очистка lock") { ruleValidating.lock = VafsRuleLock(ruleValidating.lock.asString().trim()) }
                    worker("Очистка описания") { ruleValidating.description = ruleValidating.description.trim() }
                    worker("Очистка значение в списке номеров A") {
                        ruleValidating.listForNumberA =
                            ruleValidating.listForNumberA.map { it.trim() }.filter { it.isNotEmpty() }
                    }
                    worker("Очистка значение в списке номеров B") {
                        ruleValidating.listForNumberB =
                            ruleValidating.listForNumberB.map { it.trim() }.filter { it.isNotEmpty() }
                    }

                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    validateLockNotEmpty("Проверка на непустой lock")
                    validateLockProperFormat("Проверка формата lock")
                    validateDescriptionNotEmpty("Проверка на непустое описание")
                    validateDescriptionHasContent("Проверка на наличие содержания в описании")
                    validatePriorityNotEqualZero("Проверка, что приоритет не равен 0")
                    validatePriorityLessUpperBound("Проверка, что приоритет меньше верхней границы")
                    validatePriorityMoreLowerBound("Проверка, что приоритет больше нижней границы")
                    validateListNumberANotEmpty("Что список для номера A не пустой")
                    validateListNumberBNotEmpty("Что список для номера B не пустой")
                    validateListNumberAValueProperFormat("Проверка элементов списка A на телефонный формат")
                    validateListNumberBValueProperFormat("Проверка элементов списка B на телефонный формат")
                    validateTypeOperationANotEmpty("Проверка типа операции для списка A на None")
                    validateTypeOperationBNotEmpty("Проверка типа операции для списка B на None")
                    validateTypeOperationCountNotEmpty("Проверка типа операции для счётчика на None")
                    validateTypeOperationABNotEmpty("Проверка типа булевой операции для списков A,B на None")
                    validateTypeOperationABCountNotEmpty("Проверка типа булевой операции для списков A,B и счётчика на None")
                    validateTargetCountMoreLowerBound("Проверка, что значение счётчика не равно 0")
                    validateTargetCountNotEqualZero("Проверка, что значение счётчика больше нижней границы")

                    finishRuleValidation("Успешное завершение процедуры валидации")
                }
                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика сохранения изменения"
                    repoRead("Чтение правила из БД")
                    accessValidation("Вычисление прав доступа")
                    repoPrepareUpdate("Подготовка объекта для обновления")
                    repoUpdate("Обновление правила в БД")
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
            }
            operation("Удалить правило", VafsCommand.DELETE) {
                stubs("Обработка стабов") {
                    stubDeleteSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с BД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в ruleValidating") {
                        ruleValidating = ruleRequest.copy()
                    }
                    worker("Очистка id") { ruleValidating.id = VafsRuleId(ruleValidating.id.asString().trim()) }
                    worker("Очистка lock") { ruleValidating.lock = VafsRuleLock(ruleValidating.lock.asString().trim()) }

                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    validateLockNotEmpty("Проверка на непустой lock")
                    validateLockProperFormat("Проверка формата lock")
                    finishRuleValidation("Успешное завершение процедуры валидации")
                }
                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика удаления"
                    repoRead("Чтение правила из БД")
                    accessValidation("Вычисление прав доступа")
                    repoPrepareDelete("Подготовка объекта для удаления")
                    repoDelete("Удаление правила из БД")
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
            }
            operation("Поиск правил", VafsCommand.SEARCH) {
                stubs("Обработка стабов") {
                    stubSearchSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с BД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в ruleFilterValidating") { ruleFilterValidating = ruleFilterRequest.copy() }

                    finishRuleFilterValidation("Успешное завершение процедуры валидации")
                }
                chainPermissions("Вычисление разрешений для пользователя")
                searchTypes("Подготовка поискового запроса")
                repoSearch("Поиск правил в БД по фильтру")
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
            }
        }.build()
    }
}
