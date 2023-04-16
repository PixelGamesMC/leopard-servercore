package eu.pixelgamesmc.minecraft.servercore.database.collection.cache

import com.destroystokyo.paper.profile.PlayerProfile
import com.mongodb.client.MongoCollection
import eu.pixelgamesmc.minecraft.servercore.database.collection.PixelCollection
import eu.pixelgamesmc.minecraft.servercore.database.collection.PlayerCollection
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.setValue
import redis.clients.jedis.JedisPool
import java.util.*

class CacheCollection(
    jedisPool: JedisPool,
    collection: MongoCollection<CacheUser>
): PixelCollection<CacheUser>(
    jedisPool, collection
), PlayerCollection {

    fun getCacheById(uuid: UUID): CacheUser? {
        return collection.findOne(CacheUser::uuid eq uuid)
    }

    fun getCacheByName(name: String): CacheUser? {
        return collection.findOne(CacheUser::name eq name)
    }

    override fun playerLogin(uuid: UUID, name: String, playerProfile: PlayerProfile) {
        val skin = playerProfile.textures.skin.toString()
        if (collection.findOne(CacheUser::uuid eq uuid) == null) {
            collection.insertOne(CacheUser(uuid, name, skin))
        } else {
            collection.updateOne(CacheUser::uuid eq uuid, listOf(setValue(CacheUser::name, name), setValue(CacheUser::skin, skin)))
        }
    }
}