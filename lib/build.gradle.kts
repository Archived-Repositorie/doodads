plugins {
    `java-library`
    id("com.diffplug.spotless") version "6.21.0"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter", version = "5.10.0")
    testRuntimeOnly(group = "org.junit.platform", name = "junit-platform-launcher")
    compileOnly(group = "jakarta.annotation", name = "jakarta.annotation-api", version = "2.1.1")
}

tasks.test {
    useJUnitPlatform()
}

spotless {
    java {
        palantirJavaFormat()
        formatAnnotations()
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
