package dev.latvian.mods.kubejs.core;

import com.google.gson.JsonElement;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ItemStackSet;
import dev.latvian.mods.kubejs.platform.IngredientPlatformHelper;
import dev.latvian.mods.rhino.mod.util.JsonSerializable;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import java.util.LinkedHashSet;
import java.util.Set;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

@RemapPrefixForJS("kjs$")
public interface IngredientKJS extends IngredientSupplierKJS, JsonSerializable {

    default Ingredient kjs$self() {
        throw new NoMixinException();
    }

    default boolean kjs$testItem(Item item) {
        return this.kjs$self().test(item.getDefaultInstance());
    }

    default ItemStackSet kjs$getStacks() {
        return new ItemStackSet(this.kjs$self().getItems());
    }

    default ItemStackSet kjs$getDisplayStacks() {
        ItemStackSet set = new ItemStackSet();
        for (ItemStack stack : ItemStackJS.getList()) {
            if (this.kjs$self().test(stack)) {
                set.add(stack);
            }
        }
        return set;
    }

    default Set<Item> kjs$getItemTypes() {
        ItemStack[] items = this.kjs$self().getItems();
        if (items.length == 1 && !items[0].isEmpty()) {
            return Set.of(items[0].getItem());
        } else {
            LinkedHashSet<Item> set = new LinkedHashSet(items.length);
            for (ItemStack stack : items) {
                if (!stack.isEmpty()) {
                    set.add(stack.getItem());
                }
            }
            return set;
        }
    }

    default Set<String> kjs$getItemIds() {
        ItemStack[] items = this.kjs$self().getItems();
        if (items.length == 1 && !items[0].isEmpty()) {
            return Set.of(items[0].kjs$getId());
        } else {
            LinkedHashSet<String> ids = new LinkedHashSet(items.length);
            for (ItemStack item : items) {
                if (!item.isEmpty()) {
                    ids.add(item.kjs$getId());
                }
            }
            return ids;
        }
    }

    default ItemStack kjs$getFirst() {
        for (ItemStack stack : this.kjs$self().getItems()) {
            if (!stack.isEmpty()) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    default Ingredient kjs$and(Ingredient ingredient) {
        return ingredient == Ingredient.EMPTY ? this.kjs$self() : (this == Ingredient.EMPTY ? ingredient : IngredientPlatformHelper.get().and(new Ingredient[] { this.kjs$self(), ingredient }));
    }

    default Ingredient kjs$or(Ingredient ingredient) {
        return ingredient == Ingredient.EMPTY ? this.kjs$self() : (this == Ingredient.EMPTY ? ingredient : IngredientPlatformHelper.get().or(new Ingredient[] { this.kjs$self(), ingredient }));
    }

    default Ingredient kjs$subtract(Ingredient subtracted) {
        return IngredientPlatformHelper.get().subtract(this.kjs$self(), subtracted);
    }

    default InputItem kjs$asStack() {
        return InputItem.of(this.kjs$self(), 1);
    }

    default InputItem kjs$withCount(int count) {
        return InputItem.of(this.kjs$self(), count);
    }

    default boolean kjs$isWildcard() {
        return IngredientPlatformHelper.get().isWildcard(this.kjs$self());
    }

    default boolean kjs$canBeUsedForMatching() {
        return this.getClass() == Ingredient.class;
    }

    @Override
    default Ingredient kjs$asIngredient() {
        return this.kjs$self();
    }

    @Override
    default JsonElement toJsonJS() {
        return this.kjs$self().toJson();
    }
}