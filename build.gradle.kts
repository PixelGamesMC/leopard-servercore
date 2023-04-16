import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import java.io.ByteArrayOutputStream

plugins {
    kotlin("jvm") version "1.8.0"

    kotlin("plugin.serialization") version "1.8.10"

    id("io.papermc.paperweight.userdev") version "1.5.2"
    id("xyz.jpenilla.run-paper") version "2.0.1" // Adds runServer and runMojangMappedServer tasks for testing
    id("net.minecrell.plugin-yml.bukkit") version "0.5.3" // Generates plugin.yml

    // Shades and relocates dependencies into our plugin jar. See https://imperceptiblethoughts.com/shadow/introduction/
    id("com.github.johnrengelman.shadow") version "7.1.2"

    id("maven-publish")
}

val gitDescribe: String by lazy {
    val stdout = ByteArrayOutputStream()
    rootProject.exec {
        commandLine("git", "rev-parse", "--verify", "--short", "HEAD")
        standardOutput = stdout
    }
    stdout.toString().trim()
}

group = "eu.pixelgamesmc.minecraft"
version = gitDescribe

val pixelUsername: String by project
val pixelPassword: String by project

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    api("redis.clients:jedis:4.3.2")
    api("org.litote.kmongo:kmongo:4.8.0")
    api("org.litote.kmongo:kmongo-serialization:4.8.0")
    api("org.litote.kmongo:kmongo-id-serialization:4.8.0")

    paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")
}

kotlin {
    jvmToolchain(17)
}

tasks {
    test {
        useJUnitPlatform()
    }

    // Configure reobfJar to run when invoking the build task
    assemble {
        dependsOn(reobfJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything

        // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
        // See https://openjdk.java.net/jeps/247 for more information.
        options.release.set(17)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
    }

    /*
    reobfJar {
      // This is an example of how you might change the output location for reobfJar. It's recommended not to do this
      // for a variety of reasons, however it's asked frequently enough that an example of how to do it is included here.
      outputJar.set(layout.buildDirectory.file("libs/PaperweightTestPlugin-${project.version}.jar"))
    }

    shadowJar {
        // helper function to relocate a package into our package
        fun relocate(pkg: String) = relocate(pkg, "eu.pixelgamesmc.core.dependency.$pkg")

        // relocate cloud, and its transitive dependencies
        relocate("com.mongodb")
    }*/
}

bukkit {
    name = "ServerCore"
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    main = "eu.pixelgamesmc.minecraft.servercore.ServerCore"
    apiVersion = "1.19"
    authors = listOf("NitrinCloud")
}

publishing {
    publications {
        create<MavenPublication>("Version") {
            from(components["java"])

            artifact(tasks.kotlinSourcesJar.get())
        }

        create<MavenPublication>("Default") {
            version = "-SNAPSHOT"

            from(components["java"])

            artifact(tasks.kotlinSourcesJar.get())
        }
    }

    repositories {
        maven {
            credentials {
                username = pixelUsername
                password = pixelPassword
            }

            url = uri("https://repository.pixelgamesmc.eu/releases")
        }
    }
}