package io.redspace.ironsspellbooks.jei;

import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.ModTags;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

public final class ScrollForgeRecipeMaker {

    private ScrollForgeRecipeMaker() {
    }

    public static List<ScrollForgeRecipe> getRecipes(IVanillaRecipeFactory vanillaRecipeFactory, IIngredientManager ingredientManager) {
        List<InkItem> inkItems = ForgeRegistries.ITEMS.getValues().stream().filter(item -> item instanceof InkItem).map(item -> (InkItem) item).toList();
        Stream<ScrollForgeRecipe> recipes = ForgeRegistries.ITEMS.getValues().stream().filter(item -> item.builtInRegistryHolder().is(ModTags.SCHOOL_FOCUS)).map(item -> {
            ItemStack paperInput = new ItemStack(Items.PAPER);
            ItemStack focusInput = new ItemStack(item);
            SchoolType school = SchoolRegistry.getSchoolFromFocus(focusInput);
            List<AbstractSpell> spells = SpellRegistry.getSpellsForSchool(school);
            ArrayList<ItemStack> scrollOutputs = new ArrayList();
            ArrayList<ItemStack> inkOutputs = new ArrayList();
            inkItems.forEach(ink -> {
                for (AbstractSpell spell : spells) {
                    if (spell.isEnabled()) {
                        int spellLevel = spell.getMinLevelForRarity(ink.getRarity());
                        if (spellLevel > 0 && spell != SpellRegistry.none()) {
                            inkOutputs.add(new ItemStack(ink));
                            scrollOutputs.add(getScrollStack(spell, spellLevel));
                        }
                    }
                }
            });
            return new ScrollForgeRecipe(inkOutputs, paperInput, focusInput, scrollOutputs);
        });
        return recipes.toList();
    }

    private static ItemStack getScrollStack(AbstractSpell spell, int spellLevel) {
        ItemStack scrollStack = new ItemStack(ItemRegistry.SCROLL.get());
        ISpellContainer.createScrollContainer(spell, spellLevel, scrollStack);
        return scrollStack;
    }

    private static record FocusToSchool(Item item, SchoolType schoolType) {
    }
}