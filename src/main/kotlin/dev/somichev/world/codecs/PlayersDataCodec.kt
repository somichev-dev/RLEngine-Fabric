package dev.somichev.world.codecs

import com.vedi.skyblock.world.state.codecs.NbtCodec
import dev.somichev.world.PlayerData
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import java.util.*

object PlayersDataCodec : NbtCodec<HashMap<UUID, PlayerData>, NbtCompound> {
    override fun encode(value: HashMap<UUID, PlayerData>, registryLookup: RegistryWrapper.WrapperLookup) =
        NbtCompound().apply {
            value.forEach { (k, v) ->
                put(k.toString(), NbtCompound().also {
                    if (v.gravePos != null) it.put("grave_pos", PositionCodec.encode(v.gravePos!!, registryLookup))
                })
            }
        }

    override fun decode(
        element: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup
    ): HashMap<UUID, PlayerData> {
        val map = HashMap<UUID, PlayerData>()
        for (uuid in element.keys) {
            val playerNbt = element.getCompound(uuid)

            map[UUID.fromString(uuid)] = PlayerData(
                if (playerNbt.contains("grave_pos")) PositionCodec.decode(
                    playerNbt.getCompound("grave_pos"), registryLookup
                ) else null
            )
        }
        return map
    }
}