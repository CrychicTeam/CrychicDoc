package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model;

public final class SliceType {

    private static final SliceType[] _values = new SliceType[5];

    public static final SliceType P = new SliceType("P", 0);

    public static final SliceType B = new SliceType("B", 1);

    public static final SliceType I = new SliceType("I", 2);

    public static final SliceType SP = new SliceType("SP", 3);

    public static final SliceType SI = new SliceType("SI", 4);

    private String _name;

    private int _ordinal;

    private SliceType(String name, int ordinal) {
        this._name = name;
        this._ordinal = ordinal;
        _values[ordinal] = this;
    }

    public boolean isIntra() {
        return this == I || this == SI;
    }

    public boolean isInter() {
        return this != I && this != SI;
    }

    public static SliceType[] values() {
        return _values;
    }

    public int ordinal() {
        return this._ordinal;
    }

    public String toString() {
        return this._name;
    }

    public String name() {
        return this._name;
    }

    public static SliceType fromValue(int j) {
        return values()[j];
    }
}