package icyllis.modernui.graphics.text;

import icyllis.modernui.annotation.NonNull;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class LineBreakConfig {

    public static final int LINE_BREAK_STYLE_NONE = 0;

    public static final int LINE_BREAK_STYLE_LOOSE = 1;

    public static final int LINE_BREAK_STYLE_NORMAL = 2;

    public static final int LINE_BREAK_STYLE_STRICT = 3;

    public static final int LINE_BREAK_WORD_STYLE_NONE = 0;

    public static final int LINE_BREAK_WORD_STYLE_PHRASE = 1;

    public static final int LINE_BREAK_WORD_STYLE_NORMAL = 2;

    public static final int LINE_BREAK_WORD_STYLE_BREAK_ALL = 3;

    public static final int LINE_BREAK_WORD_STYLE_KEEP_ALL = 4;

    @Internal
    public static final LineBreakConfig NONE = new LineBreakConfig(0, 0);

    private final int mLineBreakStyle;

    private final int mLineBreakWordStyle;

    @Internal
    @NonNull
    public static LineBreakConfig getLineBreakConfig(int lineBreakStyle, int lineBreakWordStyle) {
        return lineBreakStyle == 0 && lineBreakWordStyle == 0 ? NONE : new LineBreakConfig(lineBreakStyle, lineBreakWordStyle);
    }

    private LineBreakConfig(int lineBreakStyle, int lineBreakWordStyle) {
        this.mLineBreakStyle = lineBreakStyle;
        this.mLineBreakWordStyle = lineBreakWordStyle;
    }

    public int getLineBreakStyle() {
        return this.mLineBreakStyle;
    }

    public int getLineBreakWordStyle() {
        return this.mLineBreakWordStyle;
    }

    public int hashCode() {
        int result = this.mLineBreakStyle;
        return 31 * result + this.mLineBreakWordStyle;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return !(o instanceof LineBreakConfig that) ? false : this.mLineBreakStyle == that.mLineBreakStyle && this.mLineBreakWordStyle == that.mLineBreakWordStyle;
        }
    }

    public static final class Builder {

        private int mLineBreakStyle = 0;

        private int mLineBreakWordStyle = 0;

        @NonNull
        public LineBreakConfig.Builder setLineBreakStyle(int lineBreakStyle) {
            this.mLineBreakStyle = lineBreakStyle;
            return this;
        }

        @NonNull
        public LineBreakConfig.Builder setLineBreakWordStyle(int lineBreakWordStyle) {
            this.mLineBreakWordStyle = lineBreakWordStyle;
            return this;
        }

        @NonNull
        public LineBreakConfig build() {
            return new LineBreakConfig(this.mLineBreakStyle, this.mLineBreakWordStyle);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @Internal
    public @interface LineBreakStyle {
    }

    @Retention(RetentionPolicy.SOURCE)
    @Internal
    public @interface LineBreakWordStyle {
    }
}