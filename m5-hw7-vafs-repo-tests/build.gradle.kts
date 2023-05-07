plugins {
    kotlin("jvm")
}

dependencies {

    val coroutinesVersion: String by project
    val testContainersVersion: String by project
    val liquiBaseVersion: String by project

    implementation(kotlin("stdlib"))
    implementation(project(":m2-hw3-vafs-common"))
    implementation(project(":m5-hw7-vafs-repo-postgresql"))

    implementation("org.testcontainers:postgresql:$testContainersVersion")
    implementation("org.liquibase:liquibase-core:$liquiBaseVersion")

    api(kotlin("test-junit"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")


}
