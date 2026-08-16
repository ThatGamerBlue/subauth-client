rootProject.name = "subauth-multi"
include("bukkit")
include("core")
include("fabric-1-20-1")
include("fabric-1-21-10")
include("fabric-1-21-11")
include("fabric-26-1")
include("fabric-26-2")


pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://maven.fabricmc.net/") }
    }

    plugins {
        val loomVersion = "1.17-SNAPSHOT"
        id ("net.fabricmc.fabric-loom") version loomVersion
        id("net.fabricmc.fabric-loom-remap") version loomVersion
    }
}