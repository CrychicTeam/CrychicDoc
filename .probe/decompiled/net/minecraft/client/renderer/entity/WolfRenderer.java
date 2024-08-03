package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.WolfCollarLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;

public class WolfRenderer extends MobRenderer<Wolf, WolfModel<Wolf>> {

    private static final ResourceLocation WOLF_LOCATION = new ResourceLocation("textures/entity/wolf/wolf.png");

    private static final ResourceLocation WOLF_TAME_LOCATION = new ResourceLocation("textures/entity/wolf/wolf_tame.png");

    private static final ResourceLocation WOLF_ANGRY_LOCATION = new ResourceLocation("textures/entity/wolf/wolf_angry.png");

    public WolfRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new WolfModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.WOLF)), 0.5F);
        this.m_115326_(new WolfCollarLayer(this));
    }

    protected float getBob(Wolf wolf0, float float1) {
        return wolf0.getTailAngle();
    }

    public void render(Wolf wolf0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        if (wolf0.isWet()) {
            float $$6 = wolf0.getWetShade(float2);
            ((WolfModel) this.f_115290_).m_102419_($$6, $$6, $$6);
        }
        super.render(wolf0, float1, float2, poseStack3, multiBufferSource4, int5);
        if (wolf0.isWet()) {
            ((WolfModel) this.f_115290_).m_102419_(1.0F, 1.0F, 1.0F);
        }
    }

    public ResourceLocation getTextureLocation(Wolf wolf0) {
        if (wolf0.m_21824_()) {
            return WOLF_TAME_LOCATION;
        } else {
            return wolf0.m_21660_() ? WOLF_ANGRY_LOCATION : WOLF_LOCATION;
        }
    }
}