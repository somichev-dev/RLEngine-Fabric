package dev.somichev.mixin.totems;

import dev.somichev.item.CustomTotemItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Redirect(method = "tryUseTotem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    boolean processReviveComponent(ItemStack instance, Item item) {
        return instance.isOf(item) || instance.getComponents().contains(CustomTotemItem.Companion.getReviveComponent());
    }
}
