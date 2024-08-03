package icyllis.modernui.graphics.text;

import icyllis.arc3d.core.Strike;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.graphics.Rect;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.Locale;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface Font {

    int getStyle();

    default String getFullName() {
        return this.getFullName(Locale.ROOT);
    }

    String getFullName(@NonNull Locale var1);

    default String getFamilyName() {
        return this.getFamilyName(Locale.ROOT);
    }

    String getFamilyName(@NonNull Locale var1);

    int getMetrics(@NonNull FontPaint var1, @Nullable FontMetricsInt var2);

    @Internal
    boolean hasGlyph(int var1, int var2);

    @Internal
    default int calcGlyphScore(char[] buf, int start, int limit) {
        for (int i = start; i < limit; i++) {
            char c = buf[i];
            if (!this.hasGlyph(c, 0)) {
                if (!Character.isHighSurrogate(c)) {
                    return i;
                }
                if (!this.hasGlyph(Character.codePointAt(buf, i, limit), 0)) {
                    return i;
                }
                i++;
            }
        }
        return limit;
    }

    @Internal
    float doSimpleLayout(char[] var1, int var2, int var3, FontPaint var4, IntArrayList var5, FloatArrayList var6, float var7, float var8);

    @Internal
    float doComplexLayout(char[] var1, int var2, int var3, int var4, int var5, boolean var6, FontPaint var7, IntArrayList var8, FloatArrayList var9, float[] var10, int var11, Rect var12, float var13, float var14);

    @Internal
    Strike findOrCreateStrike(FontPaint var1);
}