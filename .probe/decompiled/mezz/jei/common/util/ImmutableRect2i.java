package mezz.jei.common.util;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import javax.annotation.Nonnegative;
import net.minecraft.client.renderer.Rect2i;

public class ImmutableRect2i {

    public static final ImmutableRect2i EMPTY = new ImmutableRect2i(0, 0, 0, 0);

    private final int x;

    private final int y;

    @Nonnegative
    private final int width;

    @Nonnegative
    private final int height;

    public ImmutableRect2i(Rect2i rect) {
        this(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public ImmutableRect2i(int x, int y, int width, int height) {
        Preconditions.checkArgument(width >= 0, "width must be >= 0");
        Preconditions.checkArgument(height >= 0, "height must be >= 0");
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean isEmpty() {
        return this.width == 0 || this.height == 0;
    }

    public boolean contains(double x, double y) {
        return x >= (double) this.x && y >= (double) this.y && x < (double) (this.x + this.width) && y < (double) (this.y + this.height);
    }

    public boolean intersects(ImmutableRect2i rect) {
        return !this.isEmpty() && !rect.isEmpty() ? rect.getX() + rect.getWidth() > this.x && rect.getY() + rect.getHeight() > this.y && rect.getX() < this.x + this.width && rect.getY() < this.y + this.height : false;
    }

    public ImmutableRect2i moveRight(int x) {
        return x == 0 ? this : new ImmutableRect2i(this.x + x, this.y, this.width, this.height);
    }

    public ImmutableRect2i moveLeft(int x) {
        return x == 0 ? this : new ImmutableRect2i(this.x - x, this.y, this.width, this.height);
    }

    public ImmutableRect2i moveDown(int y) {
        return y == 0 ? this : new ImmutableRect2i(this.x, this.y + y, this.width, this.height);
    }

    public ImmutableRect2i moveUp(int y) {
        return y == 0 ? this : new ImmutableRect2i(this.x, this.y - y, this.width, this.height);
    }

    public ImmutableRect2i insetBy(int amount) {
        if (amount == 0) {
            return this;
        } else {
            amount = Math.min(amount, this.width / 2);
            amount = Math.min(amount, this.height / 2);
            return new ImmutableRect2i(this.x + amount, this.y + amount, this.width - amount * 2, this.height - amount * 2);
        }
    }

    public ImmutableRect2i expandBy(int amount) {
        return amount == 0 ? this : new ImmutableRect2i(this.x - amount, this.y - amount, this.width + amount * 2, this.height + amount * 2);
    }

    public ImmutableRect2i cropRight(int amount) {
        if (amount == 0) {
            return this;
        } else {
            if (amount > this.width) {
                amount = this.width;
            }
            return new ImmutableRect2i(this.x, this.y, this.width - amount, this.height);
        }
    }

    public ImmutableRect2i cropLeft(int amount) {
        if (amount == 0) {
            return this;
        } else {
            if (amount > this.width) {
                amount = this.width;
            }
            return new ImmutableRect2i(this.x + amount, this.y, this.width - amount, this.height);
        }
    }

    public ImmutableRect2i cropBottom(int amount) {
        if (amount == 0) {
            return this;
        } else {
            if (amount > this.height) {
                amount = this.height;
            }
            return new ImmutableRect2i(this.x, this.y, this.width, this.height - amount);
        }
    }

    public ImmutableRect2i cropTop(int amount) {
        if (amount == 0) {
            return this;
        } else {
            if (amount > this.height) {
                amount = this.height;
            }
            return new ImmutableRect2i(this.x, this.y + amount, this.width, this.height - amount);
        }
    }

    public ImmutableRect2i keepTop(int amount) {
        if (amount == this.height) {
            return this;
        } else {
            return amount > this.height ? this : new ImmutableRect2i(this.x, this.y, this.width, amount);
        }
    }

    public ImmutableRect2i keepBottom(int amount) {
        if (amount == this.height) {
            return this;
        } else if (amount > this.height) {
            return this;
        } else {
            int cropAmount = this.height - amount;
            return new ImmutableRect2i(this.x, this.y + cropAmount, this.width, amount);
        }
    }

    public ImmutableRect2i keepRight(int amount) {
        if (amount == this.width) {
            return this;
        } else if (amount > this.width) {
            return this;
        } else {
            int cropAmount = this.width - amount;
            return new ImmutableRect2i(this.x + cropAmount, this.y, amount, this.height);
        }
    }

    public ImmutableRect2i keepLeft(int amount) {
        if (amount == this.width) {
            return this;
        } else {
            return amount > this.width ? this : new ImmutableRect2i(this.x, this.y, amount, this.height);
        }
    }

    public ImmutableRect2i addOffset(int x, int y) {
        return new ImmutableRect2i(this.x + x, this.y + y, this.width, this.height);
    }

    public ImmutableRect2i matchWidthAndX(ImmutableRect2i rect) {
        return new ImmutableRect2i(rect.getX(), this.y, rect.getWidth(), this.height);
    }

    public boolean equals(Object obj) {
        return !(obj instanceof ImmutableRect2i other) ? false : this.x == other.x && this.y == other.y && this.width == other.width && this.height == other.height;
    }

    public int hashCode() {
        int hash = 17;
        hash = hash * 31 + this.x;
        hash = hash * 31 + this.y;
        hash = hash * 31 + this.width;
        return hash * 31 + this.height;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("x", this.x).add("y", this.y).add("width", this.width).add("height", this.height).toString();
    }

    public Rect2i toMutable() {
        return new Rect2i(this.x, this.y, this.width, this.height);
    }
}