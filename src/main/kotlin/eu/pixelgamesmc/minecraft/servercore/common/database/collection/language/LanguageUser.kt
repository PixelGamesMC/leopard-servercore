package eu.pixelgamesmc.minecraft.servercore.common.database.collection.language

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class LanguageUser(
    @SerialName("_id") @Contextual val uuid: UUID,
    var language: Language
) {
    enum class Language {
        GERMAN
    }
}