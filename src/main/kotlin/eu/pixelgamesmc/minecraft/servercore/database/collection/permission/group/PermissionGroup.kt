package eu.pixelgamesmc.minecraft.servercore.database.collection.permission.group

import eu.pixelgamesmc.minecraft.servercore.utility.codec.NamedTextColorSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.format.NamedTextColor

@Serializable
data class PermissionGroup(
    @SerialName("_id") val name: String,
    var weight: Int,
    var prefix: String,
    @Serializable(with = NamedTextColorSerializer::class) var color: NamedTextColor,
    val permissions: MutableList<String>,
    var default: Boolean,
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