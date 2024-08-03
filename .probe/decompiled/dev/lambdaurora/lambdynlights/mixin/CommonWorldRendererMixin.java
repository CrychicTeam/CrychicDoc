package dev.lambdaurora.lambdynlights.mixin;

import dev.lambdaurora.lambdynlights.LambDynLights;
import dev.lambdaurora.lambdynlights.accessor.WorldRendererAccessor;
import dev.lambdaurora.lambdynlights.config.DynamicLightsConfig;
import dev.lambdaurora.lambdynlights.config.QualityMode;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = { LevelRenderer.class }, priority = 900)
public abstract class CommonWorldRendererMixin implements WorldRendererAccessor {

    @Invoker("setSectionDirty")
    @Override
    public abstract void dynlights_setSectionDirty(int var1, int var2, int var3, boolean var4);

    @Inject(method = { "getLightColor(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)I" }, at = { @At("TAIL") }, cancellable = true)
    private static void onGetLightmapCoordinates(BlockAndTintGetter world, BlockState j, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (!world.m_8055_(pos).m_60804_(world, pos) && DynamicLightsConfig.Quality.get() != QualityMode.OFF) {
            int vanilla = (Integer) cir.getReturnValue();
            int value = LambDynLights.get().getLightmapWithDynamicLight(pos, vanilla);
            cir.setReturnValue(value);
        }
    }
}