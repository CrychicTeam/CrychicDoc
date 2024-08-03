package dev.latvian.mods.kubejs.registry;

import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;

public interface RegistryCallback<T> {

    void accept(ResourceLocation var1, Supplier<T> var2);
}