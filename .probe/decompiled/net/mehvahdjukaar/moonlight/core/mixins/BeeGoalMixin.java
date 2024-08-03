package net.mehvahdjukaar.moonlight.core.mixins;

import net.mehvahdjukaar.moonlight.api.block.IBeeGrowable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(targets = { "net.minecraft.world.entity.animal.Bee$BeeGrowCropGoal" })
public abstract class BeeGoalMixin {

    @Shadow
    @Final
    Bee f_28021_;

    @Inject(method = { "tick" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;levelEvent(ILnet/minecraft/core/BlockPos;I)V", shift = Shift.BY, by = -2) }, locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void tick(CallbackInfo ci, int i, BlockPos blockPos, BlockState blockState, Block block, BlockState blockState2) {
        if (blockState2.m_60734_() instanceof IBeeGrowable beeGrowable) {
            beeGrowable.getPollinated(this.f_28021_.m_9236_(), blockPos, blockState2);
            this.f_28021_.m_9236_().m_46796_(2005, blockPos, 0);
            this.f_28021_.incrementNumCropsGrownSincePollination();
            ci.cancel();
        }
    }
}