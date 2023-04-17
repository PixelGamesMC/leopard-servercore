package eu.pixelgamesmc.minecraft.servercore.bukkit.utility.codec

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.text.format.NamedTextColor

object NamedTextColorSerializer : KSerializer<NamedTextColor> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("COLOR", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): NamedTextColor {
        return NamedTextColor.NAMES.value(decoder.decodeString()) ?: NamedTextColor.WHITE
    }

    override fun serialize(encoder: Encoder, value: NamedTextColor) {
        encoder.encodeString(value.toString())
    }
}