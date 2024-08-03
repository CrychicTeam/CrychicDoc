package dev.xkmc.modulargolems.compat.materials.create.automation;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;

public class ConditionalSARecipeBuilder extends SequencedAssemblyRecipeBuilder {

    public ConditionalSARecipeBuilder(ResourceLocation id) {
        super(id);
    }

    public ConditionalSARecipeBuilder withCondition(ICondition condition) {
        this.recipeConditions.add(condition);
        return this;
    }
}