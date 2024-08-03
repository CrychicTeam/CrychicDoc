package dev.latvian.mods.kubejs.recipe;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

public interface ItemMatch extends ReplacementMatch {

    boolean contains(ItemStack var1);

    boolean contains(Ingredient var1);

    @Deprecated(forRemoval = true)
    default boolean contains(Block block) {
        Item item = block.asItem();
        return item != Items.AIR && this.contains(item.getDefaultInstance());
    }

    default boolean contains(ItemLike itemLike) {
        Item item = itemLike.asItem();
        return item != Items.AIR && this.contains(item.getDefaultInstance());
    }

    default boolean containsAny(ItemLike... itemLikes) {
        for (ItemLike item : itemLikes) {
            if (this.contains(item)) {
                return true;
            }
        }
        return false;
    }

    default boolean containsAny(Iterable<ItemLike> itemLikes) {
        for (ItemLike item : itemLikes) {
            if (this.contains(item)) {
                return true;
            }
        }
        return false;
    }
}