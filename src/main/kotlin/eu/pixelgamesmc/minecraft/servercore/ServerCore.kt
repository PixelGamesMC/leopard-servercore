package eu.pixelgamesmc.minecraft.servercore

import eu.pixelgamesmc.minecraft.servercore.component.ComponentProvider
import eu.pixelgamesmc.minecraft.servercore.database.Credentials
import eu.pixelgamesmc.minecraft.servercore.database.PixelDatabase
import eu.pixelgamesmc.minecraft.servercore.database.collection.cache.CacheCollection
import eu.pixelgamesmc.minecraft.servercore.database.collection.currency.CurrencyCollection
import eu.pixelgamesmc.minecraft.servercore.database.collection.language.LanguageCollection
import eu.pixelgamesmc.minecraft.servercore.listener.InventoryListener
import eu.pixelgamesmc.minecraft.servercore.listener.PlayerConnectionListener
import eu.pixelgamesmc.minecraft.servercore.utility.PluginUtil
import org.bukkit.plugin.java.JavaPlugin
import org.litote.kmongo.getCollection

class ServerCore: JavaPlugin() {

    override fun onEnable() {
        ComponentProvider.loadComponents(this)

        PixelDatabase.connect(
            PluginUtil.loadConfiguration(this, "database", Credentials(
            Credentials.Mongo(true, "", "database"),
            Credentials.Redis(true, "", 2324, "", "")
        )))

        PixelDatabase.registerCollections { mongoDatabase ->
            listOf(
                CacheCollection(mongoDatabase.getCollection()),
                LanguageCollection(mongoDatabase.getCollection()),
                CurrencyCollection(mongoDatabase.getCollection())
            )
        }

        PluginUtil.registerEvents(this, InventoryListener(), PlayerConnectionListener())
    }

    override fun onDisable() {
        PixelDatabase.disconnect()
    }
}