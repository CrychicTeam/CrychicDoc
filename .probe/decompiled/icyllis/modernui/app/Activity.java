package icyllis.modernui.app;

import icyllis.modernui.core.Context;
import icyllis.modernui.view.WindowManager;
import icyllis.modernui.widget.ToastManager;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;

@Experimental
public abstract class Activity extends Context {

    private volatile ToastManager mToastManager;

    @Internal
    public ToastManager getToastManager() {
        if (this.mToastManager != null) {
            return this.mToastManager;
        } else {
            synchronized (this) {
                if (this.mToastManager == null) {
                    this.mToastManager = new ToastManager(this);
                }
            }
            return this.mToastManager;
        }
    }

    @Internal
    public WindowManager getWindowManager() {
        return null;
    }
}