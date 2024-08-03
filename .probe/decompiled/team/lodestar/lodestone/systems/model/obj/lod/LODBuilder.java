package team.lodestar.lodestone.systems.model.obj.lod;

import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface LODBuilder<T> {

    void create(T var1, ResourceLocation var2);
}