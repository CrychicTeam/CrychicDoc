package fuzs.puzzleslib.api.client.core.v1.context;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

@FunctionalInterface
public interface EntitySpectatorShaderContext {

    void registerSpectatorShader(ResourceLocation var1, EntityType<?>... var2);
}