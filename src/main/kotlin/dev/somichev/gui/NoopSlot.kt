package dev.somichev.gui

import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.screen.slot.Slot
import net.minecraft.text.Text
import net.minecraft.util.Formatting


class NoopSlot(inventory: Inventory?, index: Int) : Slot(inventory, index, 0, 0) {
    override fun canInsert(stack: ItemStack) = false
    override fun canTakeItems(playerEntity: PlayerEntity) = false
    override fun canTakePartial(player: PlayerEntity) = false
    override fun takeStack(amount: Int): ItemStack = ItemStack.EMPTY
    override fun insertStack(stack: ItemStack, count: Int) = stack
    override fun setStack(stack: ItemStack) {}
    override fun setStackNoCallbacks(stack: ItemStack) {}
    override fun hasStack() = false
    override fun getStack(): ItemStack {
        return Items.GRAY_STAINED_GLASS_PANE.defaultStack.also {
            it[DataComponentTypes.CUSTOM_NAME] = Text.literal("сюда нельзя").formatted(
                Formatting.GRAY, Formatting.ITALIC
            )
        }
    }
}