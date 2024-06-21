package dev.toby7002.ddj.bukkit

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import com.github.shynixn.mccoroutine.bukkit.setSuspendingExecutor
import dev.toby7002.ddj.DailyDadJokes
import dev.toby7002.ddj.bukkit.command.DDJCommandExecutor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.io.File

class DailyDadJokesBukkit : SuspendingJavaPlugin(), Listener {
    private var playersList: FileConfiguration? = null
    private var playersFile: File? = null

    override suspend fun onEnableAsync() {
        server.pluginManager.registerSuspendingEvents(this, this)
        saveDefaultConfig()
        initConfig()
        getCommand("dailydadjokes")!!.setSuspendingExecutor(DDJCommandExecutor(playersList, playersFile))
    }

    @EventHandler
    private suspend fun onJoin(e: PlayerJoinEvent) {
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