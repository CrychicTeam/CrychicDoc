package com.simibubi.create.content.processing.sequenced;

import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IAssemblyRecipe {

    default boolean supportsAssembly() {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    Component getDescriptionForAssembly();

    void addRequiredMachines(Set<ItemLike> var1);

    void addAssemblyIngredients(List<Ingredient> var1);

    default void addAssemblyFluidIngredients(List<FluidIngredient> list) {
    }

    Supplier<Supplier<SequencedAssemblySubCategory>> getJEISubCategory();
}