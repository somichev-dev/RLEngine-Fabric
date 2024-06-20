package dev.somichev.item.depers

import dev.somichev.RadioLampEngine
import eu.pb4.polymer.core.api.item.SimplePolymerItem
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.world.World

class Fan : SimplePolymerItem(Settings().maxCount(1), model.item()) {
    companion object {
        val id = RadioLampEngine.id("depers_fan")
        val model = PolymerResourcePackUtils.requestModel(Items.TOTEM_OF_UNDYING, id.withPrefixedPath("item/totems/"))
    }
    override fun getPolymerCustomModelData(itemStack: ItemStack?, player: ServerPlayerEntity?) = model.value()

    override fun inventoryTick(stack: ItemStack?, world: World?, entity: Entity?, slot: Int, selected: Boolean) {
        if(entity !is LivingEntity) return
        if(entity.offHandStack != stack) return
        entity.fallDistance = 0f
    }
}