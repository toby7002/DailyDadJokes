package dev.toby7002.ddj.bukkit

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import dev.toby7002.ddj.DailyDadJokes
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class DailyDadJokesBukkit : SuspendingJavaPlugin(), Listener {
    override suspend fun onEnableAsync() {
        server.pluginManager.registerSuspendingEvents(this, this)
        saveDefaultConfig()
    }

    @EventHandler
    private suspend fun onJoin(e: PlayerJoinEvent) {
        config.getString("message")?.let { e.player.sendMessage(it.replace("{{joke}}", DailyDadJokes.getAJoke())) }
    }
}