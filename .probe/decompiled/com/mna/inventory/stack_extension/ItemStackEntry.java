package com.mna.inventory.stack_extension;

import com.mna.ManaAndArtifice;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public class ItemStackEntry {

    public static Codec<ItemStackEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(ItemStack.CODEC.fieldOf("item").forGetter(o -> o.stack), Codec.LONG.fieldOf("count").forGetter(o -> o.count), SlotInfoTable.CODEC.optionalFieldOf("info").forGetter(o -> o.info != null && !o.info.isEmpty() ? Optional.of(o.info) : Optional.empty())).apply(instance, (a, b, c) -> new ItemStackEntry(a, b, (SlotInfoTable) c.orElse(new SlotInfoTable()))));

    public static ItemStackEntry EMPTY = new ItemStackEntry(ItemStack.EMPTY);

    private ItemStack stack;

    private long count;

    private SlotInfoTable info;

    public ItemStackEntry(ItemStack stack) {
        this.stack = stack;
        this.count = (long) stack.getCount();
        this.info = null;
    }

    private ItemStackEntry(ItemStack stack, long count, SlotInfoTable info) {
        this.stack = stack;
        this.count = count;
        this.info = info;
    }

    public ItemStack getStackOriginal() {
        return this.stack;
    }

    public ItemStack getStackCopy() {
        ItemStack copy = this.stack.copy();
        copy.setCount((int) this.count);
        return copy;
    }

    public ItemStack extract(int count) {
        if ((long) count > this.count) {
            count = (int) this.count;
        }
        if (this.stack.getCount() == count) {
            this.count = 0L;
            ItemStack result = this.stack;
            this.stack = ItemStack.EMPTY;
            return result;
        } else {
            ItemStack copy = this.getStackCopy();
            copy.setCount(count);
            this.count -= (long) count;
            return copy;
        }
    }

    public long getCount() {
        return this.count;
    }

    public void setCount(long count) {
        this.count = count;
        this.stack.setCount((int) this.count);
    }

    public void grow(long count) {
        this.count += count;
        this.stack.setCount((int) this.count);
    }

    public SlotInfoTable getInfo() {
        return this.info;
    }

    public boolean isEmpty() {
        return this.stack.isEmpty() ? true : this.count == 0L && this.info.isEmpty();
    }

    public Tag serialize() {
        return (Tag) ((DataResult) NbtOps.INSTANCE.withEncoder(CODEC).apply(this)).getOrThrow(false, ManaAndArtifice.LOGGER::error);
    }

    public static ItemStackEntry deserialize(Tag tag) {
        return tag == null ? EMPTY : (ItemStackEntry) CODEC.parse(NbtOps.INSTANCE, tag).getOrThrow(false, ManaAndArtifice.LOGGER::error);
    }
}