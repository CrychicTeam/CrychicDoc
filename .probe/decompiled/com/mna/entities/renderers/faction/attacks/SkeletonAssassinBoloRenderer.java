package com.mna.entities.renderers.faction.attacks;

import com.mna.entities.models.projectile.EntitySkeletonAssassinBoloModel;
import com.mna.entities.projectile.SkeletonAssassinBolo;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SkeletonAssassinBoloRenderer extends GeoEntityRenderer<SkeletonAssassinBolo> {

    public SkeletonAssassinBoloRenderer(EntityRendererProvider.Context context) {
        super(context, new EntitySkeletonAssassinBoloModel());
    }

    public void render(SkeletonAssassinBolo entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        float ticks = ((float) entity.f_19797_ + partialTicks) * 50.0F;
        stack.pushPose();
        stack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.f_19859_, entity.m_146908_()) - 90.0F));
        stack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.f_19860_, entity.m_146909_())));
        if (!entity.isInGround()) {
            stack.mulPose(Axis.YP.rotationDegrees(ticks));
        }
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        stack.popPose();
    }
}