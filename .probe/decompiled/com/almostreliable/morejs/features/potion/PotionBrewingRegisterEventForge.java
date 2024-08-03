package com.almostreliable.morejs.features.potion;

import com.almostreliable.morejs.mixin.BrewingRecipeRegistryAccessor;
import com.almostreliable.morejs.mixin.potion.PotionBrewingAccessor;
import com.almostreliable.morejs.util.Utils;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import java.util.ListIterator;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.common.brewing.VanillaBrewingRecipe;
import net.minecraftforge.registries.ForgeRegistries;

public class PotionBrewingRegisterEventForge extends PotionBrewingRegisterEvent {

    @Override
    public void addCustomBrewing(Ingredient topInput, Ingredient bottomInput, ItemStack output) {
        this.validate(topInput, bottomInput, output);
        BrewingRecipeRegistry.addRecipe(bottomInput, topInput, output);
    }

    @Override
    public void addPotionBrewing(Ingredient ingredient, Potion input, Potion output) {
        this.validateSimple(input, ingredient, output);
        PotionBrewingAccessor.getMixes().add(new PotionBrewing.Mix(ForgeRegistries.POTIONS, input, ingredient, output));
    }

    @Override
    protected Potion getInputPotionFromMix(PotionBrewing.Mix<Potion> mix) {
        return (Potion) mix.f_43532_.get();
    }

    @Override
    protected Potion getOutputPotionFromMix(PotionBrewing.Mix<Potion> mix) {
        return (Potion) mix.f_43534_.get();
    }

    @Override
    protected Item getOutputItemFromMix(PotionBrewing.Mix<Item> mix) {
        return (Item) mix.f_43534_.get();
    }

    @Override
    public void addContainerRecipe(Item from, Ingredient ingredient, Item output) {
        this.validateContainer(from, ingredient, output);
        PotionBrewing.Mix<Item> mix = new PotionBrewing.Mix(ForgeRegistries.ITEMS, from, ingredient, output);
        PotionBrewingAccessor.getContainerMixes().add(mix);
    }

    public void removeByCustom(@Nullable Ingredient topInput, @Nullable Ingredient bottomInput, @Nullable Ingredient output) {
        ListIterator<IBrewingRecipe> it = BrewingRecipeRegistryAccessor.getRecipes().listIterator();
        while (it.hasNext()) {
            IBrewingRecipe recipe = (IBrewingRecipe) it.next();
            if (recipe instanceof BrewingRecipe) {
                BrewingRecipe br = (BrewingRecipe) recipe;
                boolean matchesInput = topInput == null || Utils.matchesIngredient(topInput, br.getIngredient());
                boolean matchesIngredient = bottomInput == null || Utils.matchesIngredient(bottomInput, br.getInput());
                boolean matchesOutput = output == null || output.test(br.getOutput());
                if (matchesInput && matchesIngredient && matchesOutput) {
                    String s = String.format("Removing custom brewing recipe: [Input: %s][Ingredient: %s][Output: %s]", br.getInput(), br.getIngredient(), br.getOutput());
                    ConsoleJS.STARTUP.info(s);
                    it.remove();
                }
            }
        }
    }

    public void removeByCustom(Predicate<IBrewingRecipe> predicate) {
        ListIterator<IBrewingRecipe> it = BrewingRecipeRegistryAccessor.getRecipes().listIterator();
        while (it.hasNext()) {
            IBrewingRecipe recipe = (IBrewingRecipe) it.next();
            if (!(recipe instanceof BrewingRecipe) && !(recipe instanceof VanillaBrewingRecipe) && predicate.test(recipe)) {
                ConsoleJS.STARTUP.info("Removing custom brewing recipe: " + recipe);
                it.remove();
            }
        }
    }
}