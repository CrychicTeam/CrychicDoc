package io.redspace.ironsspellbooks.entity.spells.root;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.render.GeoLivingEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class RootRenderer extends GeoLivingEntityRenderer<RootEntity> {

    public RootRenderer(EntityRendererProvider.Context context) {
        super(context, new RootModel());
    }

    public void preRender(PoseStack poseStack, RootEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Entity rooted = animatable.m_146895_();
        if (rooted != null) {
            float scale = rooted.getBbWidth() / 0.6F;
            poseStack.scale(scale, scale, scale);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}