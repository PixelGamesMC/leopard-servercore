package eu.pixelgamesmc.minecraft.servercore.database.collection

import java.util.UUID

interface PlayerCollection {

    fun playerLogin(uuid: UUID, name: String, skin: String)
}