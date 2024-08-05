package icyllis.modernui.graphics;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;

public class PointF {

    public float x;

    public float y;

    public PointF() {
    }

    public PointF(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public PointF(@NonNull Point p) {
        this.x = (float) p.x;
        this.y = (float) p.y;
    }

    public PointF(@NonNull PointF p) {
        this.x = p.x;
        this.y = p.y;
    }

    @NonNull
    public static PointF copy(@Nullable PointF p) {
        return p == null ? new PointF() : p.copy();
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(@NonNull Point p) {
        this.x = (float) p.x;
        this.y = (float) p.y;
    }

    public void set(@NonNull PointF p) {
        this.x = p.x;
        this.y = p.y;
    }

    public void negate() {
        this.x = -this.x;
        this.y = -this.y;
    }

    public void offset(float dx, float dy) {
        this.x += dx;
        this.y += dy;
    }

    public float length() {
        return icyllis.arc3d.core.Point.length(this.x, this.y);
    }

    public boolean normalize() {
        double x = (double) this.x;
        double y = (double) this.y;
        double dmag = Math.sqrt(x * x + y * y);
        double dscale = 1.0 / dmag;
        float newX = (float) (x * dscale);
        float newY = (float) (y * dscale);
        if (icyllis.arc3d.core.Point.isDegenerate(newX, newY)) {
            return false;
        } else {
            this.x = newX;
            this.y = newY;
            return true;
        }
    }

    public void round(@NonNull Point dst) {
        dst.set(Math.round(this.x), Math.round(this.y));
    }

    public int hashCode() {
        int result = this.x != 0.0F ? Float.floatToIntBits(this.x) : 0;
        return 31 * result + (this.y != 0.0F ? Float.floatToIntBits(this.y) : 0);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return !(o instanceof PointF p) ? false : this.x == p.x && this.y == p.y;
        }
    }

    public String toString() {
        return "PointF(" + this.x + ", " + this.y + ")";
    }

    @NonNull
    public PointF copy() {
        return new PointF(this.x, this.y);
    }
}