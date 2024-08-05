package fr.frinn.custommachinery.api.crafting;

import fr.frinn.custommachinery.api.requirement.IRequirement;
import net.minecraft.resources.ResourceLocation;

public interface IRecipeBuilder<T extends IMachineRecipe> {

    IRecipeBuilder<T> withRequirement(IRequirement<?> var1);

    IRecipeBuilder<T> withJeiRequirement(IRequirement<?> var1);

    IRecipeBuilder<T> withPriority(int var1);

    IRecipeBuilder<T> withJeiPriority(int var1);

    IRecipeBuilder<T> hide();

    T build(ResourceLocation var1);
}