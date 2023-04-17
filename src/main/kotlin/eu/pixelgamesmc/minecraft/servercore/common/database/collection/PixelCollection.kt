package eu.pixelgamesmc.minecraft.servercore.common.database.collection

import com.mongodb.client.MongoCollection
import org.bson.codecs.DecoderContext
import org.bson.conversions.Bson
import org.litote.kmongo.*
import redis.clients.jedis.JedisPool
import redis.clients.jedis.params.GetExParams
import java.time.Duration
import kotlin.reflect.KClass

open class PixelCollection<T: Any>(
    protected val jedisPool: JedisPool,
    protected val collection: MongoCollection<T>,
    private val expireDuration: Duration = Duration.ofMinutes(30)
) {

    protected fun deleteCache(key: String, bson: Bson) {
        jedisPool.resource.use { jedis ->
            jedis.del(key)
        }
        collection.deleteOne(bson)
    }

    protected fun updateCache(key: String, value: T) {
        jedisPool.resource.use { jedis ->
            jedis.setex(key, expireDuration.toSeconds(), value.json)
        }
        collection.save(value)
    }

    protected fun getCache(key: String, bson: Bson, type: KClass<T>): T? {
        jedisPool.resource.use { jedis ->
            val jsonValue = jedis.getEx(key, GetExParams.getExParams().ex(expireDuration.toSeconds()))
            if (jsonValue != null) {
                val codec = collection.codecRegistry.get(type.java)
                return codec.decode(jsonValue.bson.asBsonReader(), DecoderContext.builder().build())
            }

            val value = collection.findOne(bson)
            if (value != null) {
                jedis.setex(key, expireDuration.toSeconds(), value.json)
            }
            return value
        }
    }
}