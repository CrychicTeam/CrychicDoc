package icyllis.modernui.lifecycle;

import icyllis.modernui.annotation.UiThread;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public abstract class ViewModel {

    static final Marker MARKER = MarkerManager.getMarker("ViewModel");

    protected void onCleared() {
    }

    @UiThread
    final void clear() {
        this.onCleared();
    }
}