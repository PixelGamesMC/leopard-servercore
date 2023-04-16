package eu.pixelgamesmc.minecraft.servercore.database.collection

import com.destroystokyo.paper.profile.PlayerProfile
import java.util.UUID

interface PlayerCollection {

    fun playerLogin(uuid: UUID, name: String, playerProfile: PlayerProfile)
}