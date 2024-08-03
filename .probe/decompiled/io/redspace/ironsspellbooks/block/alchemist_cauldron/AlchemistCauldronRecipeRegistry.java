package io.redspace.ironsspellbooks.block.alchemist_cauldron;

import com.google.common.collect.ImmutableList;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.PotionRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;

public class AlchemistCauldronRecipeRegistry {

    private static final List<AlchemistCauldronRecipe> recipes = new ArrayList();

    public static AlchemistCauldronRecipe addRecipe(AlchemistCauldronRecipe recipe) {
        recipes.add(recipe);
        return recipe;
    }

    public static ItemStack getOutput(ItemStack input, ItemStack ingredient, boolean consumeOnSucces) {
        if (!input.isEmpty() && !ingredient.isEmpty()) {
            for (AlchemistCauldronRecipe recipe : recipes) {
                ItemStack result = recipe.createOutput(input, ingredient, consumeOnSucces);
                if (!result.isEmpty()) {
                    return result;
                }
            }
            return ItemStack.EMPTY;
        } else {
            return ItemStack.EMPTY;
        }
    }

    public static boolean isValidIngredient(ItemStack itemStack) {
        for (AlchemistCauldronRecipe recipe : recipes) {
            if (ItemStack.isSameItemSameTags(recipe.getIngredient(), itemStack)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasOutput(ItemStack input, ItemStack ingredient) {
        return !getOutput(input, ingredient, false).isEmpty();
    }

    public static ImmutableList<AlchemistCauldronRecipe> getRecipes() {
        return ImmutableList.copyOf(recipes);
    }

    static {
        recipes.add(new AlchemistCauldronRecipe(Potions.STRONG_HEALING, Items.OAK_LOG, ItemRegistry.OAKSKIN_ELIXIR.get()).setBaseRequirement(2).setResultLimit(1));
        recipes.add(new AlchemistCauldronRecipe(ItemRegistry.OAKSKIN_ELIXIR.get(), Items.AMETHYST_SHARD, ItemRegistry.GREATER_OAKSKIN_ELIXIR.get()).setBaseRequirement(2).setResultLimit(1));
        recipes.add(new AlchemistCauldronRecipe(Potions.STRONG_HEALING, Items.AMETHYST_SHARD, ItemRegistry.GREATER_HEALING_POTION.get()).setBaseRequirement(4).setResultLimit(1));
        recipes.add(new AlchemistCauldronRecipe(Potions.INVISIBILITY, ItemRegistry.SHRIVING_STONE.get(), ItemRegistry.INVISIBILITY_ELIXIR.get()).setBaseRequirement(4).setResultLimit(1));
        recipes.add(new AlchemistCauldronRecipe(Potions.LONG_INVISIBILITY, ItemRegistry.SHRIVING_STONE.get(), ItemRegistry.INVISIBILITY_ELIXIR.get()).setBaseRequirement(4).setResultLimit(1));
        recipes.add(new AlchemistCauldronRecipe(ItemRegistry.INVISIBILITY_ELIXIR.get(), Items.AMETHYST_CLUSTER, ItemRegistry.GREATER_INVISIBILITY_ELIXIR.get()));
        recipes.add(new AlchemistCauldronRecipe(PotionRegistry.INSTANT_MANA_THREE.get(), Items.ENDER_PEARL, ItemRegistry.EVASION_ELIXIR.get()).setBaseRequirement(4).setResultLimit(1));
        recipes.add(new AlchemistCauldronRecipe(ItemRegistry.EVASION_ELIXIR.get(), Items.DRAGON_BREATH, ItemRegistry.GREATER_EVASION_ELIXIR.get()));
        recipes.add(new AlchemistCauldronRecipe(ItemRegistry.INK_COMMON.get(), Items.COPPER_INGOT, ItemRegistry.INK_UNCOMMON.get()).setBaseRequirement(4).setResultLimit(1));
        recipes.add(new AlchemistCauldronRecipe(ItemRegistry.INK_UNCOMMON.get(), Items.IRON_INGOT, ItemRegistry.INK_RARE.get()).setBaseRequirement(4).setResultLimit(1));
        recipes.add(new AlchemistCauldronRecipe(ItemRegistry.INK_RARE.get(), Items.GOLD_INGOT, ItemRegistry.INK_EPIC.get()).setBaseRequirement(4).setResultLimit(1));
        recipes.add(new AlchemistCauldronRecipe(ItemRegistry.INK_EPIC.get(), Items.AMETHYST_SHARD, ItemRegistry.INK_LEGENDARY.get()).setBaseRequirement(4).setResultLimit(1));
    }
}