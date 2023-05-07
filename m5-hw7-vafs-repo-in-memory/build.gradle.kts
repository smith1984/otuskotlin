plugins {
    kotlin("jvm")
}

dependencies {

    val cache4kVersion: String by project
    val coroutinesVersion: String by project

    implementation(kotlin("stdlib"))
    implementation(project(":m2-hw3-vafs-common"))
    implementation("io.github.reactivecircus.cache4k:cache4k:$cache4kVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    testImplementation(kotlin("test-junit"))
    testImplementation(project(":m5-hw7-vafs-repo-tests"))
}
