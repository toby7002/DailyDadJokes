repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    val api_version: String by project
    val mccoroutine_version: String by project
    val kotlinx_coroutines_core_version: String by project

    implementation(project(":common"))
    implementation("com.github.shynixn.mccoroutine:mccoroutine-folia-api:$mccoroutine_version")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-folia-core:$mccoroutine_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinx_coroutines_core_version")

    compileOnly("dev.folia:folia-api:$api_version")
}