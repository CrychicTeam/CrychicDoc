package mezz.jei.api.forge;

import mezz.jei.api.ingredients.IIngredientTypeWithSubtypes;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

public final class ForgeTypes {

    public static final IIngredientTypeWithSubtypes<Fluid, FluidStack> FLUID_STACK = new IIngredientTypeWithSubtypes<Fluid, FluidStack>() {

        @Override
        public Class<? extends FluidStack> getIngredientClass() {
            return FluidStack.class;
        }

        @Override
        public Class<? extends Fluid> getIngredientBaseClass() {
            return Fluid.class;
        }

        public Fluid getBase(FluidStack ingredient) {
            return ingredient.getFluid();
        }
    };

    private ForgeTypes() {
    }
}