plugins {
    application
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))

    testImplementation(kotlin("test-junit"))
}

application {
    mainClass.set("ru.beeline.vafs.m1hw1.MyFirstApp")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "ru.beeline.vafs.m1hw1.MyFirstAppKt"
    }
}