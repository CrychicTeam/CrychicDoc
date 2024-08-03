package com.mna.blocks.tileentities.renderers.wizard_lab;

import com.mna.blocks.tileentities.models.DisenchanterModel;
import com.mna.blocks.tileentities.wizard_lab.DisenchanterTile;
import com.mna.tools.render.MARenderTypes;
import com.mna.tools.render.WorldRenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class DisenchanterRenderer extends WizardLabRenderer<DisenchanterTile> {

    public DisenchanterRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(rendererDispatcherIn, new DisenchanterModel());
    }

    public void actuallyRender(PoseStack poseStack, DisenchanterTile animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (animatable.isActive() && (double) animatable.getPctComplete() > 0.9) {
            float beamPct = (animatable.getPctComplete() - 0.9F) / 0.1F;
            poseStack.pushPose();
            Vec3 start = Vec3.upFromBottomCenterOf(animatable.m_58899_(), 1.0);
            Vec3 end = Vec3.upFromBottomCenterOf(animatable.m_58899_(), 2.0);
            poseStack.translate(0.5, 2.0, 0.5);
            WorldRenderUtils.renderBeam(animatable.m_58904_(), partialTick, poseStack, bufferSource, packedLight, end, start, beamPct, new int[] { 200, 200, 255 }, 255, 0.065F, MARenderTypes.RITUAL_BEAM_RENDER_TYPE);
            poseStack.popPose();
        }
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}