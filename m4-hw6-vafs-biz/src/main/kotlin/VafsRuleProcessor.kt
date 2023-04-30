package ru.beeline.vafs.biz

import ru.beeline.vafs.biz.groups.*
import ru.beeline.vafs.biz.validation.*
import ru.beeline.vafs.biz.workers.*
import ru.beeline.vafs.common.VafsContext
import ru.beeline.vafs.common.models.*
import ru.beeline.vafs.cor.*

class VafsRuleProcessor() {
    suspend fun exec(ctx: VafsContext) = BusinessChain.exec(ctx)

    companion object {
        private val BusinessChain = rootChain<VafsContext> {
            initStatus("Инициализация статуса")

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
                    stubDbError("Имитация ошибки работы с BД")
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

                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")

                    finishRuleValidation("Успешное завершение процедуры валидации")
                }
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

            }
        }.build()
    }
}
