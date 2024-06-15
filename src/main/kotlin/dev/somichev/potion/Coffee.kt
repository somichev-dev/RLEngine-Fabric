package dev.somichev.potion

import dev.somichev.RadioLampEngine
import eu.pb4.polymer.core.api.other.SimplePolymerPotion
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.potion.Potion
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

class Coffee : SimplePolymerPotion(
    StatusEffectInstance(StatusEffects.SPEED, 20*30, 1)
) {
    val INSTANCE = Registry.register(
        Registries.POTION,
        RadioLampEngine.id("coffee"),
        Potion(
            StatusEffectInstance(StatusEffects.SPEED, 20*30, 1)
        )
    )
}