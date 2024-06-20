package dev.somichev.climate.calculation

import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.Items

object EquipmentWarmness : TemperatureCalculator {
    private val armorWarmness: Map<Item, Double> =
        mapOf(
            // netherite
            Items.NETHERITE_HELMET to 2.0,
            Items.NETHERITE_CHESTPLATE to 5.0,
            Items.NETHERITE_LEGGINGS to 3.0,
            Items.NETHERITE_BOOTS to 2.0,
            // diamond
            Items.DIAMOND_HELMET to 0.0,
            Items.DIAMOND_CHESTPLATE to 1.0,
            Items.DIAMOND_LEGGINGS to 1.0,
            Items.DIAMOND_BOOTS to 0.0,
            // iron
            Items.IRON_HELMET to 0.0,
            Items.IRON_CHESTPLATE to 1.0,
            Items.IRON_LEGGINGS to 0.0,
            Items.IRON_BOOTS to 1.0,
            // gold
            Items.GOLDEN_HELMET to -2.0,
            Items.GOLDEN_CHESTPLATE to -4.0,
            Items.GOLDEN_LEGGINGS to -3.0,
            Items.GOLDEN_BOOTS to 0.0,
            // leather
            Items.LEATHER_HELMET to 8.0,
        )

    private val itemWarmness: Map<Item, Double> =
        mapOf(
            Items.HEART_OF_THE_SEA to -10.0,
            Items.SOUL_LANTERN to -5.0,
            Items.TORCH to 3.0,
            Items.LAVA_BUCKET to 10.0,
            Items.NETHER_STAR to 20.0,
        )

    private fun inLeather(player: PlayerEntity): Boolean =
        player.inventory.armor[EquipmentSlot.CHEST.entitySlotId].item == Items.LEATHER_CHESTPLATE &&
        player.inventory.armor[EquipmentSlot.CHEST.entitySlotId].item == Items.LEATHER_LEGGINGS &&
        player.inventory.armor[EquipmentSlot.CHEST.entitySlotId].item == Items.LEATHER_BOOTS

    private fun equippedArmorWarmness(player: PlayerEntity): Double =
        player.inventory.armor.sumOf { armorWarmness[it.item] ?: 0.0 }

    private fun equippedItemWarmness(player: PlayerEntity): Double {
        val offHandWarmness = player.inventory.offHand.sumOf { itemWarmness[it.item] ?: 0.0 }
        val mainHandWarmness = itemWarmness[player.inventory.mainHandStack.item] ?: 0.0

        return offHandWarmness + mainHandWarmness
    }

    override fun calculate(player: PlayerEntity): Double {
        var temperature = 0.0

        if (inLeather(player)) {
            temperature += 17.0
        }

        temperature += equippedArmorWarmness(player)
        temperature += equippedItemWarmness(player)

        return temperature
    }
}