# HW3 (Lesson 10) — отчёт о добавлении AI-инструкций

## 1. Проект

[smith1984/otuskotlin](https://github.com/smith1984/otuskotlin) — учебный
backend «**Voice Antifraud System (VAFS)**», написанный по программе курса
OTUS Kotlin Backend Developer. Это мульти-модульный Gradle/Kotlin-проект
(17 модулей, Apache 2.0): Ktor-сервер с CRUDS-операциями над сущностью
`Rule`, собственный Chain-of-Responsibility DSL `lib-cor`, OpenAPI-Generator,
Exposed/PostgreSQL, Liquibase, Testcontainers, Keycloak, OpenSearch.

Проект подходит под условия ДЗ: реальная бизнес-логика (валидация правил
сигнального трафика голосовых вызовов), несколько слоёв (api / common /
biz / repo / app), точки расширения, открытые исходники. Это пет-проект
автора (форк/клон), доступный для модификаций.

## 2. AI-инструмент, под который специализированы инструкции

**Claude Code** (CLI и VSCode-расширение, модель Claude Opus 4.7).

Документация: https://docs.claude.com/en/docs/claude-code/

Claude Code в качестве основного источника инструкций использует файл
`CLAUDE.md` в корне репозитория — он автоматически подгружается в контекст
при запуске агента и имеет наивысший приоритет среди project-level правил.
Дополнительные документы могут лежать в `docs/ai/` и подключаются по ссылке
из `CLAUDE.md`.

## 3. Что было добавлено

| Файл | Назначение |
|---|---|
| [`CLAUDE.md`](CLAUDE.md) | Главный entry-point: контекст проекта, стек/версии, карта 17 модулей, глоссарий, ограничения, команды, формат ответа AI, проверочное требование |
| [`docs/ai/TASKS.md`](docs/ai/TASKS.md) | 4 типовых сценария с готовыми промптами и критериями приёмки |

Структура `CLAUDE.md`:

1. **Контекст и границы** — назначение проекта, стек/версии, модули,
   глоссарий (VAFS, Rule, Operation, Stub, VafsRuleContext, CoR, Principal),
   что нельзя менять, когда задавать уточняющие вопросы.
2. **Команды и проверки** — gradle-команды, Definition of Done (5 пунктов),
   стиль кода (никаких `!!`, именование handler'ов), требования к тестам
   (`runTest`, не `runBlocking`; для repo — наследовать от
   `m5-hw7-vafs-repo-tests`), правила логирования (через `IMpLogWrapper`).
3. **Формат ответа AI** — обязательные 4 блока: План / Изменения /
   Пояснение / Шаги проверки.
4. **Проверочное требование** — каждый коммит, сделанный AI-агентом, обязан
   нести trailer `AI-Tool: <инструмент> (<версия>)`. Это легко проверяется
   `git log --pretty=format:'%H %(trailers:key=AI-Tool,valueonly)'`.
5. **Куда смотреть** — навигация на `docs/ai/TASKS.md`, `docs/04-arch/`,
   `specs/`, `infra/docker-compose.yaml`, `rest/`.

`docs/ai/TASKS.md` содержит 4 готовых сценария:

1. Добавить новую validation-handler в biz-цепочку.
2. Расширить контракт repo новым методом (multi-module задача через 4
   реализации).
3. Покрыть существующий модуль/handler тестами.
4. Рефакторинг без изменения поведения.

Каждый сценарий описан в формате Промпт / Ожидаемый формат ответа /
Критерии готовности. В конце — таблица антипаттернов и шаблон git-коммита
с trailer'ом `AI-Tool`.

## 4. Практическая проверка инструкций

### 4.1. Какое изменение запрашивалось

Я попросил Claude Code (себя) выполнить **Сценарий 1** из `docs/ai/TASKS.md`:
добавить новую validation-handler в biz-цепочку, строго следуя `CLAUDE.md`.
Конкретная задача — `ValidateDescriptionLengthLessUpperBound`: запретить
описания правила длиннее 256 символов. В существующих валидаторах
описания (`ValidateDescriptionNotEmpty`, `ValidateDescriptionHasContent`)
такой проверки нет, выбор естественный и ненадуманный.

### 4.2. Промпт

Полный текст промпта — в [`PROMPTS.md`](PROMPTS.md), раздел «Промпт
практической проверки».

### 4.3. Что было сделано (diff)

| Файл | Тип | Что |
|---|---|---|
| `m4-hw6-vafs-biz/src/main/kotlin/validation/ValidateDescriptionLengthLessUpperBound.kt` | NEW | Новый handler. Падает в `state=FAILING` с `errorValidation(field="description", violationCode="tooLong")`, если `description.length > 256` |
| `m4-hw6-vafs-biz/src/main/kotlin/VafsRuleProcessor.kt` | EDIT | Подключение handler'а в цепочку validation для `CREATE` и `UPDATE` сразу после `validateDescriptionHasContent` |
| `m4-hw6-vafs-biz/src/test/kotlin/validation/ValidationDescriptionLength.kt` | NEW | 2 fixture-функции (`validationDescriptionLengthAtUpperBound`, `validationDescriptionLengthOverUpperBound`) — happy path и sad path в стиле существующих тестов модуля |
| `m4-hw6-vafs-biz/src/test/kotlin/validation/BizValidationCreateTest.kt` | EDIT | Подключены 2 теста |
| `m4-hw6-vafs-biz/src/test/kotlin/validation/BizValidationUpdateTest.kt` | EDIT | Подключены 2 теста |

### 4.4. Соответствует ли результат инструкциям?

| Требование `CLAUDE.md` | Соблюдено? | Подтверждение |
|---|---|---|
| Никаких `!!` в production-коде (§2.3) | да | в новом handler'е ноль `!!` |
| Имя handler'а — `<Глагол><Существительное>.kt` (§2.3) | да | `ValidateDescriptionLengthLessUpperBound.kt` — следует существующему паттерну `ValidatePriorityLessUpperBound.kt` |
| Новая логика в `m4-hw6-vafs-biz` сопровождается unit-тестом (§2.2 п.2) | да | 2 теста (happy + sad path) для каждой команды (CREATE и UPDATE) — итого 4 testcase |
| `runTest`, а не `runBlocking` (§2.4) | да | используется `kotlinx.coroutines.test.runTest` |
| Использование DSL `lib-cor` (а не ручной обход цепи) (TASKS.md §1) | да | `worker { ... }` через `ICorChainDsl<VafsContext>` |
| Подключение в `VafsRuleProcessor` с явным `title` (TASKS.md §1) | да | `validateDescriptionLengthLessUpperBound("Проверка, что описание не превышает верхнюю границу длины")` |
| Сгенерированные DTO не трогались (§1.5 п.3) | да | изменения только в `m4-hw6-vafs-biz` |
| Версии стека не менялись (§1.5 п.2) | да | `gradle.properties` не тронут |
| AI-Tool trailer в коммите (§4) | да | см. вывод `git log` после коммита |

### 4.5. Шаги проверки и результат

```bash
./gradlew :m4-hw6-vafs-biz:test --tests "*Description*"
```

Результат: `BUILD SUCCESSFUL`. Среди тестов прошли 4 новых:

- `BizValidationCreateTest.descriptionAtMaxLength` — 0.008s
- `BizValidationCreateTest.descriptionOverMaxLength` — 0.224s
- `BizValidationUpdateTest.descriptionAtMaxLength` — 0.002s
- `BizValidationUpdateTest.descriptionOverMaxLength` — 0.001s

(подтверждено через `m4-hw6-vafs-biz/build/test-results/test/TEST-*.xml`)

### 4.6. Найденные нарушения / отклонения

Ни одно правило `CLAUDE.md` или сценарий из `docs/ai/TASKS.md` не нарушено.
Формат ответа агента содержал все 4 блока (План / Изменения / Пояснение /
Шаги проверки).

## 5. Трудности при выполнении

1. **JDK-конфликт окружения.** Хост-машина имеет только JDK 21, а
   `gradle.properties` пинит Java 17 / Kotlin 1.8.20. `CLAUDE.md` §1.5
   запрещает повышать версии стека. Прямой запуск `./gradlew test` падает
   с `compileJava (target 21) and compileKotlin (target 17)…`.

   **Решение:** не менять `gradle.properties`, а установить Temurin JDK 17
   через **SDKMAN** (без `sudo`), переключить shell-сессию через
   `sdk use java 17.0.13-tem` и запустить тесты — прошли успешно.
   Это пример того, как `CLAUDE.md` должен работать: правило «не менять
   стек» сработало и вынудило меня искать обход в окружении, а не в коде
   проекта.

2. **Init-script для Gradle toolchain не сработал** на Gradle 8.1.1 +
   JDK 21 — Gradle 8.1.1 не запускается на JDK 21 (`Unsupported class
   file major version 65`). Это отдельная Gradle↔JDK-несовместимость,
   которую через init-script не починить, поэтому SDKMAN-путь
   единственно работающий в этом окружении.

## 6. Использованные промпты

См. [`PROMPTS.md`](PROMPTS.md).
