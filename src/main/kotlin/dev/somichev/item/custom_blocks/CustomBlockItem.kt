package dev.somichev.item.custom_blocks

import dev.somichev.block.custom_blocks.CustomBlock
import eu.pb4.polymer.core.api.item.PolymerItem
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
class CustomBlockItem(block: Block, settings: Settings) : BlockItem(block, settings), PolymerItem {
    private val model = (block as CustomBlock).getItemModel(block.defaultState)
    override fun getPolymerCustomModelData(itemStack: ItemStack?, player: ServerPlayerEntity?) = model.value()
    override fun getPolymerItem(itemStack: ItemStack, player: ServerPlayerEntity?): Item = model.item()
}