plugins {
    id("java")
    id("com.gradleup.shadow") version "9.2.2"
    id("io.freefair.lombok") version "8.14"
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/sonatype-nexus-snapshots/") {
        content {
            includeModuleByRegex("net\\.md-5", "bungeecord.*")
        }
    }
}

dependencies {
    implementation(project(":core"))
    compileOnly("org.spigotmc:spigot-api:1.8-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(7))
}