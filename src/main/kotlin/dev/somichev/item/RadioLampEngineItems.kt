package dev.somichev.item

import dev.somichev.item.depers.*
import dev.somichev.item.drugs.MoonDust
import dev.somichev.item.extras.BitardHelmet
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object RadioLampEngineItems {
    val items = mapOf<String, Item>(
    "depersBottle"    to register(Bottle.id, Bottle()),
    "depersBreeze"    to register(Breeze.id, Breeze()),
    "depersCacadoo"   to register(Cacadoo.id, Cacadoo()),
    "depersDulo"      to register(Dulo.id, Dulo()),
    "depersDurilka"   to register(Durilka.id, Durilka()),
    "depersFan"       to register(Fan.id, Fan()),
    "depersFatman"    to register(Fatman.id, Fatman()),
    "depersFriday"    to register(Friday.id, Friday()),
    "depersPlitka"    to register(Plitka.id, Plitka()),
    "depersShield"    to register(Shield.id, Shield()),
    "depersShocker"   to register(Shocker.id, Shocker()),
    "depersThermopot" to register(Thermopot.id, Thermopot()),
    "depersToaster"   to register(Toaster.id, Toaster()),
    "glider"          to register(Glider.id, Glider()),
    "bitardHelmet"    to register(BitardHelmet.id, BitardHelmet()),
    "halfBrick"       to register(HalfBrick.id, HalfBrick()),
    "moonDust"        to register(MoonDust.id, MoonDust()),
    )
    private fun<T: Item> register(id: Identifier, item: T) = Registry.register(Registries.ITEM, id, item)
}