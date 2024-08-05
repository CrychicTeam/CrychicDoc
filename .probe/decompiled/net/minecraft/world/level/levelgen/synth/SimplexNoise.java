package net.minecraft.world.level.levelgen.synth;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class SimplexNoise {

    protected static final int[][] GRADIENT = new int[][] { { 1, 1, 0 }, { -1, 1, 0 }, { 1, -1, 0 }, { -1, -1, 0 }, { 1, 0, 1 }, { -1, 0, 1 }, { 1, 0, -1 }, { -1, 0, -1 }, { 0, 1, 1 }, { 0, -1, 1 }, { 0, 1, -1 }, { 0, -1, -1 }, { 1, 1, 0 }, { 0, -1, 1 }, { -1, 1, 0 }, { 0, -1, -1 } };

    private static final double SQRT_3 = Math.sqrt(3.0);

    private static final double F2 = 0.5 * (SQRT_3 - 1.0);

    private static final double G2 = (3.0 - SQRT_3) / 6.0;

    private final int[] p = new int[512];

    public final double xo;

    public final double yo;

    public final double zo;

    public SimplexNoise(RandomSource randomSource0) {
        this.xo = randomSource0.nextDouble() * 256.0;
        this.yo = randomSource0.nextDouble() * 256.0;
        this.zo = randomSource0.nextDouble() * 256.0;
        int $$1 = 0;
        while ($$1 < 256) {
            this.p[$$1] = $$1++;
        }
        for (int $$2 = 0; $$2 < 256; $$2++) {
            int $$3 = randomSource0.nextInt(256 - $$2);
            int $$4 = this.p[$$2];
            this.p[$$2] = this.p[$$3 + $$2];
            this.p[$$3 + $$2] = $$4;
        }
    }

    private int p(int int0) {
        return this.p[int0 & 0xFF];
    }

    protected static double dot(int[] int0, double double1, double double2, double double3) {
        return (double) int0[0] * double1 + (double) int0[1] * double2 + (double) int0[2] * double3;
    }

    private double getCornerNoise3D(int int0, double double1, double double2, double double3, double double4) {
        double $$5 = double4 - double1 * double1 - double2 * double2 - double3 * double3;
        double $$6;
        if ($$5 < 0.0) {
            $$6 = 0.0;
        } else {
            $$5 *= $$5;
            $$6 = $$5 * $$5 * dot(GRADIENT[int0], double1, double2, double3);
        }
        return $$6;
    }

    public double getValue(double double0, double double1) {
        double $$2 = (double0 + double1) * F2;
        int $$3 = Mth.floor(double0 + $$2);
        int $$4 = Mth.floor(double1 + $$2);
        double $$5 = (double) ($$3 + $$4) * G2;
        double $$6 = (double) $$3 - $$5;
        double $$7 = (double) $$4 - $$5;
        double $$8 = double0 - $$6;
        double $$9 = double1 - $$7;
        int $$10;
        int $$11;
        if ($$8 > $$9) {
            $$10 = 1;
            $$11 = 0;
        } else {
            $$10 = 0;
            $$11 = 1;
        }
        double $$14 = $$8 - (double) $$10 + G2;
        double $$15 = $$9 - (double) $$11 + G2;
        double $$16 = $$8 - 1.0 + 2.0 * G2;
        double $$17 = $$9 - 1.0 + 2.0 * G2;
        int $$18 = $$3 & 0xFF;
        int $$19 = $$4 & 0xFF;
        int $$20 = this.p($$18 + this.p($$19)) % 12;
        int $$21 = this.p($$18 + $$10 + this.p($$19 + $$11)) % 12;
        int $$22 = this.p($$18 + 1 + this.p($$19 + 1)) % 12;
        double $$23 = this.getCornerNoise3D($$20, $$8, $$9, 0.0, 0.5);
        double $$24 = this.getCornerNoise3D($$21, $$14, $$15, 0.0, 0.5);
        double $$25 = this.getCornerNoise3D($$22, $$16, $$17, 0.0, 0.5);
        return 70.0 * ($$23 + $$24 + $$25);
    }

    public double getValue(double double0, double double1, double double2) {
        double $$3 = 0.3333333333333333;
        double $$4 = (double0 + double1 + double2) * 0.3333333333333333;
        int $$5 = Mth.floor(double0 + $$4);
        int $$6 = Mth.floor(double1 + $$4);
        int $$7 = Mth.floor(double2 + $$4);
        double $$8 = 0.16666666666666666;
        double $$9 = (double) ($$5 + $$6 + $$7) * 0.16666666666666666;
        double $$10 = (double) $$5 - $$9;
        double $$11 = (double) $$6 - $$9;
        double $$12 = (double) $$7 - $$9;
        double $$13 = double0 - $$10;
        double $$14 = double1 - $$11;
        double $$15 = double2 - $$12;
        int $$16;
        int $$17;
        int $$18;
        int $$19;
        int $$20;
        int $$21;
        if ($$13 >= $$14) {
            if ($$14 >= $$15) {
                $$16 = 1;
                $$17 = 0;
                $$18 = 0;
                $$19 = 1;
                $$20 = 1;
                $$21 = 0;
            } else if ($$13 >= $$15) {
                $$16 = 1;
                $$17 = 0;
                $$18 = 0;
                $$19 = 1;
                $$20 = 0;
                $$21 = 1;
            } else {
                $$16 = 0;
                $$17 = 0;
                $$18 = 1;
                $$19 = 1;
                $$20 = 0;
                $$21 = 1;
            }
        } else if ($$14 < $$15) {
            $$16 = 0;
            $$17 = 0;
            $$18 = 1;
            $$19 = 0;
            $$20 = 1;
            $$21 = 1;
        } else if ($$13 < $$15) {
            $$16 = 0;
            $$17 = 1;
            $$18 = 0;
            $$19 = 0;
            $$20 = 1;
            $$21 = 1;
        } else {
            $$16 = 0;
            $$17 = 1;
            $$18 = 0;
            $$19 = 1;
            $$20 = 1;
            $$21 = 0;
        }
        double $$52 = $$13 - (double) $$16 + 0.16666666666666666;
        double $$53 = $$14 - (double) $$17 + 0.16666666666666666;
        double $$54 = $$15 - (double) $$18 + 0.16666666666666666;
        double $$55 = $$13 - (double) $$19 + 0.3333333333333333;
        double $$56 = $$14 - (double) $$20 + 0.3333333333333333;
        double $$57 = $$15 - (double) $$21 + 0.3333333333333333;
        double $$58 = $$13 - 1.0 + 0.5;
        double $$59 = $$14 - 1.0 + 0.5;
        double $$60 = $$15 - 1.0 + 0.5;
        int $$61 = $$5 & 0xFF;
        int $$62 = $$6 & 0xFF;
        int $$63 = $$7 & 0xFF;
        int $$64 = this.p($$61 + this.p($$62 + this.p($$63))) % 12;
        int $$65 = this.p($$61 + $$16 + this.p($$62 + $$17 + this.p($$63 + $$18))) % 12;
        int $$66 = this.p($$61 + $$19 + this.p($$62 + $$20 + this.p($$63 + $$21))) % 12;
        int $$67 = this.p($$61 + 1 + this.p($$62 + 1 + this.p($$63 + 1))) % 12;
        double $$68 = this.getCornerNoise3D($$64, $$13, $$14, $$15, 0.6);
        double $$69 = this.getCornerNoise3D($$65, $$52, $$53, $$54, 0.6);
        double $$70 = this.getCornerNoise3D($$66, $$55, $$56, $$57, 0.6);
        double $$71 = this.getCornerNoise3D($$67, $$58, $$59, $$60, 0.6);
        return 32.0 * ($$68 + $$69 + $$70 + $$71);
    }
}