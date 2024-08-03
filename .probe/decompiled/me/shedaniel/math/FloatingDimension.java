package me.shedaniel.math;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class FloatingDimension implements Cloneable {

    public double width;

    public double height;

    public FloatingDimension() {
        this(0.0, 0.0);
    }

    public FloatingDimension(Dimension var1) {
        this((double) var1.width, (double) var1.height);
    }

    public FloatingDimension(FloatingDimension var1) {
        this(var1.width, var1.height);
    }

    public FloatingDimension(double var1, double var3) {
        this.width = var1;
        this.height = var3;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public void setSize(double var1, double var3) {
        this.width = var1;
        this.height = var3;
    }

    public FloatingDimension getFloatingSize() {
        return new FloatingDimension(this.width, this.height);
    }

    public Dimension getSize() {
        return new Dimension(this.width, this.height);
    }

    public void setSize(FloatingDimension var1) {
        this.setSize(var1.width, var1.height);
    }

    public void setSize(Dimension var1) {
        this.setSize((double) var1.width, (double) var1.height);
    }

    public boolean equals(Object var1) {
        if (var1 instanceof FloatingDimension) {
            var1 = var1;
            return this.width == var1.width && this.height == var1.height;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int var1 = 31 + Double.hashCode(this.width);
        return var1 * 31 + Double.hashCode(this.height);
    }

    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this);
    }

    public FloatingDimension clone() {
        return this.getFloatingSize();
    }
}