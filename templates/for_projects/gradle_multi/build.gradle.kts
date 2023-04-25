import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    // make kotlin plugin available to apply in subprojects
    kotlin("jvm") version "1.8.21"
}

repositories {
    mavenCentral()
}

subprojects {
    group = "ru.antonmarin.{someGroupHere}"
    // use kotlin for all subprojects
    apply(plugin = "org.jetbrains.kotlin.jvm")

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            // respect Spring nullability
            freeCompilerArgs = listOf("-Xjsr305=strict")
            // byte code version
            jvmTarget = "11"
            // error on warnings
            allWarningsAsErrors = true
        }
    }

    tasks.withType<Test> {
        // use JUnit5
        useJUnitPlatform()
        // enable logging exceptions
        testLogging {
            events(TestLogEvent.FAILED, TestLogEvent.SKIPPED)
            exceptionFormat = TestExceptionFormat.FULL
        }
    }
}
