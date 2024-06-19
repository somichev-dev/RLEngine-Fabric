package dev.somichev.gui

import com.mojang.datafixers.util.Pair
import dev.somichev.RadioLampEngine
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtIo
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import net.minecraft.util.Util
import net.minecraft.util.WorldSavePath
import java.io.File
import java.io.FileOutputStream


class PlayerInventoryMenu(
    type: ScreenHandlerType<*>?,
    syncId: Int,
    private val targetPlayer: ServerPlayerEntity,
    playerInventory: PlayerInventory
) : ScreenHandler(type, syncId) {
    companion object {
        val EMPTY_ARMOR_SLOT_TEXTURES: Array<Identifier> = arrayOf(
            PlayerScreenHandler.EMPTY_BOOTS_SLOT_TEXTURE,
            PlayerScreenHandler.EMPTY_LEGGINGS_SLOT_TEXTURE,
            PlayerScreenHandler.EMPTY_CHESTPLATE_SLOT_TEXTURE,
            PlayerScreenHandler.EMPTY_HELMET_SLOT_TEXTURE
        )
    }

    private val targetInv = targetPlayer.inventory

    init {
        for (j in 0..2) addSlot(NoopSlot(targetInv, 42))
        addSlot(PlayerEquipmentSlot(targetInv, 39, 8, 8, targetPlayer, EquipmentSlot.HEAD))
        addSlot(NoopSlot(targetInv, 42))
        addSlot(PlayerEquipmentSlot(targetInv, 37, 8, 8, targetPlayer, EquipmentSlot.LEGS))
        for (j in 6..9) addSlot(NoopSlot(targetInv, 42))
        addSlot(PlayerOffhandSlot(targetInv, 40, 77, 62, targetPlayer))
        addSlot(NoopSlot(targetInv, 42))
        addSlot(PlayerEquipmentSlot(targetInv, 38, 8, 8, targetPlayer, EquipmentSlot.CHEST))
        addSlot(NoopSlot(targetInv, 42))
        addSlot(PlayerEquipmentSlot(targetInv, 36, 8, 8, targetPlayer, EquipmentSlot.FEET))

        for (j in 15..17) addSlot(NoopSlot(targetInv, 42))


        val i = -3 * 18
        for (j in 0..2) {
            for (k in 0..8) {
                this.addSlot(Slot(targetInv, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i))
            }
        }
        for (j in 0..8) {
            this.addSlot(Slot(targetInv, j, 8 + j * 18, 161 + i))
        }



        for (j in 0..2) {
            for (k in 0..8) {
                this.addSlot(Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i))
            }
        }
        for (j in 0..8) {
            this.addSlot(Slot(playerInventory, j, 8 + j * 18, 161 + i))
        }
    }

    override fun quickMove(player: PlayerEntity, slotIndex: Int): ItemStack {
        val slot = getSlot(slotIndex)
        if (!slot.hasStack()) return ItemStack.EMPTY
        val stack = slot.stack
        if (slotIndex < 54) {
            if (!insertItem(stack, 54, slots.size, true)) return ItemStack.EMPTY
            if (stack.isEmpty) slot.stack = ItemStack.EMPTY else slot.markDirty()
            return stack
        }
        val equipmentSlot = MobEntity.getPreferredEquipmentSlot(stack)
        println("$stack $equipmentSlot")
        when (equipmentSlot) {
            EquipmentSlot.OFFHAND -> {
                if (insertItem(stack, 4, 4, true)) {
                    slot.markDirty()
                    return stack
                }
            }

            EquipmentSlot.FEET -> {
                if (insertItem(stack, 3, 3, true)) {
                    slot.markDirty()
                    return stack
                }
            }

            EquipmentSlot.LEGS -> {
                if (insertItem(stack, 2, 2, true)) {
                    slot.markDirty()
                    return stack
                }
            }

            EquipmentSlot.CHEST -> {
                if (insertItem(stack, 1, 1, true)) {
                    slot.markDirty()
                    return stack
                }
            }

            EquipmentSlot.HEAD -> {
                if (insertItem(stack, 0, 0, true)) {
                    slot.markDirty()
                    return stack
                }
            }

            else -> {}
        }
        if (!insertItem(stack, 18, 54, false)) {
            return ItemStack.EMPTY
        }
        if (stack.isEmpty) slot.stack = ItemStack.EMPTY else slot.markDirty()
        return stack
    }

    override fun onClosed(player: PlayerEntity) {
        super.onClosed(player)
        savePlayerData(targetPlayer)
    }

    private fun savePlayerData(player: ServerPlayerEntity) {
        val playerDataDir = player.server.getSavePath(WorldSavePath.PLAYERDATA).toFile()
        try {
            val compoundTag = player.writeNbt(NbtCompound())
            val file = File.createTempFile(player.uuidAsString + "-", ".dat", playerDataDir)
            val fos = FileOutputStream(file)
            NbtIo.writeCompressed(compoundTag, fos)
            val file2 = File(playerDataDir, player.uuidAsString + ".dat")
            val file3 = File(playerDataDir, player.uuidAsString + ".dat_old")
            Util.backupAndReplace(file2.toPath(), file.toPath(), file3.toPath())
        } catch (e: Error) {
            RadioLampEngine.logger.warn("Failed to save player data for {}", player.name.string)
        }
    }

    override fun canUse(player: PlayerEntity) = player.hasPermissionLevel(4) || targetPlayer.isSpectator

    private class PlayerOffhandSlot(inventory: Inventory, index: Int, x: Int, y: Int, val owner: ServerPlayerEntity) :
        Slot(inventory, index, x, y) {
        override fun setStack(stack: ItemStack?, previousStack: ItemStack?) {
            owner.onEquipStack(EquipmentSlot.OFFHAND, stack, previousStack)
            super.setStack(stack, previousStack)
        }

        override fun getBackgroundSprite(): Pair<Identifier, Identifier> {
            return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, PlayerScreenHandler.EMPTY_OFFHAND_ARMOR_SLOT)
        }
    }

    private class PlayerEquipmentSlot(
        inventory: Inventory,
        index: Int,
        x: Int,
        y: Int,
        val owner: ServerPlayerEntity,
        val equipmentSlot: EquipmentSlot
    ) : Slot(inventory, index, x, y) {
        override fun setStack(stack: ItemStack, previousStack: ItemStack) {
            owner.onEquipStack(equipmentSlot, stack, previousStack)
            super.setStack(stack, previousStack)
        }

        override fun getMaxItemCount() = 1

        override fun canInsert(stack: ItemStack) = equipmentSlot == MobEntity.getPreferredEquipmentSlot(stack)

        override fun canTakeItems(playerEntity: PlayerEntity): Boolean {
            val itemStack = this.stack
            if (!itemStack.isEmpty && !playerEntity.isCreative && EnchantmentHelper.hasBindingCurse(itemStack)) {
                return false
            }
            return super.canTakeItems(playerEntity)
        }

        override fun getBackgroundSprite(): Pair<Identifier, Identifier> = Pair.of(
            PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, EMPTY_ARMOR_SLOT_TEXTURES[equipmentSlot.entitySlotId]
        )
    }
}