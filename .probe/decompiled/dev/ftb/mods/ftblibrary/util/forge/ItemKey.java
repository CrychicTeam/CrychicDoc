package dev.ftb.mods.ftblibrary.util.forge;

import java.util.Objects;
import net.minecraft.world.item.ItemStack;

public final class ItemKey {

    public final ItemStack stack;

    public ItemKey(ItemStack s) {
        this.stack = s;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            ItemKey itemKey = (ItemKey) o;
            return this.stack.getItem() == itemKey.stack.getItem() && this.stack.areShareTagsEqual(itemKey.stack);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.stack.getItem(), this.stack.getTag() });
    }
}