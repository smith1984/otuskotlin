plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(mapOf("path" to ":m2-hw3-vafs-common")))
}
