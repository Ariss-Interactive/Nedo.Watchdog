plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.serialization") version "2.1.20"
    id("org.graalvm.buildtools.native") version "0.10.6"
    id("application")
    id("java")
}

group = "ariss"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
    implementation("ch.qos.logback:logback-classic:1.5.18")

    implementation("net.mamoe.yamlkt:yamlkt:0.13.0")

    implementation("org.drewcarlson:ksubprocess:0.7.0")
}

application {
    mainClass = "ariss.MainKt"
}


graalvmNative {
    toolchainDetection.set(true)
    binaries {
        named("main") {
            imageName.set("Nedo.Watchdog")

            quickBuild.set(true)
        }
    }
}

tasks.jar {
    manifest.attributes["Main-Class"] = "ariss.MainKt"
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree) // OR .map { zipTree(it) }
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}