package dev.somichev.climate.calculation

import net.minecraft.entity.player.PlayerEntity

interface TemperatureCalculator {
    fun calculate(player: PlayerEntity): Double
}