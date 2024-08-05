package net.minecraft.client.renderer.entity;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;

public class PiglinRenderer extends HumanoidMobRenderer<Mob, PiglinModel<Mob>> {

    private static final Map<EntityType<?>, ResourceLocation> TEXTURES = ImmutableMap.of(EntityType.PIGLIN, new ResourceLocation("textures/entity/piglin/piglin.png"), EntityType.ZOMBIFIED_PIGLIN, new ResourceLocation("textures/entity/piglin/zombified_piglin.png"), EntityType.PIGLIN_BRUTE, new ResourceLocation("textures/entity/piglin/piglin_brute.png"));

    private static final float PIGLIN_CUSTOM_HEAD_SCALE = 1.0019531F;

    public PiglinRenderer(EntityRendererProvider.Context entityRendererProviderContext0, ModelLayerLocation modelLayerLocation1, ModelLayerLocation modelLayerLocation2, ModelLayerLocation modelLayerLocation3, boolean boolean4) {
        super(entityRendererProviderContext0, createModel(entityRendererProviderContext0.getModelSet(), modelLayerLocation1, boolean4), 0.5F, 1.0019531F, 1.0F, 1.0019531F);
        this.m_115326_(new HumanoidArmorLayer<>(this, new HumanoidArmorModel(entityRendererProviderContext0.bakeLayer(modelLayerLocation2)), new HumanoidArmorModel(entityRendererProviderContext0.bakeLayer(modelLayerLocation3)), entityRendererProviderContext0.getModelManager()));
    }

    private static PiglinModel<Mob> createModel(EntityModelSet entityModelSet0, ModelLayerLocation modelLayerLocation1, boolean boolean2) {
        PiglinModel<Mob> $$3 = new PiglinModel<>(entityModelSet0.bakeLayer(modelLayerLocation1));
        if (boolean2) {
            $$3.rightEar.visible = false;
        }
        return $$3;
    }

    public ResourceLocation getTextureLocation(Mob mob0) {
        ResourceLocation $$1 = (ResourceLocation) TEXTURES.get(mob0.m_6095_());
        if ($$1 == null) {
            throw new IllegalArgumentException("I don't know what texture to use for " + mob0.m_6095_());
        } else {
            return $$1;
        }
    }

    protected boolean isShaking(Mob mob0) {
        return super.m_5936_(mob0) || mob0 instanceof AbstractPiglin && ((AbstractPiglin) mob0).isConverting();
    }
}