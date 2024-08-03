package dev.xkmc.l2library.util.nbt;

import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

public class ItemListTag {

    private final ItemStack stack;

    private final Supplier<ListTag> creator;

    private final Consumer<ListTag> replacer;

    @Nullable
    private ListTag tag;

    ItemListTag(ItemStack root, Supplier<ListTag> creator, Consumer<ListTag> setter, @Nullable ListTag tag) {
        this.stack = root;
        this.creator = creator;
        this.replacer = setter;
        this.tag = tag;
    }

    public ListTag getOrCreate() {
        if (this.tag == null) {
            this.tag = (ListTag) this.creator.get();
        }
        return this.tag;
    }

    public boolean isPresent() {
        return this.tag != null;
    }

    public void setTag(ListTag tag) {
        this.tag = tag;
        this.replacer.accept(tag);
    }

    public ItemCompoundTag addCompound() {
        ListTag list = this.getOrCreate();
        CompoundTag newTag = new CompoundTag();
        int index = list.size();
        list.add(newTag);
        return new ItemCompoundTag(this.stack, () -> list.getCompound(index), nextTag -> list.setTag(index, nextTag), newTag);
    }

    public void clear() {
        this.getOrCreate().clear();
    }
}