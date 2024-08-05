package me.shedaniel.math;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Dimension implements Cloneable {

    public int width;

    public int height;

    public Dimension() {
        this(0, 0);
    }

    public Dimension(Dimension var1) {
        this(var1.width, var1.height);
    }

    public Dimension(FloatingDimension var1) {
        this(var1.width, var1.height);
    }

    public Dimension(int var1, int var2) {
        this.width = var1;
        this.height = var2;
    }

    public Dimension(double var1, double var3) {
        this.width = (int) Math.ceil(var1);
        this.height = (int) Math.ceil(var3);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setSize(double var1, double var3) {
        this.width = (int) Math.ceil(var1);
        this.height = (int) Math.ceil(var3);
    }

    public FloatingDimension getFloatingSize() {
        return new FloatingDimension((double) this.width, (double) this.height);
    }

    public Dimension getSize() {
        return new Dimension(this.width, this.height);
    }

    public void setSize(Dimension var1) {
        this.setSize(var1.width, var1.height);
    }

    public void setSize(FloatingDimension var1) {
        this.setSize(var1.width, var1.height);
    }

    public void setSize(int var1, int var2) {
        this.width = var1;
        this.height = var2;
    }

    public boolean equals(Object var1) {
        if (var1 instanceof Dimension) {
            var1 = var1;
            return this.width == var1.width && this.height == var1.height;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int var1 = 31 + this.width;
        return var1 * 31 + this.height;
    }

    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this);
    }

    public Dimension clone() {
        return this.getSize();
    }
}