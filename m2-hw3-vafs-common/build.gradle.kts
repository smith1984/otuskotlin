plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))

    val datetimeVersion: String by project
    api("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
    implementation(project(":m4-hw6-vafs-logging-common"))
}
