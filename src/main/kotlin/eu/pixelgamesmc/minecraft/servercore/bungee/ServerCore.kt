package eu.pixelgamesmc.minecraft.servercore.bungee

import eu.pixelgamesmc.minecraft.servercore.bungee.listener.PlayerConnectionListener
import eu.pixelgamesmc.minecraft.servercore.bungee.listener.PlayerPermissionListener
import eu.pixelgamesmc.minecraft.servercore.bungee.utility.PluginUtil
import eu.pixelgamesmc.minecraft.servercore.common.database.Credentials
import eu.pixelgamesmc.minecraft.servercore.common.database.PixelDatabase
import eu.pixelgamesmc.minecraft.servercore.common.database.collection.cache.CacheCollection
import eu.pixelgamesmc.minecraft.servercore.common.database.collection.currency.CurrencyCollection
import eu.pixelgamesmc.minecraft.servercore.common.database.collection.language.LanguageCollection
import eu.pixelgamesmc.minecraft.servercore.common.database.collection.permission.group.PermissionGroupCollection
import eu.pixelgamesmc.minecraft.servercore.common.database.collection.permission.user.PermissionUserCollection
import net.md_5.bungee.api.plugin.Plugin
import org.litote.kmongo.getCollection

class ServerCore: Plugin() {

    override fun onEnable() {
        PixelDatabase.connect(
            PluginUtil.loadConfiguration(this, "database", Credentials(
                Credentials.Mongo(true, "", "database"),
                Credentials.Redis(true, "", 2324, "", "")
            ))
        )

        PixelDatabase.registerCollections { jedisPool, mongoDatabase ->
            listOf(
                CacheCollection(jedisPool, mongoDatabase.getCollection()),
                LanguageCollection(jedisPool, mongoDatabase.getCollection()),
                PermissionUserCollection(jedisPool, mongoDatabase.getCollection()),
                PermissionGroupCollection(jedisPool, mongoDatabase.getCollection()),
                CurrencyCollection(jedisPool, mongoDatabase.getCollection())
            )
        }

        PluginUtil.registerEvents(this, PlayerConnectionListener(), PlayerPermissionListener())
    }
}