package eu.pixelgamesmc.minecraft.servercore.utility

import eu.pixelgamesmc.minecraft.servercore.database.PixelDatabase
import eu.pixelgamesmc.minecraft.servercore.database.collection.permission.group.PermissionGroupCollection
import eu.pixelgamesmc.minecraft.servercore.database.collection.permission.user.PermissionUserCollection
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object PlayerUtil {

    fun updatePlayerList(player: Player) {
        Bukkit.getOnlinePlayers().forEach { onlinePlayer ->
            updatePlayerList(player, onlinePlayer)
        }
    }

    fun updatePlayerList(player: Player, target: Player) {
        if (player.scoreboard == Bukkit.getScoreboardManager().mainScoreboard) {
            player.scoreboard = Bukkit.getScoreboardManager().newScoreboard
        }

        val userCollection = PixelDatabase.getCollection(PermissionUserCollection::class)
        val groupCollection = PixelDatabase.getCollection(PermissionGroupCollection::class)

        val targetUser = userCollection.getUser(target.uniqueId)
        if (targetUser != null) {
            val group = targetUser.permissionGroups.mapNotNull { groupCollection.getGroup(it) }.minByOrNull { it.weight } ?: groupCollection.getDefaultGroup()

            val scoreboard = player.scoreboard
            val team = scoreboard.getTeam("${group.weight}${group.name}") ?: scoreboard.registerNewTeam("${group.weight}${group.name}")
            team.prefix(LegacyComponentSerializer.legacyAmpersand().deserialize(group.prefix))
            team.color(group.color)
            team.addPlayer(target)
        }
    }
}