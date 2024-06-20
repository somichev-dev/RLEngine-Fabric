package dev.somichev.item.drugs

import dev.somichev.RadioLampEngine
import dev.somichev.entity.attribute.RadioLampEngineEntityAttributes
import eu.pb4.polymer.core.api.item.SimplePolymerItem
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import dev.somichev.util.EffectData

class MoonDust: SimplePolymerItem(Settings().maxCount(64).fireproof(), model.item()) {
    companion object{
        val id = RadioLampEngine.id("moon_dust")
        val model = PolymerResourcePackUtils.requestModel(Items.WHITE_DYE, id.withPrefixedPath("item/"))
    }

    override fun getPolymerCustomModelData(itemStack: ItemStack?, player: ServerPlayerEntity?): Int = model.value()
    val effects = listOf(
        EffectData(StatusEffects.NIGHT_VISION, 20*60*10, 0),
        EffectData(StatusEffects.GLOWING, 20*60*10, 0),
        EffectData(StatusEffects.SATURATION, 20*60*10, 0),
        EffectData(StatusEffects.JUMP_BOOST, 20*60*10, 0),
        EffectData(StatusEffects.STRENGTH, 20*60*10, 1),
        EffectData(StatusEffects.REGENERATION, 20*60*10, 0),
        EffectData(StatusEffects.SPEED, 20*60*10, 1),
    )
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val itemStack = user.getStackInHand(hand)

        world.playSound(
            null,
            user.blockPos,
            SoundEvents.ENTITY_FOX_SNIFF,
            SoundCategory.PLAYERS,
            1.0f,
            0.5f
        )

        effects.forEach {
            user.addStatusEffect(it.instantiate())
        }
        itemStack.count -= 1

        return TypedActionResult.success(itemStack)
    }
}