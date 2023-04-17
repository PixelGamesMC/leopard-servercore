package eu.pixelgamesmc.minecraft.servercore.command

import eu.pixelgamesmc.minecraft.servercore.utility.CommandSenderUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

abstract class PixelCommand(name: String, private val onlyPlayers: Boolean, vararg aliases: String): Command(name) {

    init {
        @Suppress("LeakingThis")
        setAliases(aliases.toList())
    }

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            if (onlyPlayers) {
                CommandSenderUtil.sendMessage(sender, CommandSenderUtil.getComponent(sender, "core", "prefix"), "core", "command_only_players")
                return false
            }
        } else {
            permission?.let {
                if (!sender.hasPermission(it)) {
                    CommandSenderUtil.sendMessage(sender, CommandSenderUtil.getComponent(sender, "core", "prefix"), "core", "command_no_permission")
                    return false
                }
            }
        }

        return performCommand(sender, commandLabel, args)
    }

    abstract fun performCommand(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean
}