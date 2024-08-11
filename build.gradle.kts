plugins {
	java
	id("org.springframework.boot") version "3.3.2"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.icetea.project"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.modelmapper:modelmapper:3.1.1")
	implementation("com.oracle.database.jdbc:ojdbc8:23.2.0.0")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation ("com.querydsl:querydsl-jpa:5.0.0:jakarta")//
	implementation("org.springframework.boot:spring-boot-starter-mail:3.3.0")

	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")


	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor ("com.querydsl:querydsl-apt:5.0.0:jakarta")//
	annotationProcessor ("jakarta.annotation:jakarta.annotation-api")//
	annotationProcessor ("jakarta.persistence:jakarta.persistence-api")//

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
