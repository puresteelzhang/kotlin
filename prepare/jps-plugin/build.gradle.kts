import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

description = "Kotlin JPS plugin"

plugins {
    `java-base`
}

val projectsToShadow = listOf(
        ":kotlin-build-common",
        ":compiler:cli-common",
        ":kotlin-compiler-runner",
        ":kotlin-daemon-client",
        ":compiler:daemon-common",
        ":core:descriptors",
        ":core:descriptors.jvm",
        ":idea:idea-jps-common",
        ":jps-plugin",
        ":kotlin-preloader",
        ":compiler:util",
        ":core:util.runtime",
        ":plugins:android-extensions-jps")

val fatJarContents by configurations.creating

dependencies {
    projectsToShadow.forEach {
        fatJarContents(project(it)) { isTransitive = false }
    }
}

runtimeJar<ShadowJar>(task<ShadowJar>("jar")) {
    manifest.attributes.put("Main-Class", "org.jetbrains.kotlin.runner.Main")
    manifest.attributes.put("Class-Path", "kotlin-stdlib.jar")
    from(fatJarContents)
    from(files("$rootDir/resources/kotlinManifest.properties"))
}

ideaPlugin("lib/jps")
