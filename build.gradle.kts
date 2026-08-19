plugins {
    id("shared")
    id("com.gradleup.shadow") version "9.6.1"
}

dependencies {
    implementation(project(":paper"))
}

tasks {
    shadowJar {
        dependsOn(project(":paper").tasks.shadowJar) // fine to keep for ordering, but not what fixes this
        relocate("org.bstats", project.group.toString())
    }
    build {
        dependsOn(shadowJar)
    }
}