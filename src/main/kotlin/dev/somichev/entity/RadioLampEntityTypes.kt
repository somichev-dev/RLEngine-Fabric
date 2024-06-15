package dev.somichev.entity

import dev.somichev.entity.projectile.thrown.HalfBrickEntity
import eu.pb4.polymer.core.api.entity.PolymerEntityUtils
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RadioLampEntityTypes {
    val halfBrickEntity: EntityType<HalfBrickEntity> = Registry.register(
        Registries.ENTITY_TYPE,
        HalfBrickEntity.id,
        EntityType.Builder.create(::HalfBrickEntity, SpawnGroup.MISC)
            .dimensions(0.25f, 0.25f)
            .maxTrackingRange(4)
            .trackingTickInterval(10)
            .build()
    )

    fun init(){
        PolymerEntityUtils.registerType(halfBrickEntity)
    }
}