plugins {
    id("java")
    id("maven-publish")
    id("com.gradleup.shadow") version "9.6.1"
    id("xyz.jpenilla.run-paper") version "3.0.2"
}

java.sourceCompatibility = JavaVersion.VERSION_25

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.2.build.+")

    compileOnly("redis.clients:jedis:5.1.3")
    compileOnly("com.zaxxer:HikariCP:4.0.3")
    implementation("org.bstats:bstats-bukkit:3.2.1")

    compileOnly("com.github.NuVotifier.NuVotifier:nuvotifier-api:2.7.2")
    compileOnly("com.github.NuVotifier.NuVotifier:nuvotifier-bukkit:2.7.2")
    compileOnly("fr.maxlego08.menu:zmenu-api:1.1.1.4")
    compileOnly("me.clip:placeholderapi:2.11.6")

    compileOnly("net.strokkur.commands:annotations-paper:2.1.1")
    annotationProcessor("net.strokkur.commands:processor-paper:2.1.1")

    implementation(project(":core"))
}

tasks {
    runServer {
        downloadPlugins {
            github("NuVotifier", "NuVotifier", "v2.7.3", "nuvotifier.jar")
            modrinth("XPQ42u1g", "1.1.1.6")
            github("PlaceholderAPI", "PlaceholderAPI", "2.12.3", "PlaceholderAPI-2.12.3.jar")
        }

        minecraftVersion("26.2")
    }
    runPaper.folia.registerTask()

    processResources {
        val props = mapOf("version" to project.version, "description" to project.description)
        filesMatching("paper-plugin.yml") {
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