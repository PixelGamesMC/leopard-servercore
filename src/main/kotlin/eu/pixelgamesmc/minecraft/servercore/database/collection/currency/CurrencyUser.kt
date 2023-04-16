package eu.pixelgamesmc.minecraft.servercore.database.collection.currency

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CurrencyUser(
    @SerialName("_id") @Contextual val uuid: UUID,
    val value: Long
)