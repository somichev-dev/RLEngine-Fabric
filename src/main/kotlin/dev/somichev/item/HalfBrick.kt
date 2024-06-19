package dev.somichev.item

import dev.somichev.RadioLampEngine
import dev.somichev.entity.projectile.thrown.HalfBrickEntity
import eu.pb4.polymer.core.api.item.PolymerItem
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.EggItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.stat.Stats
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import kotlin.math.max

class HalfBrick: EggItem(Settings().maxCount(64)), PolymerItem {
    companion object{
        val id = RadioLampEngine.id("half_brick")
        val model = PolymerResourcePackUtils.requestModel(Items.EGG, id.withPrefixedPath("item/"))
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val itemStack = user.getStackInHand(hand)

        world.playSound(
            null as PlayerEntity?,
            user.x,
            user.y,
            user.z,
            SoundEvents.ENTITY_EGG_THROW,
            SoundCategory.PLAYERS,
            0.5f,
            0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f)
        )
        if(!world.isClient()){
            val brickEntity = HalfBrickEntity(world, user)
            brickEntity.setItem(itemStack)
            brickEntity.setVelocity(
                user,
                max(user.pitch - 15.0f, -90.0f),
                user.yaw,
                0.0f,
                0.75f,
                1.0f
            )
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this))
        itemStack.decrementUnlessCreative(1, user)

        return TypedActionResult.success(itemStack, world.isClient())
    }

    override fun getPolymerCustomModelData(itemStack: ItemStack?, player: ServerPlayerEntity?): Int = model.value()
    override fun getPolymerItem(itemStack: ItemStack?, player: ServerPlayerEntity?): Item? = model.item()
}