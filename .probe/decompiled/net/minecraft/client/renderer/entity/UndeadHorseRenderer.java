package net.minecraft.client.renderer.entity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

public class UndeadHorseRenderer extends AbstractHorseRenderer<AbstractHorse, HorseModel<AbstractHorse>> {

    private static final Map<EntityType<?>, ResourceLocation> MAP = Maps.newHashMap(ImmutableMap.of(EntityType.ZOMBIE_HORSE, new ResourceLocation("textures/entity/horse/horse_zombie.png"), EntityType.SKELETON_HORSE, new ResourceLocation("textures/entity/horse/horse_skeleton.png")));

    public UndeadHorseRenderer(EntityRendererProvider.Context entityRendererProviderContext0, ModelLayerLocation modelLayerLocation1) {
        super(entityRendererProviderContext0, new HorseModel<>(entityRendererProviderContext0.bakeLayer(modelLayerLocation1)), 1.0F);
    }

    public ResourceLocation getTextureLocation(AbstractHorse abstractHorse0) {
        return (ResourceLocation) MAP.get(abstractHorse0.m_6095_());
    }
}