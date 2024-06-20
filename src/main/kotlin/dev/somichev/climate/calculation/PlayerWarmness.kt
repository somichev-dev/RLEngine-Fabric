package dev.somichev.climate.calculation

import dev.somichev.RadioLampEngine
import net.minecraft.entity.player.PlayerEntity

object PlayerWarmness : TemperatureCalculator {
    override fun calculate(player: PlayerEntity): Double =
        listOf(BiomeWarmness, EquipmentWarmness, LightWarmness, WeatherWarmness).sumOf { it.calculate(player) }
}