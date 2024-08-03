package icyllis.modernui.mc.text;

import com.ibm.icu.text.Bidi;
import com.ibm.icu.text.BidiRun;
import com.ibm.icu.text.BreakIterator;
import icyllis.modernui.ModernUI;
import icyllis.modernui.graphics.text.CharArrayIterator;
import icyllis.modernui.graphics.text.CharSequenceBuilder;
import icyllis.modernui.graphics.text.EmojiFont;
import icyllis.modernui.graphics.text.Font;
import icyllis.modernui.graphics.text.FontCollection;
import icyllis.modernui.graphics.text.FontPaint;
import icyllis.modernui.graphics.text.LineBreaker;
import icyllis.modernui.graphics.text.ShapedText;
import icyllis.modernui.text.TextDirectionHeuristic;
import icyllis.modernui.text.TextDirectionHeuristics;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.StringDecomposer;
import net.minecraft.util.Unit;

public class TextLayoutProcessor {

    public static final boolean DEBUG = false;

    public static final int DEFAULT_BASE_FONT_SIZE = 8;

    public static volatile float sBaseFontSize = 8.0F;

    public static volatile int sLbStyle = 0;

    public static volatile int sLbWordStyle = 0;

    private final TextLayoutEngine mEngine;

    private final CharSequenceBuilder mBuilder = new CharSequenceBuilder();

    private final IntArrayList mStyles = new IntArrayList();

    private final ArrayList<ResourceLocation> mFontNames = new ArrayList();

    private final IntArrayList mGlyphs = new IntArrayList();

    private final ByteArrayList mFontIndices = new ByteArrayList();

    private final ArrayList<Font> mFontVec = new ArrayList();

    private final HashMap<Font, Byte> mFontMap = new HashMap();

    private final Function<Font, Byte> mNextID = font -> {
        this.mFontVec.add(font);
        return (byte) this.mFontMap.size();
    };

    private final FloatArrayList mPositions = new FloatArrayList();

    private final FloatArrayList mAdvances = new FloatArrayList();

    private final IntArrayList mGlyphFlags = new IntArrayList();

    private final IntArrayList mLineBoundaries = new IntArrayList();

    private float mTotalAdvance;

    private final FontPaint mFontPaint = new FontPaint();

    private boolean mHasEffect;

    private boolean mHasColorEmoji;

    private boolean mComputeAdvances = true;

    private boolean mComputeLineBoundaries = true;

    private final FormattedCharSink mSequenceBuilder = (index, style, codePoint) -> {
        int styleFlags = CharacterStyle.flatten(style);
        int charCount = this.mBuilder.addCodePoint(codePoint);
        while (charCount-- > 0) {
            this.mStyles.add(styleFlags);
            this.mFontNames.add(style.getFont());
        }
        return true;
    };

    private final FormattedText.StyledContentConsumer<Unit> mContentBuilder = (style, text) -> StringDecomposer.iterateFormatted(text, style, this.mSequenceBuilder) ? Optional.empty() : FormattedText.STOP_ITERATION;

    public TextLayoutProcessor(@Nonnull TextLayoutEngine engine) {
        this.mEngine = engine;
    }

    public static int computeFontSize(float resLevel) {
        return Math.min((int) ((double) (sBaseFontSize * resLevel) + 0.5), 96);
    }

    private void reset() {
        this.mBuilder.clear();
        this.mStyles.clear();
        this.mFontNames.clear();
        this.mGlyphs.clear();
        this.mFontIndices.clear();
        this.mFontVec.clear();
        this.mFontMap.clear();
        this.mPositions.clear();
        this.mAdvances.clear();
        this.mGlyphFlags.clear();
        this.mLineBoundaries.clear();
        this.mTotalAdvance = 0.0F;
        this.mHasEffect = false;
        this.mHasColorEmoji = false;
    }

    @Nonnull
    public TextLayout createVanillaLayout(@Nonnull String text, @Nonnull Style style, int resLevel, int computeFlags) {
        StringDecomposer.iterateFormatted(text, style, this.mSequenceBuilder);
        TextLayout layout = this.createNewLayout(resLevel, computeFlags);
        this.reset();
        return layout;
    }

    @Nonnull
    public TextLayout createTextLayout(@Nonnull FormattedText text, @Nonnull Style style, int resLevel, int computeFlags) {
        text.visit(this.mContentBuilder, style);
        TextLayout layout = this.createNewLayout(resLevel, computeFlags);
        this.reset();
        return layout;
    }

    @Nonnull
    public TextLayout createSequenceLayout(@Nonnull FormattedCharSequence sequence, int resLevel, int computeFlags) {
        sequence.accept(this.mSequenceBuilder);
        TextLayout layout = this.createNewLayout(resLevel, computeFlags);
        this.reset();
        return layout;
    }

    @Nonnull
    private TextLayout createNewLayout(int resLevel, int computeFlags) {
        if (this.mBuilder.isEmpty()) {
            return TextLayout.makeEmpty();
        } else {
            this.mFontPaint.setLocale(ModernUI.getSelectedLocale());
            this.mComputeAdvances = (computeFlags & 1) != 0;
            this.mComputeLineBoundaries = (computeFlags & 4) != 0;
            int fontSize = computeFontSize((float) resLevel);
            this.mFontPaint.setFontSize(fontSize);
            if (this.mComputeAdvances) {
                this.mAdvances.size(this.mBuilder.length());
            }
            char[] textBuf = this.mBuilder.toCharArray();
            this.analyzeBidi(textBuf);
            float[] positions = this.mPositions.toFloatArray();
            for (int i = 0; i < positions.length; i++) {
                positions[i] /= (float) resLevel;
            }
            byte[] fontIndices;
            if (this.mFontVec.size() > 1) {
                fontIndices = this.mFontIndices.toByteArray();
            } else {
                fontIndices = null;
            }
            float[] advances;
            if (this.mComputeAdvances) {
                advances = this.mAdvances.toFloatArray();
                for (int i = 0; i < this.mBuilder.length(); i++) {
                    advances[i] /= (float) resLevel;
                }
            } else {
                advances = null;
            }
            int[] lineBoundaries;
            if (this.mComputeLineBoundaries) {
                lineBoundaries = this.mLineBoundaries.toIntArray();
                Arrays.sort(lineBoundaries);
            } else {
                lineBoundaries = null;
            }
            this.mTotalAdvance /= (float) resLevel;
            return new TextLayout(textBuf, this.mGlyphs.toIntArray(), positions, fontIndices, (Font[]) this.mFontVec.toArray(new Font[0]), advances, this.mGlyphFlags.toIntArray(), lineBoundaries, this.mTotalAdvance, this.mHasEffect, this.mHasColorEmoji, resLevel, computeFlags);
        }
    }

    private void analyzeBidi(@Nonnull char[] text) {
        TextDirectionHeuristic dir = this.mEngine.getTextDirectionHeuristic();
        if ((dir == TextDirectionHeuristics.LTR || dir == TextDirectionHeuristics.FIRSTSTRONG_LTR || dir == TextDirectionHeuristics.ANYRTL_LTR) && !Bidi.requiresBidi(text, 0, text.length)) {
            this.handleBidiRun(text, 0, text.length, false);
        } else {
            byte paraLevel;
            if (dir == TextDirectionHeuristics.LTR) {
                paraLevel = 0;
            } else if (dir == TextDirectionHeuristics.RTL) {
                paraLevel = 1;
            } else if (dir == TextDirectionHeuristics.FIRSTSTRONG_LTR) {
                paraLevel = 126;
            } else if (dir == TextDirectionHeuristics.FIRSTSTRONG_RTL) {
                paraLevel = 127;
            } else {
                boolean isRtl = dir.isRtl(text, 0, text.length);
                paraLevel = (byte) (isRtl ? 1 : 0);
            }
            Bidi bidi = new Bidi(text.length, 0);
            bidi.setPara(text, paraLevel, null);
            if (bidi.isRightToLeft()) {
                this.handleBidiRun(text, 0, text.length, true);
            } else if (bidi.isLeftToRight()) {
                this.handleBidiRun(text, 0, text.length, false);
            } else {
                int runCount = bidi.getRunCount();
                for (int visualIndex = 0; visualIndex < runCount; visualIndex++) {
                    BidiRun run = bidi.getVisualRun(visualIndex);
                    this.handleBidiRun(text, run.getStart(), run.getLimit(), run.isOddRun());
                }
            }
        }
    }

    private void handleBidiRun(@Nonnull char[] text, int start, int limit, boolean isRtl) {
        IntArrayList styles = this.mStyles;
        List<ResourceLocation> fonts = this.mFontNames;
        if (isRtl) {
            int lastPos = limit;
            int currPos = limit - 1;
            int lastStyle = styles.getInt(currPos);
            ResourceLocation lastFont = (ResourceLocation) fonts.get(currPos);
            int currStyle = lastStyle;
            ResourceLocation currFont;
            for (currFont = lastFont; currPos > start; currPos--) {
                if ((currStyle = styles.getInt(currPos - 1)) != lastStyle || (currFont = (ResourceLocation) fonts.get(currPos - 1)) != lastFont) {
                    this.handleStyleRun(text, currPos, lastPos, true, lastStyle, lastFont);
                    lastPos = currPos;
                    lastStyle = currStyle;
                    lastFont = currFont;
                }
            }
            assert currPos == start;
            this.handleStyleRun(text, currPos, lastPos, true, currStyle, currFont);
        } else {
            int lastPos = start;
            int currPos = start;
            int lastStyle = styles.getInt(start);
            ResourceLocation lastFont = (ResourceLocation) fonts.get(start);
            int currStyle = lastStyle;
            ResourceLocation currFontx = lastFont;
            while (currPos + 1 < limit) {
                currPos++;
                if ((currStyle = styles.getInt(currPos)) != lastStyle || (currFontx = (ResourceLocation) fonts.get(currPos)) != lastFont) {
                    this.handleStyleRun(text, lastPos, currPos, false, lastStyle, lastFont);
                    lastPos = currPos;
                    lastStyle = currStyle;
                    lastFont = currFontx;
                }
            }
            assert currPos + 1 == limit;
            this.handleStyleRun(text, lastPos, currPos + 1, false, currStyle, currFontx);
        }
    }

    private void handleStyleRun(@Nonnull char[] text, int start, int limit, boolean isRtl, int styleFlags, ResourceLocation fontName) {
        int fontStyle = 0;
        if ((styleFlags & 16777216) != 0) {
            fontStyle |= 1;
        }
        if ((styleFlags & 33554432) != 0) {
            fontStyle |= 2;
        }
        this.mFontPaint.setFont(this.mEngine.getFontCollection(fontName));
        this.mFontPaint.setFontStyle(fontStyle);
        if ((styleFlags & 268435456) == 0) {
            int glyphStart = this.mGlyphs.size();
            float advance = ShapedText.doLayoutRun(text, start, limit, start, limit, isRtl, this.mFontPaint, 0, this.mComputeAdvances ? this.mAdvances.elements() : null, this.mTotalAdvance, this.mGlyphs, this.mPositions, this.mFontIndices, f -> (Byte) this.mFontMap.computeIfAbsent(f, this.mNextID), null, null);
            int glyphIndex = glyphStart;
            for (int glyphEnd = this.mGlyphs.size(); glyphIndex < glyphEnd; glyphIndex++) {
                this.mHasEffect |= (styleFlags & 201326592) != 0;
                int glyphFlags = styleFlags;
                Font font = (Font) this.mFontVec.get(this.mFontIndices.getByte(glyphIndex));
                if (font instanceof BitmapFont) {
                    glyphFlags = styleFlags | 1073741824;
                } else if (font instanceof EmojiFont) {
                    glyphFlags = styleFlags | 553648127;
                    glyphFlags &= Integer.MAX_VALUE;
                    this.mHasColorEmoji = true;
                }
                this.mGlyphFlags.add(glyphFlags);
            }
            this.mTotalAdvance += advance;
        } else {
            List<FontCollection.Run> items = this.mFontPaint.getFont().itemize(text, start, limit);
            int runIndex = isRtl ? items.size() - 1 : 0;
            while (isRtl ? runIndex >= 0 : runIndex < items.size()) {
                FontCollection.Run run = (FontCollection.Run) items.get(runIndex);
                Font font = run.getBestFont(text, fontStyle);
                int runStart = run.start();
                int runLimit = run.limit();
                float adv = font.doSimpleLayout(new char[] { '0' }, 0, 1, this.mFontPaint, null, null, 0.0F, 0.0F);
                if (adv > 0.0F) {
                    float offset = this.mTotalAdvance;
                    byte fontIdx = (Byte) this.mFontMap.computeIfAbsent(font, this.mNextID);
                    for (int i = runStart; i < runLimit; i++) {
                        if (this.mComputeAdvances) {
                            this.mAdvances.set(i, adv);
                        }
                        this.mGlyphs.add(0);
                        this.mPositions.add(offset);
                        this.mPositions.add(0.0F);
                        this.mFontIndices.add(fontIdx);
                        this.mGlyphFlags.add(styleFlags);
                        this.mHasEffect |= (styleFlags & 201326592) != 0;
                        offset += adv;
                        char c1 = text[i];
                        if (i + 1 < limit && Character.isHighSurrogate(c1)) {
                            char c2 = text[i + 1];
                            if (Character.isLowSurrogate(c2)) {
                                i++;
                            }
                        }
                    }
                    this.mTotalAdvance = offset;
                }
                if (isRtl) {
                    runIndex--;
                } else {
                    runIndex++;
                }
            }
        }
        if (this.mComputeLineBoundaries) {
            BreakIterator breaker = BreakIterator.getLineInstance(LineBreaker.getLocaleWithLineBreakOption(this.mFontPaint.getLocale(), sLbStyle, sLbWordStyle));
            CharArrayIterator charIterator = new CharArrayIterator(text, start, limit);
            breaker.setText(charIterator);
            int prevPos = start;
            int currPos;
            while ((currPos = breaker.following(prevPos)) != -1) {
                this.mLineBoundaries.add(currPos);
                prevPos = currPos;
            }
        }
    }
}