package dev.somichev.item

import com.mojang.serialization.Codec
import dev.somichev.RadioLampEngine
import dev.somichev.item.depers.Durilka
import dev.somichev.util.registerComponentType
import eu.pb4.polymer.core.api.item.SimplePolymerItem
import net.minecraft.component.DataComponentType
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.network.codec.PacketCodec
import net.minecraft.util.Unit as MinecraftUnit

// copy postProcessComponents from here if you want to extend a different item class
open class CustomTotemItem : SimplePolymerItem(Settings().maxCount(1), Items.TOTEM_OF_UNDYING) {
    companion object {
        val reviveComponent: DataComponentType<MinecraftUnit> = registerComponentType(RadioLampEngine.id("revive")) {
            it.codec(Codec.unit(MinecraftUnit.INSTANCE)).packetCodec(PacketCodec.unit(MinecraftUnit.INSTANCE))
        }
    }

    override fun postProcessComponents(stack: ItemStack) {
        super.postProcessComponents(stack)
        stack[reviveComponent] = MinecraftUnit.INSTANCE
    }
}