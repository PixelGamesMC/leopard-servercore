package eu.pixelgamesmc.minecraft.servercore.listener

import eu.pixelgamesmc.minecraft.servercore.database.PixelDatabase
import eu.pixelgamesmc.minecraft.servercore.database.collection.permission.group.PermissionGroupCollection
import eu.pixelgamesmc.minecraft.servercore.database.collection.permission.user.PermissionUserCollection
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class PlayerChatListener: Listener {

    @EventHandler
    fun playerChat(event: AsyncChatEvent) {
        event.renderer { source, sourceDisplayName, message, _ ->
            val userCollection = PixelDatabase.getCollection(PermissionUserCollection::class)
            val groupCollection = PixelDatabase.getCollection(PermissionGroupCollection::class)

            val targetUser = userCollection.getUser(source.uniqueId)
            if (targetUser != null) {
                val group = targetUser.permissionGroups.mapNotNull { groupCollection.getGroup(it) }.minByOrNull { it.weight } ?: groupCollection.getDefaultGroup()

                LegacyComponentSerializer.legacyAmpersand().deserialize(group.prefix)
                    .append(sourceDisplayName.color(NamedTextColor.GRAY))
                    .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
                    .append(message.color(NamedTextColor.GRAY))
            } else {
                Component.empty()
            }
        }
    }
}