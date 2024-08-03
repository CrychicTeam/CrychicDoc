package icyllis.arc3d.engine;

import java.util.Objects;

public abstract class GpuBuffer extends GpuResource {

    public static final int kRead_LockMode = 0;

    public static final int kWriteDiscard_LockMode = 1;

    protected final int mSize;

    protected final int mUsage;

    private int mLockOffset;

    private int mLockSize;

    protected GpuBuffer(GpuDevice device, int size, int usage) {
        super(device);
        this.mSize = size;
        this.mUsage = usage;
    }

    public final int getSize() {
        return this.mSize;
    }

    public final int getUsage() {
        return this.mUsage;
    }

    @Override
    public final long getMemorySize() {
        return (long) this.mSize;
    }

    private static int getLockMode(int usage) {
        return (usage & 8) != 0 ? 0 : 1;
    }

    public final long lock() {
        if (this.isDestroyed()) {
            return 0L;
        } else if (this.isLocked()) {
            throw new IllegalStateException("Already locked");
        } else {
            this.mLockOffset = 0;
            this.mLockSize = this.mSize;
            return this.onLock(getLockMode(this.mUsage), 0, this.mSize);
        }
    }

    public final long lock(int offset, int size) {
        if (this.isDestroyed()) {
            return 0L;
        } else if (this.isLocked()) {
            throw new IllegalStateException("Already locked");
        } else {
            Objects.checkFromIndexSize(offset, size, this.mSize);
            this.mLockOffset = offset;
            this.mLockSize = size;
            return this.onLock(getLockMode(this.mUsage), offset, size);
        }
    }

    public final void unlock() {
        if (!this.isDestroyed()) {
            if (this.isLocked()) {
                this.onUnlock(getLockMode(this.mUsage), this.mLockOffset, this.mLockSize);
            }
            assert !this.isLocked();
        }
    }

    public final void unlock(int offset, int size) {
        if (!this.isDestroyed()) {
            if (this.isLocked()) {
                if (offset < this.mLockOffset || size > this.mLockSize) {
                    throw new IllegalStateException();
                }
                this.onUnlock(getLockMode(this.mUsage), offset, size);
            }
            assert !this.isLocked();
        }
    }

    protected abstract long onLock(int var1, int var2, int var3);

    protected abstract void onUnlock(int var1, int var2, int var3);

    public abstract boolean isLocked();

    public abstract long getLockedBuffer();

    public boolean updateData(int offset, int size, long data) {
        assert data != 0L;
        if (!this.isDestroyed() && !this.isLocked()) {
            assert size > 0 && offset + size <= this.mSize;
            return (this.mUsage & 8) != 0 ? false : this.onUpdateData(offset, size, data);
        } else {
            return false;
        }
    }

    protected abstract boolean onUpdateData(int var1, int var2, long var3);
}