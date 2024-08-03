package dev.latvian.mods.kubejs.integration.forge.jei;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.util.ListJS;
import java.util.Set;
import java.util.stream.Collectors;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;

public class InformationJEIEventJS extends EventJS {

    private final IRecipeRegistration registration;

    public InformationJEIEventJS(IRecipeRegistration reg) {
        this.registration = reg;
    }

    public void addItem(Ingredient item, Component[] s) {
        this.registration.addIngredientInfo(item.kjs$getStacks().toList(), VanillaTypes.ITEM_STACK, s);
    }

    public void addFluid(Object fluid, Component[] s) {
        this.registration.addIngredientInfo(JEIPlugin.fromArchitectury(FluidStackJS.of(fluid).getFluidStack()), ForgeTypes.FLUID_STACK, s);
    }

    public <T> void addForType(IIngredientType<T> type, Object o, Component[] s) {
        Set<String> targets = (Set<String>) ListJS.orSelf(o).stream().map(String::valueOf).collect(Collectors.toSet());
        IIngredientManager manager = this.registration.getIngredientManager();
        IIngredientHelper<T> helper = manager.getIngredientHelper(type);
        this.registration.addIngredientInfo(manager.getAllIngredients(type).stream().filter(t -> targets.contains(helper.getWildcardId((T) t))).toList(), type, s);
    }
}