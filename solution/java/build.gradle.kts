plugins {
    id("java")
}

group = "org.ythirion"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.vavr:vavr:0.10.4")
    compileOnly("org.projectlombok:lombok:1.18.26")

    annotationProcessor("org.projectlombok:lombok:1.18.26")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testCompileOnly("org.projectlombok:lombok:1.18.26")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.26")

    testImplementation("org.assertj:assertj-vavr:0.4.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.2")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("io.vavr:vavr-test:0.10.4")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}