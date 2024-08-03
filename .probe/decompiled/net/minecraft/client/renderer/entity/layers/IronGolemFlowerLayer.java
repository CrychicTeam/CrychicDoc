package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.level.block.Blocks;

public class IronGolemFlowerLayer extends RenderLayer<IronGolem, IronGolemModel<IronGolem>> {

    private final BlockRenderDispatcher blockRenderer;

    public IronGolemFlowerLayer(RenderLayerParent<IronGolem, IronGolemModel<IronGolem>> renderLayerParentIronGolemIronGolemModelIronGolem0, BlockRenderDispatcher blockRenderDispatcher1) {
        super(renderLayerParentIronGolemIronGolemModelIronGolem0);
        this.blockRenderer = blockRenderDispatcher1;
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, IronGolem ironGolem3, float float4, float float5, float float6, float float7, float float8, float float9) {
        if (ironGolem3.getOfferFlowerTick() != 0) {
            poseStack0.pushPose();
            ModelPart $$10 = ((IronGolemModel) this.m_117386_()).getFlowerHoldingArm();
            $$10.translateAndRotate(poseStack0);
            poseStack0.translate(-1.1875F, 1.0625F, -0.9375F);
            poseStack0.translate(0.5F, 0.5F, 0.5F);
            float $$11 = 0.5F;
            poseStack0.scale(0.5F, 0.5F, 0.5F);
            poseStack0.mulPose(Axis.XP.rotationDegrees(-90.0F));
            poseStack0.translate(-0.5F, -0.5F, -0.5F);
            this.blockRenderer.renderSingleBlock(Blocks.POPPY.defaultBlockState(), poseStack0, multiBufferSource1, int2, OverlayTexture.NO_OVERLAY);
            poseStack0.popPose();
        }
    }
}