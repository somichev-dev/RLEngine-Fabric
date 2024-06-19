package com.vedi.skyblock.world.state.codecs

import net.minecraft.nbt.NbtElement
import net.minecraft.registry.RegistryWrapper

interface NbtCodec<T, N : NbtElement> {
    fun encode(value: T, registryLookup: RegistryWrapper.WrapperLookup): N
    fun decode(element: N, registryLookup: RegistryWrapper.WrapperLookup): T
}