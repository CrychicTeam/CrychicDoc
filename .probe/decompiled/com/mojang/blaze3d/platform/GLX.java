package com.mojang.blaze3d.platform;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.DontObfuscate;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.logging.LogUtils;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import net.minecraft.client.renderer.GameRenderer;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWErrorCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.slf4j.Logger;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

@DontObfuscate
public class GLX {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static String cpuInfo;

    public static String getOpenGLVersionString() {
        RenderSystem.assertOnRenderThread();
        return GLFW.glfwGetCurrentContext() == 0L ? "NO CONTEXT" : GlStateManager._getString(7937) + " GL version " + GlStateManager._getString(7938) + ", " + GlStateManager._getString(7936);
    }

    public static int _getRefreshRate(Window window0) {
        RenderSystem.assertOnRenderThread();
        long $$1 = GLFW.glfwGetWindowMonitor(window0.getWindow());
        if ($$1 == 0L) {
            $$1 = GLFW.glfwGetPrimaryMonitor();
        }
        GLFWVidMode $$2 = $$1 == 0L ? null : GLFW.glfwGetVideoMode($$1);
        return $$2 == null ? 0 : $$2.refreshRate();
    }

    public static String _getLWJGLVersion() {
        RenderSystem.assertInInitPhase();
        return Version.getVersion();
    }

    public static LongSupplier _initGlfw() {
        RenderSystem.assertInInitPhase();
        Window.checkGlfwError((p_242032_, p_242033_) -> {
            throw new IllegalStateException(String.format(Locale.ROOT, "GLFW error before init: [0x%X]%s", p_242032_, p_242033_));
        });
        List<String> $$0 = Lists.newArrayList();
        GLFWErrorCallback $$1 = GLFW.glfwSetErrorCallback((p_69365_, p_69366_) -> $$0.add(String.format(Locale.ROOT, "GLFW error during init: [0x%X]%s", p_69365_, p_69366_)));
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW, errors: " + Joiner.on(",").join($$0));
        } else {
            LongSupplier $$2 = () -> (long) (GLFW.glfwGetTime() * 1.0E9);
            for (String $$3 : $$0) {
                LOGGER.error("GLFW error collected during initialization: {}", $$3);
            }
            RenderSystem.setErrorCallback($$1);
            return $$2;
        }
    }

    public static void _setGlfwErrorCallback(GLFWErrorCallbackI gLFWErrorCallbackI0) {
        RenderSystem.assertInInitPhase();
        GLFWErrorCallback $$1 = GLFW.glfwSetErrorCallback(gLFWErrorCallbackI0);
        if ($$1 != null) {
            $$1.free();
        }
    }

    public static boolean _shouldClose(Window window0) {
        return GLFW.glfwWindowShouldClose(window0.getWindow());
    }

    public static void _init(int int0, boolean boolean1) {
        RenderSystem.assertInInitPhase();
        try {
            CentralProcessor $$2 = new SystemInfo().getHardware().getProcessor();
            cpuInfo = String.format(Locale.ROOT, "%dx %s", $$2.getLogicalProcessorCount(), $$2.getProcessorIdentifier().getName()).replaceAll("\\s+", " ");
        } catch (Throwable var3) {
        }
        GlDebug.enableDebugCallback(int0, boolean1);
    }

    public static String _getCpuInfo() {
        return cpuInfo == null ? "<unknown>" : cpuInfo;
    }

    public static void _renderCrosshair(int int0, boolean boolean1, boolean boolean2, boolean boolean3) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._depthMask(false);
        GlStateManager._disableCull();
        RenderSystem.setShader(GameRenderer::m_172757_);
        Tesselator $$4 = RenderSystem.renderThreadTesselator();
        BufferBuilder $$5 = $$4.getBuilder();
        RenderSystem.lineWidth(4.0F);
        $$5.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);
        if (boolean1) {
            $$5.m_5483_(0.0, 0.0, 0.0).color(0, 0, 0, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
            $$5.m_5483_((double) int0, 0.0, 0.0).color(0, 0, 0, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
        }
        if (boolean2) {
            $$5.m_5483_(0.0, 0.0, 0.0).color(0, 0, 0, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
            $$5.m_5483_(0.0, (double) int0, 0.0).color(0, 0, 0, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
        }
        if (boolean3) {
            $$5.m_5483_(0.0, 0.0, 0.0).color(0, 0, 0, 255).normal(0.0F, 0.0F, 1.0F).endVertex();
            $$5.m_5483_(0.0, 0.0, (double) int0).color(0, 0, 0, 255).normal(0.0F, 0.0F, 1.0F).endVertex();
        }
        $$4.end();
        RenderSystem.lineWidth(2.0F);
        $$5.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);
        if (boolean1) {
            $$5.m_5483_(0.0, 0.0, 0.0).color(255, 0, 0, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
            $$5.m_5483_((double) int0, 0.0, 0.0).color(255, 0, 0, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
        }
        if (boolean2) {
            $$5.m_5483_(0.0, 0.0, 0.0).color(0, 255, 0, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
            $$5.m_5483_(0.0, (double) int0, 0.0).color(0, 255, 0, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
        }
        if (boolean3) {
            $$5.m_5483_(0.0, 0.0, 0.0).color(127, 127, 255, 255).normal(0.0F, 0.0F, 1.0F).endVertex();
            $$5.m_5483_(0.0, 0.0, (double) int0).color(127, 127, 255, 255).normal(0.0F, 0.0F, 1.0F).endVertex();
        }
        $$4.end();
        RenderSystem.lineWidth(1.0F);
        GlStateManager._enableCull();
        GlStateManager._depthMask(true);
    }

    public static <T> T make(Supplier<T> supplierT0) {
        return (T) supplierT0.get();
    }

    public static <T> T make(T t0, Consumer<T> consumerT1) {
        consumerT1.accept(t0);
        return t0;
    }
}