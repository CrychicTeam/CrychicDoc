package dev.lambdaurora.lambdynlights.mixin.sodium;

import dev.lambdaurora.lambdynlights.SodiumDynamicLightHandler;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = { "me.jellysquid.mods.sodium.client.model.light.data.LightDataAccess" }, remap = false)
public abstract class LightDataAccessMixin {

    @Inject(method = { "getLightmap" }, at = { @At("RETURN") }, remap = false, cancellable = true)
    @Dynamic
    private static void lambdynlights$getLightmap(int word, CallbackInfoReturnable<Integer> cir) {
        int lightmap = SodiumDynamicLightHandler.lambdynlights$getLightmap((BlockPos) SodiumDynamicLightHandler.lambdynlights$pos.get(), word, cir.getReturnValueI());
        cir.setReturnValue(lightmap);
    }
}