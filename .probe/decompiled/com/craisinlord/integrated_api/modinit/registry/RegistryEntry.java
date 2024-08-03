package com.craisinlord.integrated_api.modinit.registry;

import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;

public interface RegistryEntry<T> extends Supplier<T> {

    T get();

    ResourceLocation getId();
}