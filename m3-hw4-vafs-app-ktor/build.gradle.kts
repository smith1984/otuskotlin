import org.jetbrains.kotlin.util.suffixIfNot
import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage

val ktorVersion: String by project
val logbackVersion: String by project


fun ktor(module: String, prefix: String = "server-", version: String? = this@Build_gradle.ktorVersion): Any =
    "io.ktor:ktor-${prefix.suffixIfNot("-")}$module:$version"

plugins {
    kotlin("jvm")

    id("application")

    id("com.bmuschko.docker-java-application")
    id("com.bmuschko.docker-remote-api")
}

repositories {
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

application {
    mainClass.set("ru.beeline.vafs.ktor.ApplicationKt")
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
    implementation(ktor("default-headers"))
    implementation(ktor("websockets"))

    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    implementation(project(":m2-hw3-vafs-api-v1-jakson"))
    implementation(project(":m2-hw3-vafs-common"))
    implementation(project(":m2-hw3-vafs-mappers-v1"))
    implementation(project(":m3-hw4-vafs-stubs"))
    implementation("io.ktor:ktor-server-core-jvm:2.2.3")
    implementation("io.ktor:ktor-server-websockets-jvm:2.2.3")

    testImplementation(kotlin("test-junit"))
    testImplementation(ktor("content-negotiation", prefix = "client-"))
    testImplementation(ktor("test-host"))

}