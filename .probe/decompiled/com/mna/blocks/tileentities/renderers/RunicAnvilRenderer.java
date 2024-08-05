package com.mna.blocks.tileentities.renderers;

import com.mna.api.tools.RLoc;
import com.mna.blocks.tileentities.RunicAnvilTile;
import com.mna.blocks.tileentities.models.RunicAnvilModel;
import com.mna.tools.render.ModelUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.util.RenderUtils;

public class RunicAnvilRenderer extends GeoBlockRenderer<RunicAnvilTile> {

    public static final ResourceLocation ring_small = RLoc.create("block/runic_anvil/ring_small");

    public static final ResourceLocation ring_large = RLoc.create("block/runic_anvil/ring_large");

    private final ItemRenderer itemRenderer;

    protected Minecraft mc = Minecraft.getInstance();

    public RunicAnvilRenderer(BlockEntityRendererProvider.Context context) {
        super(new RunicAnvilModel());
        this.itemRenderer = this.mc.getItemRenderer();
    }

    public void actuallyRender(PoseStack poseStack, RunicAnvilTile animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.renderItems(animatable, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void renderRecursively(PoseStack stack, RunicAnvilTile animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (this.mc != null) {
            stack.pushPose();
            RenderUtils.translateMatrixToBone(stack, bone);
            RenderUtils.translateToPivotPoint(stack, bone);
            RenderUtils.rotateMatrixAroundBone(stack, bone);
            RenderUtils.scaleMatrixForBone(stack, bone);
            BlockState state = animatable.m_58900_();
            BlockPos pos = animatable.m_58899_();
            if (!bone.isHidden()) {
                stack.pushPose();
                String var17 = bone.getName();
                switch(var17) {
                    case "CIRCLE_BASE":
                        ModelUtils.renderModel(bufferSource, this.mc.level, pos, state, ring_large, stack, packedLight, packedOverlay);
                        break;
                    case "CIRCLE_FRONT":
                    case "CIRCLE_REAR":
                        ModelUtils.renderModel(bufferSource, this.mc.level, pos, state, ring_small, stack, packedLight, packedOverlay);
                }
                stack.popPose();
                for (GeoBone childBone : bone.getChildBones()) {
                    this.renderRecursively(stack, animatable, childBone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
                }
            }
            stack.popPose();
        }
    }

    private void renderItems(RunicAnvilTile anvil, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ItemStack[] stacks = anvil.getDisplayedItems();
        float offset = 0.03F;
        float craftPct = (float) anvil.craftProgress / (float) anvil.getMaxCraftProgress();
        matrixStackIn.pushPose();
        if (!stacks[0].isEmpty()) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5, 1.025, 0.5);
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            this.itemRenderer.renderStatic(stacks[0], ItemDisplayContext.FIXED, combinedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, this.mc.level, 0);
            matrixStackIn.popPose();
        }
        if (!stacks[1].isEmpty()) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5, 1.025 + (double) (offset - offset * craftPct), 0.5);
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            this.itemRenderer.renderStatic(stacks[1], ItemDisplayContext.FIXED, combinedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, this.mc.level, 0);
            matrixStackIn.popPose();
        }
        matrixStackIn.popPose();
    }
}