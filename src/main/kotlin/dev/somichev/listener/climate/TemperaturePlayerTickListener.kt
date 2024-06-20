package dev.somichev.listener.climate

import dev.somichev.RadioLampEngine
import dev.somichev.climate.TemperatureEffects
import dev.somichev.climate.calculation.BiomeWarmness
import dev.somichev.climate.calculation.PlayerWarmness
import dev.somichev.entity.attribute.RadioLampEngineEntityAttributes
import dev.somichev.events.PlayerEvents.PlayerTickStart
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.math.MathHelper.lerp

object TemperaturePlayerTickListener : PlayerTickStart {
    val inertia = 0.002

    override fun onPlayerTickStart(player: ServerPlayerEntity, server: MinecraftServer) {
        server.bossBarManager.get(RadioLampEngine.id(player.uuidAsString.lowercase()))?.let {
            val temperature = updatePlayerTemperature(player)

            TemperatureEffects.applyTo(player)

            it.value = temperature.toInt() + 100
            it.name = Text.literal("${temperature.toInt()}°C")
        }
    }

    private fun updatePlayerTemperature(player: PlayerEntity): Double {
        val currentTemperature = player.attributes.getValue(RadioLampEngineEntityAttributes.temperature)

        if (player.attributes.hasAttribute(RadioLampEngineEntityAttributes.temperature)) {
            val nextTemperature = lerp(inertia, currentTemperature, PlayerWarmness.calculate(player))
            player.attributes.getCustomInstance(RadioLampEngineEntityAttributes.temperature)!!.baseValue = nextTemperature

            return nextTemperature
        }

        return currentTemperature
    }
}