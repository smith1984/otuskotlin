# Использованные промпты

## 1. Промпт на генерацию `CLAUDE.md`

> Изучи репозиторий otuskotlin (мульти-модульный Kotlin/Gradle, 17 модулей,
> Voice Antifraud System) и создай в корне `CLAUDE.md` — основную инструкцию
> для Claude Code и совместимых AI-агентов. Включи:
>
> 1. Контекст и границы: назначение, стек/версии, карта модулей с их
>    ответственностью, глоссарий ключевых сущностей (Rule, VafsRuleContext,
>    CoR и т.д.), что нельзя менять и почему, должен ли AI задавать
>    уточняющие вопросы.
> 2. Команды и Definition of Done: gradle-команды, требования к тестам,
>    стиль кода, требования к логированию.
> 3. Формат ответа: 4 блока — План / Изменения / Пояснение решений /
>    Шаги проверки.
> 4. Проверочное требование, которое легко отслеживается через grep по
>    git-логу — git trailer `AI-Tool: <инструмент> (<версия>)`. Объясни,
>    почему это лучше, чем «преамбула в каждом файле».
> 5. Ссылки куда смотреть, если задача не покрыта: TASKS.md, docs/04-arch,
>    specs, infra, rest.
>
> Стек, версии и имена модулей возьми из gradle.properties и
> settings.gradle.kts. Версии запрещено повышать.

## 2. Промпт на генерацию `docs/ai/TASKS.md`

> Создай `docs/ai/TASKS.md` как дополнение к CLAUDE.md с 4 типовыми
> сценариями работы с проектом VAFS. Для каждого сценария укажи:
>
> - Когда применять.
> - Затрагиваемые модули.
> - Готовый промпт (его можно скопировать в чат с агентом).
> - Ожидаемый формат ответа (4 блока: План / Изменения / Пояснение /
>   Шаги проверки).
> - Критерии готовности.
>
> Сценарии:
>
> 1. Добавить новую validation-handler в biz-цепочку
>    (m4-hw6-vafs-biz/validation, паттерн ValidateXxx.kt + подключение
>    в VafsRuleProcessor + happy/sad-path тест).
> 2. Расширить контракт repo новым методом — multi-module задача через
>    4 имплементации (stubs / in-memory / postgresql / tests + biz).
>    Учти Liquibase changeset, suspend-стиль, контрактный тест в
>    m5-hw7-vafs-repo-tests, не дублировать.
> 3. Покрыть существующий модуль/handler тестами без изменения поведения.
> 4. Рефакторинг без изменения observable behavior — никакой существующий
>    тест не должен потребовать правки.
>
> В конце — таблица антипаттернов (`!!`, переименование модулей,
> правка сгенерированных DTO, runBlocking вместо runTest, дублирование
> repo-тестов, мега-коммиты на 5 модулей, изменение gradle.properties без
> отдельной задачи) и шаблон git-коммита с trailer `AI-Tool`.

## 3. Промпт практической проверки

> Выполни Сценарий 1 из docs/ai/TASKS.md (добавление validation-handler в
> biz-цепочку). Строго следуй инструкциям CLAUDE.md.
>
> Конкретная задача: добавить `ValidateDescriptionLengthLessUpperBound`
> — handler, который падает в state=FAILING с
> errorValidation(field="description", violationCode="tooLong"), если
> `ruleValidating.description.length > 256`. В существующих валидаторах
> описания такой проверки нет.
>
> Что должно появиться:
>
> - `m4-hw6-vafs-biz/src/main/kotlin/validation/ValidateDescriptionLengthLessUpperBound.kt`
>   — новый handler по паттерну существующих ValidateDescription*.kt
>   через `worker {}` из lib-cor DSL.
> - В `VafsRuleProcessor.kt` для CREATE и UPDATE подключи новый handler
>   сразу после `validateDescriptionHasContent` с явным title.
> - `m4-hw6-vafs-biz/src/test/kotlin/validation/ValidationDescriptionLength.kt`
>   — 2 fixture-функции в стиле ValidationBadDescription.kt:
>   - happy path: description ровно 256 символов — state не FAILING,
>     errors пустые;
>   - sad path: description 257 символов — state == FAILING, error.field
>     == "description", error.code == "validation-description-tooLong".
> - В `BizValidationCreateTest` и `BizValidationUpdateTest` подключи
>   обе fixture-функции.
>
> Соблюдай:
> - никаких `!!`;
> - именование handler'а — `<Глагол><Существительное>.kt`;
> - используй DSL `lib-cor` (worker { on { } handle { fail(errorValidation(...)) } });
> - в тестах — `runTest`, не `runBlocking`;
> - не трогай сгенерированные DTO и gradle.properties.
>
> Ответ дай в 4 блоках: План / Изменения в коде / Пояснение решений /
> Шаги проверки. После выполнения прогони
> `./gradlew :m4-hw6-vafs-biz:test --tests "*Description*"` и подтверди
> по test-results/*.xml, что 4 новых testcase прошли.
>
> При коммите добавь trailer
> `AI-Tool: Claude Code (Opus 4.7, 1M ctx)`.

## 4. Дополнительные служебные промпты

### 4.1. Поиск шаблона существующего validator'а

> Покажи мне один-два уже существующих handler'а из
> `m4-hw6-vafs-biz/src/main/kotlin/validation/` (например,
> ValidatePriorityLessUpperBound и ValidateDescriptionHasContent) и
> один тест-fixture из `validation/ValidationBadDescription.kt`, чтобы
> я следовал точно такому же паттерну.

### 4.2. Проверка test-results

> Проверь, что файлы test-results/TEST-ru.beeline.vafs.biz.validation.\*Test.xml
> содержат testcase descriptionAtMaxLength и descriptionOverMaxLength
> без элементов `<failure/>`.
