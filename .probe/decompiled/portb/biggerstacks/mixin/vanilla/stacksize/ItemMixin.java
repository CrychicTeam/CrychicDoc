package portb.biggerstacks.mixin.vanilla.stacksize;

import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.util.ItemStackSizeHelper;

@Mixin({ Item.class })
public class ItemMixin {

    @Inject(method = { "getMaxStackSize" }, at = { @At("RETURN") }, cancellable = true)
    private void increaseStackLimit(CallbackInfoReturnable<Integer> returnInfo) {
        ItemStackSizeHelper.applyStackSizeToItem(((Item) this).getDefaultInstance(), returnInfo);
    }
}