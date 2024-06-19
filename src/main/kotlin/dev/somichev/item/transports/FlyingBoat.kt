package dev.somichev.item.transports

import dev.somichev.RadioLampEngine
import dev.somichev.entity.RadioLampEngineEntityTypes
import dev.somichev.entity.rideable.FlyingBoatEntity
import eu.pb4.polymer.core.api.item.PolymerItem
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils
import net.minecraft.entity.vehicle.BoatEntity
import net.minecraft.item.*
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.ActionResult

class FlyingBoat: BoatItem(false, BoatEntity.Type.OAK, Settings().maxCount(1)), PolymerItem {
    companion object{
        val id = RadioLampEngine.id("flying_boat")
        val model = PolymerResourcePackUtils.requestModel(Items.ACACIA_BOAT, id.withPrefixedPath("item/"))
    }

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val position = context.blockPos.toCenterPos()
        val world = context.world
        if(!world.isClient()){
            val flyingBoatEntity = FlyingBoatEntity(RadioLampEngineEntityTypes.flyingBoat, world)
            flyingBoatEntity.setPosition(position)
        }
        return ActionResult.SUCCESS
    }

    override fun getPolymerItem(itemStack: ItemStack?, player: ServerPlayerEntity?): Item = model.item()

}