plugins {
    id("java")
    id("maven-publish")
    id("com.gradleup.shadow") version "9.6.1"
    id("xyz.jpenilla.run-velocity") version "3.1.0"
}

java.sourceCompatibility = JavaVersion.VERSION_25

dependencies {
    // Velocity and configurate
    compileOnly("com.velocitypowered:velocity-api:4.1.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:4.1.0-SNAPSHOT")

    // Database stuff
    compileOnly("redis.clients:jedis:5.1.3")
    compileOnly("com.zaxxer:HikariCP:4.0.3")

    // Plugin Dependencies
    implementation("org.bstats:bstats-velocity:3.2.1")
    compileOnly("com.github.NuVotifier.NuVotifier:nuvotifier-api:2.7.2")
    compileOnly("com.github.NuVotifier.NuVotifier:nuvotifier-velocity:2.7.2")

    // StrokkCommands
    compileOnly("net.strokkur.commands:annotations-velocity:2.1.1")
    annotationProcessor("net.strokkur.commands:processor-velocity:2.1.1")

    implementation(project(":core"))
}

tasks {
    runVelocity {
        velocityVersion("4.1.0-SNAPSHOT")
    }

    processResources {
        val props = mapOf("version" to project.version, "description" to project.description)
        filesMatching("velocity-plugin.json") {
            expand(props)
        }
    }

    shadowJar {
        configurations = project.configurations.runtimeClasspath.map { setOf(it) }
        relocate("org.bstats", project.group.toString())
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "ChamoSMP-Releases"
            url = uri("https://maven.chamosmp.net/releases")
            credentials {
                username = System.getenv("REPOSILITE_USER")
                password = System.getenv("REPOSILITE_TOKEN")
            }
        }
        maven {
            name = "ChamoSMP-Snapshots"
            url = uri("https://maven.chamosmp.net/snapshots")
            credentials {
                username = System.getenv("REPOSILITE_USER")
                password = System.getenv("REPOSILITE_TOKEN")
            }
        }
    }
}