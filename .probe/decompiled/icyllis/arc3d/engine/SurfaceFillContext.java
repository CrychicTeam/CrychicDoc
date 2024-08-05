package icyllis.arc3d.engine;

import icyllis.arc3d.core.ColorSpace;
import icyllis.arc3d.core.RefCnt;
import icyllis.arc3d.core.SharedPtr;
import icyllis.arc3d.engine.ops.OpsTask;

public class SurfaceFillContext extends SurfaceContext {

    @SharedPtr
    private OpsTask mOpsTask;

    private final SurfaceView mWriteView;

    public SurfaceFillContext(RecordingContext context, SurfaceView readView, SurfaceView writeView, int colorType, int alphaType, ColorSpace colorSpace) {
        super(context, readView, colorType, alphaType, colorSpace);
        this.mWriteView = writeView;
    }

    public OpsTask getOpsTask() {
        assert this.mContext.isOwnerThread();
        return this.mOpsTask != null && !this.mOpsTask.isClosed() ? this.mOpsTask : this.nextOpsTask();
    }

    public OpsTask nextOpsTask() {
        OpsTask newOpsTask = this.getDrawingManager().newOpsTask(this.mWriteView);
        this.mOpsTask = RefCnt.move(this.mOpsTask, newOpsTask);
        return this.mOpsTask;
    }
}