rootProject.name = "subauth-multi"
include("bukkit")
include("core")
include("fabric-1-21-10")


pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://maven.fabricmc.net/") }
    }
}