plugins {
	java
	idea
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
	jcenter()
	mavenCentral()
}

dependencies {
	testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
}

configure<JavaPluginConvention> {
	sourceCompatibility = JavaVersion.VERSION_14
}

tasks.test {
	useJUnitPlatform()
	testLogging {
		events("passed", "skipped", "failed")
	}
}

tasks.withType<JavaCompile> {
	options.encoding = "UTF-8"
}

