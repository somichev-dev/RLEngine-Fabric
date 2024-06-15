package dev.somichev.item.depers

import dev.somichev.RadioLampEngine
import eu.pb4.polymer.core.api.item.SimplePolymerItem
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.predicate.entity.EntityPredicates
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.Box
import net.minecraft.world.World

class Dulo : SimplePolymerItem(Settings().maxCount(1), model.item()) {
    companion object {
        val id = RadioLampEngine.id("depers_dulo")
        val model = PolymerResourcePackUtils.requestModel(Items.TOTEM_OF_UNDYING, id.withPrefixedPath("item/"))
    }
    override fun getPolymerCustomModelData(itemStack: ItemStack?, player: ServerPlayerEntity?) = model.value()

    override fun inventoryTick(stack: ItemStack?, world: World?, entity: Entity?, slot: Int, selected: Boolean) {
        if(entity !is LivingEntity) return
        if(entity.offHandStack != stack) return
        world?.getEntitiesByType(
            EntityType.FIREBALL,
            Box.of(entity.pos, 5.0, 5.0, 5.0),
            EntityPredicates.VALID_ENTITY
        )?.forEach {
            it.remove(Entity.RemovalReason.KILLED)
        }
    }
}