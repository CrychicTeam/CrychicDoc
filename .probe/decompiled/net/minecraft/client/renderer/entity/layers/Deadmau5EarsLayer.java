package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.util.Mth;

public class Deadmau5EarsLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public Deadmau5EarsLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderLayerParentAbstractClientPlayerPlayerModelAbstractClientPlayer0) {
        super(renderLayerParentAbstractClientPlayerPlayerModelAbstractClientPlayer0);
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, AbstractClientPlayer abstractClientPlayer3, float float4, float float5, float float6, float float7, float float8, float float9) {
        if ("deadmau5".equals(abstractClientPlayer3.m_7755_().getString()) && abstractClientPlayer3.isSkinLoaded() && !abstractClientPlayer3.m_20145_()) {
            VertexConsumer $$10 = multiBufferSource1.getBuffer(RenderType.entitySolid(abstractClientPlayer3.getSkinTextureLocation()));
            int $$11 = LivingEntityRenderer.getOverlayCoords(abstractClientPlayer3, 0.0F);
            for (int $$12 = 0; $$12 < 2; $$12++) {
                float $$13 = Mth.lerp(float6, abstractClientPlayer3.f_19859_, abstractClientPlayer3.m_146908_()) - Mth.lerp(float6, abstractClientPlayer3.f_20884_, abstractClientPlayer3.f_20883_);
                float $$14 = Mth.lerp(float6, abstractClientPlayer3.f_19860_, abstractClientPlayer3.m_146909_());
                poseStack0.pushPose();
                poseStack0.mulPose(Axis.YP.rotationDegrees($$13));
                poseStack0.mulPose(Axis.XP.rotationDegrees($$14));
                poseStack0.translate(0.375F * (float) ($$12 * 2 - 1), 0.0F, 0.0F);
                poseStack0.translate(0.0F, -0.375F, 0.0F);
                poseStack0.mulPose(Axis.XP.rotationDegrees(-$$14));
                poseStack0.mulPose(Axis.YP.rotationDegrees(-$$13));
                float $$15 = 1.3333334F;
                poseStack0.scale(1.3333334F, 1.3333334F, 1.3333334F);
                ((PlayerModel) this.m_117386_()).renderEars(poseStack0, $$10, int2, $$11);
                poseStack0.popPose();
            }
        }
    }
}