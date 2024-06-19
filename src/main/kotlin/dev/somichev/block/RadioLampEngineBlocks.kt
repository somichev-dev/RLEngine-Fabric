package dev.somichev.block

import net.minecraft.block.Block
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object RadioLampEngineBlocks {
    val kalyk = register(KalykBlock.id, KalykBlock())

    fun <T: Block> register(id: Identifier, block: T) = Registry.register(Registries.BLOCK, id, block)

}