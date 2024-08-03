package brightspark.asynclocator.mixins;

import brightspark.asynclocator.ALConstants;
import brightspark.asynclocator.AsyncLocator;
import brightspark.asynclocator.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.entity.animal.Dolphin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(targets = { "net.minecraft.world.entity.animal.Dolphin$DolphinSwimToTreasureGoal" })
public class DolphinSwimToTreasureGoalMixin {

    @Final
    @Shadow
    private Dolphin dolphin;

    @Shadow
    private boolean stuck;

    private AsyncLocator.LocateTask<BlockPos> locateTask = null;

    @Inject(method = { "start" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;findNearestMapStructure(Lnet/minecraft/tags/TagKey;Lnet/minecraft/core/BlockPos;IZ)Lnet/minecraft/core/BlockPos;") }, cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    public void findTreasureAsync(CallbackInfo ci, ServerLevel level, BlockPos blockpos) {
        if (Services.CONFIG.dolphinTreasureEnabled()) {
            ALConstants.logDebug("Intercepted DolphinSwimToTreasureGoal#start call");
            this.handleFindTreasureAsync(level, blockpos);
            ci.cancel();
        }
    }

    @Inject(method = { "canContinueToUse" }, at = { @At("HEAD") }, cancellable = true)
    public void continueToUseIfLocatingTreasure(CallbackInfoReturnable<Boolean> cir) {
        if (this.locateTask != null) {
            ALConstants.logDebug("Locating task ongoing - returning true for continueToUse()");
            cir.setReturnValue(true);
        }
    }

    @Inject(method = { "stop" }, at = { @At("HEAD") })
    public void stopLocatingTreasure(CallbackInfo ci) {
        if (this.locateTask != null) {
            ALConstants.logDebug("Locating task ongoing - cancelling during stop()");
            this.locateTask.cancel();
            this.locateTask = null;
        }
    }

    @Inject(method = { "tick" }, at = { @At("HEAD") }, cancellable = true)
    public void skipTickingIfLocatingTreasure(CallbackInfo ci) {
        if (this.locateTask != null) {
            ALConstants.logDebug("Locating task ongoing - skipping tick()");
            ci.cancel();
        }
    }

    private void handleFindTreasureAsync(ServerLevel level, BlockPos blockPos) {
        this.locateTask = AsyncLocator.locate(level, StructureTags.DOLPHIN_LOCATED, blockPos, 50, false).thenOnServerThread(pos -> this.handleLocationFound(level, pos));
    }

    private void handleLocationFound(ServerLevel level, BlockPos pos) {
        this.locateTask = null;
        if (pos != null) {
            ALConstants.logInfo("Location found - updating dolphin treasure pos");
            this.dolphin.setTreasurePos(pos);
            level.broadcastEntityEvent(this.dolphin, (byte) 38);
        } else {
            ALConstants.logInfo("No location found - marking dolphin as stuck");
            this.stuck = true;
        }
    }
}