package noppes.npcs.shared.client.model.util;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.nio.FloatBuffer;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import noppes.npcs.shared.common.util.NopVector2i;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

public class BatchRenderer {

    private static final FloatBuffer MATRIX_BUFFER = MemoryUtil.memAllocFloat(16);

    public static RenderType lastType = null;

    private static final BatchRenderer instance = new BatchRenderer();

    private final Map<RenderType, List<BatchRenderer.Batch>> queue = new LinkedHashMap();

    public static BatchRenderer getInstance() {
        return instance;
    }

    public void add(RenderType renderType, ResourceLocation resource, int id, VertexFormat format, Matrix4f matrix, int vertexCount, NopVector2i texPos, int light, int overlay, float red, float green, float blue, float alpha) {
        if (renderType == null) {
            renderType = lastType;
        }
        ((List) this.queue.computeIfAbsent(renderType, k -> new LinkedList())).add(new BatchRenderer.Batch(resource, id, format, matrix, vertexCount, texPos, light, overlay, red, green, blue, alpha));
    }

    public void draw() {
        this.queue.forEach((renderType, batches) -> {
            if (!batches.isEmpty()) {
                RenderSystem.assertOnRenderThread();
                renderType.m_110185_();
                RenderSystem.setShader(GameRenderer::m_172838_);
                ShaderInstance shaderinstance = RenderSystem.getShader();
                for (BatchRenderer.Batch b : batches) {
                    RenderSystem.setShaderTexture(0, b.resource);
                    shaderinstance.COLOR_MODULATOR.set(new float[] { b.red, b.green, b.blue, b.alpha });
                    if (shaderinstance.LIGHT0_DIRECTION != null) {
                        shaderinstance.LIGHT0_DIRECTION.set(b.light1);
                    }
                    if (shaderinstance.LIGHT1_DIRECTION != null) {
                        shaderinstance.LIGHT1_DIRECTION.set(b.light2);
                    }
                    shaderinstance.MODEL_VIEW_MATRIX.set(b.matrix);
                    if (shaderinstance.INVERSE_VIEW_ROTATION_MATRIX != null) {
                        shaderinstance.INVERSE_VIEW_ROTATION_MATRIX.set(RenderSystem.getInverseViewRotationMatrix());
                    }
                    if (shaderinstance.FOG_START != null) {
                        shaderinstance.FOG_START.set(RenderSystem.getShaderFogStart());
                    }
                    if (shaderinstance.FOG_END != null) {
                        shaderinstance.FOG_END.set(RenderSystem.getShaderFogEnd());
                    }
                    if (shaderinstance.FOG_COLOR != null) {
                        shaderinstance.FOG_COLOR.set(RenderSystem.getShaderFogColor());
                    }
                    if (shaderinstance.FOG_SHAPE != null) {
                        shaderinstance.FOG_SHAPE.set(RenderSystem.getShaderFogShape().getIndex());
                    }
                    if (shaderinstance.TEXTURE_MATRIX != null) {
                        shaderinstance.TEXTURE_MATRIX.set(createTranslateMatrix((float) b.texPos.x, (float) b.texPos.y, 0.0F));
                    }
                    if (shaderinstance.GAME_TIME != null) {
                        shaderinstance.GAME_TIME.set(RenderSystem.getShaderGameTime());
                    }
                    if (shaderinstance.SCREEN_SIZE != null) {
                        Window window = Minecraft.getInstance().getWindow();
                        shaderinstance.SCREEN_SIZE.set((float) window.getWidth(), (float) window.getHeight());
                    }
                    RenderSystem.glBindBuffer(34962, () -> b.id);
                    b.format.setupBufferState();
                    shaderinstance.apply();
                    RenderSystem.drawElements(4, 0, b.vertexCount);
                    shaderinstance.clear();
                    b.format.clearBufferState();
                    RenderSystem.glBindBuffer(34962, () -> 0);
                }
                renderType.m_110188_();
            }
        });
        this.queue.clear();
    }

    public static Matrix4f createTranslateMatrix(float float0, float float1, float float2) {
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.m00(1.0F);
        matrix4f.m11(1.0F);
        matrix4f.m22(1.0F);
        matrix4f.m33(1.0F);
        matrix4f.m03(float0);
        matrix4f.m13(float1);
        matrix4f.m23(float2);
        return matrix4f;
    }

    class Batch {

        final Matrix4f matrix;

        final int vertexCount;

        final ResourceLocation resource;

        final int id;

        final VertexFormat format;

        final int light1;

        final int light2;

        final int overlay1;

        final int overlay2;

        final float red;

        final float green;

        final float blue;

        final float alpha;

        final NopVector2i texPos;

        public Batch(ResourceLocation resource, int id, VertexFormat format, Matrix4f matrix, int vertexCount, NopVector2i texPos, int light, int overlay, float red, float green, float blue, float alpha) {
            this.resource = resource;
            this.id = id;
            this.format = format;
            this.matrix = matrix;
            this.vertexCount = vertexCount;
            this.texPos = texPos;
            this.light1 = light & 65535;
            this.light2 = light >> 16 & 65535;
            this.overlay1 = overlay & 65535;
            this.overlay2 = overlay >> 16 & 65535;
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }
    }
}