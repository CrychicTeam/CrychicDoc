package me.shedaniel.math;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Point implements Cloneable {

    public int x;

    public int y;

    public Point() {
        this(0, 0);
    }

    public Point(Point var1) {
        this(var1.x, var1.y);
    }

    public Point(FloatingPoint var1) {
        this(var1.x, var1.y);
    }

    public Point(double var1, double var3) {
        this((int) var1, (int) var3);
    }

    public Point(int var1, int var2) {
        this.x = var1;
        this.y = var2;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public FloatingPoint getFloatingLocation() {
        return new FloatingPoint((double) this.x, (double) this.y);
    }

    public Point getLocation() {
        return new Point(this.x, this.y);
    }

    public Point clone() {
        return this.getLocation();
    }

    public void setLocation(double var1, double var3) {
        this.x = (int) Math.floor(var1 + 0.5);
        this.y = (int) Math.floor(var3 + 0.5);
    }

    public void move(int var1, int var2) {
        this.x = var1;
        this.y = var2;
    }

    public void translate(int var1, int var2) {
        this.x += var1;
        this.y += var2;
    }

    public boolean equals(Object var1) {
        if (var1 instanceof Point) {
            var1 = var1;
            return this.x == var1.x && this.y == var1.y;
        } else {
            return super.equals(var1);
        }
    }

    public int hashCode() {
        int var1 = 31 + this.x;
        return var1 * 31 + this.y;
    }

    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this);
    }
}