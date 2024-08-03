package net.minecraft.client.renderer.entity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.model.ChestedHorseModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;

public class ChestedHorseRenderer<T extends AbstractChestedHorse> extends AbstractHorseRenderer<T, ChestedHorseModel<T>> {

    private static final Map<EntityType<?>, ResourceLocation> MAP = Maps.newHashMap(ImmutableMap.of(EntityType.DONKEY, new ResourceLocation("textures/entity/horse/donkey.png"), EntityType.MULE, new ResourceLocation("textures/entity/horse/mule.png")));

    public ChestedHorseRenderer(EntityRendererProvider.Context entityRendererProviderContext0, float float1, ModelLayerLocation modelLayerLocation2) {
        super(entityRendererProviderContext0, new ChestedHorseModel<>(entityRendererProviderContext0.bakeLayer(modelLayerLocation2)), float1);
    }

    public ResourceLocation getTextureLocation(T t0) {
        return (ResourceLocation) MAP.get(t0.m_6095_());
    }
}