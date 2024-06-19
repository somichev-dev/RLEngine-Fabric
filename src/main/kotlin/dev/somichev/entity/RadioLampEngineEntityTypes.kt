package dev.somichev.entity

import dev.somichev.entity.projectile.thrown.HalfBrickEntity
import dev.somichev.entity.rideable.FlyingBoatEntity
import eu.pb4.polymer.core.api.entity.PolymerEntityUtils
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RadioLampEngineEntityTypes {
    val halfBrickEntity: EntityType<HalfBrickEntity> = Registry.register(
        Registries.ENTITY_TYPE,
        HalfBrickEntity.id,
        EntityType.Builder.create(::HalfBrickEntity, SpawnGroup.MISC)
            .dimensions(0.25f, 0.25f)
            .maxTrackingRange(4)
            .trackingTickInterval(10)
            .build()
    )
    val flyingBoat: EntityType<FlyingBoatEntity> = Registry.register(
        Registries.ENTITY_TYPE,
        FlyingBoatEntity.id,
        EntityType.Builder.create(::FlyingBoatEntity, SpawnGroup.MISC)
            .dimensions(1.375f, 0.5625f)
            .eyeHeight(0.5625f)
            .maxTrackingRange(10)
            .build()
    )
    val corpseEntity: EntityType<CorpseEntity> = Registry.register(
        Registries.ENTITY_TYPE,
        CorpseEntity.id,
        EntityType.Builder.create(::CorpseEntity, SpawnGroup.MISC).dimensions(1f, 1f).maxTrackingRange(8).build()
    )

    fun init() {
        PolymerEntityUtils.registerType(halfBrickEntity, flyingBoat, corpseEntity)
    }
}