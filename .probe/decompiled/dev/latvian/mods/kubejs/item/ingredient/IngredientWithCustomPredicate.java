package dev.latvian.mods.kubejs.item.ingredient;

import java.util.UUID;
import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

public class IngredientWithCustomPredicate {

    public final Ingredient parent;

    public final UUID uuid;

    public final Predicate<ItemStack> predicate;

    public IngredientWithCustomPredicate(Ingredient parent, @Nullable UUID uuid, Predicate<ItemStack> predicate) {
        this.parent = parent;
        this.uuid = uuid;
        this.predicate = predicate;
    }
}