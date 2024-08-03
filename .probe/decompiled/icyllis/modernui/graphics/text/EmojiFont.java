package icyllis.modernui.graphics.text;

import com.ibm.icu.text.BreakIterator;
import icyllis.arc3d.core.Strike;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.graphics.Rect;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.List;
import java.util.Locale;

public final class EmojiFont implements Font {

    private final String mName;

    private final IntSet mCoverage;

    private final float mBaseSize;

    private final float mBaseAscent;

    private final float mBaseDescent;

    private final float mBaseSpacing;

    private final Object2IntMap<CharSequence> mMap;

    private final List<String> mFiles;

    private final CharSequenceBuilder mLookupKey = new CharSequenceBuilder();

    public EmojiFont(String name, IntSet coverage, int size, int ascent, int spacing, int base, Object2IntMap<CharSequence> map, List<String> files) {
        this.mName = name;
        this.mCoverage = coverage;
        this.mBaseSize = (float) size / (float) base;
        this.mBaseAscent = (float) ascent / (float) base;
        this.mBaseDescent = (float) (size - ascent) / (float) base;
        this.mBaseSpacing = (float) spacing / (float) base;
        this.mMap = map;
        this.mFiles = files;
    }

    public String getFileName(int glyphId) {
        return (String) this.mFiles.get(glyphId - 1);
    }

    @Override
    public int getStyle() {
        return 0;
    }

    @Override
    public String getFullName(@NonNull Locale locale) {
        return this.mName;
    }

    @Override
    public String getFamilyName(@NonNull Locale locale) {
        return this.mName;
    }

    @Override
    public int getMetrics(@NonNull FontPaint paint, @Nullable FontMetricsInt fm) {
        int size = paint.getFontSize();
        int ascent = (int) (0.95 + (double) (this.mBaseAscent * (float) size));
        int descent = (int) (0.95 + (double) (this.mBaseDescent * (float) size));
        if (fm != null) {
            fm.extendBy(-ascent, descent);
        }
        return ascent + descent;
    }

    @Override
    public boolean hasGlyph(int ch, int vs) {
        return this.mCoverage.contains(ch);
    }

    @Override
    public int calcGlyphScore(char[] buf, int start, int limit) {
        BreakIterator breaker = BreakIterator.getCharacterInstance(Locale.ROOT);
        CharArrayIterator iterator = new CharArrayIterator(buf, start, limit);
        breaker.setText(iterator);
        int prevPos = start;
        int currPos;
        while ((currPos = breaker.following(prevPos)) != -1) {
            int glyphId = this.find(buf, prevPos, currPos);
            if (glyphId == 0) {
                return prevPos;
            }
            prevPos = currPos;
        }
        return prevPos;
    }

    private int find(char[] buf, int start, int limit) {
        int glyphId;
        synchronized (this.mLookupKey) {
            glyphId = this.mMap.getInt(this.mLookupKey.updateChars(buf, start, limit));
        }
        if (glyphId == 0) {
            char vs = buf[limit - 1];
            if (vs == '️') {
                synchronized (this.mLookupKey) {
                    glyphId = this.mMap.getInt(this.mLookupKey.updateChars(buf, start, limit - 1));
                }
            }
        }
        return glyphId;
    }

    @Override
    public float doSimpleLayout(char[] buf, int start, int limit, FontPaint paint, IntArrayList glyphs, FloatArrayList positions, float x, float y) {
        return 0.0F;
    }

    @Override
    public float doComplexLayout(char[] buf, int contextStart, int contextLimit, int layoutStart, int layoutLimit, boolean isRtl, FontPaint paint, IntArrayList glyphs, FloatArrayList positions, float[] advances, int advanceOffset, Rect bounds, float x, float y) {
        BreakIterator breaker = BreakIterator.getCharacterInstance(Locale.ROOT);
        CharArrayIterator iterator = new CharArrayIterator(buf, layoutStart, layoutLimit);
        breaker.setText(iterator);
        boolean hint = (paint.getRenderFlags() & 2) == 0;
        float sz = (float) paint.getFontSize();
        float add = this.mBaseSpacing * sz;
        if (hint) {
            add = (float) Math.max(1, (int) (0.95 + (double) add));
        }
        sz = this.mBaseSize * sz;
        if (hint) {
            sz = (float) ((int) (0.95 + (double) sz));
        }
        float adv = sz + add * 2.0F;
        if (hint) {
            x = (float) ((int) x);
            y = (float) ((int) y);
        }
        int prevPos;
        if (isRtl) {
            prevPos = layoutLimit;
        } else {
            prevPos = layoutStart;
        }
        float currAdvance = 0.0F;
        int currPos;
        while ((currPos = isRtl ? breaker.preceding(prevPos) : breaker.following(prevPos)) != -1) {
            int pieceStart = Math.min(prevPos, currPos);
            int pieceLimit = Math.max(prevPos, currPos);
            int glyphId = this.find(buf, pieceStart, pieceLimit);
            if (glyphId != 0) {
                if (advances != null) {
                    advances[pieceStart - advanceOffset] = adv;
                }
                if (glyphs != null) {
                    glyphs.add(glyphId);
                }
                if (positions != null) {
                    positions.add(x + currAdvance + add);
                    positions.add(y);
                }
                currAdvance += adv;
            }
            prevPos = currPos;
        }
        if (bounds != null) {
            int s = paint.getFontSize();
            bounds.union((int) x, (int) ((double) (y - this.mBaseAscent * (float) s) - 0.05), (int) ((double) (x + currAdvance) + 0.95), (int) ((double) (y + this.mBaseDescent * (float) s) + 0.95));
        }
        return currAdvance;
    }

    @Override
    public Strike findOrCreateStrike(FontPaint paint) {
        return null;
    }
}