package dev.somichev.entity.projectile.thrown

import dev.somichev.RadioLampEngine
import dev.somichev.entity.RadioLampEngineEntityTypes
import dev.somichev.item.HalfBrick
import dev.somichev.item.RadioLampEngineItems
import eu.pb4.polymer.core.api.entity.PolymerEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.thrown.EggEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.particle.ItemStackParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.world.World

class HalfBrickEntity : ThrownItemEntity, PolymerEntity {
    companion object {
        val id = RadioLampEngine.id("half_brick")
    }

    constructor(type: EntityType<HalfBrickEntity>, world: World) : super(type, world)
    constructor(world: World, owner: LivingEntity) : super(RadioLampEngineEntityTypes.halfBrickEntity, owner, world)
    constructor(world: World, x: Double, y: Double, z: Double) : super(RadioLampEngineEntityTypes.halfBrickEntity, x, y, z, world)

    override fun handleStatus(status: Byte) {
        if (status.toInt() == 3) {
            val d = 0.08

            for (i in 0..7) {
                world.addParticle(
                    ItemStackParticleEffect(ParticleTypes.ITEM, this.stack),
                    this.x,
                    this.y,
                    this.z,
                    (random.nextFloat().toDouble() - 0.5) * 0.08,
                    (random.nextFloat().toDouble() - 0.5) * 0.08,
                    (random.nextFloat().toDouble() - 0.5) * 0.08
                )
            }
        }
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        super.onEntityHit(entityHitResult)
        entityHitResult.entity.damage(this.damageSources.thrown(this, this.owner), 0.5f)
    }

    override fun onCollision(hitResult: HitResult) {
        super.onCollision(hitResult)

        if (!this.world.isClient) {
            dropStack(stack)

            world.sendEntityStatus(this, 3.toByte())
            this.discard()
        }
    }

    override fun getDefaultItem(): HalfBrick = RadioLampEngineItems.halfBrick
    override fun getPolymerEntityType(player: ServerPlayerEntity): EntityType<EggEntity> = EntityType.EGG
}