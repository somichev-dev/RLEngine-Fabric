package dev.somichev.item.depers

import dev.somichev.RadioLampEngine
import dev.somichev.item.CustomTotemItem
import eu.pb4.polymer.core.api.item.SimplePolymerItem
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.particle.ParticleTypes
import net.minecraft.predicate.entity.EntityPredicates
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Box
import net.minecraft.world.World
import kotlin.random.Random

class Plitka : CustomTotemItem() {
    companion object {
        val id = RadioLampEngine.id("depers_plitka")
        val model = PolymerResourcePackUtils.requestModel(Items.TOTEM_OF_UNDYING, id.withPrefixedPath("item/"))
    }
    override fun getPolymerCustomModelData(itemStack: ItemStack?, player: ServerPlayerEntity?) = model.value()
    override fun inventoryTick(stack: ItemStack?, world: World?, entity: Entity?, slot: Int, selected: Boolean) {
        if(entity !is LivingEntity) return
        if(entity.offHandStack != stack) return
        if(!entity.isSneaking) return
        if(Random.nextDouble() > 0.7){
                (world as ServerWorld).spawnParticles(
                    ParticleTypes.WHITE_SMOKE,
                    entity.x,
                    entity.y+0.8,
                    entity.z,
                    Random.nextInt(20,30),
                    Random.nextDouble(0.0, 0.1),
                    Random.nextDouble(0.1, 0.2),
                    Random.nextDouble(0.0, 0.1),
                    0.01
                )

        }
    }
}