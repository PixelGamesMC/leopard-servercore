package eu.pixelgamesmc.minecraft.servercore.database.collection.language

import com.mongodb.client.MongoCollection
import eu.pixelgamesmc.minecraft.servercore.database.collection.PixelCollection
import eu.pixelgamesmc.minecraft.servercore.database.collection.PlayerCollection
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.save
import redis.clients.jedis.JedisPool
import java.util.*

class LanguageCollection(
    collection: MongoCollection<LanguageUser>
): PixelCollection<LanguageUser>(
    collection
), PlayerCollection {

    fun updateUser(languageUser: LanguageUser) {
        collection.save(languageUser)
    }

    fun getUser(uuid: UUID): LanguageUser? {
        return collection.findOne(LanguageUser::uuid eq uuid)
    }

    override fun playerLogin(uuid: UUID, name: String, skin: String) {
        if (collection.findOne(LanguageUser::uuid eq uuid) == null) {
            collection.insertOne(LanguageUser(uuid, LanguageUser.Language.GERMAN))
        }
    }
}