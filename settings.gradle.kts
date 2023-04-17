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
