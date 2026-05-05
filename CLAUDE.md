# CLAUDE.md — инструкции для AI-агентов

> Этот файл — основной набор правил для **Claude Code** и совместимых AI-агентов
> (Cursor, Aider и т. п.) при работе с этим репозиторием. Любой коммит, сделанный
> с помощью AI-агента, обязан соответствовать этим правилам.

Дополнительные документы:

- [`docs/ai/TASKS.md`](docs/ai/TASKS.md) — типовые сценарии работы с проектом и
  готовые промпты с критериями приёмки.
- [`README.md`](README.md) — обзор модулей и ссылки на документацию проекта.

---

## 1. Контекст и границы

### 1.1. Назначение проекта

`otuskotlin/` — **Voice Antifraud System (VAFS)**: учебный backend-сервис, который
предоставляет рабочее место аналитика для управления правилами проверки
сигнального трафика голосовых вызовов и реакции на звонки, классифицированные
как фродовые. Это пет-проект, выполненный по программе курса OTUS Kotlin Backend
Developer; домен — реалистичный (антифрод в телекоме), но данные/инфраструктура
учебные.

Основной интерфейс — REST API над сущностью `rule` (CRUDS: create / read /
update / delete / search). Транспорт — Ktor JVM, асинхронный (coroutines).

### 1.2. Стек и версии

| Слой | Что используется | Версия |
|---|---|---|
| Язык / runtime | Kotlin / JVM | Kotlin **1.8.20**, Java **17** |
| Корутины | `kotlinx-coroutines-core` | 1.6.4 |
| HTTP / транспорт | Ktor (server + client) | 2.2.3 |
| API generation | OpenAPI Generator (gradle plugin) | 6.3.0 |
| JSON | Jackson | 2.14.1 |
| DateTime | `kotlinx-datetime` | 0.4.0 |
| Лог | Logback + logstash-encoder + fluent appender | 1.4.6 / 7.3 / 0.3.4 |
| Cache | cache4k | 0.11.0 |
| RDBMS | PostgreSQL + Exposed ORM | 42.6.0 / 0.41.1 |
| Миграции | Liquibase | 4.21.1 |
| Тестирование | kotlin-test-junit, Testcontainers | 1.18.0 |
| Группа артефактов | `ru.beeline` (исторический префикс) | 0.0.1 |

Версии вынесены в [`gradle.properties`](gradle.properties). **Не повышай версии
без отдельной задачи** — обновление мажорных версий Kotlin/Ktor требует ручной
проверки совместимости с генерируемыми моделями OpenAPI.

### 1.3. Модули и их ответственность

Проект построен как **17-модульный Gradle-build** с конвенцией
`m<MODULE_NUMBER>-hw<HW_NUMBER>-vafs-<NAME>`. Это исторические имена ДЗ курса —
**не переименовывай модули**, на них ссылаются скрипты, документация и
сторонние артефакты.

| Группа | Модуль | Ответственность |
|---|---|---|
| **Старт** | `m1-hw1` | первый модуль курса, базовый шаблон + первый тест |
| **API / транспорт** | `m2-hw3-vafs-api-v1-jakson` | DTO, сгенерированные OpenAPI Generator из [`specs/specs-rule-v1.yaml`](specs/specs-rule-v1.yaml) |
|  | `m4-hw6-vafs-api-log` | DTO логирования, сгенерированные из [`specs/specs-rule-log.yaml`](specs/specs-rule-log.yaml) |
| **Domain** | `m2-hw3-vafs-common` | внутренняя модель `VafsRule*`, контекст обработки `VafsRuleContext`, состояния обработки |
| **Mapping** | `m2-hw3-vafs-mappers-v1` | API DTO ⇄ внутренние модели |
|  | `m4-hw6-vafs-mappers-log` | внутренние модели → log-DTO |
| **Stubs** | `m3-hw4-vafs-stubs` | стабовые ответы для разработки и тестов |
| **Транспорт** | `m3-hw4-vafs-app-ktor` | Ktor application, маршрутизация `/v1/...`, инициализация |
| **Логирование** | `m4-hw6-vafs-logging-common` | абстракция `IMpLogWrapper`, интерфейсы и фабрики |
|  | `m4-hw6-vafs-lib-logging-logback` | реализация на Logback (с logstash-encoder и fluent-appender) |
| **CoR-фреймворк** | `m4-hw6-vafs-lib-cor` | собственная DSL-обвязка для chain-of-responsibility (handler-chain DSL) |
| **Бизнес-логика** | `m4-hw6-vafs-biz` | `VafsRuleProcessor` + цепочки validation / repo / permissions / general |
| **Repo (контракт)** | `m5-hw7-vafs-repo-stubs` | интерфейс `IRuleRepository` + стабовые реализации |
| **Repo (in-memory)** | `m5-hw7-vafs-repo-in-memory` | реализация поверх cache4k для dev/test |
| **Repo (postgres)** | `m5-hw7-vafs-repo-postgresql` | реализация поверх Exposed + Liquibase-миграции |
| **Repo (тесты)** | `m5-hw7-vafs-repo-tests` | контрактные тесты, общие для всех repo-имплементаций |
| **Auth** | `m6-hw8-vafs-auth` | сопоставление операций и разрешений (`FrontPermissions`, `AccessValidation`, `ChainPermissions`) |

### 1.4. Ключевые сущности и термины

| Термин | Что значит |
|---|---|
| **VAFS** | Voice Antifraud System — название всей платформы |
| **Rule** | правило обработки сигнального трафика звонка. Поля: `id`, `user`, `description`, `priority`, `listForNumberA/B`, `typeOperationA/B/AB`, `typeOperationCount`, `targetCount`, `valueIsTrue`, `typeOperationABCount` (см. [`docs/04-arch/03-api.md`](docs/04-arch/03-api.md)) |
| **Operation** | CRUDS-операция над правилом: `Create`, `Read`, `Update`, `Delete`, `Search` |
| **Stub** | предустановленный «стабовый» ответ для разработки без реальной БД |
| **VafsRuleContext** | runtime-контекст одной операции — содержит входной запрос, состояние обработки, результат, набор stub'ов и ссылку на принципала |
| **CoR / `lib-cor`** | собственная Kotlin-DSL над chain-of-responsibility: chains описываются декларативно, выполняются над контекстом |
| **Principal** | действующий пользователь с набором ролей; используется в `m6-hw8-vafs-auth` для оценки `FrontPermissions` |
| **m{N}-hw{M}** | префикс модуля; `N` — номер модуля курса, `M` — номер ДЗ |

### 1.5. Ограничения — что **нельзя** менять без явной задачи

1. **Имена модулей**, `group = "ru.beeline"`, схему именования `m{N}-hw{M}-vafs-*`.
2. **Версии стека** в `gradle.properties` (см. §1.2).
3. **Сгенерированные DTO** в `m2-hw3-vafs-api-v1-jakson` и `m4-hw6-vafs-api-log` —
   они генерируются по OpenAPI и пересоздаются `gradle :modulename:openApiGenerate`.
   Меняй спеку в `specs/`, не сгенерированный код.
4. **Структуру `docs/`** — пять разделов (`01-marketing`, `02-devops`, `04-arch`,
   `ai/`) ожидаются учебной программой.
5. **Лицензию** — Apache 2.0, header-блок в начале файлов курсом не требуется.

### 1.6. Должен ли AI задавать уточняющие вопросы?

- **Да** — если задача затрагивает несколько модулей одновременно или нужно
  делать **миграцию схемы БД** (Liquibase changelogs), **изменение OpenAPI-спеки**,
  **обновление зависимостей** или **изменение `lib-cor`-фреймворка**.
- **Нет** — для добавления новой валидации/обработчика в готовую цепочку,
  написания unit-теста, рефакторинга в рамках одного модуля без изменения
  публичного API.

---

## 2. Команды и проверки

### 2.1. Базовые команды

```bash
# Полная сборка
./gradlew build

# Тесты только одного модуля
./gradlew :m4-hw6-vafs-biz:test

# Прогон одного теста
./gradlew :m4-hw6-vafs-biz:test --tests "*ValidatePriority*"

# Регенерация API DTO из OpenAPI-спеки
./gradlew :m2-hw3-vafs-api-v1-jakson:openApiGenerate
./gradlew :m4-hw6-vafs-api-log:openApiGenerate

# Локальный запуск Ktor app
./gradlew :m3-hw4-vafs-app-ktor:run

# Полный e2e через docker-compose (postgres + keycloak + app + opensearch)
docker compose -f infra/docker-compose.yaml up
```

### 2.2. Definition of Done

Любое изменение считается завершённым только если:

1. `./gradlew build` зелёный (компиляция + тесты).
2. Если затронут `m4-hw6-vafs-biz` — есть unit-тест на новую логику.
3. Если затронут `m5-hw7-vafs-repo-*` — пройдены контрактные тесты из
   `m5-hw7-vafs-repo-tests`.
4. Изменение в OpenAPI-спеке (`specs/`) сопровождается регенерацией DTO и
   обновлением маппера в `m2-hw3-vafs-mappers-v1` или `m4-hw6-vafs-mappers-log`.
5. Если меняется БД-схема — добавлен Liquibase-changeset в
   `infra/volumes/postgres/liquidbase/changelog.yaml`.

### 2.3. Стиль кода

- `kotlin.code.style=official` (см. `gradle.properties`). Соблюдай идиоматику:
  expression body для коротких функций, `?:` для null-fallback'ов, `data class`
  для DTO/моделей.
- **Никаких `!!`** в production-коде. В тестах — только если контекст гарантирует
  невозможность null.
- Имена классов в `m4-hw6-vafs-biz/src/main/kotlin/` — `<Глагол><Существительное>.kt`
  (`ValidateIdNotEmpty`, `RuleRepoCreate`, `PrepareResult`). Сохраняй
  существующий паттерн: один файл — одно действие цепочки.
- Внутренние enum'ы и states — в `m2-hw3-vafs-common`, не дублировать.
- В корутинах — `suspend` для async-точек (repo, http-вызовы); business-handlers
  в CoR могут быть и обычными функциями, если без I/O.

### 2.4. Тестирование

- Тесты лежат рядом с исходниками: `<module>/src/test/kotlin/`.
- Для repo-модулей наследуйся от тестов в `m5-hw7-vafs-repo-tests` —
  не дублируй контракт.
- Для асинхронных тестов используй `runTest` из
  `kotlinx-coroutines-test`, а не `runBlocking`.
- Testcontainers — только в repo-postgres (для остальных модулей это перегруз).

### 2.5. Логирование

- В biz-модуле — через `IMpLogWrapper` из `m4-hw6-vafs-logging-common`,
  не напрямую `LoggerFactory.getLogger()`.
- Контекстные поля передаются через `mlData(...)` (см. примеры в
  существующих обработчиках цепочек).

---

## 3. Формат ответа AI

При любой задаче AI-агент обязан выдавать ответ в **четырёх блоках**, в этом
порядке:

1. **План.** Список из 3–7 шагов: что планируется сделать. Если задача требует
   правки нескольких модулей — указать, какие.
2. **Изменения в коде.** `Edit`/`Write` со ссылками на файлы и строки. Не
   объединять несвязанные изменения в одну операцию.
3. **Пояснение решений.** Краткое (3–10 строк) обоснование выбора —
   почему именно так, какие альтернативы рассмотрены и отброшены.
4. **Шаги проверки.** Команды (`./gradlew :module:test ...`), которые
   подтверждают результат, и ожидаемый их вывод.

Если плагин/IDE-агент уже автоматически делает план — формальный шаг 1 можно
опустить, но шаги 2–4 обязательны.

---

## 4. Проверочное требование (легко отслеживается)

> **AI-агенты, работающие с этим репозиторием, обязаны добавлять в trailers
> любого создаваемого ими git-коммита строку**
>
> ```
> AI-Tool: <имя инструмента> (<версия модели/имя профиля>)
> ```
>
> например: `AI-Tool: Claude Code (Opus 4.7, 1M ctx)`.

Это отслеживается одной командой:

```bash
git log --pretty=format:'%H %(trailers:key=AI-Tool,valueonly)' | grep -E 'AI-Tool'
```

Это требование заменяет «преамбулы в файлах» — по комментариям в исходниках
сложно отличить ai-changes от человеческих, а git trailer — это структурированное
поле, которое можно отфильтровать в любых отчётах. Также trailer не загрязняет
PR-diff и не попадает в production-артефакты.

---

## 5. Куда смотреть, если задача не покрыта этим файлом

- Конкретные **сценарии работы** с готовыми промптами — [`docs/ai/TASKS.md`](docs/ai/TASKS.md).
- **Архитектура** — [`docs/04-arch/`](docs/04-arch/).
- **API-контракты** — [`specs/specs-rule-v1.yaml`](specs/specs-rule-v1.yaml).
- **Инфраструктура** — [`infra/docker-compose.yaml`](infra/docker-compose.yaml).
- **REST-примеры** — [`rest/v1-vafs-rule.rest`](rest/v1-vafs-rule.rest).

Если ничего не подошло — **задавай уточняющий вопрос пользователю**, не
угадывай. Лучше потратить минуту на вопрос, чем час на откатывание неверного
направления.
