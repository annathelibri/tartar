plugins {
    kotlin("multiplatform") version "1.7.20"
    `maven-publish`
    id("org.jetbrains.dokka") version "1.7.10"
}

group = "net.notjustanna"
version = "4.1.0"

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "13"
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(BOTH) {
        browser()
        nodejs()
    }
    linuxX64()
    linuxArm64()
    macosX64()
    macosArm64()
    mingwX64()

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting
        val commonNonJvmMain by creating {
            dependsOn(commonMain)
        }
        val jsMain by getting {
            dependsOn(commonNonJvmMain)
        }
        val jsTest by getting
        val nativeMain by creating {
            dependsOn(commonNonJvmMain)
        }
        val linuxX64Main by getting {
            dependsOn(nativeMain)
        }
        val linuxArm64Main by getting {
            dependsOn(nativeMain)
        }
        val mingwX64Main by getting {
            dependsOn(nativeMain)
        }
        val macosX64Main by getting {
            dependsOn(nativeMain)
        }
        val macosArm64Main by getting {
            dependsOn(nativeMain)
        }
    }
}


tasks {
    dokkaHtml.configure {
        dokkaSourceSets {
            configureEach {
                if (name == "commonNonJvmMain") {
                    displayName.set("common (non-jvm)")
                }
                includes.from("dokka_modules.md")
            }
        }
    }
    register<Jar>("dokkaJar") {
        from(dokkaHtml)
        dependsOn(dokkaHtml)
        archiveClassifier.set("javadoc")
    }
}

publishing {
    publications.withType<MavenPublication> {
        artifact(tasks["dokkaJar"])
    }

    repositories {
        maven {
            url = uri("https://maven.cafeteria.dev/releases")

            credentials {
                username = "${project.findProperty("mcdUsername") ?: System.getenv("MCD_USERNAME")}"
                password = "${project.findProperty("mcdPassword") ?: System.getenv("MCD_PASSWORD")}"
            }
            authentication {
                create("basic", BasicAuthentication::class.java)
            }
        }
        mavenLocal()
    }
}
