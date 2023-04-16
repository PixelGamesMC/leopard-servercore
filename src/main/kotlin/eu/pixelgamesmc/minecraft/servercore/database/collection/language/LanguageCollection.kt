package eu.pixelgamesmc.minecraft.servercore.database.collection.language

import com.destroystokyo.paper.profile.PlayerProfile
import com.mongodb.client.MongoCollection
import eu.pixelgamesmc.minecraft.servercore.database.collection.PixelCollection
import eu.pixelgamesmc.minecraft.servercore.database.collection.PlayerCollection
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import redis.clients.jedis.JedisPool
import java.util.*

class LanguageCollection(
    jedisPool: JedisPool,
    collection: MongoCollection<LanguageUser>
): PixelCollection<LanguageUser>(
    jedisPool, collection
), PlayerCollection {

    fun updateUser(languageUser: LanguageUser) {
        updateCache("language_user#${languageUser.uuid}", languageUser)
    }

    fun getUser(uuid: UUID): LanguageUser? {
        return getCache("language_user#$uuid", LanguageUser::uuid eq uuid, LanguageUser::class)
    }

    override fun playerLogin(uuid: UUID, name: String, playerProfile: PlayerProfile) {
        if (collection.findOne(LanguageUser::uuid eq uuid) == null) {
            collection.insertOne(LanguageUser(uuid, LanguageUser.Language.GERMAN))
        }
    }
}