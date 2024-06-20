package dev.somichev.climate.calculation

import net.minecraft.entity.player.PlayerEntity

object WeatherWarmness : TemperatureCalculator {
    override fun calculate(player: PlayerEntity): Double =
        if (player.world.isRaining || player.world.isThundering) -5.0 else 5.0
}