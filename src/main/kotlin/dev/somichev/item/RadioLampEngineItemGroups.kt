package dev.somichev.item

import dev.somichev.RadioLampEngine
import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.Text

object RadioLampEngineItemGroups {
    val groupEntries = listOf(
        RadioLampEngineItems.depersBottle,
        RadioLampEngineItems.depersBreeze,
        RadioLampEngineItems.depersCacadoo,
        RadioLampEngineItems.depersDulo,
        RadioLampEngineItems.depersDurilka,
        RadioLampEngineItems.depersFan,
        RadioLampEngineItems.depersFatman,
        RadioLampEngineItems.depersFriday,
        RadioLampEngineItems.depersPlitka,
        RadioLampEngineItems.depersShield,
        RadioLampEngineItems.depersShocker,
        RadioLampEngineItems.depersThermopot,
        RadioLampEngineItems.depersToaster,
        RadioLampEngineItems.glider,
        RadioLampEngineItems.bitardHelmet,
        RadioLampEngineItems.halfBrick,
        RadioLampEngineItems.moonDust,
        RadioLampEngineItems.flyingBoat,
        RadioLampEngineItems.kalyk,
    )
    private val JULY_GROUP = register(
        "july",
        PolymerItemGroupUtils.builder()
            .displayName(Text.translatable("itemGroup.radiolamp"))
            .icon{ ItemStack(Items.CACTUS) }
            .entries{ _, entries ->
                groupEntries.forEach{
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