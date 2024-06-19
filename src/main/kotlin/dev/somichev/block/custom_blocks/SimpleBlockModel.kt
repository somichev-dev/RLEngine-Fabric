package dev.somichev.block.custom_blocks

import eu.pb4.factorytools.api.virtualentity.BlockModel
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil
import eu.pb4.polymer.resourcepack.api.PolymerModelData
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.CustomModelDataComponent
import net.minecraft.util.math.Vec3d
import org.joml.Vector3f

class SimpleBlockModel(model: PolymerModelData) : BlockModel() {
    private var base: ItemDisplayElement = ItemDisplayElementUtil.createSimple(model.item().defaultStack.also {
        it[DataComponentTypes.CUSTOM_MODEL_DATA] = CustomModelDataComponent(model.value())
    })

    init {
        base.scale = Vector3f(0.7f)
        base.pitch = -90f
        base.offset = Vec3d(0.0, -0.4, 0.0)
        addElement(base)
    }
}