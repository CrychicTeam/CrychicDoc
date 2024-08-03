package dev.xkmc.l2complements.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ Item.class })
public class ItemMixin {

    @WrapOperation(at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;getMaxDamage(Lnet/minecraft/world/item/ItemStack;)I", remap = false) }, method = { "getBarWidth" })
    public int l2complements$getBarWidth$getMaxDamage(Item item, ItemStack stack, Operation<Integer> op) {
        return stack.getMaxDamage();
    }

    @WrapOperation(at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;getMaxDamage(Lnet/minecraft/world/item/ItemStack;)I", remap = false) }, method = { "getBarColor" })
    public int l2complements$getBarColor$getMaxDamage(Item item, ItemStack stack, Operation<Integer> op) {
        return stack.getMaxDamage();
    }
}