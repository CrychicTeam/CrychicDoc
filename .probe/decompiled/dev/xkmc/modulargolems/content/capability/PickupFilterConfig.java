package dev.xkmc.modulargolems.content.capability;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.ArrayList;
import net.minecraft.world.item.ItemStack;

@SerialClass
public class PickupFilterConfig {

    public static final int SIZE = 27;

    @SerialField
    protected final ArrayList<ItemStack> filter = new ArrayList();

    @SerialField
    protected boolean blacklist = true;

    @SerialField
    protected boolean matchNBT = false;

    public boolean internalMatch(ItemStack stack) {
        for (ItemStack filter : this.filter) {
            if (stack.getItem() == filter.getItem()) {
                if (!this.matchNBT) {
                    return true;
                }
                if (ItemStack.isSameItemSameTags(stack, filter)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean allowPickup(ItemStack stack) {
        return this.internalMatch(stack) != this.blacklist;
    }
}