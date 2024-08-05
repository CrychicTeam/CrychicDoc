package me.jellysquid.mods.lithium.mixin.block.hopper;

import me.jellysquid.mods.lithium.common.hopper.BlockStateOnlyInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ComposterMixin {

    @Mixin(targets = { "net.minecraft.block.ComposterBlock$ComposterInventory" })
    abstract static class ComposterBlockComposterInventoryMixin implements BlockStateOnlyInventory {

        @Shadow
        private boolean changed;

        @Inject(method = { "markDirty()V" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/block/ComposterBlock$ComposterInventory;removeStack(I)Lnet/minecraft/item/ItemStack;") })
        private void resetDirty(CallbackInfo ci) {
            this.changed = false;
        }
    }

    @Mixin(targets = { "net.minecraft.block.ComposterBlock$DummyInventory" })
    abstract static class ComposterBlockDummyInventoryMixin implements BlockStateOnlyInventory {
    }

    @Mixin(targets = { "net.minecraft.block.ComposterBlock$FullComposterInventory" })
    abstract static class ComposterBlockFullComposterInventoryMixin implements BlockStateOnlyInventory {
    }
}