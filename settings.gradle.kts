rootProject.name = "otuskotlin"

pluginManagement {
    val kotlinVersion: String by settings
    val ktorPluginVersion: String by settings
    val openapiVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion apply false

        id("io.ktor.plugin") version ktorPluginVersion apply false

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
include("m5-hw7-vafs-repo-stubs")
include("m5-hw7-vafs-repo-tests")
include("m5-hw7-vafs-repo-in-memory")
include("m5-hw7-vafs-repo-postgresql")
include("m6-hw8-vafs-auth")
