package com.mojang.blaze3d.platform;

import com.google.common.base.Charsets;
import com.mojang.blaze3d.DontObfuscate;
import com.mojang.blaze3d.systems.RenderSystem;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import javax.annotation.Nullable;
import net.minecraft.Util;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL20C;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32C;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

@DontObfuscate
public class GlStateManager {

    private static final boolean ON_LINUX = Util.getPlatform() == Util.OS.LINUX;

    public static final int TEXTURE_COUNT = 12;

    private static final GlStateManager.BlendState BLEND = new GlStateManager.BlendState();

    private static final GlStateManager.DepthState DEPTH = new GlStateManager.DepthState();

    private static final GlStateManager.CullState CULL = new GlStateManager.CullState();

    private static final GlStateManager.PolygonOffsetState POLY_OFFSET = new GlStateManager.PolygonOffsetState();

    private static final GlStateManager.ColorLogicState COLOR_LOGIC = new GlStateManager.ColorLogicState();

    private static final GlStateManager.StencilState STENCIL = new GlStateManager.StencilState();

    private static final GlStateManager.ScissorState SCISSOR = new GlStateManager.ScissorState();

    private static int activeTexture;

    private static final GlStateManager.TextureState[] TEXTURES = (GlStateManager.TextureState[]) IntStream.range(0, 12).mapToObj(p_157120_ -> new GlStateManager.TextureState()).toArray(GlStateManager.TextureState[]::new);

    private static final GlStateManager.ColorMask COLOR_MASK = new GlStateManager.ColorMask();

    public static void _disableScissorTest() {
        RenderSystem.assertOnRenderThreadOrInit();
        SCISSOR.mode.disable();
    }

    public static void _enableScissorTest() {
        RenderSystem.assertOnRenderThreadOrInit();
        SCISSOR.mode.enable();
    }

    public static void _scissorBox(int int0, int int1, int int2, int int3) {
        RenderSystem.assertOnRenderThreadOrInit();
        GL20.glScissor(int0, int1, int2, int3);
    }

    public static void _disableDepthTest() {
        RenderSystem.assertOnRenderThreadOrInit();
        DEPTH.mode.disable();
    }

    public static void _enableDepthTest() {
        RenderSystem.assertOnRenderThreadOrInit();
        DEPTH.mode.enable();
    }

    public static void _depthFunc(int int0) {
        RenderSystem.assertOnRenderThreadOrInit();
        if (int0 != DEPTH.func) {
            DEPTH.func = int0;
            GL11.glDepthFunc(int0);
        }
    }

    public static void _depthMask(boolean boolean0) {
        RenderSystem.assertOnRenderThread();
        if (boolean0 != DEPTH.mask) {
            DEPTH.mask = boolean0;
            GL11.glDepthMask(boolean0);
        }
    }

    public static void _disableBlend() {
        RenderSystem.assertOnRenderThread();
        BLEND.mode.disable();
    }

    public static void _enableBlend() {
        RenderSystem.assertOnRenderThread();
        BLEND.mode.enable();
    }

    public static void _blendFunc(int int0, int int1) {
        RenderSystem.assertOnRenderThread();
        if (int0 != BLEND.srcRgb || int1 != BLEND.dstRgb) {
            BLEND.srcRgb = int0;
            BLEND.dstRgb = int1;
            GL11.glBlendFunc(int0, int1);
        }
    }

    public static void _blendFuncSeparate(int int0, int int1, int int2, int int3) {
        RenderSystem.assertOnRenderThread();
        if (int0 != BLEND.srcRgb || int1 != BLEND.dstRgb || int2 != BLEND.srcAlpha || int3 != BLEND.dstAlpha) {
            BLEND.srcRgb = int0;
            BLEND.dstRgb = int1;
            BLEND.srcAlpha = int2;
            BLEND.dstAlpha = int3;
            glBlendFuncSeparate(int0, int1, int2, int3);
        }
    }

    public static void _blendEquation(int int0) {
        RenderSystem.assertOnRenderThread();
        GL14.glBlendEquation(int0);
    }

    public static int glGetProgrami(int int0, int int1) {
        RenderSystem.assertOnRenderThread();
        return GL20.glGetProgrami(int0, int1);
    }

    public static void glAttachShader(int int0, int int1) {
        RenderSystem.assertOnRenderThread();
        GL20.glAttachShader(int0, int1);
    }

    public static void glDeleteShader(int int0) {
        RenderSystem.assertOnRenderThread();
        GL20.glDeleteShader(int0);
    }

    public static int glCreateShader(int int0) {
        RenderSystem.assertOnRenderThread();
        return GL20.glCreateShader(int0);
    }

    public static void glShaderSource(int int0, List<String> listString1) {
        RenderSystem.assertOnRenderThread();
        StringBuilder $$2 = new StringBuilder();
        for (String $$3 : listString1) {
            $$2.append($$3);
        }
        byte[] $$4 = $$2.toString().getBytes(Charsets.UTF_8);
        ByteBuffer $$5 = MemoryUtil.memAlloc($$4.length + 1);
        $$5.put($$4);
        $$5.put((byte) 0);
        $$5.flip();
        try {
            MemoryStack $$6 = MemoryStack.stackPush();
            try {
                PointerBuffer $$7 = $$6.mallocPointer(1);
                $$7.put($$5);
                GL20C.nglShaderSource(int0, 1, $$7.address0(), 0L);
            } catch (Throwable var13) {
                if ($$6 != null) {
                    try {
                        $$6.close();
                    } catch (Throwable var12) {
                        var13.addSuppressed(var12);
                    }
                }
                throw var13;
            }
            if ($$6 != null) {
                $$6.close();
            }
        } finally {
            MemoryUtil.memFree($$5);
        }
    }

    public static void glCompileShader(int int0) {
        RenderSystem.assertOnRenderThread();
        GL20.glCompileShader(int0);
    }

    public static int glGetShaderi(int int0, int int1) {
        RenderSystem.assertOnRenderThread();
        return GL20.glGetShaderi(int0, int1);
    }

    public static void _glUseProgram(int int0) {
        RenderSystem.assertOnRenderThread();
        GL20.glUseProgram(int0);
    }

    public static int glCreateProgram() {
        RenderSystem.assertOnRenderThread();
        return GL20.glCreateProgram();
    }

    public static void glDeleteProgram(int int0) {
        RenderSystem.assertOnRenderThread();
        GL20.glDeleteProgram(int0);
    }

    public static void glLinkProgram(int int0) {
        RenderSystem.assertOnRenderThread();
        GL20.glLinkProgram(int0);
    }

    public static int _glGetUniformLocation(int int0, CharSequence charSequence1) {
        RenderSystem.assertOnRenderThread();
        return GL20.glGetUniformLocation(int0, charSequence1);
    }

    public static void _glUniform1(int int0, IntBuffer intBuffer1) {
        RenderSystem.assertOnRenderThread();
        GL20.glUniform1iv(int0, intBuffer1);
    }

    public static void _glUniform1i(int int0, int int1) {
        RenderSystem.assertOnRenderThread();
        GL20.glUniform1i(int0, int1);
    }

    public static void _glUniform1(int int0, FloatBuffer floatBuffer1) {
        RenderSystem.assertOnRenderThread();
        GL20.glUniform1fv(int0, floatBuffer1);
    }

    public static void _glUniform2(int int0, IntBuffer intBuffer1) {
        RenderSystem.assertOnRenderThread();
        GL20.glUniform2iv(int0, intBuffer1);
    }

    public static void _glUniform2(int int0, FloatBuffer floatBuffer1) {
        RenderSystem.assertOnRenderThread();
        GL20.glUniform2fv(int0, floatBuffer1);
    }

    public static void _glUniform3(int int0, IntBuffer intBuffer1) {
        RenderSystem.assertOnRenderThread();
        GL20.glUniform3iv(int0, intBuffer1);
    }

    public static void _glUniform3(int int0, FloatBuffer floatBuffer1) {
        RenderSystem.assertOnRenderThread();
        GL20.glUniform3fv(int0, floatBuffer1);
    }

    public static void _glUniform4(int int0, IntBuffer intBuffer1) {
        RenderSystem.assertOnRenderThread();
        GL20.glUniform4iv(int0, intBuffer1);
    }

    public static void _glUniform4(int int0, FloatBuffer floatBuffer1) {
        RenderSystem.assertOnRenderThread();
        GL20.glUniform4fv(int0, floatBuffer1);
    }

    public static void _glUniformMatrix2(int int0, boolean boolean1, FloatBuffer floatBuffer2) {
        RenderSystem.assertOnRenderThread();
        GL20.glUniformMatrix2fv(int0, boolean1, floatBuffer2);
    }

    public static void _glUniformMatrix3(int int0, boolean boolean1, FloatBuffer floatBuffer2) {
        RenderSystem.assertOnRenderThread();
        GL20.glUniformMatrix3fv(int0, boolean1, floatBuffer2);
    }

    public static void _glUniformMatrix4(int int0, boolean boolean1, FloatBuffer floatBuffer2) {
        RenderSystem.assertOnRenderThread();
        GL20.glUniformMatrix4fv(int0, boolean1, floatBuffer2);
    }

    public static int _glGetAttribLocation(int int0, CharSequence charSequence1) {
        RenderSystem.assertOnRenderThread();
        return GL20.glGetAttribLocation(int0, charSequence1);
    }

    public static void _glBindAttribLocation(int int0, int int1, CharSequence charSequence2) {
        RenderSystem.assertOnRenderThread();
        GL20.glBindAttribLocation(int0, int1, charSequence2);
    }

    public static int _glGenBuffers() {
        RenderSystem.assertOnRenderThreadOrInit();
        return GL15.glGenBuffers();
    }

    public static int _glGenVertexArrays() {
        RenderSystem.assertOnRenderThreadOrInit();
        return GL30.glGenVertexArrays();
    }

    public static void _glBindBuffer(int int0, int int1) {
        RenderSystem.assertOnRenderThreadOrInit();
        GL15.glBindBuffer(int0, int1);
    }

    public static void _glBindVertexArray(int int0) {
        RenderSystem.assertOnRenderThreadOrInit();
        GL30.glBindVertexArray(int0);
    }

    public static void _glBufferData(int int0, ByteBuffer byteBuffer1, int int2) {
        RenderSystem.assertOnRenderThreadOrInit();
        GL15.glBufferData(int0, byteBuffer1, int2);
    }

    public static void _glBufferData(int int0, long long1, int int2) {
        RenderSystem.assertOnRenderThreadOrInit();
        GL15.glBufferData(int0, long1, int2);
    }

    @Nullable
    public static ByteBuffer _glMapBuffer(int int0, int int1) {
        RenderSystem.assertOnRenderThreadOrInit();
        return GL15.glMapBuffer(int0, int1);
    }

    public static void _glUnmapBuffer(int int0) {
        RenderSystem.assertOnRenderThreadOrInit();
        GL15.glUnmapBuffer(int0);
    }

    public static void _glDeleteBuffers(int int0) {
        RenderSystem.assertOnRenderThread();
        if (ON_LINUX) {
            GL32C.glBindBuffer(34962, int0);
            GL32C.glBufferData(34962, 0L, 35048);
            GL32C.glBindBuffer(34962, 0);
        }
        GL15.glDeleteBuffers(int0);
    }

    public static void _glCopyTexSubImage2D(int int0, int int1, int int2, int int3, int int4, int int5, int int6, int int7) {
        RenderSystem.assertOnRenderThreadOrInit();
        GL20.glCopyTexSubImage2D(int0, int1, int2, int3, int4, int5, int6, int7);
    }

    public static void _glDeleteVertexArrays(int int0) {
        RenderSystem.assertOnRenderThread();
        GL30.glDeleteVertexArrays(int0);
    }

    public static void _glBindFramebuffer(int int0, int int1) {
        RenderSystem.assertOnRenderThreadOrInit();
        GL30.glBindFramebuffer(int0, int1);
    }

    public static void _glBlitFrameBuffer(int int0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, int int8, int int9) {
        RenderSystem.assertOnRenderThreadOrInit();
        GL30.glBlitFramebuffer(int0, int1, int2, int3, int4, int5, int6, int7, int8, int9);
    }

    public static void _glBindRenderbuffer(int int0, int int1) {
        RenderSystem.assertOnRenderThreadOrInit();
        GL30.glBindRenderbuffer(int0, int1);
    }

    public static void _glDeleteRenderbuffers(int int0) {
        RenderSystem.assertOnRenderThreadOrInit();
        GL30.glDeleteRenderbuffers(int0);
    }

    public static void _glDeleteFramebuffers(int int0) {
        RenderSystem.assertOnRenderThreadOrInit();
        GL30.glDeleteFramebuffers(int0);
    }

    public static int glGenFramebuffers() {
        RenderSystem.assertOnRenderThreadOrInit();
        return GL30.glGenFramebuffers();
    }

    public static int glGenRenderbuffers() {
        RenderSystem.assertOnRenderThreadOrInit();
        return GL30.glGenRenderbuffers();
    }

    public static void _glRenderbufferStorage(int int0, int int1, int int2, int int3) {
        RenderSystem.assertOnRenderThreadOrInit();
        GL30.glRenderbufferStorage(int0, int1, int2, int3);
    }

    public static void _glFramebufferRenderbuffer(int int0, int int1, int int2, int int3) {
        RenderSystem.assertOnRenderThreadOrInit();
        GL30.glFramebufferRenderbuffer(int0, int1, int2, int3);
    }

    public static int glCheckFramebufferStatus(int int0) {
        RenderSystem.assertOnRenderThreadOrInit();
        return GL30.glCheckFramebufferStatus(int0);
    }

    public static void _glFramebufferTexture2D(int int0, int int1, int int2, int int3, int int4) {
        RenderSystem.assertOnRenderThreadOrInit();
        GL30.glFramebufferTexture2D(int0, int1, int2, int3, int4);
    }

    public static int getBoundFramebuffer() {
        RenderSystem.assertOnRenderThread();
        return _getInteger(36006);
    }

    public static void glActiveTexture(int int0) {
        RenderSystem.assertOnRenderThread();
        GL13.glActiveTexture(int0);
    }

    public static void glBlendFuncSeparate(int int0, int int1, int int2, int int3) {
        RenderSystem.assertOnRenderThread();
        GL14.glBlendFuncSeparate(int0, int1, int2, int3);
    }

    public static String glGetShaderInfoLog(int int0, int int1) {
        RenderSystem.assertOnRenderThread();
        return GL20.glGetShaderInfoLog(int0, int1);
    }

    public static String glGetProgramInfoLog(int int0, int int1) {
        RenderSystem.assertOnRenderThread();
        return GL20.glGetProgramInfoLog(int0, int1);
    }

    public static void setupLevelDiffuseLighting(Vector3f vectorF0, Vector3f vectorF1, Matrix4f matrixF2) {
        RenderSystem.assertOnRenderThread();
        Vector4f $$3 = matrixF2.transform(new Vector4f(vectorF0, 1.0F));
        Vector4f $$4 = matrixF2.transform(new Vector4f(vectorF1, 1.0F));
        RenderSystem.setShaderLights(new Vector3f($$3.x(), $$3.y(), $$3.z()), new Vector3f($$4.x(), $$4.y(), $$4.z()));
    }

    public static void setupGuiFlatDiffuseLighting(Vector3f vectorF0, Vector3f vectorF1) {
        RenderSystem.assertOnRenderThread();
        Matrix4f $$2 = new Matrix4f().scaling(1.0F, -1.0F, 1.0F).rotateY((float) (-Math.PI / 8)).rotateX((float) (Math.PI * 3.0 / 4.0));
        setupLevelDiffuseLighting(vectorF0, vectorF1, $$2);
    }

    public static void setupGui3DDiffuseLighting(Vector3f vectorF0, Vector3f vectorF1) {
        RenderSystem.assertOnRenderThread();
        Matrix4f $$2 = new Matrix4f().rotationYXZ(1.0821041F, 3.2375858F, 0.0F).rotateYXZ((float) (-Math.PI / 8), (float) (Math.PI * 3.0 / 4.0), 0.0F);
        setupLevelDiffuseLighting(vectorF0, vectorF1, $$2);
    }

    public static void _enableCull() {
        RenderSystem.assertOnRenderThread();
        CULL.enable.enable();
    }

    public static void _disableCull() {
        RenderSystem.assertOnRenderThread();
        CULL.enable.disable();
    }

    public static void _polygonMode(int int0, int int1) {
        RenderSystem.assertOnRenderThread();
        GL11.glPolygonMode(int0, int1);
    }

    public static void _enablePolygonOffset() {
        RenderSystem.assertOnRenderThread();
        POLY_OFFSET.fill.enable();
    }

    public static void _disablePolygonOffset() {
        RenderSystem.assertOnRenderThread();
        POLY_OFFSET.fill.disable();
    }

    public static void _polygonOffset(float float0, float float1) {
        RenderSystem.assertOnRenderThread();
        if (float0 != POLY_OFFSET.factor || float1 != POLY_OFFSET.units) {
            POLY_OFFSET.factor = float0;
            POLY_OFFSET.units = float1;
            GL11.glPolygonOffset(float0, float1);
        }
    }

    public static void _enableColorLogicOp() {
        RenderSystem.assertOnRenderThread();
        COLOR_LOGIC.enable.enable();
    }

    public static void _disableColorLogicOp() {
        RenderSystem.assertOnRenderThread();
        COLOR_LOGIC.enable.disable();
    }

    public static void _logicOp(int int0) {
        RenderSystem.assertOnRenderThread();
        if (int0 != COLOR_LOGIC.op) {
            COLOR_LOGIC.op = int0;
            GL11.glLogicOp(int0);
        }
    }

    public static void _activeTexture(int int0) {
        RenderSystem.assertOnRenderThread();
        if (activeTexture != int0 - 33984) {
            activeTexture = int0 - 33984;
            glActiveTexture(int0);
        }
    }

    public static void _texParameter(int int0, int int1, float float2) {
        RenderSystem.assertOnRenderThreadOrInit();
        GL11.glTexParameterf(int0, int1, float2);
    }

    public static void _texParameter(int int0, int int1, int int2) {
        RenderSystem.assertOnRenderThreadOrInit();
        GL11.glTexParameteri(int0, int1, int2);
    }

    public static int _getTexLevelParameter(int int0, int int1, int int2) {
        RenderSystem.assertInInitPhase();
        return GL11.glGetTexLevelParameteri(int0, int1, int2);
    }

    public static int _genTexture() {
        RenderSystem.assertOnRenderThreadOrInit();
        return GL11.glGenTextures();
    }

    public static void _genTextures(int[] int0) {
        RenderSystem.assertOnRenderThreadOrInit();
        GL11.glGenTextures(int0);
    }

    public static void _deleteTexture(int int0) {
        RenderSystem.assertOnRenderThreadOrInit();
        GL11.glDeleteTextures(int0);
        for (GlStateManager.TextureState $$1 : TEXTURES) {
            if ($$1.binding == int0) {
                $$1.binding = -1;
            }
        }
    }

    public static void _deleteTextures(int[] int0) {
        RenderSystem.assertOnRenderThreadOrInit();
        for (GlStateManager.TextureState $$1 : TEXTURES) {
            for (int $$2 : int0) {
                if ($$1.binding == $$2) {
                    $$1.binding = -1;
                }
            }
        }
        GL11.glDeleteTextures(int0);
    }

    public static void _bindTexture(int int0) {
        RenderSystem.assertOnRenderThreadOrInit();
        if (int0 != TEXTURES[activeTexture].binding) {
            TEXTURES[activeTexture].binding = int0;
            GL11.glBindTexture(3553, int0);
        }
    }

    public static int _getActiveTexture() {
        return activeTexture + 33984;
    }

    public static void _texImage2D(int int0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, @Nullable IntBuffer intBuffer8) {
        RenderSystem.assertOnRenderThreadOrInit();
        GL11.glTexImage2D(int0, int1, int2, int3, int4, int5, int6, int7, intBuffer8);
    }

    public static void _texSubImage2D(int int0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, long long8) {
        RenderSystem.assertOnRenderThreadOrInit();
        GL11.glTexSubImage2D(int0, int1, int2, int3, int4, int5, int6, int7, long8);
    }

    public static void upload(int int0, int int1, int int2, int int3, int int4, NativeImage.Format nativeImageFormat5, IntBuffer intBuffer6, Consumer<IntBuffer> consumerIntBuffer7) {
        if (!RenderSystem.isOnRenderThreadOrInit()) {
            RenderSystem.recordRenderCall(() -> _upload(int0, int1, int2, int3, int4, nativeImageFormat5, intBuffer6, consumerIntBuffer7));
        } else {
            _upload(int0, int1, int2, int3, int4, nativeImageFormat5, intBuffer6, consumerIntBuffer7);
        }
    }

    private static void _upload(int int0, int int1, int int2, int int3, int int4, NativeImage.Format nativeImageFormat5, IntBuffer intBuffer6, Consumer<IntBuffer> consumerIntBuffer7) {
        try {
            RenderSystem.assertOnRenderThreadOrInit();
            _pixelStore(3314, int3);
            _pixelStore(3316, 0);
            _pixelStore(3315, 0);
            nativeImageFormat5.setUnpackPixelStoreState();
            GL11.glTexSubImage2D(3553, int0, int1, int2, int3, int4, nativeImageFormat5.glFormat(), 5121, intBuffer6);
        } finally {
            consumerIntBuffer7.accept(intBuffer6);
        }
    }

    public static void _getTexImage(int int0, int int1, int int2, int int3, long long4) {
        RenderSystem.assertOnRenderThread();
        GL11.glGetTexImage(int0, int1, int2, int3, long4);
    }

    public static void _viewport(int int0, int int1, int int2, int int3) {
        RenderSystem.assertOnRenderThreadOrInit();
        GlStateManager.Viewport.INSTANCE.x = int0;
        GlStateManager.Viewport.INSTANCE.y = int1;
        GlStateManager.Viewport.INSTANCE.width = int2;
        GlStateManager.Viewport.INSTANCE.height = int3;
        GL11.glViewport(int0, int1, int2, int3);
    }

    public static void _colorMask(boolean boolean0, boolean boolean1, boolean boolean2, boolean boolean3) {
        RenderSystem.assertOnRenderThread();
        if (boolean0 != COLOR_MASK.red || boolean1 != COLOR_MASK.green || boolean2 != COLOR_MASK.blue || boolean3 != COLOR_MASK.alpha) {
            COLOR_MASK.red = boolean0;
            COLOR_MASK.green = boolean1;
            COLOR_MASK.blue = boolean2;
            COLOR_MASK.alpha = boolean3;
            GL11.glColorMask(boolean0, boolean1, boolean2, boolean3);
        }
    }

    public static void _stencilFunc(int int0, int int1, int int2) {
        RenderSystem.assertOnRenderThread();
        if (int0 != STENCIL.func.func || int0 != STENCIL.func.ref || int0 != STENCIL.func.mask) {
            STENCIL.func.func = int0;
            STENCIL.func.ref = int1;
            STENCIL.func.mask = int2;
            GL11.glStencilFunc(int0, int1, int2);
        }
    }

    public static void _stencilMask(int int0) {
        RenderSystem.assertOnRenderThread();
        if (int0 != STENCIL.mask) {
            STENCIL.mask = int0;
            GL11.glStencilMask(int0);
        }
    }

    public static void _stencilOp(int int0, int int1, int int2) {
        RenderSystem.assertOnRenderThread();
        if (int0 != STENCIL.fail || int1 != STENCIL.zfail || int2 != STENCIL.zpass) {
            STENCIL.fail = int0;
            STENCIL.zfail = int1;
            STENCIL.zpass = int2;
            GL11.glStencilOp(int0, int1, int2);
        }
    }

    public static void _clearDepth(double double0) {
        RenderSystem.assertOnRenderThreadOrInit();
        GL11.glClearDepth(double0);
    }

    public static void _clearColor(float float0, float float1, float float2, float float3) {
        RenderSystem.assertOnRenderThreadOrInit();
        GL11.glClearColor(float0, float1, float2, float3);
    }

    public static void _clearStencil(int int0) {
        RenderSystem.assertOnRenderThread();
        GL11.glClearStencil(int0);
    }

    public static void _clear(int int0, boolean boolean1) {
        RenderSystem.assertOnRenderThreadOrInit();
        GL11.glClear(int0);
        if (boolean1) {
            _getError();
        }
    }

    public static void _glDrawPixels(int int0, int int1, int int2, int int3, long long4) {
        RenderSystem.assertOnRenderThread();
        GL11.glDrawPixels(int0, int1, int2, int3, long4);
    }

    public static void _vertexAttribPointer(int int0, int int1, int int2, boolean boolean3, int int4, long long5) {
        RenderSystem.assertOnRenderThread();
        GL20.glVertexAttribPointer(int0, int1, int2, boolean3, int4, long5);
    }

    public static void _vertexAttribIPointer(int int0, int int1, int int2, int int3, long long4) {
        RenderSystem.assertOnRenderThread();
        GL30.glVertexAttribIPointer(int0, int1, int2, int3, long4);
    }

    public static void _enableVertexAttribArray(int int0) {
        RenderSystem.assertOnRenderThread();
        GL20.glEnableVertexAttribArray(int0);
    }

    public static void _disableVertexAttribArray(int int0) {
        RenderSystem.assertOnRenderThread();
        GL20.glDisableVertexAttribArray(int0);
    }

    public static void _drawElements(int int0, int int1, int int2, long long3) {
        RenderSystem.assertOnRenderThread();
        GL11.glDrawElements(int0, int1, int2, long3);
    }

    public static void _pixelStore(int int0, int int1) {
        RenderSystem.assertOnRenderThreadOrInit();
        GL11.glPixelStorei(int0, int1);
    }

    public static void _readPixels(int int0, int int1, int int2, int int3, int int4, int int5, ByteBuffer byteBuffer6) {
        RenderSystem.assertOnRenderThread();
        GL11.glReadPixels(int0, int1, int2, int3, int4, int5, byteBuffer6);
    }

    public static void _readPixels(int int0, int int1, int int2, int int3, int int4, int int5, long long6) {
        RenderSystem.assertOnRenderThread();
        GL11.glReadPixels(int0, int1, int2, int3, int4, int5, long6);
    }

    public static int _getError() {
        RenderSystem.assertOnRenderThread();
        return GL11.glGetError();
    }

    public static String _getString(int int0) {
        RenderSystem.assertOnRenderThread();
        return GL11.glGetString(int0);
    }

    public static int _getInteger(int int0) {
        RenderSystem.assertOnRenderThreadOrInit();
        return GL11.glGetInteger(int0);
    }

    static class BlendState {

        public final GlStateManager.BooleanState mode = new GlStateManager.BooleanState(3042);

        public int srcRgb = 1;

        public int dstRgb = 0;

        public int srcAlpha = 1;

        public int dstAlpha = 0;
    }

    static class BooleanState {

        private final int state;

        private boolean enabled;

        public BooleanState(int int0) {
            this.state = int0;
        }

        public void disable() {
            this.setEnabled(false);
        }

        public void enable() {
            this.setEnabled(true);
        }

        public void setEnabled(boolean boolean0) {
            RenderSystem.assertOnRenderThreadOrInit();
            if (boolean0 != this.enabled) {
                this.enabled = boolean0;
                if (boolean0) {
                    GL11.glEnable(this.state);
                } else {
                    GL11.glDisable(this.state);
                }
            }
        }
    }

    static class ColorLogicState {

        public final GlStateManager.BooleanState enable = new GlStateManager.BooleanState(3058);

        public int op = 5379;
    }

    static class ColorMask {

        public boolean red = true;

        public boolean green = true;

        public boolean blue = true;

        public boolean alpha = true;
    }

    static class CullState {

        public final GlStateManager.BooleanState enable = new GlStateManager.BooleanState(2884);

        public int mode = 1029;
    }

    static class DepthState {

        public final GlStateManager.BooleanState mode = new GlStateManager.BooleanState(2929);

        public boolean mask = true;

        public int func = 513;
    }

    @DontObfuscate
    public static enum DestFactor {

        CONSTANT_ALPHA(32771),
        CONSTANT_COLOR(32769),
        DST_ALPHA(772),
        DST_COLOR(774),
        ONE(1),
        ONE_MINUS_CONSTANT_ALPHA(32772),
        ONE_MINUS_CONSTANT_COLOR(32770),
        ONE_MINUS_DST_ALPHA(773),
        ONE_MINUS_DST_COLOR(775),
        ONE_MINUS_SRC_ALPHA(771),
        ONE_MINUS_SRC_COLOR(769),
        SRC_ALPHA(770),
        SRC_COLOR(768),
        ZERO(0);

        public final int value;

        private DestFactor(int p_84652_) {
            this.value = p_84652_;
        }
    }

    public static enum LogicOp {

        AND(5377),
        AND_INVERTED(5380),
        AND_REVERSE(5378),
        CLEAR(5376),
        COPY(5379),
        COPY_INVERTED(5388),
        EQUIV(5385),
        INVERT(5386),
        NAND(5390),
        NOOP(5381),
        NOR(5384),
        OR(5383),
        OR_INVERTED(5389),
        OR_REVERSE(5387),
        SET(5391),
        XOR(5382);

        public final int value;

        private LogicOp(int p_84721_) {
            this.value = p_84721_;
        }
    }

    static class PolygonOffsetState {

        public final GlStateManager.BooleanState fill = new GlStateManager.BooleanState(32823);

        public final GlStateManager.BooleanState line = new GlStateManager.BooleanState(10754);

        public float factor;

        public float units;
    }

    static class ScissorState {

        public final GlStateManager.BooleanState mode = new GlStateManager.BooleanState(3089);
    }

    @DontObfuscate
    public static enum SourceFactor {

        CONSTANT_ALPHA(32771),
        CONSTANT_COLOR(32769),
        DST_ALPHA(772),
        DST_COLOR(774),
        ONE(1),
        ONE_MINUS_CONSTANT_ALPHA(32772),
        ONE_MINUS_CONSTANT_COLOR(32770),
        ONE_MINUS_DST_ALPHA(773),
        ONE_MINUS_DST_COLOR(775),
        ONE_MINUS_SRC_ALPHA(771),
        ONE_MINUS_SRC_COLOR(769),
        SRC_ALPHA(770),
        SRC_ALPHA_SATURATE(776),
        SRC_COLOR(768),
        ZERO(0);

        public final int value;

        private SourceFactor(int p_84757_) {
            this.value = p_84757_;
        }
    }

    static class StencilFunc {

        public int func = 519;

        public int ref;

        public int mask = -1;
    }

    static class StencilState {

        public final GlStateManager.StencilFunc func = new GlStateManager.StencilFunc();

        public int mask = -1;

        public int fail = 7680;

        public int zfail = 7680;

        public int zpass = 7680;
    }

    static class TextureState {

        public int binding;
    }

    public static enum Viewport {

        INSTANCE;

        protected int x;

        protected int y;

        protected int width;

        protected int height;

        public static int x() {
            return INSTANCE.x;
        }

        public static int y() {
            return INSTANCE.y;
        }

        public static int width() {
            return INSTANCE.width;
        }

        public static int height() {
            return INSTANCE.height;
        }
    }
}