package dev.somichev.climate

import dev.somichev.entity.attribute.RadioLampEngineEntityAttributes
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import util.EffectData

object TemperatureEffects {
    private val effects: Map<ClosedFloatingPointRange<Double>, List<EffectData>> =
        mapOf(
            Double.NEGATIVE_INFINITY..-50.0 to
                listOf(
                    EffectData(StatusEffects.INSTANT_DAMAGE, 2 * 20, 0),
                ),
            Double.NEGATIVE_INFINITY..-48.0 to
                listOf(
                    EffectData(StatusEffects.SLOWNESS, 2 * 20, 1),
                ),
            Double.NEGATIVE_INFINITY..-45.0 to
                listOf(
                    EffectData(StatusEffects.WEAKNESS, 2 * 20, 0),
                ),
            45.0..Double.POSITIVE_INFINITY to
                listOf(
                    EffectData(StatusEffects.SLOWNESS, 2 * 20, 1),
                ),
            48.0..Double.POSITIVE_INFINITY to
                listOf(
                    EffectData(StatusEffects.NAUSEA, 9 * 20, 0),
                ),
            50.0..Double.POSITIVE_INFINITY to
                listOf(
                    EffectData(StatusEffects.NAUSEA, 2 * 20, 0),
                ),
        )

    fun applyTo(player: PlayerEntity) {
        val temperature = player.attributes.getValue(RadioLampEngineEntityAttributes.temperature)

        effects.entries.forEach { entry ->
            if (temperature in entry.key) entry.value.forEach {
                player.addStatusEffect(it.instantiate())
            }
        }
    }
}