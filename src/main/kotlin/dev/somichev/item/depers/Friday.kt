package dev.somichev.item.depers

import dev.somichev.RadioLampEngine
import dev.somichev.item.CustomTotemItem
import eu.pb4.polymer.core.api.item.SimplePolymerItem
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.item.PotionItem
import net.minecraft.registry.Registries
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.world.World

class Friday : CustomTotemItem() {
    companion object {
        val id = RadioLampEngine.id("depers_friday")
        val model = PolymerResourcePackUtils.requestModel(Items.TOTEM_OF_UNDYING, id.withPrefixedPath("item/totems/"))
    }
    override fun getPolymerCustomModelData(itemStack: ItemStack?, player: ServerPlayerEntity?) = model.value()

    override fun inventoryTick(stack: ItemStack?, world: World?, entity: Entity?, slot: Int, selected: Boolean) {
        //if(entity !is PlayerEntity) return
        //if(entity.offHandStack != stack) return
        //val mainItem = entity.inventory.mainHandStack.item
        //if(mainItem is PotionItem){
        //    entity.inventory.removeStack(entity.inventory.selectedSlot)
        //}
        // TODO
    }
}