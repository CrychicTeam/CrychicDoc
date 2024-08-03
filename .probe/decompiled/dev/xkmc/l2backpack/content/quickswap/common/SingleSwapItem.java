package dev.xkmc.l2backpack.content.quickswap.common;

import dev.xkmc.l2backpack.content.common.BaseBagItem;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class SingleSwapItem extends BaseBagItem implements IQuickSwapItem {

    public static void setSelected(ItemStack stack, int i) {
        int slot = i;
        if (i < 0) {
            slot = getSelected(stack);
            if (i == -1) {
                slot--;
            } else {
                slot++;
            }
            slot = (slot + 9) % 9;
        }
        stack.getOrCreateTag().putInt("selected", slot);
    }

    public static int getSelected(ItemStack stack) {
        return Mth.clamp(stack.getOrCreateTag().getInt("selected"), 0, 8);
    }

    public SingleSwapItem(Item.Properties props) {
        super(props.stacksTo(1).fireResistant());
    }
}