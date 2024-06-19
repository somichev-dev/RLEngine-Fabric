package dev.somichev.entity.rideable

import dev.somichev.RadioLampEngine
import eu.pb4.polymer.core.api.entity.PolymerEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.vehicle.BoatEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.world.World

class FlyingBoatEntity(type: EntityType<FlyingBoatEntity>, world: World) : BoatEntity(type, world), PolymerEntity {
    init{
        setNoGravity(true)
        world.players.forEach {
            it.sendMessage(Text.of(hasNoGravity().toString()))
        }
    }
    companion object{
        val id = RadioLampEngine.id("flying_boat")
    }

    override fun getPolymerEntityType(player: ServerPlayerEntity): EntityType<BoatEntity> = EntityType.BOAT
}