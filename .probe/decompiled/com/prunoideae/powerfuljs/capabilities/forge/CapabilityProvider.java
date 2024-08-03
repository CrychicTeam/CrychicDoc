package com.prunoideae.powerfuljs.capabilities.forge;

import java.util.function.Predicate;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CapabilityProvider implements ICapabilityProvider {

    protected final Capability<?> capability;

    protected final LazyOptional<?> instance;

    protected final Predicate<Direction> available;

    public static CapabilityProvider of(Object capability, Object instance, Predicate<Direction> available) {
        return new CapabilityProvider((Capability<?>) capability, LazyOptional.of(() -> instance), available);
    }

    public CapabilityProvider(Capability<?> capability, LazyOptional<?> instance, Predicate<Direction> available) {
        this.capability = capability;
        this.instance = instance;
        this.available = available;
    }

    @NotNull
    @Override
    public <C> LazyOptional<C> getCapability(@NotNull Capability<C> capability, @Nullable Direction arg) {
        return this.capability == capability && this.available.test(arg) ? this.instance.cast() : LazyOptional.empty();
    }
}