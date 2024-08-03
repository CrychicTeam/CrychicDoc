package com.mna.blocks.tileentities.renderers;

import com.mna.blocks.tileentities.ChalkRuneTile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ChalkRuneRenderer implements BlockEntityRenderer<ChalkRuneTile> {

    private final ItemRenderer itemRenderer;

    private final Minecraft mc = Minecraft.getInstance();

    public ChalkRuneRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = this.mc.getItemRenderer();
    }

    public void render(ChalkRuneTile tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ItemStack itemstack = tileEntityIn.getDisplayedItem();
        if (!itemstack.isEmpty()) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5, 0.075, 0.5);
            if (itemstack.getItem() instanceof BlockItem) {
                matrixStackIn.translate(0.0F, 0.1F, 0.0F);
            } else {
                matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
            }
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            this.itemRenderer.renderStatic(itemstack, ItemDisplayContext.FIXED, combinedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, this.mc.level, 0);
            matrixStackIn.popPose();
        }
    }
}