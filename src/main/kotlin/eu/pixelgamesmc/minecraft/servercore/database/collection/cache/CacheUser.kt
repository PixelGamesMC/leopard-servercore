package eu.pixelgamesmc.minecraft.servercore.database.collection.cache

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CacheUser(
    @SerialName("_id") @Contextual val uuid: UUID,
    val name: String,
    val skin: String,
)