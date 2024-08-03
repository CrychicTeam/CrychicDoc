package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.PhantomModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.layers.PhantomEyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Phantom;

public class PhantomRenderer extends MobRenderer<Phantom, PhantomModel<Phantom>> {

    private static final ResourceLocation PHANTOM_LOCATION = new ResourceLocation("textures/entity/phantom.png");

    public PhantomRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new PhantomModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.PHANTOM)), 0.75F);
        this.m_115326_(new PhantomEyesLayer<>(this));
    }

    public ResourceLocation getTextureLocation(Phantom phantom0) {
        return PHANTOM_LOCATION;
    }

    protected void scale(Phantom phantom0, PoseStack poseStack1, float float2) {
        int $$3 = phantom0.getPhantomSize();
        float $$4 = 1.0F + 0.15F * (float) $$3;
        poseStack1.scale($$4, $$4, $$4);
        poseStack1.translate(0.0F, 1.3125F, 0.1875F);
    }

    protected void setupRotations(Phantom phantom0, PoseStack poseStack1, float float2, float float3, float float4) {
        super.m_7523_(phantom0, poseStack1, float2, float3, float4);
        poseStack1.mulPose(Axis.XP.rotationDegrees(phantom0.m_146909_()));
    }
}