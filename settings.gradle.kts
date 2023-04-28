rootProject.name = "otuskotlin"

pluginManagement {
    val kotlinVersion: String by settings
    val openapiVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion apply false

        id("org.openapi.generator") version openapiVersion apply false
    }
}

include("m1-hw1")
include("m2-hw3-vafs-api-v1-jakson")
include("m2-hw3-vafs-common")
include("m2-hw3-vafs-mappers-v1")
include("m3-hw4-vafs-stubs")
include("m3-hw4-vafs-app-ktor")
include("m4-hw6-vafs-api-log")
include("m4-hw6-vafs-logging-common")
include("m4-hw6-vafs-lib-logging-logback")
include("m4-hw6-vafs-mappers-log")
include("m4-hw6-vafs-lib-cor")
include("m4-hw6-vafs-biz")