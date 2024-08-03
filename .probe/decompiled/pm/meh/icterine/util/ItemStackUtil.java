package pm.meh.icterine.util;

import java.util.function.Supplier;
import net.minecraft.world.item.ItemStack;
import pm.meh.icterine.iface.IItemStackMixin;

public class ItemStackUtil {

    public static void processItemStackInTriggerSlotListeners(ItemStack oldStack, ItemStack newStack) {
        LogHelper.debug((Supplier<String>) (() -> "Stack changed from %s to %s".formatted(oldStack, newStack)));
        if (ItemStack.isSameItem(newStack, oldStack)) {
            ((IItemStackMixin) newStack).icterine$setPreviousStackSize(oldStack.getCount());
        }
    }
}