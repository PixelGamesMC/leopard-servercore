package eu.pixelgamesmc.minecraft.servercore.bukkit.component

import eu.pixelgamesmc.minecraft.servercore.common.database.collection.language.LanguageUser
import kotlinx.serialization.Serializable

@Serializable
data class Components(
    val identifier: String,
    val coreComponents: Map<String, String>,
    val messages: List<LanguageContainer>
) {
    @Serializable
    data class LanguageContainer(
        val language: LanguageUser.Language,
        val components: Map<String, String>
    )
}