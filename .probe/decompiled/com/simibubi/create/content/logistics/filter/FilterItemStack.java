package com.simibubi.create.content.logistics.filter;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import com.simibubi.create.foundation.utility.Pair;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;

public class FilterItemStack {

    private ItemStack filterItemStack;

    private boolean fluidExtracted;

    private FluidStack filterFluidStack;

    public static FilterItemStack of(ItemStack filter) {
        if (filter.hasTag()) {
            if (AllItems.FILTER.isIn(filter)) {
                return new FilterItemStack.ListFilterItemStack(filter);
            }
            if (AllItems.ATTRIBUTE_FILTER.isIn(filter)) {
                return new FilterItemStack.AttributeFilterItemStack(filter);
            }
        }
        return new FilterItemStack(filter);
    }

    public static FilterItemStack of(CompoundTag tag) {
        return of(ItemStack.of(tag));
    }

    public static FilterItemStack empty() {
        return of(ItemStack.EMPTY);
    }

    public boolean isEmpty() {
        return this.filterItemStack.isEmpty();
    }

    public CompoundTag serializeNBT() {
        return this.filterItemStack.serializeNBT();
    }

    public ItemStack item() {
        return this.filterItemStack;
    }

    public FluidStack fluid(Level level) {
        this.resolveFluid(level);
        return this.filterFluidStack;
    }

    public boolean isFilterItem() {
        return this.filterItemStack.getItem() instanceof FilterItem;
    }

    public boolean test(Level world, ItemStack stack) {
        return this.test(world, stack, false);
    }

    public boolean test(Level world, FluidStack stack) {
        return this.test(world, stack, true);
    }

    public boolean test(Level world, ItemStack stack, boolean matchNBT) {
        return this.isEmpty() ? true : FilterItem.testDirect(this.filterItemStack, stack, matchNBT);
    }

    public boolean test(Level world, FluidStack stack, boolean matchNBT) {
        if (this.isEmpty()) {
            return true;
        } else if (stack.isEmpty()) {
            return false;
        } else {
            this.resolveFluid(world);
            if (this.filterFluidStack.isEmpty()) {
                return false;
            } else {
                return !matchNBT ? this.filterFluidStack.getFluid().isSame(stack.getFluid()) : this.filterFluidStack.isFluidEqual(stack);
            }
        }
    }

    private void resolveFluid(Level world) {
        if (!this.fluidExtracted) {
            this.fluidExtracted = true;
            if (GenericItemEmptying.canItemBeEmptied(world, this.filterItemStack)) {
                this.filterFluidStack = GenericItemEmptying.emptyItem(world, this.filterItemStack, true).getFirst();
            }
        }
    }

    protected FilterItemStack(ItemStack filter) {
        this.filterItemStack = filter;
        this.filterFluidStack = FluidStack.EMPTY;
        this.fluidExtracted = false;
    }

    public static class AttributeFilterItemStack extends FilterItemStack {

        public FilterItemStack.AttributeFilterItemStack.WhitelistMode whitelistMode;

        public List<Pair<ItemAttribute, Boolean>> attributeTests;

        protected AttributeFilterItemStack(ItemStack filter) {
            super(filter);
            boolean defaults = !filter.hasTag();
            this.attributeTests = new ArrayList();
            this.whitelistMode = FilterItemStack.AttributeFilterItemStack.WhitelistMode.values()[defaults ? 0 : filter.getTag().getInt("WhitelistMode")];
            for (Tag inbt : defaults ? new ListTag() : filter.getTag().getList("MatchedAttributes", 10)) {
                CompoundTag compound = (CompoundTag) inbt;
                ItemAttribute attribute = ItemAttribute.fromNBT(compound);
                if (attribute != null) {
                    this.attributeTests.add(Pair.of(attribute, compound.getBoolean("Inverted")));
                }
            }
        }

        @Override
        public boolean test(Level world, FluidStack stack, boolean matchNBT) {
            return false;
        }

        @Override
        public boolean test(Level world, ItemStack stack, boolean matchNBT) {
            if (this.attributeTests.isEmpty()) {
                return super.test(world, stack, matchNBT);
            } else {
                for (Pair<ItemAttribute, Boolean> test : this.attributeTests) {
                    ItemAttribute attribute = test.getFirst();
                    boolean inverted = test.getSecond();
                    boolean matches = attribute.appliesTo(stack, world) != inverted;
                    if (matches) {
                        switch(this.whitelistMode) {
                            case BLACKLIST:
                                return false;
                            case WHITELIST_CONJ:
                            default:
                                break;
                            case WHITELIST_DISJ:
                                return true;
                        }
                    } else {
                        switch(this.whitelistMode) {
                            case BLACKLIST:
                            case WHITELIST_DISJ:
                            default:
                                break;
                            case WHITELIST_CONJ:
                                return false;
                        }
                    }
                }
                switch(this.whitelistMode) {
                    case BLACKLIST:
                        return true;
                    case WHITELIST_CONJ:
                        return true;
                    case WHITELIST_DISJ:
                        return false;
                    default:
                        return false;
                }
            }
        }

        public static enum WhitelistMode {

            WHITELIST_DISJ, WHITELIST_CONJ, BLACKLIST
        }
    }

    public static class ListFilterItemStack extends FilterItemStack {

        public List<FilterItemStack> containedItems;

        public boolean shouldRespectNBT;

        public boolean isBlacklist;

        protected ListFilterItemStack(ItemStack filter) {
            super(filter);
            boolean defaults = !filter.hasTag();
            this.containedItems = new ArrayList();
            ItemStackHandler items = FilterItem.getFilterItems(filter);
            for (int i = 0; i < items.getSlots(); i++) {
                ItemStack stackInSlot = items.getStackInSlot(i);
                if (!stackInSlot.isEmpty()) {
                    this.containedItems.add(FilterItemStack.of(stackInSlot));
                }
            }
            this.shouldRespectNBT = !defaults ? false : filter.getTag().getBoolean("RespectNBT");
            this.isBlacklist = defaults ? false : filter.getTag().getBoolean("Blacklist");
        }

        @Override
        public boolean test(Level world, ItemStack stack, boolean matchNBT) {
            if (this.containedItems.isEmpty()) {
                return super.test(world, stack, matchNBT);
            } else {
                for (FilterItemStack filterItemStack : this.containedItems) {
                    if (filterItemStack.test(world, stack, this.shouldRespectNBT)) {
                        return !this.isBlacklist;
                    }
                }
                return this.isBlacklist;
            }
        }

        @Override
        public boolean test(Level world, FluidStack stack, boolean matchNBT) {
            for (FilterItemStack filterItemStack : this.containedItems) {
                if (filterItemStack.test(world, stack, this.shouldRespectNBT)) {
                    return !this.isBlacklist;
                }
            }
            return this.isBlacklist;
        }
    }
}