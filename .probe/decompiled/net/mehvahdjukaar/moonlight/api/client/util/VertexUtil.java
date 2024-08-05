package net.mehvahdjukaar.moonlight.api.client.util;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntUnaryOperator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.FastColor;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class VertexUtil {

    private static final int POSITION = 0;

    private static final int COLOR = 3;

    private static final int UV0 = 4;

    private static final int NORMAL = 7;

    private static int getStride() {
        return DefaultVertexFormat.BLOCK.getIntegerSize();
    }

    @Deprecated(forRemoval = true)
    public static List<BakedQuad> swapSprite(List<BakedQuad> quads, TextureAtlasSprite sprite) {
        List<BakedQuad> newList = new ArrayList();
        for (BakedQuad q : quads) {
            newList.add(swapSprite(q, sprite));
        }
        return newList;
    }

    @Deprecated(forRemoval = true)
    public static BakedQuad swapSprite(BakedQuad q, TextureAtlasSprite sprite) {
        TextureAtlasSprite oldSprite = q.getSprite();
        int stride = getStride();
        int[] v = Arrays.copyOf(q.getVertices(), q.getVertices().length);
        float segmentWScale = (float) sprite.contents().width() / (float) oldSprite.contents().width();
        float segmentHScale = (float) sprite.contents().height() / (float) oldSprite.contents().height();
        for (int i = 0; i < 4; i++) {
            int offset = i * stride + 4;
            float originalU = Float.intBitsToFloat(v[offset]);
            float originalV = Float.intBitsToFloat(v[offset + 1]);
            float u1 = (originalU - oldSprite.getU0()) * segmentWScale;
            v[offset] = Float.floatToRawIntBits(u1 + sprite.getU0());
            float v1 = (originalV - oldSprite.getV0()) * segmentHScale;
            v[offset + 1] = Float.floatToRawIntBits(v1 + sprite.getV0());
        }
        return new BakedQuad(v, q.getTintIndex(), q.getDirection(), sprite, q.isShade());
    }

    @Deprecated(forRemoval = true)
    public static void transformVertices(int[] v, Matrix3f transform) {
        Matrix4f transform4 = new Matrix4f();
        transform4.set(transform);
        transformVertices(v, transform4);
    }

    @Deprecated(forRemoval = true)
    public static void transformVertices(int[] v, Matrix4f transform) {
        int stride = getStride();
        for (int i = 0; i < 4; i++) {
            int offset = i * stride + 0;
            float originalX = Float.intBitsToFloat(v[offset]) - 0.5F;
            float originalY = Float.intBitsToFloat(v[offset + 1]) - 0.5F;
            float originalZ = Float.intBitsToFloat(v[offset + 2]) - 0.5F;
            Vector4f vec = new Vector4f(originalX, originalY, originalZ, 1.0F);
            vec.mul(transform);
            vec.div(vec.w);
            v[offset] = Float.floatToRawIntBits(vec.x() + 0.5F);
            v[offset + 1] = Float.floatToRawIntBits(vec.y() + 0.5F);
            v[offset + 2] = Float.floatToRawIntBits(vec.z() + 0.5F);
        }
        Matrix3f normalTransform = new Matrix3f(transform).invert().transpose();
        for (int i = 0; i < 4; i++) {
            int offset = i * stride + 7;
            int normalIn = v[offset];
            if ((normalIn & 16777215) != 0) {
                float normalX = (float) ((byte) (normalIn & 0xFF)) / 127.0F;
                float normalY = (float) ((byte) (normalIn >> 8 & 0xFF)) / 127.0F;
                float normalZ = (float) ((byte) (normalIn >> 16 & 0xFF)) / 127.0F;
                Vector3f vec = new Vector3f(normalX, normalY, normalZ);
                vec.mul(normalTransform);
                vec.normalize();
                v[offset] = (byte) ((int) (vec.x() * 127.0F)) & 255 | ((byte) ((int) (vec.y() * 127.0F)) & 255) << 8 | ((byte) ((int) (vec.z() * 127.0F)) & 255) << 16 | normalIn & 0xFF000000;
            }
        }
    }

    @Deprecated(forRemoval = true)
    public static void recolorVertices(int[] v, IntUnaryOperator indexToABGR) {
        int stride = getStride();
        for (int i = 0; i < 4; i++) {
            int i1 = indexToABGR.applyAsInt(i);
            v[i * stride + 3] = i1;
        }
    }

    @Deprecated(forRemoval = true)
    public static void recolorVertices(int[] v, int ABGR) {
        recolorVertices(v, i -> ABGR);
    }

    public static void addCube(VertexConsumer builder, PoseStack poseStack, float width, float height, int light, int color) {
        addCube(builder, poseStack, 0.0F, 0.0F, width, height, light, color);
    }

    public static void addCube(VertexConsumer builder, PoseStack poseStack, float uOff, float vOff, float width, float height, int light, int color) {
        addCube(builder, poseStack, uOff, vOff, width, height, light, color, 1.0F, true, true, false);
    }

    public static void addCube(VertexConsumer builder, PoseStack poseStack, float uOff, float vOff, float w, float h, int combinedLightIn, int color, float alpha, boolean up, boolean down, boolean wrap) {
        addCube(builder, poseStack, uOff, 1.0F - (vOff + h), uOff + w, 1.0F - vOff, w, h, combinedLightIn, color, alpha, up, down, wrap);
    }

    public static void addCube(VertexConsumer builder, PoseStack poseStack, float minU, float minV, float maxU, float maxV, float w, float h, int combinedLightIn, int color, float alpha, boolean up, boolean down, boolean wrap) {
        int lu = combinedLightIn & 65535;
        int lv = combinedLightIn >> 16 & 65535;
        float minV2 = maxV - w;
        int r = FastColor.ARGB32.red(color);
        int g = FastColor.ARGB32.green(color);
        int b = FastColor.ARGB32.blue(color);
        int a = (int) (255.0F * alpha);
        float hw = w / 2.0F;
        float hh = h / 2.0F;
        float inc = 0.0F;
        poseStack.pushPose();
        poseStack.translate(0.0F, hh, 0.0F);
        for (Direction d : Direction.values()) {
            float v0 = minV;
            float t = hw;
            float y0 = -hh;
            float y1 = hh;
            float i = inc;
            if (d.getAxis() == Direction.Axis.Y) {
                if (!up && d == Direction.UP || !down) {
                    continue;
                }
                t = hh;
                y0 = -hw;
                y1 = hw;
                v0 = minV2;
            } else if (wrap) {
                inc += w;
            }
            poseStack.pushPose();
            poseStack.mulPose(RotHlpr.rot(d));
            poseStack.translate(0.0F, 0.0F, -t);
            addQuad(builder, poseStack, -hw, y0, hw, y1, minU + i, v0, maxU + i, maxV, r, g, b, a, lu, lv);
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    public static void addQuad(VertexConsumer builder, PoseStack poseStack, float x0, float y0, float x1, float y1, int lu, int lv) {
        addQuad(builder, poseStack, x0, y0, x1, y1, 255, 255, 255, 255, lu, lv);
    }

    public static void addQuad(VertexConsumer builder, PoseStack poseStack, float x0, float y0, float x1, float y1, int r, int g, int b, int a, int lu, int lv) {
        addQuad(builder, poseStack, x0, y0, x1, y1, 0.0F, 0.0F, 1.0F, 1.0F, r, g, b, a, lu, lv);
    }

    public static void addQuad(VertexConsumer builder, PoseStack poseStack, float x0, float y0, float x1, float y1, float u0, float v0, float u1, float v1, int r, int g, int b, int a, int lu, int lv) {
        PoseStack.Pose last = poseStack.last();
        Vector3f vector3f = last.normal().transform(new Vector3f(0.0F, 0.0F, -1.0F));
        float nx = vector3f.x;
        float ny = vector3f.y;
        float nz = vector3f.z;
        vertF(builder, poseStack, x0, y1, 0.0F, u0, v0, r, g, b, a, lu, lv, nx, ny, nz);
        vertF(builder, poseStack, x1, y1, 0.0F, u1, v0, r, g, b, a, lu, lv, nx, ny, nz);
        vertF(builder, poseStack, x1, y0, 0.0F, u1, v1, r, g, b, a, lu, lv, nx, ny, nz);
        vertF(builder, poseStack, x0, y0, 0.0F, u0, v1, r, g, b, a, lu, lv, nx, ny, nz);
    }

    public static void vert(VertexConsumer builder, PoseStack poseStack, float x, float y, float z, float u, float v, float r, float g, float b, float a, int lu, int lv, float nx, float ny, float nz) {
        builder.vertex(poseStack.last().pose(), x, y, z);
        builder.color(r, g, b, a);
        builder.uv(u, v);
        builder.overlayCoords(0, 10);
        builder.uv2(lu, lv);
        builder.normal(poseStack.last().normal(), nx, ny, nz);
        builder.endVertex();
    }

    private static void vertF(VertexConsumer builder, PoseStack poseStack, float x, float y, float z, float u, float v, int r, int g, int b, int a, int lu, int lv, float nx, float ny, float nz) {
        builder.vertex(poseStack.last().pose(), x, y, z);
        builder.color(r, g, b, a);
        builder.uv(u, v);
        builder.overlayCoords(0, 10);
        builder.uv2(lu, lv);
        builder.normal(nx, ny, nz);
        builder.endVertex();
    }

    private static void vertF(VertexConsumer builder, PoseStack poseStack, float x, float y, float z, float u, float v, int color, int lu, int lv, float nx, float ny, float nz) {
        builder.vertex(poseStack.last().pose(), x, y, z);
        builder.color(color);
        builder.uv(u, v);
        builder.overlayCoords(0, 10);
        builder.uv2(lu, lv);
        builder.normal(nx, ny, nz);
        builder.endVertex();
    }
}