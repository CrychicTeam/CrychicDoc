package org.embeddedt.modernfix.forge.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;
import net.minecraftforge.fml.loading.ImmediateWindowHandler;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

public class AsyncLoadingScreen extends Thread implements AutoCloseable {

    private final long theWindow;

    private final AtomicBoolean keepRunning;

    private static int splashThreadNum = 1;

    private static GLCapabilities caps;

    public AsyncLoadingScreen() {
        this.setName("ModernFix splash thread " + splashThreadNum++);
        this.theWindow = GLFW.glfwGetCurrentContext();
        if (caps == null) {
            caps = GL.createCapabilities();
        }
        if (this.theWindow == 0L) {
            throw new IllegalStateException("No context found but async loading screen was requested");
        } else {
            this.keepRunning = new AtomicBoolean(true);
            this.start();
        }
    }

    public synchronized void start() {
        GLFW.glfwMakeContextCurrent(0L);
        super.start();
    }

    public void run() {
        GLFW.glfwMakeContextCurrent(this.theWindow);
        GL.setCapabilities(caps);
        while (this.keepRunning.get()) {
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(50L));
            ImmediateWindowHandler.renderTick();
        }
        GLFW.glfwMakeContextCurrent(0L);
    }

    public void close() {
        this.keepRunning.set(false);
        try {
            this.join();
        } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
        }
        GLFW.glfwMakeContextCurrent(this.theWindow);
    }
}