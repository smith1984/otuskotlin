plugins {
    kotlin("jvm")
}

dependencies {
    val coroutinesVersion: String by project

    implementation(kotlin("stdlib"))

    implementation(project(":m2-hw3-vafs-common"))
    implementation(project(":m3-hw4-vafs-stubs"))
    implementation(project(":m4-hw6-vafs-lib-cor"))
    implementation(project(":m6-hw8-vafs-auth"))

    implementation(project(":m4-hw6-vafs-lib-logging-logback"))
    implementation(project(":m4-hw6-vafs-logging-common"))

    testImplementation(kotlin("test-junit"))
    testApi("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    testImplementation(project(":m5-hw7-vafs-repo-in-memory"))
    testImplementation(project(":m5-hw7-vafs-repo-stubs"))
    testImplementation(project(":m5-hw7-vafs-repo-tests"))
}
