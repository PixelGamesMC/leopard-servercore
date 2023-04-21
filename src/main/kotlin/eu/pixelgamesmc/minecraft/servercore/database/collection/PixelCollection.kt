package eu.pixelgamesmc.minecraft.servercore.database.collection

import com.mongodb.client.MongoCollection
import org.bson.codecs.DecoderContext
import org.bson.conversions.Bson
import org.litote.kmongo.*
import redis.clients.jedis.JedisPool
import redis.clients.jedis.params.GetExParams
import java.time.Duration
import kotlin.reflect.KClass

open class PixelCollection<T: Any>(
    protected val collection: MongoCollection<T>
)