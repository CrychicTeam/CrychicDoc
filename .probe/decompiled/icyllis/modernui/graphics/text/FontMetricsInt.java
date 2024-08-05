package icyllis.modernui.graphics.text;

import javax.annotation.Nonnull;

public class FontMetricsInt {

    public int ascent;

    public int descent;

    public int leading;

    public void reset() {
        this.ascent = this.descent = this.leading = 0;
    }

    public void extendBy(@Nonnull FontMetricsInt fm) {
        this.extendBy(fm.ascent, fm.descent, fm.leading);
    }

    public void extendBy(int ascent, int descent) {
        this.ascent = Math.min(this.ascent, ascent);
        this.descent = Math.max(this.descent, descent);
    }

    public void extendBy(int ascent, int descent, int leading) {
        this.extendBy(ascent, descent);
        this.leading = Math.max(this.leading, leading);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            FontMetricsInt that = (FontMetricsInt) o;
            if (this.ascent != that.ascent) {
                return false;
            } else {
                return this.descent != that.descent ? false : this.leading == that.leading;
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.ascent;
        result = 31 * result + this.descent;
        return 31 * result + this.leading;
    }

    public String toString() {
        return "FontMetricsInt{ascent=" + this.ascent + ", descent=" + this.descent + ", leading=" + this.leading + "}";
    }
}