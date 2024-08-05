package net.minecraft.client;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.StringDecomposer;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;

public class StringSplitter {

    final StringSplitter.WidthProvider widthProvider;

    public StringSplitter(StringSplitter.WidthProvider stringSplitterWidthProvider0) {
        this.widthProvider = stringSplitterWidthProvider0;
    }

    public float stringWidth(@Nullable String string0) {
        if (string0 == null) {
            return 0.0F;
        } else {
            MutableFloat $$1 = new MutableFloat();
            StringDecomposer.iterateFormatted(string0, Style.EMPTY, (p_92429_, p_92430_, p_92431_) -> {
                $$1.add(this.widthProvider.getWidth(p_92431_, p_92430_));
                return true;
            });
            return $$1.floatValue();
        }
    }

    public float stringWidth(FormattedText formattedText0) {
        MutableFloat $$1 = new MutableFloat();
        StringDecomposer.iterateFormatted(formattedText0, Style.EMPTY, (p_92420_, p_92421_, p_92422_) -> {
            $$1.add(this.widthProvider.getWidth(p_92422_, p_92421_));
            return true;
        });
        return $$1.floatValue();
    }

    public float stringWidth(FormattedCharSequence formattedCharSequence0) {
        MutableFloat $$1 = new MutableFloat();
        formattedCharSequence0.accept((p_92400_, p_92401_, p_92402_) -> {
            $$1.add(this.widthProvider.getWidth(p_92402_, p_92401_));
            return true;
        });
        return $$1.floatValue();
    }

    public int plainIndexAtWidth(String string0, int int1, Style style2) {
        StringSplitter.WidthLimitedCharSink $$3 = new StringSplitter.WidthLimitedCharSink((float) int1);
        StringDecomposer.iterate(string0, style2, $$3);
        return $$3.getPosition();
    }

    public String plainHeadByWidth(String string0, int int1, Style style2) {
        return string0.substring(0, this.plainIndexAtWidth(string0, int1, style2));
    }

    public String plainTailByWidth(String string0, int int1, Style style2) {
        MutableFloat $$3 = new MutableFloat();
        MutableInt $$4 = new MutableInt(string0.length());
        StringDecomposer.iterateBackwards(string0, style2, (p_92407_, p_92408_, p_92409_) -> {
            float $$6 = $$3.addAndGet(this.widthProvider.getWidth(p_92409_, p_92408_));
            if ($$6 > (float) int1) {
                return false;
            } else {
                $$4.setValue(p_92407_);
                return true;
            }
        });
        return string0.substring($$4.intValue());
    }

    public int formattedIndexByWidth(String string0, int int1, Style style2) {
        StringSplitter.WidthLimitedCharSink $$3 = new StringSplitter.WidthLimitedCharSink((float) int1);
        StringDecomposer.iterateFormatted(string0, style2, $$3);
        return $$3.getPosition();
    }

    @Nullable
    public Style componentStyleAtWidth(FormattedText formattedText0, int int1) {
        StringSplitter.WidthLimitedCharSink $$2 = new StringSplitter.WidthLimitedCharSink((float) int1);
        return (Style) formattedText0.visit((p_92343_, p_92344_) -> StringDecomposer.iterateFormatted(p_92344_, p_92343_, $$2) ? Optional.empty() : Optional.of(p_92343_), Style.EMPTY).orElse(null);
    }

    @Nullable
    public Style componentStyleAtWidth(FormattedCharSequence formattedCharSequence0, int int1) {
        StringSplitter.WidthLimitedCharSink $$2 = new StringSplitter.WidthLimitedCharSink((float) int1);
        MutableObject<Style> $$3 = new MutableObject();
        formattedCharSequence0.accept((p_92348_, p_92349_, p_92350_) -> {
            if (!$$2.accept(p_92348_, p_92349_, p_92350_)) {
                $$3.setValue(p_92349_);
                return false;
            } else {
                return true;
            }
        });
        return (Style) $$3.getValue();
    }

    public String formattedHeadByWidth(String string0, int int1, Style style2) {
        return string0.substring(0, this.formattedIndexByWidth(string0, int1, style2));
    }

    public FormattedText headByWidth(FormattedText formattedText0, int int1, Style style2) {
        final StringSplitter.WidthLimitedCharSink $$3 = new StringSplitter.WidthLimitedCharSink((float) int1);
        return (FormattedText) formattedText0.visit(new FormattedText.StyledContentConsumer<FormattedText>() {

            private final ComponentCollector collector = new ComponentCollector();

            @Override
            public Optional<FormattedText> accept(Style p_92443_, String p_92444_) {
                $$3.resetPosition();
                if (!StringDecomposer.iterateFormatted(p_92444_, p_92443_, $$3)) {
                    String $$2 = p_92444_.substring(0, $$3.getPosition());
                    if (!$$2.isEmpty()) {
                        this.collector.append(FormattedText.of($$2, p_92443_));
                    }
                    return Optional.of(this.collector.getResultOrEmpty());
                } else {
                    if (!p_92444_.isEmpty()) {
                        this.collector.append(FormattedText.of(p_92444_, p_92443_));
                    }
                    return Optional.empty();
                }
            }
        }, style2).orElse(formattedText0);
    }

    public int findLineBreak(String string0, int int1, Style style2) {
        StringSplitter.LineBreakFinder $$3 = new StringSplitter.LineBreakFinder((float) int1);
        StringDecomposer.iterateFormatted(string0, style2, $$3);
        return $$3.getSplitPosition();
    }

    public static int getWordPosition(String string0, int int1, int int2, boolean boolean3) {
        int $$4 = int2;
        boolean $$5 = int1 < 0;
        int $$6 = Math.abs(int1);
        for (int $$7 = 0; $$7 < $$6; $$7++) {
            if ($$5) {
                while (boolean3 && $$4 > 0 && (string0.charAt($$4 - 1) == ' ' || string0.charAt($$4 - 1) == '\n')) {
                    $$4--;
                }
                while ($$4 > 0 && string0.charAt($$4 - 1) != ' ' && string0.charAt($$4 - 1) != '\n') {
                    $$4--;
                }
            } else {
                int $$8 = string0.length();
                int $$9 = string0.indexOf(32, $$4);
                int $$10 = string0.indexOf(10, $$4);
                if ($$9 == -1 && $$10 == -1) {
                    $$4 = -1;
                } else if ($$9 != -1 && $$10 != -1) {
                    $$4 = Math.min($$9, $$10);
                } else if ($$9 != -1) {
                    $$4 = $$9;
                } else {
                    $$4 = $$10;
                }
                if ($$4 == -1) {
                    $$4 = $$8;
                } else {
                    while (boolean3 && $$4 < $$8 && (string0.charAt($$4) == ' ' || string0.charAt($$4) == '\n')) {
                        $$4++;
                    }
                }
            }
        }
        return $$4;
    }

    public void splitLines(String string0, int int1, Style style2, boolean boolean3, StringSplitter.LinePosConsumer stringSplitterLinePosConsumer4) {
        int $$5 = 0;
        int $$6 = string0.length();
        Style $$7 = style2;
        while ($$5 < $$6) {
            StringSplitter.LineBreakFinder $$8 = new StringSplitter.LineBreakFinder((float) int1);
            boolean $$9 = StringDecomposer.iterateFormatted(string0, $$5, $$7, style2, $$8);
            if ($$9) {
                stringSplitterLinePosConsumer4.accept($$7, $$5, $$6);
                break;
            }
            int $$10 = $$8.getSplitPosition();
            char $$11 = string0.charAt($$10);
            int $$12 = $$11 != '\n' && $$11 != ' ' ? $$10 : $$10 + 1;
            stringSplitterLinePosConsumer4.accept($$7, $$5, boolean3 ? $$12 : $$10);
            $$5 = $$12;
            $$7 = $$8.getSplitStyle();
        }
    }

    public List<FormattedText> splitLines(String string0, int int1, Style style2) {
        List<FormattedText> $$3 = Lists.newArrayList();
        this.splitLines(string0, int1, style2, false, (p_92373_, p_92374_, p_92375_) -> $$3.add(FormattedText.of(string0.substring(p_92374_, p_92375_), p_92373_)));
        return $$3;
    }

    public List<FormattedText> splitLines(FormattedText formattedText0, int int1, Style style2) {
        List<FormattedText> $$3 = Lists.newArrayList();
        this.splitLines(formattedText0, int1, style2, (p_92378_, p_92379_) -> $$3.add(p_92378_));
        return $$3;
    }

    public List<FormattedText> splitLines(FormattedText formattedText0, int int1, Style style2, FormattedText formattedText3) {
        List<FormattedText> $$4 = Lists.newArrayList();
        this.splitLines(formattedText0, int1, style2, (p_168619_, p_168620_) -> $$4.add(p_168620_ ? FormattedText.composite(formattedText3, p_168619_) : p_168619_));
        return $$4;
    }

    public void splitLines(FormattedText formattedText0, int int1, Style style2, BiConsumer<FormattedText, Boolean> biConsumerFormattedTextBoolean3) {
        List<StringSplitter.LineComponent> $$4 = Lists.newArrayList();
        formattedText0.visit((p_92382_, p_92383_) -> {
            if (!p_92383_.isEmpty()) {
                $$4.add(new StringSplitter.LineComponent(p_92383_, p_92382_));
            }
            return Optional.empty();
        }, style2);
        StringSplitter.FlatComponents $$5 = new StringSplitter.FlatComponents($$4);
        boolean $$6 = true;
        boolean $$7 = false;
        boolean $$8 = false;
        while ($$6) {
            $$6 = false;
            StringSplitter.LineBreakFinder $$9 = new StringSplitter.LineBreakFinder((float) int1);
            for (StringSplitter.LineComponent $$10 : $$5.parts) {
                boolean $$11 = StringDecomposer.iterateFormatted($$10.contents, 0, $$10.style, style2, $$9);
                if (!$$11) {
                    int $$12 = $$9.getSplitPosition();
                    Style $$13 = $$9.getSplitStyle();
                    char $$14 = $$5.charAt($$12);
                    boolean $$15 = $$14 == '\n';
                    boolean $$16 = $$15 || $$14 == ' ';
                    $$7 = $$15;
                    FormattedText $$17 = $$5.splitAt($$12, $$16 ? 1 : 0, $$13);
                    biConsumerFormattedTextBoolean3.accept($$17, $$8);
                    $$8 = !$$15;
                    $$6 = true;
                    break;
                }
                $$9.addToOffset($$10.contents.length());
            }
        }
        FormattedText $$18 = $$5.getRemainder();
        if ($$18 != null) {
            biConsumerFormattedTextBoolean3.accept($$18, $$8);
        } else if ($$7) {
            biConsumerFormattedTextBoolean3.accept(FormattedText.EMPTY, false);
        }
    }

    static class FlatComponents {

        final List<StringSplitter.LineComponent> parts;

        private String flatParts;

        public FlatComponents(List<StringSplitter.LineComponent> listStringSplitterLineComponent0) {
            this.parts = listStringSplitterLineComponent0;
            this.flatParts = (String) listStringSplitterLineComponent0.stream().map(p_92459_ -> p_92459_.contents).collect(Collectors.joining());
        }

        public char charAt(int int0) {
            return this.flatParts.charAt(int0);
        }

        public FormattedText splitAt(int int0, int int1, Style style2) {
            ComponentCollector $$3 = new ComponentCollector();
            ListIterator<StringSplitter.LineComponent> $$4 = this.parts.listIterator();
            int $$5 = int0;
            boolean $$6 = false;
            while ($$4.hasNext()) {
                StringSplitter.LineComponent $$7 = (StringSplitter.LineComponent) $$4.next();
                String $$8 = $$7.contents;
                int $$9 = $$8.length();
                if (!$$6) {
                    if ($$5 > $$9) {
                        $$3.append($$7);
                        $$4.remove();
                        $$5 -= $$9;
                    } else {
                        String $$10 = $$8.substring(0, $$5);
                        if (!$$10.isEmpty()) {
                            $$3.append(FormattedText.of($$10, $$7.style));
                        }
                        $$5 += int1;
                        $$6 = true;
                    }
                }
                if ($$6) {
                    if ($$5 <= $$9) {
                        String $$11 = $$8.substring($$5);
                        if ($$11.isEmpty()) {
                            $$4.remove();
                        } else {
                            $$4.set(new StringSplitter.LineComponent($$11, style2));
                        }
                        break;
                    }
                    $$4.remove();
                    $$5 -= $$9;
                }
            }
            this.flatParts = this.flatParts.substring(int0 + int1);
            return $$3.getResultOrEmpty();
        }

        @Nullable
        public FormattedText getRemainder() {
            ComponentCollector $$0 = new ComponentCollector();
            this.parts.forEach($$0::m_90675_);
            this.parts.clear();
            return $$0.getResult();
        }
    }

    class LineBreakFinder implements FormattedCharSink {

        private final float maxWidth;

        private int lineBreak = -1;

        private Style lineBreakStyle = Style.EMPTY;

        private boolean hadNonZeroWidthChar;

        private float width;

        private int lastSpace = -1;

        private Style lastSpaceStyle = Style.EMPTY;

        private int nextChar;

        private int offset;

        public LineBreakFinder(float float0) {
            this.maxWidth = Math.max(float0, 1.0F);
        }

        @Override
        public boolean accept(int int0, Style style1, int int2) {
            int $$3 = int0 + this.offset;
            switch(int2) {
                case 10:
                    return this.finishIteration($$3, style1);
                case 32:
                    this.lastSpace = $$3;
                    this.lastSpaceStyle = style1;
                default:
                    float $$4 = StringSplitter.this.widthProvider.getWidth(int2, style1);
                    this.width += $$4;
                    if (!this.hadNonZeroWidthChar || !(this.width > this.maxWidth)) {
                        this.hadNonZeroWidthChar |= $$4 != 0.0F;
                        this.nextChar = $$3 + Character.charCount(int2);
                        return true;
                    } else {
                        return this.lastSpace != -1 ? this.finishIteration(this.lastSpace, this.lastSpaceStyle) : this.finishIteration($$3, style1);
                    }
            }
        }

        private boolean finishIteration(int int0, Style style1) {
            this.lineBreak = int0;
            this.lineBreakStyle = style1;
            return false;
        }

        private boolean lineBreakFound() {
            return this.lineBreak != -1;
        }

        public int getSplitPosition() {
            return this.lineBreakFound() ? this.lineBreak : this.nextChar;
        }

        public Style getSplitStyle() {
            return this.lineBreakStyle;
        }

        public void addToOffset(int int0) {
            this.offset += int0;
        }
    }

    static class LineComponent implements FormattedText {

        final String contents;

        final Style style;

        public LineComponent(String string0, Style style1) {
            this.contents = string0;
            this.style = style1;
        }

        @Override
        public <T> Optional<T> visit(FormattedText.ContentConsumer<T> formattedTextContentConsumerT0) {
            return formattedTextContentConsumerT0.accept(this.contents);
        }

        @Override
        public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> formattedTextStyledContentConsumerT0, Style style1) {
            return formattedTextStyledContentConsumerT0.accept(this.style.applyTo(style1), this.contents);
        }
    }

    @FunctionalInterface
    public interface LinePosConsumer {

        void accept(Style var1, int var2, int var3);
    }

    class WidthLimitedCharSink implements FormattedCharSink {

        private float maxWidth;

        private int position;

        public WidthLimitedCharSink(float float0) {
            this.maxWidth = float0;
        }

        @Override
        public boolean accept(int int0, Style style1, int int2) {
            this.maxWidth = this.maxWidth - StringSplitter.this.widthProvider.getWidth(int2, style1);
            if (this.maxWidth >= 0.0F) {
                this.position = int0 + Character.charCount(int2);
                return true;
            } else {
                return false;
            }
        }

        public int getPosition() {
            return this.position;
        }

        public void resetPosition() {
            this.position = 0;
        }
    }

    @FunctionalInterface
    public interface WidthProvider {

        float getWidth(int var1, Style var2);
    }
}