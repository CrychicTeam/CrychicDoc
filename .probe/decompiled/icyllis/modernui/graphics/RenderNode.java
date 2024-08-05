package icyllis.modernui.graphics;

import javax.annotation.Nonnull;

public final class RenderNode extends RenderProperties {

    private Canvas mCurrentRecordingCanvas;

    @Nonnull
    public Canvas beginRecording(int width, int height) {
        if (this.mCurrentRecordingCanvas != null) {
            throw new IllegalStateException("Recording currently in progress - missing #endRecording() call?");
        } else {
            return this.mCurrentRecordingCanvas;
        }
    }

    public void endRecording() {
        if (this.mCurrentRecordingCanvas == null) {
            throw new IllegalStateException("No recording in progress, forgot to call #beginRecording()?");
        } else {
            Canvas canvas = this.mCurrentRecordingCanvas;
            this.mCurrentRecordingCanvas = null;
        }
    }
}