package dev.latvian.mods.kubejs.platform;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.util.Lazy;
import dev.latvian.mods.kubejs.util.Tags;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

public interface IngredientPlatformHelper {

    Lazy<IngredientPlatformHelper> INSTANCE = Lazy.serviceLoader(IngredientPlatformHelper.class);

    static IngredientPlatformHelper get() {
        return INSTANCE.get();
    }

    default InputItem stack(Ingredient ingredient, int count) {
        return InputItem.of(ingredient, count);
    }

    Ingredient wildcard();

    Ingredient custom(Ingredient var1, Predicate<ItemStack> var2);

    Ingredient custom(Ingredient var1, @Nullable UUID var2);

    default Ingredient tag(String tag) {
        return Ingredient.of(Tags.item(UtilsJS.getMCID(null, tag)));
    }

    Ingredient mod(String var1);

    Ingredient regex(Pattern var1);

    Ingredient creativeTab(CreativeModeTab var1);

    Ingredient subtract(Ingredient var1, Ingredient var2);

    Ingredient or(Ingredient[] var1);

    Ingredient and(Ingredient[] var1);

    Ingredient strongNBT(ItemStack var1);

    Ingredient weakNBT(ItemStack var1);

    boolean isWildcard(Ingredient var1);
}