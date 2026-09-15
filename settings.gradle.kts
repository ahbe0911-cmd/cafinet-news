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
    }
}

rootProject.name = "CafinetNews"

include(":app")

include(":core:common")
include(":core:network")
include(":core:database")
include(":core:ui")

include(":domain:model")
include(":domain:repository")
include(":domain:usecase")

include(":data:api")
include(":data:local")
include(":data:repository")
