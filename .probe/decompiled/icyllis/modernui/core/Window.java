package icyllis.modernui.core;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.graphics.Bitmap;
import icyllis.modernui.util.LongSparseArray;
import java.nio.IntBuffer;
import javax.annotation.concurrent.NotThreadSafe;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWImage.Buffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.NativeType;

@NotThreadSafe
public class Window implements AutoCloseable {

    private static final LongSparseArray<Window> sWindows = new LongSparseArray<>();

    public static final int LAST_SYSTEM_WINDOW = 2999;

    protected final long mHandle;

    public Window(long handle) {
        Core.checkMainThread();
        this.mHandle = handle;
        if (sWindows.put(handle, this) != null) {
            throw new IllegalStateException("Duplicated window: 0x" + Long.toHexString(handle));
        }
    }

    @Nullable
    public static Window get(@NativeType("GLFWwindow *") long handle) {
        return sWindows.get(handle);
    }

    @NativeType("GLFWwindow *")
    public final long getHandle() {
        return this.mHandle;
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(this.mHandle);
    }

    public void setShouldClose(boolean value) {
        GLFW.glfwSetWindowShouldClose(this.mHandle, value);
    }

    public void setTitle(@NonNull String title) {
        GLFW.glfwSetWindowTitle(this.mHandle, title);
    }

    public void setIcon(@Nullable Bitmap... icons) {
        if (icons != null && icons.length != 0) {
            MemoryStack stack = MemoryStack.stackPush();
            try {
                Buffer images = GLFWImage.malloc(icons.length, stack);
                for (int i = 0; i < icons.length; i++) {
                    Bitmap icon = icons[i];
                    images.position(i);
                    images.width(icon.getWidth());
                    images.height(icon.getHeight());
                    MemoryUtil.memPutAddress(images.address() + (long) GLFWImage.PIXELS, icon.getAddress());
                }
                images.flip();
                GLFW.glfwSetWindowIcon(this.mHandle, images);
            } catch (Throwable var7) {
                if (stack != null) {
                    try {
                        stack.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }
                throw var7;
            }
            if (stack != null) {
                stack.close();
            }
        } else {
            GLFW.nglfwSetWindowIcon(this.mHandle, 0, 0L);
        }
    }

    public void makeCurrent() {
        GLFW.glfwMakeContextCurrent(this.mHandle);
    }

    public void swapBuffers() {
        GLFW.glfwSwapBuffers(this.mHandle);
    }

    public void swapInterval(int interval) {
        GLFW.glfwSwapInterval(interval);
    }

    public int getWidth() {
        MemoryStack stack = MemoryStack.stackPush();
        int var3;
        try {
            IntBuffer w = stack.mallocInt(1);
            GLFW.glfwGetFramebufferSize(this.mHandle, w, null);
            var3 = w.get(0);
        } catch (Throwable var5) {
            if (stack != null) {
                try {
                    stack.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }
            }
            throw var5;
        }
        if (stack != null) {
            stack.close();
        }
        return var3;
    }

    public int getHeight() {
        MemoryStack stack = MemoryStack.stackPush();
        int var3;
        try {
            IntBuffer h = stack.mallocInt(1);
            GLFW.glfwGetFramebufferSize(this.mHandle, null, h);
            var3 = h.get(0);
        } catch (Throwable var5) {
            if (stack != null) {
                try {
                    stack.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }
            }
            throw var5;
        }
        if (stack != null) {
            stack.close();
        }
        return var3;
    }

    public int getScreenY() {
        MemoryStack stack = MemoryStack.stackPush();
        int var3;
        try {
            IntBuffer w = stack.mallocInt(1);
            GLFW.glfwGetWindowPos(this.mHandle, w, null);
            var3 = w.get(0);
        } catch (Throwable var5) {
            if (stack != null) {
                try {
                    stack.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }
            }
            throw var5;
        }
        if (stack != null) {
            stack.close();
        }
        return var3;
    }

    public int getScreenX() {
        MemoryStack stack = MemoryStack.stackPush();
        int var3;
        try {
            IntBuffer h = stack.mallocInt(1);
            GLFW.glfwGetWindowPos(this.mHandle, null, h);
            var3 = h.get(0);
        } catch (Throwable var5) {
            if (stack != null) {
                try {
                    stack.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }
            }
            throw var5;
        }
        if (stack != null) {
            stack.close();
        }
        return var3;
    }

    public int getScreenWidth() {
        MemoryStack stack = MemoryStack.stackPush();
        int var3;
        try {
            IntBuffer w = stack.mallocInt(1);
            GLFW.glfwGetWindowSize(this.mHandle, w, null);
            var3 = w.get(0);
        } catch (Throwable var5) {
            if (stack != null) {
                try {
                    stack.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }
            }
            throw var5;
        }
        if (stack != null) {
            stack.close();
        }
        return var3;
    }

    public int getScreenHeight() {
        MemoryStack stack = MemoryStack.stackPush();
        int var3;
        try {
            IntBuffer h = stack.mallocInt(1);
            GLFW.glfwGetWindowSize(this.mHandle, null, h);
            var3 = h.get(0);
        } catch (Throwable var5) {
            if (stack != null) {
                try {
                    stack.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }
            }
            throw var5;
        }
        if (stack != null) {
            stack.close();
        }
        return var3;
    }

    public void minimize() {
        GLFW.glfwIconifyWindow(this.mHandle);
    }

    public void restore() {
        GLFW.glfwRestoreWindow(this.mHandle);
    }

    public void maximize() {
        GLFW.glfwMaximizeWindow(this.mHandle);
    }

    public void show() {
        GLFW.glfwShowWindow(this.mHandle);
    }

    public void hide() {
        GLFW.glfwHideWindow(this.mHandle);
    }

    public void close() {
        Window w = sWindows.remove(this.mHandle);
        if (w != null) {
            assert w == this;
            Callbacks.glfwFreeCallbacks(this.mHandle);
            GLFW.glfwDestroyWindow(this.mHandle);
        }
    }

    public static enum State {

        WINDOWED, FULLSCREEN, FULLSCREEN_BORDERLESS, MAXIMIZED, MINIMIZED
    }
}