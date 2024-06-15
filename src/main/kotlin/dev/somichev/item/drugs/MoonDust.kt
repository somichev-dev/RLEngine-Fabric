package dev.somichev.item.drugs

import dev.somichev.RadioLampEngine
import eu.pb4.polymer.core.api.item.SimplePolymerItem
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class MoonDust: SimplePolymerItem(Settings().maxCount(64).fireproof(), model.item()) {
    companion object{
        val id = RadioLampEngine.id("moon_dust")
        val model = PolymerResourcePackUtils.requestModel(Items.WHITE_DYE, id.withPrefixedPath("item/"))
    }
    /*
        private val effects = listOf(
        PotionEffect(PotionEffectType.NIGHT_VISION, 20*60*10, 0),
        PotionEffect(PotionEffectType.GLOWING, 20*60*10, 0),
        PotionEffect(PotionEffectType.SATURATION, 20*60*10, 0),
        PotionEffect(PotionEffectType.JUMP, 20*60*10, 0),
        PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*60*10, 1),
        PotionEffect(PotionEffectType.REGENERATION, 20*60*10, 0),
        PotionEffect(PotionEffectType.SPEED, 20*60*10, 1),
    )
     */
    data class effectData(val effect: RegistryEntry<StatusEffect>, val duration: Int, val amplifier: Int)
    val effects = listOf(
        effectData(StatusEffects.NIGHT_VISION, 20*60*10, 0),
        effectData(StatusEffects.GLOWING, 20*60*10, 0),
        effectData(StatusEffects.SATURATION, 20*60*10, 0),
        effectData(StatusEffects.JUMP_BOOST, 20*60*10, 0),
        effectData(StatusEffects.STRENGTH, 20*60*10, 1),
        effectData(StatusEffects.REGENERATION, 20*60*10, 0),
        effectData(StatusEffects.SPEED, 20*60*10, 1),
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
            user.addStatusEffect(StatusEffectInstance(it.effect, it.duration, it.amplifier))
        }

        return TypedActionResult.consume(itemStack)
    }
}