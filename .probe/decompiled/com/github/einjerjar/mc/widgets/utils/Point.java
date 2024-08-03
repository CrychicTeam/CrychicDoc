package com.github.einjerjar.mc.widgets.utils;

import java.util.Objects;

public class Point<T> {

    T x;

    T y;

    public Point(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public Point(T xy) {
        this.x = xy;
        this.y = xy;
    }

    public void setXY(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Point<?> point = (Point<?>) o;
            return Objects.equals(this.x, point.x) && Objects.equals(this.y, point.y);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.x, this.y });
    }

    public T x() {
        return this.x;
    }

    public Point<T> x(T x) {
        this.x = x;
        return this;
    }

    public T y() {
        return this.y;
    }

    public Point<T> y(T y) {
        this.y = y;
        return this;
    }
}