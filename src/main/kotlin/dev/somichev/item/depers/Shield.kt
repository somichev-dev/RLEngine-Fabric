package dev.somichev.item.depers

import dev.somichev.RadioLampEngine
import eu.pb4.polymer.core.api.item.SimplePolymerItem
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.AxeItem
import net.minecraft.item.HoeItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.item.PickaxeItem
import net.minecraft.item.ShovelItem
import net.minecraft.item.SwordItem
import net.minecraft.item.ToolItem
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.world.World
import kotlin.random.Random

class Shield : SimplePolymerItem(Settings().maxCount(1), model.item()) {
    companion object {
        val id = RadioLampEngine.id("depers_shield")
        val model = PolymerResourcePackUtils.requestModel(Items.TOTEM_OF_UNDYING, id.withPrefixedPath("item/totems/"))
    }
    override fun getPolymerCustomModelData(itemStack: ItemStack?, player: ServerPlayerEntity?) = model.value()

    override fun inventoryTick(stack: ItemStack?, world: World?, entity: Entity?, slot: Int, selected: Boolean) {
        if(entity !is LivingEntity) return
        if(entity.offHandStack != stack) return
        val tool = entity.mainHandStack.item
        when(tool){
            is ShovelItem -> {
                entity.addStatusEffect(
                    StatusEffectInstance(
                        StatusEffects.HASTE,
                        40, 1
                    )
                )
            }
            is AxeItem -> {
                entity.addStatusEffect(
                    StatusEffectInstance(
                        StatusEffects.HASTE,
                        40, 1
                    )
                )
                entity.addStatusEffect(
                    StatusEffectInstance(
                        StatusEffects.STRENGTH,
                        40, 0
                    )
                )
            }
            is PickaxeItem -> {
                entity.addStatusEffect(
                    StatusEffectInstance(
                        StatusEffects.HASTE,
                        40, 1
                    )
                )
            }
            is HoeItem -> {
                entity.addStatusEffect(
                    StatusEffectInstance(
                        StatusEffects.STRENGTH,
                        40, 7
                    )
                )
            }
            is SwordItem -> {
                entity.addStatusEffect(
                    StatusEffectInstance(
                        StatusEffects.STRENGTH,
                        40, 1
                    )
                )
            }
        }
    }
}