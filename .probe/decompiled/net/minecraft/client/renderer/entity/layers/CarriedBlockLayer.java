package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.block.state.BlockState;

public class CarriedBlockLayer extends RenderLayer<EnderMan, EndermanModel<EnderMan>> {

    private final BlockRenderDispatcher blockRenderer;

    public CarriedBlockLayer(RenderLayerParent<EnderMan, EndermanModel<EnderMan>> renderLayerParentEnderManEndermanModelEnderMan0, BlockRenderDispatcher blockRenderDispatcher1) {
        super(renderLayerParentEnderManEndermanModelEnderMan0);
        this.blockRenderer = blockRenderDispatcher1;
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, EnderMan enderMan3, float float4, float float5, float float6, float float7, float float8, float float9) {
        BlockState $$10 = enderMan3.getCarriedBlock();
        if ($$10 != null) {
            poseStack0.pushPose();
            poseStack0.translate(0.0F, 0.6875F, -0.75F);
            poseStack0.mulPose(Axis.XP.rotationDegrees(20.0F));
            poseStack0.mulPose(Axis.YP.rotationDegrees(45.0F));
            poseStack0.translate(0.25F, 0.1875F, 0.25F);
            float $$11 = 0.5F;
            poseStack0.scale(-0.5F, -0.5F, 0.5F);
            poseStack0.mulPose(Axis.YP.rotationDegrees(90.0F));
            this.blockRenderer.renderSingleBlock($$10, poseStack0, multiBufferSource1, int2, OverlayTexture.NO_OVERLAY);
            poseStack0.popPose();
        }
    }
}