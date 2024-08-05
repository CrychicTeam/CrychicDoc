package net.darkhax.attributefix.temp;

import java.util.Collection;
import net.minecraft.resources.ResourceLocation;

public interface RegistryHelper<T> {

    T get(ResourceLocation var1);

    ResourceLocation getId(T var1);

    boolean isRegistered(T var1);

    boolean exists(ResourceLocation var1);

    Collection<T> getValues();
}