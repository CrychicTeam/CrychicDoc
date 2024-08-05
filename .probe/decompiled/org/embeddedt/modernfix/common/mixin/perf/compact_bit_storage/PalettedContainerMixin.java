package org.embeddedt.modernfix.common.mixin.perf.compact_bit_storage;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.chunk.PalettedContainer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ PalettedContainer.class })
public abstract class PalettedContainerMixin<T> {

    @Shadow
    private volatile PalettedContainer.Data<T> data;

    @Shadow
    protected abstract PalettedContainer.Data<T> createOrReuseData(@Nullable PalettedContainer.Data<T> var1, int var2);

    @Inject(method = { "read(Lnet/minecraft/network/FriendlyByteBuf;)V" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/world/level/chunk/PalettedContainer;data:Lnet/minecraft/world/level/chunk/PalettedContainer$Data;", opcode = 181, shift = Shift.AFTER) }, locals = LocalCapture.CAPTURE_FAILHARD)
    private void validateData(FriendlyByteBuf buffer, CallbackInfo ci, int i) {
        if (i > 1) {
            long[] storArray = this.data.storage().getRaw();
            boolean empty = true;
            for (long l : storArray) {
                if (l != 0L) {
                    empty = false;
                    break;
                }
            }
            if (empty && storArray.length > 0) {
                T value = this.data.palette().valueFor(0);
                this.data = this.createOrReuseData(null, 0);
                this.data.palette().idFor(value);
            }
        }
    }
}