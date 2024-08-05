package com.mna.blocks.tileentities.renderers;

import com.mna.blocks.runeforging.RuneforgeBlock;
import com.mna.blocks.tileentities.RuneForgeTile;
import com.mna.tools.render.WorldRenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class RuneforgeRenderer implements BlockEntityRenderer<RuneForgeTile> {

    private final Minecraft mc = Minecraft.getInstance();

    private final ItemRenderer itemRenderer = this.mc.getItemRenderer();

    public RuneforgeRenderer(BlockEntityRendererProvider.Context context) {
    }

    public void render(RuneForgeTile tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ItemStack itemstack = tileEntityIn.getDisplayedItem();
        if (!itemstack.isEmpty()) {
            matrixStackIn.pushPose();
            Direction dir = (Direction) tileEntityIn.m_58900_().m_61143_(RuneforgeBlock.FACING);
            matrixStackIn.translate(0.5, 1.05, 0.5);
            switch(dir) {
                case NORTH:
                default:
                    matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
                    break;
                case SOUTH:
                    matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
                    matrixStackIn.mulPose(Axis.ZP.rotationDegrees(180.0F));
                    break;
                case EAST:
                    matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
                    matrixStackIn.mulPose(Axis.ZP.rotationDegrees(90.0F));
                    break;
                case WEST:
                    matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
                    matrixStackIn.mulPose(Axis.ZP.rotationDegrees(270.0F));
            }
            matrixStackIn.scale(0.3F, 0.3F, 0.3F);
            this.itemRenderer.renderStatic(itemstack, ItemDisplayContext.FIXED, combinedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, this.mc.level, 0);
            matrixStackIn.popPose();
            if ((Boolean) tileEntityIn.m_58900_().m_61143_(RuneforgeBlock.ACTIVE)) {
                Minecraft mc = Minecraft.getInstance();
                if (mc.hitResult == null || mc.hitResult.getType() != HitResult.Type.BLOCK) {
                    return;
                }
                BlockHitResult brtr = (BlockHitResult) mc.hitResult;
                if (!brtr.getBlockPos().equals(tileEntityIn.m_58899_())) {
                    return;
                }
                float pct = 1.0F;
                if ((Boolean) tileEntityIn.m_58900_().m_61143_(RuneforgeBlock.REPAIR)) {
                    int damage = tileEntityIn.m_8020_(0).getDamageValue();
                    int maxDamage = tileEntityIn.m_8020_(0).getMaxDamage();
                    int durabilityRemaining = maxDamage - damage;
                    pct = (float) durabilityRemaining / (float) maxDamage;
                } else {
                    pct = tileEntityIn.getBurnPct();
                }
                int color = Mth.hsvToRgb(pct / 3.0F, 1.0F, 1.0F) | 0xFF000000;
                int r = FastColor.ARGB32.red(color) / 3 * 2;
                int g = FastColor.ARGB32.green(color) / 3 * 2;
                int b = FastColor.ARGB32.blue(color) / 3 * 2;
                for (int i = 0; i < 4; i++) {
                    matrixStackIn.pushPose();
                    matrixStackIn.translate(0.5, 0.0, 0.5);
                    matrixStackIn.mulPose(Axis.YP.rotationDegrees((float) (i * 90)));
                    matrixStackIn.translate(0.0, 0.90625, -0.377);
                    matrixStackIn.scale(0.44F, 0.05F, 1.0F);
                    WorldRenderUtils.renderProgressBar(matrixStackIn, bufferIn, pct, new int[] { r, g, b }, 255);
                    matrixStackIn.popPose();
                }
            }
        }
    }
}