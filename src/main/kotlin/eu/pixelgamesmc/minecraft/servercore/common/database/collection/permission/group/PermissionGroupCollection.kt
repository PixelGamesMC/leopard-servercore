package eu.pixelgamesmc.minecraft.servercore.common.database.collection.permission.group

import com.mongodb.client.MongoCollection
import eu.pixelgamesmc.minecraft.servercore.common.database.collection.PixelCollection
import net.kyori.adventure.text.format.NamedTextColor
import org.litote.kmongo.eq
import redis.clients.jedis.JedisPool

class PermissionGroupCollection(
    jedisPool: JedisPool,
    collection: MongoCollection<PermissionGroup>
): PixelCollection<PermissionGroup>(
    jedisPool, collection
) {

    fun createGroup(name: String) {
        collection.insertOne(PermissionGroup(name, 0, "", NamedTextColor.WHITE, mutableListOf(), false))
    }

    fun deleteGroup(permissionGroup: PermissionGroup) {
        deleteCache("permission_group#${permissionGroup.name}", PermissionGroup::name eq permissionGroup.name)
    }

    fun update(permissionGroup: PermissionGroup) {
        updateCache("permission_group#${permissionGroup.name}", permissionGroup)
    }

    fun updateDefault(permissionGroup: PermissionGroup) {
        val currentDefaultGroup = getDefaultGroup()
        currentDefaultGroup.default = false
        update(currentDefaultGroup)

        permissionGroup.default = true
        updateCache("permission_group#default", permissionGroup)
    }

    fun getGroup(name: String): PermissionGroup? {
        return getCache("permission_group#$name", PermissionGroup::name eq name, PermissionGroup::class)
    }

    fun getDefaultGroup(): PermissionGroup {
        return getCache("permission_group#default", PermissionGroup::default eq true, PermissionGroup::class)
            ?: PermissionGroup("default", 0, "", NamedTextColor.WHITE, mutableListOf(), true).apply {
                collection.insertOne(this)
            }
    }

    fun getGroups(): List<PermissionGroup> {
        return collection.find().toList()
    }
}