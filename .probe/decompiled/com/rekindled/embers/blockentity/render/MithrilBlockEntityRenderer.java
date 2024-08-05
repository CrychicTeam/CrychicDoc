package com.rekindled.embers.blockentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rekindled.embers.blockentity.MithrilBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class MithrilBlockEntityRenderer implements BlockEntityRenderer<MithrilBlockEntity> {

    private final ItemRenderer itemRenderer;

    public MithrilBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
        this.itemRenderer = pContext.getItemRenderer();
    }

    public void render(MithrilBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (blockEntity != null) {
            ItemStack block = new ItemStack(blockEntity.m_58904_().getBlockState(blockEntity.m_58899_()).m_60734_());
            int seed = block.isEmpty() ? 187 : Item.getId(block.getItem()) + block.getDamageValue();
            BakedModel bakedmodel = this.itemRenderer.getModel(block, blockEntity.m_58904_(), null, seed);
            poseStack.translate(0.5, 0.5, 0.5);
            this.itemRenderer.render(block, ItemDisplayContext.NONE, false, poseStack, bufferSource, packedLight, packedOverlay, bakedmodel);
        }
    }
}