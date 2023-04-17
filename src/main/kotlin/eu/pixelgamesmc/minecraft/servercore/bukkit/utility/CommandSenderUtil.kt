package eu.pixelgamesmc.minecraft.servercore.bukkit.utility

import eu.pixelgamesmc.minecraft.servercore.common.database.PixelDatabase
import eu.pixelgamesmc.minecraft.servercore.common.database.collection.language.LanguageCollection
import eu.pixelgamesmc.minecraft.servercore.common.database.collection.language.LanguageUser
import eu.pixelgamesmc.minecraft.servercore.bukkit.component.ComponentProvider
import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object CommandSenderUtil {

    fun getComponent(sender: CommandSender, identifier: String, key: String, vararg replacements: Pair<String, Any>): Component {
        return getComponents(sender, identifier, key, *replacements).single()
    }

    fun getComponents(sender: CommandSender, identifier: String, key: String, vararg replacements: Pair<String, Any>): List<Component> {
        var language = LanguageUser.Language.GERMAN
        if (sender is Player) {
            val languageCollection = PixelDatabase.getCollection(LanguageCollection::class)
            languageCollection.getUser(sender.uniqueId)?.let {
                language = it.language
            }
        }
        return ComponentProvider.getComponents(identifier, language, key, *replacements)
    }

    fun sendMessage(sender: CommandSender, prefix: Component?, identifier: String, key: String, vararg replacements: Pair<String, Any>) {
        sender.sendMessage(getComponent(sender, identifier, key, *replacements).run {
            prefix?.append(this) ?: this
        })
    }

    fun sendMessage(sender: CommandSender, identifier: String, key: String, vararg replacements: Pair<String, Any>) {
        sendMessage(sender, null, identifier, key, *replacements)
    }
}