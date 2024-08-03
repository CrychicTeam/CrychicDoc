package com.rekindled.embers.api.filter;

import com.rekindled.embers.util.FilterUtil;
import java.util.List;
import java.util.Objects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class FilterSieve implements IFilter {

    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation("embers", "sieve");

    private ItemStack stack1;

    private ItemStack stack2;

    private int offset;

    private EnumFilterSetting setting;

    private boolean inverted;

    private IFilterComparator comparator;

    public FilterSieve(CompoundTag tag) {
        this.readFromNBT(tag);
    }

    public FilterSieve(ItemStack stack1, ItemStack stack2, int offset, EnumFilterSetting setting, boolean inverted) {
        this.stack1 = stack1;
        this.stack2 = stack2;
        this.offset = offset;
        this.setting = setting;
        this.inverted = inverted;
        this.findComparator();
    }

    private void findComparator() {
        if (this.stack1.isEmpty() && this.stack2.isEmpty()) {
            this.comparator = FilterUtil.ANY;
        } else {
            List<IFilterComparator> comparators = FilterUtil.getComparators(this.stack1, this.stack2);
            this.comparator = (IFilterComparator) comparators.get(this.offset % comparators.size());
        }
    }

    @Override
    public ResourceLocation getType() {
        return RESOURCE_LOCATION;
    }

    @Override
    public boolean acceptsItem(ItemStack stack) {
        return this.comparator.isBetween(this.stack1, this.stack2, stack, this.setting) != this.inverted;
    }

    @Override
    public String formatFilter() {
        return this.comparator == null ? "INVALID COMPARATOR" : this.comparator.format(this.stack1, this.stack2, this.setting, this.inverted);
    }

    @Override
    public CompoundTag writeToNBT(CompoundTag tag) {
        tag.putString("type", this.getType().toString());
        tag.put("stack1", this.stack1.serializeNBT());
        tag.put("stack2", this.stack2.serializeNBT());
        tag.putInt("offset", this.offset);
        tag.putInt("setting", this.setting.ordinal());
        tag.putBoolean("inverted", this.inverted);
        tag.putString("comparator", this.comparator.getName());
        return tag;
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        this.stack1 = ItemStack.of(tag.getCompound("stack1"));
        this.stack2 = ItemStack.of(tag.getCompound("stack2"));
        this.offset = tag.getInt("offset");
        this.setting = EnumFilterSetting.values()[tag.getInt("setting")];
        this.inverted = tag.getBoolean("invert");
        if (tag.contains("comparator")) {
            this.comparator = FilterUtil.getComparator(tag.getString("comparator"));
        } else {
            this.findComparator();
        }
    }

    public boolean equals(Object obj) {
        return obj instanceof FilterSieve ? this.equals((FilterSieve) obj) : super.equals(obj);
    }

    private boolean equals(FilterSieve other) {
        return Objects.equals(this.comparator, other.comparator) && Objects.equals(this.inverted, other.inverted) && Objects.equals(this.setting, other.setting) && ItemStack.isSameItemSameTags(this.stack1, other.stack1) && ItemStack.isSameItemSameTags(this.stack2, other.stack2);
    }
}