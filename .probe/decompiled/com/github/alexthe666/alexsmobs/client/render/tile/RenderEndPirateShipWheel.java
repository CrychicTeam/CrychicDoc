package com.github.alexthe666.alexsmobs.client.render.tile;

import com.github.alexthe666.alexsmobs.block.BlockEndPirateShipWheel;
import com.github.alexthe666.alexsmobs.client.model.ModelEndPirateShipWheel;
import com.github.alexthe666.alexsmobs.client.render.AMRenderTypes;
import com.github.alexthe666.alexsmobs.tileentity.TileEntityEndPirateShipWheel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class RenderEndPirateShipWheel<T extends TileEntityEndPirateShipWheel> implements BlockEntityRenderer<T> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/end_pirate/ship_wheel.png");

    private static final ResourceLocation TEXTURE_GLOW = new ResourceLocation("alexsmobs:textures/entity/end_pirate/ship_wheel_glow.png");

    private static final ModelEndPirateShipWheel WHEEL_MODEL = new ModelEndPirateShipWheel();

    public RenderEndPirateShipWheel(BlockEntityRendererProvider.Context rendererDispatcherIn) {
    }

    public void render(T tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();
        Direction dir = (Direction) tileEntityIn.m_58900_().m_61143_(BlockEndPirateShipWheel.FACING);
        switch(dir) {
            case UP:
                matrixStackIn.translate(0.5F, 1.5F, 0.5F);
                break;
            case DOWN:
                matrixStackIn.translate(0.5F, -0.5F, 0.5F);
                break;
            case NORTH:
                matrixStackIn.translate(0.5, 0.5, -0.5);
                break;
            case EAST:
                matrixStackIn.translate(1.5F, 0.5F, 0.5F);
                break;
            case SOUTH:
                matrixStackIn.translate(0.5, 0.5, 1.5);
                break;
            case WEST:
                matrixStackIn.translate(-0.5F, 0.5F, 0.5F);
        }
        matrixStackIn.mulPose(dir.getOpposite().getRotation());
        matrixStackIn.pushPose();
        WHEEL_MODEL.renderWheel(tileEntityIn, partialTicks);
        WHEEL_MODEL.m_7695_(matrixStackIn, bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        WHEEL_MODEL.m_7695_(matrixStackIn, bufferIn.getBuffer(AMRenderTypes.m_110458_(TEXTURE_GLOW)), 240, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
        matrixStackIn.popPose();
    }
}