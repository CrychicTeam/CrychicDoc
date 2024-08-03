package com.mna.entities.renderers.faction;

import com.mna.entities.faction.SkeletonAssassin;
import com.mna.entities.models.faction.SkeletonAssassinModel;
import com.mna.entities.renderers.MAGeckoRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SkeletonAssassinRenderer extends MAGeckoRenderer<SkeletonAssassin> {

    public SkeletonAssassinRenderer(EntityRendererProvider.Context context) {
        super(context, new SkeletonAssassinModel());
    }

    public void render(SkeletonAssassin entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        stack.mulPose(Axis.YP.rotationDegrees(180.0F));
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }
}