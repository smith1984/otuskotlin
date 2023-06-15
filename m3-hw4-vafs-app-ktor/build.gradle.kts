import io.ktor.plugin.features.*
import org.jetbrains.kotlin.util.suffixIfNot

val ktorVersion: String by project
val fluentLoggerVersion: String by project
val logbackAppendersVersion: String by project
val javaVersion: String by project

fun ktor(module: String, prefix: String = "server-", version: String? = this@Build_gradle.ktorVersion): Any =
    "io.ktor:ktor-${prefix.suffixIfNot("-")}$module:$version"

plugins {
    kotlin("jvm")
    id("application")
    id("io.ktor.plugin")
}

repositories {
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

application {
    mainClass.set("ru.beeline.vafs.ktor.ApplicationKt")
}

ktor {
    docker {
        localImageName.set(project.name)
        imageTag.set(project.version.toString())
        jreVersion.set(JreVersion.valueOf("JRE_$javaVersion"))
        externalRegistry.set(
            DockerImageRegistry.dockerHub(
                appName = provider { "vafs-app" },
                username = providers.environmentVariable("DOCKER_HUB_USERNAME"),
                password = providers.environmentVariable("DOCKER_HUB_PASSWORD")
            )
        )
    }
}

dependencies {

    implementation(kotlin("stdlib"))

    implementation(ktor("core"))
    implementation(ktor("netty"))

    implementation(ktor("jackson", "serialization"))
    implementation(ktor("content-negotiation"))
    implementation(ktor("kotlinx-json", "serialization"))

    implementation(ktor("locations"))
    implementation(ktor("caching-headers"))
    implementation(ktor("call-logging"))
    implementation(ktor("auto-head-response"))
    implementation(ktor("cors"))
    implementation(ktor("auth"))
    implementation(ktor("auth-jwt"))
    implementation(ktor("default-headers"))
    implementation(ktor("websockets"))
    implementation(ktor("config-yaml"))
    implementation(ktor("swagger"))
    implementation(ktor("openapi"))


    implementation(project(":m4-hw6-vafs-lib-logging-logback"))
    implementation("com.sndyuk:logback-more-appenders:$logbackAppendersVersion")
    implementation("org.fluentd:fluent-logger:$fluentLoggerVersion")

    implementation(project(":m2-hw3-vafs-api-v1-jakson"))
    implementation(project(":m2-hw3-vafs-common"))
    implementation(project(":m2-hw3-vafs-mappers-v1"))
    implementation(project(":m3-hw4-vafs-stubs"))
    implementation(project(":m4-hw6-vafs-logging-common"))
    implementation(project(":m4-hw6-vafs-mappers-log"))
    implementation(project(":m4-hw6-vafs-api-log"))
    implementation(project(":m4-hw6-vafs-biz"))
    implementation(project(":m5-hw7-vafs-repo-stubs"))
    implementation(project(":m5-hw7-vafs-repo-in-memory"))
    implementation(project(":m5-hw7-vafs-repo-postgresql"))

    testImplementation(kotlin("test-junit"))
    testImplementation(ktor("content-negotiation", prefix = "client-"))
    testImplementation(ktor("test-host"))
    testImplementation(project(":m5-hw7-vafs-repo-tests"))

}

tasks {
    @Suppress("UnstableApiUsage")
    withType<ProcessResources>().configureEach {
        from("$rootDir/specs") {
            into("specs")
            filter {
                it.replace("{VERSION_APP}", project.version.toString())
            }
        }
    }
}
