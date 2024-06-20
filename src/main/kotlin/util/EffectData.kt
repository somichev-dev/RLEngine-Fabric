package util

import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.registry.entry.RegistryEntry

data class EffectData(
    val effect: RegistryEntry<StatusEffect>,
    val duration: Int,
    val amplifier: Int,
    val ambient: Boolean = true,
    val particles: Boolean = false,
    val icon: Boolean = false) {
    fun instantiate(): StatusEffectInstance = StatusEffectInstance(effect, duration, amplifier, ambient, particles, icon)
}
