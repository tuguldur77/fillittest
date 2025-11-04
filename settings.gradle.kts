pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()       // 🔥 absolutely required for Firebase
        mavenCentral()
    }
}

rootProject.name = "fillit_android_compose"
include(":app")
