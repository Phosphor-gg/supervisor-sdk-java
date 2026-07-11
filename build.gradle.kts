plugins {
    `java-library`
    `maven-publish`
}

group = "gg.supervisor"
version = "0.2.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    testImplementation("org.junit.jupiter:junit-jupiter:6.1.1")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name.set("Supervisor SDK")
                description.set("Official Java SDK for the Supervisor content moderation API")
                url.set("https://supervisor.gg")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
            }
        }
    }
}
