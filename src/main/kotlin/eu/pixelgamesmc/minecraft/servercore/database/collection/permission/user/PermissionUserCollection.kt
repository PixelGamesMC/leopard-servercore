package eu.pixelgamesmc.minecraft.servercore.database.collection.permission.user

import com.mongodb.client.MongoCollection
import eu.pixelgamesmc.minecraft.servercore.database.collection.PixelCollection
import eu.pixelgamesmc.minecraft.servercore.database.collection.PlayerCollection
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.save
import redis.clients.jedis.JedisPool
import java.util.*

class PermissionUserCollection(
    collection: MongoCollection<PermissionUser>
): PixelCollection<PermissionUser>(
    collection
), PlayerCollection {

    fun update(permissionUser: PermissionUser) {
        collection.save(permissionUser)
    }

    fun getUser(uuid: UUID): PermissionUser? {
        return collection.findOne(PermissionUser::uuid eq uuid)
    }

    override fun playerLogin(uuid: UUID, name: String, skin: String) {
        if (collection.findOne(PermissionUser::uuid eq uuid) == null) {
            collection.insertOne(PermissionUser(uuid, mutableListOf(), mutableListOf()))
        }
    }
}