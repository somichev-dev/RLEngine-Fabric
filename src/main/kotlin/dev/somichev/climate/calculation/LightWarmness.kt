package dev.somichev.climate.calculation

import net.minecraft.entity.player.PlayerEntity

object LightWarmness : TemperatureCalculator{
    override fun calculate(player: PlayerEntity): Double =
        when (player.world.getLightLevel(player.blockPos)) {
            in 0..10 -> -4.0
            else -> 0.0
        }
}