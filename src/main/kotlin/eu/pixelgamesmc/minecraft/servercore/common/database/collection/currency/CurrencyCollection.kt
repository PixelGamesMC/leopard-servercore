package eu.pixelgamesmc.minecraft.servercore.common.database.collection.currency

import com.destroystokyo.paper.profile.PlayerProfile
import com.mongodb.client.MongoCollection
import eu.pixelgamesmc.minecraft.servercore.common.database.collection.PixelCollection
import eu.pixelgamesmc.minecraft.servercore.common.database.collection.PlayerCollection
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import redis.clients.jedis.JedisPool
import java.util.*

class CurrencyCollection(
    jedisPool: JedisPool,
    collection: MongoCollection<CurrencyUser>
): PixelCollection<CurrencyUser>(
    jedisPool, collection
), PlayerCollection {

    fun updateUser(currencyUser: CurrencyUser) {
        updateCache("currency_user#${currencyUser.uuid}", currencyUser)
    }

    fun getUser(uuid: UUID): CurrencyUser? {
        return getCache("currency_user#$uuid", CurrencyUser::uuid eq uuid, CurrencyUser::class)
    }

    override fun playerLogin(uuid: UUID, name: String, skin: String) {
        if (collection.findOne(CurrencyUser::uuid eq uuid) == null) {
            collection.insertOne(CurrencyUser(uuid, 0))
        }
    }
}