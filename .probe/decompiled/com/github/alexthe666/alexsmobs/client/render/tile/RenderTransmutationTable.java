package com.github.alexthe666.alexsmobs.client.render.tile;

import com.github.alexthe666.alexsmobs.block.BlockTransmutationTable;
import com.github.alexthe666.alexsmobs.client.model.ModelTransmutationTable;
import com.github.alexthe666.alexsmobs.client.render.AMRenderTypes;
import com.github.alexthe666.alexsmobs.tileentity.TileEntityTransmutationTable;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class RenderTransmutationTable<T extends TileEntityTransmutationTable> implements BlockEntityRenderer<T> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/farseer/transmutation_table.png");

    private static final ResourceLocation OVERLAY = new ResourceLocation("alexsmobs:textures/entity/farseer/transmutation_table_overlay.png");

    private static final ResourceLocation GLOW_TEXTURE = new ResourceLocation("alexsmobs:textures/entity/farseer/transmutation_table_glow.png");

    private static final ModelTransmutationTable MODEL = new ModelTransmutationTable(0.0F);

    private static final ModelTransmutationTable OVERLAY_MODEL = new ModelTransmutationTable(0.01F);

    public RenderTransmutationTable(BlockEntityRendererProvider.Context rendererDispatcherIn) {
    }

    public void render(T tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();
        Direction dir = (Direction) tileEntityIn.m_58900_().m_61143_(BlockTransmutationTable.FACING);
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
        float ageInTicks = partialTicks + (float) tileEntityIn.ticksExisted;
        matrixStackIn.mulPose(dir.getOpposite().getRotation());
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
        matrixStackIn.pushPose();
        MODEL.animate(tileEntityIn, partialTicks);
        MODEL.m_7695_(matrixStackIn, bufferIn.getBuffer(RenderType.entityTranslucent(TEXTURE)), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        MODEL.m_7695_(matrixStackIn, bufferIn.getBuffer(AMRenderTypes.getEyesAlphaEnabled(GLOW_TEXTURE)), 240, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 0.5F + (float) Math.sin((double) (ageInTicks * 0.05F)) * 0.25F);
        VertexConsumer staticyOverlay = AMRenderTypes.createMergedVertexConsumer(bufferIn.getBuffer(AMRenderTypes.STATIC_PORTAL), bufferIn.getBuffer(RenderType.entityCutoutNoCull(OVERLAY)));
        OVERLAY_MODEL.animate(tileEntityIn, partialTicks);
        OVERLAY_MODEL.m_7695_(matrixStackIn, staticyOverlay, combinedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
        matrixStackIn.popPose();
    }

    private static void vertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, int int3, float float4, float float5, int int6, int int7) {
        vertexConsumer0.vertex(matrixF1, float4, float5, 0.0F).color(255, 255, 255, 100).uv((float) int6, (float) int7).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(int3).normal(matrixF2, 0.0F, 1.0F, 0.0F).endVertex();
    }
}