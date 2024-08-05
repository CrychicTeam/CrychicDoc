package icyllis.arc3d.engine;

import icyllis.arc3d.core.RefCounted;
import icyllis.arc3d.core.SharedPtr;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.invoke.MethodHandles.Lookup;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import org.jetbrains.annotations.ApiStatus.Internal;

@NotThreadSafe
public abstract class GpuResource implements RefCounted {

    private static final VarHandle REF_CNT;

    private static final VarHandle COMMAND_BUFFER_USAGE_CNT;

    private volatile int mRefCnt = 1;

    private volatile int mCommandBufferUsageCnt = 0;

    static final PriorityQueue.Access<GpuResource> QUEUE_ACCESS;

    int mCacheIndex = -1;

    int mTimestamp;

    private long mLastUsedTime;

    IScratchKey mScratchKey;

    IUniqueKey mUniqueKey;

    GpuDevice mDevice;

    private byte mBudgetType = 1;

    private boolean mWrapped = false;

    private final GpuResource.UniqueID mUniqueID = new GpuResource.UniqueID();

    protected GpuResource(GpuDevice device) {
        assert device != null;
        this.mDevice = device;
    }

    @SharedPtr
    public static <T extends GpuResource> T move(@SharedPtr T sp) {
        if (sp != null) {
            sp.unref();
        }
        return null;
    }

    @SharedPtr
    public static <T extends GpuResource> T move(@SharedPtr T sp, @SharedPtr T that) {
        if (sp != null) {
            sp.unref();
        }
        return that;
    }

    @SharedPtr
    public static <T extends GpuResource> T create(@SharedPtr T that) {
        if (that != null) {
            that.ref();
        }
        return that;
    }

    @SharedPtr
    public static <T extends GpuResource> T create(@SharedPtr T sp, @SharedPtr T that) {
        if (sp != null) {
            sp.unref();
        }
        if (that != null) {
            that.ref();
        }
        return that;
    }

    public final boolean unique() {
        return (int) REF_CNT.getAcquire(this) == 1;
    }

    @Override
    public final void ref() {
        assert this.hasRef();
        REF_CNT.getAndAddRelease(this, 1);
    }

    @Override
    public final void unref() {
        assert this.hasRef();
        if ((int) REF_CNT.getAndAdd(this, -1) == 1) {
            this.notifyACntReachedZero(false);
        }
    }

    public final void addCommandBufferUsage() {
        COMMAND_BUFFER_USAGE_CNT.getAndAddRelease(this, 1);
    }

    public final void removeCommandBufferUsage() {
        assert this.hasCommandBufferUsage();
        if ((int) COMMAND_BUFFER_USAGE_CNT.getAndAdd(this, -1) == 1) {
            this.notifyACntReachedZero(true);
        }
    }

    protected final boolean hasRef() {
        return (int) REF_CNT.getOpaque(this) > 0;
    }

    protected final boolean hasCommandBufferUsage() {
        return (int) COMMAND_BUFFER_USAGE_CNT.getAcquire(this) > 0;
    }

    final void addInitialRef() {
        REF_CNT.getAndAddRelease(this, 1);
    }

    private void notifyACntReachedZero(boolean commandBufferUsage) {
        if (this.mDevice != null) {
            this.mDevice.getContext().getResourceCache().notifyACntReachedZero(this, commandBufferUsage);
        }
    }

    public final boolean isDestroyed() {
        return this.mDevice == null;
    }

    @Nullable
    public final DirectContext getContext() {
        return this.mDevice != null ? this.mDevice.getContext() : null;
    }

    public abstract long getMemorySize();

    @Nullable
    public final IUniqueKey getUniqueKey() {
        return this.mUniqueKey;
    }

    @Nonnull
    public GpuResource.UniqueID getUniqueID() {
        return this.mUniqueID;
    }

    @Nonnull
    public final String getLabel() {
        return this.mUniqueID.mLabel;
    }

    public final void setLabel(String label) {
        label = label != null ? label.trim() : "";
        if (!this.mUniqueID.mLabel.equals(label)) {
            this.mUniqueID.mLabel = label;
            this.onSetLabel(label);
        }
    }

    @Internal
    public final void setUniqueKey(IUniqueKey key) {
        assert this.hasRef();
        if (this.mBudgetType == 0 || this.mWrapped) {
            if (this.mDevice != null) {
                this.mDevice.getContext().getResourceCache().changeUniqueKey(this, key);
            }
        }
    }

    @Internal
    public final void removeUniqueKey() {
        if (this.mDevice != null) {
            this.mDevice.getContext().getResourceCache().removeUniqueKey(this);
        }
    }

    @Internal
    public final void makeBudgeted(boolean budgeted) {
        if (budgeted) {
            assert !this.mWrapped;
            assert this.mBudgetType != 2;
            if (this.mDevice != null && this.mBudgetType == 1) {
                this.mBudgetType = 0;
                this.mDevice.getContext().getResourceCache().didChangeBudgetStatus(this);
            }
        } else if (this.mDevice != null && this.mBudgetType == 0 && this.mUniqueKey == null) {
            this.mBudgetType = 1;
            this.mDevice.getContext().getResourceCache().didChangeBudgetStatus(this);
        }
    }

    @Internal
    public final int getBudgetType() {
        assert this.mBudgetType == 0 || this.mWrapped || this.mUniqueKey == null;
        return this.mBudgetType;
    }

    @Internal
    public final boolean isWrapped() {
        return this.mWrapped;
    }

    @Nullable
    @Internal
    public final IScratchKey getScratchKey() {
        return this.mScratchKey;
    }

    @Internal
    public final void removeScratchKey() {
        if (this.mDevice != null && this.mScratchKey != null) {
            this.mDevice.getContext().getResourceCache().willRemoveScratchKey(this);
            this.mScratchKey = null;
        }
    }

    @Internal
    public final boolean isFree() {
        return !this.hasRef() && !this.hasCommandBufferUsage() && (this.mBudgetType != 2 || this.mUniqueKey == null);
    }

    @Internal
    public final boolean hasRefOrCommandBufferUsage() {
        return this.hasRef() || this.hasCommandBufferUsage();
    }

    protected final void registerWithCache(boolean budgeted) {
        assert this.mBudgetType == 1;
        this.mBudgetType = (byte) (budgeted ? 0 : 1);
        this.mScratchKey = this.computeScratchKey();
        this.mDevice.getContext().getResourceCache().insertResource(this);
    }

    protected final void registerWithCacheWrapped(boolean cacheable) {
        assert this.mBudgetType == 1;
        this.mBudgetType = (byte) (cacheable ? 2 : 1);
        this.mWrapped = true;
        this.mDevice.getContext().getResourceCache().insertResource(this);
    }

    protected GpuDevice getDevice() {
        return this.mDevice;
    }

    protected abstract void onRelease();

    protected abstract void onDiscard();

    protected void onSetLabel(@Nonnull String label) {
    }

    @Nullable
    protected IScratchKey computeScratchKey() {
        return null;
    }

    final boolean isScratch() {
        return this.mBudgetType == 0 && this.mScratchKey != null && this.mUniqueKey == null;
    }

    final boolean isUsableAsScratch() {
        return this.isScratch() && !this.hasRef();
    }

    final void release() {
        assert this.mDevice != null;
        this.onRelease();
        this.mDevice.getContext().getResourceCache().removeResource(this);
        this.mDevice = null;
    }

    final void discard() {
        if (this.mDevice != null) {
            this.onDiscard();
            this.mDevice.getContext().getResourceCache().removeResource(this);
            this.mDevice = null;
        }
    }

    final void setLastUsedTime() {
        assert this.isFree();
        this.mLastUsedTime = System.currentTimeMillis();
    }

    final long getLastUsedTime() {
        assert this.isFree();
        return this.mLastUsedTime;
    }

    static {
        Lookup lookup = MethodHandles.lookup();
        try {
            REF_CNT = lookup.findVarHandle(GpuResource.class, "mRefCnt", int.class);
            COMMAND_BUFFER_USAGE_CNT = lookup.findVarHandle(GpuResource.class, "mCommandBufferUsageCnt", int.class);
        } catch (IllegalAccessException | NoSuchFieldException var2) {
            throw new RuntimeException(var2);
        }
        QUEUE_ACCESS = new PriorityQueue.Access<GpuResource>() {

            public void setIndex(GpuResource resource, int index) {
                resource.mCacheIndex = index;
            }

            public int getIndex(GpuResource resource) {
                return resource.mCacheIndex;
            }
        };
    }

    public static final class UniqueID {

        @Nonnull
        private String mLabel = "";

        @Nonnull
        public String toString() {
            return "GpuResource.UniqueID@" + Integer.toHexString(this.hashCode()) + "{" + this.mLabel + "}";
        }
    }
}