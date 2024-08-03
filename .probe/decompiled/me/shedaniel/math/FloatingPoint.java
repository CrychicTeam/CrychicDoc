package me.shedaniel.math;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class FloatingPoint implements Cloneable {

    public double x;

    public double y;

    public FloatingPoint() {
        this(0.0, 0.0);
    }

    public FloatingPoint(Point var1) {
        this((double) var1.x, (double) var1.y);
    }

    public FloatingPoint(FloatingPoint var1) {
        this(var1.x, var1.y);
    }

    public FloatingPoint(double var1, double var3) {
        this.x = var1;
        this.y = var3;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public FloatingPoint getFloatingLocation() {
        return new FloatingPoint(this.x, this.y);
    }

    public Point getLocation() {
        return new Point(this.x, this.y);
    }

    public FloatingPoint clone() {
        return this.getFloatingLocation();
    }

    public void setLocation(double var1, double var3) {
        this.x = var1;
        this.y = var3;
    }

    public void move(double var1, double var3) {
        this.x = var1;
        this.y = var3;
    }

    public void translate(double var1, double var3) {
        this.x += var1;
        this.y += var3;
    }

    public boolean equals(Object var1) {
        if (var1 instanceof FloatingPoint) {
            var1 = var1;
            return this.x == var1.x && this.y == var1.y;
        } else {
            return super.equals(var1);
        }
    }

    public int hashCode() {
        int var1 = 31 + Double.hashCode(this.x);
        return var1 * 31 + Double.hashCode(this.y);
    }

    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this);
    }
}