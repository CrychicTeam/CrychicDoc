package net.minecraft.util;

public class Tuple<A, B> {

    private A a;

    private B b;

    public Tuple(A a0, B b1) {
        this.a = a0;
        this.b = b1;
    }

    public A getA() {
        return this.a;
    }

    public void setA(A a0) {
        this.a = a0;
    }

    public B getB() {
        return this.b;
    }

    public void setB(B b0) {
        this.b = b0;
    }
}