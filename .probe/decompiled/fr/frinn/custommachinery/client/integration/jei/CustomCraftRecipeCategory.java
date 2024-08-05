package fr.frinn.custommachinery.client.integration.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import fr.frinn.custommachinery.api.component.IMachineComponentTemplate;
import fr.frinn.custommachinery.api.integration.jei.IJEIIngredientWrapper;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.client.integration.jei.wrapper.ItemIngredientWrapper;
import fr.frinn.custommachinery.common.component.ItemMachineComponent;
import fr.frinn.custommachinery.common.component.variant.item.ResultItemComponentVariant;
import fr.frinn.custommachinery.common.crafting.craft.CustomCraftRecipe;
import fr.frinn.custommachinery.common.machine.CustomMachine;
import fr.frinn.custommachinery.common.util.ingredient.ItemIngredient;
import java.util.List;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;

public class CustomCraftRecipeCategory extends AbstractRecipeCategory<CustomCraftRecipe> {

    public CustomCraftRecipeCategory(CustomMachine machine, RecipeType<CustomCraftRecipe> type, IJeiHelpers helpers) {
        super(machine, type, helpers);
        this.wrapperCache = CacheBuilder.newBuilder().build(new CacheLoader<CustomCraftRecipe, List<IJEIIngredientWrapper<?>>>() {

            public List<IJEIIngredientWrapper<?>> load(CustomCraftRecipe recipe) {
                Builder<IJEIIngredientWrapper<?>> wrappers = ImmutableList.builder();
                recipe.getJEIIngredientRequirements().forEach(requirement -> wrappers.addAll(requirement.getJEIIngredientWrappers(recipe)));
                String resultSlot = (String) machine.getComponentTemplates().stream().filter(template -> {
                    if (template instanceof ItemMachineComponent.Template slotTemplate && slotTemplate.getVariant() == ResultItemComponentVariant.INSTANCE) {
                        return true;
                    }
                    return false;
                }).findFirst().map(IMachineComponentTemplate::getId).orElse("");
                wrappers.add(new ItemIngredientWrapper(RequirementIOMode.OUTPUT, new ItemIngredient(recipe.getOutput().getItem()), recipe.getOutput().getCount(), 1.0, false, recipe.getOutput().getTag(), resultSlot, false));
                return wrappers.build();
            }
        });
    }
}