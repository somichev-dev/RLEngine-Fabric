package dev.somichev.item

import dev.somichev.block.RadioLampEngineBlocks
import dev.somichev.block.custom_blocks.CustomBlock
import dev.somichev.item.custom_blocks.CustomBlockItem
import dev.somichev.item.depers.*
import dev.somichev.item.drugs.MoonDust
import dev.somichev.item.extras.BitardHelmet
import dev.somichev.item.transports.FlyingBoat
import dev.somichev.item.transports.Glider
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object RadioLampEngineItems {
    val depersBottle    = register(Bottle.id, Bottle())
    val depersBreeze    = register(Breeze.id, Breeze())
    val depersCacadoo   = register(Cacadoo.id, Cacadoo())
    val depersDulo      = register(Dulo.id, Dulo())
    val depersDurilka   = register(Durilka.id, Durilka())
    val depersFan       = register(Fan.id, Fan())
    val depersFatman    = register(Fatman.id, Fatman())
    val depersFriday    = register(Friday.id, Friday())
    val depersPlitka    = register(Plitka.id, Plitka())
    val depersShield    = register(Shield.id, Shield())
    val depersShocker   = register(Shocker.id, Shocker())
    val depersThermopot = register(Thermopot.id, Thermopot())
    val depersToaster   = register(Toaster.id, Toaster())
    val glider          = register(Glider.id, Glider())
    val bitardHelmet    = register(BitardHelmet.id, BitardHelmet())
    val halfBrick       = register(HalfBrick.id, HalfBrick())
    val moonDust        = register(MoonDust.id, MoonDust())
    val flyingBoat      = register(FlyingBoat.id, FlyingBoat())
    val kalyk           = registerBlockItem(RadioLampEngineBlocks.kalyk, Item.Settings())

    private fun<T: Item> register(id: Identifier, item: T) = Registry.register(Registries.ITEM, id, item)

    fun <T: CustomBlock> registerBlockItem(block: T, settings: Item.Settings)
        = Registry.register(Registries.ITEM, Registries.BLOCK.getId(block as Block), CustomBlockItem(block, settings))
}