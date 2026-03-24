plugins {
}

group = "com.thatgamerblue.subauth"
version = "1.7"

repositories {
    mavenCentral()
}

subprojects {
    version = rootProject.version
}

tasks {
    register<Copy>("dist") {
        into("./dist/")
        subprojects.forEach {
            try {
                var distJar = it.tasks.named("distJar")
                if (distJar.isPresent) {
                    from(distJar.get().outputs)
                }
            } catch (ignored: UnknownTaskException) {
            }
        }
    }
}