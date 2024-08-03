package fr.frinn.custommachinery.api.crafting;

import fr.frinn.custommachinery.api.integration.jei.IJEIIngredientRequirement;
import fr.frinn.custommachinery.api.requirement.IRequirement;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;

public interface IMachineRecipe {

    ResourceLocation getRecipeId();

    ResourceLocation getMachineId();

    int getRecipeTime();

    List<IRequirement<?>> getRequirements();

    List<IRequirement<?>> getJeiRequirements();

    default List<IRequirement<?>> getDisplayInfoRequirements() {
        return this.getJeiRequirements().isEmpty() ? this.getRequirements() : this.getJeiRequirements();
    }

    default List<IJEIIngredientRequirement<?>> getJEIIngredientRequirements() {
        return this.getJeiRequirements().isEmpty() ? (List) this.getRequirements().stream().filter(requirement -> requirement instanceof IJEIIngredientRequirement).map(requirement -> (IJEIIngredientRequirement) requirement).collect(Collectors.toList()) : (List) this.getJeiRequirements().stream().filter(requirement -> requirement instanceof IJEIIngredientRequirement).map(requirement -> (IJEIIngredientRequirement) requirement).collect(Collectors.toList());
    }

    int getPriority();

    int getJeiPriority();

    boolean shouldResetOnError();

    boolean showInJei();
}