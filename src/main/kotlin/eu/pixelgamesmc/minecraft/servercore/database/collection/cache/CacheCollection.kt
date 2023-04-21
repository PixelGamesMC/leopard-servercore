package eu.pixelgamesmc.minecraft.servercore.database.collection.cache

import com.mongodb.client.MongoCollection
import eu.pixelgamesmc.minecraft.servercore.database.collection.PixelCollection
import eu.pixelgamesmc.minecraft.servercore.database.collection.PlayerCollection
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.setValue
import redis.clients.jedis.JedisPool
import java.util.*

class CacheCollection(
    collection: MongoCollection<CacheUser>
): PixelCollection<CacheUser>(
    collection
), PlayerCollection {

    fun getCacheById(uuid: UUID): CacheUser? {
        return collection.findOne(CacheUser::uuid eq uuid)
    }

    fun getCacheByName(name: String): CacheUser? {
        return collection.findOne(CacheUser::name eq name)
    }

    override fun playerLogin(uuid: UUID, name: String, skin: String) {
        if (collection.findOne(CacheUser::uuid eq uuid) == null) {
            collection.insertOne(CacheUser(uuid, name, skin))
        } else {
            collection.updateOne(CacheUser::uuid eq uuid, listOf(setValue(CacheUser::name, name), setValue(CacheUser::skin, skin)))
        }
    }
}