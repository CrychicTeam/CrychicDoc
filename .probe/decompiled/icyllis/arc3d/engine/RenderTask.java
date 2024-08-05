package icyllis.arc3d.engine;

import icyllis.arc3d.core.RefCnt;
import icyllis.arc3d.core.SharedPtr;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;

public abstract class RenderTask extends RefCnt {

    private static final AtomicInteger sNextID = new AtomicInteger(1);

    public static final int RESOLVE_FLAG_MSAA = 1;

    public static final int RESOLVE_FLAG_MIPMAPS = 2;

    protected static final int CLOSED_FLAG = 1;

    protected static final int DETACHED_FLAG = 2;

    protected static final int SKIPPABLE_FLAG = 4;

    protected static final int ATLAS_FLAG = 8;

    protected static final int IN_RESULT_FLAG = 16;

    protected static final int TEMP_MARK_FLAG = 32;

    static final TopologicalSort.Access<RenderTask> SORT_ACCESS = new TopologicalSort.Access<RenderTask>() {

        public void setIndex(@Nonnull RenderTask node, int index) {
            node.setIndex(index);
        }

        public int getIndex(@Nonnull RenderTask node) {
            return node.getIndex();
        }

        public void setTempMarked(@Nonnull RenderTask node, boolean marked) {
            if (marked) {
                node.mFlags |= 32;
            } else {
                node.mFlags &= -33;
            }
        }

        public boolean isTempMarked(@Nonnull RenderTask node) {
            return (node.mFlags & 32) != 0;
        }

        public Collection<RenderTask> getIncomingEdges(@Nonnull RenderTask node) {
            return node.mDependencies;
        }
    };

    private final int mUniqueID;

    private int mFlags;

    final List<RenderTask> mDependencies = new ArrayList(1);

    final List<RenderTask> mDependents = new ArrayList(1);

    private TextureResolveTask mTextureResolveTask;

    @SharedPtr
    protected final List<SurfaceProxy> mTargets = new ArrayList(1);

    protected RenderTaskManager mTaskManager;

    private static int createUniqueID() {
        int value;
        int newValue;
        do {
            value = sNextID.get();
            newValue = value == -1 ? 1 : value + 1;
        } while (!sNextID.weakCompareAndSetVolatile(value, newValue));
        return value;
    }

    protected RenderTask(@Nonnull RenderTaskManager taskManager) {
        this.mTaskManager = taskManager;
        this.mUniqueID = createUniqueID();
    }

    public final int getUniqueID() {
        return this.mUniqueID;
    }

    public final int numTargets() {
        return this.mTargets.size();
    }

    public final SurfaceProxy getTarget(int index) {
        return (SurfaceProxy) this.mTargets.get(index);
    }

    public final SurfaceProxy getTarget() {
        assert this.numTargets() == 1;
        return (SurfaceProxy) this.mTargets.get(0);
    }

    private void setIndex(int index) {
        assert (this.mFlags & 16) == 0;
        assert index >= 0 && index < 67108864;
        this.mFlags |= index << 6 | 16;
    }

    private int getIndex() {
        return (this.mFlags & 16) != 0 ? this.mFlags >>> 6 : -1;
    }

    protected final void addTarget(@SharedPtr SurfaceProxy surfaceProxy) {
        assert this.mTaskManager.getContext().isOwnerThread();
        assert !this.isClosed();
        this.mTaskManager.setLastRenderTask(surfaceProxy, this);
        surfaceProxy.isUsedAsTaskTarget();
        this.mTargets.add(surfaceProxy);
    }

    @Override
    protected void deallocate() {
        this.mTargets.forEach(RefCnt::unref);
        this.mTargets.clear();
        assert (this.mFlags & 2) != 0;
    }

    public void gatherSurfaceIntervals(SurfaceAllocator alloc) {
    }

    public void prePrepare(RecordingContext context) {
    }

    public void prepare(OpFlushState flushState) {
    }

    public abstract boolean execute(OpFlushState var1);

    public final void makeClosed(RecordingContext context) {
        if (!this.isClosed()) {
            assert this.mTaskManager.getContext().isOwnerThread();
            this.onMakeClosed(context);
            if (this.mTextureResolveTask != null) {
                this.addDependency(this.mTextureResolveTask);
                this.mTextureResolveTask.makeClosed(context);
                this.mTextureResolveTask = null;
            }
            this.mFlags |= 1;
        }
    }

    protected void onMakeClosed(RecordingContext context) {
    }

    public final boolean isClosed() {
        return (this.mFlags & 1) != 0;
    }

    public final void detach(RenderTaskManager taskManager) {
        assert this.isClosed();
        assert this.mTaskManager == taskManager;
        if ((this.mFlags & 2) == 0) {
            assert taskManager.getContext().isOwnerThread();
            this.mTaskManager = null;
            this.mFlags |= 2;
            for (SurfaceProxy target : this.mTargets) {
                if (taskManager.getLastRenderTask(target) == this) {
                    taskManager.setLastRenderTask(target, null);
                }
            }
        }
    }

    public final void makeSkippable() {
        assert this.isClosed();
        if (!this.isSkippable()) {
            assert this.mTaskManager.getContext().isOwnerThread();
            this.mFlags |= 4;
            this.onMakeSkippable();
        }
    }

    protected void onMakeSkippable() {
    }

    public final boolean isSkippable() {
        return (this.mFlags & 4) != 0;
    }

    public final void addDependency(TextureProxy dependency, int samplerState) {
        assert this.mTaskManager.getContext().isOwnerThread();
        assert !this.isClosed();
        RenderTask dependencyTask = this.mTaskManager.getLastRenderTask(dependency);
        if (dependencyTask != this) {
            if (dependencyTask != null) {
                if (this.dependsOn(dependencyTask) || this.mTextureResolveTask == dependencyTask) {
                    return;
                }
                if ((dependencyTask.mFlags & 8) == 0) {
                    dependencyTask.makeClosed(this.mTaskManager.getContext());
                }
            }
            int resolveFlags = 0;
            if (dependency.isManualMSAAResolve() && dependency.needsResolve()) {
                resolveFlags |= 1;
            }
            if (SamplerState.isMipmapped(samplerState) && dependency.isMipmapped() && dependency.isMipmapsDirty()) {
                resolveFlags |= 2;
            }
            if (resolveFlags != 0) {
                if (this.mTextureResolveTask == null) {
                    this.mTextureResolveTask = new TextureResolveTask(this.mTaskManager);
                }
                this.mTextureResolveTask.addTexture(RefCnt.create(dependency), resolveFlags);
                assert dependencyTask == null || dependencyTask.isClosed();
                assert this.mTaskManager.getLastRenderTask(dependency) == this.mTextureResolveTask;
                assert dependencyTask == null || this.mTextureResolveTask.dependsOn(dependencyTask);
                assert !dependency.isManualMSAAResolve() || !dependency.needsResolve();
                assert !dependency.isMipmapped() || !dependency.isMipmapsDirty();
            } else {
                if (dependencyTask != null) {
                    this.addDependency(dependencyTask);
                }
            }
        }
    }

    public final boolean dependsOn(RenderTask dependency) {
        for (RenderTask task : this.mDependencies) {
            if (task == dependency) {
                return true;
            }
        }
        return false;
    }

    public final boolean isInstantiated() {
        for (SurfaceProxy target : this.mTargets) {
            if (!target.isInstantiated()) {
                return false;
            }
            GpuTexture texture = target.getGpuTexture();
            if (texture != null && texture.isDestroyed()) {
                return false;
            }
        }
        return true;
    }

    void addDependency(RenderTask dependency) {
        assert !dependency.dependsOn(this);
        assert !this.dependsOn(dependency);
        this.mDependencies.add(dependency);
        dependency.addDependent(this);
    }

    void addDependent(RenderTask dependent) {
        this.mDependents.add(dependent);
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        int numTargets = this.numTargets();
        if (numTargets > 0) {
            out.append("Targets: \n");
            for (int i = 0; i < numTargets; i++) {
                out.append(this.getTarget(i));
                out.append("\n");
            }
        }
        out.append("Dependencies (").append(this.mDependencies.size()).append("): ");
        for (RenderTask task : this.mDependencies) {
            out.append(task.mUniqueID).append(", ");
        }
        out.append("\n");
        out.append("Dependents (").append(this.mDependents.size()).append("): ");
        for (RenderTask task : this.mDependents) {
            out.append(task.mUniqueID).append(", ");
        }
        out.append("\n");
        return out.toString();
    }
}