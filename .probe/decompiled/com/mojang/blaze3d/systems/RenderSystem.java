package com.mojang.blaze3d.systems;

import com.google.common.collect.Queues;
import com.mojang.blaze3d.DontObfuscate;
import com.mojang.blaze3d.pipeline.RenderCall;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexSorting;
import com.mojang.logging.LogUtils;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeSource;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallbackI;
import org.slf4j.Logger;

@DontObfuscate
public class RenderSystem {

    static final Logger LOGGER = LogUtils.getLogger();

    private static final ConcurrentLinkedQueue<RenderCall> recordingQueue = Queues.newConcurrentLinkedQueue();

    private static final Tesselator RENDER_THREAD_TESSELATOR = new Tesselator();

    private static final int MINIMUM_ATLAS_TEXTURE_SIZE = 1024;

    private static boolean isReplayingQueue;

    @Nullable
    private static Thread gameThread;

    @Nullable
    private static Thread renderThread;

    private static int MAX_SUPPORTED_TEXTURE_SIZE = -1;

    private static boolean isInInit;

    private static double lastDrawTime = Double.MIN_VALUE;

    private static final RenderSystem.AutoStorageIndexBuffer sharedSequential = new RenderSystem.AutoStorageIndexBuffer(1, 1, IntConsumer::accept);

    private static final RenderSystem.AutoStorageIndexBuffer sharedSequentialQuad = new RenderSystem.AutoStorageIndexBuffer(4, 6, (p_157398_, p_157399_) -> {
        p_157398_.accept(p_157399_ + 0);
        p_157398_.accept(p_157399_ + 1);
        p_157398_.accept(p_157399_ + 2);
        p_157398_.accept(p_157399_ + 2);
        p_157398_.accept(p_157399_ + 3);
        p_157398_.accept(p_157399_ + 0);
    });

    private static final RenderSystem.AutoStorageIndexBuffer sharedSequentialLines = new RenderSystem.AutoStorageIndexBuffer(4, 6, (p_157401_, p_157402_) -> {
        p_157401_.accept(p_157402_ + 0);
        p_157401_.accept(p_157402_ + 1);
        p_157401_.accept(p_157402_ + 2);
        p_157401_.accept(p_157402_ + 3);
        p_157401_.accept(p_157402_ + 2);
        p_157401_.accept(p_157402_ + 1);
    });

    private static Matrix3f inverseViewRotationMatrix = new Matrix3f().zero();

    private static Matrix4f projectionMatrix = new Matrix4f();

    private static Matrix4f savedProjectionMatrix = new Matrix4f();

    private static VertexSorting vertexSorting = VertexSorting.DISTANCE_TO_ORIGIN;

    private static VertexSorting savedVertexSorting = VertexSorting.DISTANCE_TO_ORIGIN;

    private static final PoseStack modelViewStack = new PoseStack();

    private static Matrix4f modelViewMatrix = new Matrix4f();

    private static Matrix4f textureMatrix = new Matrix4f();

    private static final int[] shaderTextures = new int[12];

    private static final float[] shaderColor = new float[] { 1.0F, 1.0F, 1.0F, 1.0F };

    private static float shaderGlintAlpha = 1.0F;

    private static float shaderFogStart;

    private static float shaderFogEnd = 1.0F;

    private static final float[] shaderFogColor = new float[] { 0.0F, 0.0F, 0.0F, 0.0F };

    private static FogShape shaderFogShape = FogShape.SPHERE;

    private static final Vector3f[] shaderLightDirections = new Vector3f[2];

    private static float shaderGameTime;

    private static float shaderLineWidth = 1.0F;

    private static String apiDescription = "Unknown";

    @Nullable
    private static ShaderInstance shader;

    private static final AtomicLong pollEventsWaitStart = new AtomicLong();

    private static final AtomicBoolean pollingEvents = new AtomicBoolean(false);

    public static void initRenderThread() {
        if (renderThread == null && gameThread != Thread.currentThread()) {
            renderThread = Thread.currentThread();
        } else {
            throw new IllegalStateException("Could not initialize render thread");
        }
    }

    public static boolean isOnRenderThread() {
        return Thread.currentThread() == renderThread;
    }

    public static boolean isOnRenderThreadOrInit() {
        return isInInit || isOnRenderThread();
    }

    public static void initGameThread(boolean boolean0) {
        boolean $$1 = renderThread == Thread.currentThread();
        if (gameThread == null && renderThread != null && $$1 != boolean0) {
            gameThread = Thread.currentThread();
        } else {
            throw new IllegalStateException("Could not initialize tick thread");
        }
    }

    public static boolean isOnGameThread() {
        return true;
    }

    public static void assertInInitPhase() {
        if (!isInInitPhase()) {
            throw constructThreadException();
        }
    }

    public static void assertOnGameThreadOrInit() {
        if (!isInInit && !isOnGameThread()) {
            throw constructThreadException();
        }
    }

    public static void assertOnRenderThreadOrInit() {
        if (!isInInit && !isOnRenderThread()) {
            throw constructThreadException();
        }
    }

    public static void assertOnRenderThread() {
        if (!isOnRenderThread()) {
            throw constructThreadException();
        }
    }

    public static void assertOnGameThread() {
        if (!isOnGameThread()) {
            throw constructThreadException();
        }
    }

    private static IllegalStateException constructThreadException() {
        return new IllegalStateException("Rendersystem called from wrong thread");
    }

    public static boolean isInInitPhase() {
        return true;
    }

    public static void recordRenderCall(RenderCall renderCall0) {
        recordingQueue.add(renderCall0);
    }

    private static void pollEvents() {
        pollEventsWaitStart.set(Util.getMillis());
        pollingEvents.set(true);
        GLFW.glfwPollEvents();
        pollingEvents.set(false);
    }

    public static boolean isFrozenAtPollEvents() {
        return pollingEvents.get() && Util.getMillis() - pollEventsWaitStart.get() > 200L;
    }

    public static void flipFrame(long long0) {
        pollEvents();
        replayQueue();
        Tesselator.getInstance().getBuilder().clear();
        GLFW.glfwSwapBuffers(long0);
        pollEvents();
    }

    public static void replayQueue() {
        isReplayingQueue = true;
        while (!recordingQueue.isEmpty()) {
            RenderCall $$0 = (RenderCall) recordingQueue.poll();
            $$0.execute();
        }
        isReplayingQueue = false;
    }

    public static void limitDisplayFPS(int int0) {
        double $$1 = lastDrawTime + 1.0 / (double) int0;
        double $$2;
        for ($$2 = GLFW.glfwGetTime(); $$2 < $$1; $$2 = GLFW.glfwGetTime()) {
            GLFW.glfwWaitEventsTimeout($$1 - $$2);
        }
        lastDrawTime = $$2;
    }

    public static void disableDepthTest() {
        assertOnRenderThread();
        GlStateManager._disableDepthTest();
    }

    public static void enableDepthTest() {
        assertOnGameThreadOrInit();
        GlStateManager._enableDepthTest();
    }

    public static void enableScissor(int int0, int int1, int int2, int int3) {
        assertOnGameThreadOrInit();
        GlStateManager._enableScissorTest();
        GlStateManager._scissorBox(int0, int1, int2, int3);
    }

    public static void disableScissor() {
        assertOnGameThreadOrInit();
        GlStateManager._disableScissorTest();
    }

    public static void depthFunc(int int0) {
        assertOnRenderThread();
        GlStateManager._depthFunc(int0);
    }

    public static void depthMask(boolean boolean0) {
        assertOnRenderThread();
        GlStateManager._depthMask(boolean0);
    }

    public static void enableBlend() {
        assertOnRenderThread();
        GlStateManager._enableBlend();
    }

    public static void disableBlend() {
        assertOnRenderThread();
        GlStateManager._disableBlend();
    }

    public static void blendFunc(GlStateManager.SourceFactor glStateManagerSourceFactor0, GlStateManager.DestFactor glStateManagerDestFactor1) {
        assertOnRenderThread();
        GlStateManager._blendFunc(glStateManagerSourceFactor0.value, glStateManagerDestFactor1.value);
    }

    public static void blendFunc(int int0, int int1) {
        assertOnRenderThread();
        GlStateManager._blendFunc(int0, int1);
    }

    public static void blendFuncSeparate(GlStateManager.SourceFactor glStateManagerSourceFactor0, GlStateManager.DestFactor glStateManagerDestFactor1, GlStateManager.SourceFactor glStateManagerSourceFactor2, GlStateManager.DestFactor glStateManagerDestFactor3) {
        assertOnRenderThread();
        GlStateManager._blendFuncSeparate(glStateManagerSourceFactor0.value, glStateManagerDestFactor1.value, glStateManagerSourceFactor2.value, glStateManagerDestFactor3.value);
    }

    public static void blendFuncSeparate(int int0, int int1, int int2, int int3) {
        assertOnRenderThread();
        GlStateManager._blendFuncSeparate(int0, int1, int2, int3);
    }

    public static void blendEquation(int int0) {
        assertOnRenderThread();
        GlStateManager._blendEquation(int0);
    }

    public static void enableCull() {
        assertOnRenderThread();
        GlStateManager._enableCull();
    }

    public static void disableCull() {
        assertOnRenderThread();
        GlStateManager._disableCull();
    }

    public static void polygonMode(int int0, int int1) {
        assertOnRenderThread();
        GlStateManager._polygonMode(int0, int1);
    }

    public static void enablePolygonOffset() {
        assertOnRenderThread();
        GlStateManager._enablePolygonOffset();
    }

    public static void disablePolygonOffset() {
        assertOnRenderThread();
        GlStateManager._disablePolygonOffset();
    }

    public static void polygonOffset(float float0, float float1) {
        assertOnRenderThread();
        GlStateManager._polygonOffset(float0, float1);
    }

    public static void enableColorLogicOp() {
        assertOnRenderThread();
        GlStateManager._enableColorLogicOp();
    }

    public static void disableColorLogicOp() {
        assertOnRenderThread();
        GlStateManager._disableColorLogicOp();
    }

    public static void logicOp(GlStateManager.LogicOp glStateManagerLogicOp0) {
        assertOnRenderThread();
        GlStateManager._logicOp(glStateManagerLogicOp0.value);
    }

    public static void activeTexture(int int0) {
        assertOnRenderThread();
        GlStateManager._activeTexture(int0);
    }

    public static void texParameter(int int0, int int1, int int2) {
        GlStateManager._texParameter(int0, int1, int2);
    }

    public static void deleteTexture(int int0) {
        assertOnGameThreadOrInit();
        GlStateManager._deleteTexture(int0);
    }

    public static void bindTextureForSetup(int int0) {
        bindTexture(int0);
    }

    public static void bindTexture(int int0) {
        GlStateManager._bindTexture(int0);
    }

    public static void viewport(int int0, int int1, int int2, int int3) {
        assertOnGameThreadOrInit();
        GlStateManager._viewport(int0, int1, int2, int3);
    }

    public static void colorMask(boolean boolean0, boolean boolean1, boolean boolean2, boolean boolean3) {
        assertOnRenderThread();
        GlStateManager._colorMask(boolean0, boolean1, boolean2, boolean3);
    }

    public static void stencilFunc(int int0, int int1, int int2) {
        assertOnRenderThread();
        GlStateManager._stencilFunc(int0, int1, int2);
    }

    public static void stencilMask(int int0) {
        assertOnRenderThread();
        GlStateManager._stencilMask(int0);
    }

    public static void stencilOp(int int0, int int1, int int2) {
        assertOnRenderThread();
        GlStateManager._stencilOp(int0, int1, int2);
    }

    public static void clearDepth(double double0) {
        assertOnGameThreadOrInit();
        GlStateManager._clearDepth(double0);
    }

    public static void clearColor(float float0, float float1, float float2, float float3) {
        assertOnGameThreadOrInit();
        GlStateManager._clearColor(float0, float1, float2, float3);
    }

    public static void clearStencil(int int0) {
        assertOnRenderThread();
        GlStateManager._clearStencil(int0);
    }

    public static void clear(int int0, boolean boolean1) {
        assertOnGameThreadOrInit();
        GlStateManager._clear(int0, boolean1);
    }

    public static void setShaderFogStart(float float0) {
        assertOnRenderThread();
        _setShaderFogStart(float0);
    }

    private static void _setShaderFogStart(float float0) {
        shaderFogStart = float0;
    }

    public static float getShaderFogStart() {
        assertOnRenderThread();
        return shaderFogStart;
    }

    public static void setShaderGlintAlpha(double double0) {
        setShaderGlintAlpha((float) double0);
    }

    public static void setShaderGlintAlpha(float float0) {
        assertOnRenderThread();
        _setShaderGlintAlpha(float0);
    }

    private static void _setShaderGlintAlpha(float float0) {
        shaderGlintAlpha = float0;
    }

    public static float getShaderGlintAlpha() {
        assertOnRenderThread();
        return shaderGlintAlpha;
    }

    public static void setShaderFogEnd(float float0) {
        assertOnRenderThread();
        _setShaderFogEnd(float0);
    }

    private static void _setShaderFogEnd(float float0) {
        shaderFogEnd = float0;
    }

    public static float getShaderFogEnd() {
        assertOnRenderThread();
        return shaderFogEnd;
    }

    public static void setShaderFogColor(float float0, float float1, float float2, float float3) {
        assertOnRenderThread();
        _setShaderFogColor(float0, float1, float2, float3);
    }

    public static void setShaderFogColor(float float0, float float1, float float2) {
        setShaderFogColor(float0, float1, float2, 1.0F);
    }

    private static void _setShaderFogColor(float float0, float float1, float float2, float float3) {
        shaderFogColor[0] = float0;
        shaderFogColor[1] = float1;
        shaderFogColor[2] = float2;
        shaderFogColor[3] = float3;
    }

    public static float[] getShaderFogColor() {
        assertOnRenderThread();
        return shaderFogColor;
    }

    public static void setShaderFogShape(FogShape fogShape0) {
        assertOnRenderThread();
        _setShaderFogShape(fogShape0);
    }

    private static void _setShaderFogShape(FogShape fogShape0) {
        shaderFogShape = fogShape0;
    }

    public static FogShape getShaderFogShape() {
        assertOnRenderThread();
        return shaderFogShape;
    }

    public static void setShaderLights(Vector3f vectorF0, Vector3f vectorF1) {
        assertOnRenderThread();
        _setShaderLights(vectorF0, vectorF1);
    }

    public static void _setShaderLights(Vector3f vectorF0, Vector3f vectorF1) {
        shaderLightDirections[0] = vectorF0;
        shaderLightDirections[1] = vectorF1;
    }

    public static void setupShaderLights(ShaderInstance shaderInstance0) {
        assertOnRenderThread();
        if (shaderInstance0.LIGHT0_DIRECTION != null) {
            shaderInstance0.LIGHT0_DIRECTION.set(shaderLightDirections[0]);
        }
        if (shaderInstance0.LIGHT1_DIRECTION != null) {
            shaderInstance0.LIGHT1_DIRECTION.set(shaderLightDirections[1]);
        }
    }

    public static void setShaderColor(float float0, float float1, float float2, float float3) {
        if (!isOnRenderThread()) {
            recordRenderCall(() -> _setShaderColor(float0, float1, float2, float3));
        } else {
            _setShaderColor(float0, float1, float2, float3);
        }
    }

    private static void _setShaderColor(float float0, float float1, float float2, float float3) {
        shaderColor[0] = float0;
        shaderColor[1] = float1;
        shaderColor[2] = float2;
        shaderColor[3] = float3;
    }

    public static float[] getShaderColor() {
        assertOnRenderThread();
        return shaderColor;
    }

    public static void drawElements(int int0, int int1, int int2) {
        assertOnRenderThread();
        GlStateManager._drawElements(int0, int1, int2, 0L);
    }

    public static void lineWidth(float float0) {
        if (!isOnRenderThread()) {
            recordRenderCall(() -> shaderLineWidth = float0);
        } else {
            shaderLineWidth = float0;
        }
    }

    public static float getShaderLineWidth() {
        assertOnRenderThread();
        return shaderLineWidth;
    }

    public static void pixelStore(int int0, int int1) {
        assertOnGameThreadOrInit();
        GlStateManager._pixelStore(int0, int1);
    }

    public static void readPixels(int int0, int int1, int int2, int int3, int int4, int int5, ByteBuffer byteBuffer6) {
        assertOnRenderThread();
        GlStateManager._readPixels(int0, int1, int2, int3, int4, int5, byteBuffer6);
    }

    public static void getString(int int0, Consumer<String> consumerString1) {
        assertOnRenderThread();
        consumerString1.accept(GlStateManager._getString(int0));
    }

    public static String getBackendDescription() {
        assertInInitPhase();
        return String.format(Locale.ROOT, "LWJGL version %s", GLX._getLWJGLVersion());
    }

    public static String getApiDescription() {
        return apiDescription;
    }

    public static TimeSource.NanoTimeSource initBackendSystem() {
        assertInInitPhase();
        return GLX._initGlfw()::getAsLong;
    }

    public static void initRenderer(int int0, boolean boolean1) {
        assertInInitPhase();
        GLX._init(int0, boolean1);
        apiDescription = GLX.getOpenGLVersionString();
    }

    public static void setErrorCallback(GLFWErrorCallbackI gLFWErrorCallbackI0) {
        assertInInitPhase();
        GLX._setGlfwErrorCallback(gLFWErrorCallbackI0);
    }

    public static void renderCrosshair(int int0) {
        assertOnRenderThread();
        GLX._renderCrosshair(int0, true, true, true);
    }

    public static String getCapsString() {
        assertOnRenderThread();
        return "Using framebuffer using OpenGL 3.2";
    }

    public static void setupDefaultState(int int0, int int1, int int2, int int3) {
        assertInInitPhase();
        GlStateManager._clearDepth(1.0);
        GlStateManager._enableDepthTest();
        GlStateManager._depthFunc(515);
        projectionMatrix.identity();
        savedProjectionMatrix.identity();
        modelViewMatrix.identity();
        textureMatrix.identity();
        GlStateManager._viewport(int0, int1, int2, int3);
    }

    public static int maxSupportedTextureSize() {
        if (MAX_SUPPORTED_TEXTURE_SIZE == -1) {
            assertOnRenderThreadOrInit();
            int $$0 = GlStateManager._getInteger(3379);
            for (int $$1 = Math.max(32768, $$0); $$1 >= 1024; $$1 >>= 1) {
                GlStateManager._texImage2D(32868, 0, 6408, $$1, $$1, 0, 6408, 5121, null);
                int $$2 = GlStateManager._getTexLevelParameter(32868, 0, 4096);
                if ($$2 != 0) {
                    MAX_SUPPORTED_TEXTURE_SIZE = $$1;
                    return $$1;
                }
            }
            MAX_SUPPORTED_TEXTURE_SIZE = Math.max($$0, 1024);
            LOGGER.info("Failed to determine maximum texture size by probing, trying GL_MAX_TEXTURE_SIZE = {}", MAX_SUPPORTED_TEXTURE_SIZE);
        }
        return MAX_SUPPORTED_TEXTURE_SIZE;
    }

    public static void glBindBuffer(int int0, IntSupplier intSupplier1) {
        GlStateManager._glBindBuffer(int0, intSupplier1.getAsInt());
    }

    public static void glBindVertexArray(Supplier<Integer> supplierInteger0) {
        GlStateManager._glBindVertexArray((Integer) supplierInteger0.get());
    }

    public static void glBufferData(int int0, ByteBuffer byteBuffer1, int int2) {
        assertOnRenderThreadOrInit();
        GlStateManager._glBufferData(int0, byteBuffer1, int2);
    }

    public static void glDeleteBuffers(int int0) {
        assertOnRenderThread();
        GlStateManager._glDeleteBuffers(int0);
    }

    public static void glDeleteVertexArrays(int int0) {
        assertOnRenderThread();
        GlStateManager._glDeleteVertexArrays(int0);
    }

    public static void glUniform1i(int int0, int int1) {
        assertOnRenderThread();
        GlStateManager._glUniform1i(int0, int1);
    }

    public static void glUniform1(int int0, IntBuffer intBuffer1) {
        assertOnRenderThread();
        GlStateManager._glUniform1(int0, intBuffer1);
    }

    public static void glUniform2(int int0, IntBuffer intBuffer1) {
        assertOnRenderThread();
        GlStateManager._glUniform2(int0, intBuffer1);
    }

    public static void glUniform3(int int0, IntBuffer intBuffer1) {
        assertOnRenderThread();
        GlStateManager._glUniform3(int0, intBuffer1);
    }

    public static void glUniform4(int int0, IntBuffer intBuffer1) {
        assertOnRenderThread();
        GlStateManager._glUniform4(int0, intBuffer1);
    }

    public static void glUniform1(int int0, FloatBuffer floatBuffer1) {
        assertOnRenderThread();
        GlStateManager._glUniform1(int0, floatBuffer1);
    }

    public static void glUniform2(int int0, FloatBuffer floatBuffer1) {
        assertOnRenderThread();
        GlStateManager._glUniform2(int0, floatBuffer1);
    }

    public static void glUniform3(int int0, FloatBuffer floatBuffer1) {
        assertOnRenderThread();
        GlStateManager._glUniform3(int0, floatBuffer1);
    }

    public static void glUniform4(int int0, FloatBuffer floatBuffer1) {
        assertOnRenderThread();
        GlStateManager._glUniform4(int0, floatBuffer1);
    }

    public static void glUniformMatrix2(int int0, boolean boolean1, FloatBuffer floatBuffer2) {
        assertOnRenderThread();
        GlStateManager._glUniformMatrix2(int0, boolean1, floatBuffer2);
    }

    public static void glUniformMatrix3(int int0, boolean boolean1, FloatBuffer floatBuffer2) {
        assertOnRenderThread();
        GlStateManager._glUniformMatrix3(int0, boolean1, floatBuffer2);
    }

    public static void glUniformMatrix4(int int0, boolean boolean1, FloatBuffer floatBuffer2) {
        assertOnRenderThread();
        GlStateManager._glUniformMatrix4(int0, boolean1, floatBuffer2);
    }

    public static void setupOverlayColor(IntSupplier intSupplier0, int int1) {
        assertOnRenderThread();
        int $$2 = intSupplier0.getAsInt();
        setShaderTexture(1, $$2);
    }

    public static void teardownOverlayColor() {
        assertOnRenderThread();
        setShaderTexture(1, 0);
    }

    public static void setupLevelDiffuseLighting(Vector3f vectorF0, Vector3f vectorF1, Matrix4f matrixF2) {
        assertOnRenderThread();
        GlStateManager.setupLevelDiffuseLighting(vectorF0, vectorF1, matrixF2);
    }

    public static void setupGuiFlatDiffuseLighting(Vector3f vectorF0, Vector3f vectorF1) {
        assertOnRenderThread();
        GlStateManager.setupGuiFlatDiffuseLighting(vectorF0, vectorF1);
    }

    public static void setupGui3DDiffuseLighting(Vector3f vectorF0, Vector3f vectorF1) {
        assertOnRenderThread();
        GlStateManager.setupGui3DDiffuseLighting(vectorF0, vectorF1);
    }

    public static void beginInitialization() {
        isInInit = true;
    }

    public static void finishInitialization() {
        isInInit = false;
        if (!recordingQueue.isEmpty()) {
            replayQueue();
        }
        if (!recordingQueue.isEmpty()) {
            throw new IllegalStateException("Recorded to render queue during initialization");
        }
    }

    public static void glGenBuffers(Consumer<Integer> consumerInteger0) {
        if (!isOnRenderThread()) {
            recordRenderCall(() -> consumerInteger0.accept(GlStateManager._glGenBuffers()));
        } else {
            consumerInteger0.accept(GlStateManager._glGenBuffers());
        }
    }

    public static void glGenVertexArrays(Consumer<Integer> consumerInteger0) {
        if (!isOnRenderThread()) {
            recordRenderCall(() -> consumerInteger0.accept(GlStateManager._glGenVertexArrays()));
        } else {
            consumerInteger0.accept(GlStateManager._glGenVertexArrays());
        }
    }

    public static Tesselator renderThreadTesselator() {
        assertOnRenderThread();
        return RENDER_THREAD_TESSELATOR;
    }

    public static void defaultBlendFunc() {
        blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    }

    @Deprecated
    public static void runAsFancy(Runnable runnable0) {
        boolean $$1 = Minecraft.useShaderTransparency();
        if (!$$1) {
            runnable0.run();
        } else {
            OptionInstance<GraphicsStatus> $$2 = Minecraft.getInstance().options.graphicsMode();
            GraphicsStatus $$3 = $$2.get();
            $$2.set(GraphicsStatus.FANCY);
            runnable0.run();
            $$2.set($$3);
        }
    }

    public static void setShader(Supplier<ShaderInstance> supplierShaderInstance0) {
        if (!isOnRenderThread()) {
            recordRenderCall(() -> shader = (ShaderInstance) supplierShaderInstance0.get());
        } else {
            shader = (ShaderInstance) supplierShaderInstance0.get();
        }
    }

    @Nullable
    public static ShaderInstance getShader() {
        assertOnRenderThread();
        return shader;
    }

    public static void setShaderTexture(int int0, ResourceLocation resourceLocation1) {
        if (!isOnRenderThread()) {
            recordRenderCall(() -> _setShaderTexture(int0, resourceLocation1));
        } else {
            _setShaderTexture(int0, resourceLocation1);
        }
    }

    public static void _setShaderTexture(int int0, ResourceLocation resourceLocation1) {
        if (int0 >= 0 && int0 < shaderTextures.length) {
            TextureManager $$2 = Minecraft.getInstance().getTextureManager();
            AbstractTexture $$3 = $$2.getTexture(resourceLocation1);
            shaderTextures[int0] = $$3.getId();
        }
    }

    public static void setShaderTexture(int int0, int int1) {
        if (!isOnRenderThread()) {
            recordRenderCall(() -> _setShaderTexture(int0, int1));
        } else {
            _setShaderTexture(int0, int1);
        }
    }

    public static void _setShaderTexture(int int0, int int1) {
        if (int0 >= 0 && int0 < shaderTextures.length) {
            shaderTextures[int0] = int1;
        }
    }

    public static int getShaderTexture(int int0) {
        assertOnRenderThread();
        return int0 >= 0 && int0 < shaderTextures.length ? shaderTextures[int0] : 0;
    }

    public static void setProjectionMatrix(Matrix4f matrixF0, VertexSorting vertexSorting1) {
        Matrix4f $$2 = new Matrix4f(matrixF0);
        if (!isOnRenderThread()) {
            recordRenderCall(() -> {
                projectionMatrix = $$2;
                vertexSorting = vertexSorting1;
            });
        } else {
            projectionMatrix = $$2;
            vertexSorting = vertexSorting1;
        }
    }

    public static void setInverseViewRotationMatrix(Matrix3f matrixF0) {
        Matrix3f $$1 = new Matrix3f(matrixF0);
        if (!isOnRenderThread()) {
            recordRenderCall(() -> inverseViewRotationMatrix = $$1);
        } else {
            inverseViewRotationMatrix = $$1;
        }
    }

    public static void setTextureMatrix(Matrix4f matrixF0) {
        Matrix4f $$1 = new Matrix4f(matrixF0);
        if (!isOnRenderThread()) {
            recordRenderCall(() -> textureMatrix = $$1);
        } else {
            textureMatrix = $$1;
        }
    }

    public static void resetTextureMatrix() {
        if (!isOnRenderThread()) {
            recordRenderCall(() -> textureMatrix.identity());
        } else {
            textureMatrix.identity();
        }
    }

    public static void applyModelViewMatrix() {
        Matrix4f $$0 = new Matrix4f(modelViewStack.last().pose());
        if (!isOnRenderThread()) {
            recordRenderCall(() -> modelViewMatrix = $$0);
        } else {
            modelViewMatrix = $$0;
        }
    }

    public static void backupProjectionMatrix() {
        if (!isOnRenderThread()) {
            recordRenderCall(() -> _backupProjectionMatrix());
        } else {
            _backupProjectionMatrix();
        }
    }

    private static void _backupProjectionMatrix() {
        savedProjectionMatrix = projectionMatrix;
        savedVertexSorting = vertexSorting;
    }

    public static void restoreProjectionMatrix() {
        if (!isOnRenderThread()) {
            recordRenderCall(() -> _restoreProjectionMatrix());
        } else {
            _restoreProjectionMatrix();
        }
    }

    private static void _restoreProjectionMatrix() {
        projectionMatrix = savedProjectionMatrix;
        vertexSorting = savedVertexSorting;
    }

    public static Matrix4f getProjectionMatrix() {
        assertOnRenderThread();
        return projectionMatrix;
    }

    public static Matrix3f getInverseViewRotationMatrix() {
        assertOnRenderThread();
        return inverseViewRotationMatrix;
    }

    public static Matrix4f getModelViewMatrix() {
        assertOnRenderThread();
        return modelViewMatrix;
    }

    public static PoseStack getModelViewStack() {
        return modelViewStack;
    }

    public static Matrix4f getTextureMatrix() {
        assertOnRenderThread();
        return textureMatrix;
    }

    public static RenderSystem.AutoStorageIndexBuffer getSequentialBuffer(VertexFormat.Mode vertexFormatMode0) {
        assertOnRenderThread();
        return switch(vertexFormatMode0) {
            case QUADS ->
                sharedSequentialQuad;
            case LINES ->
                sharedSequentialLines;
            default ->
                sharedSequential;
        };
    }

    public static void setShaderGameTime(long long0, float float1) {
        float $$2 = ((float) (long0 % 24000L) + float1) / 24000.0F;
        if (!isOnRenderThread()) {
            recordRenderCall(() -> shaderGameTime = $$2);
        } else {
            shaderGameTime = $$2;
        }
    }

    public static float getShaderGameTime() {
        assertOnRenderThread();
        return shaderGameTime;
    }

    public static VertexSorting getVertexSorting() {
        assertOnRenderThread();
        return vertexSorting;
    }

    public static final class AutoStorageIndexBuffer {

        private final int vertexStride;

        private final int indexStride;

        private final RenderSystem.AutoStorageIndexBuffer.IndexGenerator generator;

        private int name;

        private VertexFormat.IndexType type = VertexFormat.IndexType.SHORT;

        private int indexCount;

        AutoStorageIndexBuffer(int int0, int int1, RenderSystem.AutoStorageIndexBuffer.IndexGenerator renderSystemAutoStorageIndexBufferIndexGenerator2) {
            this.vertexStride = int0;
            this.indexStride = int1;
            this.generator = renderSystemAutoStorageIndexBufferIndexGenerator2;
        }

        public boolean hasStorage(int int0) {
            return int0 <= this.indexCount;
        }

        public void bind(int int0) {
            if (this.name == 0) {
                this.name = GlStateManager._glGenBuffers();
            }
            GlStateManager._glBindBuffer(34963, this.name);
            this.ensureStorage(int0);
        }

        private void ensureStorage(int int0) {
            if (!this.hasStorage(int0)) {
                int0 = Mth.roundToward(int0 * 2, this.indexStride);
                RenderSystem.LOGGER.debug("Growing IndexBuffer: Old limit {}, new limit {}.", this.indexCount, int0);
                VertexFormat.IndexType $$1 = VertexFormat.IndexType.least(int0);
                int $$2 = Mth.roundToward(int0 * $$1.bytes, 4);
                GlStateManager._glBufferData(34963, (long) $$2, 35048);
                ByteBuffer $$3 = GlStateManager._glMapBuffer(34963, 35001);
                if ($$3 == null) {
                    throw new RuntimeException("Failed to map GL buffer");
                } else {
                    this.type = $$1;
                    it.unimi.dsi.fastutil.ints.IntConsumer $$4 = this.intConsumer($$3);
                    for (int $$5 = 0; $$5 < int0; $$5 += this.indexStride) {
                        this.generator.accept($$4, $$5 * this.vertexStride / this.indexStride);
                    }
                    GlStateManager._glUnmapBuffer(34963);
                    this.indexCount = int0;
                }
            }
        }

        private it.unimi.dsi.fastutil.ints.IntConsumer intConsumer(ByteBuffer byteBuffer0) {
            switch(this.type) {
                case SHORT:
                    return p_157482_ -> byteBuffer0.putShort((short) p_157482_);
                case INT:
                default:
                    return byteBuffer0::putInt;
            }
        }

        public VertexFormat.IndexType type() {
            return this.type;
        }

        interface IndexGenerator {

            void accept(it.unimi.dsi.fastutil.ints.IntConsumer var1, int var2);
        }
    }
}