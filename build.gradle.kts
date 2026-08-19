plugins {
    id("shared")
    id("com.gradleup.shadow") version "9.6.1"
}

dependencies {
    implementation(project(":paper"))
}

tasks {
    shadowJar {
        dependsOn(project(":paper").tasks.shadowJar)
    }
    build {
        dependsOn(shadowJar)
    }
}