package dev.somichev.item.depers

import dev.somichev.RadioLampEngine
import dev.somichev.item.CustomTotemItem
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class Durilka : CustomTotemItem() {
    val statusEffectsBlacklist = listOf(
        StatusEffects.SLOWNESS,
        StatusEffects.NAUSEA,
        StatusEffects.BLINDNESS,
        StatusEffects.DARKNESS,
        StatusEffects.HUNGER,
        StatusEffects.POISON,
        StatusEffects.MINING_FATIGUE,
        StatusEffects.UNLUCK,
        StatusEffects.WEAKNESS
    )

    companion object {
        val id = RadioLampEngine.id("depers_durilka")
        val model = PolymerResourcePackUtils.requestModel(Items.TOTEM_OF_UNDYING, id.withPrefixedPath("item/"))
    }
    override fun getPolymerCustomModelData(itemStack: ItemStack?, player: ServerPlayerEntity?) = model.value()

    override fun use(world: World?, user: PlayerEntity, hand: Hand?): TypedActionResult<ItemStack>? {
        val itemStack: ItemStack = user.getStackInHand(hand)

        (user as ServerPlayerEntity).networkHandler.sendPacket(GameStateChangeS2CPacket(GameStateChangeS2CPacket.RAIN_STARTED, 0.0f))
        user.networkHandler.sendPacket(GameStateChangeS2CPacket(GameStateChangeS2CPacket.RAIN_GRADIENT_CHANGED, 1.0f))
        user.networkHandler.sendPacket(GameStateChangeS2CPacket(GameStateChangeS2CPacket.THUNDER_GRADIENT_CHANGED, 1.0f))

        return TypedActionResult.success(itemStack)
    }

    override fun inventoryTick(stack: ItemStack?, world: World?, entity: Entity?, slot: Int, selected: Boolean) {
        if(entity !is LivingEntity) return
        if(entity.offHandStack != stack) return
        statusEffectsBlacklist.forEach {
            entity.removeStatusEffect(it)
        }
        // TODO
    }

}