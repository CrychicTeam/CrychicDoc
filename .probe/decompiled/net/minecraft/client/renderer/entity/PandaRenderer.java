package net.minecraft.client.renderer.entity;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.client.model.PandaModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.layers.PandaHoldsItemLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Panda;

public class PandaRenderer extends MobRenderer<Panda, PandaModel<Panda>> {

    private static final Map<Panda.Gene, ResourceLocation> TEXTURES = Util.make(Maps.newEnumMap(Panda.Gene.class), p_115647_ -> {
        p_115647_.put(Panda.Gene.NORMAL, new ResourceLocation("textures/entity/panda/panda.png"));
        p_115647_.put(Panda.Gene.LAZY, new ResourceLocation("textures/entity/panda/lazy_panda.png"));
        p_115647_.put(Panda.Gene.WORRIED, new ResourceLocation("textures/entity/panda/worried_panda.png"));
        p_115647_.put(Panda.Gene.PLAYFUL, new ResourceLocation("textures/entity/panda/playful_panda.png"));
        p_115647_.put(Panda.Gene.BROWN, new ResourceLocation("textures/entity/panda/brown_panda.png"));
        p_115647_.put(Panda.Gene.WEAK, new ResourceLocation("textures/entity/panda/weak_panda.png"));
        p_115647_.put(Panda.Gene.AGGRESSIVE, new ResourceLocation("textures/entity/panda/aggressive_panda.png"));
    });

    public PandaRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new PandaModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.PANDA)), 0.9F);
        this.m_115326_(new PandaHoldsItemLayer(this, entityRendererProviderContext0.getItemInHandRenderer()));
    }

    public ResourceLocation getTextureLocation(Panda panda0) {
        return (ResourceLocation) TEXTURES.getOrDefault(panda0.getVariant(), (ResourceLocation) TEXTURES.get(Panda.Gene.NORMAL));
    }

    protected void setupRotations(Panda panda0, PoseStack poseStack1, float float2, float float3, float float4) {
        super.m_7523_(panda0, poseStack1, float2, float3, float4);
        if (panda0.rollCounter > 0) {
            int $$5 = panda0.rollCounter;
            int $$6 = $$5 + 1;
            float $$7 = 7.0F;
            float $$8 = panda0.m_6162_() ? 0.3F : 0.8F;
            if ($$5 < 8) {
                float $$9 = (float) (90 * $$5) / 7.0F;
                float $$10 = (float) (90 * $$6) / 7.0F;
                float $$11 = this.getAngle($$9, $$10, $$6, float4, 8.0F);
                poseStack1.translate(0.0F, ($$8 + 0.2F) * ($$11 / 90.0F), 0.0F);
                poseStack1.mulPose(Axis.XP.rotationDegrees(-$$11));
            } else if ($$5 < 16) {
                float $$12 = ((float) $$5 - 8.0F) / 7.0F;
                float $$13 = 90.0F + 90.0F * $$12;
                float $$14 = 90.0F + 90.0F * ((float) $$6 - 8.0F) / 7.0F;
                float $$15 = this.getAngle($$13, $$14, $$6, float4, 16.0F);
                poseStack1.translate(0.0F, $$8 + 0.2F + ($$8 - 0.2F) * ($$15 - 90.0F) / 90.0F, 0.0F);
                poseStack1.mulPose(Axis.XP.rotationDegrees(-$$15));
            } else if ((float) $$5 < 24.0F) {
                float $$16 = ((float) $$5 - 16.0F) / 7.0F;
                float $$17 = 180.0F + 90.0F * $$16;
                float $$18 = 180.0F + 90.0F * ((float) $$6 - 16.0F) / 7.0F;
                float $$19 = this.getAngle($$17, $$18, $$6, float4, 24.0F);
                poseStack1.translate(0.0F, $$8 + $$8 * (270.0F - $$19) / 90.0F, 0.0F);
                poseStack1.mulPose(Axis.XP.rotationDegrees(-$$19));
            } else if ($$5 < 32) {
                float $$20 = ((float) $$5 - 24.0F) / 7.0F;
                float $$21 = 270.0F + 90.0F * $$20;
                float $$22 = 270.0F + 90.0F * ((float) $$6 - 24.0F) / 7.0F;
                float $$23 = this.getAngle($$21, $$22, $$6, float4, 32.0F);
                poseStack1.translate(0.0F, $$8 * ((360.0F - $$23) / 90.0F), 0.0F);
                poseStack1.mulPose(Axis.XP.rotationDegrees(-$$23));
            }
        }
        float $$24 = panda0.getSitAmount(float4);
        if ($$24 > 0.0F) {
            poseStack1.translate(0.0F, 0.8F * $$24, 0.0F);
            poseStack1.mulPose(Axis.XP.rotationDegrees(Mth.lerp($$24, panda0.m_146909_(), panda0.m_146909_() + 90.0F)));
            poseStack1.translate(0.0F, -1.0F * $$24, 0.0F);
            if (panda0.isScared()) {
                float $$25 = (float) (Math.cos((double) panda0.f_19797_ * 1.25) * Math.PI * 0.05F);
                poseStack1.mulPose(Axis.YP.rotationDegrees($$25));
                if (panda0.m_6162_()) {
                    poseStack1.translate(0.0F, 0.8F, 0.55F);
                }
            }
        }
        float $$26 = panda0.getLieOnBackAmount(float4);
        if ($$26 > 0.0F) {
            float $$27 = panda0.m_6162_() ? 0.5F : 1.3F;
            poseStack1.translate(0.0F, $$27 * $$26, 0.0F);
            poseStack1.mulPose(Axis.XP.rotationDegrees(Mth.lerp($$26, panda0.m_146909_(), panda0.m_146909_() + 180.0F)));
        }
    }

    private float getAngle(float float0, float float1, int int2, float float3, float float4) {
        return (float) int2 < float4 ? Mth.lerp(float3, float0, float1) : float0;
    }
}