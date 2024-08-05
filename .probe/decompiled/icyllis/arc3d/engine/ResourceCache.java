package icyllis.arc3d.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public final class ResourceCache implements AutoCloseable {

    private static final Comparator<GpuResource> TIMESTAMP_COMPARATOR = (lhs, rhs) -> Integer.compareUnsigned(lhs.mTimestamp, rhs.mTimestamp);

    private SurfaceProvider mSurfaceProvider = null;

    private ThreadSafeCache mThreadSafeCache = null;

    private int mTimestamp = 0;

    private final PriorityQueue<GpuResource> mFreeQueue;

    private GpuResource[] mNonFreeList;

    private int mNonFreeSize;

    private final LinkedListMultimap<IScratchKey, GpuResource> mScratchMap;

    private final HashMap<IUniqueKey, GpuResource> mUniqueMap;

    private long mMaxBytes = 268435456L;

    private int mCount = 0;

    private long mBytes = 0L;

    private int mBudgetedCount = 0;

    private long mBudgetedBytes = 0L;

    private long mFreeBytes = 0L;

    private int mDirtyCount = 0;

    private final int mContextID;

    ResourceCache(int contextID) {
        this.mContextID = contextID;
        this.mFreeQueue = new PriorityQueue<>(TIMESTAMP_COMPARATOR, GpuResource.QUEUE_ACCESS);
        this.mNonFreeList = new GpuResource[10];
        this.mScratchMap = new LinkedListMultimap<>();
        this.mUniqueMap = new HashMap();
    }

    public int getContextID() {
        return this.mContextID;
    }

    public void setCacheLimit(long maxBytes) {
        this.mMaxBytes = maxBytes;
        this.cleanup();
    }

    public int getResourceCount() {
        return this.mFreeQueue.size() + this.mNonFreeSize;
    }

    public int getBudgetedResourceCount() {
        return this.mBudgetedCount;
    }

    public long getResourceBytes() {
        return this.mBytes;
    }

    public long getBudgetedResourceBytes() {
        return this.mBudgetedBytes;
    }

    public long getFreeResourceBytes() {
        return this.mFreeBytes;
    }

    public long getMaxResourceBytes() {
        return this.mMaxBytes;
    }

    public void releaseAll() {
        while (this.mNonFreeSize > 0) {
            GpuResource back = this.mNonFreeList[this.mNonFreeSize - 1];
            assert !back.isDestroyed();
            back.release();
        }
        while (!this.mFreeQueue.isEmpty()) {
            GpuResource top = this.mFreeQueue.peek();
            assert !top.isDestroyed();
            top.release();
        }
        assert this.mScratchMap.isEmpty();
        assert this.mUniqueMap.isEmpty();
        assert this.mCount == 0 : this.mCount;
        assert this.getResourceCount() == 0;
        assert this.mBytes == 0L;
        assert this.mBudgetedCount == 0;
        assert this.mBudgetedBytes == 0L;
        assert this.mFreeBytes == 0L;
    }

    public void discardAll() {
        while (this.mNonFreeSize > 0) {
            GpuResource back = this.mNonFreeList[this.mNonFreeSize - 1];
            assert !back.isDestroyed();
            back.discard();
        }
        while (!this.mFreeQueue.isEmpty()) {
            GpuResource top = this.mFreeQueue.peek();
            assert !top.isDestroyed();
            top.discard();
        }
        assert this.mScratchMap.isEmpty();
        assert this.mUniqueMap.isEmpty();
        assert this.mCount == 0;
        assert this.getResourceCount() == 0;
        assert this.mBytes == 0L;
        assert this.mBudgetedCount == 0;
        assert this.mBudgetedBytes == 0L;
        assert this.mFreeBytes == 0L;
    }

    @Nullable
    public GpuResource findAndRefScratchResource(IScratchKey key) {
        assert key != null;
        GpuResource resource = this.mScratchMap.pollFirstEntry(key);
        if (resource != null) {
            this.refAndMakeResourceMRU(resource);
            return resource;
        } else {
            return null;
        }
    }

    @Nullable
    public GpuResource findAndRefUniqueResource(IUniqueKey key) {
        assert key != null;
        GpuResource resource = (GpuResource) this.mUniqueMap.get(key);
        if (resource != null) {
            this.refAndMakeResourceMRU(resource);
        }
        return resource;
    }

    public boolean hasUniqueKey(IUniqueKey key) {
        return this.mUniqueMap.containsKey(key);
    }

    public void setSurfaceProvider(SurfaceProvider surfaceProvider) {
        this.mSurfaceProvider = surfaceProvider;
    }

    public void setThreadSafeCache(ThreadSafeCache threadSafeCache) {
        this.mThreadSafeCache = threadSafeCache;
    }

    public boolean cleanup() {
        boolean stillOverBudget;
        for (stillOverBudget = this.isOverBudget(); stillOverBudget && !this.mFreeQueue.isEmpty(); stillOverBudget = this.isOverBudget()) {
            GpuResource resource = this.mFreeQueue.peek();
            assert resource.isFree();
            resource.release();
        }
        if (stillOverBudget) {
            this.mThreadSafeCache.dropUniqueRefs(this);
            for (stillOverBudget = this.isOverBudget(); stillOverBudget && !this.mFreeQueue.isEmpty(); stillOverBudget = this.isOverBudget()) {
                GpuResource resource = this.mFreeQueue.peek();
                assert resource.isFree();
                resource.release();
            }
        }
        return stillOverBudget;
    }

    public void purgeFreeResources(boolean scratchOnly) {
        this.purgeFreeResourcesOlderThan(0L, scratchOnly);
    }

    public void purgeFreeResourcesOlderThan(long timeMillis, boolean scratchOnly) {
        if (scratchOnly) {
            if (timeMillis >= 0L && !this.mFreeQueue.isEmpty() && this.mFreeQueue.peek().getLastUsedTime() >= timeMillis) {
                return;
            }
            this.mFreeQueue.sort();
            List<GpuResource> scratchResources = new ArrayList();
            for (int i = 0; i < this.mFreeQueue.size(); i++) {
                GpuResource resource = this.mFreeQueue.elementAt(i);
                if (timeMillis >= 0L && resource.getLastUsedTime() >= timeMillis) {
                    break;
                }
                assert resource.isFree();
                if (resource.mUniqueKey == null) {
                    scratchResources.add(resource);
                }
            }
            scratchResources.forEach(GpuResource::release);
        } else {
            if (timeMillis >= 0L) {
                this.mThreadSafeCache.dropUniqueRefsOlderThan(timeMillis);
            } else {
                this.mThreadSafeCache.dropUniqueRefs(null);
            }
            while (!this.mFreeQueue.isEmpty()) {
                GpuResource resourcex = this.mFreeQueue.peek();
                if (timeMillis >= 0L && resourcex.getLastUsedTime() >= timeMillis) {
                    break;
                }
                assert resourcex.isFree();
                resourcex.release();
            }
        }
        this.mFreeQueue.trim();
    }

    public void purgeFreeResourcesUpToBytes(long bytesToPurge, boolean preferScratch) {
    }

    public boolean purgeFreeResourcesToReserveBytes(long bytesToReserve) {
        return false;
    }

    public boolean isOverBudget() {
        return this.mBudgetedBytes > this.mMaxBytes;
    }

    public boolean isFlushNeeded() {
        return this.isOverBudget() && this.mFreeQueue.isEmpty() && this.mDirtyCount > 0;
    }

    void notifyACntReachedZero(GpuResource resource, boolean commandBufferUsage) {
        assert !resource.isDestroyed();
        assert this.isInCache(resource);
        assert this.mNonFreeList[resource.mCacheIndex] == resource;
        if (!commandBufferUsage && resource.isUsableAsScratch()) {
            this.mScratchMap.addFirstEntry(resource.mScratchKey, resource);
        }
        if (!resource.hasRefOrCommandBufferUsage()) {
            resource.mTimestamp = this.getNextTimestamp();
            if (!resource.isFree() && resource.getBudgetType() == 0) {
                this.mDirtyCount++;
            }
            if (resource.isFree()) {
                this.removeFromNonFreeArray(resource);
                this.mFreeQueue.add(resource);
                resource.setLastUsedTime();
                this.mFreeBytes = this.mFreeBytes + resource.getMemorySize();
                boolean hasUniqueKey = resource.mUniqueKey != null;
                int budgetedType = resource.getBudgetType();
                if (budgetedType == 0) {
                    boolean hasKey = hasUniqueKey || resource.mScratchKey != null;
                    if (!this.isOverBudget() && hasKey) {
                        return;
                    }
                } else {
                    if (hasUniqueKey && budgetedType == 2) {
                        return;
                    }
                    if (!resource.isWrapped() && resource.mScratchKey != null && this.mBudgetedBytes + resource.getMemorySize() <= this.mMaxBytes) {
                        resource.makeBudgeted(true);
                        return;
                    }
                }
                int beforeCount = this.getResourceCount();
                resource.release();
                assert this.getResourceCount() < beforeCount;
            }
        }
    }

    void insertResource(GpuResource resource) {
        assert !this.isInCache(resource);
        assert !resource.isDestroyed();
        assert !resource.isFree();
        resource.mTimestamp = this.getNextTimestamp();
        this.addToNonFreeArray(resource);
        long size = resource.getMemorySize();
        this.mCount++;
        this.mBytes += size;
        if (resource.getBudgetType() == 0) {
            this.mBudgetedCount++;
            this.mBudgetedBytes += size;
        }
        assert !resource.isUsableAsScratch();
        this.cleanup();
    }

    void removeResource(GpuResource resource) {
        assert this.isInCache(resource);
        long size = resource.getMemorySize();
        if (resource.isFree()) {
            this.mFreeQueue.removeAt(resource.mCacheIndex);
            this.mFreeBytes -= size;
        } else {
            this.removeFromNonFreeArray(resource);
        }
        this.mCount--;
        this.mBytes -= size;
        if (resource.getBudgetType() == 0) {
            this.mBudgetedCount--;
            this.mBudgetedBytes -= size;
        }
        if (resource.isUsableAsScratch()) {
            this.mScratchMap.removeFirstEntry(resource.mScratchKey, resource);
        }
        if (resource.mUniqueKey != null) {
            this.mUniqueMap.remove(resource.mUniqueKey);
        }
    }

    void changeUniqueKey(GpuResource resource, IUniqueKey newKey) {
        assert this.isInCache(resource);
        if (newKey != null) {
            GpuResource old;
            if ((old = (GpuResource) this.mUniqueMap.get(newKey)) != null) {
                if (old.mScratchKey == null && old.isFree()) {
                    old.release();
                } else {
                    old.ref();
                    this.removeUniqueKey(old);
                    old.unref();
                }
            }
            assert !this.mUniqueMap.containsKey(newKey);
            if (resource.mUniqueKey != null) {
                assert this.mUniqueMap.get(resource.mUniqueKey) == resource;
                this.mUniqueMap.remove(resource.mUniqueKey);
                assert !this.mUniqueMap.containsKey(resource.mUniqueKey);
            } else if (resource.isUsableAsScratch()) {
                this.mScratchMap.removeFirstEntry(resource.mScratchKey, resource);
            }
            resource.mUniqueKey = newKey;
            this.mUniqueMap.put(resource.mUniqueKey, resource);
        } else {
            this.removeUniqueKey(resource);
        }
    }

    void removeUniqueKey(GpuResource resource) {
        if (resource.mUniqueKey != null) {
            assert this.mUniqueMap.get(resource.mUniqueKey) == resource;
            this.mUniqueMap.remove(resource.mUniqueKey);
        }
        if (resource.mUniqueKey != null) {
            resource.mUniqueKey = null;
        }
        if (resource.isUsableAsScratch()) {
            this.mScratchMap.addFirstEntry(resource.mScratchKey, resource);
        }
        assert !resource.isFree();
    }

    void didChangeBudgetStatus(GpuResource resource) {
        assert this.isInCache(resource);
        long size = resource.getMemorySize();
        boolean wasCleanable = resource.isFree();
        if (resource.getBudgetType() == 0) {
            this.mBudgetedCount++;
            this.mBudgetedBytes += size;
            if (!resource.isFree() && !resource.hasRefOrCommandBufferUsage()) {
                this.mDirtyCount++;
            }
            if (resource.isUsableAsScratch()) {
                this.mScratchMap.addFirstEntry(resource.mScratchKey, resource);
            }
            this.cleanup();
        } else {
            assert resource.getBudgetType() == 2;
            this.mBudgetedCount--;
            this.mBudgetedBytes -= size;
            if (!resource.isFree() && !resource.hasRefOrCommandBufferUsage()) {
                this.mDirtyCount--;
            }
            if (!resource.hasRef() && resource.mUniqueKey == null && resource.mScratchKey != null) {
                this.mScratchMap.removeFirstEntry(resource.mScratchKey, resource);
            }
        }
        assert wasCleanable == resource.isFree();
    }

    void willRemoveScratchKey(GpuResource resource) {
        assert resource.mScratchKey != null;
        if (resource.isUsableAsScratch()) {
            this.mScratchMap.removeFirstEntry(resource.mScratchKey, resource);
        }
    }

    private void refAndMakeResourceMRU(GpuResource resource) {
        assert this.isInCache(resource);
        if (resource.isFree()) {
            this.mFreeBytes = this.mFreeBytes - resource.getMemorySize();
            this.mFreeQueue.removeAt(resource.mCacheIndex);
            this.addToNonFreeArray(resource);
        } else if (!resource.hasRefOrCommandBufferUsage() && resource.getBudgetType() == 0) {
            assert this.mDirtyCount > 0;
            this.mDirtyCount--;
        }
        resource.addInitialRef();
        resource.mTimestamp = this.getNextTimestamp();
    }

    private void addToNonFreeArray(GpuResource resource) {
        GpuResource[] es = this.mNonFreeList;
        int s = this.mNonFreeSize;
        if (s == es.length) {
            this.mNonFreeList = es = (GpuResource[]) Arrays.copyOf(es, s + (s >> 1));
        }
        es[s] = resource;
        resource.mCacheIndex = s;
        this.mNonFreeSize = s + 1;
    }

    private void removeFromNonFreeArray(GpuResource resource) {
        GpuResource[] es = this.mNonFreeList;
        int pos = resource.mCacheIndex;
        assert es[pos] == resource;
        int s = --this.mNonFreeSize;
        GpuResource tail = es[s];
        es[s] = null;
        es[pos] = tail;
        tail.mCacheIndex = pos;
        resource.mCacheIndex = -1;
    }

    private int getNextTimestamp() {
        if (this.mTimestamp == 0) {
            int count = this.getResourceCount();
            if (count > 0) {
                int freeSize = this.mFreeQueue.size();
                GpuResource[] sortedFree = new GpuResource[freeSize];
                for (int i = 0; i < freeSize; i++) {
                    sortedFree[i] = (GpuResource) this.mFreeQueue.remove();
                }
                Arrays.sort(this.mNonFreeList, 0, this.mNonFreeSize, TIMESTAMP_COMPARATOR);
                int currF = 0;
                int currNF = 0;
                while (currF < freeSize && currNF < this.mNonFreeSize) {
                    int tsP = sortedFree[currF].mTimestamp;
                    int tsNP = this.mNonFreeList[currNF].mTimestamp;
                    assert tsP != tsNP;
                    if (tsP < tsNP) {
                        sortedFree[currF++].mTimestamp = this.mTimestamp++;
                    } else {
                        this.mNonFreeList[currNF].mCacheIndex = currNF;
                        this.mNonFreeList[currNF++].mTimestamp = this.mTimestamp++;
                    }
                }
                while (currF < freeSize) {
                    sortedFree[currF++].mTimestamp = this.mTimestamp++;
                }
                while (currNF < this.mNonFreeSize) {
                    this.mNonFreeList[currNF].mCacheIndex = currNF;
                    this.mNonFreeList[currNF++].mTimestamp = this.mTimestamp++;
                }
                Collections.addAll(this.mFreeQueue, sortedFree);
                assert this.mTimestamp == count;
                assert this.mTimestamp == this.getResourceCount();
            }
        }
        return this.mTimestamp++;
    }

    private boolean isInCache(GpuResource resource) {
        int index = resource.mCacheIndex;
        if (index < 0) {
            return false;
        } else if (index < this.mFreeQueue.size() && this.mFreeQueue.elementAt(index) == resource) {
            return true;
        } else if (index < this.mNonFreeSize && this.mNonFreeList[index] == resource) {
            return true;
        } else {
            throw new IllegalStateException("Resource index should be -1 or the resource should be in the cache.");
        }
    }

    public void close() {
        this.releaseAll();
    }
}