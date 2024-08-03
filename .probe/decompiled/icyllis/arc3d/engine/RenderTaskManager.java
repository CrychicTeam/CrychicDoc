package icyllis.arc3d.engine;

import icyllis.arc3d.core.RefCnt;
import icyllis.arc3d.core.SharedPtr;
import icyllis.arc3d.engine.ops.OpsTask;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.util.ArrayList;

public class RenderTaskManager {

    private final RecordingContext mContext;

    private final DirectContext mDirect;

    @SharedPtr
    private final ArrayList<RenderTask> mDAG = new ArrayList();

    private final Reference2ObjectOpenHashMap<Object, RenderTask> mLastRenderTasks = new Reference2ObjectOpenHashMap();

    private OpsTask mActiveOpsTask = null;

    private final OpFlushState mFlushState;

    private final SurfaceAllocator mSurfaceAllocator;

    private boolean mFlushing;

    public RenderTaskManager(RecordingContext context) {
        this.mContext = context;
        if (context instanceof DirectContext direct) {
            this.mDirect = direct;
            this.mFlushState = new OpFlushState(direct.getDevice(), direct.getResourceProvider());
            this.mSurfaceAllocator = new SurfaceAllocator(direct);
        } else {
            this.mDirect = null;
            this.mFlushState = null;
            this.mSurfaceAllocator = null;
        }
    }

    void destroy() {
        this.closeTasks();
        this.clearTasks();
    }

    RecordingContext getContext() {
        return this.mContext;
    }

    public OpFlushState getFlushState() {
        return this.mFlushState;
    }

    public boolean flush(FlushInfo info) {
        if (!this.mFlushing && !this.mContext.isDiscarded()) {
            this.mFlushing = true;
            DirectContext context = this.mDirect;
            assert context != null;
            GpuDevice device = context.getDevice();
            assert device != null;
            this.closeTasks();
            this.mActiveOpsTask = null;
            TopologicalSort.topologicalSort(this.mDAG, RenderTask.SORT_ACCESS);
            for (RenderTask task : this.mDAG) {
                task.gatherSurfaceIntervals(this.mSurfaceAllocator);
            }
            this.mSurfaceAllocator.simulate();
            this.mSurfaceAllocator.allocate();
            boolean cleanup = false;
            if (!this.mSurfaceAllocator.isInstantiationFailed()) {
                cleanup = this.executeRenderTasks();
            }
            this.mSurfaceAllocator.reset();
            this.clearTasks();
            if (cleanup) {
                context.getResourceCache().cleanup();
            }
            this.mFlushing = false;
            return true;
        } else {
            if (info != null) {
                if (info.mSubmittedCallback != null) {
                    info.mSubmittedCallback.onSubmitted(false);
                }
                if (info.mFinishedCallback != null) {
                    info.mFinishedCallback.onFinished();
                }
            }
            return false;
        }
    }

    public void closeTasks() {
        for (RenderTask task : this.mDAG) {
            task.makeClosed(this.mContext);
        }
    }

    public void clearTasks() {
        for (RenderTask task : this.mDAG) {
            task.detach(this);
            task.unref();
        }
        this.mDAG.clear();
        this.mLastRenderTasks.clear();
    }

    public RenderTask appendTask(@SharedPtr RenderTask task) {
        this.mDAG.add(RefCnt.create(task));
        return task;
    }

    public RenderTask prependTask(@SharedPtr RenderTask task) {
        if (this.mDAG.isEmpty()) {
            this.mDAG.add(task);
        } else {
            int pos = this.mDAG.size() - 1;
            this.mDAG.add((RenderTask) this.mDAG.get(pos));
            this.mDAG.set(pos, task);
        }
        return task;
    }

    public void setLastRenderTask(SurfaceProxy surfaceProxy, RenderTask task) {
        if (task != null) {
            this.mLastRenderTasks.put(surfaceProxy.getUniqueID(), task);
        } else {
            this.mLastRenderTasks.remove(surfaceProxy);
        }
    }

    public RenderTask getLastRenderTask(SurfaceProxy proxy) {
        return (RenderTask) this.mLastRenderTasks.get(proxy.getUniqueID());
    }

    @SharedPtr
    public OpsTask newOpsTask(SurfaceView writeView) {
        OpsTask opsTask = new OpsTask(this, writeView);
        this.appendTask(opsTask);
        return opsTask;
    }

    private boolean executeRenderTasks() {
        boolean executed = false;
        for (RenderTask task : this.mDAG) {
            if (task.isInstantiated()) {
                task.prepare(this.mFlushState);
            }
        }
        for (RenderTask taskx : this.mDAG) {
            if (taskx.isInstantiated()) {
                executed |= taskx.execute(this.mFlushState);
            }
        }
        this.mFlushState.reset();
        return executed;
    }
}