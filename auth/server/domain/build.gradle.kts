import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.*
import org.jooq.meta.jaxb.Configuration
import org.jooq.meta.jaxb.Target

plugins {
    java
    `java-test-fixtures`

    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "org.dazai.booksourcing.auth.domain"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":shared"))
    api("tech.ydb.jdbc:ydb-jdbc-driver:2.3.7")
    api("tech.ydb:ydb-sdk-topic:2.3.9")
    implementation("tech.ydb.dialects:flyway-ydb-dialect:1.0.0")
    implementation("tech.ydb.dialects:jooq-ydb-dialect:1.1.0")
    api("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.security:spring-security-test")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    runtimeOnly("org.postgresql:postgresql")

    testFixturesApi("org.springframework.boot:spring-boot-starter-test")
    testFixturesApi("io.zonky.test:embedded-postgres:1.3.1")
    testFixturesApi("io.zonky.test:embedded-database-spring-test:2.1.1")
    testFixturesApi(enforcedPlatform("io.zonky.test.postgres:embedded-postgres-binaries-bom:14.3.0"))
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("tech.ydb.dialects:jooq-ydb-dialect:1.1.0")
        classpath("tech.ydb.jdbc:ydb-jdbc-driver-shaded:2.3.7")
        classpath("org.springframework.boot:spring-boot-starter-jdbc:3.4.2")
        classpath("org.jooq:jooq-codegen:3.19.18")
    }
}

tasks.register("generate") {
    GenerationTool.generate(
        Configuration().withGenerator(
            Generator().withDatabase(
                Database()
                    .withName("tech.ydb.jooq.codegen.YdbDatabase")
                    .withExcludes(".sys.*")
            )
                .withStrategy(
                    Strategy()
                    .withName("tech.ydb.jooq.codegen.YdbGeneratorStrategy")
                )
                .withGenerate(
                    Generate()
                ).withTarget(
                    Target()
                        .withPackageName("org.dazai.booksourcing.auth.domain.db")
                        .withDirectory("$projectDir/src/main/java/")
                )
        ).withJdbc(
            Jdbc().withUrl("jdbc:ydb:grpc://localhost:2136/local?usePrefixPath=auth_gen")
                .withDriver("tech.ydb.jdbc.YdbDriver")
        )
    )
}