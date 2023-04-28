plugins {
    kotlin("jvm")
}

dependencies {
    val coroutinesVersion: String by project

    implementation(kotlin("stdlib"))

    implementation(project(":m2-hw3-vafs-common"))
    implementation(project(":m3-hw4-vafs-stubs"))
    implementation(project(":m4-hw6-vafs-lib-cor"))

    testImplementation(kotlin("test-junit"))
    testApi("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
}
