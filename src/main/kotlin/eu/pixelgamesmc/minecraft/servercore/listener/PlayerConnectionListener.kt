package eu.pixelgamesmc.minecraft.servercore.listener

import eu.pixelgamesmc.minecraft.servercore.database.PixelDatabase
import eu.pixelgamesmc.minecraft.servercore.database.collection.PlayerCollection
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent

class PlayerConnectionListener: Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun asyncPlayerPreLogin(event: AsyncPlayerPreLoginEvent) {
        PixelDatabase.getCollections(PlayerCollection::class).forEach { playerCollection ->
            playerCollection.playerLogin(event.uniqueId, event.name, event.playerProfile.textures.skin.toString())
        }
    }
}