package com.github.alexthe666.alexsmobs.client.render.tile;

import com.github.alexthe666.alexsmobs.block.BlockEndPirateFlag;
import com.github.alexthe666.alexsmobs.client.model.ModelEndPirateFlag;
import com.github.alexthe666.alexsmobs.tileentity.TileEntityEndPirateFlag;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class RenderEndPirateFlag<T extends TileEntityEndPirateFlag> implements BlockEntityRenderer<T> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/end_pirate/flag.png");

    private static final ModelEndPirateFlag FLAG_MODEL = new ModelEndPirateFlag();

    public RenderEndPirateFlag(BlockEntityRendererProvider.Context rendererDispatcherIn) {
    }

    public void render(T tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();
        Direction dir = (Direction) tileEntityIn.m_58900_().m_61143_(BlockEndPirateFlag.FACING);
        switch(dir) {
            case NORTH:
                matrixStackIn.translate(0.5, 1.5, 0.5);
                break;
            case EAST:
                matrixStackIn.translate(0.5F, 1.5F, 0.5F);
                break;
            case SOUTH:
                matrixStackIn.translate(0.5, 1.5, 0.5);
                break;
            case WEST:
                matrixStackIn.translate(0.5F, 1.5F, 0.5F);
        }
        matrixStackIn.mulPose(dir.getOpposite().getRotation());
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
        matrixStackIn.mulPose(Axis.YN.rotationDegrees(dir.getAxis() == Direction.Axis.Y ? -90.0F : 90.0F));
        matrixStackIn.pushPose();
        FLAG_MODEL.renderFlag(tileEntityIn, partialTicks);
        FLAG_MODEL.m_7695_(matrixStackIn, bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
        matrixStackIn.popPose();
    }
}