package com.mna.blocks.tileentities.renderers;

import com.mna.blocks.runeforging.PedestalBlock;
import com.mna.blocks.tileentities.PedestalTile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class PedestalRenderer implements BlockEntityRenderer<PedestalTile> {

    private final Minecraft mc = Minecraft.getInstance();

    private final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

    private final EntityRenderDispatcher renderDispatcher = this.mc.getEntityRenderDispatcher();

    private final Font font;

    public PedestalRenderer(BlockEntityRendererProvider.Context context) {
        this.font = this.mc.font;
    }

    public void render(PedestalTile tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ItemStack itemstack = tileEntityIn.getDisplayedItem();
        if (!itemstack.isEmpty()) {
            matrixStackIn.pushPose();
            if (itemstack.getItem() instanceof BlockItem) {
                matrixStackIn.translate(0.5, 1.2, 0.5);
            } else {
                matrixStackIn.translate(0.5, 1.0, 0.5);
                matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
            }
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            this.itemRenderer.renderStatic(itemstack, ItemDisplayContext.FIXED, combinedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, this.mc.level, 0);
            matrixStackIn.popPose();
            this.renderHoverText(tileEntityIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
    }

    private void renderHoverText(PedestalTile tileEntityIn, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if ((Boolean) tileEntityIn.m_58900_().m_61143_(PedestalBlock.SHOW_SIGN)) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.hitResult != null && mc.hitResult.getType() == HitResult.Type.BLOCK) {
                BlockHitResult brtr = (BlockHitResult) mc.hitResult;
                if (brtr.getBlockPos().equals(tileEntityIn.m_58899_())) {
                    if (brtr.getDirection().equals(tileEntityIn.m_58900_().m_61143_(HorizontalDirectionalBlock.FACING))) {
                        Vec3 dist = mc.player.m_20182_().subtract(Vec3.atCenterOf(tileEntityIn.m_58899_())).normalize().scale(0.5);
                        matrixStackIn.pushPose();
                        matrixStackIn.translate(0.5, 1.1, 0.5);
                        matrixStackIn.translate(dist.x, dist.y, dist.z);
                        matrixStackIn.scale(0.5F, 0.5F, 0.5F);
                        this.renderString(tileEntityIn.getDisplayedItem().getDisplayName().getString(), matrixStackIn, bufferIn, combinedLightIn);
                        matrixStackIn.popPose();
                    }
                }
            }
        }
    }

    protected void renderString(String text, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.mulPose(this.renderDispatcher.cameraOrientation());
        stack.scale(-0.025F, -0.025F, 0.025F);
        Matrix4f matrix4f = stack.last().pose();
        float opacity = 0.25F;
        int textColor = (int) (opacity * 255.0F) << 24;
        float hOffset = (float) (-this.font.width(text) / 2);
        this.font.drawInBatch(text, hOffset, 0.0F, 553648127, false, matrix4f, buffer, Font.DisplayMode.NORMAL, textColor, packedLight);
        this.font.drawInBatch(text, hOffset, 0.0F, -1, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, packedLight);
        stack.popPose();
    }
}