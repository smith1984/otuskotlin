plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":m4-hw6-vafs-api-log"))
    implementation(project(":m2-hw3-vafs-common"))

    testImplementation(kotlin("test-junit"))
}