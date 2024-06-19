package dev.somichev.item.transports

import dev.somichev.RadioLampEngine
import eu.pb4.polymer.core.api.item.SimplePolymerItem
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils
import net.minecraft.block.CampfireBlock
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World

class Glider: SimplePolymerItem(Settings().maxCount(1).maxDamage(64).equipmentSlot { EquipmentSlot.CHEST }, model.item()) {
    companion object{
        val id = RadioLampEngine.id("glider")
        val model = PolymerResourcePackUtils.requestModel(Items.LEATHER_CHESTPLATE, id.withPrefixedPath("item/"))
        val armorModel = PolymerResourcePackUtils.requestArmor(id)
    }

    override fun inventoryTick(stack: ItemStack?, world: World?, entity: Entity?, slot: Int, selected: Boolean) {
        if(entity !is PlayerEntity || !entity.inventory.armor.contains(stack)) return
        entity.addStatusEffect(
            StatusEffectInstance(StatusEffects.SLOW_FALLING, 40, 0)
        )
        //jump if standing on campfire
        if(world?.getBlockState(entity.blockPos.subtract(Vec3i(0,1,0)))?.block is CampfireBlock){
            entity.addVelocity(Vec3d(0.0,5.0,0.0))
            entity.velocityModified = true
            stack?.damage(1, entity, EquipmentSlot.CHEST)
        }
    }

    override fun getPolymerArmorColor(itemStack: ItemStack?, player: ServerPlayerEntity?): Int = armorModel.color()
    override fun getPolymerCustomModelData(itemStack: ItemStack?, player: ServerPlayerEntity?): Int = model.value()
}
