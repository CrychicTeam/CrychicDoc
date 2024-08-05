package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.FoxModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.layers.FoxHeldItemLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Fox;

public class FoxRenderer extends MobRenderer<Fox, FoxModel<Fox>> {

    private static final ResourceLocation RED_FOX_TEXTURE = new ResourceLocation("textures/entity/fox/fox.png");

    private static final ResourceLocation RED_FOX_SLEEP_TEXTURE = new ResourceLocation("textures/entity/fox/fox_sleep.png");

    private static final ResourceLocation SNOW_FOX_TEXTURE = new ResourceLocation("textures/entity/fox/snow_fox.png");

    private static final ResourceLocation SNOW_FOX_SLEEP_TEXTURE = new ResourceLocation("textures/entity/fox/snow_fox_sleep.png");

    public FoxRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new FoxModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.FOX)), 0.4F);
        this.m_115326_(new FoxHeldItemLayer(this, entityRendererProviderContext0.getItemInHandRenderer()));
    }

    protected void setupRotations(Fox fox0, PoseStack poseStack1, float float2, float float3, float float4) {
        super.m_7523_(fox0, poseStack1, float2, float3, float4);
        if (fox0.isPouncing() || fox0.isFaceplanted()) {
            float $$5 = -Mth.lerp(float4, fox0.f_19860_, fox0.m_146909_());
            poseStack1.mulPose(Axis.XP.rotationDegrees($$5));
        }
    }

    public ResourceLocation getTextureLocation(Fox fox0) {
        if (fox0.getVariant() == Fox.Type.RED) {
            return fox0.isSleeping() ? RED_FOX_SLEEP_TEXTURE : RED_FOX_TEXTURE;
        } else {
            return fox0.isSleeping() ? SNOW_FOX_SLEEP_TEXTURE : SNOW_FOX_TEXTURE;
        }
    }
}