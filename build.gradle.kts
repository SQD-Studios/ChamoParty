plugins {
    id("java-library")
    id("maven-publish")
    id("com.gradleup.shadow") version "9.4.1"
    id("xyz.jpenilla.run-paper") version "3.0.2"
}

group = "net.chamosmp"
version = "0.0.1"
description = "ChamoParty v0.0.1 ready to thrive"
java.sourceCompatibility = JavaVersion.VERSION_25

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }

    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    maven {
        url = uri("https://libraries.minecraft.net/")
    }

    maven {
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }

    maven {
        url = uri("https://repo.groupez.dev/releases")
    }

    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
    maven {
        name = "tcoded-releases"
        url = uri("https://repo.tcoded.com/releases")
    }
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    /*maven {
        name = "chamosmpRepoReleases"
        url = uri("https://maven.chamosmp.net/releases")
    }
     */
    maven {
        name = "eldonexus"
        url = uri("https://eldonexus.de/repository/maven-public/")
    }
}
dependencies {
    compileOnly("io.papermc.paper:paper-api:26.1.2.build.+")
    compileOnly("redis.clients:jedis:5.1.3")
    compileOnly("com.github.NuVotifier.NuVotifier:nuvotifier-api:2.7.2")
    compileOnly("com.github.NuVotifier.NuVotifier:nuvotifier-bukkit:2.7.2")
    implementation("com.mojang:authlib:4.0.43")
    compileOnly("fr.maxlego08.menu:zmenu-api:1.1.1.4")
    compileOnly("me.clip:placeholderapi:2.11.6")
    implementation("commons-lang:commons-lang:2.6")
    //compileOnly("com.github.technicallycoded:FoliaLib:0.4.3")
    implementation("com.tcoded:FoliaLib:0.5.1")
    implementation("org.bstats:bstats-bukkit:3.2.1")
    compileOnly("net.strokkur.commands:annotations-paper:2.1.1")
    annotationProcessor("net.strokkur.commands:processor-paper:2.1.1")

}

tasks.shadowJar {
    configurations = project.configurations.runtimeClasspath.map { setOf(it) }

    dependencies {

    }
    relocate("com.tcoded.folialib", "net.chamosmp.zvoteparty.libs.folialib")
    relocate("org.bstats", project.group.toString())
}




tasks.withType<JavaCompile>() {
    options.compilerArgs.add("-Xlint:deprecation")
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}

tasks {
    runServer {
        downloadPlugins {
            github("NuVotifier", "NuVotifier", "v2.7.3", "nuvotifier.jar")
            github("Maxlego08", "zMenu", "1.1.1.4", "zMenu-1.1.1.4.jar")
            github("PlaceholderAPI", "PlaceholderAPI", "2.12.2", "PlaceholderAPI-2.12.2.jar")
        }

        minecraftVersion("26.1.2")
    }
    runPaper.folia.registerTask()
}


tasks.processResources {
    val props = mapOf("version" to project.version)
    inputs.properties(props)
    filteringCharset = "UTF-8"

    filesMatching("plugin.yml") {
        expand(props)
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
