package icyllis.arc3d.core;

import icyllis.arc3d.engine.RecordingContext;

public final class DisplayListRecorder implements AutoCloseable {

    private final SurfaceCharacterization mCharacterization;

    private RecordingContext mContext;

    public DisplayListRecorder(SurfaceCharacterization c) {
        this.mCharacterization = c;
        if (c != null) {
            this.mContext = RecordingContext.makeRecording(c.getContextInfo());
        }
    }

    public void close() {
        this.mContext = RefCnt.move(this.mContext);
    }
}