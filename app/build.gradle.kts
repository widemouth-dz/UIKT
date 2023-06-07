plugins {
	kotlin("android")
	id("com.android.application")
}

android {
	namespace = "wedo.widemouth.uikt.app"
	compileSdk = libs.versions.compileSdk.get().toInt()

	defaultConfig {
		applicationId = "wedo.widemouth.uikt.app"
		minSdk = libs.versions.minSdk.get().toInt()
		targetSdk = libs.versions.targetSdk.get().toInt()
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}


dependencies {
	implementation(project(":uikt"))

	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.appcompat)
	implementation(libs.android.material)

	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.test.ext. junit)
	androidTestImplementation(libs.androidx.test.espresso.core)
}