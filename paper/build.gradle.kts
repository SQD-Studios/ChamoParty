plugins {
    id("shared")
    id("com.gradleup.shadow") version "9.6.1"
    id("xyz.jpenilla.run-paper") version "3.1.0"
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.2.build.+") { // Exclude these "vulnerable" dependencies, and we didn't need them anyway.
        exclude("org.codehaus.plexus", "plexus-utils")
        exclude("org.apache.commons", "commons-lang3")
    }

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
            modrinth("XPQ42u1g", "1.1.1.7")
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

        dependsOn(processResources)
    }
}