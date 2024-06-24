package dev.toby7002.ddj.folia

import com.github.shynixn.mccoroutine.folia.*
import dev.toby7002.ddj.DailyDadJokes
import dev.toby7002.ddj.folia.command.DDJCommandExecutor
import kotlinx.coroutines.withContext
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.io.File
import kotlin.coroutines.CoroutineContext
import kotlin.Pair

class DailyDadJokesFolia: SuspendingJavaPlugin(), Listener {
    private var playersList: FileConfiguration? = null
    private var playersFile: File? = null

    override suspend fun onEnableAsync() {
        val eventDispatcher = mapOf<Class<out Event>, (event: Event) -> CoroutineContext>(
            Pair(PlayerJoinEvent::class.java) {
                require(it is PlayerJoinEvent)
                entityDispatcher(it.player)
            }
        )
        server.pluginManager.registerSuspendingEvents(this, this, eventDispatcher)
        saveDefaultConfig()
        initConfig()
        getCommand("dailydadjokes")!!.setSuspendingExecutor(DDJCommandExecutor(playersList, playersFile))
    }

    @EventHandler
    suspend fun onJoin(e: PlayerJoinEvent) {
        withContext(mainDispatcher) {
            val playerName = e.player.name
            val player = playersList?.get(playerName)

            if (player == null) {
                playersList?.set(playerName, config.getBoolean("enableByDefault"))
                playersList?.save(playersFile!!)
            }

            if (playersList?.get(playerName) == true) {
                config.getString("message")?.let { e.player.sendMessage(it.replace("{{joke}}", DailyDadJokes.getAJoke())) }
            }
        }
    }

    private fun initConfig() {
        val playersFilename = "players.yml"
        playersFile = File(dataFolder, playersFilename)

        if (!playersFile!!.exists()) {
            playersFile!!.parentFile.mkdirs()
            saveResource(playersFilename, false)
        }

        playersList = YamlConfiguration()
        playersList?.load(playersFile!!)
    }
}