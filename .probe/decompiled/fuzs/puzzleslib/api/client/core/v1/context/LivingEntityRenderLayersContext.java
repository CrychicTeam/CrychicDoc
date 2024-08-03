package fuzs.puzzleslib.api.client.core.v1.context;

import java.util.function.BiFunction;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface LivingEntityRenderLayersContext {

    <E extends LivingEntity, T extends E, M extends EntityModel<T>> void registerRenderLayer(EntityType<E> var1, BiFunction<RenderLayerParent<T, M>, EntityRendererProvider.Context, RenderLayer<T, M>> var2);
}