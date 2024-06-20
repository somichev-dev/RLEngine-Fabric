package dev.somichev.util

import com.mojang.serialization.Codec
import eu.pb4.polymer.core.api.utils.PolymerSyncedObject
import net.minecraft.component.DataComponentType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import net.minecraft.util.Util
import java.util.*
import java.util.function.UnaryOperator

class PolymerItemComponentImpl<T>(
    private val codec: Codec<T>?,
    private val packetCodec: PacketCodec<in RegistryByteBuf?, T>?
) : DataComponentType<T>, PolymerSyncedObject<DataComponentType<T>> {
    companion object {
        fun <T> builder() = Builder<T>()
    }

    override fun getCodec(): Codec<T>? {
        return this.codec
    }

    override fun getPacketCodec(): PacketCodec<in RegistryByteBuf?, T>? {
        return this.packetCodec
    }

    override fun toString(): String {
        return Util.registryValueToString(
            Registries.DATA_COMPONENT_TYPE,
            this
        )
    }

    override fun getPolymerReplacement(player: ServerPlayerEntity?): DataComponentType<T> {
        TODO("Not yet implemented")
    }

    class Builder<T> : DataComponentType.Builder<T>() {
        private var codec: Codec<T>? = null
        private var packetCodec: PacketCodec<in RegistryByteBuf?, T>? = null

        override fun codec(codec: Codec<T>?): Builder<T> {
            this.codec = codec
            return this
        }

        override fun packetCodec(packetCodec: PacketCodec<in RegistryByteBuf?, T>?): Builder<T> {
            this.packetCodec = packetCodec
            return this
        }

        override fun build(): DataComponentType<T> {
            val packetCodec: PacketCodec<in RegistryByteBuf?, T>? = Objects.requireNonNullElseGet(
                this.packetCodec
            ) {
                PacketCodecs.registryCodec(
                    Objects.requireNonNull(
                        this.codec,
                        "Missing Codec for component"
                    )
                )
            }
            val codec = codec!!
            return PolymerItemComponentImpl(codec, packetCodec)
        }
    }
}

fun <T> registerComponentType(
    id: Identifier, builderOperator: UnaryOperator<DataComponentType.Builder<T>>
): DataComponentType<T> = Registry.register(
    Registries.DATA_COMPONENT_TYPE,
    id,
    builderOperator.apply(PolymerItemComponentImpl.builder()).build()
)