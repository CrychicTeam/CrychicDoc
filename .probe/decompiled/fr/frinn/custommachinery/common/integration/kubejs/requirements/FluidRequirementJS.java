package fr.frinn.custommachinery.common.integration.kubejs.requirements;

import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.rhino.mod.util.NBTUtils;
import fr.frinn.custommachinery.api.integration.kubejs.RecipeJSBuilder;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.common.requirement.FluidRequirement;
import fr.frinn.custommachinery.common.util.ingredient.FluidIngredient;
import fr.frinn.custommachinery.common.util.ingredient.FluidTagIngredient;
import java.util.Map;

public interface FluidRequirementJS extends RecipeJSBuilder {

    default RecipeJSBuilder requireFluid(FluidStackJS stack) {
        return this.requireFluid(stack, "");
    }

    default RecipeJSBuilder requireFluid(FluidStackJS stack, String tank) {
        return this.addRequirement(new FluidRequirement(RequirementIOMode.INPUT, new FluidIngredient(stack.getFluid()), stack.kjs$getAmount(), stack.getFluidStack().getTag(), tank));
    }

    default RecipeJSBuilder requireFluidTag(String tag, int amount) {
        return this.requireFluidTag(tag, amount, null, "");
    }

    default RecipeJSBuilder requireFluidTag(String tag, int amount, Object thing) {
        return thing instanceof String ? this.requireFluidTag(tag, amount, null, (String) thing) : this.requireFluidTag(tag, amount, MapJS.of(thing), "");
    }

    default RecipeJSBuilder requireFluidTag(String tag, int amount, Map<?, ?> nbt, String tank) {
        try {
            return this.addRequirement(new FluidRequirement(RequirementIOMode.INPUT, FluidTagIngredient.create(tag), (long) amount, nbt == null ? null : NBTUtils.toTagCompound(nbt), tank));
        } catch (IllegalArgumentException var6) {
            return this.error(var6.getMessage(), new Object[0]);
        }
    }

    default RecipeJSBuilder produceFluid(FluidStackJS stack) {
        return this.produceFluid(stack, "");
    }

    default RecipeJSBuilder produceFluid(FluidStackJS stack, String tank) {
        return this.addRequirement(new FluidRequirement(RequirementIOMode.OUTPUT, new FluidIngredient(stack.getFluid()), stack.kjs$getAmount(), stack.getFluidStack().getTag(), tank));
    }
}