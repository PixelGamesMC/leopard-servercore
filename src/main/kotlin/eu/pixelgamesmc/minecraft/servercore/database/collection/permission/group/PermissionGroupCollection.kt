package eu.pixelgamesmc.minecraft.servercore.database.collection.permission.group

import com.mongodb.client.MongoCollection
import eu.pixelgamesmc.minecraft.servercore.database.collection.PixelCollection
import net.kyori.adventure.text.format.NamedTextColor
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.save
import redis.clients.jedis.JedisPool

class PermissionGroupCollection(
    collection: MongoCollection<PermissionGroup>
): PixelCollection<PermissionGroup>(
    collection
) {

    fun createGroup(name: String) {
        collection.insertOne(PermissionGroup(name, 0, "", NamedTextColor.WHITE, mutableListOf(), false))
    }

    fun deleteGroup(permissionGroup: PermissionGroup) {
        collection.deleteOne(PermissionGroup::name eq permissionGroup.name)
    }

    fun update(permissionGroup: PermissionGroup) {
        collection.save(permissionGroup)
    }

    fun getGroup(name: String): PermissionGroup? {
        return collection.findOne(PermissionGroup::name eq name)
    }

    fun getDefaultGroups(): List<PermissionGroup> {
        return collection.find(PermissionGroup::default eq true).toList()
    }

    fun getGroups(): List<PermissionGroup> {
        return collection.find().toList()
    }
}