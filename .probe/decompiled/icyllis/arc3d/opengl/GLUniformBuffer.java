package icyllis.arc3d.opengl;

import icyllis.arc3d.core.SharedPtr;
import icyllis.arc3d.engine.ManagedResource;
import javax.annotation.Nullable;

public class GLUniformBuffer extends ManagedResource {

    private final int mBinding;

    private final int mSize;

    private int mBuffer;

    private GLUniformBuffer(GLDevice device, int binding, int size, int buffer) {
        super(device);
        this.mSize = size;
        this.mBuffer = buffer;
        this.mBinding = binding;
        if (!device.getCaps().hasDSASupport()) {
            device.currentCommandBuffer().bindUniformBuffer(this);
            if (device.getCaps().skipErrorChecks()) {
                GLCore.glBufferData(35345, (long) size, 35048);
            } else {
                GLCore.glClearErrors();
                GLCore.glBufferData(35345, (long) size, 35048);
                if (GLCore.glGetError() != 0) {
                    GLCore.glDeleteBuffers(this.mBuffer);
                    this.mBuffer = 0;
                }
            }
        }
    }

    @Nullable
    @SharedPtr
    public static GLUniformBuffer make(GLDevice device, int size, int binding) {
        assert size > 0;
        if (device.getCaps().hasDSASupport()) {
            int buffer = GLCore.glCreateBuffers();
            if (buffer == 0) {
                return null;
            } else {
                if (device.getCaps().skipErrorChecks()) {
                    GLCore.glNamedBufferStorage(buffer, (long) size, 256);
                } else {
                    GLCore.glClearErrors();
                    GLCore.glNamedBufferStorage(buffer, (long) size, 256);
                    if (GLCore.glGetError() != 0) {
                        GLCore.glDeleteBuffers(buffer);
                        return null;
                    }
                }
                return new GLUniformBuffer(device, binding, size, buffer);
            }
        } else {
            int buffer = GLCore.glGenBuffers();
            if (buffer == 0) {
                return null;
            } else {
                GLUniformBuffer res = new GLUniformBuffer(device, binding, size, buffer);
                if (res.mBuffer == 0) {
                    res.unref();
                    return null;
                } else {
                    return res;
                }
            }
        }
    }

    @Override
    protected void deallocate() {
        if (this.mBuffer != 0) {
            GLCore.glDeleteBuffers(this.mBuffer);
        }
        this.mBuffer = 0;
    }

    public void discard() {
        this.mBuffer = 0;
    }

    public int getSize() {
        return this.mSize;
    }

    public int getHandle() {
        return this.mBuffer;
    }

    public int getBinding() {
        return this.mBinding;
    }
}