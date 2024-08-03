package net.mehvahdjukaar.moonlight.api.block;

import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidTank;

public interface ISoftFluidTankProvider {

    SoftFluidTank getSoftFluidTank();

    default boolean canInteractWithSoftFluidTank() {
        return true;
    }
}