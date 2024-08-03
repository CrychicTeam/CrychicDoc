package me.jellysquid.mods.lithium.mixin.chunk.no_locking;

import net.minecraft.util.ThreadingDetector;
import net.minecraft.world.level.chunk.PalettedContainer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ PalettedContainer.class })
public class PalettedContainerMixin {

    @Shadow
    @Final
    @Mutable
    private ThreadingDetector threadingDetector;

    @Inject(method = { "<init>(Lnet/minecraft/util/collection/IndexedIterable;Ljava/lang/Object;Lnet/minecraft/world/chunk/PalettedContainer$PaletteProvider;)V", "<init>(Lnet/minecraft/util/collection/IndexedIterable;Lnet/minecraft/world/chunk/PalettedContainer$PaletteProvider;Lnet/minecraft/world/chunk/PalettedContainer$Data;)V", "<init>(Lnet/minecraft/util/collection/IndexedIterable;Lnet/minecraft/world/chunk/PalettedContainer$PaletteProvider;Lnet/minecraft/world/chunk/PalettedContainer$DataProvider;Lnet/minecraft/util/collection/PaletteStorage;Ljava/util/List;)V" }, at = { @At("TAIL") })
    public void removeLockHelper(CallbackInfo ci) {
        this.threadingDetector = null;
    }

    @Overwrite
    public void acquire() {
    }

    @Overwrite
    public void release() {
    }
}