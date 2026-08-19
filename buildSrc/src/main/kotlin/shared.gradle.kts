plugins {
    id("java")
    id("maven-publish")
}

group = "net.chamosmp.chamoparty"
version = "1.0.0"
description = "ChamoParty, fork of zVoteParty, but just better"
java.sourceCompatibility = JavaVersion.VERSION_25
java.targetCompatibility = JavaVersion.VERSION_25

repositories {
    maven {
        url = uri("https://jitpack.io")
    }
    maven {
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
    maven {
        url = uri("https://repo.groupez.dev/releases")
    }
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "eldonexus"
        url = uri("https://eldonexus.de/repository/maven-public/")
    }
    mavenCentral()
}

tasks {
    withType<Javadoc> {
        options.encoding = "UTF-8"
    }
    withType<JavaCompile> {
        options.compilerArgs.add("-Xlint:deprecation")
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