package dev.somichev.block.custom_blocks

import eu.pb4.factorytools.api.virtualentity.BlockModel
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil
import eu.pb4.polymer.resourcepack.api.PolymerModelData
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.CustomModelDataComponent
import net.minecraft.util.math.AffineTransformation
import org.joml.Quaternionf
import org.joml.Vector3f

class SimpleOverlayedBlockModel(model: PolymerModelData) : BlockModel() {
    private var base: ItemDisplayElement = ItemDisplayElementUtil.createSimple(model.item().defaultStack.also {
        it[DataComponentTypes.CUSTOM_MODEL_DATA] = CustomModelDataComponent(model.value())
    })

    init {
        base.setTransformation(
            AffineTransformation(
                Vector3f(0f, 0.002f, 0f).mul(2f), Quaternionf(), Vector3f(1.001f, 1.005f, 1.001f).mul(2f), Quaternionf()
            )
        )
        addElement(base)
    }
}