package io.github.lightman314.lightmanscurrency.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class OutlineUtil {

    public static Vector4f decodeColor(int color, float alpha) {
        float r = (float) (color >> 16 & 0xFF) / 255.0F;
        float g = (float) (color >> 8 & 0xFF) / 255.0F;
        float b = (float) (color & 0xFF) / 255.0F;
        return new Vector4f(r, g, b, alpha);
    }

    public static Vector4f decodeColor(int color) {
        float r = (float) (color >> 24 & 0xFF) / 255.0F;
        float g = (float) (color >> 16 & 0xFF) / 255.0F;
        float b = (float) (color >> 8 & 0xFF) / 255.0F;
        float a = (float) (color & 0xFF) / 255.0F;
        return new Vector4f(r, g, b, a);
    }

    public static void renderBox(@Nonnull PoseStack pose, @Nonnull MultiBufferSource buffer, @Nonnull AABB area, int color) {
        renderBox(pose, buffer, area, decodeColor(color));
    }

    public static void renderBox(@Nonnull PoseStack pose, @Nonnull MultiBufferSource buffer, @Nonnull AABB area, int color, float alpha) {
        renderBox(pose, buffer, area, decodeColor(color, alpha));
    }

    public static void renderBox(@Nonnull PoseStack pose, @Nonnull MultiBufferSource buffer, @Nonnull AABB area, Vector4f color) {
        Vec3 camera = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        float inflate = 0.015625F;
        if (area.contains(camera)) {
            inflate *= -1.0F;
        }
        Vector3f minPos = new Vector3f((float) area.minX - inflate, (float) area.minY - inflate, (float) area.minZ - inflate);
        Vector3f maxPos = new Vector3f((float) area.maxX + inflate, (float) area.maxY + inflate, (float) area.maxZ + inflate);
        renderBoxFaces(pose, buffer, minPos, maxPos, color);
        VertexConsumer consumer = buffer.getBuffer(RenderType.lines());
        LevelRenderer.renderLineBox(pose, consumer, (double) minPos.x(), (double) minPos.y(), (double) minPos.z(), (double) maxPos.x(), (double) maxPos.y(), (double) maxPos.z(), color.x(), color.y(), color.z(), 1.0F);
    }

    private static void renderBoxFaces(@Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, Vector3f minPos, Vector3f maxPos, Vector4f color) {
        PoseStack.Pose pose = poseStack.last();
        VertexConsumer consumer = buffer.getBuffer(LCRenderTypes.getOutlineTranslucent());
        renderBoxFace(pose, consumer, minPos, maxPos, Direction.DOWN, color);
        renderBoxFace(pose, consumer, minPos, maxPos, Direction.UP, color);
        renderBoxFace(pose, consumer, minPos, maxPos, Direction.NORTH, color);
        renderBoxFace(pose, consumer, minPos, maxPos, Direction.SOUTH, color);
        renderBoxFace(pose, consumer, minPos, maxPos, Direction.EAST, color);
        renderBoxFace(pose, consumer, minPos, maxPos, Direction.WEST, color);
    }

    private static void renderBoxFace(@Nonnull PoseStack.Pose pose, @Nonnull VertexConsumer consumer, Vector3f minPos, Vector3f maxPos, Direction face, Vector4f color) {
        Vector3f pos0 = new Vector3f();
        Vector3f pos1 = new Vector3f();
        Vector3f pos2 = new Vector3f();
        Vector3f pos3 = new Vector3f();
        Vector3f normal = new Vector3f();
        float minX = minPos.x();
        float minY = minPos.y();
        float minZ = minPos.z();
        float maxX = maxPos.x();
        float maxY = maxPos.y();
        float maxZ = maxPos.z();
        switch(face) {
            case DOWN:
                pos0.set(maxX, minY, minZ);
                pos1.set(maxX, minY, maxZ);
                pos2.set(minX, minY, maxZ);
                pos3.set(minX, minY, minZ);
                normal.set(0.0F, -1.0F, 0.0F);
                break;
            case UP:
                pos0.set(minX, maxY, minZ);
                pos1.set(minX, maxY, maxZ);
                pos2.set(maxX, maxY, maxZ);
                pos3.set(maxX, maxY, minZ);
                normal.set(0.0F, 1.0F, 0.0F);
                break;
            case NORTH:
                pos0.set(maxX, maxY, minZ);
                pos1.set(maxX, minY, minZ);
                pos2.set(minX, minY, minZ);
                pos3.set(minX, maxY, minZ);
                normal.set(0.0F, 0.0F, -1.0F);
                break;
            case SOUTH:
                pos0.set(minX, maxY, maxZ);
                pos1.set(minX, minY, maxZ);
                pos2.set(maxX, minY, maxZ);
                pos3.set(maxX, maxY, maxZ);
                normal.set(0.0F, 0.0F, 1.0F);
                break;
            case WEST:
                pos0.set(minX, maxY, minZ);
                pos1.set(minX, minY, minZ);
                pos2.set(minX, minY, maxZ);
                pos3.set(minX, maxY, maxZ);
                normal.set(-1.0F, 0.0F, 0.0F);
                break;
            case EAST:
                pos0.set(maxX, maxY, maxZ);
                pos1.set(maxX, minY, maxZ);
                pos2.set(maxX, minY, minZ);
                pos3.set(maxX, maxY, minZ);
                normal.set(1.0F, 0.0F, 0.0F);
        }
        Matrix4f posMatrix = pose.pose();
        Vector4f posTransformTemp = new Vector4f(pos0.x(), pos0.y(), pos0.z(), 1.0F);
        posTransformTemp.set(pos0.x(), pos0.y(), pos0.z(), 1.0F);
        posTransformTemp.mul(posMatrix);
        double x0 = (double) posTransformTemp.x();
        double y0 = (double) posTransformTemp.y();
        double z0 = (double) posTransformTemp.z();
        posTransformTemp.set(pos1.x(), pos1.y(), pos1.z(), 1.0F);
        posTransformTemp.mul(posMatrix);
        double x1 = (double) posTransformTemp.x();
        double y1 = (double) posTransformTemp.y();
        double z1 = (double) posTransformTemp.z();
        posTransformTemp.set(pos2.x(), pos2.y(), pos2.z(), 1.0F);
        posTransformTemp.mul(posMatrix);
        double x2 = (double) posTransformTemp.x();
        double y2 = (double) posTransformTemp.y();
        double z2 = (double) posTransformTemp.z();
        posTransformTemp.set(pos3.x(), pos3.y(), pos3.z(), 1.0F);
        posTransformTemp.mul(posMatrix);
        double x3 = (double) posTransformTemp.x();
        double y3 = (double) posTransformTemp.y();
        double z3 = (double) posTransformTemp.z();
        float r = color.x();
        float g = color.y();
        float b = color.z();
        float a = color.w();
        Vector3f normalTransformTemp = new Vector3f();
        normalTransformTemp.set(normal);
        normalTransformTemp.mul(pose.normal());
        float nx = normalTransformTemp.x();
        float ny = normalTransformTemp.y();
        float nz = normalTransformTemp.z();
        consumer.vertex(x0, y0, z0).color(r, g, b, a).uv(0.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(nx, ny, nz).endVertex();
        consumer.vertex(x1, y1, z1).color(r, g, b, a).uv(0.0F, 2.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(nx, ny, nz).endVertex();
        consumer.vertex(x2, y2, z2).color(r, g, b, a).uv(1.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(nx, ny, nz).endVertex();
        consumer.vertex(x3, y3, z3).color(r, g, b, a).uv(1.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(nx, ny, nz).endVertex();
    }
}