plugins {
    kotlin("jvm") version "2.0.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("com.diffplug.spotless") version "6.25.0"
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.8"
}

val version_major: String by project
val version_minor: String by project
val version_patch: String by project
val project_id: String by project

repositories { mavenCentral() }

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.github.johnrengelman.shadow")

    group = project_id
    version = "$version_major.$version_minor.$version_patch"

    repositories { mavenCentral() }

    val platform = project.name

    base { archivesName.set("$project_id-$platform") }

    java {
        withSourcesJar()
        toolchain.languageVersion = JavaLanguageVersion.of(21) // Kotlin can read this line
    }

    tasks {
        jar { archiveClassifier.set("noshade") }
        shadowJar {
            archiveClassifier.set("")
            archiveBaseName.set(archiveBaseName.get())
            archiveVersion.set(version.toString())
        }
        build { dependsOn(shadowJar) }
        processResources {
            val project_name: String by project
            val project_license: String by project
            val project_author: String by project
            val project_description: String by project

            filesMatching(listOf("META-INF/neoforge.mods.toml", "fabric.mod.json", "plugin.yml")) {
                expand(
                    mapOf(
                        "project_id" to project_id,
                        "project_name" to project_name,
                        "project_license" to project_license,
                        "project_version" to version,
                        "project_author" to project_author,
                        "project_description" to project_description
                    )
                )
            }
        }
    }
}

spotless {
    kotlin {
        endWithNewline()
        indentWithTabs()
        ktfmt().kotlinlangStyle().configure { it.setRemoveUnusedImport(true) }
    }

    kotlinGradle {
        target("*.gradle.kts")
        ktfmt().kotlinlangStyle()
    }

    format("dotfiles") {
        target(".gitignore", ".gitattributes", ".editorconfig")
        indentWithSpaces(2)
        trimTrailingWhitespace()
        endWithNewline()
    }
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}
