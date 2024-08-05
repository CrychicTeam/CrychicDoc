package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.phys.Vec3;

public class IllusionerRenderer extends IllagerRenderer<Illusioner> {

    private static final ResourceLocation ILLUSIONER = new ResourceLocation("textures/entity/illager/illusioner.png");

    public IllusionerRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new IllagerModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.ILLUSIONER)), 0.5F);
        this.m_115326_(new ItemInHandLayer<Illusioner, IllagerModel<Illusioner>>(this, entityRendererProviderContext0.getItemInHandRenderer()) {

            public void render(PoseStack p_114989_, MultiBufferSource p_114990_, int p_114991_, Illusioner p_114992_, float p_114993_, float p_114994_, float p_114995_, float p_114996_, float p_114997_, float p_114998_) {
                if (p_114992_.m_33736_() || p_114992_.m_5912_()) {
                    super.render(p_114989_, p_114990_, p_114991_, p_114992_, p_114993_, p_114994_, p_114995_, p_114996_, p_114997_, p_114998_);
                }
            }
        });
        ((IllagerModel) this.f_115290_).getHat().visible = true;
    }

    public ResourceLocation getTextureLocation(Illusioner illusioner0) {
        return ILLUSIONER;
    }

    public void render(Illusioner illusioner0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        if (illusioner0.m_20145_()) {
            Vec3[] $$6 = illusioner0.getIllusionOffsets(float2);
            float $$7 = this.m_6930_(illusioner0, float2);
            for (int $$8 = 0; $$8 < $$6.length; $$8++) {
                poseStack3.pushPose();
                poseStack3.translate($$6[$$8].x + (double) Mth.cos((float) $$8 + $$7 * 0.5F) * 0.025, $$6[$$8].y + (double) Mth.cos((float) $$8 + $$7 * 0.75F) * 0.0125, $$6[$$8].z + (double) Mth.cos((float) $$8 + $$7 * 0.7F) * 0.025);
                super.m_7392_(illusioner0, float1, float2, poseStack3, multiBufferSource4, int5);
                poseStack3.popPose();
            }
        } else {
            super.m_7392_(illusioner0, float1, float2, poseStack3, multiBufferSource4, int5);
        }
    }

    protected boolean isBodyVisible(Illusioner illusioner0) {
        return true;
    }
}