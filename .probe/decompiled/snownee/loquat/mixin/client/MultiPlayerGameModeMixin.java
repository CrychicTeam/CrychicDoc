package snownee.loquat.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import snownee.loquat.core.select.SelectionManager;
import snownee.loquat.network.CSelectionClickPacket;

@Mixin({ MultiPlayerGameMode.class })
public class MultiPlayerGameModeMixin {

    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(method = { "startDestroyBlock" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/Tutorial;onDestroyBlock(Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;F)V") }, cancellable = true)
    private void loquat$startDestroyBlock(BlockPos pos, Direction face, CallbackInfoReturnable<Boolean> cir) {
        if (SelectionManager.isHoldingTool(this.minecraft.player)) {
            CSelectionClickPacket.send(pos);
            cir.setReturnValue(true);
        }
    }

    @Inject(method = { "continueDestroyBlock" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/Tutorial;onDestroyBlock(Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;F)V") }, cancellable = true)
    private void loquat$continueDestroyBlock(BlockPos posBlock, Direction directionFacing, CallbackInfoReturnable<Boolean> cir) {
        if (SelectionManager.isHoldingTool(this.minecraft.player)) {
            cir.setReturnValue(true);
        }
    }
}