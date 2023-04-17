package eu.pixelgamesmc.minecraft.servercore.utility

import org.bukkit.Bukkit

object CraftClass {

    fun getVersion(): String {
        val name = Bukkit.getServer().javaClass.`package`.name
        return name.substring(name.lastIndexOf(".") + 1)
    }

    fun getClass(name: String): Class<*> {
        return Class.forName("org.bukkit.craftbukkit.${getVersion()}.$name")
    }
}