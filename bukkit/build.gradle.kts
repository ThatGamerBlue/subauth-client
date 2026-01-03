import groovy.json.JsonSlurper
import java.net.URI

plugins {
    id("java")
    id("com.gradleup.shadow") version "9.2.2"
    id("io.freefair.lombok") version "8.14"
    id("com.modrinth.minotaur") version "2.+"
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
    if (!providers.gradleProperty("modernBuild").isPresent) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(7))
    }
}

tasks.shadowJar {
    relocate("org.json", "com.thatgamerblue.json")

    archiveBaseName = "subauth-" + project.name

	archiveClassifier = ""
}

var distJarTask: Task = tasks.register("distJar") {
    outputs.files(tasks.shadowJar.get().outputs.files)
}.get()

fun getMinecraftVersions(): List<String> {
    val list: ArrayList<String> = ArrayList()
    val uri = URI("https://launchermeta.mojang.com/mc/game/version_manifest.json")
    val versions: List<Map<String, String>> = (JsonSlurper().parse(uri.toURL()) as Map<*, *>)["versions"] as List<Map<String, String>>
    versions.forEach { version ->
        if (version["type"] == "release") {
            if (version["id"]?.startsWith("1.6") ?: true) {
                return list
            }
            if (version["id"] != null) {
                list.add(version["id"]!!)
            }
        }
    }
    return list
}

tasks.modrinth {
    dependsOn(distJarTask)
}

modrinth {
    token = System.getenv("MODRINTH_TOKEN")
    projectId = System.getenv("MODRINTH_PROJECT") ?: "subauth"
    versionName = "${project.version} Bukkit"
    versionType = if ((project.version as String).contains("SNAPSHOT")) "beta" else "release"
    uploadFile = distJarTask.outputs.files.first()
    gameVersions.addAll(getMinecraftVersions())
    loaders.addAll("paper", "purpur", "spigot", "bukkit")
}

tasks.processResources {
    val props = mapOf(Pair("version", version))
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}