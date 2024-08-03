package com.rekindled.embers.blockentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rekindled.embers.blockentity.StampBaseBlockEntity;
import com.rekindled.embers.render.FluidCuboid;
import com.rekindled.embers.render.FluidRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.joml.Vector3f;

public class StampBaseBlockEntityRenderer implements BlockEntityRenderer<StampBaseBlockEntity> {

    private final ItemRenderer itemRenderer;

    FluidCuboid cube = new FluidCuboid(new Vector3f(4.0F, 12.0F, 4.0F), new Vector3f(12.0F, 15.0F, 12.0F), FluidCuboid.DEFAULT_FACES);

    public StampBaseBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
        this.itemRenderer = pContext.getItemRenderer();
    }

    public void render(StampBaseBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (blockEntity != null) {
            if (!blockEntity.inventory.getStackInSlot(0).isEmpty()) {
                poseStack.pushPose();
                ItemStack stack = blockEntity.inventory.getStackInSlot(0);
                int seed = stack.isEmpty() ? 187 : Item.getId(stack.getItem()) + stack.getDamageValue();
                BakedModel bakedmodel = this.itemRenderer.getModel(stack, blockEntity.m_58904_(), null, seed);
                float f2 = bakedmodel.getTransforms().getTransform(ItemDisplayContext.GROUND).scale.y();
                poseStack.translate(0.5, (double) (0.25F * f2) + 0.75, 0.5);
                this.itemRenderer.render(stack, ItemDisplayContext.GROUND, false, poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY, bakedmodel);
                poseStack.popPose();
            }
            FluidStack fluidStack = blockEntity.getFluidStack();
            int capacity = blockEntity.getCapacity();
            if (!fluidStack.isEmpty() && capacity > 0) {
                float offset = blockEntity.renderOffset;
                if (!(offset > 1.2F) && !(offset < -1.2F)) {
                    blockEntity.renderOffset = 0.0F;
                } else {
                    offset -= (offset / 12.0F + 0.1F) * partialTick;
                    blockEntity.renderOffset = offset;
                }
                FluidRenderer.renderScaledCuboid(poseStack, bufferSource, this.cube, fluidStack, offset, capacity, packedLight, packedOverlay, false);
            } else {
                blockEntity.renderOffset = 0.0F;
            }
        }
    }
}