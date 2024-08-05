package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class CatRenderer extends MobRenderer<Cat, CatModel<Cat>> {

    public CatRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new CatModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.CAT)), 0.4F);
        this.m_115326_(new CatCollarLayer(this, entityRendererProviderContext0.getModelSet()));
    }

    public ResourceLocation getTextureLocation(Cat cat0) {
        return cat0.getResourceLocation();
    }

    protected void scale(Cat cat0, PoseStack poseStack1, float float2) {
        super.m_7546_(cat0, poseStack1, float2);
        poseStack1.scale(0.8F, 0.8F, 0.8F);
    }

    protected void setupRotations(Cat cat0, PoseStack poseStack1, float float2, float float3, float float4) {
        super.m_7523_(cat0, poseStack1, float2, float3, float4);
        float $$5 = cat0.getLieDownAmount(float4);
        if ($$5 > 0.0F) {
            poseStack1.translate(0.4F * $$5, 0.15F * $$5, 0.1F * $$5);
            poseStack1.mulPose(Axis.ZP.rotationDegrees(Mth.rotLerp($$5, 0.0F, 90.0F)));
            BlockPos $$6 = cat0.m_20183_();
            for (Player $$8 : cat0.m_9236_().m_45976_(Player.class, new AABB($$6).inflate(2.0, 2.0, 2.0))) {
                if ($$8.m_5803_()) {
                    poseStack1.translate(0.15F * $$5, 0.0F, 0.0F);
                    break;
                }
            }
        }
    }
}