rootProject.name = "UIKT"
rootProject.buildFileName = "build.gradle.kts"

include(":app")
include(":uikt")
include(":compiler")
include(":annotation")

pluginManagement {
	repositories {
		google()
		mavenCentral()
		gradlePluginPortal()
	}
}
dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		google()
		mavenCentral()
		maven { url = uri("https://www.jitpack.io") }
	}
}