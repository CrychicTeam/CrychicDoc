package icyllis.modernui.text;

import icyllis.modernui.text.style.TabStopSpan;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;

public class TabStops {

    private float[] mStops;

    private int mNumStops;

    private float mTabWidth;

    public TabStops(float[] stops, float tabWidth) {
        this.mStops = stops;
        this.mNumStops = stops == null ? 0 : stops.length;
        this.mTabWidth = tabWidth;
    }

    public TabStops(float tabWidth, @Nonnull List<?> spans) {
        this.reset(tabWidth, spans);
    }

    public void reset(float tabWidth, @Nonnull List<?> spans) {
        this.mTabWidth = tabWidth;
        int ns = 0;
        float[] stops = this.mStops;
        for (Object o : spans) {
            if (o instanceof TabStopSpan) {
                if (stops == null) {
                    stops = new float[2];
                } else if (ns == stops.length) {
                    float[] newStops = new float[ns << 1];
                    System.arraycopy(stops, 0, newStops, 0, ns);
                    stops = newStops;
                }
                stops[ns++] = (float) ((TabStopSpan) o).getTabStop();
            }
        }
        if (ns > 1) {
            Arrays.sort(stops, 0, ns);
        }
        if (stops != this.mStops) {
            this.mStops = stops;
        }
        this.mNumStops = ns;
    }

    public float nextTab(float width) {
        int ns = this.mNumStops;
        if (ns > 0) {
            float[] stops = this.mStops;
            for (int i = 0; i < ns; i++) {
                float stop = stops[i];
                if (stop > width) {
                    return stop;
                }
            }
        }
        return nextDefaultStop(width, this.mTabWidth);
    }

    public static float nextDefaultStop(float width, float tabWidth) {
        return (float) ((int) (width / tabWidth + 1.0F)) * tabWidth;
    }
}