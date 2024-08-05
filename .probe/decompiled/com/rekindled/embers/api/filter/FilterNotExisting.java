package com.rekindled.embers.api.filter;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class FilterNotExisting extends FilterExisting {

    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation("embers", "not_existing");

    @Override
    public ResourceLocation getType() {
        return RESOURCE_LOCATION;
    }

    @Override
    public boolean acceptsItem(ItemStack stack, IItemHandler itemHandler) {
        return !super.acceptsItem(stack, itemHandler);
    }

    @Override
    public String formatFilter() {
        return I18n.get("embers.filter.not_existing");
    }
}