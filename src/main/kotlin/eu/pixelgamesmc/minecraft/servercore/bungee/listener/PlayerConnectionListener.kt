package eu.pixelgamesmc.minecraft.servercore.bungee.listener

import eu.pixelgamesmc.minecraft.servercore.common.database.PixelDatabase
import eu.pixelgamesmc.minecraft.servercore.common.database.collection.PlayerCollection
import net.md_5.bungee.api.event.PreLoginEvent
import net.md_5.bungee.api.plugin.Listener
import org.bukkit.event.EventHandler

class PlayerConnectionListener: Listener {

    @EventHandler
    fun postLogin(event: PreLoginEvent) {
        PixelDatabase.getCollections(PlayerCollection::class).forEach { playerCollection ->
            playerCollection.playerLogin(event.connection.uniqueId, event.connection.name, "null")
        }
    }
}