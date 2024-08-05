package com.illusivesoulworks.polymorph.common.impl;

import com.illusivesoulworks.polymorph.api.common.base.IRecipePair;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record RecipePair(ResourceLocation resourceLocation, ItemStack output) implements IRecipePair {

    @Override
    public ItemStack getOutput() {
        return this.output;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return this.resourceLocation;
    }

    public int compareTo(@Nonnull IRecipePair other) {
        ItemStack output1 = this.getOutput();
        ItemStack output2 = other.getOutput();
        int compare = output1.getDescriptionId().compareTo(output2.getDescriptionId());
        if (compare == 0) {
            int diff = output1.getCount() - output2.getCount();
            if (diff == 0) {
                String tag1 = output1.getTag() != null ? output1.getTag().m_7916_() : "";
                String tag2 = output2.getTag() != null ? output2.getTag().m_7916_() : "";
                return tag1.compareTo(tag2);
            } else {
                return diff;
            }
        } else if (this.getResourceLocation().getNamespace().equals("minecraft") && !other.getResourceLocation().getNamespace().equals("minecraft")) {
            return 1;
        } else {
            return !this.getResourceLocation().getNamespace().equals("minecraft") && other.getResourceLocation().getNamespace().equals("minecraft") ? -1 : compare;
        }
    }
}