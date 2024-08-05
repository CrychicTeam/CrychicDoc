package icyllis.modernui.mc.text;

import icyllis.modernui.graphics.text.LineBreaker;
import icyllis.modernui.mc.MuiModApi;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.Optional;
import java.util.function.BiConsumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.ComponentCollector;
import net.minecraft.client.StringSplitter;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.Unit;
import org.apache.commons.lang3.mutable.MutableObject;

public final class ModernStringSplitter extends StringSplitter {

    private final TextLayoutEngine mEngine;

    private static final int NOWHERE = -1;

    public ModernStringSplitter(TextLayoutEngine engine, StringSplitter.WidthProvider widthProvider) {
        super(widthProvider);
        this.mEngine = engine;
    }

    @Override
    public float stringWidth(@Nullable String text) {
        return this.measureText(text);
    }

    @Override
    public float stringWidth(@Nonnull FormattedText text) {
        return this.measureText(text);
    }

    @Override
    public float stringWidth(@Nonnull FormattedCharSequence text) {
        return this.measureText(text);
    }

    public float measureText(@Nullable String text) {
        return text == null ? 0.0F : this.mEngine.lookupVanillaLayout(text).getTotalAdvance();
    }

    public float measureText(@Nonnull FormattedText text) {
        return this.mEngine.lookupFormattedLayout(text).getTotalAdvance();
    }

    public float measureText(@Nonnull FormattedCharSequence text) {
        return this.mEngine.lookupFormattedLayout(text).getTotalAdvance();
    }

    @Override
    public int plainIndexAtWidth(@Nonnull String text, int width, @Nonnull Style style) {
        return this.indexByWidth(text, (float) width, style);
    }

    @Nonnull
    @Override
    public String plainHeadByWidth(@Nonnull String text, int width, @Nonnull Style style) {
        return this.headByWidth(text, (float) width, style);
    }

    @Nonnull
    @Override
    public String plainTailByWidth(@Nonnull String text, int width, @Nonnull Style style) {
        return this.tailByWidth(text, (float) width, style);
    }

    @Override
    public int formattedIndexByWidth(@Nonnull String text, int width, @Nonnull Style style) {
        return this.indexByWidth(text, (float) width, style);
    }

    @Nullable
    @Override
    public Style componentStyleAtWidth(@Nonnull FormattedText text, int width) {
        return this.styleAtWidth(text, (float) width);
    }

    @Nullable
    @Override
    public Style componentStyleAtWidth(@Nonnull FormattedCharSequence text, int width) {
        return this.styleAtWidth(text, (float) width);
    }

    @Nonnull
    @Override
    public String formattedHeadByWidth(@Nonnull String text, int width, @Nonnull Style style) {
        return this.headByWidth(text, (float) width, style);
    }

    @Nonnull
    @Override
    public FormattedText headByWidth(@Nonnull FormattedText text, int width, @Nonnull Style style) {
        return this.headByWidth(text, (float) width, style);
    }

    public static int breakText(@Nonnull TextLayout layout, boolean forwards, float width) {
        int limit = layout.getCharCount();
        if (forwards) {
            int i;
            for (i = 0; i < limit; i++) {
                width -= layout.getAdvances()[i];
                if (width < 0.0F) {
                    break;
                }
            }
            while (i > 0 && layout.getTextBuf()[i - 1] == ' ') {
                i--;
            }
            return i;
        } else {
            int i;
            for (i = limit - 1; i >= 0; i--) {
                width -= layout.getAdvances()[i];
                if (width < 0.0F) {
                    break;
                }
            }
            while (i < limit - 1 && (layout.getTextBuf()[i + 1] == ' ' || layout.getAdvances()[i + 1] == 0.0F)) {
                i++;
            }
            return i + 1;
        }
    }

    public int breakText(@Nonnull String text, float width, @Nonnull Style style, boolean forwards) {
        if (!text.isEmpty() && !(width < 0.0F)) {
            TextLayout layout = this.mEngine.lookupVanillaLayout(text, style, 1);
            if (width >= layout.getTotalAdvance()) {
                return forwards ? text.length() : 0;
            } else {
                int breakIndex = breakText(layout, forwards, width);
                int length = text.length();
                for (int j = 0; j < length && j != breakIndex; j++) {
                    if (text.charAt(j) == 167) {
                        j++;
                        breakIndex += 2;
                    }
                }
                return breakIndex;
            }
        } else {
            return 0;
        }
    }

    public int indexByWidth(@Nonnull String text, float width, @Nonnull Style style) {
        return this.breakText(text, width, style, true);
    }

    @Nonnull
    public String headByWidth(@Nonnull String text, float width, @Nonnull Style style) {
        return text.substring(0, this.indexByWidth(text, width, style));
    }

    @Nonnull
    public String tailByWidth(@Nonnull String text, float width, @Nonnull Style style) {
        return text.substring(this.breakText(text, width, style, false));
    }

    @Nullable
    public Style styleAtWidth(@Nonnull FormattedText text, float width) {
        if (text != CommonComponents.EMPTY && text != FormattedText.EMPTY && !(width < 0.0F)) {
            TextLayout layout = this.mEngine.lookupFormattedLayout(text, Style.EMPTY, 1);
            if (width >= layout.getTotalAdvance()) {
                return null;
            } else {
                final int breakIndex = breakText(layout, true, width);
                return (Style) text.visit(new FormattedText.StyledContentConsumer<Style>() {

                    private int mStripIndex;

                    @Nonnull
                    @Override
                    public Optional<Style> accept(@Nonnull Style style, @Nonnull String string) {
                        int length = string.length();
                        for (int i = 0; i < length; i++) {
                            if (string.charAt(i) == 167) {
                                i++;
                            } else if (++this.mStripIndex > breakIndex) {
                                return Optional.of(style);
                            }
                        }
                        return Optional.empty();
                    }
                }, Style.EMPTY).orElse(null);
            }
        } else {
            return null;
        }
    }

    @Nullable
    public Style styleAtWidth(@Nonnull FormattedCharSequence text, float width) {
        if (text == FormattedCharSequence.EMPTY || width < 0.0F) {
            return null;
        } else if (text instanceof FormattedTextWrapper) {
            return this.styleAtWidth(((FormattedTextWrapper) text).mText, width);
        } else {
            TextLayout layout = this.mEngine.lookupFormattedLayout(text, 1);
            if (width >= layout.getTotalAdvance()) {
                return null;
            } else {
                final int breakIndex = breakText(layout, true, width);
                final MutableObject<Style> result = new MutableObject();
                text.accept(new FormattedCharSink() {

                    private int mStripIndex;

                    @Override
                    public boolean accept(int index, @Nonnull Style style, int codePoint) {
                        if ((this.mStripIndex = this.mStripIndex + Character.charCount(codePoint)) > breakIndex) {
                            result.setValue(style);
                            return false;
                        } else {
                            return true;
                        }
                    }
                });
                return (Style) result.getValue();
            }
        }
    }

    @Nonnull
    public FormattedText headByWidth(@Nonnull FormattedText text, float width, @Nonnull Style style) {
        if (text != CommonComponents.EMPTY && text != FormattedText.EMPTY && !(width < 0.0F)) {
            TextLayout layout = this.mEngine.lookupFormattedLayout(text, style, 1);
            if (width >= layout.getTotalAdvance()) {
                return text;
            } else {
                final int breakIndex = breakText(layout, true, width);
                return (FormattedText) text.visit(new FormattedText.StyledContentConsumer<FormattedText>() {

                    private final ComponentCollector mCollector = new ComponentCollector();

                    private int mSegmentIndex;

                    @Nonnull
                    @Override
                    public Optional<FormattedText> accept(@Nonnull Style sty, @Nonnull String string) {
                        int length = string.length();
                        int stripIndex = 0;
                        for (int i = 0; i < length; i++) {
                            if (string.charAt(i) == 167) {
                                i++;
                            } else if (this.mSegmentIndex + ++stripIndex > breakIndex) {
                                String substring = string.substring(0, stripIndex);
                                if (!substring.isEmpty()) {
                                    this.mCollector.append(FormattedText.of(substring, sty));
                                }
                                return Optional.of(this.mCollector.getResultOrEmpty());
                            }
                        }
                        if (length > 0) {
                            this.mCollector.append(FormattedText.of(string, sty));
                        }
                        this.mSegmentIndex += stripIndex;
                        return Optional.empty();
                    }
                }, style).orElse(text);
            }
        } else {
            return FormattedText.EMPTY;
        }
    }

    @Override
    public void splitLines(@Nonnull String text, int width, @Nonnull Style style, @Deprecated boolean withEndSpace, @Nonnull StringSplitter.LinePosConsumer linePos) {
        this.computeLineBreaks(text, (float) width, style, linePos);
    }

    @Override
    public void splitLines(@Nonnull FormattedText text, int width, @Nonnull Style style, @Nonnull BiConsumer<FormattedText, Boolean> consumer) {
        this.computeLineBreaks(text, (float) width, style, consumer);
    }

    public void computeLineBreaks(@Nonnull String text, float width, @Nonnull Style base, @Nonnull StringSplitter.LinePosConsumer consumer) {
        if (!text.isEmpty() && !(width < 0.0F)) {
            TextLayout layout = this.mEngine.lookupVanillaLayout(text, base, 5);
            char[] buf = layout.getTextBuf();
            if (width >= layout.getTotalAdvance()) {
                boolean hasLineFeed = false;
                int i = 0;
                for (int e = layout.getCharCount(); i < e; i++) {
                    if (buf[i] == '\n') {
                        hasLineFeed = true;
                        break;
                    }
                }
                if (!hasLineFeed) {
                    consumer.accept(base, 0, text.length());
                    return;
                }
            }
            ModernStringSplitter.LineProcessor lineBreaker = new ModernStringSplitter.LineProcessor(width);
            int end = layout.getCharCount();
            int nextBoundaryIndex = 0;
            int paraStart = 0;
            while (paraStart < end) {
                int paraEnd = -1;
                for (int i = paraStart; i < end; i++) {
                    if (buf[i] == '\n') {
                        paraEnd = i;
                        break;
                    }
                }
                if (paraEnd < 0) {
                    paraEnd = end;
                } else {
                    paraEnd++;
                }
                nextBoundaryIndex = lineBreaker.process(layout, buf, paraStart, paraEnd, nextBoundaryIndex);
                paraStart = paraEnd;
            }
            IntList result = lineBreaker.mBreakPoints;
            int mStripIndex = 0;
            int mBreakOffsetIndex = 0;
            int mBreakPointOffset = result.getInt(mBreakOffsetIndex++);
            Style currStyle = base;
            Style lastStyle = base;
            int lastSubPos = 0;
            int ix = 0;
            for (int ex = text.length(); ix < ex; ix++) {
                char c = text.charAt(ix);
                if (c == 167) {
                    if (++ix < ex) {
                        ChatFormatting formatting = MuiModApi.getFormattingByCode(text.charAt(ix));
                        if (formatting != null) {
                            currStyle = formatting == ChatFormatting.RESET ? base : currStyle.applyLegacyFormat(formatting);
                        }
                    }
                } else if (++mStripIndex >= mBreakPointOffset) {
                    consumer.accept(lastStyle, lastSubPos, ix + 1);
                    lastSubPos = ix + 1;
                    lastStyle = currStyle;
                    if (mBreakOffsetIndex >= result.size()) {
                        break;
                    }
                    mBreakPointOffset = result.getInt(mBreakOffsetIndex++);
                }
            }
        }
    }

    public void computeLineBreaks(@Nonnull FormattedText text, float width, @Nonnull Style base, @Nonnull BiConsumer<FormattedText, Boolean> consumer) {
        if (text != CommonComponents.EMPTY && text != FormattedText.EMPTY && !(width < 0.0F)) {
            TextLayout layout = this.mEngine.lookupFormattedLayout(text, base, 5);
            char[] buf = layout.getTextBuf();
            if (width >= layout.getTotalAdvance()) {
                boolean hasLineFeed = false;
                int i = 0;
                for (int e = layout.getCharCount(); i < e; i++) {
                    if (buf[i] == '\n') {
                        hasLineFeed = true;
                        break;
                    }
                }
                if (!hasLineFeed) {
                    consumer.accept(text, Boolean.FALSE);
                    return;
                }
            }
            ModernStringSplitter.LineProcessor lineBreaker = new ModernStringSplitter.LineProcessor(width);
            int end = layout.getCharCount();
            int nextBoundaryIndex = 0;
            int paraStart = 0;
            while (paraStart < end) {
                int paraEnd = -1;
                for (int i = paraStart; i < end; i++) {
                    if (buf[i] == '\n') {
                        paraEnd = i;
                        break;
                    }
                }
                if (paraEnd < 0) {
                    paraEnd = end;
                } else {
                    paraEnd++;
                }
                nextBoundaryIndex = lineBreaker.process(layout, buf, paraStart, paraEnd, nextBoundaryIndex);
                paraStart = paraEnd;
            }
            final IntList result = lineBreaker.mBreakPoints;
            class LineBreakVisitor implements FormattedText.StyledContentConsumer<Unit> {

                private ComponentCollector mCollector = new ComponentCollector();

                private int mStripIndex = 0;

                private int mBreakOffsetIndex = 0;

                private int mBreakPointOffset = result.getInt(ModernStringSplitter.super.mBreakOffsetIndex++);

                private boolean mNonNewPara = false;

                @Nonnull
                @Override
                public Optional<Unit> accept(@Nonnull Style aStyle, @Nonnull String aText) {
                    Style currStyle = aStyle;
                    Style lastStyle = aStyle;
                    int lastSubPos = 0;
                    int i = 0;
                    for (int e = aText.length(); i < e; i++) {
                        char c = aText.charAt(i);
                        if (c == 167) {
                            if (++i < e) {
                                ChatFormatting formatting = MuiModApi.getFormattingByCode(aText.charAt(i));
                                if (formatting != null) {
                                    currStyle = formatting == ChatFormatting.RESET ? aStyle : currStyle.applyLegacyFormat(formatting);
                                }
                            }
                        } else if (++this.mStripIndex >= this.mBreakPointOffset) {
                            String substring = aText.substring(lastSubPos, i + 1);
                            if (!substring.isEmpty()) {
                                this.mCollector.append(FormattedText.of(substring, lastStyle));
                            }
                            consumer.accept(this.mCollector.getResultOrEmpty(), this.mNonNewPara);
                            if (this.mBreakOffsetIndex >= result.size()) {
                                return FormattedText.STOP_ITERATION;
                            }
                            lastSubPos = i + 1;
                            lastStyle = currStyle;
                            this.mCollector = new ComponentCollector();
                            this.mBreakPointOffset = result.getInt(this.mBreakOffsetIndex++);
                            this.mNonNewPara = c != '\n';
                        }
                    }
                    String substringx = aText.substring(lastSubPos);
                    if (!substringx.isEmpty()) {
                        this.mCollector.append(FormattedText.of(substringx, lastStyle));
                    }
                    return Optional.empty();
                }
            }
            text.visit(new LineBreakVisitor(), base);
        }
    }

    public static record LineComponent(String text, Style style) implements FormattedText {

        @Nonnull
        @Override
        public <T> Optional<T> visit(@Nonnull FormattedText.ContentConsumer<T> consumer) {
            return consumer.accept(this.text);
        }

        @Nonnull
        @Override
        public <T> Optional<T> visit(@Nonnull FormattedText.StyledContentConsumer<T> consumer, @Nonnull Style base) {
            return consumer.accept(this.style.applyTo(base), this.text);
        }
    }

    public static class LineProcessor {

        private float mLineWidth;

        private float mCharsAdvance;

        private final float mLineWidthLimit;

        private int mPrevBoundaryOffset;

        private float mCharsAdvanceAtPrevBoundary;

        private final IntList mBreakPoints = new IntArrayList();

        public LineProcessor(float lineWidthLimit) {
            this.mLineWidthLimit = lineWidthLimit;
        }

        public int process(@Nonnull TextLayout layout, @Nonnull char[] buf, int start, int end, int nextBoundaryIndex) {
            this.mLineWidth = 0.0F;
            this.mCharsAdvance = 0.0F;
            this.mPrevBoundaryOffset = -1;
            this.mCharsAdvanceAtPrevBoundary = 0.0F;
            float[] advances = layout.getAdvances();
            int[] lineBoundaries = layout.getLineBoundaries();
            int nextLineBoundary = lineBoundaries[nextBoundaryIndex++];
            for (int i = start; i < end; i++) {
                this.updateLineWidth(buf[i], advances[i]);
                if (i + 1 == nextLineBoundary) {
                    this.processLineBreak(advances, i + 1);
                    if (nextLineBoundary < end) {
                        nextLineBoundary = lineBoundaries[nextBoundaryIndex++];
                    }
                    if (nextLineBoundary > end) {
                        nextLineBoundary = end;
                    }
                }
            }
            if (this.getPrevLineBreakOffset() != end && this.mPrevBoundaryOffset != -1) {
                this.breakLineAt(this.mPrevBoundaryOffset, 0.0F, 0.0F);
            }
            return nextBoundaryIndex;
        }

        private void processLineBreak(float[] advances, int offset) {
            while (this.mLineWidth > this.mLineWidthLimit) {
                int start = this.getPrevLineBreakOffset();
                if (!this.tryLineBreak() && this.doLineBreakWithGraphemeBounds(advances, start, offset)) {
                    return;
                }
            }
            this.mPrevBoundaryOffset = offset;
            this.mCharsAdvanceAtPrevBoundary = this.mCharsAdvance;
        }

        private boolean tryLineBreak() {
            if (this.mPrevBoundaryOffset == -1) {
                return false;
            } else {
                this.breakLineAt(this.mPrevBoundaryOffset, this.mLineWidth - this.mCharsAdvanceAtPrevBoundary, this.mCharsAdvance - this.mCharsAdvanceAtPrevBoundary);
                return true;
            }
        }

        private boolean doLineBreakWithGraphemeBounds(float[] advances, int start, int end) {
            float width = advances[start];
            for (int i = start + 1; i < end; i++) {
                float w = advances[i];
                if (w != 0.0F) {
                    if (width + w > this.mLineWidthLimit) {
                        this.breakLineAt(i, this.mLineWidth - width, this.mCharsAdvance - width);
                        return false;
                    }
                    width += w;
                }
            }
            this.breakLineAt(end, 0.0F, 0.0F);
            return true;
        }

        private void breakLineAt(int offset, float remainingNextLineWidth, float remainingNextCharsAdvance) {
            this.mBreakPoints.add(offset);
            this.mLineWidth = remainingNextLineWidth;
            this.mCharsAdvance = remainingNextCharsAdvance;
            this.mPrevBoundaryOffset = -1;
            this.mCharsAdvanceAtPrevBoundary = 0.0F;
        }

        private void updateLineWidth(char c, float adv) {
            this.mCharsAdvance += adv;
            if (!LineBreaker.isLineEndSpace(c)) {
                this.mLineWidth = this.mCharsAdvance;
            }
        }

        private int getPrevLineBreakOffset() {
            return this.mBreakPoints.isEmpty() ? 0 : this.mBreakPoints.getInt(this.mBreakPoints.size() - 1);
        }
    }
}