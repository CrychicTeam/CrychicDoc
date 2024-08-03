package me.jellysquid.mods.sodium.client.gl.sync;

import java.nio.IntBuffer;
import org.lwjgl.opengl.GL32C;
import org.lwjgl.system.MemoryStack;

public class GlFence {

    private final long id;

    private boolean disposed;

    public GlFence(long id) {
        this.id = id;
    }

    public boolean isCompleted() {
        this.checkDisposed();
        MemoryStack stack = MemoryStack.stackPush();
        int result;
        try {
            IntBuffer count = stack.callocInt(1);
            result = GL32C.glGetSynci(this.id, 37140, count);
            if (count.get(0) != 1) {
                throw new RuntimeException("glGetSync returned more than one value");
            }
        } catch (Throwable var6) {
            if (stack != null) {
                try {
                    stack.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }
            }
            throw var6;
        }
        if (stack != null) {
            stack.close();
        }
        return result == 37145;
    }

    public void sync() {
        this.checkDisposed();
        this.sync(Long.MAX_VALUE);
    }

    public void sync(long timeout) {
        this.checkDisposed();
        GL32C.glWaitSync(this.id, 1, timeout);
    }

    public void delete() {
        GL32C.glDeleteSync(this.id);
        this.disposed = true;
    }

    private void checkDisposed() {
        if (this.disposed) {
            throw new IllegalStateException("Fence object has been disposed");
        }
    }
}