package me.shedaniel.math;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Rectangle implements Cloneable {

    public int x;

    public int y;

    public int width;

    public int height;

    public Rectangle() {
        this(0, 0, 0, 0);
    }

    public Rectangle(Rectangle var1) {
        this(var1.x, var1.y, var1.width, var1.height);
    }

    public Rectangle(FloatingRectangle var1) {
        this(var1.x, var1.y, var1.width, var1.height);
    }

    public Rectangle(int var1, int var2) {
        this(0, 0, var1, var2);
    }

    public Rectangle(Point var1, Dimension var2) {
        this(var1.x, var1.y, var2.width, var2.height);
    }

    public Rectangle(Point var1, FloatingDimension var2) {
        this((double) var1.x, (double) var1.y, var2.width, var2.height);
    }

    public Rectangle(FloatingPoint var1, Dimension var2) {
        this(var1.x, var1.y, (double) var2.width, (double) var2.height);
    }

    public Rectangle(FloatingPoint var1, FloatingDimension var2) {
        this(var1.x, var1.y, var2.width, var2.height);
    }

    public Rectangle(Point var1) {
        this(var1.x, var1.y, 0, 0);
    }

    public Rectangle(FloatingPoint var1) {
        this(var1.x, var1.y, 0.0, 0.0);
    }

    public Rectangle(Dimension var1) {
        this(0, 0, var1.width, var1.height);
    }

    public Rectangle(FloatingDimension var1) {
        this(0.0, 0.0, var1.width, var1.height);
    }

    public Rectangle(double var1, double var3, double var5, double var7) {
        this((int) Math.round(var1), (int) Math.round(var3), (int) Math.round(var5), (int) Math.round(var7));
    }

    public Rectangle(int var1, int var2, int var3, int var4) {
        this.x = var1;
        this.y = var2;
        this.width = var3;
        this.height = var4;
    }

    public int getX() {
        return this.x;
    }

    public int getMinX() {
        return this.x;
    }

    public int getMaxX() {
        return this.x + this.width;
    }

    public int getCenterX() {
        return this.x + this.width / 2;
    }

    public int getY() {
        return this.y;
    }

    public int getMinY() {
        return this.y;
    }

    public int getMaxY() {
        return this.y + this.height;
    }

    public int getCenterY() {
        return this.y + this.height / 2;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public FloatingRectangle getFloatingBounds() {
        return new FloatingRectangle((double) this.x, (double) this.y, (double) this.width, (double) this.height);
    }

    public Rectangle getBounds() {
        return new Rectangle(this.x, this.y, this.width, this.height);
    }

    public void setBounds(FloatingRectangle var1) {
        this.setBounds(var1.x, var1.y, var1.width, var1.height);
    }

    public void setBounds(Rectangle var1) {
        this.setBounds(var1.x, var1.y, var1.width, var1.height);
    }

    public void setBounds(double var1, double var3, double var5, double var7) {
        this.setBounds((int) Math.round(var1), (int) Math.round(var3), (int) Math.round(var5), (int) Math.round(var7));
    }

    public void setBounds(int var1, int var2, int var3, int var4) {
        this.reshape(var1, var2, var3, var4);
    }

    public void reshape(int var1, int var2, int var3, int var4) {
        this.x = var1;
        this.y = var2;
        this.width = var3;
        this.height = var4;
    }

    public FloatingPoint getFloatingLocation() {
        return new FloatingPoint((double) this.x, (double) this.y);
    }

    public Point getLocation() {
        return new Point(this.x, this.y);
    }

    public void setLocation(FloatingPoint var1) {
        this.setLocation(var1.x, var1.y);
    }

    public void setLocation(Point var1) {
        this.setLocation(var1.x, var1.y);
    }

    public void setLocation(double var1, double var3) {
        this.move((int) Math.round(var1), (int) Math.round(var3));
    }

    public void setLocation(int var1, int var2) {
        this.move(var1, var2);
    }

    public void move(int var1, int var2) {
        this.x = var1;
        this.y = var2;
    }

    public void translate(int var1, int var2) {
        this.x += var1;
        this.y += var2;
    }

    public Rectangle clone() {
        return this.getBounds();
    }

    public FloatingDimension getFloatingSize() {
        return new FloatingDimension((double) this.width, (double) this.height);
    }

    public void setSize(FloatingDimension var1) {
        this.setSize(var1.width, var1.height);
    }

    public Dimension getSize() {
        return new Dimension(this.width, this.height);
    }

    public void setSize(Dimension var1) {
        this.setSize(var1.width, var1.height);
    }

    public void setSize(double var1, double var3) {
        this.resize((int) Math.round(var1), (int) Math.round(var3));
    }

    public void setSize(int var1, int var2) {
        this.resize(var1, var2);
    }

    public void resize(int var1, int var2) {
        this.width = var1;
        this.height = var2;
    }

    public boolean contains(Point var1) {
        return this.contains(var1.x, var1.y);
    }

    public boolean contains(FloatingPoint var1) {
        return this.contains(var1.x, var1.y);
    }

    public boolean contains(int var1, int var2) {
        return this.inside(var1, var2);
    }

    public boolean contains(double var1, double var3) {
        return this.inside((int) var1, (int) var3);
    }

    public boolean contains(FloatingRectangle var1) {
        return this.contains((int) var1.x, (int) var1.y, (int) var1.width, (int) var1.height);
    }

    public boolean contains(Rectangle var1) {
        return this.contains(var1.x, var1.y, var1.width, var1.height);
    }

    public boolean contains(int var1, int var2, int var3, int var4) {
        return this.contains(var1, var2) && this.contains(var3, var4);
    }

    public boolean inside(int var1, int var2) {
        int var3 = this.x;
        int var4 = this.y;
        return !this.isEmpty() && var1 >= var3 && var1 <= var3 + this.width && var2 >= var4 && var2 <= var4 + this.height;
    }

    public boolean intersects(Rectangle var1) {
        int var2 = this.width;
        int var3 = this.height;
        int var4 = var1.width;
        int var5 = var1.height;
        if (var4 > 0 && var5 > 0 && var2 > 0 && var3 > 0) {
            int var6 = this.x;
            int var7 = this.y;
            int var8 = var1.x;
            int var9 = var1.y;
            var4 += var8;
            var5 += var9;
            var2 += var6;
            var3 += var7;
            return (var4 < var8 || var4 > var6) && (var5 < var9 || var5 > var7) && (var2 < var6 || var2 > var8) && (var3 < var7 || var3 > var9);
        } else {
            return false;
        }
    }

    public Rectangle intersection(Rectangle var1) {
        int var2 = this.x;
        int var3 = this.y;
        int var4 = var1.x;
        int var5 = var1.y;
        long var6;
        var6 = (var6 = (long) var2) + (long) this.width;
        long var8;
        var8 = (var8 = (long) var3) + (long) this.height;
        long var10;
        var10 = (var10 = (long) var4) + (long) var1.width;
        long var12;
        var12 = (var12 = (long) var5) + (long) var1.height;
        if (var2 < var4) {
            var2 = var4;
        }
        if (var3 < var5) {
            var3 = var5;
        }
        if (var6 > var10) {
            var6 = var10;
        }
        if (var8 > var12) {
            var8 = var12;
        }
        var6 -= (long) var2;
        var8 -= (long) var3;
        if (var6 < -2147483648L) {
            var6 = -2147483648L;
        }
        if (var8 < -2147483648L) {
            var8 = -2147483648L;
        }
        return new Rectangle(var2, var3, (int) var6, (int) var8);
    }

    public Rectangle union(Rectangle var1) {
        long var2 = (long) this.width;
        long var4 = (long) this.height;
        if ((var2 | var4) < 0L) {
            return new Rectangle(var1);
        } else {
            long var6 = (long) var1.width;
            long var8 = (long) var1.height;
            if ((var6 | var8) < 0L) {
                return new Rectangle(this);
            } else {
                int var10 = this.x;
                int var11 = this.y;
                var2 += (long) var10;
                var4 += (long) var11;
                int var12 = var1.x;
                int var13 = var1.y;
                var6 += (long) var12;
                var8 += (long) var13;
                if (var10 > var12) {
                    var10 = var12;
                }
                if (var11 > var13) {
                    var11 = var13;
                }
                if (var2 < var6) {
                    var2 = var6;
                }
                if (var4 < var8) {
                    var4 = var8;
                }
                var2 -= (long) var10;
                var4 -= (long) var11;
                if (var2 > 2147483647L) {
                    var2 = 2147483647L;
                }
                if (var4 > 2147483647L) {
                    var4 = 2147483647L;
                }
                return new Rectangle(var10, var11, (int) var2, (int) var4);
            }
        }
    }

    public void add(int var1, int var2) {
        if ((this.width | this.height) < 0) {
            this.x = var1;
            this.y = var2;
            this.width = this.height = 0;
        } else {
            int var3 = this.x;
            int var4 = this.y;
            long var5 = (long) this.width;
            long var7 = (long) this.height;
            var5 += (long) var3;
            var7 += (long) var4;
            if (var3 > var1) {
                var3 = var1;
            }
            if (var4 > var2) {
                var4 = var2;
            }
            if (var5 < (long) var1) {
                var5 = (long) var1;
            }
            if (var7 < (long) var2) {
                var7 = (long) var2;
            }
            var5 -= (long) var3;
            var7 -= (long) var4;
            if (var5 > 2147483647L) {
                var5 = 2147483647L;
            }
            if (var7 > 2147483647L) {
                var7 = 2147483647L;
            }
            this.reshape(var3, var4, (int) var5, (int) var7);
        }
    }

    public void add(FloatingPoint var1) {
        this.add((int) var1.x, (int) var1.y);
    }

    public void add(Point var1) {
        this.add(var1.x, var1.y);
    }

    public void add(Rectangle var1) {
        long var2 = (long) this.width;
        long var4 = (long) this.height;
        if ((var2 | var4) < 0L) {
            this.reshape(var1.x, var1.y, var1.width, var1.height);
        }
        long var6 = (long) var1.width;
        long var8 = (long) var1.height;
        if ((var6 | var8) >= 0L) {
            int var10 = this.x;
            int var11 = this.y;
            var2 += (long) var10;
            var4 += (long) var11;
            int var12 = var1.x;
            int var13 = var1.y;
            var6 += (long) var12;
            var8 += (long) var13;
            if (var10 > var12) {
                var10 = var12;
            }
            if (var11 > var13) {
                var11 = var13;
            }
            if (var2 < var6) {
                var2 = var6;
            }
            if (var4 < var8) {
                var4 = var8;
            }
            var2 -= (long) var10;
            var4 -= (long) var11;
            if (var2 > 2147483647L) {
                var2 = 2147483647L;
            }
            if (var4 > 2147483647L) {
                var4 = 2147483647L;
            }
            this.reshape(var10, var11, (int) var2, (int) var4);
        }
    }

    public void grow(int var1, int var2) {
        long var3 = (long) this.x;
        long var5 = (long) this.y;
        long var7 = (long) this.width;
        long var9 = (long) this.height;
        var7 += var3;
        var9 += var5;
        var3 -= (long) var1;
        var5 -= (long) var2;
        var7 += (long) var1;
        var9 += (long) var2;
        if (var7 < var3) {
            if ((var7 = var7 - var3) < -2147483648L) {
                var7 = -2147483648L;
            }
            if (var3 < -2147483648L) {
                var3 = -2147483648L;
            } else if (var3 > 2147483647L) {
                var3 = 2147483647L;
            }
        } else {
            if (var3 < -2147483648L) {
                var3 = -2147483648L;
            } else if (var3 > 2147483647L) {
                var3 = 2147483647L;
            }
            if ((var7 = var7 - var3) < -2147483648L) {
                var7 = -2147483648L;
            } else if (var7 > 2147483647L) {
                var7 = 2147483647L;
            }
        }
        if (var9 < var5) {
            if ((var9 = var9 - var5) < -2147483648L) {
                var9 = -2147483648L;
            }
            if (var5 < -2147483648L) {
                var5 = -2147483648L;
            } else if (var5 > 2147483647L) {
                var5 = 2147483647L;
            }
        } else {
            if (var5 < -2147483648L) {
                var5 = -2147483648L;
            } else if (var5 > 2147483647L) {
                var5 = 2147483647L;
            }
            if ((var9 = var9 - var5) < -2147483648L) {
                var9 = -2147483648L;
            } else if (var9 > 2147483647L) {
                var9 = 2147483647L;
            }
        }
        this.reshape((int) var3, (int) var5, (int) var7, (int) var9);
    }

    public boolean isEmpty() {
        return this.width <= 0 || this.height <= 0;
    }

    public boolean equals(Object var1) {
        if (var1 == this) {
            return true;
        } else if (var1 instanceof Rectangle) {
            var1 = var1;
            return this.x == var1.x && this.y == var1.y && this.width == var1.width && this.height == var1.height;
        } else {
            return super.equals(var1);
        }
    }

    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this);
    }

    public int hashCode() {
        int var1 = 31 + this.x;
        var1 = var1 * 31 + this.y;
        var1 = var1 * 31 + this.width;
        return var1 * 31 + this.height;
    }
}