package com.simibubi.create.content.processing.sequenced;

import com.simibubi.create.foundation.utility.Color;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SequencedAssemblyItem extends Item {

    public SequencedAssemblyItem(Item.Properties p_i48487_1_) {
        super(p_i48487_1_.stacksTo(1));
    }

    public float getProgress(ItemStack stack) {
        if (!stack.hasTag()) {
            return 0.0F;
        } else {
            CompoundTag tag = stack.getTag();
            return !tag.contains("SequencedAssembly") ? 0.0F : tag.getCompound("SequencedAssembly").getFloat("Progress");
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(this.getProgress(stack) * 13.0F);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Color.mixColors(-16268, -12124192, this.getProgress(stack));
    }
}