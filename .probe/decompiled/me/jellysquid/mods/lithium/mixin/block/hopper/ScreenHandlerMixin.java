package me.jellysquid.mods.lithium.mixin.block.hopper;

import me.jellysquid.mods.lithium.api.inventory.LithiumInventory;
import me.jellysquid.mods.lithium.common.hopper.InventoryHelper;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ AbstractContainerMenu.class })
public abstract class ScreenHandlerMixin {

    @Inject(method = { "calculateComparatorOutput(Lnet/minecraft/inventory/Inventory;)I" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;size()I", shift = Shift.BEFORE, ordinal = 0) }, cancellable = true)
    private static void getFastOutputStrength(Container inventory, CallbackInfoReturnable<Integer> cir) {
        if (inventory instanceof LithiumInventory optimizedInventory) {
            cir.setReturnValue(InventoryHelper.getLithiumStackList(optimizedInventory).getSignalStrength(inventory));
        }
    }
}