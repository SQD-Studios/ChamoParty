# ChamoParty
**A fork of zVoteParty to modernize the VoteParty plugins** <br>

> [!CAUTION]
> Keep in mind that this is still in progress and IS NOT PRODUCTION READY <br>

Here is the checklist:
- [x] Hook into NuVotifier (Idk how we ended up like this)
- [x] Hook into zMenu
- [ ] Better MySQL Support
- [x] Use better systems
- [x] Modern Version support (Kind of?)
- [ ] Good Api
- [x] Production Ready
- [x] Successfully rename everything to ChamoParty (or chamoparty)

# Building
Build with PowerShell or a Linux Console
``` shell
./gradlew shadowJar
```
Build with a Windows CMD
``` shell
gradlew shadowJar
```

# API
To use the api

Gradle Kotlin DSL (build.gradle.kts)
``` kotlin
maven {
    name = "chamosmpRepoReleases"
    url = uri("https://maven.chamosmp.net/releases")
}
```
``` kotlin
compileOnly("net.chamosmp:ChamoParty:version")
```
Maven (pom.xml)
``` xml
<repository>
  <id>chamosmp-repo-releases</id>
  <name>ChamoSMP Maven Repository</name>
  <url>https://maven.chamosmp.net/releases</url>
</repository>
```
``` xml
<dependency>
  <groupId>net.chamosmp</groupId>
  <artifactId>ChamoParty</artifactId>
  <version>version</version>
</dependency>
```
## FAQ
### **Add-Ons**<br>
Keep in mind that this plugin will not work with "addons" that worked with zVoteParty as it changed its classes to net.chamosmp.chamoparty and its plugin name to ChamoParty but that api has still remained the same
### **Drop-in Replacement**
This isn't a drop in replacement as it changed its structure to ChamoParty and its configs has changed a little but most is the same
