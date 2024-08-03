package net.minecraftforge.registries;

import net.minecraft.resources.ResourceLocation;

public interface IForgeRegistryModifiable<V> extends IForgeRegistry<V> {

    void clear();

    V remove(ResourceLocation var1);

    boolean isLocked();
}