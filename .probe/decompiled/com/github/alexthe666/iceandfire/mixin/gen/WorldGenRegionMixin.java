package com.github.alexthe666.iceandfire.mixin.gen;

import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ WorldGenRegion.class })
public class WorldGenRegionMixin {

    @Shadow
    @Nullable
    private Supplier<String> currentlyGenerating;

    @Inject(method = { "ensureCanWrite" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/Util;logAndPauseIfInIde(Ljava/lang/String;)V", shift = Shift.BEFORE) }, cancellable = true)
    private void skipLog(BlockPos position, CallbackInfoReturnable<Boolean> callback) {
        if (this.currentlyGenerating != null && ((String) this.currentlyGenerating.get()).contains("iceandfire")) {
            callback.setReturnValue(false);
        }
    }
}