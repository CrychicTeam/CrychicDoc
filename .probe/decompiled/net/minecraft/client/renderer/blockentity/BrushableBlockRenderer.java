package net.minecraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class BrushableBlockRenderer implements BlockEntityRenderer<BrushableBlockEntity> {

    private final ItemRenderer itemRenderer;

    public BrushableBlockRenderer(BlockEntityRendererProvider.Context blockEntityRendererProviderContext0) {
        this.itemRenderer = blockEntityRendererProviderContext0.getItemRenderer();
    }

    public void render(BrushableBlockEntity brushableBlockEntity0, float float1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4, int int5) {
        if (brushableBlockEntity0.m_58904_() != null) {
            int $$6 = (Integer) brushableBlockEntity0.m_58900_().m_61143_(BlockStateProperties.DUSTED);
            if ($$6 > 0) {
                Direction $$7 = brushableBlockEntity0.getHitDirection();
                if ($$7 != null) {
                    ItemStack $$8 = brushableBlockEntity0.getItem();
                    if (!$$8.isEmpty()) {
                        poseStack2.pushPose();
                        poseStack2.translate(0.0F, 0.5F, 0.0F);
                        float[] $$9 = this.translations($$7, $$6);
                        poseStack2.translate($$9[0], $$9[1], $$9[2]);
                        poseStack2.mulPose(Axis.YP.rotationDegrees(75.0F));
                        boolean $$10 = $$7 == Direction.EAST || $$7 == Direction.WEST;
                        poseStack2.mulPose(Axis.YP.rotationDegrees((float) (($$10 ? 90 : 0) + 11)));
                        poseStack2.scale(0.5F, 0.5F, 0.5F);
                        int $$11 = LevelRenderer.getLightColor(brushableBlockEntity0.m_58904_(), brushableBlockEntity0.m_58900_(), brushableBlockEntity0.m_58899_().relative($$7));
                        this.itemRenderer.renderStatic($$8, ItemDisplayContext.FIXED, $$11, OverlayTexture.NO_OVERLAY, poseStack2, multiBufferSource3, brushableBlockEntity0.m_58904_(), 0);
                        poseStack2.popPose();
                    }
                }
            }
        }
    }

    private float[] translations(Direction direction0, int int1) {
        float[] $$2 = new float[] { 0.5F, 0.0F, 0.5F };
        float $$3 = (float) int1 / 10.0F * 0.75F;
        switch(direction0) {
            case EAST:
                $$2[0] = 0.73F + $$3;
                break;
            case WEST:
                $$2[0] = 0.25F - $$3;
                break;
            case UP:
                $$2[1] = 0.25F + $$3;
                break;
            case DOWN:
                $$2[1] = -0.23F - $$3;
                break;
            case NORTH:
                $$2[2] = 0.25F - $$3;
                break;
            case SOUTH:
                $$2[2] = 0.73F + $$3;
        }
        return $$2;
    }
}