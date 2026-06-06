# ChamoParty

> **A modern fork of zVoteParty** — rebuilt with better systems, modern version support, and a cleaner API.

[![License: GPL v3](https://img.shields.io/badge/Code%20License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![License: MIT](https://img.shields.io/badge/Wiki%20License-MIT-green.svg)](https://opensource.org/licenses/MIT)
[![Maven](https://img.shields.io/badge/Maven-maven.chamosmp.net-orange)](https://maven.chamosmp.net/releases)

ChamoParty is a modernized fork of the zVoteParty plugin, rewritten with cleaner internals, modern Minecraft version support, and a proper API for developers.

---

## Roadmap

- [x] Hook into NuVotifier (Idk how we ended up like this)
- [x] Hook into zMenu
- [ ] Better MySQL Support
- [x] Use better systems
- [x] Modern Version support (Kind of?)
- [ ] Good Api
- [x] Production Ready
- [x] Successfully rename everything to ChamoParty (or chamoparty)

---

## Building

Make sure you have **Java 25+** installed before building.

**PowerShell / Linux / macOS**
```shell
./gradlew shadowJar
```

**Windows CMD**
```shell
gradlew shadowJar
```

The compiled JAR will be output to `build/libs/ChamoParty-version-all.jar`.

---

## API

ChamoParty is available on the ChamoSMP Maven repository. Replace `version` with the version you want to target.

> [!WARNING]
> The API is still a work-in-progress. Breaking changes may occur between versions until a stable release is published.

### Gradle (Kotlin DSL) — `build.gradle.kts`

```kotlin
repositories {
    maven {
        name = "chamosmpRepoReleases"
        url = uri("https://maven.chamosmp.net/releases")
    }
}

dependencies {
    compileOnly("net.chamosmp:ChamoParty:version")
}
```

### Maven — `pom.xml`

```xml
<repositories>
  <repository>
    <id>chamosmp-repo-releases</id>
    <name>ChamoSMP Maven Repository</name>
    <url>https://maven.chamosmp.net/releases</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>net.chamosmp</groupId>
    <artifactId>ChamoParty</artifactId>
    <version>version</version>
    <scope>provided</scope>
  </dependency>
</dependencies>
```

### Plugin Dependency (`plugin.yml`)

Add ChamoParty as a soft or hard dependency in your plugin manifest:

```yaml
depend: [ChamoParty]
# or, if optional:
softdepend: [ChamoParty]
```

---

## FAQ

### Will zVoteParty add-ons work with ChamoParty?

**No.** ChamoParty has moved all classes to `net.chamosmp.chamoparty` and renamed the plugin to `ChamoParty`. Any add-ons built against zVoteParty's class paths will be incompatible. The API surface is largely the same, but the package and plugin name changes break binary compatibility.

### Is this a drop-in replacement for zVoteParty?

**No.** While most config options carry over, the config structure has changed and the internal package paths are different. You will need to review and migrate your configuration when switching from zVoteParty.

### What Minecraft versions are supported?

1.20 and above are supported.

### Where can I report bugs or request features?

Open an issue on the GitHub repository.

---

## License

| Component | License |
|---|---|
| Plugin Code | [GPL v3](https://www.gnu.org/licenses/gpl-3.0) |
| Wiki / Documentation | [MIT](https://opensource.org/licenses/MIT) |
