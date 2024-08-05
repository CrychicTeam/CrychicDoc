package com.mna.api.entities.construct.ai.parameter;

import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class ConstructTaskItemStackParameter extends ConstructAITaskParameter {

    private ItemStack stack = ItemStack.EMPTY;

    public ConstructTaskItemStackParameter(String id) {
        super(id, ConstructParameterTypes.ITEMSTACK);
    }

    @Nullable
    public ItemStack getStack() {
        return this.stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack.copy();
    }

    @Override
    public void loadData(CompoundTag nbt) {
        super.loadData(nbt);
        this.stack = ItemStack.of(nbt);
    }

    @Override
    public CompoundTag saveData() {
        CompoundTag tag = super.saveData();
        this.stack.save(tag);
        return tag;
    }
}