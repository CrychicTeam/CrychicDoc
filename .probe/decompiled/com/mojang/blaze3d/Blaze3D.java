package com.mojang.blaze3d;

import com.mojang.blaze3d.pipeline.RenderCall;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

public class Blaze3D {

    public static void process(RenderPipeline renderPipeline0, float float1) {
        ConcurrentLinkedQueue<RenderCall> $$2 = renderPipeline0.getRecordingQueue();
    }

    public static void render(RenderPipeline renderPipeline0, float float1) {
        ConcurrentLinkedQueue<RenderCall> $$2 = renderPipeline0.getProcessedQueue();
    }

    public static void youJustLostTheGame() {
        MemoryUtil.memSet(0L, 0, 1L);
    }

    public static double getTime() {
        return GLFW.glfwGetTime();
    }
}