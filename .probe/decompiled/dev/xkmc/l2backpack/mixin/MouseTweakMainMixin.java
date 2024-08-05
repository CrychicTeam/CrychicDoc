package dev.xkmc.l2backpack.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.l2backpack.compat.MouseTweakCompat;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = { "yalter.mousetweaks.Main" }, remap = false)
public class MouseTweakMainMixin {

    @WrapOperation(method = { "findPushSlots" }, at = { @At(value = "INVOKE", remap = true, target = "Lnet/minecraft/world/inventory/Slot;getItem()Lnet/minecraft/world/item/ItemStack;") })
    private static ItemStack l2backpack$pushItem$drawerDelegate(Slot instance, Operation<ItemStack> original) {
        ItemStack stack = (ItemStack) original.call(new Object[] { instance });
        return MouseTweakCompat.wrapSlotGet(stack);
    }

    @WrapOperation(method = { "findPullSlot" }, at = { @At(value = "INVOKE", remap = true, target = "Lnet/minecraft/world/inventory/Slot;getItem()Lnet/minecraft/world/item/ItemStack;") })
    private static ItemStack l2backpack$pullItem$drawerDelegate(Slot instance, Operation<ItemStack> original) {
        ItemStack stack = (ItemStack) original.call(new Object[] { instance });
        return MouseTweakCompat.wrapSlotGet(stack);
    }

    @WrapOperation(method = { "onMouseScrolled" }, at = { @At(value = "INVOKE", remap = true, target = "Lnet/minecraft/world/inventory/Slot;getItem()Lnet/minecraft/world/item/ItemStack;") })
    private static ItemStack l2backpack$scrollItem$drawerDelegate(Slot instance, Operation<ItemStack> original) {
        ItemStack stack = (ItemStack) original.call(new Object[] { instance });
        return MouseTweakCompat.wrapSlotGet(stack);
    }
}