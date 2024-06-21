repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    val api_version: String by project
    val mccoroutine_version: String by project

    implementation(project(":common"))
    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:$mccoroutine_version")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:$mccoroutine_version")

    compileOnly("org.spigotmc:spigot-api:$api_version")
}