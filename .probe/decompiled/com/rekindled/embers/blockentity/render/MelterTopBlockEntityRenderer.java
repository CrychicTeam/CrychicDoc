package com.rekindled.embers.blockentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.rekindled.embers.blockentity.MelterTopBlockEntity;
import com.rekindled.embers.render.FluidCuboid;
import com.rekindled.embers.render.FluidRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.joml.Vector3f;

public class MelterTopBlockEntityRenderer implements BlockEntityRenderer<MelterTopBlockEntity> {

    private final ItemRenderer itemRenderer;

    private final RandomSource random = RandomSource.create();

    FluidCuboid cube = new FluidCuboid(new Vector3f(4.0F, 0.0F, 4.0F), new Vector3f(12.0F, 14.0F, 12.0F), FluidCuboid.DEFAULT_FACES);

    public MelterTopBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
        this.itemRenderer = pContext.getItemRenderer();
    }

    public void render(MelterTopBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (blockEntity != null) {
            for (int i = 0; i < blockEntity.inventory.getSlots(); i++) {
                if (!blockEntity.inventory.getStackInSlot(i).isEmpty()) {
                    poseStack.pushPose();
                    ItemStack stack = blockEntity.inventory.getStackInSlot(i);
                    int seed = stack.isEmpty() ? 187 : Item.getId(stack.getItem()) + stack.getDamageValue();
                    this.random.setSeed((long) seed);
                    BakedModel bakedmodel = this.itemRenderer.getModel(stack, blockEntity.m_58904_(), null, seed);
                    boolean flag = bakedmodel.isGui3d();
                    int j = this.getRenderAmount(stack);
                    float f2 = bakedmodel.getTransforms().getTransform(ItemDisplayContext.GROUND).scale.y();
                    poseStack.translate(0.5, (double) (0.25F * f2), 0.5);
                    float f3 = ((float) blockEntity.angle + partialTick) / 20.0F;
                    poseStack.mulPose(Axis.YP.rotation(f3));
                    if (!flag) {
                        float f7 = -0.0F * (float) (j - 1) * 0.5F;
                        float f8 = -0.0F * (float) (j - 1) * 0.5F;
                        float f9 = -0.09375F * (float) (j - 1) * 0.5F;
                        poseStack.translate((double) f7, (double) f8, (double) f9);
                    }
                    for (int k = 0; k < j; k++) {
                        poseStack.pushPose();
                        if (k > 0) {
                            if (flag) {
                                float f11 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                                float f13 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                                float f10 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                                poseStack.translate(f11, f13, f10);
                            } else {
                                float f12 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                                float f14 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                                poseStack.translate((double) f12, (double) f14, 0.0);
                            }
                        }
                        this.itemRenderer.render(stack, ItemDisplayContext.GROUND, false, poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY, bakedmodel);
                        poseStack.popPose();
                        if (!flag) {
                            poseStack.translate(0.0, 0.0, 0.09375);
                        }
                    }
                    poseStack.popPose();
                }
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

    protected int getRenderAmount(ItemStack pStack) {
        int i = 1;
        if (pStack.getCount() > 48) {
            i = 5;
        } else if (pStack.getCount() > 32) {
            i = 4;
        } else if (pStack.getCount() > 16) {
            i = 3;
        } else if (pStack.getCount() > 1) {
            i = 2;
        }
        return i;
    }
}