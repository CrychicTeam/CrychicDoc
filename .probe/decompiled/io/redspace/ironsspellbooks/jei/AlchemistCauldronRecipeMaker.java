package io.redspace.ironsspellbooks.jei;

import com.google.common.collect.ImmutableList;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.block.alchemist_cauldron.AlchemistCauldronRecipe;
import io.redspace.ironsspellbooks.block.alchemist_cauldron.AlchemistCauldronRecipeRegistry;
import io.redspace.ironsspellbooks.block.alchemist_cauldron.AlchemistCauldronTile;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

public final class AlchemistCauldronRecipeMaker {

    private AlchemistCauldronRecipeMaker() {
    }

    public static List<AlchemistCauldronJeiRecipe> getRecipes(IVanillaRecipeFactory vanillaRecipeFactory, IIngredientManager ingredientManager) {
        return Stream.of(getScrollRecipes(vanillaRecipeFactory, ingredientManager), getCustomRecipes(vanillaRecipeFactory, ingredientManager), getPotionRecipes(vanillaRecipeFactory, ingredientManager)).flatMap(x -> x).toList();
    }

    private static Stream<AlchemistCauldronJeiRecipe> getScrollRecipes(IVanillaRecipeFactory vanillaRecipeFactory, IIngredientManager ingredientManager) {
        return Arrays.stream(SpellRarity.values()).map(AlchemistCauldronRecipeMaker::enumerateSpellsForRarity);
    }

    private static Stream<AlchemistCauldronJeiRecipe> getCustomRecipes(IVanillaRecipeFactory vanillaRecipeFactory, IIngredientManager ingredientManager) {
        ImmutableList<AlchemistCauldronRecipe> recipes = AlchemistCauldronRecipeRegistry.getRecipes();
        List<ItemStack> reagents = ingredientManager.getAllItemStacks().stream().filter(AlchemistCauldronRecipeRegistry::isValidIngredient).toList();
        return reagents.stream().map(reagentStack -> {
            List<ItemStack> catalysts = new ArrayList();
            List<ItemStack> outputs = new ArrayList();
            AlchemistCauldronRecipeRegistry.getRecipes().forEach(recipe -> {
                if (ItemStack.isSameItemSameTags(reagentStack, recipe.getIngredient())) {
                    catalysts.add(recipe.getInput());
                    ItemStack result = recipe.getResult();
                    if (result.getCount() == 4) {
                        result.setCount(1);
                    }
                    outputs.add(result);
                }
            });
            return new AlchemistCauldronJeiRecipe(List.of(reagentStack), outputs, catalysts);
        });
    }

    private static Stream<AlchemistCauldronJeiRecipe> getPotionRecipes(IVanillaRecipeFactory vanillaRecipeFactory, IIngredientManager ingredientManager) {
        if (!ServerConfigs.ALLOW_CAULDRON_BREWING.get()) {
            return Stream.of();
        } else {
            List<ItemStack> potionReagents = ingredientManager.getAllItemStacks().stream().filter(AlchemistCauldronRecipeMaker::isIngredient).toList();
            return potionReagents.stream().map(reagentStack -> {
                List<ItemStack> catalysts = new ArrayList();
                List<ItemStack> outputs = new ArrayList();
                ingredientManager.getAllItemStacks().stream().filter(itemStack -> itemStack.getItem() instanceof PotionItem && BrewingRecipeRegistry.hasOutput(itemStack, reagentStack)).forEach(baseStack -> {
                    catalysts.add(baseStack);
                    outputs.add(BrewingRecipeRegistry.getOutput(baseStack, reagentStack));
                });
                return new AlchemistCauldronJeiRecipe(List.of(reagentStack), outputs, catalysts);
            });
        }
    }

    private static List<ItemStack> enumerateScrollLevels(AbstractSpell spell) {
        ItemStack scrollStack = new ItemStack(ItemRegistry.SCROLL.get());
        ArrayList<ItemStack> scrolls = new ArrayList();
        IntStream.rangeClosed(spell.getMinLevel(), spell.getMaxLevel()).forEach(spellLevel -> scrolls.add(getScrollStack(scrollStack, spell, spellLevel)));
        return scrolls;
    }

    private static AlchemistCauldronJeiRecipe enumerateSpellsForRarity(SpellRarity spellRarity) {
        ArrayList<ItemStack> inputs = new ArrayList();
        ArrayList<ItemStack> catalysts = new ArrayList();
        ArrayList<ItemStack> outputs = new ArrayList();
        ItemStack scrollStack = new ItemStack(ItemRegistry.SCROLL.get());
        SpellRegistry.getEnabledSpells().forEach(spell -> IntStream.rangeClosed(spell.getMinLevel(), spell.getMaxLevel()).filter(spellLevel -> spell.getRarity(spellLevel) == spellRarity).forEach(filteredLevel -> inputs.add(getScrollStack(scrollStack, spell, filteredLevel))));
        inputs.forEach(itemStack -> {
            catalysts.add(ItemStack.EMPTY);
            outputs.add(new ItemStack(AlchemistCauldronTile.getInkFromScroll(itemStack)));
        });
        return new AlchemistCauldronJeiRecipe(inputs, outputs, catalysts);
    }

    private static ItemStack getScrollStack(ItemStack stack, AbstractSpell spell, int spellLevel) {
        ItemStack scrollStack = stack.copy();
        ISpellContainer.createScrollContainer(spell, spellLevel, scrollStack);
        return scrollStack;
    }

    private static boolean isIngredient(ItemStack itemStack) {
        try {
            return PotionBrewing.isIngredient(itemStack);
        } catch (LinkageError | RuntimeException var2) {
            IronsSpellbooks.LOGGER.error("Failed to check if item is a potion reagent {}.", itemStack.toString(), var2);
            return false;
        }
    }
}