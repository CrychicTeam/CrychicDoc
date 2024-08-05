package icyllis.modernui.util;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import org.jetbrains.annotations.ApiStatus.Internal;

public class ColorStateList {

    private static final int DEFAULT_COLOR = -65536;

    private static final int[][] EMPTY = new int[][] { new int[0] };

    @GuardedBy("sCache")
    private static final SparseArray<WeakReference<ColorStateList>> sCache = new SparseArray<>();

    private int[][] mStateSpecs;

    private int[] mColors;

    private int mDefaultColor;

    private boolean mIsOpaque;

    public ColorStateList(int[][] states, int[] colors) {
        this.mStateSpecs = states;
        this.mColors = colors;
        this.onColorsChanged();
    }

    @Nonnull
    public static ColorStateList valueOf(int color) {
        synchronized (sCache) {
            int index = sCache.indexOfKey(color);
            if (index >= 0) {
                ColorStateList cached = (ColorStateList) sCache.valueAt(index).get();
                if (cached != null) {
                    return cached;
                }
                sCache.removeAt(index);
            }
            int N = sCache.size();
            for (int i = N - 1; i >= 0; i--) {
                if (sCache.valueAt(i).get() == null) {
                    sCache.removeAt(i);
                }
            }
            ColorStateList csl = new ColorStateList(EMPTY, new int[] { color });
            sCache.put(color, new WeakReference(csl));
            return csl;
        }
    }

    private ColorStateList(@Nullable ColorStateList orig) {
        if (orig != null) {
            this.mStateSpecs = orig.mStateSpecs;
            this.mDefaultColor = orig.mDefaultColor;
            this.mIsOpaque = orig.mIsOpaque;
            this.mColors = orig.mColors;
        }
    }

    @Nonnull
    public ColorStateList withAlpha(int alpha) {
        int[] colors = new int[this.mColors.length];
        int len = colors.length;
        for (int i = 0; i < len; i++) {
            colors[i] = this.mColors[i] & 16777215 | alpha << 24;
        }
        return new ColorStateList(this.mStateSpecs, colors);
    }

    public boolean isStateful() {
        return this.mStateSpecs.length >= 1 && this.mStateSpecs[0].length > 0;
    }

    @Internal
    public boolean hasFocusStateSpecified() {
        return StateSet.containsAttribute(this.mStateSpecs, 16844130);
    }

    public boolean isOpaque() {
        return this.mIsOpaque;
    }

    public int getColorForState(int[] stateSet, int defaultColor) {
        int setLength = this.mStateSpecs.length;
        for (int i = 0; i < setLength; i++) {
            int[] stateSpec = this.mStateSpecs[i];
            if (StateSet.stateSetMatches(stateSpec, stateSet)) {
                return this.mColors[i];
            }
        }
        return defaultColor;
    }

    public int getDefaultColor() {
        return this.mDefaultColor;
    }

    @Internal
    public int[][] getStates() {
        return this.mStateSpecs;
    }

    @Internal
    public int[] getColors() {
        return this.mColors;
    }

    @Internal
    public boolean hasState(int state) {
        int[][] stateSpecs = this.mStateSpecs;
        for (int[] states : stateSpecs) {
            for (int i : states) {
                if (i == state || i == ~state) {
                    return true;
                }
            }
        }
        return false;
    }

    public String toString() {
        return "ColorStateList{mStateSpecs=" + Arrays.deepToString(this.mStateSpecs) + "mColors=" + Arrays.toString(this.mColors) + "mDefaultColor=" + this.mDefaultColor + "}";
    }

    private void onColorsChanged() {
        int defaultColor = -65536;
        boolean isOpaque = true;
        int[][] states = this.mStateSpecs;
        int[] colors = this.mColors;
        int N = states.length;
        if (N > 0) {
            defaultColor = colors[0];
            for (int i = N - 1; i > 0; i--) {
                if (states[i].length == 0) {
                    defaultColor = colors[i];
                    break;
                }
            }
            for (int ix = 0; ix < N; ix++) {
                if (colors[ix] >>> 24 != 255) {
                    isOpaque = false;
                    break;
                }
            }
        }
        this.mDefaultColor = defaultColor;
        this.mIsOpaque = isOpaque;
    }
}