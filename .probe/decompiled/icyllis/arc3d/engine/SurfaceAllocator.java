package icyllis.arc3d.engine;

import icyllis.arc3d.core.SharedPtr;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import javax.annotation.Nonnull;

public final class SurfaceAllocator {

    private final DirectContext mContext;

    private final Reference2ObjectOpenHashMap<Object, SurfaceAllocator.Interval> mIntervalHash = new Reference2ObjectOpenHashMap();

    private final SurfaceAllocator.IntervalList mIntervalList = new SurfaceAllocator.IntervalList();

    private final SurfaceAllocator.IntervalList mActiveIntervals = new SurfaceAllocator.IntervalList();

    private final SurfaceAllocator.IntervalList mFinishedIntervals = new SurfaceAllocator.IntervalList();

    private final LinkedListMultimap<TextureProxy, SurfaceAllocator.Register> mFreePool = new LinkedListMultimap<>();

    private final Object2ObjectOpenHashMap<Object, SurfaceAllocator.Register> mUniqueKeyRegisters = new Object2ObjectOpenHashMap();

    private int mNumOps;

    private boolean mSimulated;

    private boolean mAllocated;

    private boolean mInstantiationFailed;

    private final SurfaceAllocator.Register[] mRegisterPool = new SurfaceAllocator.Register[128];

    private int mRegisterPoolSize;

    private final SurfaceAllocator.Interval[] mIntervalPool = new SurfaceAllocator.Interval[128];

    private int mIntervalPoolSize;

    public SurfaceAllocator(DirectContext context) {
        this.mContext = context;
    }

    public int curOp() {
        return this.mNumOps;
    }

    public void incOps() {
        this.mNumOps++;
    }

    public void addInterval(@Nonnull SurfaceProxy proxy, int start, int end, boolean actualUse) {
        assert start <= end;
        assert !this.mAllocated;
        if (!proxy.shouldSkipAllocator()) {
            if (!proxy.isReadOnly()) {
                Object proxyID = proxy.getUniqueID();
                SurfaceAllocator.Interval interval = (SurfaceAllocator.Interval) this.mIntervalHash.get(proxyID);
                if (interval != null) {
                    if (actualUse) {
                        interval.mUses++;
                    }
                    if (end > interval.mEnd) {
                        interval.mEnd = end;
                    }
                } else {
                    SurfaceAllocator.Interval newInterval = this.makeInterval(proxy, start, end);
                    if (actualUse) {
                        newInterval.mUses++;
                    }
                    this.mIntervalList.insertByIncreasingStart(newInterval);
                    this.mIntervalHash.put(proxyID, newInterval);
                }
            } else {
                ResourceProvider resourceProvider = this.mContext.getResourceProvider();
                if (proxy.isLazy() && !proxy.doLazyInstantiation(resourceProvider)) {
                    this.mInstantiationFailed = true;
                } else {
                    assert proxy.isInstantiated();
                }
            }
        }
    }

    public boolean isInstantiationFailed() {
        return this.mInstantiationFailed;
    }

    public boolean simulate() {
        this.mIntervalHash.clear();
        assert !this.mSimulated && !this.mAllocated;
        this.mSimulated = true;
        ResourceProvider resourceProvider = this.mContext.getResourceProvider();
        for (SurfaceAllocator.Interval cur = this.mIntervalList.peekHead(); cur != null; cur = cur.mNext) {
            this.expire(cur.mStart);
            this.mActiveIntervals.insertByIncreasingEnd(cur);
            if (!cur.mSurfaceProxy.isInstantiated()) {
                if (cur.mSurfaceProxy.isLazy()) {
                    if (cur.mSurfaceProxy.isLazyMost()) {
                        this.mInstantiationFailed = !cur.mSurfaceProxy.doLazyInstantiation(resourceProvider);
                        if (this.mInstantiationFailed) {
                            break;
                        }
                    }
                } else {
                    TextureProxy textureProxy = cur.mSurfaceProxy.asTexture();
                    assert textureProxy != null;
                    SurfaceAllocator.Register r = this.findOrCreateRegister(textureProxy, resourceProvider);
                    assert textureProxy.getGpuTexture() == null;
                    cur.mRegister = r;
                }
            }
        }
        this.expire(Integer.MAX_VALUE);
        return !this.mInstantiationFailed;
    }

    public boolean allocate() {
        if (this.mInstantiationFailed) {
            return false;
        } else {
            assert this.mSimulated && !this.mAllocated;
            this.mAllocated = true;
            ResourceProvider resourceProvider = this.mContext.getResourceProvider();
            SurfaceAllocator.Interval cur;
            while ((cur = this.mFinishedIntervals.popHead()) != null && !this.mInstantiationFailed) {
                if (!cur.mSurfaceProxy.isInstantiated()) {
                    if (cur.mSurfaceProxy.isLazy()) {
                        this.mInstantiationFailed = !cur.mSurfaceProxy.doLazyInstantiation(resourceProvider);
                    } else {
                        SurfaceAllocator.Register r = cur.mRegister;
                        assert r != null;
                        TextureProxy textureRef = cur.mSurfaceProxy.asTexture();
                        assert textureRef != null;
                        this.mInstantiationFailed = !r.instantiateTexture(textureRef, resourceProvider);
                    }
                }
            }
            return !this.mInstantiationFailed;
        }
    }

    public void reset() {
        this.mNumOps = 0;
        this.mSimulated = false;
        this.mAllocated = false;
        this.mInstantiationFailed = false;
        assert this.mActiveIntervals.isEmpty();
        this.mFinishedIntervals.clear();
        SurfaceAllocator.Interval cur;
        while ((cur = this.mIntervalList.popHead()) != null) {
            if (cur.mRegister != null && cur.mRegister.reset()) {
                this.freeRegister(cur.mRegister);
            }
            if (cur.reset()) {
                this.freeInterval(cur);
            } else {
                assert false;
            }
        }
        this.mIntervalList.clear();
        this.mIntervalHash.clear();
        this.mFreePool.clear();
        this.mUniqueKeyRegisters.clear();
    }

    private void expire(int curIndex) {
        while (!this.mActiveIntervals.isEmpty() && this.mActiveIntervals.peekHead().mEnd < curIndex) {
            SurfaceAllocator.Interval interval = this.mActiveIntervals.popHead();
            assert interval.mNext == null;
            SurfaceAllocator.Register r = interval.mRegister;
            if (r != null && r.isRecyclable(interval.mSurfaceProxy, interval.mUses)) {
                this.mFreePool.addLastEntry(r.mProxy, r);
            }
            this.mFinishedIntervals.insertByIncreasingStart(interval);
        }
    }

    private SurfaceAllocator.Register findOrCreateRegister(@Nonnull TextureProxy proxy, ResourceProvider provider) {
        Object uniqueKey = proxy.getUniqueKey();
        if (uniqueKey != null) {
            SurfaceAllocator.Register r = (SurfaceAllocator.Register) this.mUniqueKeyRegisters.get(uniqueKey);
            if (r != null) {
                return r;
            } else {
                r = this.makeRegister(proxy, provider, false);
                this.mUniqueKeyRegisters.put(uniqueKey, r);
                return r;
            }
        } else {
            SurfaceAllocator.Register r = this.mFreePool.pollFirstEntry(proxy);
            return r != null ? r : this.makeRegister(proxy, provider, true);
        }
    }

    private SurfaceAllocator.Register makeRegister(@Nonnull TextureProxy proxy, ResourceProvider provider, boolean scratch) {
        return this.mRegisterPoolSize == 0 ? new SurfaceAllocator.Register(proxy, provider, scratch) : this.mRegisterPool[--this.mRegisterPoolSize].init(proxy, provider, scratch);
    }

    private void freeRegister(@Nonnull SurfaceAllocator.Register register) {
        if (this.mRegisterPoolSize != this.mRegisterPool.length) {
            this.mRegisterPool[this.mRegisterPoolSize++] = register;
        }
    }

    private SurfaceAllocator.Interval makeInterval(@Nonnull SurfaceProxy proxy, int start, int end) {
        return this.mIntervalPoolSize == 0 ? new SurfaceAllocator.Interval(proxy, start, end) : this.mIntervalPool[--this.mIntervalPoolSize].init(proxy, start, end);
    }

    private void freeInterval(@Nonnull SurfaceAllocator.Interval interval) {
        if (this.mIntervalPoolSize != this.mIntervalPool.length) {
            this.mIntervalPool[this.mIntervalPoolSize++] = interval;
        }
    }

    private static class Interval {

        private SurfaceProxy mSurfaceProxy;

        private int mStart;

        private int mEnd;

        private SurfaceAllocator.Interval mNext;

        private int mUses;

        private SurfaceAllocator.Register mRegister;

        private boolean mInit;

        public Interval(SurfaceProxy surfaceProxy, int start, int end) {
            this.init(surfaceProxy, start, end);
        }

        public SurfaceAllocator.Interval init(SurfaceProxy proxy, int start, int end) {
            assert !this.mInit;
            assert proxy != null;
            this.mSurfaceProxy = proxy;
            this.mStart = start;
            this.mEnd = end;
            this.mNext = null;
            this.mUses = 0;
            this.mRegister = null;
            this.mInit = true;
            return this;
        }

        public boolean reset() {
            if (this.mInit) {
                this.mSurfaceProxy = null;
                this.mNext = null;
                this.mRegister = null;
                this.mInit = false;
                return true;
            } else {
                assert this.mSurfaceProxy == null;
                assert this.mNext == null;
                assert this.mRegister == null;
                return false;
            }
        }
    }

    private static class IntervalList {

        private SurfaceAllocator.Interval mHead;

        private SurfaceAllocator.Interval mTail;

        public IntervalList() {
        }

        public void clear() {
            this.mHead = this.mTail = null;
        }

        public boolean isEmpty() {
            assert this.mHead == null == (this.mTail == null);
            return this.mHead == null;
        }

        public SurfaceAllocator.Interval peekHead() {
            return this.mHead;
        }

        public SurfaceAllocator.Interval popHead() {
            SurfaceAllocator.Interval temp = this.mHead;
            if (temp != null) {
                this.mHead = temp.mNext;
                if (this.mHead == null) {
                    this.mTail = null;
                }
                temp.mNext = null;
            }
            return temp;
        }

        public void insertByIncreasingStart(@Nonnull SurfaceAllocator.Interval interval) {
            assert interval.mNext == null;
            if (this.mHead == null) {
                this.mHead = this.mTail = interval;
            } else if (interval.mStart <= this.mHead.mStart) {
                interval.mNext = this.mHead;
                this.mHead = interval;
            } else if (this.mTail.mStart <= interval.mStart) {
                this.mTail.mNext = interval;
                this.mTail = interval;
            } else {
                SurfaceAllocator.Interval prev = this.mHead;
                SurfaceAllocator.Interval next;
                for (next = prev.mNext; interval.mStart > next.mStart; next = next.mNext) {
                    prev = next;
                }
                interval.mNext = next;
                prev.mNext = interval;
            }
        }

        public void insertByIncreasingEnd(@Nonnull SurfaceAllocator.Interval interval) {
            assert interval.mNext == null;
            if (this.mHead == null) {
                this.mHead = this.mTail = interval;
            } else if (interval.mEnd <= this.mHead.mEnd) {
                interval.mNext = this.mHead;
                this.mHead = interval;
            } else if (this.mTail.mEnd <= interval.mEnd) {
                this.mTail.mNext = interval;
                this.mTail = interval;
            } else {
                SurfaceAllocator.Interval prev = this.mHead;
                SurfaceAllocator.Interval next;
                for (next = prev.mNext; interval.mEnd > next.mEnd; next = next.mNext) {
                    prev = next;
                }
                interval.mNext = next;
                prev.mNext = interval;
            }
        }
    }

    private static class Register {

        private TextureProxy mProxy;

        @SharedPtr
        private GpuTexture mTextureResource;

        private boolean mInit;

        public Register(TextureProxy proxy, ResourceProvider provider, boolean scratch) {
            this.init(proxy, provider, scratch);
        }

        public SurfaceAllocator.Register init(TextureProxy proxy, ResourceProvider provider, boolean scratch) {
            assert !this.mInit;
            assert proxy != null;
            assert !proxy.isInstantiated();
            assert !proxy.isLazy();
            this.mProxy = proxy;
            if (scratch) {
                this.mTextureResource = provider.findAndRefScratchTexture(proxy, null);
            } else {
                assert proxy.getUniqueKey() != null;
                this.mTextureResource = provider.findByUniqueKey(proxy.getUniqueKey());
            }
            this.mInit = true;
            return this;
        }

        public boolean isRecyclable(SurfaceProxy proxy, int knownUseCount) {
            if (this.mProxy.getUniqueKey() != null) {
                return false;
            } else {
                assert proxy.asTexture() != null;
                return !this.refCntGreaterThan(proxy, knownUseCount);
            }
        }

        public boolean refCntGreaterThan(SurfaceProxy proxy, int threadIsolatedTestCnt) {
            int cnt = proxy.getRefCntAcquire();
            assert cnt >= threadIsolatedTestCnt;
            return cnt > threadIsolatedTestCnt;
        }

        public boolean instantiateTexture(TextureProxy proxy, ResourceProvider resourceProvider) {
            assert proxy.getGpuTexture() == null;
            GpuTexture gpuTexture;
            if (this.mTextureResource == null) {
                if (this.mProxy == proxy) {
                    gpuTexture = proxy.createGpuTexture(resourceProvider);
                } else {
                    gpuTexture = GpuResource.create(this.mProxy.getGpuTexture());
                }
                if (gpuTexture == null) {
                    return false;
                }
            } else {
                gpuTexture = GpuResource.create(this.mTextureResource);
            }
            assert gpuTexture != null;
            assert (proxy.mSurfaceFlags & 8) == 0 || gpuTexture.asRenderTarget() != null;
            if (proxy.isBudgeted() && gpuTexture.getBudgetType() != 0) {
                gpuTexture.makeBudgeted(true);
            }
            if (proxy.getUniqueKey() != null) {
                if (gpuTexture.getUniqueKey() == null) {
                    resourceProvider.assignUniqueKeyToResource(proxy.getUniqueKey(), gpuTexture);
                }
                assert proxy.getUniqueKey().equals(gpuTexture.getUniqueKey());
            }
            assert proxy.mGpuTexture == null;
            proxy.mGpuTexture = gpuTexture;
            return true;
        }

        public boolean reset() {
            if (this.mInit) {
                this.mProxy = null;
                this.mTextureResource = GpuResource.move(this.mTextureResource);
                this.mInit = false;
                return true;
            } else {
                assert this.mProxy == null;
                assert this.mTextureResource == null;
                return false;
            }
        }
    }
}