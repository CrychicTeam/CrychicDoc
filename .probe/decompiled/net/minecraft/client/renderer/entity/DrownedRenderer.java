package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.DrownedModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.layers.DrownedOuterLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Zombie;

public class DrownedRenderer extends AbstractZombieRenderer<Drowned, DrownedModel<Drowned>> {

    private static final ResourceLocation DROWNED_LOCATION = new ResourceLocation("textures/entity/zombie/drowned.png");

    public DrownedRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new DrownedModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.DROWNED)), new DrownedModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.DROWNED_INNER_ARMOR)), new DrownedModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.DROWNED_OUTER_ARMOR)));
        this.m_115326_(new DrownedOuterLayer<>(this, entityRendererProviderContext0.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(Zombie zombie0) {
        return DROWNED_LOCATION;
    }

    protected void setupRotations(Drowned drowned0, PoseStack poseStack1, float float2, float float3, float float4) {
        super.m_7523_(drowned0, poseStack1, float2, float3, float4);
        float $$5 = drowned0.m_20998_(float4);
        if ($$5 > 0.0F) {
            float $$6 = -10.0F - drowned0.m_146909_();
            float $$7 = Mth.lerp($$5, 0.0F, $$6);
            poseStack1.rotateAround(Axis.XP.rotationDegrees($$7), 0.0F, drowned0.m_20206_() / 2.0F, 0.0F);
        }
    }
}