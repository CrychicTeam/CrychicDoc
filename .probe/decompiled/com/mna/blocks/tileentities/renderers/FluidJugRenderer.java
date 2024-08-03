package com.mna.blocks.tileentities.renderers;

import com.mna.blocks.artifice.FluidJugBlock;
import com.mna.blocks.tileentities.FluidJugTile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class FluidJugRenderer implements BlockEntityRenderer<FluidJugTile> {

    public FluidJugRenderer(BlockEntityRendererProvider.Context context) {
    }

    public void render(FluidJugTile jug, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        BlockState state = jug.m_58900_();
        FluidStack flStack = jug.getContainedFluid();
        IClientFluidTypeExtensions extension = IClientFluidTypeExtensions.of(flStack.getFluid());
        matrixStackIn.pushPose();
        if (state.m_61143_(FluidJugBlock.f_54117_) != Direction.WEST && state.m_61143_(FluidJugBlock.f_54117_) != Direction.EAST) {
            matrixStackIn.translate(0.5, 0.1875, 0.3125);
        } else {
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(90.0F));
            matrixStackIn.translate(-0.5, 0.1875, 0.3125);
        }
        this.renderFluidBar(matrixStackIn, bufferIn, combinedLightIn, partialTicks, extension, jug.getFillPct(), jug.isInfinite());
        matrixStackIn.popPose();
        matrixStackIn.pushPose();
        if (state.m_61143_(FluidJugBlock.f_54117_) != Direction.WEST && state.m_61143_(FluidJugBlock.f_54117_) != Direction.EAST) {
            matrixStackIn.translate(0.5, 0.1875, 0.6875);
        } else {
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(90.0F));
            matrixStackIn.translate(-0.5, 0.1875, 0.6875);
        }
        this.renderFluidBar(matrixStackIn, bufferIn, combinedLightIn, partialTicks, extension, jug.getFillPct(), jug.isInfinite());
        matrixStackIn.popPose();
    }

    private void renderFluidBar(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLight, float partialTicks, IClientFluidTypeExtensions attrs, float fillPct, boolean infinite) {
        float width = infinite ? 0.08F : 0.12F;
        float min = infinite ? 0.02F : 0.0F;
        float height = infinite ? 0.2F : 0.5F;
        ResourceLocation fluidTexBase = attrs.getStillTexture();
        if (fluidTexBase != null) {
            TextureAtlasSprite sp = (TextureAtlasSprite) Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(fluidTexBase);
            RenderType liquid = RenderType.armorCutoutNoCull(TextureAtlas.LOCATION_BLOCKS);
            VertexConsumer builder = bufferIn.getBuffer(liquid);
            int color = attrs.getTintColor();
            float r = (float) (color >> 16 & 0xFF) / 255.0F;
            float g = (float) (color >> 8 & 0xFF) / 255.0F;
            float b = (float) (color >> 0 & 0xFF) / 255.0F;
            float a = (float) (color >> 24 & 0xFF) / 255.0F;
            float[] rgba = new float[] { r, g, b, a };
            Matrix3f normal = ((PoseStack.Pose) matrixStackIn.poseStack.getLast()).normal();
            Matrix4f pos = ((PoseStack.Pose) matrixStackIn.poseStack.getLast()).pose();
            Vector3f nrm = new Vector3f(0.0F, 1.0F, 0.0F);
            nrm.mul(normal);
            float maxV = sp.getV0() + (sp.getV1() - sp.getV0()) * fillPct;
            float minU = sp.getU(0.0);
            float maxU = sp.getU(8.0);
            int light = 15728880;
            builder.vertex(pos, -width, min, 0.0F);
            builder.color(rgba[0], rgba[1], rgba[2], rgba[3]);
            builder.uv(minU, sp.getV0());
            builder.overlayCoords(OverlayTexture.NO_OVERLAY);
            builder.uv2(light);
            builder.normal(nrm.x(), nrm.y(), nrm.z());
            builder.endVertex();
            builder.vertex(pos, width, min, 0.0F);
            builder.color(rgba[0], rgba[1], rgba[2], rgba[3]);
            builder.uv(maxU, sp.getV0());
            builder.overlayCoords(OverlayTexture.NO_OVERLAY);
            builder.uv2(light);
            builder.normal(nrm.x(), nrm.y(), nrm.z());
            builder.endVertex();
            builder.vertex(pos, width, min + height * fillPct, 0.0F);
            builder.color(rgba[0], rgba[1], rgba[2], rgba[3]);
            builder.uv(maxU, maxV);
            builder.overlayCoords(OverlayTexture.NO_OVERLAY);
            builder.uv2(light);
            builder.normal(nrm.x(), nrm.y(), nrm.z());
            builder.endVertex();
            builder.vertex(pos, -width, min + height * fillPct, 0.0F);
            builder.color(rgba[0], rgba[1], rgba[2], rgba[3]);
            builder.uv(minU, maxV);
            builder.overlayCoords(OverlayTexture.NO_OVERLAY);
            builder.uv2(light);
            builder.normal(nrm.x(), nrm.y(), nrm.z());
            builder.endVertex();
        }
    }
}