package fr.frinn.custommachinery.common.integration.kubejs.requirements;

import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.rhino.mod.util.NBTUtils;
import fr.frinn.custommachinery.api.integration.kubejs.RecipeJSBuilder;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.common.requirement.FluidPerTickRequirement;
import fr.frinn.custommachinery.common.util.ingredient.FluidIngredient;
import fr.frinn.custommachinery.common.util.ingredient.FluidTagIngredient;
import java.util.Map;

public interface FluidPerTickRequirementJS extends RecipeJSBuilder {

    default RecipeJSBuilder requireFluidPerTick(FluidStackJS stack) {
        return this.requireFluidPerTick(stack, "");
    }

    default RecipeJSBuilder requireFluidPerTick(FluidStackJS stack, String tank) {
        return this.addRequirement(new FluidPerTickRequirement(RequirementIOMode.INPUT, new FluidIngredient(stack.getFluid()), stack.kjs$getAmount(), stack.getFluidStack().getTag(), tank));
    }

    default RecipeJSBuilder requireFluidTagPerTick(String tag, int amount) {
        return this.requireFluidTagPerTick(tag, amount, null, "");
    }

    default RecipeJSBuilder requireFluidTagPerTick(String tag, int amount, Object thing) {
        return thing instanceof String ? this.requireFluidTagPerTick(tag, amount, null, (String) thing) : this.requireFluidTagPerTick(tag, amount, MapJS.of(thing), "");
    }

    default RecipeJSBuilder requireFluidTagPerTick(String tag, int amount, Map<?, ?> nbt, String tank) {
        try {
            return this.addRequirement(new FluidPerTickRequirement(RequirementIOMode.INPUT, FluidTagIngredient.create(tag), (long) amount, nbt == null ? null : NBTUtils.toTagCompound(nbt), tank));
        } catch (IllegalArgumentException var6) {
            return this.error(var6.getMessage(), new Object[0]);
        }
    }

    default RecipeJSBuilder produceFluidPerTick(FluidStackJS stack) {
        return this.produceFluidPerTick(stack, "");
    }

    default RecipeJSBuilder produceFluidPerTick(FluidStackJS stack, String tank) {
        return this.addRequirement(new FluidPerTickRequirement(RequirementIOMode.OUTPUT, new FluidIngredient(stack.getFluid()), stack.kjs$getAmount(), stack.getFluidStack().getTag(), tank));
    }
}