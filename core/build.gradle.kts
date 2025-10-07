plugins {
    id("java")
    id("com.gradleup.shadow") version "9.2.2"
    id("io.freefair.lombok") version "8.14"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.java-websocket:Java-WebSocket:1.6.0")
    implementation("com.googlecode.json-simple:json-simple:1.1.1") {
        isTransitive = false
    }
}

tasks.shadowJar {
    relocate("org.json", "com.thatgamerblue.json")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

java {
    if (!providers.gradleProperty("modernBuild").isPresent) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(7))
    }
}