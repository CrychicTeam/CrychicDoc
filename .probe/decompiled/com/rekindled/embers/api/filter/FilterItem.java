package com.rekindled.embers.api.filter;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class FilterItem implements IFilter {

    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation("embers", "item");

    private ItemStack filterItem = ItemStack.EMPTY;

    public FilterItem(ItemStack filterItem) {
        this.filterItem = filterItem;
    }

    public FilterItem(CompoundTag tag) {
        this.readFromNBT(tag);
    }

    @Override
    public ResourceLocation getType() {
        return RESOURCE_LOCATION;
    }

    @Override
    public boolean acceptsItem(ItemStack stack) {
        return this.filterItem.getItem() == stack.getItem() && this.filterItem.getDamageValue() == stack.getDamageValue();
    }

    @Override
    public String formatFilter() {
        return I18n.get("embers.filter.strict", this.filterItem.getDisplayName());
    }

    @Override
    public CompoundTag writeToNBT(CompoundTag tag) {
        tag.putString("type", this.getType().toString());
        tag.put("filterStack", this.filterItem.serializeNBT());
        return tag;
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        this.filterItem = ItemStack.of(tag.getCompound("filterStack"));
    }

    public boolean equals(Object obj) {
        return obj instanceof FilterItem ? this.equals((FilterItem) obj) : super.equals(obj);
    }

    private boolean equals(FilterItem other) {
        return ItemStack.isSameItemSameTags(this.filterItem, other.filterItem);
    }
}