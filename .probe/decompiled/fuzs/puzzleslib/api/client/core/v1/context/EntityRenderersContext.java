package fuzs.puzzleslib.api.client.core.v1.context;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

@FunctionalInterface
public interface EntityRenderersContext {

    <T extends Entity> void registerEntityRenderer(EntityType<? extends T> var1, EntityRendererProvider<T> var2);
}