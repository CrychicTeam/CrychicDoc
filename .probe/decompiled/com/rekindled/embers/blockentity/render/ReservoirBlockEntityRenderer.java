package com.rekindled.embers.blockentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rekindled.embers.blockentity.ReservoirBlockEntity;
import com.rekindled.embers.render.EmbersRenderTypes;
import com.rekindled.embers.render.FluidRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import org.joml.Vector3f;

public class ReservoirBlockEntityRenderer implements BlockEntityRenderer<ReservoirBlockEntity> {

    float[] bounds = getBlockBounds(2, 0.0F, 1.0F);

    public ReservoirBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
    }

    public void render(ReservoirBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (blockEntity != null) {
            FluidStack fluidStack = blockEntity.getFluidStack();
            int capacity = blockEntity.getCapacity();
            if (!fluidStack.isEmpty() && capacity > 0) {
                poseStack.pushPose();
                poseStack.translate(-0.5, 1.0, -0.5);
                float offset = blockEntity.renderOffset;
                if (!(offset > 1.2F) && !(offset < -1.2F)) {
                    blockEntity.renderOffset = 0.0F;
                } else {
                    offset -= (offset / 12.0F + 0.1F) * partialTick;
                    blockEntity.renderOffset = offset;
                }
                renderLargeFluidCuboid(poseStack, bufferSource.getBuffer(EmbersRenderTypes.FLUID), fluidStack, packedLight, packedOverlay, 1, this.bounds, 1, this.bounds, 0.0F, ((float) blockEntity.height - 0.0625F) * ((float) fluidStack.getAmount() - offset) / (float) capacity);
                poseStack.popPose();
            } else {
                blockEntity.renderOffset = 0.0F;
            }
        }
    }

    private static void renderLargeFluidCuboid(PoseStack matrices, VertexConsumer builder, FluidStack fluid, int brightness, int packedOverlay, int xd, float[] xBounds, int zd, float[] zBounds, float yMin, float yMax) {
        if (!(yMin >= yMax) && !fluid.isEmpty()) {
            FluidType type = fluid.getFluid().getFluidType();
            IClientFluidTypeExtensions clientType = IClientFluidTypeExtensions.of(type);
            TextureAtlasSprite still = FluidRenderer.getBlockSprite(clientType.getStillTexture(fluid));
            int color = clientType.getTintColor(fluid);
            brightness = FluidRenderer.withBlockLight(brightness, type.getLightLevel(fluid));
            int yd = (int) (yMax - (float) ((int) yMin));
            if ((double) yMax % 1.0 == 0.0) {
                yd--;
            }
            float[] yBounds = getBlockBounds(yd, yMin, yMax);
            Vector3f from = new Vector3f();
            Vector3f to = new Vector3f();
            for (int y = 0; y <= yd; y++) {
                for (int z = 0; z <= zd; z++) {
                    for (int x = 0; x <= xd; x++) {
                        from.set(xBounds[x], yBounds[y], zBounds[z]);
                        to.set(xBounds[x + 1], yBounds[y + 1], zBounds[z + 1]);
                        if (x == 0) {
                            FluidRenderer.putTexturedQuad(builder, matrices, still, from, to, Direction.WEST, color, brightness, packedOverlay, 0, false);
                        }
                        if (x == xd) {
                            FluidRenderer.putTexturedQuad(builder, matrices, still, from, to, Direction.EAST, color, brightness, packedOverlay, 0, false);
                        }
                        if (z == 0) {
                            FluidRenderer.putTexturedQuad(builder, matrices, still, from, to, Direction.NORTH, color, brightness, packedOverlay, 0, false);
                        }
                        if (z == zd) {
                            FluidRenderer.putTexturedQuad(builder, matrices, still, from, to, Direction.SOUTH, color, brightness, packedOverlay, 0, false);
                        }
                        if (y == yd) {
                            FluidRenderer.putTexturedQuad(builder, matrices, still, from, to, Direction.UP, color, brightness, packedOverlay, 0, false);
                        }
                        if (y == 0) {
                            from.set(from.x(), from.y() + 0.001F, from.z());
                            FluidRenderer.putTexturedQuad(builder, matrices, still, from, to, Direction.DOWN, color, brightness, packedOverlay, 0, false);
                        }
                    }
                }
            }
        }
    }

    private static float[] getBlockBounds(int delta, float start, float end) {
        float[] bounds = new float[2 + delta];
        bounds[0] = start;
        int offset = (int) start;
        for (int i = 1; i <= delta; i++) {
            bounds[i] = (float) (i + offset);
        }
        bounds[delta + 1] = end;
        return bounds;
    }
}