# ChamoParty

[![Build](https://img.shields.io/github/actions/workflow/status/SQD-Studios/ChamoParty/gradle.yml?style=flat-square)](https://github.com/SQD-Studios/ChamoItemSkins/actions)
[![Documentation](https://img.shields.io/badge/Documentation-8A2BE2?style=flat-square)](https://sqd-studios.github.io/docs/chamoparty/administration/introduction/installing/)

**A fork of zVoteParty to modernize the VoteParty plugins. Seriously, the most modern one was updated 5 years ago** <br>

Here is the checklist:<br>

- [x] Hook into NuVotifier (Idk how we ended up like this)<br>
- [x] Hook into zMenu<br>
- [ ] Better MySQL Support<br>
- [x] Use better systems<br>
- [x] Modern Version support (Kind of?)<br>
- [ ] Good Api<br>
- [x] Production Ready<br>

## Building

Build with PowerShell or a Linux Console

```shell
./gradlew shadowJar
```

Build with a Windows CMD

```shell
gradlew shadowJar
```

## API

### Repository

<details>
<summary>Gradle Kotlin DSL</summary>

```kotlin
maven {
    name = "chamosmpRepoReleases"
    url = uri("https://maven.chamosmp.net/releases")
}
```

```kotlin
compileOnly("net.chamosmp.chamoparty:paper:version")
compileOnly("net.chamosmp.chamoparty:core:version")
```
</details>
<details>
<summary>Maven</summary>

```xml
<repository>
    <id>chamosmp-repo-releases</id>
    <name>ChamoSMP Maven Repository</name>
    <url>https://maven.chamosmp.net/releases</url>
</repository>
```

```xml
<dependency>
    <groupId>net.chamosmp.chamoparty</groupId>
    <artifactId>core</artifactId>
    <version>version</version>
</dependency>
<dependency> 
    <groupId>net.chamosmp.chamoparty</groupId>
    <artifactId>paper</artifactId>
    <version>version</version>
</dependency>
```
</details>
<details>
<summary>Gradle Groovy DSL</summary>

```groovy
maven {
    name = "chamosmpRepoReleases"
    url = "https://maven.chamosmp.net/releases"
}
```

```groovy
compileOnly "net.chamosmp.chamoparty:paper:version"
compileOnly "net.chamosmp.chamoparty:core:version"
```
</details>

## Want to get the original?

The original project on which this was based can be found and built here: https://github.com/Maxlego08/zVoteParty
