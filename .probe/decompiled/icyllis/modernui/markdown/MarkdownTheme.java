package icyllis.modernui.markdown;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.core.Context;
import icyllis.modernui.text.Typeface;
import java.util.function.IntUnaryOperator;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class MarkdownTheme {

    private static final float[] HEADING_SIZES = new float[] { 2.0F, 1.5F, 1.17F, 1.0F, 0.83F, 0.67F };

    private final int mBlockQuoteMargin;

    private final int mBlockQuoteWidth;

    private final int mBlockQuoteColor;

    private final int mListItemMargin;

    private final int mListItemColor;

    private final int mCodeTextColor;

    private final int mCodeBlockTextColor;

    private final int mCodeBackgroundColor;

    private final int mCodeBlockBackgroundColor;

    private final int mCodeBlockMargin;

    private final Typeface mCodeTypeface;

    private final Typeface mCodeBlockTypeface;

    private final int mCodeTextSize;

    private final int mCodeBlockTextSize;

    private final int mHeadingBreakColor;

    private final Typeface mHeadingTypeface;

    private final float[] mHeadingTextSizeMultipliers;

    private final int mThematicBreakColor;

    @NonNull
    public static MarkdownTheme.Builder builder() {
        return new MarkdownTheme.Builder();
    }

    @NonNull
    public static MarkdownTheme.Builder builderWithDefaults(@NonNull Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        IntUnaryOperator dp = value -> (int) ((float) value * density + 0.5F);
        return new MarkdownTheme.Builder().setListItemMargin(dp.applyAsInt(24)).setBlockQuoteMargin(dp.applyAsInt(16)).setCodeBlockMargin(dp.applyAsInt(12));
    }

    private MarkdownTheme(@NonNull MarkdownTheme.Builder b) {
        this.mBlockQuoteMargin = b.mBlockQuoteMargin;
        this.mBlockQuoteWidth = b.mBlockQuoteWidth;
        this.mBlockQuoteColor = b.mBlockQuoteColor;
        this.mListItemMargin = b.mListItemMargin;
        this.mListItemColor = b.mListItemColor;
        this.mCodeTextColor = b.mCodeTextColor;
        this.mCodeBlockTextColor = b.mCodeBlockTextColor;
        this.mCodeBackgroundColor = b.mCodeBackgroundColor;
        this.mCodeBlockBackgroundColor = b.mCodeBlockBackgroundColor;
        this.mCodeBlockMargin = b.mCodeBlockMargin;
        this.mCodeTypeface = b.mCodeTypeface;
        this.mCodeBlockTypeface = b.mCodeBlockTypeface;
        this.mCodeTextSize = b.mCodeTextSize;
        this.mCodeBlockTextSize = b.mCodeBlockTextSize;
        this.mHeadingBreakColor = b.mHeadingBreakColor;
        this.mHeadingTypeface = b.mHeadingTypeface;
        this.mHeadingTextSizeMultipliers = b.mHeadingTextSizeMultipliers;
        this.mThematicBreakColor = b.mThematicBreakColor;
    }

    public int getBlockQuoteMargin() {
        return this.mBlockQuoteMargin;
    }

    public int getBlockQuoteWidth() {
        return this.mBlockQuoteWidth == 0 ? this.mBlockQuoteMargin >> 2 : this.mBlockQuoteWidth;
    }

    public int getBlockQuoteColor() {
        return this.mBlockQuoteColor != 0 ? this.mBlockQuoteColor : 822083583;
    }

    public int getListItemMargin() {
        return this.mListItemMargin;
    }

    public int getListItemColor() {
        return this.mListItemColor;
    }

    public int getCodeTextColor() {
        return this.mCodeTextColor;
    }

    public int getCodeBackgroundColor() {
        return this.mCodeBackgroundColor != 0 ? this.mCodeBackgroundColor : 1073741824;
    }

    @NonNull
    public Typeface getCodeTypeface() {
        return this.mCodeTypeface != null ? this.mCodeTypeface : Typeface.MONOSPACED;
    }

    public int getCodeTextSize() {
        return this.mCodeTextSize;
    }

    public int getCodeBlockTextColor() {
        return this.mCodeBlockTextColor;
    }

    public int getCodeBlockBackgroundColor() {
        return this.mCodeBlockBackgroundColor != 0 ? this.mCodeBlockBackgroundColor : this.getCodeBackgroundColor();
    }

    @NonNull
    public Typeface getCodeBlockTypeface() {
        return this.mCodeBlockTypeface != null ? this.mCodeBlockTypeface : this.getCodeTypeface();
    }

    public int getCodeBlockTextSize() {
        return this.mCodeBlockTextSize;
    }

    public int getCodeBlockMargin() {
        return this.mCodeBlockMargin;
    }

    public int getHeadingBreakColor() {
        return this.mHeadingBreakColor != 0 ? this.mHeadingBreakColor : 1358954495;
    }

    public Typeface getHeadingTypeface() {
        return this.mHeadingTypeface;
    }

    public float getHeadingTextSizeMultiplier(int level) {
        return this.mHeadingTextSizeMultipliers != null ? this.mHeadingTextSizeMultipliers[level - 1] : HEADING_SIZES[level - 1];
    }

    public int getThematicBreakColor() {
        return this.mThematicBreakColor != 0 ? this.mThematicBreakColor : 822083583;
    }

    public static final class Builder {

        private int mBlockQuoteMargin;

        private int mBlockQuoteWidth;

        private int mBlockQuoteColor;

        private int mListItemMargin;

        private int mListItemColor;

        private int mCodeTextColor;

        private int mCodeBlockTextColor;

        private int mCodeBackgroundColor;

        private int mCodeBlockBackgroundColor;

        private int mCodeBlockMargin;

        private Typeface mCodeTypeface;

        private Typeface mCodeBlockTypeface;

        private int mCodeTextSize;

        private int mCodeBlockTextSize;

        private int mHeadingBreakColor;

        private Typeface mHeadingTypeface;

        private float[] mHeadingTextSizeMultipliers;

        private int mThematicBreakColor;

        Builder() {
        }

        @NonNull
        public MarkdownTheme.Builder setBlockQuoteMargin(int blockQuoteMargin) {
            this.mBlockQuoteMargin = blockQuoteMargin;
            return this;
        }

        @NonNull
        public MarkdownTheme.Builder setBlockQuoteWidth(int blockQuoteWidth) {
            this.mBlockQuoteWidth = blockQuoteWidth;
            return this;
        }

        @NonNull
        public MarkdownTheme.Builder setBlockQuoteColor(int blockQuoteColor) {
            this.mBlockQuoteColor = blockQuoteColor;
            return this;
        }

        @NonNull
        public MarkdownTheme.Builder setListItemMargin(int listItemMargin) {
            this.mListItemMargin = listItemMargin;
            return this;
        }

        @NonNull
        public MarkdownTheme.Builder setListItemColor(int listItemColor) {
            this.mListItemColor = listItemColor;
            return this;
        }

        @NonNull
        public MarkdownTheme.Builder setCodeTextColor(int codeTextColor) {
            this.mCodeTextColor = codeTextColor;
            return this;
        }

        @NonNull
        public MarkdownTheme.Builder setCodeBlockTextColor(int codeBlockTextColor) {
            this.mCodeBlockTextColor = codeBlockTextColor;
            return this;
        }

        @NonNull
        public MarkdownTheme.Builder setCodeBackgroundColor(int codeBackgroundColor) {
            this.mCodeBackgroundColor = codeBackgroundColor;
            return this;
        }

        @NonNull
        public MarkdownTheme.Builder setCodeBlockBackgroundColor(int codeBlockBackgroundColor) {
            this.mCodeBlockBackgroundColor = codeBlockBackgroundColor;
            return this;
        }

        @NonNull
        public MarkdownTheme.Builder setCodeBlockMargin(int codeBlockMargin) {
            this.mCodeBlockMargin = codeBlockMargin;
            return this;
        }

        @NonNull
        public MarkdownTheme.Builder setCodeTypeface(Typeface codeTypeface) {
            this.mCodeTypeface = codeTypeface;
            return this;
        }

        @NonNull
        public MarkdownTheme.Builder setCodeBlockTypeface(Typeface codeBlockTypeface) {
            this.mCodeBlockTypeface = codeBlockTypeface;
            return this;
        }

        @NonNull
        public MarkdownTheme.Builder setCodeTextSize(int codeTextSize) {
            this.mCodeTextSize = codeTextSize;
            return this;
        }

        @NonNull
        public MarkdownTheme.Builder setCodeBlockTextSize(int codeBlockTextSize) {
            this.mCodeBlockTextSize = codeBlockTextSize;
            return this;
        }

        @NonNull
        public MarkdownTheme.Builder setHeadingBreakColor(int headingBreakColor) {
            this.mHeadingBreakColor = headingBreakColor;
            return this;
        }

        @NonNull
        public MarkdownTheme.Builder setHeadingTypeface(Typeface headingTypeface) {
            this.mHeadingTypeface = headingTypeface;
            return this;
        }

        @NonNull
        public MarkdownTheme.Builder setHeadingTextSizeMultipliers(float[] headingTextSizeMultipliers) {
            this.mHeadingTextSizeMultipliers = headingTextSizeMultipliers;
            return this;
        }

        @NonNull
        public MarkdownTheme.Builder setThematicBreakColor(int thematicBreakColor) {
            this.mThematicBreakColor = thematicBreakColor;
            return this;
        }

        @NonNull
        public MarkdownTheme build() {
            return new MarkdownTheme(this);
        }
    }
}