package dev.somichev

import dev.somichev.block.RadioLampEngineBlocks
import dev.somichev.entity.RadioLampEngineEntityTypes
import dev.somichev.entity.attribute.RadioLampEngineEntityAttributes
import dev.somichev.events.PlayerEvents
import dev.somichev.item.RadioLampEngineItemGroups
import dev.somichev.item.RadioLampEngineItems
import dev.somichev.listener.climate.TempBarJoinListener
import dev.somichev.listener.climate.TemperaturePlayerTickListener
import dev.somichev.world.PositionState
import dev.somichev.world.RadioLampPersistentState
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents
import net.minecraft.entity.SpawnReason
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket
import net.minecraft.network.packet.s2c.play.PositionFlag
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.world.GameMode
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object RadioLampEngine : ModInitializer {
    val logger: Logger = LoggerFactory.getLogger("radiolampengine")
    private const val MOD_ID = "radiolampengine"
    fun id(name: String) = Identifier(MOD_ID, name)
    override fun onInitialize() {
        ServerWorldEvents.LOAD.register { server, _ ->
            RadioLampPersistentState.getServerState(server)
        }

        RadioLampEngineBlocks
        RadioLampEngineItems
        RadioLampEngineItemGroups
        RadioLampEngineEntityAttributes
        RadioLampEngineEntityTypes.init()
        logger.info("W E L C O M E  T O  J U L E!")

        PolymerResourcePackUtils.addModAssets(MOD_ID)
        PolymerResourcePackUtils.buildMain()
        PolymerResourcePackUtils.markAsRequired()

        PlayerEvents.DEATH_EVENT.register { player, _, source ->
            if (player.isCreative || player.isSpectator) return@register
            player.changeGameMode(GameMode.SPECTATOR)

            val world = player.serverWorld
            val corpse = RadioLampEngineEntityTypes.corpseEntity.spawn(world, player.blockPos, SpawnReason.TRIGGERED)
            if (corpse != null) {
                val centerPos = player.blockPos.toCenterPos()
                corpse.teleport(
                    world, centerPos.x, centerPos.y - 1, centerPos.z, PositionFlag.entries.toSet(), player.yaw, 0f
                )
                RadioLampPersistentState.getPlayerState(player).gravePos =
                    PositionState(corpse.blockPos, corpse.world.dimensionEntry.key.get())
                corpse.playerUUID = player.uuid
                corpse.playerDeathText = source.getDeathMessage(player)
            }
            player.networkHandler.sendPacket(GameStateChangeS2CPacket(GameStateChangeS2CPacket.IMMEDIATE_RESPAWN, 1f))
        }
        PlayerEvents.SPAWN_EVENT.register { player, _ ->
            // TODO: когда будут сделаны жизни заменить на нормальную проверку
            val grave = RadioLampPersistentState.getPlayerState(player).gravePos
            if (player.isSpectator && grave != null) {
                val text = Text.literal("Ваш труп находится ")
                if (grave.dimension != player.world.dimensionEntry.key.get()) text.append(Text.literal("в измерении ${grave.dimension.value} "))
                text.append(Text.literal("по координатам X: ${grave.blockPos.x} Y: ${grave.blockPos.y} Z: ${grave.blockPos.z}"))
                player.sendMessage(text.formatted(Formatting.BOLD))
            }
        }

        registerListeners()
    }

    fun registerListeners() {
        PlayerEvents.JOIN_EVENT.register(TempBarJoinListener)
        PlayerEvents.TICK_START_EVENT.register(TemperaturePlayerTickListener)
    }

    fun log(s: String) = logger.info(s)
}