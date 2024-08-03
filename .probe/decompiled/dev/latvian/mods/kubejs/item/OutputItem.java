package dev.latvian.mods.kubejs.item;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.platform.IngredientPlatformHelper;
import dev.latvian.mods.kubejs.recipe.OutputReplacement;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.ReplacementMatch;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class OutputItem implements OutputReplacement {

    public static final OutputItem EMPTY = new OutputItem(ItemStack.EMPTY, Double.NaN, null);

    public final ItemStack item;

    public final double chance;

    public final IntProvider rolls;

    public static OutputItem of(ItemStack item, double chance) {
        return item.isEmpty() ? EMPTY : new OutputItem(item, chance, null);
    }

    public static OutputItem of(Object from) {
        if (from instanceof OutputItem) {
            return (OutputItem) from;
        } else if (from instanceof ItemStack stack) {
            return of(stack, Double.NaN);
        } else {
            ItemStack item = ItemStackJS.of(from);
            if (item.isEmpty()) {
                return EMPTY;
            } else {
                double chance = Double.NaN;
                IntProvider rolls = null;
                if (from instanceof JsonObject j) {
                    if (j.has("chance")) {
                        chance = j.get("chance").getAsDouble();
                    }
                    if (j.has("minRolls") && j.has("maxRolls")) {
                        rolls = UniformInt.of(j.get("minRolls").getAsInt(), j.get("maxRolls").getAsInt());
                    }
                }
                return new OutputItem(item, chance, rolls);
            }
        }
    }

    @Deprecated
    protected OutputItem(ItemStack item, double chance) {
        this(item, chance, null);
    }

    protected OutputItem(ItemStack item, double chance, @Nullable IntProvider rolls) {
        this.item = item;
        this.chance = chance;
        this.rolls = rolls;
    }

    public OutputItem withCount(int count) {
        return new OutputItem(this.item.kjs$withCount(count), this.chance, this.rolls);
    }

    public OutputItem withChance(double chance) {
        return new OutputItem(this.item.copy(), chance, this.rolls);
    }

    public OutputItem withRolls(IntProvider rolls) {
        return new OutputItem(this.item.copy(), this.chance, rolls);
    }

    public OutputItem withRolls(int min, int max) {
        return this.withRolls(UniformInt.of(min, max));
    }

    public boolean hasChance() {
        return !Double.isNaN(this.chance);
    }

    public double getChance() {
        return this.chance;
    }

    public int getCount() {
        return this.item.getCount();
    }

    public CompoundTag getNbt() {
        return this.item.getTag();
    }

    public String toString() {
        return this.item.kjs$toItemString();
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    @Override
    public Object replaceOutput(RecipeJS recipe, ReplacementMatch match, OutputReplacement original) {
        if (original instanceof OutputItem o) {
            OutputItem replacement = new OutputItem(this.item.copy(), o.chance, o.rolls);
            replacement.item.setCount(o.getCount());
            return replacement;
        } else {
            return new OutputItem(this.item.copy(), Double.NaN, null);
        }
    }

    @Deprecated
    public InputItem ignoreNBT() {
        ConsoleJS console = ConsoleJS.getCurrent(ConsoleJS.SERVER);
        console.warn("You don't need to call .ignoreNBT() anymore, all item ingredients ignore NBT by default!");
        return InputItem.of(this.item.getItem().kjs$asIngredient(), this.item.getCount());
    }

    public InputItem weakNBT() {
        return InputItem.of(IngredientPlatformHelper.get().weakNBT(this.item), this.item.getCount());
    }

    public InputItem strongNBT() {
        return InputItem.of(IngredientPlatformHelper.get().strongNBT(this.item), this.item.getCount());
    }
}