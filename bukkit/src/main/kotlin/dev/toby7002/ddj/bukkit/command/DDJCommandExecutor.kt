package dev.toby7002.ddj.bukkit.command

import com.github.shynixn.mccoroutine.bukkit.SuspendingCommandExecutor
import org.bukkit.Color
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import java.io.File

class DDJCommandExecutor(private val playersList: FileConfiguration?, private val playersFile: File?): SuspendingCommandExecutor {
    override suspend fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (sender !is Player) {
            sender.sendMessage("You cannot run this command in here!")
            return false
        }
        val currentMode = playersList?.getBoolean(sender.name)!!

        if (currentMode) {
            sender.sendMessage("Disabled Daily Dad Jokes")
        } else {
            sender.sendMessage("Enabled Daily Dad Jokes")
        }

        playersList.set(sender.name, !currentMode)
        playersList.save(playersFile!!)

        return true
    }
}