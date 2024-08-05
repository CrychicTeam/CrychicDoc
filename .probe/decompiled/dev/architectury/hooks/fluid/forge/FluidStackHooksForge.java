package dev.architectury.hooks.fluid.forge;

import dev.architectury.fluid.FluidStack;
import dev.architectury.fluid.forge.FluidStackImpl;

public final class FluidStackHooksForge {

    private FluidStackHooksForge() {
    }

    public static FluidStack fromForge(net.minecraftforge.fluids.FluidStack stack) {
        return (FluidStack) FluidStackImpl.fromValue.apply(stack);
    }

    public static net.minecraftforge.fluids.FluidStack toForge(FluidStack stack) {
        return (net.minecraftforge.fluids.FluidStack) FluidStackImpl.toValue.apply(stack);
    }
}