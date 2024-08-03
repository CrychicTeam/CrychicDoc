package icyllis.arc3d.engine;

import icyllis.arc3d.core.SharedPtr;
import java.util.ArrayList;

public abstract class CommandBuffer {

    @SharedPtr
    private final ArrayList<GpuBuffer> mTrackingGpuBuffers = new ArrayList();

    public void moveAndTrackGpuBuffer(@SharedPtr GpuBuffer buffer) {
        this.mTrackingGpuBuffers.add(buffer);
    }
}