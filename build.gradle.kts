plugins {
    kotlin("jvm") version "2.0.21"
}

group = "com.psousa50"
version = "1.0-SNAPSHOT"

val kotestVersion="5.9.1"

repositories {
    mavenCentral()
}

dependencies {
}

dependencies {
    implementation ("com.xenomachina:kotlin-argparser:2.0.7")

    testImplementation(kotlin("test"))

    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-json-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    testImplementation("io.kotest:kotest-framework-datatest:$kotestVersion")

    testImplementation(kotlin("test"))
}


tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}