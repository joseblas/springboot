
plugins {
    id("java-library")
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.openapi.generator") version "7.9.0"

    id("software.amazon.smithy.gradle.smithy-jar").version("1.1.0")
}

group = "com.jt"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}


java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}


configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}



dependencies {

    implementation("org.springframework.boot:spring-boot-starter") {
        exclude(group = "commons-logging", module = "commons-logging")
    }
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.r2dbc:r2dbc-pool")
    implementation("org.postgresql:r2dbc-postgresql")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.6.0")
    implementation("org.springdoc:springdoc-openapi-webflux-ui:1.8.0")

    implementation("org.openapitools:jackson-databind-nullable:0.2.6")

    implementation("software.amazon.smithy:smithy-model:[1.0, 2.0[")
    implementation("software.amazon.smithy:smithy-codegen-core:[1.0, 2.0[")
    implementation("software.amazon.smithy:smithy-openapi:[1.0, 2.0[") // Ensure this matches your plugin version
    implementation("io.swagger.codegen.v3:swagger-codegen:3.0.62") // For OpenAPI to Java code generation
}

openApiGenerate {
    generatorName = "spring"
    apiNameSuffix = "PriceController"
    inputSpec = "$rootDir/build/smithyprojections/${rootProject.name}/openapi/openapi/PriceControllerApi.openapi.json"
    outputDir = "$buildDir/generated"
    apiPackage = "com.jt.openapi"
    modelPackage = "com.jt.model"
    configOptions = mapOf("library" to "spring-boot",
        "useTags" to "false",
        "useSpringBoot3" to "true",
        "interfaceOnly" to "true",
        "reactive" to "true",
        "dateLibrary" to "java8"
    )
    // Este mapping es para facilitar esta prueba, pero deberia hacerse con un converter que genere in integer desde smithy.
    typeMappings = mapOf(
        "number" to "int",
        "OffsetDateTime" to "java.time.LocalDateTime",
        "ZonedDateTime"  to "java.time.LocalDateTime"
    )

    importMappings = mapOf(
        "number" to "int",
        "java.time.Offseto9DateTime" to "java.time.LocalDateTime",
        "java.time.ZonedDateTime"  to "java.time.LocalDateTime"
    )
}

tasks.named("openApiGenerate") {
    dependsOn("smithyBuild")
}

tasks.named("compileJava") {
    dependsOn("openApiGenerate")
}
sourceSets {
    main {
        java {
            srcDir("$buildDir/generated/src/main/java")
        }
    }
}

tasks.jar {
    enabled = false
}

tasks.test {
    // Show standard output and standard error for every test case
    testLogging {
        // Show the output of the tests
        showStandardStreams = true

        // Display test events, useful for debugging
        events("passed", "skipped", "failed")

        // Display full stack traces for exceptions
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }

    // Log the outcome of each test case
    afterTest(KotlinClosure2<TestDescriptor, TestResult, Unit>({ descriptor, result ->
        logger.lifecycle("Test: ${descriptor.name} [${result.resultType}]")
    }))
}

java.sourceSets["main"].java {
    srcDirs("model", "src/main/smithy")
}
