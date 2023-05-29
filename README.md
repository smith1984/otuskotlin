# Voice Antifraud System

Учебный проект курса [Otus](https://otus.ru) [Kotlin Backend Developer](https://otus.ru/lessons/kotlin/)

Voice Antifraud System -- это платформа для антифрод аналитиков телекоммуникационных компаний. 

Задача платформы -- предоставить рабочее место аналитика для создания/редактирования и удаления правил проверки сигнального трафика звонков и реагирования системы на звонки определённые как фродовые.

## Документация

1. Маркетинг
   * [Заинтересанты](./docs/01-marketing/01-stakeholders.md)
   * [Целевая аудитория](./docs/01-marketing/02-target-audience.md)
   * [Конкурентный анализ](./docs/01-marketing/03-concurrency.md)
   * [Анализ экономики](./docs/01-marketing/04-economy.md)
2. DevOps
   * [Схема развертывания](./docs/02-devops/01-infrastruture.md)
   * [Схема мониторинга](./docs/02-devops/02-monitoring.md)
3. Тесты
   * [Пример первого теста](./m1-hw1/src/test)
4. Архитектура
   * [Архитектура сервиса](./docs/04-arch/01-architecture.md)
   * [Дизайн backoffice](./docs/04-arch/02-design.md)
   * [Описание API](./docs/04-arch/03-api.md)

## Структура проекта

## Инфраструктура
1. [infra](infra) - Инструменты мониторинга и деплоя

## First App and Test

1. [module 1 homework 1](m1-hw1) - Первая домашняя работа (первая программа и тест)

## Транспортные модели, API

1. [specs](specs) - описание API в форме OpenAPI-спецификаций
2. [m2-hw3-vafs-api-v1-jakson](m2-hw3-vafs-api-v1-jakson) - Генерация первой версии транспортных модеелй с
   Jackson
3. [m4-hw6-vafs-api-log](m4-hw6-vafs-api-log) - Генерация моделей логирования
4. [m2-hw3-vafs-common](m2-hw3-vafs-common) - модуль с общими классами для модулей проекта. В частности, там
   располагаются внутренние модели и контекст.
5. [m2-hw3-vafs-mappers-v1](m2-hw3-vafs-mappers-v1) - Мапер между внутренними моделями и моделями API v1
6. [m4-hw6-vafs-mappers-log](m4-hw6-vafs-mappers-log) -  Мапер между внутренними моделями и моделями логирования

## Фреймворки и транспорты
1. [m3-hw4-vafs-stubs](m3-hw4-vafs-stubs) - Стабы для ответов
2. [m3-hw4-vafs-app-ktor](m3-hw4-vafs-app-ktor) - Приложение на Ktor JVM + асинхронный транспорт

## Мониторинг и логи
1. [m4-hw6-vafs-logging-common](m4-hw6-vafs-logging-common) - Общая библиотека для логирования
2. [m4-hw6-vafs-lib-logging-logback](m4-hw6-vafs-lib-logging-logback) - Библиотека логирования на базе библиотеки
   Logback

## Модули бизнес-логики
1. [m4-hw6-vafs-lib-cor](m4-hw6-vafs-lib-cor) - Библиотека цепочки обязанностей для бизнес-логики
2. [m4-hw6-vafs-biz](m4-hw6-vafs-biz) - Модуль бизнес-логики приложения

## Хранение, репозитории, базы данных
1. [m5-hw7-vafs-repo-tests](m5-hw7-vafs-repo-tests) - Базовые тесты для репозиториев всех баз данных
2. [m5-hw7-vafs-repo-stubs](m5-hw7-vafs-repo-stubs) - Интерфейс репозитория в виде стабов
3. [m5-hw7-vafs-repo-in-memory](m5-hw7-vafs-repo-in-memory) - Репозиторий на базе кэша в памяти для тестирования
4. [m5-hw7-vafs-repo-postgresql](m5-hw7-vafs-repo-postgresql) - Репозиторий на базе PostgreSQL

## Аутентификация и авторизация
1. [m6-hw8-vafs-auth](m6-hw8-vafs-auth) - Сопоставления разрешений и отношений для них, для определения прав пользователя