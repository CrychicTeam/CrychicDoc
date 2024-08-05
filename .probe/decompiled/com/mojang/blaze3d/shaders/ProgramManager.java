package com.mojang.blaze3d.shaders;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import org.slf4j.Logger;

public class ProgramManager {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static void glUseProgram(int int0) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._glUseProgram(int0);
    }

    public static void releaseProgram(Shader shader0) {
        RenderSystem.assertOnRenderThread();
        shader0.getFragmentProgram().close();
        shader0.getVertexProgram().close();
        GlStateManager.glDeleteProgram(shader0.getId());
    }

    public static int createProgram() throws IOException {
        RenderSystem.assertOnRenderThread();
        int $$0 = GlStateManager.glCreateProgram();
        if ($$0 <= 0) {
            throw new IOException("Could not create shader program (returned program ID " + $$0 + ")");
        } else {
            return $$0;
        }
    }

    public static void linkShader(Shader shader0) {
        RenderSystem.assertOnRenderThread();
        shader0.attachToProgram();
        GlStateManager.glLinkProgram(shader0.getId());
        int $$1 = GlStateManager.glGetProgrami(shader0.getId(), 35714);
        if ($$1 == 0) {
            LOGGER.warn("Error encountered when linking program containing VS {} and FS {}. Log output:", shader0.getVertexProgram().getName(), shader0.getFragmentProgram().getName());
            LOGGER.warn(GlStateManager.glGetProgramInfoLog(shader0.getId(), 32768));
        }
    }
}