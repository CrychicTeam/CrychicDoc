package icyllis.modernui.graphics;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;

public class Point {

    public int x;

    public int y;

    public Point() {
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(@NonNull Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    @NonNull
    public static Point copy(@Nullable Point p) {
        return p == null ? new Point() : p.copy();
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(@NonNull Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public void negate() {
        this.x = -this.x;
        this.y = -this.y;
    }

    public void offset(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public int hashCode() {
        int result = this.x;
        return 31 * result + this.y;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return !(o instanceof Point p) ? false : this.x == p.x && this.y == p.y;
        }
    }

    public String toString() {
        return "Point(" + this.x + ", " + this.y + ")";
    }

    @NonNull
    public Point copy() {
        return new Point(this.x, this.y);
    }
}