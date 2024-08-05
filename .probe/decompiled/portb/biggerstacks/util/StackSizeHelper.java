package portb.biggerstacks.util;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.config.StackSizeRules;

public class StackSizeHelper {

    public static void scaleSlotLimit(CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        callbackInfoReturnable.cancel();
        callbackInfoReturnable.setReturnValue(scaleSlotLimit((Integer) callbackInfoReturnable.getReturnValue()));
    }

    public static int scaleSlotLimit(int original) {
        int newStackSize = StackSizeRules.getMaxStackSize();
        if (original == 1) {
            return 1;
        } else {
            return newStackSize < 64 ? 64 : Math.max(original, original * newStackSize / 64);
        }
    }

    public static int getNewStackSize() {
        return Math.max(StackSizeRules.getMaxStackSize(), 64);
    }

    public static void scaleTransferRate(CallbackInfoReturnable<Integer> callbackInfoReturnable, boolean respectSingle) {
        callbackInfoReturnable.cancel();
        callbackInfoReturnable.setReturnValue(scaleTransferRate((Integer) callbackInfoReturnable.getReturnValue(), respectSingle));
    }

    public static int scaleTransferRate(int originalRate, boolean respectSingle) {
        return originalRate == 1 && respectSingle ? 1 : Math.max(1, originalRate * StackSizeRules.getMaxStackSize() / 64);
    }

    public static int increaseTransferRate(int originalRate) {
        return originalRate == 1 ? 1 : Math.max(1, StackSizeRules.getMaxStackSize());
    }
}