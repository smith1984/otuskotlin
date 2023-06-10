plugins {
    kotlin("jvm")
}

dependencies {

    val coroutinesVersion: String by project
    val datetimeVersion: String by project

    implementation(kotlin("stdlib"))

    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    api("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")

    implementation(project(":m2-hw3-vafs-common"))
}
