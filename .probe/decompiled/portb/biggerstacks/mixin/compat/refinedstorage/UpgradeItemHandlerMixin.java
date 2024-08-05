package portb.biggerstacks.mixin.compat.refinedstorage;

import com.refinedmods.refinedstorage.inventory.item.UpgradeItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.config.ServerConfig;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin({ UpgradeItemHandler.class })
public class UpgradeItemHandlerMixin {

    @Inject(method = { "getStackInteractCount" }, at = { @At("RETURN") }, cancellable = true, require = 0, remap = false)
    private void increaseTransferRate(CallbackInfoReturnable<Integer> returnInfo) {
        if (ServerConfig.get().increaseTransferRate.get()) {
            StackSizeHelper.scaleTransferRate(returnInfo, false);
        }
    }
}