package icyllis.arc3d.opengl;

import icyllis.arc3d.core.RefCnt;
import icyllis.arc3d.core.SharedPtr;
import icyllis.arc3d.engine.CpuBuffer;
import icyllis.arc3d.engine.GpuBuffer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class GLBuffer extends GpuBuffer {

    private int mBuffer;

    private boolean mLocked;

    private long mMappedBuffer;

    @SharedPtr
    private CpuBuffer mStagingBuffer;

    private final boolean mPersistentlyMapped;

    private static final int MAX_BYTES_PER_UPDATE = 262144;

    private GLBuffer(GLDevice device, int size, int usage, int buffer, long mappedBuffer) {
        super(device, size, usage);
        this.mBuffer = buffer;
        this.mMappedBuffer = mappedBuffer;
        this.mPersistentlyMapped = this.mMappedBuffer != 0L;
        this.registerWithCache(true);
    }

    @Nullable
    @SharedPtr
    public static GLBuffer make(GLDevice device, int size, int usage) {
        assert size > 0;
        int typeFlags = usage & 63;
        assert typeFlags != 0;
        boolean preferBufferStorage = (usage & 256) != 0;
        if (!preferBufferStorage && Integer.bitCount(typeFlags) != 1) {
            new Throwable("RHICreateBuffer, only one type bit is allowed, given 0x" + Integer.toHexString(typeFlags)).printStackTrace(device.getContext().getErrorWriter());
            return null;
        } else if (device.getCaps().hasDSASupport()) {
            int buffer = GLCore.glCreateBuffers();
            if (buffer == 0) {
                return null;
            } else {
                long persistentlyMappedBuffer = 0L;
                if (preferBufferStorage) {
                    int allocFlags = getBufferStorageFlags(usage);
                    if (device.getCaps().skipErrorChecks()) {
                        GLCore.glNamedBufferStorage(buffer, (long) size, allocFlags);
                    } else {
                        GLCore.glClearErrors();
                        GLCore.glNamedBufferStorage(buffer, (long) size, allocFlags);
                        if (GLCore.glGetError() != 0) {
                            GLCore.glDeleteBuffers(buffer);
                            new Throwable("RHICreateBuffer, failed to allocate " + size + " bytes from device").printStackTrace(device.getContext().getErrorWriter());
                            return null;
                        }
                    }
                    if ((usage & 256) != 0) {
                        persistentlyMappedBuffer = GLCore.nglMapNamedBufferRange(buffer, 0L, (long) size, 194);
                        if (persistentlyMappedBuffer == 0L) {
                            GLCore.glDeleteBuffers(buffer);
                            new Throwable("RHICreateBuffer, failed to map buffer range persistently").printStackTrace(device.getContext().getErrorWriter());
                            return null;
                        }
                    }
                } else {
                    int allocUsage = getBufferUsageM(usage);
                    if (device.getCaps().skipErrorChecks()) {
                        GLCore.glNamedBufferData(buffer, (long) size, allocUsage);
                    } else {
                        GLCore.glClearErrors();
                        GLCore.glNamedBufferData(buffer, (long) size, allocUsage);
                        if (GLCore.glGetError() != 0) {
                            GLCore.glDeleteBuffers(buffer);
                            new Throwable("RHICreateBuffer, failed to allocate " + size + " bytes from device").printStackTrace(device.getContext().getErrorWriter());
                            return null;
                        }
                    }
                }
                return new GLBuffer(device, size, usage, buffer, persistentlyMappedBuffer);
            }
        } else {
            int buffer = GLCore.glGenBuffers();
            if (buffer == 0) {
                return null;
            } else {
                int target = device.bindBufferForSetup(usage, buffer);
                long persistentlyMappedBuffer = 0L;
                if (preferBufferStorage && device.getCaps().hasBufferStorageSupport()) {
                    int allocFlagsx = getBufferStorageFlags(usage);
                    if (device.getCaps().skipErrorChecks()) {
                        GLCore.glBufferStorage(target, (long) size, allocFlagsx);
                    } else {
                        GLCore.glClearErrors();
                        GLCore.glBufferStorage(target, (long) size, allocFlagsx);
                        if (GLCore.glGetError() != 0) {
                            GLCore.glDeleteBuffers(buffer);
                            new Throwable("RHICreateBuffer, failed to allocate " + size + " bytes from device").printStackTrace(device.getContext().getErrorWriter());
                            return null;
                        }
                    }
                    if ((usage & 256) != 0) {
                        persistentlyMappedBuffer = GLCore.nglMapBufferRange(target, 0L, (long) size, 194);
                        if (persistentlyMappedBuffer == 0L) {
                            GLCore.glDeleteBuffers(buffer);
                            new Throwable("RHICreateBuffer, failed to map buffer range persistently").printStackTrace(device.getContext().getErrorWriter());
                            return null;
                        }
                    }
                } else {
                    int allocUsage = getBufferUsageM(usage);
                    if (device.getCaps().skipErrorChecks()) {
                        GLCore.glBufferData(target, (long) size, allocUsage);
                    } else {
                        GLCore.glClearErrors();
                        GLCore.glBufferData(target, (long) size, allocUsage);
                        if (GLCore.glGetError() != 0) {
                            GLCore.glDeleteBuffers(buffer);
                            new Throwable("RHICreateBuffer, failed to allocate " + size + " bytes from device").printStackTrace(device.getContext().getErrorWriter());
                            return null;
                        }
                    }
                }
                return new GLBuffer(device, size, usage, buffer, persistentlyMappedBuffer);
            }
        }
    }

    public static int getBufferUsageM(int usage) {
        int allocUsage;
        if ((usage & 8) != 0) {
            allocUsage = 35049;
        } else if ((usage & 256) != 0) {
            allocUsage = 35040;
        } else if ((usage & 128) != 0) {
            allocUsage = 35048;
        } else if ((usage & 64) != 0) {
            allocUsage = 35044;
        } else {
            allocUsage = 35048;
        }
        return allocUsage;
    }

    public static int getBufferStorageFlags(int usage) {
        int allocFlags = 0;
        if ((usage & 4) != 0) {
            allocFlags |= 2;
        }
        if ((usage & 8) != 0) {
            allocFlags |= 1;
        }
        if ((usage & 256) != 0) {
            allocFlags |= 194;
        } else if ((usage & 3) != 0) {
            allocFlags |= 256;
        }
        return allocFlags;
    }

    public int getHandle() {
        return this.mBuffer;
    }

    @Override
    protected void onSetLabel(@Nonnull String label) {
        if (this.getDevice().getCaps().hasDebugSupport()) {
            if (label.isEmpty()) {
                GLCore.nglObjectLabel(33504, this.mBuffer, 0, 0L);
            } else {
                label = label.substring(0, Math.min(label.length(), this.getDevice().getCaps().maxLabelLength()));
                GLCore.glObjectLabel(33504, this.mBuffer, label);
            }
        }
    }

    @Override
    protected void onRelease() {
        if (this.mBuffer != 0) {
            GLCore.glDeleteBuffers(this.mBuffer);
        }
        this.onDiscard();
    }

    @Override
    protected void onDiscard() {
        this.mBuffer = 0;
        this.mLocked = false;
        this.mMappedBuffer = 0L;
        this.mStagingBuffer = RefCnt.move(this.mStagingBuffer);
    }

    protected GLDevice getDevice() {
        return (GLDevice) super.getDevice();
    }

    @Override
    protected long onLock(int mode, int offset, int size) {
        assert this.getDevice().getContext().isOwnerThread();
        assert !this.mLocked;
        assert this.mBuffer != 0;
        this.mLocked = true;
        if (this.mPersistentlyMapped) {
            assert this.mMappedBuffer != 0L;
            return this.mMappedBuffer;
        } else if (mode == 0) {
            this.mMappedBuffer = GLCore.nglMapNamedBufferRange(this.mBuffer, (long) offset, (long) size, 1);
            if (this.mMappedBuffer == 0L) {
                new Throwable("Failed to map buffer " + this).printStackTrace(this.getDevice().getContext().getErrorWriter());
            }
            return this.mMappedBuffer;
        } else {
            assert mode == 1;
            this.mStagingBuffer = this.getDevice().getCpuBufferPool().makeBuffer(size);
            assert this.mStagingBuffer != null;
            return this.mStagingBuffer.data();
        }
    }

    @Override
    protected void onUnlock(int mode, int offset, int size) {
        assert this.getDevice().getContext().isOwnerThread();
        assert this.mLocked;
        assert this.mBuffer != 0;
        if (this.mPersistentlyMapped) {
            assert this.mMappedBuffer != 0L;
        } else if (mode == 0) {
            assert this.mMappedBuffer != 0L;
            GLCore.glUnmapNamedBuffer(this.mBuffer);
            this.mMappedBuffer = 0L;
        } else {
            assert mode == 1;
            int target = 0;
            if (!this.getDevice().getCaps().hasDSASupport()) {
                target = this.getDevice().bindBuffer(this);
            }
            if ((this.mUsage & 64) == 0) {
                this.doInvalidateBuffer(target, offset, size);
            }
            assert this.mStagingBuffer != null;
            this.doUploadData(target, this.mStagingBuffer.data(), offset, size);
            this.mStagingBuffer = RefCnt.move(this.mStagingBuffer);
        }
        this.mLocked = false;
    }

    @Override
    public boolean isLocked() {
        return this.mLocked;
    }

    @Override
    public long getLockedBuffer() {
        if (this.mLocked) {
            return this.mMappedBuffer != 0L ? this.mMappedBuffer : this.mStagingBuffer.data();
        } else {
            return 0L;
        }
    }

    @Override
    protected boolean onUpdateData(int offset, int size, long data) {
        assert this.getDevice().getContext().isOwnerThread();
        assert this.mBuffer != 0;
        int target = 0;
        if (!this.getDevice().getCaps().hasDSASupport()) {
            target = this.getDevice().bindBuffer(this);
        }
        if ((this.mUsage & 64) == 0 && !this.doInvalidateBuffer(target, offset, size)) {
            return false;
        } else {
            this.doUploadData(target, data, offset, size);
            return true;
        }
    }

    private void doUploadData(int target, long data, int offset, int totalSize) {
        if (target == 0) {
            while (totalSize > 0) {
                int size = Math.min(262144, totalSize);
                GLCore.nglNamedBufferSubData(this.mBuffer, (long) offset, (long) size, data);
                data += (long) size;
                offset += size;
                totalSize -= size;
            }
        } else {
            while (totalSize > 0) {
                int size = Math.min(262144, totalSize);
                GLCore.nglBufferSubData(target, (long) offset, (long) size, data);
                data += (long) size;
                offset += size;
                totalSize -= size;
            }
        }
    }

    private boolean doInvalidateBuffer(int target, int offset, int size) {
        GLDevice device = this.getDevice();
        switch(device.getCaps().getInvalidateBufferType()) {
            case 1:
                assert target != 0;
                int allocUsage = getBufferUsageM(this.getUsage());
                if (device.getCaps().skipErrorChecks()) {
                    GLCore.glBufferData(target, (long) this.mSize, allocUsage);
                } else {
                    GLCore.glClearErrors();
                    GLCore.glBufferData(target, (long) this.mSize, allocUsage);
                    if (GLCore.glGetError() != 0) {
                        return false;
                    }
                }
                break;
            case 2:
                GLCore.glInvalidateBufferSubData(this.mBuffer, (long) offset, (long) size);
        }
        return true;
    }
}