import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.ByteArrayOutputStream
import kotlin.collections.listOf

plugins {
    id("org.springframework.boot") version "3.1.2"
    id("io.spring.dependency-management") version "1.1.2"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
    id("au.com.dius.pact") version "4.6.2"

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
    if (System.getenv("EXTRACTED_BRANCH_NAME") != null) {
        // in CI pipeline
        val sourceBranchName = System.getenv("EXTRACTED_BRANCH_NAME")
        sourceBranchName
    } else {
        // locally
        val stdout = ByteArrayOutputStream()
        exec {
            commandLine("git", "rev-parse", "--symbolic-full-name", "--abbrev-ref", "HEAD")
            standardOutput = stdout
        }
        stdout.toString().trim()
    }
}

val getGitHash = {
    val stdout = ByteArrayOutputStream()
    exec {
        // commandLine("git", "rev-parse", "--short", System.getenv("GITHUB_SHA"))
        commandLine("git", "rev-parse", "--short", "HEAD")
        standardOutput = stdout
    }
    stdout.toString().trim()
}

pact {
    publish {
        pactDirectory = "build/pacts"
        pactBrokerUrl = "http://16.171.86.61/"
        tags = listOf(getGitBranch(), "test", "prod")
        consumerVersion = getGitHash()
    }
    broker {
        pactBrokerUrl = "http://16.171.86.61/"
        retryCountWhileUnknown = 7
        retryWhileUnknownInterval = 30

        // To use basic auth
//        pactBrokerUsername = '<USERNAME>'
//        pactBrokerPassword = '<PASSWORD>'

        // OR to use a bearer token
//        pactBrokerToken = '<TOKEN>'

        // Customise the authentication header from the default `Authorization`
//        pactBrokerAuthenticationHeader = 'my-auth-header'
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
}
