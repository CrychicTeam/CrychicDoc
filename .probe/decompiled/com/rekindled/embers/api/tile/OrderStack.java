package com.rekindled.embers.api.tile;

import com.rekindled.embers.api.filter.IFilter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;

public class OrderStack {

    private BlockPos pos;

    private IFilter filter;

    private int size;

    public OrderStack(BlockPos pos, IFilter filter, int size) {
        this.pos = pos;
        this.filter = filter;
        this.size = size;
    }

    public OrderStack(CompoundTag tag) {
        this.readFromNBT(tag);
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public IOrderSource getSource(Level world) {
        BlockEntity tile = world.getBlockEntity(this.pos);
        return tile instanceof IOrderSource ? (IOrderSource) tile : null;
    }

    public IFilter getFilter() {
        return this.filter;
    }

    public int getSize() {
        return this.size;
    }

    public boolean acceptsItem(Level world, ItemStack stack) {
        IOrderSource source = this.getSource(world);
        if (source != null) {
            IItemHandler itemHandler = source.getItemHandler();
            if (itemHandler != null) {
                return this.filter.acceptsItem(stack, itemHandler);
            }
        }
        return false;
    }

    public void deplete(int n) {
        this.size -= n;
    }

    public void increment(int n) {
        this.size += n;
    }

    public CompoundTag writeToNBT(CompoundTag tag) {
        tag.putInt("x", this.pos.m_123341_());
        tag.putInt("y", this.pos.m_123342_());
        tag.putInt("z", this.pos.m_123343_());
        tag.put("filter", this.filter.writeToNBT(new CompoundTag()));
        tag.putInt("size", this.size);
        return tag;
    }

    public void readFromNBT(CompoundTag tag) {
        this.pos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
        this.size = tag.getInt("size");
    }

    public void reset(IFilter filter, int size) {
        this.filter = filter;
        this.size = size;
    }
}