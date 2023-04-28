plugins {
    kotlin("jvm")
}

dependencies {

    val datetimeVersion: String by project

    implementation(kotlin("stdlib"))

    api("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")

}

