import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.ByteArrayOutputStream
import kotlin.collections.listOf


plugins {
    id("org.springframework.boot") version "3.1.2"
    id("io.spring.dependency-management") version "1.1.2"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
    id("au.com.dius.pact") version "4.1.7"

}

group = "com.hakob"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("au.com.dius.pact.consumer:kotlin:4.6.1")
    testImplementation("au.com.dius.pact.consumer:junit5:4.6.1")
}

val getGitBranch = {
    val stdout = ByteArrayOutputStream()
    exec {
//        commandLine("git", "rev-parse", "--symbolic-full-name", "--abbrev-ref", "HEAD")
        commandLine("git", "log", "-n", "1", "--pretty=%d", "HEAD")
        standardOutput = stdout
    }
    stdout.toString().trim()
}

val getGitHash = {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine("git", "rev-parse", "--short", System.getenv("GITHUB_SHA"))
        standardOutput = stdout
    }
    stdout.toString().trim()
}

//def getGitBranch = { ->
//    def stdout = new ByteArrayOutputStream()
//    exec {
//        commandLine 'git', 'rev-parse', '--abbrev-ref', 'HEAD'
//        standardOutput = stdout
//    }
//    return stdout.toString().trim()
//}

pact {
    publish {
        pactDirectory = "build/pacts"
        pactBrokerUrl = "http://16.171.86.61/"
//        pactBrokerUsername = 'pact_workshop'
//        pactBrokerPassword = 'pact_workshop'
        tags = listOf(getGitBranch(), "test", "prod")
        consumerVersion = getGitHash()
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    println("hkeeeeee branch: ${getGitBranch()}")
    println("hkeeeeee git commit hash: ${getGitHash()}")
}
