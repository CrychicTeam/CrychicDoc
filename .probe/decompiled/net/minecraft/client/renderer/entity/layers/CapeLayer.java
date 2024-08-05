package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CapeLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public CapeLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderLayerParentAbstractClientPlayerPlayerModelAbstractClientPlayer0) {
        super(renderLayerParentAbstractClientPlayerPlayerModelAbstractClientPlayer0);
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, AbstractClientPlayer abstractClientPlayer3, float float4, float float5, float float6, float float7, float float8, float float9) {
        if (abstractClientPlayer3.isCapeLoaded() && !abstractClientPlayer3.m_20145_() && abstractClientPlayer3.m_36170_(PlayerModelPart.CAPE) && abstractClientPlayer3.getCloakTextureLocation() != null) {
            ItemStack $$10 = abstractClientPlayer3.m_6844_(EquipmentSlot.CHEST);
            if (!$$10.is(Items.ELYTRA)) {
                poseStack0.pushPose();
                poseStack0.translate(0.0F, 0.0F, 0.125F);
                double $$11 = Mth.lerp((double) float6, abstractClientPlayer3.f_36102_, abstractClientPlayer3.f_36105_) - Mth.lerp((double) float6, abstractClientPlayer3.f_19854_, abstractClientPlayer3.m_20185_());
                double $$12 = Mth.lerp((double) float6, abstractClientPlayer3.f_36103_, abstractClientPlayer3.f_36106_) - Mth.lerp((double) float6, abstractClientPlayer3.f_19855_, abstractClientPlayer3.m_20186_());
                double $$13 = Mth.lerp((double) float6, abstractClientPlayer3.f_36104_, abstractClientPlayer3.f_36075_) - Mth.lerp((double) float6, abstractClientPlayer3.f_19856_, abstractClientPlayer3.m_20189_());
                float $$14 = Mth.rotLerp(float6, abstractClientPlayer3.f_20884_, abstractClientPlayer3.f_20883_);
                double $$15 = (double) Mth.sin($$14 * (float) (Math.PI / 180.0));
                double $$16 = (double) (-Mth.cos($$14 * (float) (Math.PI / 180.0)));
                float $$17 = (float) $$12 * 10.0F;
                $$17 = Mth.clamp($$17, -6.0F, 32.0F);
                float $$18 = (float) ($$11 * $$15 + $$13 * $$16) * 100.0F;
                $$18 = Mth.clamp($$18, 0.0F, 150.0F);
                float $$19 = (float) ($$11 * $$16 - $$13 * $$15) * 100.0F;
                $$19 = Mth.clamp($$19, -20.0F, 20.0F);
                if ($$18 < 0.0F) {
                    $$18 = 0.0F;
                }
                float $$20 = Mth.lerp(float6, abstractClientPlayer3.f_36099_, abstractClientPlayer3.f_36100_);
                $$17 += Mth.sin(Mth.lerp(float6, abstractClientPlayer3.f_19867_, abstractClientPlayer3.f_19787_) * 6.0F) * 32.0F * $$20;
                if (abstractClientPlayer3.m_6047_()) {
                    $$17 += 25.0F;
                }
                poseStack0.mulPose(Axis.XP.rotationDegrees(6.0F + $$18 / 2.0F + $$17));
                poseStack0.mulPose(Axis.ZP.rotationDegrees($$19 / 2.0F));
                poseStack0.mulPose(Axis.YP.rotationDegrees(180.0F - $$19 / 2.0F));
                VertexConsumer $$21 = multiBufferSource1.getBuffer(RenderType.entitySolid(abstractClientPlayer3.getCloakTextureLocation()));
                ((PlayerModel) this.m_117386_()).renderCloak(poseStack0, $$21, int2, OverlayTexture.NO_OVERLAY);
                poseStack0.popPose();
            }
        }
    }
}