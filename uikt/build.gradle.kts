plugins {
	kotlin("android")
	id("com.android.library")
	id("com.google.devtools.ksp")
}

android {
	namespace = "wedo.widemouth.uikt"
	compileSdk = libs.versions.compileSdk.get().toInt()

	defaultConfig {
		minSdk = libs.versions.minSdk.get().toInt()
		targetSdk = libs.versions.targetSdk.get().toInt()

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		consumerProguardFiles("consumer-rules.pro")
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
		freeCompilerArgs = freeCompilerArgs + "-Xcontext-receivers"
	}

	sourceSets.configureEach {
		kotlin.srcDir("$buildDir/generated/ksp/$name/kotlin/")
	}

}

dependencies {

	implementation(project(":annotation"))
	ksp(project(":compiler"))

	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.appcompat)
	implementation(libs.android.material)

	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.test.ext.junit)
	androidTestImplementation(libs.androidx.test.espresso.core)
}