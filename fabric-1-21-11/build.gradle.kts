import com.modrinth.minotaur.Minotaur

val minecraft_version: String = "1.21.11"
val loader_version: String = "0.18.2"
val fabric_version: String = "0.139.4+1.21.11"

plugins {
    val loom_version = "1.14-SNAPSHOT"
    id("java")
    id("io.freefair.lombok") version "8.14"
    id("net.fabricmc.fabric-loom-remap") version loom_version
    id("com.modrinth.minotaur") version "2.+"
}

base {
    archivesName = "subauth-fabric-$minecraft_version"
}

repositories {
    mavenCentral()
}

dependencies {
    // this sucks...
    compileOnly(project(":core"))
    include(project(":core"))
    include("org.java-websocket:Java-WebSocket:1.6.0")
    include("com.googlecode.json-simple:json-simple:1.1.1")

    minecraft("com.mojang:minecraft:${minecraft_version}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${loader_version}")

    // Fabric API. This is technically optional, but you probably want it anyway.
    modImplementation("net.fabricmc.fabric-api:fabric-api:${fabric_version}")
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand(mapOf(Pair("version", inputs.properties["version"])))
    }
}

java {
    if (!providers.gradleProperty("modernBuild").isPresent) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.jar {
    inputs.property("archivesName", project.base.archivesName)
}

var distJarTask: Task = tasks.register("distJar") {
    distJarTask = this
    dependsOn(tasks.build)
    outputs.file("${project.layout.buildDirectory.get()}/libs/${base.archivesName.get()}-${version}.jar")
}.get()

tasks.modrinth {
    dependsOn(distJarTask)
}

modrinth {
    token = System.getenv("MODRINTH_TOKEN")
    projectId = System.getenv("MODRINTH_PROJECT") ?: "subauth"
    versionName = "${project.version} Fabric $minecraft_version"
    versionType = if ((project.version as String).contains("SNAPSHOT")) "beta" else "release"
    uploadFile = distJarTask.outputs.files.first()
    gameVersions.addAll(minecraft_version)
    loaders.addAll("fabric", "quilt")
    dependencies {
        required.project("fabric-api")
    }
}