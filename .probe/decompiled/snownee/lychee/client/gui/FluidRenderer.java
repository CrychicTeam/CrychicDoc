package snownee.lychee.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

public class FluidRenderer {

    public static VertexConsumer getFluidBuilder(MultiBufferSource buffer) {
        return buffer.getBuffer(RenderTypes.getFluid());
    }

    public static void renderFluidBox(FluidState fluidState, float xMin, float yMin, float zMin, float xMax, float yMax, float zMax, MultiBufferSource buffer, PoseStack ms, int light, boolean renderBottom) {
        renderFluidBox(fluidState, xMin, yMin, zMin, xMax, yMax, zMax, getFluidBuilder(buffer), ms, light, renderBottom);
    }

    public static void renderFluidBox(FluidState fluidState, float xMin, float yMin, float zMin, float xMax, float yMax, float zMax, VertexConsumer builder, PoseStack ms, int light, boolean renderBottom) {
        Fluid fluid = fluidState.getType();
        IClientFluidTypeExtensions fluidAttributes = IClientFluidTypeExtensions.of(fluid);
        FluidStack fluidStack = new FluidStack(fluid, 1000);
        ResourceLocation stillTex = fluidAttributes.getStillTexture(fluidStack);
        TextureAtlasSprite fluidTexture = (TextureAtlasSprite) Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTex);
        int color = fluidAttributes.getTintColor(fluidStack);
        int blockLightIn = light >> 4 & 15;
        int luminosity = Math.max(blockLightIn, fluid.getFluidType().getLightLevel(fluidStack));
        light = light & 15728640 | luminosity << 4;
        ms.pushPose();
        for (Direction side : Direction.values()) {
            if (side != Direction.DOWN || renderBottom) {
                boolean positive = side.getAxisDirection() == Direction.AxisDirection.POSITIVE;
                if (side.getAxis().isHorizontal()) {
                    if (side.getAxis() == Direction.Axis.X) {
                        renderStillTiledFace(side, zMin, yMin, zMax, yMax, positive ? xMax : xMin, builder, ms, light, color, fluidTexture);
                    } else {
                        renderStillTiledFace(side, xMin, yMin, xMax, yMax, positive ? zMax : zMin, builder, ms, light, color, fluidTexture);
                    }
                } else {
                    renderStillTiledFace(side, xMin, zMin, xMax, zMax, positive ? yMax : yMin, builder, ms, light, color, fluidTexture);
                }
            }
        }
        ms.popPose();
    }

    public static void renderStillTiledFace(Direction dir, float left, float down, float right, float up, float depth, VertexConsumer builder, PoseStack ms, int light, int color, TextureAtlasSprite texture) {
        renderTiledFace(dir, left, down, right, up, depth, builder, ms, light, color, texture, 1.0F);
    }

    public static void renderFlowingTiledFace(Direction dir, float left, float down, float right, float up, float depth, VertexConsumer builder, PoseStack ms, int light, int color, TextureAtlasSprite texture) {
        renderTiledFace(dir, left, down, right, up, depth, builder, ms, light, color, texture, 0.5F);
    }

    public static void renderTiledFace(Direction dir, float left, float down, float right, float up, float depth, VertexConsumer builder, PoseStack ms, int light, int color, TextureAtlasSprite texture, float textureScale) {
        boolean positive = dir.getAxisDirection() == Direction.AxisDirection.POSITIVE;
        boolean horizontal = dir.getAxis().isHorizontal();
        boolean x = dir.getAxis() == Direction.Axis.X;
        float shrink = texture.uvShrinkRatio() * 0.25F * textureScale;
        float centerU = texture.getU0() + (texture.getU1() - texture.getU0()) * 0.5F * textureScale;
        float centerV = texture.getV0() + (texture.getV1() - texture.getV0()) * 0.5F * textureScale;
        float x2 = 0.0F;
        float y2 = 0.0F;
        float x1 = left;
        while (x1 < right) {
            float f = (float) Mth.floor(x1);
            x2 = Math.min(f + 1.0F, right);
            float u1;
            float u2;
            if (dir != Direction.NORTH && dir != Direction.EAST) {
                u1 = texture.getU((double) ((x1 - f) * 16.0F * textureScale));
                u2 = texture.getU((double) ((x2 - f) * 16.0F * textureScale));
            } else {
                f = (float) Mth.ceil(x2);
                u1 = texture.getU((double) ((f - x2) * 16.0F * textureScale));
                u2 = texture.getU((double) ((f - x1) * 16.0F * textureScale));
            }
            u1 = Mth.lerp(shrink, u1, centerU);
            u2 = Mth.lerp(shrink, u2, centerU);
            float y1 = down;
            while (y1 < up) {
                f = (float) Mth.floor(y1);
                y2 = Math.min(f + 1.0F, up);
                float v1;
                float v2;
                if (dir == Direction.UP) {
                    v1 = texture.getV((double) ((y1 - f) * 16.0F * textureScale));
                    v2 = texture.getV((double) ((y2 - f) * 16.0F * textureScale));
                } else {
                    f = (float) Mth.ceil(y2);
                    v1 = texture.getV((double) ((f - y2) * 16.0F * textureScale));
                    v2 = texture.getV((double) ((f - y1) * 16.0F * textureScale));
                }
                v1 = Mth.lerp(shrink, v1, centerV);
                v2 = Mth.lerp(shrink, v2, centerV);
                if (horizontal) {
                    if (x) {
                        putVertex(builder, ms, depth, y2, positive ? x2 : x1, color, u1, v1, dir, light);
                        putVertex(builder, ms, depth, y1, positive ? x2 : x1, color, u1, v2, dir, light);
                        putVertex(builder, ms, depth, y1, positive ? x1 : x2, color, u2, v2, dir, light);
                        putVertex(builder, ms, depth, y2, positive ? x1 : x2, color, u2, v1, dir, light);
                    } else {
                        putVertex(builder, ms, positive ? x1 : x2, y2, depth, color, u1, v1, dir, light);
                        putVertex(builder, ms, positive ? x1 : x2, y1, depth, color, u1, v2, dir, light);
                        putVertex(builder, ms, positive ? x2 : x1, y1, depth, color, u2, v2, dir, light);
                        putVertex(builder, ms, positive ? x2 : x1, y2, depth, color, u2, v1, dir, light);
                    }
                } else {
                    putVertex(builder, ms, x1, depth, positive ? y1 : y2, color, u1, v1, dir, light);
                    putVertex(builder, ms, x1, depth, positive ? y2 : y1, color, u1, v2, dir, light);
                    putVertex(builder, ms, x2, depth, positive ? y2 : y1, color, u2, v2, dir, light);
                    putVertex(builder, ms, x2, depth, positive ? y1 : y2, color, u2, v1, dir, light);
                }
                y1 = y2;
            }
            x1 = x2;
        }
    }

    private static void putVertex(VertexConsumer builder, PoseStack ms, float x, float y, float z, int color, float u, float v, Direction face, int light) {
        Vec3i normal = face.getNormal();
        PoseStack.Pose peek = ms.last();
        int a = color >> 24 & 0xFF;
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;
        builder.vertex(peek.pose(), x, y, z).color(r, g, b, a).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(peek.normal(), (float) normal.getX(), (float) normal.getY(), (float) normal.getZ()).endVertex();
    }
}