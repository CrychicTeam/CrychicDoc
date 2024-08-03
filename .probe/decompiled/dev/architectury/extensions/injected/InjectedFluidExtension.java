package dev.architectury.extensions.injected;

import net.minecraft.core.Holder;
import net.minecraft.world.level.material.Fluid;

public interface InjectedFluidExtension extends InjectedRegistryEntryExtension<Fluid> {

    @Override
    default Holder<Fluid> arch$holder() {
        return ((Fluid) this).builtInRegistryHolder();
    }
}