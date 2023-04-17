package eu.pixelgamesmc.minecraft.servercore.database

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import eu.pixelgamesmc.minecraft.servercore.database.collection.PixelCollection
import org.bson.UuidRepresentation
import org.litote.kmongo.KMongo
import org.litote.kmongo.serialization.SerializationClassMappingTypeService
import org.litote.kmongo.util.CollectionNameFormatter
import redis.clients.jedis.JedisPool
import kotlin.reflect.KClass

object PixelDatabase {

    private lateinit var jedisPool: JedisPool
    private lateinit var mongoClient: MongoClient
    private lateinit var mongoDatabase: MongoDatabase

    private val collections: MutableList<PixelCollection<*>> = mutableListOf()

    internal fun connect(credentials: Credentials) {
        connectJedis(credentials)
        connectMongo(credentials)
    }

    internal fun disconnect() {
        jedisPool.close()
        mongoClient.close()
    }

    private fun connectJedis(credentials: Credentials) {
        jedisPool = if (credentials.redis.local) {
            JedisPool()
        } else {
            JedisPool(credentials.redis.hostname, credentials.redis.port, credentials.redis.username, credentials.redis.password)
        }
    }

    private fun connectMongo(credentials: Credentials) {
        prepareMongo()

        val settings = MongoClientSettings.builder().uuidRepresentation(UuidRepresentation.STANDARD)
        mongoClient = if (credentials.mongo.local) {
            KMongo.createClient(settings.build())
        } else {
            KMongo.createClient(settings.applyConnectionString(ConnectionString(credentials.mongo.connectionString)).build())
        }

        mongoDatabase = mongoClient.getDatabase(credentials.mongo.database)
    }

    private fun prepareMongo() {
        System.setProperty("org.litote.mongo.mapping.service", SerializationClassMappingTypeService::class.qualifiedName!!)

        CollectionNameFormatter.useSnakeCaseCollectionNameBuilder()
    }

    fun registerCollection(creator: (JedisPool, MongoDatabase) -> PixelCollection<*>) {
        val collection = creator.invoke(jedisPool, mongoDatabase)

        collections.add(collection)
    }

    fun registerCollections(creator: (JedisPool, MongoDatabase) -> List<PixelCollection<*>>) {
        val collections = creator.invoke(jedisPool, mongoDatabase)

        PixelDatabase.collections.addAll(collections)
    }

    fun <T: Any> getCollection(clazz: KClass<T>): T {
        return collections.filterIsInstance(clazz.java).single()
    }

    fun <T: Any> getCollections(clazz: KClass<T>): List<T> {
        return collections.filterIsInstance(clazz.java)
    }
}