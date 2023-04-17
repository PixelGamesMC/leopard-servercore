package eu.pixelgamesmc.minecraft.servercore.listener

import eu.pixelgamesmc.minecraft.servercore.database.PixelDatabase
import eu.pixelgamesmc.minecraft.servercore.database.collection.PlayerCollection
import eu.pixelgamesmc.minecraft.servercore.PixelPermissible
import eu.pixelgamesmc.minecraft.servercore.utility.CraftClass
import eu.pixelgamesmc.minecraft.servercore.utility.PlayerUtil
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerJoinEvent

class PlayerConnectionListener: Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun asyncPlayerPreLogin(event: AsyncPlayerPreLoginEvent) {
        PixelDatabase.getCollections(PlayerCollection::class).forEach { playerCollection ->
            playerCollection.playerLogin(event.uniqueId, event.name, event.playerProfile.textures.skin.toString())
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun playerJoin(event: PlayerJoinEvent) {
        val player = event.player
        Bukkit.getOnlinePlayers().forEach { onlinePlayer ->
            PlayerUtil.updatePlayerList(player, onlinePlayer)
            PlayerUtil.updatePlayerList(onlinePlayer, player)
        }

        injectPermission(player)
    }

    private fun injectPermission(player: Player) {
        val clazz = CraftClass.getClass("entity.CraftHumanEntity")
        val field = clazz.getDeclaredField("perm")
        field.isAccessible = true
        field.set(player, PixelPermissible(player))
        field.isAccessible = false

        player.isOp = false
        player.updateCommands()
    }
}