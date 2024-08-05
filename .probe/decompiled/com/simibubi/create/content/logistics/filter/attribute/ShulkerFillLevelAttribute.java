package com.simibubi.create.content.logistics.filter.attribute;

import com.simibubi.create.content.logistics.filter.ItemAttribute;
import com.simibubi.create.foundation.utility.Lang;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;

public class ShulkerFillLevelAttribute implements ItemAttribute {

    public static final ShulkerFillLevelAttribute EMPTY = new ShulkerFillLevelAttribute(null);

    private final ShulkerFillLevelAttribute.ShulkerLevels levels;

    public ShulkerFillLevelAttribute(ShulkerFillLevelAttribute.ShulkerLevels levels) {
        this.levels = levels;
    }

    @Override
    public boolean appliesTo(ItemStack stack) {
        return this.levels != null && this.levels.canApply(stack);
    }

    @Override
    public List<ItemAttribute> listAttributesOf(ItemStack stack) {
        return (List<ItemAttribute>) Arrays.stream(ShulkerFillLevelAttribute.ShulkerLevels.values()).filter(shulkerLevels -> shulkerLevels.canApply(stack)).map(ShulkerFillLevelAttribute::new).collect(Collectors.toList());
    }

    @Override
    public String getTranslationKey() {
        return "shulker_level";
    }

    @Override
    public Object[] getTranslationParameters() {
        String parameter = "";
        if (this.levels != null) {
            parameter = Lang.translateDirect("item_attributes." + this.getTranslationKey() + "." + this.levels.key).getString();
        }
        return new Object[] { parameter };
    }

    @Override
    public void writeNBT(CompoundTag nbt) {
        if (this.levels != null) {
            nbt.putString("id", this.levels.key);
        }
    }

    @Override
    public ItemAttribute readNBT(CompoundTag nbt) {
        return nbt.contains("id") ? new ShulkerFillLevelAttribute(ShulkerFillLevelAttribute.ShulkerLevels.fromKey(nbt.getString("id"))) : EMPTY;
    }

    static enum ShulkerLevels {

        EMPTY("empty", amount -> amount == 0), PARTIAL("partial", amount -> amount > 0 && amount < Integer.MAX_VALUE), FULL("full", amount -> amount == Integer.MAX_VALUE);

        private final Predicate<Integer> requiredSize;

        private final String key;

        private ShulkerLevels(String key, Predicate<Integer> requiredSize) {
            this.key = key;
            this.requiredSize = requiredSize;
        }

        @Nullable
        public static ShulkerFillLevelAttribute.ShulkerLevels fromKey(String key) {
            return (ShulkerFillLevelAttribute.ShulkerLevels) Arrays.stream(values()).filter(shulkerLevels -> shulkerLevels.key.equals(key)).findFirst().orElse(null);
        }

        private static boolean isShulker(ItemStack stack) {
            return Block.byItem(stack.getItem()) instanceof ShulkerBoxBlock;
        }

        public boolean canApply(ItemStack testStack) {
            if (!isShulker(testStack)) {
                return false;
            } else {
                CompoundTag compoundnbt = testStack.getTagElement("BlockEntityTag");
                if (compoundnbt == null) {
                    return this.requiredSize.test(0);
                } else if (compoundnbt.contains("LootTable", 8)) {
                    return false;
                } else if (compoundnbt.contains("Items", 9)) {
                    int rawSize = compoundnbt.getList("Items", 10).size();
                    if (rawSize < 27) {
                        return this.requiredSize.test(rawSize);
                    } else {
                        NonNullList<ItemStack> inventory = NonNullList.withSize(27, ItemStack.EMPTY);
                        ContainerHelper.loadAllItems(compoundnbt, inventory);
                        boolean isFull = inventory.stream().allMatch(itemStack -> !itemStack.isEmpty() && itemStack.getCount() == itemStack.getMaxStackSize());
                        return this.requiredSize.test(isFull ? Integer.MAX_VALUE : rawSize);
                    }
                } else {
                    return this.requiredSize.test(0);
                }
            }
        }
    }
}