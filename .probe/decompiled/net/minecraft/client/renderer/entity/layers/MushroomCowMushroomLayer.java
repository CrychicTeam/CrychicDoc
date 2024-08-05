package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.level.block.state.BlockState;

public class MushroomCowMushroomLayer<T extends MushroomCow> extends RenderLayer<T, CowModel<T>> {

    private final BlockRenderDispatcher blockRenderer;

    public MushroomCowMushroomLayer(RenderLayerParent<T, CowModel<T>> renderLayerParentTCowModelT0, BlockRenderDispatcher blockRenderDispatcher1) {
        super(renderLayerParentTCowModelT0);
        this.blockRenderer = blockRenderDispatcher1;
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, T t3, float float4, float float5, float float6, float float7, float float8, float float9) {
        if (!t3.m_6162_()) {
            Minecraft $$10 = Minecraft.getInstance();
            boolean $$11 = $$10.shouldEntityAppearGlowing(t3) && t3.m_20145_();
            if (!t3.m_20145_() || $$11) {
                BlockState $$12 = t3.getVariant().getBlockState();
                int $$13 = LivingEntityRenderer.getOverlayCoords(t3, 0.0F);
                BakedModel $$14 = this.blockRenderer.getBlockModel($$12);
                poseStack0.pushPose();
                poseStack0.translate(0.2F, -0.35F, 0.5F);
                poseStack0.mulPose(Axis.YP.rotationDegrees(-48.0F));
                poseStack0.scale(-1.0F, -1.0F, 1.0F);
                poseStack0.translate(-0.5F, -0.5F, -0.5F);
                this.renderMushroomBlock(poseStack0, multiBufferSource1, int2, $$11, $$12, $$13, $$14);
                poseStack0.popPose();
                poseStack0.pushPose();
                poseStack0.translate(0.2F, -0.35F, 0.5F);
                poseStack0.mulPose(Axis.YP.rotationDegrees(42.0F));
                poseStack0.translate(0.1F, 0.0F, -0.6F);
                poseStack0.mulPose(Axis.YP.rotationDegrees(-48.0F));
                poseStack0.scale(-1.0F, -1.0F, 1.0F);
                poseStack0.translate(-0.5F, -0.5F, -0.5F);
                this.renderMushroomBlock(poseStack0, multiBufferSource1, int2, $$11, $$12, $$13, $$14);
                poseStack0.popPose();
                poseStack0.pushPose();
                ((CowModel) this.m_117386_()).getHead().translateAndRotate(poseStack0);
                poseStack0.translate(0.0F, -0.7F, -0.2F);
                poseStack0.mulPose(Axis.YP.rotationDegrees(-78.0F));
                poseStack0.scale(-1.0F, -1.0F, 1.0F);
                poseStack0.translate(-0.5F, -0.5F, -0.5F);
                this.renderMushroomBlock(poseStack0, multiBufferSource1, int2, $$11, $$12, $$13, $$14);
                poseStack0.popPose();
            }
        }
    }

    private void renderMushroomBlock(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, boolean boolean3, BlockState blockState4, int int5, BakedModel bakedModel6) {
        if (boolean3) {
            this.blockRenderer.getModelRenderer().renderModel(poseStack0.last(), multiBufferSource1.getBuffer(RenderType.outline(TextureAtlas.LOCATION_BLOCKS)), blockState4, bakedModel6, 0.0F, 0.0F, 0.0F, int2, int5);
        } else {
            this.blockRenderer.renderSingleBlock(blockState4, poseStack0, multiBufferSource1, int2, int5);
        }
    }
}