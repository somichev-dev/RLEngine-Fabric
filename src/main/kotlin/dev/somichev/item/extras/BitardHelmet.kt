package dev.somichev.item.extras

import dev.somichev.RadioLampEngine
import eu.pb4.polymer.core.api.item.SimplePolymerItem
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity

class BitardHelmet: SimplePolymerItem(
    Settings().maxCount(1).equipmentSlot { EquipmentSlot.HEAD },
    Items.CARVED_PUMPKIN
) {
    companion object{
        val id = RadioLampEngine.id("bitard_helmet")
        val model = PolymerResourcePackUtils.requestModel(Items.CARVED_PUMPKIN, id.withPrefixedPath("hat/"))
    }

    override fun getPolymerCustomModelData(itemStack: ItemStack?, player: ServerPlayerEntity?): Int = model.value()
}