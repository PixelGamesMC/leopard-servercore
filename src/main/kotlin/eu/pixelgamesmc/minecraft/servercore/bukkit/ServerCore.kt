package eu.pixelgamesmc.minecraft.servercore.bukkit

import eu.pixelgamesmc.minecraft.servercore.bukkit.component.ComponentProvider
import eu.pixelgamesmc.minecraft.servercore.common.database.Credentials
import eu.pixelgamesmc.minecraft.servercore.common.database.PixelDatabase
import eu.pixelgamesmc.minecraft.servercore.common.database.collection.cache.CacheCollection
import eu.pixelgamesmc.minecraft.servercore.common.database.collection.currency.CurrencyCollection
import eu.pixelgamesmc.minecraft.servercore.common.database.collection.language.LanguageCollection
import eu.pixelgamesmc.minecraft.servercore.common.database.collection.permission.group.PermissionGroupCollection
import eu.pixelgamesmc.minecraft.servercore.common.database.collection.permission.user.PermissionUserCollection
import eu.pixelgamesmc.minecraft.servercore.bukkit.listener.InventoryListener
import eu.pixelgamesmc.minecraft.servercore.bukkit.listener.PlayerChatListener
import eu.pixelgamesmc.minecraft.servercore.bukkit.listener.PlayerConnectionListener
import eu.pixelgamesmc.minecraft.servercore.bukkit.utility.PluginUtil
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

        PixelDatabase.registerCollections { jedisPool, mongoDatabase ->
            listOf(
                CacheCollection(jedisPool, mongoDatabase.getCollection()),
                LanguageCollection(jedisPool, mongoDatabase.getCollection()),
                PermissionUserCollection(jedisPool, mongoDatabase.getCollection()),
                PermissionGroupCollection(jedisPool, mongoDatabase.getCollection()),
                CurrencyCollection(jedisPool, mongoDatabase.getCollection())
            )
        }

        PluginUtil.registerEvents(this, InventoryListener(), PlayerChatListener(), PlayerConnectionListener())
    }

    override fun onDisable() {
        PixelDatabase.disconnect()
    }
}