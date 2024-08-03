package com.mna.blocks.tileentities.renderers;

import com.mna.blocks.decoration.ScrollshelfBlock;
import com.mna.blocks.tileentities.BookshelfTile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BookshelfRenderer implements BlockEntityRenderer<BookshelfTile> {

    private final ItemRenderer itemRenderer;

    private final Minecraft mc = Minecraft.getInstance();

    public BookshelfRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = this.mc.getItemRenderer();
    }

    public void render(BookshelfTile pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        BlockState state = pBlockEntity.m_58900_();
        pPoseStack.pushPose();
        switch((Direction) state.m_61143_(ScrollshelfBlock.FACING)) {
            case EAST:
                pPoseStack.translate(1.0F, 0.0F, 1.0F);
                pPoseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
                break;
            case SOUTH:
                pPoseStack.translate(0.0F, 0.0F, 1.0F);
                pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                break;
            case WEST:
                pPoseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
                break;
            default:
                pPoseStack.translate(1.0F, 0.0F, 0.0F);
        }
        pPoseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        pPoseStack.translate(0.55, 1.4325, 0.2125);
        int idx = 0;
        for (int col = 0; col < 5; col++) {
            ItemStack stack = pBlockEntity.getDisplayItem(idx++);
            double xOffset = 0.15 * (double) col;
            if (!stack.isEmpty()) {
                pPoseStack.pushPose();
                pPoseStack.translate(0.0, 0.0, xOffset);
                pPoseStack.scale(0.7F, 0.7F, 0.7F);
                this.itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, pBufferSource, this.mc.level, 0);
                pPoseStack.popPose();
            }
            stack = pBlockEntity.getDisplayItem(idx++);
            if (!stack.isEmpty()) {
                pPoseStack.pushPose();
                pPoseStack.translate(0.0, -0.5, xOffset);
                pPoseStack.scale(0.7F, 0.7F, 0.7F);
                this.itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, pBufferSource, this.mc.level, 0);
                pPoseStack.popPose();
            }
            stack = pBlockEntity.getDisplayItem(idx++);
            if (!stack.isEmpty()) {
                pPoseStack.pushPose();
                pPoseStack.translate(0.0, -1.065, xOffset);
                pPoseStack.scale(0.7F, 0.7F, 0.7F);
                this.itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, pBufferSource, this.mc.level, 0);
                pPoseStack.popPose();
            }
        }
        pPoseStack.popPose();
    }
}