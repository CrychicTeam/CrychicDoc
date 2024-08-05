package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Slime;

public class SlimeRenderer extends MobRenderer<Slime, SlimeModel<Slime>> {

    private static final ResourceLocation SLIME_LOCATION = new ResourceLocation("textures/entity/slime/slime.png");

    public SlimeRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new SlimeModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.SLIME)), 0.25F);
        this.m_115326_(new SlimeOuterLayer<>(this, entityRendererProviderContext0.getModelSet()));
    }

    public void render(Slime slime0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        this.f_114477_ = 0.25F * (float) slime0.getSize();
        super.render(slime0, float1, float2, poseStack3, multiBufferSource4, int5);
    }

    protected void scale(Slime slime0, PoseStack poseStack1, float float2) {
        float $$3 = 0.999F;
        poseStack1.scale(0.999F, 0.999F, 0.999F);
        poseStack1.translate(0.0F, 0.001F, 0.0F);
        float $$4 = (float) slime0.getSize();
        float $$5 = Mth.lerp(float2, slime0.oSquish, slime0.squish) / ($$4 * 0.5F + 1.0F);
        float $$6 = 1.0F / ($$5 + 1.0F);
        poseStack1.scale($$6 * $$4, 1.0F / $$6 * $$4, $$6 * $$4);
    }

    public ResourceLocation getTextureLocation(Slime slime0) {
        return SLIME_LOCATION;
    }
}