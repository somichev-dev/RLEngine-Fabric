package dev.somichev.world.codecs

import com.vedi.skyblock.world.state.codecs.NbtCodec
import dev.somichev.world.PositionState
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

object PositionCodec : NbtCodec<PositionState, NbtCompound> {
    override fun encode(value: PositionState, registryLookup: RegistryWrapper.WrapperLookup) = NbtCompound().apply {
        putString("dimension", value.dimension.value.toString())
        putIntArray("block_pos", listOf(value.blockPos.x, value.blockPos.y, value.blockPos.z))
    }

    override fun decode(element: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) = element.let {
        val dimId = Identifier(it.getString("dimension"))
        val dimKey = RegistryKey.of(RegistryKeys.DIMENSION_TYPE, dimId)
        PositionState(it.getIntArray("block_pos").let { list ->
            BlockPos(list[0], list[1], list[2])
        }, dimKey)
    }
}