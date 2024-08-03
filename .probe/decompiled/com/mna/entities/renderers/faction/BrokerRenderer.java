package com.mna.entities.renderers.faction;

import com.mna.entities.faction.Broker;
import com.mna.entities.models.faction.BrokerModel;
import com.mna.events.ClientEventHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BrokerRenderer extends GeoEntityRenderer<Broker> {

    public BrokerRenderer(EntityRendererProvider.Context context) {
        super(context, new BrokerModel());
    }

    public RenderType getRenderType(Broker animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entitySmoothCutout(texture);
    }

    @Override
    public boolean firePreRenderEvent(PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        ClientEventHandler.setFogAmount(Math.min(ClientEventHandler.getFogAmount() + 0.025F, 0.85F));
        ClientEventHandler.fogColor[0] = 0.85F;
        ClientEventHandler.fogColor[1] = 0.85F;
        ClientEventHandler.fogColor[2] = 0.85F;
        return super.firePreRenderEvent(poseStack, model, bufferSource, partialTick, packedLight);
    }
}