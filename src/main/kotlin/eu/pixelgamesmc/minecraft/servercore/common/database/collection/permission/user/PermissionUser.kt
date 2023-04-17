package eu.pixelgamesmc.minecraft.servercore.common.database.collection.permission.user

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class PermissionUser(
    @SerialName("_id") @Contextual val uuid: UUID,
    val permissions: MutableList<String>,
    val permissionGroups: MutableList<String>
) {

    fun hasPermission(name: String): Boolean {
        permissions.forEach { permission ->
            if (permission == "*" || permission == name || name.startsWith(permission)) {
                return true
            }
        }
        return false
    }
}