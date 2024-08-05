package pm.meh.icterine.impl;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Supplier;
import net.minecraft.world.item.ItemStack;
import pm.meh.icterine.iface.IItemStackMixin;
import pm.meh.icterine.util.LogHelper;

public class StackSizeThresholdManager {

    private static final SortedSet<Integer> stackSizeThresholds = new TreeSet();

    public static void clear() {
        stackSizeThresholds.clear();
        stackSizeThresholds.add(1);
    }

    public static void add(int value) {
        stackSizeThresholds.add(value);
    }

    public static boolean doesStackPassThreshold(ItemStack stack) {
        int prevValue = ((IItemStackMixin) stack).icterine$getPreviousStackSize();
        int newValue = stack.getCount();
        for (int thr : stackSizeThresholds) {
            if (prevValue < thr && newValue >= thr) {
                return true;
            }
        }
        return false;
    }

    public static void debugPrint() {
        LogHelper.debug((Supplier<String>) (() -> "InventoryChangeTrigger stack size thresholds: %s".formatted(stackSizeThresholds)));
    }
}