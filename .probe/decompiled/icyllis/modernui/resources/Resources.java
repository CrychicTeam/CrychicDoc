package icyllis.modernui.resources;

import icyllis.modernui.util.DisplayMetrics;
import org.jetbrains.annotations.ApiStatus.Internal;

public class Resources {

    private final DisplayMetrics mMetrics = new DisplayMetrics();

    public Resources() {
        this.mMetrics.setToDefaults();
    }

    @Internal
    public void updateMetrics(DisplayMetrics metrics) {
        if (metrics != null) {
            this.mMetrics.setTo(metrics);
        }
    }

    public DisplayMetrics getDisplayMetrics() {
        return this.mMetrics;
    }
}