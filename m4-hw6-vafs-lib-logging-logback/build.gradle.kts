plugins {
    kotlin("jvm")
}

dependencies {
    val logbackVersion: String by project
    val logbackEncoderVersion: String by project
    val coroutinesVersion: String by project

    implementation(kotlin("stdlib"))

    implementation(project(":m4-hw6-vafs-logging-common"))

    implementation("net.logstash.logback:logstash-logback-encoder:$logbackEncoderVersion")
    api("ch.qos.logback:logback-classic:$logbackVersion")

    testImplementation(kotlin("test-junit"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
}
