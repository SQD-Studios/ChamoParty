import org.gradle.api.internal.provider.ValueSupplier.ValueProducer.task

plugins {
    id("java")
    id("maven-publish")
}

allprojects {
    group = "net.chamosmp.chamoparty"
    version = "0.1.0"
    description = "ChamoParty v0.0.1 ready to thrive"

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

    tasks.withType<JavaCompile> {
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

dependencies {
    compileOnly(project(":core"))
    implementation(project(":paper"))
}
