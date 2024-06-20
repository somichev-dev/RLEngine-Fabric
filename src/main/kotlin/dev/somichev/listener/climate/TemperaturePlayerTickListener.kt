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
import net.minecraft.world.GameMode

object TemperaturePlayerTickListener : PlayerTickStart {
    private const val INERTIA = 0.002

    override fun onPlayerTickStart(player: ServerPlayerEntity, server: MinecraftServer) {
        server.bossBarManager.get(RadioLampEngine.id(player.uuidAsString.lowercase()))?.let {
            val temperature = calculatePlayerTemperature(player)

            if (player.interactionManager.gameMode != GameMode.SPECTATOR &&
                player.attributes.hasAttribute(RadioLampEngineEntityAttributes.temperature)) {
                player.attributes.getCustomInstance(RadioLampEngineEntityAttributes.temperature)!!.baseValue = temperature
                TemperatureEffects.applyTo(player)
            }

            it.value = temperature.toInt() + 100
            it.name = Text.literal("${temperature.toInt()}°C")
        }
    }

    private fun calculatePlayerTemperature(player: PlayerEntity): Double =
        lerp(INERTIA, player.attributes.getValue(RadioLampEngineEntityAttributes.temperature), PlayerWarmness.calculate(player))
}