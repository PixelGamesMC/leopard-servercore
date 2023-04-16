package eu.pixelgamesmc.minecraft.servercore.component

import eu.pixelgamesmc.minecraft.servercore.database.collection.language.LanguageUser
import eu.pixelgamesmc.minecraft.servercore.utility.PluginUtil
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.plugin.Plugin

object ComponentProvider {

    private val configurations: MutableList<Components> = mutableListOf()

    fun loadComponents(plugin: Plugin) {
        val components = PluginUtil.loadConfigurations<Components>(plugin, "components")
        components.forEach { value ->
            if (configurations.find { it.identifier == value.identifier } != null) {
                throw RuntimeException("Components with identifier ${value.identifier} already exists")
            }
            configurations.add(value)
        }
    }

    fun getCoreComponent(identifier: String, key: String, vararg replacements: Pair<String, Any>): Component {
        return getCoreComponents(identifier, key, *replacements).single()
    }

    fun getCoreComponents(identifier: String, key: String, vararg replacements: Pair<String, Any>): List<Component> {
        val configuration = configurations.find { it.identifier == identifier }
        val component = configuration?.coreComponents?.get(key)
            ?: ""

        return convertStringToComponents(component, *replacements)
    }

    fun getComponents(identifier: String, language: LanguageUser.Language, key: String, vararg replacements: Pair<String, Any>): List<Component> {
        val configuration = configurations.find { it.identifier == identifier }
        val languageContainer = configuration?.messages?.find { it.language == language }
        val component = languageContainer?.components?.get(key)
            ?: ""

        return convertStringToComponents(component, *replacements)
    }

    private fun convertStringToComponents(component: String, vararg replacements: Pair<String, Any>): List<Component> {
        var value = component
        replacements.forEach { replacement ->
            value = value.replace(replacement.first, replacement.second.toString())
        }
        val lines = value.split("\n")
        return lines.map { LegacyComponentSerializer.legacyAmpersand().deserialize(it).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE) }
    }
}