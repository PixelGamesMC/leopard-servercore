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
                val group = (targetUser.permissionGroups.mapNotNull { groupCollection.getGroup(it) } + groupCollection.getDefaultGroups()).minByOrNull { it.weight }

                if (group != null) {
                    val legacyAmpersand = LegacyComponentSerializer.legacyAmpersand()
                    val newMessage = if (targetUser.hasPermission("pixelgamesmc.chat.color")) {
                        val serializedMessage = legacyAmpersand.serialize(message)
                        legacyAmpersand.deserialize(serializedMessage)
                    } else {
                        message
                    }
                    legacyAmpersand.deserialize(group.prefix)
                        .append(sourceDisplayName.color(NamedTextColor.GRAY))
                        .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
                        .append(newMessage.colorIfAbsent(NamedTextColor.GRAY))
                } else {
                    Component.empty()
                }
            } else {
                Component.empty()
            }
        }
    }
}