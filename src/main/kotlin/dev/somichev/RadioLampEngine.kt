package dev.somichev

import dev.somichev.entity.RadioLampEntityTypes
import dev.somichev.item.RadioLampEngineItemGroups
import dev.somichev.item.RadioLampEngineItems
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory

object RadioLampEngine : ModInitializer {
    private val logger = LoggerFactory.getLogger("radiolampengine")
	val modId = "radiolampengine"
	fun id(name: String) = Identifier(modId, name)
	override fun onInitialize() {
		RadioLampEngineItems
		RadioLampEntityTypes.init()
		RadioLampEngineItemGroups
		logger.info("W E L C O M E  T O  J U L E!")

		PolymerResourcePackUtils.addModAssets(modId)
		PolymerResourcePackUtils.buildMain()
		PolymerResourcePackUtils.markAsRequired()
	}
}