package fuzs.puzzleslib.api.item.v2.crafting;

import fuzs.puzzleslib.impl.core.CommonFactories;
import java.util.Objects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

public interface CombinedIngredients {

    CombinedIngredients INSTANCE = CommonFactories.INSTANCE.getCombinedIngredients();

    Ingredient all(Ingredient... var1);

    Ingredient any(Ingredient... var1);

    Ingredient difference(Ingredient var1, Ingredient var2);

    default Ingredient nbt(ItemLike item, @Nullable CompoundTag nbt, boolean strict) {
        Objects.requireNonNull(item, "item is null");
        if (!strict) {
            Objects.requireNonNull(nbt, "nbt is null");
        }
        ItemStack stack = new ItemStack(item);
        stack.setTag(nbt);
        return this.nbt(stack, strict);
    }

    Ingredient nbt(ItemStack var1, boolean var2);
}