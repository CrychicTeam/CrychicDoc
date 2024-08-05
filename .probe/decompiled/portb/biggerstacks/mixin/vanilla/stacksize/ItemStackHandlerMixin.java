package portb.biggerstacks.mixin.vanilla.stacksize;

import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin({ ItemStackHandler.class })
public class ItemStackHandlerMixin {

    @Inject(method = { "getSlotLimit" }, at = { @At("RETURN") }, cancellable = true, remap = false)
    private void increaseStackLimit(int slot, CallbackInfoReturnable<Integer> returnInfo) {
        StackSizeHelper.scaleSlotLimit(returnInfo);
    }
}