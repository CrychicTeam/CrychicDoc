package dev.latvian.mods.kubejs.create;

import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.latvian.mods.kubejs.fluid.FluidLike;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.fluid.InputFluid;
import dev.latvian.mods.kubejs.util.Tags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

public record CreateInputFluid(FluidIngredient ingredient) implements InputFluid {

    public static final CreateInputFluid EMPTY = new CreateInputFluid(FluidIngredient.EMPTY);

    @Override
    public boolean kjs$isEmpty() {
        return this.ingredient.equals(FluidIngredient.EMPTY) || this.ingredient.getMatchingFluidStacks().isEmpty();
    }

    @Override
    public long kjs$getAmount() {
        return (long) this.ingredient.getRequiredAmount();
    }

    @Override
    public FluidLike kjs$copy(long amount) {
        if (this.ingredient instanceof FluidIngredient.FluidStackIngredient in) {
            FluidStack fs = (FluidStack) in.getMatchingFluidStacks().get(0);
            return FluidStackJS.of(fs.getFluid(), amount, fs.getTag() == null ? null : fs.getTag().copy());
        } else {
            if (this.ingredient instanceof FluidIngredient.FluidTagIngredient in && (long) in.getRequiredAmount() != amount) {
                TagKey<Fluid> tag = Tags.fluid(new ResourceLocation(in.serialize().get("fluidTag").getAsString()));
                return new CreateInputFluid(FluidIngredient.fromTag(tag, (int) amount));
            }
            return this;
        }
    }
}