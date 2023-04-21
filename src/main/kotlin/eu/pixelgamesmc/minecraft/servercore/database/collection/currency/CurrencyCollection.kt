package eu.pixelgamesmc.minecraft.servercore.database.collection.currency

import com.mongodb.client.MongoCollection
import eu.pixelgamesmc.minecraft.servercore.database.collection.PixelCollection
import eu.pixelgamesmc.minecraft.servercore.database.collection.PlayerCollection
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.save
import redis.clients.jedis.JedisPool
import java.util.*

class CurrencyCollection(
    collection: MongoCollection<CurrencyUser>
): PixelCollection<CurrencyUser>(
     collection
), PlayerCollection {

    fun updateUser(currencyUser: CurrencyUser) {
        collection.save(currencyUser)
    }

    fun getUser(uuid: UUID): CurrencyUser? {
        return collection.findOne(CurrencyUser::uuid eq uuid)
    }

    override fun playerLogin(uuid: UUID, name: String, skin: String) {
        if (collection.findOne(CurrencyUser::uuid eq uuid) == null) {
            collection.insertOne(CurrencyUser(uuid, 0))
        }
    }
}