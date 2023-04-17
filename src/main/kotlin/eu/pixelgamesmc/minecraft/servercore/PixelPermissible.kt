package eu.pixelgamesmc.minecraft.servercore

import eu.pixelgamesmc.minecraft.servercore.database.PixelDatabase
import eu.pixelgamesmc.minecraft.servercore.database.collection.permission.group.PermissionGroupCollection
import eu.pixelgamesmc.minecraft.servercore.database.collection.permission.user.PermissionUserCollection
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissibleBase
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionAttachmentInfo

class PixelPermissible(private val player: Player): PermissibleBase(player) {

    override fun isOp(): Boolean {
        return false
    }

    override fun hasPermission(name: String): Boolean {
        val userCollection = PixelDatabase.getCollection(PermissionUserCollection::class)
        val groupCollection = PixelDatabase.getCollection(PermissionGroupCollection::class)
        val permissionUser = userCollection.getUser(player.uniqueId)
        if (permissionUser != null) {
            val permissionGroups = permissionUser.permissionGroups.mapNotNull { groupCollection.getGroup(it) } + groupCollection.getDefaultGroup()
            permissionGroups.forEach { permissionGroup ->
                if (permissionGroup.hasPermission(name)) {
                    return true
                }
            }
            return permissionUser.hasPermission(name)
        }
        return false
    }

    override fun hasPermission(permission: Permission): Boolean {
        return hasPermission(permission.name)
    }

    override fun isPermissionSet(name: String): Boolean {
        return hasPermission(name)
    }

    override fun isPermissionSet(permission: Permission): Boolean {
        return hasPermission(permission)
    }

    override fun getEffectivePermissions(): MutableSet<PermissionAttachmentInfo> {
        return mutableSetOf()
    }
}