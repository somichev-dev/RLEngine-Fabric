package dev.somichev.block.custom_blocks

import eu.pb4.factorytools.api.virtualentity.BlockModel
import eu.pb4.polymer.core.api.block.PolymerBlock
import eu.pb4.polymer.resourcepack.api.PolymerModelData
import eu.pb4.polymer.virtualentity.api.BlockWithElementHolder
import eu.pb4.polymer.virtualentity.api.ElementHolder
import net.minecraft.block.BlockState
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

interface CustomBlock : PolymerBlock, BlockWithElementHolder {
    fun getItemModel(state: BlockState): PolymerModelData

    override fun createMovingElementHolder(
        world: ServerWorld, blockPos: BlockPos, blockState: BlockState, oldStaticElementHolder: ElementHolder?
    ): ElementHolder? = if (oldStaticElementHolder is BlockModel) oldStaticElementHolder else null

    override fun createStaticElementHolder(
        world: ServerWorld, blockPos: BlockPos, blockState: BlockState, oldMovingElementHolder: ElementHolder?
    ): ElementHolder? = if (oldMovingElementHolder is BlockModel) oldMovingElementHolder else null
}