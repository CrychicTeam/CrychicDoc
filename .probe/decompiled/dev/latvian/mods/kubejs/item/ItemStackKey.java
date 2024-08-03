package dev.latvian.mods.kubejs.item;

import java.util.Objects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class ItemStackKey {

    public static ItemStackKey EMPTY = new ItemStackKey(Items.AIR, null);

    private final Item item;

    private final CompoundTag tag;

    private int hashCode = 0;

    public static ItemStackKey of(ItemStack stack) {
        if (stack.isEmpty()) {
            return EMPTY;
        } else {
            return stack.getTag() == null ? stack.getItem().kjs$getTypeItemStackKey() : new ItemStackKey(stack);
        }
    }

    public ItemStackKey(Item item, @Nullable CompoundTag tag) {
        this.item = item;
        this.tag = tag;
    }

    private ItemStackKey(ItemStack is) {
        this(is.getItem(), is.getTag());
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = this.item == Items.AIR ? 0 : (this.tag == null ? this.item.hashCode() : this.item.hashCode() * 31 + this.tag.hashCode());
            if (this.hashCode == 0) {
                this.hashCode = 1;
            }
        }
        return this.hashCode;
    }

    public boolean equals(Object obj) {
        return !(obj instanceof ItemStackKey k) ? false : this.item == k.item && this.hashCode() == k.hashCode() && Objects.equals(this.tag, k.tag);
    }
}