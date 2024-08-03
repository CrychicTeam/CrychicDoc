package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model;

public final class NALUnitType {

    public static final NALUnitType NON_IDR_SLICE = new NALUnitType(1, "NON_IDR_SLICE", "non IDR slice");

    public static final NALUnitType SLICE_PART_A = new NALUnitType(2, "SLICE_PART_A", "slice part a");

    public static final NALUnitType SLICE_PART_B = new NALUnitType(3, "SLICE_PART_B", "slice part b");

    public static final NALUnitType SLICE_PART_C = new NALUnitType(4, "SLICE_PART_C", "slice part c");

    public static final NALUnitType IDR_SLICE = new NALUnitType(5, "IDR_SLICE", "idr slice");

    public static final NALUnitType SEI = new NALUnitType(6, "SEI", "sei");

    public static final NALUnitType SPS = new NALUnitType(7, "SPS", "sequence parameter set");

    public static final NALUnitType PPS = new NALUnitType(8, "PPS", "picture parameter set");

    public static final NALUnitType ACC_UNIT_DELIM = new NALUnitType(9, "ACC_UNIT_DELIM", "access unit delimiter");

    public static final NALUnitType END_OF_SEQ = new NALUnitType(10, "END_OF_SEQ", "end of sequence");

    public static final NALUnitType END_OF_STREAM = new NALUnitType(11, "END_OF_STREAM", "end of stream");

    public static final NALUnitType FILLER_DATA = new NALUnitType(12, "FILLER_DATA", "filler data");

    public static final NALUnitType SEQ_PAR_SET_EXT = new NALUnitType(13, "SEQ_PAR_SET_EXT", "sequence parameter set extension");

    public static final NALUnitType AUX_SLICE = new NALUnitType(19, "AUX_SLICE", "auxilary slice");

    private static final NALUnitType[] lut = new NALUnitType[256];

    private static final NALUnitType[] _values = new NALUnitType[] { NON_IDR_SLICE, SLICE_PART_A, SLICE_PART_B, SLICE_PART_C, IDR_SLICE, SEI, SPS, PPS, ACC_UNIT_DELIM, END_OF_SEQ, END_OF_STREAM, FILLER_DATA, SEQ_PAR_SET_EXT, AUX_SLICE };

    private final int value;

    private final String displayName;

    private String _name;

    private NALUnitType(int value, String name, String displayName) {
        this.value = value;
        this._name = name;
        this.displayName = displayName;
    }

    public String getName() {
        return this.displayName;
    }

    public int getValue() {
        return this.value;
    }

    public static NALUnitType fromValue(int value) {
        return value < lut.length ? lut[value] : null;
    }

    public String toString() {
        return this._name;
    }

    static {
        for (int i = 0; i < _values.length; i++) {
            NALUnitType nalUnitType = _values[i];
            lut[nalUnitType.value] = nalUnitType;
        }
    }
}