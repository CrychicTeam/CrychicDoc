package com.simibubi.create.foundation.render;

import com.jozufozu.flywheel.api.vertex.ShadedVertexList;
import com.jozufozu.flywheel.api.vertex.VertexList;
import com.jozufozu.flywheel.backend.ShadersModHandler;
import com.jozufozu.flywheel.core.model.ShadeSeparatedBufferedData;
import com.jozufozu.flywheel.core.vertex.BlockVertexList;
import com.jozufozu.flywheel.core.vertex.BlockVertexList.Shaded;
import com.jozufozu.flywheel.util.DiffuseLightCalculator;
import com.jozufozu.flywheel.util.transform.TStack;
import com.jozufozu.flywheel.util.transform.Transform;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.utility.Color;
import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import java.nio.ByteBuffer;
import java.util.function.IntPredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class SuperByteBuffer implements Transform<SuperByteBuffer>, TStack<SuperByteBuffer> {

    private final VertexList template;

    private final IntPredicate shadedPredicate;

    private final PoseStack transforms = new PoseStack();

    private boolean shouldColor;

    private int r;

    private int g;

    private int b;

    private int a;

    private boolean disableDiffuseMult;

    private DiffuseLightCalculator diffuseCalculator;

    private SuperByteBuffer.SpriteShiftFunc spriteShiftFunc;

    private boolean hasOverlay;

    private int overlay = OverlayTexture.NO_OVERLAY;

    private boolean useWorldLight;

    private Matrix4f lightTransform;

    private boolean hasCustomLight;

    private int packedLightCoords;

    private boolean hybridLight;

    private boolean fullNormalTransform;

    private static final Long2IntMap WORLD_LIGHT_CACHE = new Long2IntOpenHashMap();

    public SuperByteBuffer(ByteBuffer vertexBuffer, BufferBuilder.DrawState drawState, int unshadedStartVertex) {
        int vertexCount = drawState.vertexCount();
        int stride = drawState.format().getVertexSize();
        ShadedVertexList template = new Shaded(vertexBuffer, vertexCount, stride, unshadedStartVertex);
        this.shadedPredicate = template::isShaded;
        this.template = template;
        this.transforms.pushPose();
    }

    public SuperByteBuffer(ShadeSeparatedBufferedData data) {
        this(data.vertexBuffer(), data.drawState(), data.unshadedStartVertex());
    }

    public SuperByteBuffer(ByteBuffer vertexBuffer, BufferBuilder.DrawState drawState) {
        int vertexCount = drawState.vertexCount();
        int stride = drawState.format().getVertexSize();
        this.template = new BlockVertexList(vertexBuffer, vertexCount, stride);
        this.shadedPredicate = index -> true;
        this.transforms.pushPose();
    }

    public void renderInto(PoseStack input, VertexConsumer builder) {
        if (!this.isEmpty()) {
            Matrix4f modelMat = new Matrix4f(input.last().pose());
            Matrix4f localTransforms = this.transforms.last().pose();
            modelMat.mul(localTransforms);
            Matrix3f normalMat;
            if (this.fullNormalTransform) {
                normalMat = new Matrix3f(input.last().normal());
                Matrix3f localNormalTransforms = this.transforms.last().normal();
                normalMat.mul(localNormalTransforms);
            } else {
                normalMat = new Matrix3f(this.transforms.last().normal());
            }
            if (this.useWorldLight) {
                WORLD_LIGHT_CACHE.clear();
            }
            Vector4f pos = new Vector4f();
            Vector3f normal = new Vector3f();
            Vector4f lightPos = new Vector4f();
            DiffuseLightCalculator diffuseCalculator = ForcedDiffuseState.getForcedCalculator();
            boolean disableDiffuseMult = this.disableDiffuseMult || ShadersModHandler.isShaderPackInUse() && diffuseCalculator == null;
            if (diffuseCalculator == null) {
                diffuseCalculator = this.diffuseCalculator;
                if (diffuseCalculator == null) {
                    diffuseCalculator = DiffuseLightCalculator.forCurrentLevel();
                }
            }
            int vertexCount = this.template.getVertexCount();
            for (int i = 0; i < vertexCount; i++) {
                float x = this.template.getX(i);
                float y = this.template.getY(i);
                float z = this.template.getZ(i);
                pos.set(x, y, z, 1.0F);
                pos.mul(modelMat);
                builder.vertex((double) pos.x(), (double) pos.y(), (double) pos.z());
                float normalX = this.template.getNX(i);
                float normalY = this.template.getNY(i);
                float normalZ = this.template.getNZ(i);
                normal.set(normalX, normalY, normalZ);
                normal.mul(normalMat);
                float nx = normal.x();
                float ny = normal.y();
                float nz = normal.z();
                byte r;
                byte g;
                byte b;
                byte a;
                if (this.shouldColor) {
                    r = (byte) this.r;
                    g = (byte) this.g;
                    b = (byte) this.b;
                    a = (byte) this.a;
                } else {
                    r = this.template.getR(i);
                    g = this.template.getG(i);
                    b = this.template.getB(i);
                    a = this.template.getA(i);
                }
                if (disableDiffuseMult) {
                    builder.color(r, g, b, a);
                } else {
                    float instanceDiffuse = diffuseCalculator.getDiffuse(nx, ny, nz, this.shadedPredicate.test(i));
                    int colorR = transformColor(r, instanceDiffuse);
                    int colorG = transformColor(g, instanceDiffuse);
                    int colorB = transformColor(b, instanceDiffuse);
                    builder.color(colorR, colorG, colorB, a);
                }
                float u = this.template.getU(i);
                float v = this.template.getV(i);
                if (this.spriteShiftFunc != null) {
                    this.spriteShiftFunc.shift(builder, u, v);
                } else {
                    builder.uv(u, v);
                }
                if (this.hasOverlay) {
                    builder.overlayCoords(this.overlay);
                }
                int light;
                if (this.useWorldLight) {
                    lightPos.set((x - 0.5F) * 15.0F / 16.0F + 0.5F, (y - 0.5F) * 15.0F / 16.0F + 0.5F, (z - 0.5F) * 15.0F / 16.0F + 0.5F, 1.0F);
                    lightPos.mul(localTransforms);
                    if (this.lightTransform != null) {
                        lightPos.mul(this.lightTransform);
                    }
                    light = getLight(Minecraft.getInstance().level, lightPos);
                    if (this.hasCustomLight) {
                        light = maxLight(light, this.packedLightCoords);
                    }
                } else if (this.hasCustomLight) {
                    light = this.packedLightCoords;
                } else {
                    light = this.template.getLight(i);
                }
                if (this.hybridLight) {
                    builder.uv2(maxLight(light, this.template.getLight(i)));
                } else {
                    builder.uv2(light);
                }
                builder.normal(nx, ny, nz);
                builder.endVertex();
            }
            this.reset();
        }
    }

    public SuperByteBuffer reset() {
        while (!this.transforms.clear()) {
            this.transforms.popPose();
        }
        this.transforms.pushPose();
        this.shouldColor = false;
        this.r = 0;
        this.g = 0;
        this.b = 0;
        this.a = 0;
        this.disableDiffuseMult = false;
        this.diffuseCalculator = null;
        this.spriteShiftFunc = null;
        this.hasOverlay = false;
        this.overlay = OverlayTexture.NO_OVERLAY;
        this.useWorldLight = false;
        this.lightTransform = null;
        this.hasCustomLight = false;
        this.packedLightCoords = 0;
        this.hybridLight = false;
        this.fullNormalTransform = false;
        return this;
    }

    public boolean isEmpty() {
        return this.template.isEmpty();
    }

    public void delete() {
        this.template.delete();
    }

    public PoseStack getTransforms() {
        return this.transforms;
    }

    public SuperByteBuffer translate(double x, double y, double z) {
        this.transforms.translate(x, y, z);
        return this;
    }

    public SuperByteBuffer multiply(Quaternionf quaternion) {
        this.transforms.mulPose(quaternion);
        return this;
    }

    public SuperByteBuffer scale(float factorX, float factorY, float factorZ) {
        this.transforms.scale(factorX, factorY, factorZ);
        return this;
    }

    public SuperByteBuffer pushPose() {
        this.transforms.pushPose();
        return this;
    }

    public SuperByteBuffer popPose() {
        this.transforms.popPose();
        return this;
    }

    public SuperByteBuffer mulPose(Matrix4f pose) {
        this.transforms.last().pose().mul(pose);
        return this;
    }

    public SuperByteBuffer mulNormal(Matrix3f normal) {
        this.transforms.last().normal().mul(normal);
        return this;
    }

    public SuperByteBuffer transform(PoseStack stack) {
        this.transforms.last().pose().mul(stack.last().pose());
        this.transforms.last().normal().mul(stack.last().normal());
        return this;
    }

    public SuperByteBuffer rotateCentered(Direction axis, float radians) {
        ((SuperByteBuffer) this.translate(0.5, 0.5, 0.5).rotate(axis, radians)).translate(-0.5, -0.5, -0.5);
        return this;
    }

    public SuperByteBuffer rotateCentered(Quaternionf q) {
        this.translate(0.5, 0.5, 0.5).multiply(q).translate(-0.5, -0.5, -0.5);
        return this;
    }

    public SuperByteBuffer color(int r, int g, int b, int a) {
        this.shouldColor = true;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        return this;
    }

    public SuperByteBuffer color(int color) {
        this.shouldColor = true;
        this.r = color >> 16 & 0xFF;
        this.g = color >> 8 & 0xFF;
        this.b = color & 0xFF;
        this.a = 255;
        return this;
    }

    public SuperByteBuffer color(Color c) {
        return this.color(c.getRGB());
    }

    public SuperByteBuffer disableDiffuse() {
        this.disableDiffuseMult = true;
        return this;
    }

    public SuperByteBuffer diffuseCalculator(DiffuseLightCalculator diffuseCalculator) {
        this.diffuseCalculator = diffuseCalculator;
        return this;
    }

    public SuperByteBuffer shiftUV(SpriteShiftEntry entry) {
        this.spriteShiftFunc = (builder, u, v) -> builder.uv(entry.getTargetU(u), entry.getTargetV(v));
        return this;
    }

    public SuperByteBuffer shiftUVScrolling(SpriteShiftEntry entry, float scrollV) {
        return this.shiftUVScrolling(entry, 0.0F, scrollV);
    }

    public SuperByteBuffer shiftUVScrolling(SpriteShiftEntry entry, float scrollU, float scrollV) {
        this.spriteShiftFunc = (builder, u, v) -> {
            float targetU = u - entry.getOriginal().getU0() + entry.getTarget().getU0() + scrollU;
            float targetV = v - entry.getOriginal().getV0() + entry.getTarget().getV0() + scrollV;
            builder.uv(targetU, targetV);
        };
        return this;
    }

    public SuperByteBuffer shiftUVtoSheet(SpriteShiftEntry entry, float uTarget, float vTarget, int sheetSize) {
        this.spriteShiftFunc = (builder, u, v) -> {
            float targetU = entry.getTarget().getU((double) (SpriteShiftEntry.getUnInterpolatedU(entry.getOriginal(), u) / (float) sheetSize + uTarget * 16.0F));
            float targetV = entry.getTarget().getV((double) (SpriteShiftEntry.getUnInterpolatedV(entry.getOriginal(), v) / (float) sheetSize + vTarget * 16.0F));
            builder.uv(targetU, targetV);
        };
        return this;
    }

    public SuperByteBuffer overlay() {
        this.hasOverlay = true;
        return this;
    }

    public SuperByteBuffer overlay(int overlay) {
        this.hasOverlay = true;
        this.overlay = overlay;
        return this;
    }

    public SuperByteBuffer light() {
        this.useWorldLight = true;
        return this;
    }

    public SuperByteBuffer light(Matrix4f lightTransform) {
        this.useWorldLight = true;
        this.lightTransform = lightTransform;
        return this;
    }

    public SuperByteBuffer light(int packedLightCoords) {
        this.hasCustomLight = true;
        this.packedLightCoords = packedLightCoords;
        return this;
    }

    public SuperByteBuffer light(Matrix4f lightTransform, int packedLightCoords) {
        this.light(lightTransform);
        this.light(packedLightCoords);
        return this;
    }

    public SuperByteBuffer hybridLight() {
        this.hybridLight = true;
        return this;
    }

    public SuperByteBuffer fullNormalTransform() {
        this.fullNormalTransform = true;
        return this;
    }

    public SuperByteBuffer forEntityRender() {
        this.disableDiffuse();
        this.overlay();
        this.fullNormalTransform();
        return this;
    }

    public static int transformColor(byte component, float scale) {
        return Mth.clamp((int) ((float) Byte.toUnsignedInt(component) * scale), 0, 255);
    }

    public static int transformColor(int component, float scale) {
        return Mth.clamp((int) ((float) component * scale), 0, 255);
    }

    public static int maxLight(int packedLight1, int packedLight2) {
        int blockLight1 = LightTexture.block(packedLight1);
        int skyLight1 = LightTexture.sky(packedLight1);
        int blockLight2 = LightTexture.block(packedLight2);
        int skyLight2 = LightTexture.sky(packedLight2);
        return LightTexture.pack(Math.max(blockLight1, blockLight2), Math.max(skyLight1, skyLight2));
    }

    private static int getLight(Level world, Vector4f lightPos) {
        BlockPos pos = BlockPos.containing((double) lightPos.x(), (double) lightPos.y(), (double) lightPos.z());
        return WORLD_LIGHT_CACHE.computeIfAbsent(pos.asLong(), $ -> LevelRenderer.getLightColor(world, pos));
    }

    @FunctionalInterface
    public interface SpriteShiftFunc {

        void shift(VertexConsumer var1, float var2, float var3);
    }

    @FunctionalInterface
    public interface VertexLighter {

        int getPackedLight(float var1, float var2, float var3);
    }
}