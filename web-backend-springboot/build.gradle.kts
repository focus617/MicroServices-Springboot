import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.4"
    id("io.spring.dependency-management") version "1.0.14.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

repositories {
    mavenCentral()
}

group = "com.focus617"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

extra["springfoxVersion"] = "3.0.0"
extra["mockkVersion"] = "1.12.8"
extra["bootstrapVersion"] = "5.2.0"
extra["jqueryVersion"] = "3.6.1"

dependencies {
    // Web
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect")
    implementation("org.webjars:bootstrap:${property("bootstrapVersion")}")
    implementation("org.webjars.bower:jquery:${property("jqueryVersion")}")

    // Hot-Refresh
    implementation("org.springframework.boot:spring-boot-devtools")

    // JSON: Jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Database
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("mysql:mysql-connector-java")

    // Swagger for API document
    implementation("io.springfox:springfox-boot-starter:${property("springfoxVersion")}")

    // Service-Registry: Eureka
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.mockk:mockk:${property("mockkVersion")}")

}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}
