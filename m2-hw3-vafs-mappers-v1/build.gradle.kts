plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":m2-hw3-vafs-api-v1-jakson"))
    implementation(project(":m2-hw3-vafs-common"))

    testImplementation(kotlin("test-junit"))
}