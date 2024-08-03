package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.aso;

public class MBToSliceGroupMap {

    private int[] groups;

    private int[] indices;

    private int[][] inverse;

    public MBToSliceGroupMap(int[] groups, int[] indices, int[][] inverse) {
        this.groups = groups;
        this.indices = indices;
        this.inverse = inverse;
    }

    public int[] getGroups() {
        return this.groups;
    }

    public int[] getIndices() {
        return this.indices;
    }

    public int[][] getInverse() {
        return this.inverse;
    }
}