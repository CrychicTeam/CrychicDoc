package dev.xkmc.l2library.util.nbt;

import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

public class ItemCompoundTag {

    private final ItemStack stack;

    private final Supplier<CompoundTag> creator;

    private final Consumer<CompoundTag> replacer;

    @Nullable
    private CompoundTag tag;

    public static ItemCompoundTag of(ItemStack stack) {
        return new ItemCompoundTag(stack, stack::m_41784_, stack::m_41751_, stack.getTag());
    }

    ItemCompoundTag(ItemStack root, Supplier<CompoundTag> creator, Consumer<CompoundTag> replacer, @Nullable CompoundTag tag) {
        this.stack = root;
        this.creator = creator;
        this.replacer = replacer;
        this.tag = tag;
    }

    public ItemStack getHolderStack() {
        return this.stack;
    }

    public boolean isPresent() {
        return this.tag != null;
    }

    public CompoundTag getOrCreate() {
        if (this.tag == null) {
            this.tag = (CompoundTag) this.creator.get();
        }
        return this.tag;
    }

    public void setTag(CompoundTag tag) {
        this.tag = tag;
        this.replacer.accept(tag);
    }

    public ItemCompoundTag getSubTag(String key) {
        CompoundTag sub = null;
        if (this.tag != null && this.tag.contains(key, 10)) {
            sub = this.tag.getCompound(key);
        }
        return new ItemCompoundTag(this.stack, () -> {
            CompoundTag self = this.getOrCreate();
            CompoundTag next = self.getCompound(key);
            if (!self.contains(key)) {
                self.put(key, next);
            }
            return next;
        }, newTag -> {
            CompoundTag self = this.getOrCreate();
            self.put(key, newTag);
        }, sub);
    }

    public ItemListTag getSubList(String key, int type) {
        ListTag sub = null;
        if (this.tag != null && this.tag.contains(key, 9)) {
            sub = this.tag.getList(key, type);
        }
        return new ItemListTag(this.stack, () -> {
            CompoundTag self = this.getOrCreate();
            ListTag next = self.getList(key, type);
            if (!self.contains(key)) {
                self.put(key, next);
            }
            return next;
        }, newTag -> {
            CompoundTag self = this.getOrCreate();
            self.put(key, newTag);
        }, sub);
    }
}