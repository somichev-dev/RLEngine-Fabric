package dev.somichev.entity.attribute

import dev.somichev.RadioLampEngine
import eu.pb4.polymer.core.api.utils.PolymerSyncedObject
import net.minecraft.entity.attribute.ClampedEntityAttribute
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.server.network.ServerPlayerEntity

object RadioLampEngineEntityAttributes {
    val temperature = register("temperature",
        PolymerClampedEntityAttribute("attribute.name.temperature", 0.0, -100.0, 100.0).setTracked(false)
    )

    private fun register(id: String, attribute: EntityAttribute): RegistryEntry<EntityAttribute> =
        Registry.registerReference(Registries.ATTRIBUTE, RadioLampEngine.id(id), attribute)

    class PolymerClampedEntityAttribute(
        translationKey: String?, fallback: Double, min: Double, max: Double
    ) : ClampedEntityAttribute(translationKey, fallback, min, max), PolymerSyncedObject<EntityAttribute> {
        override fun getPolymerReplacement(player: ServerPlayerEntity?): EntityAttribute? {
            return null
        }
    }
}