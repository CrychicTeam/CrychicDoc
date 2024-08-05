package dev.lambdaurora.lambdynlights.mixin;

import dev.lambdaurora.lambdynlights.DynamicLightSource;
import dev.lambdaurora.lambdynlights.config.DynamicLightsConfig;
import java.util.Iterator;
import net.minecraft.core.BlockPos;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ Level.class })
public abstract class WorldMixin {

    @Shadow
    public abstract boolean isClientSide();

    @Shadow
    @Nullable
    public abstract BlockEntity getBlockEntity(BlockPos var1);

    @Inject(method = { "tickBlockEntities" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/TickingBlockEntity;tick()V", shift = Shift.BEFORE) }, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void onBlockEntityTick(CallbackInfo ci, ProfilerFiller profiler, Iterator<BlockEntity> iterator, TickingBlockEntity blockEntityTickInvoker) {
        if (this.isClientSide() && DynamicLightsConfig.TileEntityLighting.get()) {
            BlockEntity blockEntity = this.getBlockEntity(blockEntityTickInvoker.getPos());
            if (blockEntity != null) {
                ((DynamicLightSource) blockEntity).tdv$dynamicLightTick();
            }
        }
    }
}