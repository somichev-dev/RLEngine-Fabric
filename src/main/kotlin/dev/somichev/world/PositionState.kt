package dev.somichev.world

import net.minecraft.registry.RegistryKey
import net.minecraft.util.math.BlockPos
import net.minecraft.world.dimension.DimensionType

data class PositionState(val blockPos: BlockPos, val dimension: RegistryKey<DimensionType>) {
    override fun equals(other: Any?): Boolean {
        if (other is PositionState) {
            return other.blockPos == blockPos && other.dimension == dimension
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = blockPos.hashCode()
        result = 31 * result + dimension.hashCode()
        return result
    }
}
