plugins {
    id("shared")
    id("com.gradleup.shadow") version "9.6.1"
}

dependencies {
    implementation(project(":paper"))
}

tasks {
    shadowJar {
        relocate("org.bstats", project.group.toString())
    }
    build {
        dependsOn(shadowJar)
    }
}