package com.rekindled.embers.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class FluidRenderer {

    public static TextureAtlasSprite getBlockSprite(ResourceLocation sprite) {
        return Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(sprite);
    }

    public static int withBlockLight(int combinedLight, int blockLight) {
        return combinedLight & -65536 | Math.max(blockLight << 4, combinedLight & 65535);
    }

    private static float boundUV(float value, boolean upper) {
        value %= 1.0F;
        if (value == 0.0F) {
            return upper ? 1.0F : 0.0F;
        } else {
            return value < 0.0F ? value + 1.0F : value;
        }
    }

    public static void putTexturedQuad(VertexConsumer renderer, PoseStack matrices, TextureAtlasSprite sprite, Vector3f from, Vector3f to, Direction face, int color, int brightness, int packedOverlay, int rotation, boolean flowing) {
        Matrix4f matrix = matrices.last().pose();
        Matrix3f normal = matrices.last().normal();
        float x1 = from.x();
        float y1 = from.y();
        float z1 = from.z();
        float x2 = to.x();
        float y2 = to.y();
        float z2 = to.z();
        float u1;
        float u2;
        float v1;
        float v2;
        switch(face) {
            case UP:
                u1 = x1;
                u2 = x2;
                v1 = -z1;
                v2 = -z2;
                break;
            case NORTH:
                u1 = -x1;
                u2 = -x2;
                v1 = y1;
                v2 = y2;
                break;
            case SOUTH:
                u1 = x2;
                u2 = x1;
                v1 = y1;
                v2 = y2;
                break;
            case WEST:
                u1 = z2;
                u2 = z1;
                v1 = y1;
                v2 = y2;
                break;
            case EAST:
                u1 = -z1;
                u2 = -z2;
                v1 = y1;
                v2 = y2;
                break;
            default:
                u1 = x1;
                u2 = x2;
                v1 = z2;
                v2 = z1;
        }
        if (rotation == 0 || rotation == 270) {
            float temp = v1;
            v1 = -v2;
            v2 = -temp;
        }
        if (rotation >= 180) {
            float temp = u1;
            u1 = -u2;
            u2 = -temp;
        }
        boolean reverse = u1 > u2;
        u1 = boundUV(u1, reverse);
        u2 = boundUV(u2, !reverse);
        reverse = v1 > v2;
        v1 = boundUV(v1, reverse);
        v2 = boundUV(v2, !reverse);
        double size = flowing ? 8.0 : 16.0;
        float minU;
        float maxU;
        float minV;
        float maxV;
        if (rotation % 180 == 90) {
            minU = sprite.getU((double) v1 * size);
            maxU = sprite.getU((double) v2 * size);
            minV = sprite.getV((double) u1 * size);
            maxV = sprite.getV((double) u2 * size);
        } else {
            minU = sprite.getU((double) u1 * size);
            maxU = sprite.getU((double) u2 * size);
            minV = sprite.getV((double) v1 * size);
            maxV = sprite.getV((double) v2 * size);
        }
        float u3;
        float u4;
        float v3;
        float v4;
        switch(rotation) {
            case 90:
                u1 = minU;
                v1 = minV;
                u2 = maxU;
                v2 = minV;
                u3 = maxU;
                v3 = maxV;
                u4 = minU;
                v4 = maxV;
                break;
            case 180:
                u1 = maxU;
                v1 = minV;
                u2 = maxU;
                v2 = maxV;
                u3 = minU;
                v3 = maxV;
                u4 = minU;
                v4 = minV;
                break;
            case 270:
                u1 = maxU;
                v1 = maxV;
                u2 = minU;
                v2 = maxV;
                u3 = minU;
                v3 = minV;
                u4 = maxU;
                v4 = minV;
                break;
            default:
                u1 = minU;
                v1 = maxV;
                u2 = minU;
                v2 = minV;
                u3 = maxU;
                v3 = minV;
                u4 = maxU;
                v4 = maxV;
        }
        int light1 = brightness & 65535;
        int light2 = brightness >> 16 & 65535;
        int a = color >> 24 & 0xFF;
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;
        switch(face) {
            case UP:
                renderer.vertex(matrix, x1, y2, z1).color(r, g, b, a).uv(u1, v1).overlayCoords(packedOverlay).uv2(light1, light2).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x1, y2, z2).color(r, g, b, a).uv(u2, v2).overlayCoords(packedOverlay).uv2(light1, light2).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x2, y2, z2).color(r, g, b, a).uv(u3, v3).overlayCoords(packedOverlay).uv2(light1, light2).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x2, y2, z1).color(r, g, b, a).uv(u4, v4).overlayCoords(packedOverlay).uv2(light1, light2).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                break;
            case NORTH:
                renderer.vertex(matrix, x1, y1, z1).color(r, g, b, a).uv(u1, v1).overlayCoords(packedOverlay).uv2(light1, light2).normal(normal, 0.0F, 0.0F, -1.0F).endVertex();
                renderer.vertex(matrix, x1, y2, z1).color(r, g, b, a).uv(u2, v2).overlayCoords(packedOverlay).uv2(light1, light2).normal(normal, 0.0F, 0.0F, -1.0F).endVertex();
                renderer.vertex(matrix, x2, y2, z1).color(r, g, b, a).uv(u3, v3).overlayCoords(packedOverlay).uv2(light1, light2).normal(normal, 0.0F, 0.0F, -1.0F).endVertex();
                renderer.vertex(matrix, x2, y1, z1).color(r, g, b, a).uv(u4, v4).overlayCoords(packedOverlay).uv2(light1, light2).normal(normal, 0.0F, 0.0F, -1.0F).endVertex();
                break;
            case SOUTH:
                renderer.vertex(matrix, x2, y1, z2).color(r, g, b, a).uv(u1, v1).overlayCoords(packedOverlay).uv2(light1, light2).normal(normal, 0.0F, 0.0F, 1.0F).endVertex();
                renderer.vertex(matrix, x2, y2, z2).color(r, g, b, a).uv(u2, v2).overlayCoords(packedOverlay).uv2(light1, light2).normal(normal, 0.0F, 0.0F, 1.0F).endVertex();
                renderer.vertex(matrix, x1, y2, z2).color(r, g, b, a).uv(u3, v3).overlayCoords(packedOverlay).uv2(light1, light2).normal(normal, 0.0F, 0.0F, 1.0F).endVertex();
                renderer.vertex(matrix, x1, y1, z2).color(r, g, b, a).uv(u4, v4).overlayCoords(packedOverlay).uv2(light1, light2).normal(normal, 0.0F, 0.0F, 1.0F).endVertex();
                break;
            case WEST:
                renderer.vertex(matrix, x1, y1, z2).color(r, g, b, a).uv(u1, v1).overlayCoords(packedOverlay).uv2(light1, light2).normal(normal, -1.0F, 0.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x1, y2, z2).color(r, g, b, a).uv(u2, v2).overlayCoords(packedOverlay).uv2(light1, light2).normal(normal, -1.0F, 0.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x1, y2, z1).color(r, g, b, a).uv(u3, v3).overlayCoords(packedOverlay).uv2(light1, light2).normal(normal, -1.0F, 0.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x1, y1, z1).color(r, g, b, a).uv(u4, v4).overlayCoords(packedOverlay).uv2(light1, light2).normal(normal, -1.0F, 0.0F, 0.0F).endVertex();
                break;
            case EAST:
                renderer.vertex(matrix, x2, y1, z1).color(r, g, b, a).uv(u1, v1).overlayCoords(packedOverlay).uv2(light1, light2).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x2, y2, z1).color(r, g, b, a).uv(u2, v2).overlayCoords(packedOverlay).uv2(light1, light2).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x2, y2, z2).color(r, g, b, a).uv(u3, v3).overlayCoords(packedOverlay).uv2(light1, light2).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x2, y1, z2).color(r, g, b, a).uv(u4, v4).overlayCoords(packedOverlay).uv2(light1, light2).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
                break;
            case DOWN:
                renderer.vertex(matrix, x1, y1, z2).color(r, g, b, a).uv(u1, v1).overlayCoords(packedOverlay).uv2(light1, light2).normal(normal, 0.0F, -1.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x1, y1, z1).color(r, g, b, a).uv(u2, v2).overlayCoords(packedOverlay).uv2(light1, light2).normal(normal, 0.0F, -1.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x2, y1, z1).color(r, g, b, a).uv(u3, v3).overlayCoords(packedOverlay).uv2(light1, light2).normal(normal, 0.0F, -1.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x2, y1, z2).color(r, g, b, a).uv(u4, v4).overlayCoords(packedOverlay).uv2(light1, light2).normal(normal, 0.0F, -1.0F, 0.0F).endVertex();
        }
    }

    public static void renderCuboid(PoseStack matrices, VertexConsumer buffer, FluidCuboid cube, TextureAtlasSprite still, TextureAtlasSprite flowing, Vector3f from, Vector3f to, int color, int light, int packedOverlay, boolean isGas) {
        int rotation = isGas ? 180 : 0;
        for (Direction dir : Direction.values()) {
            FluidCuboid.FluidFace face = cube.getFace(dir);
            if (face != null) {
                boolean isFlowing = face.isFlowing();
                int faceRot = (rotation + face.rotation()) % 360;
                putTexturedQuad(buffer, matrices, isFlowing ? flowing : still, from, to, dir, color, light, packedOverlay, faceRot, isFlowing);
            }
        }
    }

    public static void renderCuboids(PoseStack matrices, VertexConsumer buffer, List<FluidCuboid> cubes, FluidStack fluid, int light, int packedOverlay) {
        if (!fluid.isEmpty()) {
            FluidType type = fluid.getFluid().getFluidType();
            IClientFluidTypeExtensions clientType = IClientFluidTypeExtensions.of(type);
            TextureAtlasSprite still = getBlockSprite(clientType.getStillTexture(fluid));
            TextureAtlasSprite flowing = getBlockSprite(clientType.getFlowingTexture(fluid));
            boolean isGas = type.isLighterThanAir();
            light = withBlockLight(light, type.getLightLevel(fluid));
            int color = clientType.getTintColor(fluid);
            for (FluidCuboid cube : cubes) {
                renderCuboid(matrices, buffer, cube, still, flowing, cube.getFromScaled(), cube.getToScaled(), color, light, packedOverlay, isGas);
            }
        }
    }

    public static void renderCuboid(PoseStack matrices, VertexConsumer buffer, FluidCuboid cube, float yOffset, TextureAtlasSprite still, TextureAtlasSprite flowing, int color, int light, int packedOverlay, boolean isGas) {
        if (yOffset != 0.0F) {
            matrices.pushPose();
            matrices.translate(0.0F, yOffset, 0.0F);
        }
        renderCuboid(matrices, buffer, cube, still, flowing, cube.getFromScaled(), cube.getToScaled(), color, light, packedOverlay, isGas);
        if (yOffset != 0.0F) {
            matrices.popPose();
        }
    }

    public static void renderScaledCuboid(PoseStack matrices, MultiBufferSource buffer, FluidCuboid cube, FluidStack fluid, float offset, int capacity, int light, int packedOverlay, boolean flipGas) {
        if (!fluid.isEmpty() && capacity > 0) {
            FluidType type = fluid.getFluid().getFluidType();
            IClientFluidTypeExtensions clientType = IClientFluidTypeExtensions.of(type);
            TextureAtlasSprite still = getBlockSprite(clientType.getStillTexture(fluid));
            TextureAtlasSprite flowing = getBlockSprite(clientType.getFlowingTexture(fluid));
            boolean isGas = type.isLighterThanAir();
            light = withBlockLight(light, type.getLightLevel(fluid));
            Vector3f from = cube.getFromScaled();
            Vector3f to = cube.getToScaled();
            float minY = from.y();
            float maxY = to.y();
            float height = ((float) fluid.getAmount() - offset) / (float) capacity;
            if (isGas && flipGas) {
                from = new Vector3f(from);
                from.set(from.x(), maxY + height * (minY - maxY), from.z());
            } else {
                to = new Vector3f(to);
                to.set(to.x(), minY + height * (maxY - minY), to.z());
            }
            renderCuboid(matrices, buffer.getBuffer(EmbersRenderTypes.FLUID), cube, still, flowing, from, to, clientType.getTintColor(fluid), light, packedOverlay, isGas && flipGas);
        }
    }
}