package com.mojang.blaze3d.platform;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWVidMode.Buffer;

public final class Monitor {

    private final long monitor;

    private final List<VideoMode> videoModes;

    private VideoMode currentMode;

    private int x;

    private int y;

    public Monitor(long long0) {
        this.monitor = long0;
        this.videoModes = Lists.newArrayList();
        this.refreshVideoModes();
    }

    public void refreshVideoModes() {
        RenderSystem.assertInInitPhase();
        this.videoModes.clear();
        Buffer $$0 = GLFW.glfwGetVideoModes(this.monitor);
        for (int $$1 = $$0.limit() - 1; $$1 >= 0; $$1--) {
            $$0.position($$1);
            VideoMode $$2 = new VideoMode($$0);
            if ($$2.getRedBits() >= 8 && $$2.getGreenBits() >= 8 && $$2.getBlueBits() >= 8) {
                this.videoModes.add($$2);
            }
        }
        int[] $$3 = new int[1];
        int[] $$4 = new int[1];
        GLFW.glfwGetMonitorPos(this.monitor, $$3, $$4);
        this.x = $$3[0];
        this.y = $$4[0];
        GLFWVidMode $$5 = GLFW.glfwGetVideoMode(this.monitor);
        this.currentMode = new VideoMode($$5);
    }

    public VideoMode getPreferredVidMode(Optional<VideoMode> optionalVideoMode0) {
        RenderSystem.assertInInitPhase();
        if (optionalVideoMode0.isPresent()) {
            VideoMode $$1 = (VideoMode) optionalVideoMode0.get();
            for (VideoMode $$2 : this.videoModes) {
                if ($$2.equals($$1)) {
                    return $$2;
                }
            }
        }
        return this.getCurrentMode();
    }

    public int getVideoModeIndex(VideoMode videoMode0) {
        RenderSystem.assertInInitPhase();
        return this.videoModes.indexOf(videoMode0);
    }

    public VideoMode getCurrentMode() {
        return this.currentMode;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public VideoMode getMode(int int0) {
        return (VideoMode) this.videoModes.get(int0);
    }

    public int getModeCount() {
        return this.videoModes.size();
    }

    public long getMonitor() {
        return this.monitor;
    }

    public String toString() {
        return String.format(Locale.ROOT, "Monitor[%s %sx%s %s]", this.monitor, this.x, this.y, this.currentMode);
    }
}