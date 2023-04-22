plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib-common"))
    implementation(kotlin("test-common"))
    implementation(kotlin("test-annotations-common"))

    val datetimeVersion: String by project
    api("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
}
