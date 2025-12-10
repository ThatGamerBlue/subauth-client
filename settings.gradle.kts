rootProject.name = "subauth-multi"
include("bukkit")
include("core")
include("fabric-1-21-10")
include("fabric-1-21-11")


pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://maven.fabricmc.net/") }
    }
}