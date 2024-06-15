package dev.somichev.item

import dev.somichev.RadioLampEngine
import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.Text

object RadioLampEngineItemGroups {
    private val JULY_GROUP = register(
        "july",
        PolymerItemGroupUtils.builder()
            .displayName(Text.translatable("itemGroup.radiolamp"))
            .icon{ ItemStack(Items.CACTUS) }
            .entries{ _, entries ->
                RadioLampEngineItems.items.values.forEach{
                    entries.add(it as ItemConvertible)
                }
            }
            .build()
    )

    fun register(id: String, group: ItemGroup): ItemGroup{
        PolymerItemGroupUtils.registerPolymerItemGroup(RadioLampEngine.id(id), group)

        return group
    }
}