package net.minecraft.world.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.world.item.ItemStack;

public class ItemCombinerMenuSlotDefinition {

    private final List<ItemCombinerMenuSlotDefinition.SlotDefinition> slots;

    private final ItemCombinerMenuSlotDefinition.SlotDefinition resultSlot;

    ItemCombinerMenuSlotDefinition(List<ItemCombinerMenuSlotDefinition.SlotDefinition> listItemCombinerMenuSlotDefinitionSlotDefinition0, ItemCombinerMenuSlotDefinition.SlotDefinition itemCombinerMenuSlotDefinitionSlotDefinition1) {
        if (!listItemCombinerMenuSlotDefinitionSlotDefinition0.isEmpty() && !itemCombinerMenuSlotDefinitionSlotDefinition1.equals(ItemCombinerMenuSlotDefinition.SlotDefinition.EMPTY)) {
            this.slots = listItemCombinerMenuSlotDefinitionSlotDefinition0;
            this.resultSlot = itemCombinerMenuSlotDefinitionSlotDefinition1;
        } else {
            throw new IllegalArgumentException("Need to define both inputSlots and resultSlot");
        }
    }

    public static ItemCombinerMenuSlotDefinition.Builder create() {
        return new ItemCombinerMenuSlotDefinition.Builder();
    }

    public boolean hasSlot(int int0) {
        return this.slots.size() >= int0;
    }

    public ItemCombinerMenuSlotDefinition.SlotDefinition getSlot(int int0) {
        return (ItemCombinerMenuSlotDefinition.SlotDefinition) this.slots.get(int0);
    }

    public ItemCombinerMenuSlotDefinition.SlotDefinition getResultSlot() {
        return this.resultSlot;
    }

    public List<ItemCombinerMenuSlotDefinition.SlotDefinition> getSlots() {
        return this.slots;
    }

    public int getNumOfInputSlots() {
        return this.slots.size();
    }

    public int getResultSlotIndex() {
        return this.getNumOfInputSlots();
    }

    public List<Integer> getInputSlotIndexes() {
        return (List<Integer>) this.slots.stream().map(ItemCombinerMenuSlotDefinition.SlotDefinition::f_266086_).collect(Collectors.toList());
    }

    public static class Builder {

        private final List<ItemCombinerMenuSlotDefinition.SlotDefinition> slots = new ArrayList();

        private ItemCombinerMenuSlotDefinition.SlotDefinition resultSlot = ItemCombinerMenuSlotDefinition.SlotDefinition.EMPTY;

        public ItemCombinerMenuSlotDefinition.Builder withSlot(int int0, int int1, int int2, Predicate<ItemStack> predicateItemStack3) {
            this.slots.add(new ItemCombinerMenuSlotDefinition.SlotDefinition(int0, int1, int2, predicateItemStack3));
            return this;
        }

        public ItemCombinerMenuSlotDefinition.Builder withResultSlot(int int0, int int1, int int2) {
            this.resultSlot = new ItemCombinerMenuSlotDefinition.SlotDefinition(int0, int1, int2, p_266825_ -> false);
            return this;
        }

        public ItemCombinerMenuSlotDefinition build() {
            return new ItemCombinerMenuSlotDefinition(this.slots, this.resultSlot);
        }
    }

    public static record SlotDefinition(int f_266086_, int f_266065_, int f_265926_, Predicate<ItemStack> f_265897_) {

        private final int slotIndex;

        private final int x;

        private final int y;

        private final Predicate<ItemStack> mayPlace;

        static final ItemCombinerMenuSlotDefinition.SlotDefinition EMPTY = new ItemCombinerMenuSlotDefinition.SlotDefinition(0, 0, 0, p_267109_ -> true);

        public SlotDefinition(int f_266086_, int f_266065_, int f_265926_, Predicate<ItemStack> f_265897_) {
            this.slotIndex = f_266086_;
            this.x = f_266065_;
            this.y = f_265926_;
            this.mayPlace = f_265897_;
        }
    }
}