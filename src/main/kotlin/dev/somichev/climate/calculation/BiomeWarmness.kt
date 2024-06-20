package dev.somichev.climate.calculation

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.registry.RegistryKey
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.BiomeKeys

object BiomeWarmness : TemperatureCalculator {
    private val temperatures: Map<RegistryKey<Biome>, Double> =
        mapOf(
            // safe biomes
            BiomeKeys.MUSHROOM_FIELDS to 10.0,
            BiomeKeys.CHERRY_GROVE to 30.0,
            // jule biomes
            BiomeKeys.PLAINS to 15.0,
            BiomeKeys.RIVER to 15.0,
            // cursed biomes
            BiomeKeys.BADLANDS to 35.0,
            BiomeKeys.ERODED_BADLANDS to 35.0,
            BiomeKeys.COLD_OCEAN to -65.0,
            BiomeKeys.DARK_FOREST to 20.0,
            BiomeKeys.DEEP_OCEAN to -20.0,
            BiomeKeys.DESERT to 32.0,
            BiomeKeys.FROZEN_RIVER to -65.0,
            BiomeKeys.JUNGLE to 37.0,
            BiomeKeys.OCEAN to 10.0,
            BiomeKeys.SNOWY_BEACH to  -63.0,
            BiomeKeys.SNOWY_PLAINS to -63.0,
            BiomeKeys.SNOWY_SLOPES to -63.0,
            BiomeKeys.SNOWY_TAIGA to  -63.0,
            BiomeKeys.SWAMP to 15.0,
            BiomeKeys.TAIGA to -58.0,
        )

    override fun calculate(player: PlayerEntity): Double {
        if (player.world.registryKey == World.NETHER) return 44.0
        if (player.world.registryKey == World.END) return -99.0

        val biome = player.world.getBiome(player.blockPos).key.get()

        return temperatures[biome] ?: 15.0
    }
}