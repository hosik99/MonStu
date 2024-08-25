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

	implementation("io.jsonwebtoken:jjwt-api:0.11.5")	//JWT
	implementation("io.jsonwebtoken:jjwt-impl:0.11.5")	//JWT
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")	//JWT
	implementation("jakarta.servlet:jakarta.servlet-api:5.0.0")//FOR JWT

	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")

	implementation ("com.querydsl:querydsl-jpa:5.0.0:jakarta")//querydsl

	implementation("org.springframework.boot:spring-boot-starter-mail:3.3.0")	//mail

	implementation("org.springframework.boot:spring-boot-starter-webflux")	//HTTP

//	implementation("io.netty:netty-resolver-dns-native-macos:4.1.68.Final")	//Mac-DNS
	runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.104.Final:osx-aarch_64")

	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")


	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor ("com.querydsl:querydsl-apt:5.0.0:jakarta")//querydsl
	annotationProcessor ("jakarta.annotation:jakarta.annotation-api")//querydsl
	annotationProcessor ("jakarta.persistence:jakarta.persistence-api")//querydsl

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
