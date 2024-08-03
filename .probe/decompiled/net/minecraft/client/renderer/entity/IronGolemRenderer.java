package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.layers.IronGolemCrackinessLayer;
import net.minecraft.client.renderer.entity.layers.IronGolemFlowerLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.IronGolem;

public class IronGolemRenderer extends MobRenderer<IronGolem, IronGolemModel<IronGolem>> {

    private static final ResourceLocation GOLEM_LOCATION = new ResourceLocation("textures/entity/iron_golem/iron_golem.png");

    public IronGolemRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new IronGolemModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.IRON_GOLEM)), 0.7F);
        this.m_115326_(new IronGolemCrackinessLayer(this));
        this.m_115326_(new IronGolemFlowerLayer(this, entityRendererProviderContext0.getBlockRenderDispatcher()));
    }

    public ResourceLocation getTextureLocation(IronGolem ironGolem0) {
        return GOLEM_LOCATION;
    }

    protected void setupRotations(IronGolem ironGolem0, PoseStack poseStack1, float float2, float float3, float float4) {
        super.m_7523_(ironGolem0, poseStack1, float2, float3, float4);
        if (!((double) ironGolem0.f_267362_.speed() < 0.01)) {
            float $$5 = 13.0F;
            float $$6 = ironGolem0.f_267362_.position(float4) + 6.0F;
            float $$7 = (Math.abs($$6 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            poseStack1.mulPose(Axis.ZP.rotationDegrees(6.5F * $$7));
        }
    }
}