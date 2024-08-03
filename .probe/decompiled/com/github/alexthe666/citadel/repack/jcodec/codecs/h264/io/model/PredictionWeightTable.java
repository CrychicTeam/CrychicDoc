package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model;

public class PredictionWeightTable {

    public int lumaLog2WeightDenom;

    public int chromaLog2WeightDenom;

    public int[][] lumaWeight = new int[2][];

    public int[][][] chromaWeight = new int[2][][];

    public int[][] lumaOffset = new int[2][];

    public int[][][] chromaOffset = new int[2][][];
}