package eu.pixelgamesmc.minecraft.servercore.bungee.utility

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin

object PluginUtil {

    val CONFIGURATION_JSON = Json {
        prettyPrint = true
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    inline fun <reified T: Any> loadConfiguration(plugin: Plugin, default: T): T {
        return loadConfiguration(plugin, default::class.simpleName.toString().lowercase(), default)
    }

    inline fun <reified T: Any> loadConfiguration(plugin: Plugin, name: String, default: T): T {
        val dataFolder = plugin.dataFolder
        if (!dataFolder.exists())
            dataFolder.mkdirs()

        val file = dataFolder.resolve("$name.json")
        if (!file.exists()) {
            saveConfiguration(plugin, name, default)
        }
        return CONFIGURATION_JSON.decodeFromString(file.readText())
    }

    inline fun <reified T: Any> loadConfigurations(plugin: Plugin, path: String): List<T> {
        val dataFolder = plugin.dataFolder
        if (!dataFolder.exists())
            dataFolder.mkdirs()

        val folder = dataFolder.resolve(path)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        val files = folder.listFiles()
        return files?.map { file ->
            CONFIGURATION_JSON.decodeFromString(file.readText())
        } ?: listOf()
    }

    inline fun <reified T : Any> saveConfiguration(plugin: Plugin, configuration: T) {
        saveConfiguration(plugin, configuration::class.simpleName.toString().lowercase(), configuration)
    }

    inline fun <reified T : Any> saveConfiguration(plugin: Plugin, name: String, configuration: T) {
        val json = CONFIGURATION_JSON.encodeToString(configuration)

        val dataFolder = plugin.dataFolder
        if (!dataFolder.exists())
            dataFolder.mkdirs()

        val file = dataFolder.resolve("$name.json")
        if (!file.exists())
            file.createNewFile()
        file.writeText(json)
    }

    fun registerEvents(plugin: Plugin, vararg listener: Listener) {
        listener.forEach { plugin.proxy.pluginManager.registerListener(plugin, it) }
    }

    fun registerCommands(plugin: Plugin, vararg command: Command) {
        command.forEach { plugin.proxy.pluginManager.registerCommand(plugin, it) }
    }
}