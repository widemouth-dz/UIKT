plugins {
	`java-library`
	kotlin("jvm")
}

java {
	sourceCompatibility = JavaVersion.VERSION_1_8
	targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
	implementation(project(":annotation"))

	implementation(libs.kotlinpoet)
	implementation(libs.kotlinpoet.ksp)
	implementation(libs.ksp)
}