package dev.somichev.world

import dev.somichev.world.codecs.PlayersDataCodec
import net.minecraft.entity.LivingEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.server.MinecraftServer
import net.minecraft.world.PersistentState
import net.minecraft.world.World
import java.util.*

class RadioLampPersistentState : PersistentState() {
    var players: HashMap<UUID, PlayerData> = HashMap()

    companion object {
        private var instance: RadioLampPersistentState? = null
        val INSTANCE: RadioLampPersistentState
            get() = instance ?: throw Error("net")
        private val type = Type({ RadioLampPersistentState() }, Companion::createFromNbt, null)

        private fun createFromNbt(
            tag: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup
        ): RadioLampPersistentState {
            val state = RadioLampPersistentState()

            state.players = PlayersDataCodec.decode(tag.getCompound("players_data"), registryLookup)

            return state
        }

        fun getServerState(server: MinecraftServer): RadioLampPersistentState {
            val persistentStateManager = server.getWorld(World.OVERWORLD)!!.persistentStateManager

            val state = persistentStateManager.getOrCreate(type, "radiolamp_state") as RadioLampPersistentState

            state.markDirty()

            instance = state

            return state
        }

        fun getPlayerState(player: LivingEntity): PlayerData {
            val serverState = INSTANCE

            val playerState = serverState.players.computeIfAbsent(player.uuid) { PlayerData() }

            return playerState
        }
    }

    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup): NbtCompound {
        nbt.put("players_data", PlayersDataCodec.encode(players, registryLookup))

        return nbt
    }
}