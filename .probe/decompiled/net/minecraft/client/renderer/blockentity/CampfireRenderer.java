package net.minecraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;

public class CampfireRenderer implements BlockEntityRenderer<CampfireBlockEntity> {

    private static final float SIZE = 0.375F;

    private final ItemRenderer itemRenderer;

    public CampfireRenderer(BlockEntityRendererProvider.Context blockEntityRendererProviderContext0) {
        this.itemRenderer = blockEntityRendererProviderContext0.getItemRenderer();
    }

    public void render(CampfireBlockEntity campfireBlockEntity0, float float1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4, int int5) {
        Direction $$6 = (Direction) campfireBlockEntity0.m_58900_().m_61143_(CampfireBlock.FACING);
        NonNullList<ItemStack> $$7 = campfireBlockEntity0.getItems();
        int $$8 = (int) campfireBlockEntity0.m_58899_().asLong();
        for (int $$9 = 0; $$9 < $$7.size(); $$9++) {
            ItemStack $$10 = $$7.get($$9);
            if ($$10 != ItemStack.EMPTY) {
                poseStack2.pushPose();
                poseStack2.translate(0.5F, 0.44921875F, 0.5F);
                Direction $$11 = Direction.from2DDataValue(($$9 + $$6.get2DDataValue()) % 4);
                float $$12 = -$$11.toYRot();
                poseStack2.mulPose(Axis.YP.rotationDegrees($$12));
                poseStack2.mulPose(Axis.XP.rotationDegrees(90.0F));
                poseStack2.translate(-0.3125F, -0.3125F, 0.0F);
                poseStack2.scale(0.375F, 0.375F, 0.375F);
                this.itemRenderer.renderStatic($$10, ItemDisplayContext.FIXED, int4, int5, poseStack2, multiBufferSource3, campfireBlockEntity0.m_58904_(), $$8 + $$9);
                poseStack2.popPose();
            }
        }
    }
}