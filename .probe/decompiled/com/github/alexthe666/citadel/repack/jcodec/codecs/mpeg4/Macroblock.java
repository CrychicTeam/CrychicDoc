package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg4;

public class Macroblock {

    public static final int MBPRED_SIZE = 15;

    public Macroblock.Vector[] mvs = new Macroblock.Vector[4];

    public short[][] predValues;

    public int[] acpredDirections;

    public int mode;

    public int quant;

    public boolean fieldDCT;

    public boolean fieldPred;

    public boolean fieldForTop;

    public boolean fieldForBottom;

    private Macroblock.Vector[] pmvs = new Macroblock.Vector[4];

    private Macroblock.Vector[] qmvs = new Macroblock.Vector[4];

    public int cbp;

    public Macroblock.Vector[] bmvs = new Macroblock.Vector[4];

    public Macroblock.Vector[] bqmvs = new Macroblock.Vector[4];

    public Macroblock.Vector amv;

    public Macroblock.Vector mvsAvg;

    public int x;

    public int y;

    public int bound;

    public boolean acpredFlag;

    public short[] predictors;

    public short[][] block;

    public boolean coded;

    public boolean mcsel;

    public byte[][] pred;

    public static Macroblock.Vector vec() {
        return new Macroblock.Vector(0, 0);
    }

    public Macroblock() {
        for (int i = 0; i < 4; i++) {
            this.mvs[i] = vec();
            this.pmvs[i] = vec();
            this.qmvs[i] = vec();
            this.bmvs[i] = vec();
            this.bqmvs[i] = vec();
        }
        this.pred = new byte[][] { new byte[256], new byte[64], new byte[64], new byte[256], new byte[64], new byte[64] };
        this.predValues = new short[6][15];
        this.acpredDirections = new int[6];
        this.amv = vec();
        this.predictors = new short[8];
        this.block = new short[6][64];
    }

    public void reset(int x2, int y2, int bound2) {
        this.x = x2;
        this.y = y2;
        this.bound = bound2;
    }

    public static class Vector {

        public int x;

        public int y;

        public Vector(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}