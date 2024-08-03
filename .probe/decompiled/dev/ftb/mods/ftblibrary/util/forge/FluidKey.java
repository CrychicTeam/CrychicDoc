package dev.ftb.mods.ftblibrary.util.forge;

import java.util.Objects;
import net.minecraftforge.fluids.FluidStack;

public final class FluidKey {

    public final FluidStack stack;

    public FluidKey(FluidStack s) {
        this.stack = s;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            FluidKey fluidKey = (FluidKey) o;
            return this.stack.isFluidEqual(fluidKey.stack);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.stack.getFluid(), this.stack.getTag() });
    }
}