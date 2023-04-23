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

## First App and Test

1. [module 1 homework 1](m1-hw1) - Первая домашняя работа (первая программа и тест)

## Транспортные модели, API

1. [specs](specs) - описание API в форме OpenAPI-спецификаций
2. [m2-hw3-vafs-api-v1-jakson](m2-hw3-vafs-api-v1-jakson) - Генерация первой версии транспортных модеелй с
   Jackson
3. [m2-hw3-vafs-common](m2-hw3-vafs-common) - модуль с общими классами для модулей проекта. В частности, там
   располагаются внутренние модели и контекст.
4. [m2-hw3-vafs-mappers-v1](m2-hw3-vafs-mappers-v1) - Мапер между внутренними моделями и моделями API v1

## Фреймворки и транспорты
1. [m3-hw4-vafs-stubs](m3-hw4-vafs-stubs) - Стабы для ответов
2. [m3-hw4-vafs-app-ktor](m3-hw4-vafs-app-ktor) - Приложение на Ktor JVM + асинхронный транспорт