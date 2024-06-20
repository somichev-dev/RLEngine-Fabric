package dev.somichev.listener.climate

import dev.somichev.RadioLampEngine
import dev.somichev.events.PlayerEvents
import net.minecraft.entity.boss.BossBar
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

object TempBarJoinListener : PlayerEvents.PlayerJoin {
    override fun onPlayerJoin(player: ServerPlayerEntity, server: MinecraftServer) {
        val barId = RadioLampEngine.id(player.uuidAsString.lowercase())

        if (server.bossBarManager.get(barId) == null) {
            val bar = server.bossBarManager.add(barId, Text.literal("stas")) // "рот болит и попе больно"

            bar.color = BossBar.Color.PINK
            bar.style = BossBar.Style.NOTCHED_12
            bar.maxValue = 200
            bar.value = 100

            bar.addPlayer(player)
        }
    }
}