package portb.biggerstacks.mixin.vanilla.stacksize;

import net.minecraft.world.item.BundleItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin({ BundleItem.class })
public class BundleItemMixin {

    @ModifyConstant(method = { "getFullnessDisplay" }, constant = { @Constant(floatValue = 64.0F) })
    private static float increaseFloatStackLimit(float value) {
        return (float) StackSizeHelper.getNewStackSize();
    }

    @ModifyConstant(method = { "overrideStackedOnOther", "getBarWidth", "getWeight", "appendHoverText", "add" }, constant = { @Constant(intValue = 64) })
    private static int increaseStackLimit(int value) {
        return StackSizeHelper.getNewStackSize();
    }
}