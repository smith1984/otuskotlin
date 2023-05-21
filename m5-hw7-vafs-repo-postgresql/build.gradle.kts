plugins {
    kotlin("jvm")
}

dependencies {
    val exposedVersion: String by project
    val postgresDriverVersion: String by project
    val testContainersVersion: String by project
    val liquiBaseVersion: String by project

    implementation(kotlin("stdlib"))
    implementation(project(":m2-hw3-vafs-common"))

    implementation("org.postgresql:postgresql:$postgresDriverVersion")

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")

    testImplementation(kotlin("test-junit"))
    testImplementation(project(":m5-hw7-vafs-repo-tests"))
    testImplementation("org.testcontainers:postgresql:$testContainersVersion")
    testImplementation("org.liquibase:liquibase-core:$liquiBaseVersion")
}
