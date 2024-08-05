package com.github.einjerjar.mc.widgets.utils;

import java.util.Objects;

public class Rect {

    int x;

    int y;

    int w;

    int h;

    public Rect(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return !(o instanceof Rect rect) ? false : this.x == rect.x && this.y == rect.y && this.w == rect.w && this.h == rect.h;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.x, this.y, this.w, this.h });
    }

    public int top() {
        return this.y;
    }

    public int bottom() {
        return this.y + this.h;
    }

    public int left() {
        return this.x;
    }

    public int right() {
        return this.x + this.w;
    }

    public void top(int t) {
        this.y = t;
    }

    public void bottom(int b) {
        this.h = b - this.y;
    }

    public void left(int l) {
        this.x = l;
    }

    public void right(int r) {
        this.w = r - this.x;
    }

    public int midX() {
        return (this.right() + this.left()) / 2;
    }

    public int midY() {
        return (this.bottom() + this.top()) / 2;
    }

    public boolean contains(double x, double y) {
        return x >= (double) this.left() && x <= (double) this.right() && y >= (double) this.top() && y <= (double) this.bottom();
    }

    public boolean contains(Point<Integer> p) {
        return this.contains((double) p.x().intValue(), (double) p.y().intValue());
    }

    public int x() {
        return this.x;
    }

    public Rect x(int x) {
        this.x = x;
        return this;
    }

    public int y() {
        return this.y;
    }

    public Rect y(int y) {
        this.y = y;
        return this;
    }

    public int w() {
        return this.w;
    }

    public Rect w(int w) {
        this.w = w;
        return this;
    }

    public int h() {
        return this.h;
    }

    public Rect h(int h) {
        this.h = h;
        return this;
    }
}