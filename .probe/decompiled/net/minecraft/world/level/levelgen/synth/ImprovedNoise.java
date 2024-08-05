package net.minecraft.world.level.levelgen.synth;

import com.google.common.annotations.VisibleForTesting;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public final class ImprovedNoise {

    private static final float SHIFT_UP_EPSILON = 1.0E-7F;

    private final byte[] p;

    public final double xo;

    public final double yo;

    public final double zo;

    public ImprovedNoise(RandomSource randomSource0) {
        this.xo = randomSource0.nextDouble() * 256.0;
        this.yo = randomSource0.nextDouble() * 256.0;
        this.zo = randomSource0.nextDouble() * 256.0;
        this.p = new byte[256];
        for (int $$1 = 0; $$1 < 256; $$1++) {
            this.p[$$1] = (byte) $$1;
        }
        for (int $$2 = 0; $$2 < 256; $$2++) {
            int $$3 = randomSource0.nextInt(256 - $$2);
            byte $$4 = this.p[$$2];
            this.p[$$2] = this.p[$$2 + $$3];
            this.p[$$2 + $$3] = $$4;
        }
    }

    public double noise(double double0, double double1, double double2) {
        return this.noise(double0, double1, double2, 0.0, 0.0);
    }

    @Deprecated
    public double noise(double double0, double double1, double double2, double double3, double double4) {
        double $$5 = double0 + this.xo;
        double $$6 = double1 + this.yo;
        double $$7 = double2 + this.zo;
        int $$8 = Mth.floor($$5);
        int $$9 = Mth.floor($$6);
        int $$10 = Mth.floor($$7);
        double $$11 = $$5 - (double) $$8;
        double $$12 = $$6 - (double) $$9;
        double $$13 = $$7 - (double) $$10;
        double $$16;
        if (double3 != 0.0) {
            double $$14;
            if (double4 >= 0.0 && double4 < $$12) {
                $$14 = double4;
            } else {
                $$14 = $$12;
            }
            $$16 = (double) Mth.floor($$14 / double3 + 1.0E-7F) * double3;
        } else {
            $$16 = 0.0;
        }
        return this.sampleAndLerp($$8, $$9, $$10, $$11, $$12 - $$16, $$13, $$12);
    }

    public double noiseWithDerivative(double double0, double double1, double double2, double[] double3) {
        double $$4 = double0 + this.xo;
        double $$5 = double1 + this.yo;
        double $$6 = double2 + this.zo;
        int $$7 = Mth.floor($$4);
        int $$8 = Mth.floor($$5);
        int $$9 = Mth.floor($$6);
        double $$10 = $$4 - (double) $$7;
        double $$11 = $$5 - (double) $$8;
        double $$12 = $$6 - (double) $$9;
        return this.sampleWithDerivative($$7, $$8, $$9, $$10, $$11, $$12, double3);
    }

    private static double gradDot(int int0, double double1, double double2, double double3) {
        return SimplexNoise.dot(SimplexNoise.GRADIENT[int0 & 15], double1, double2, double3);
    }

    private int p(int int0) {
        return this.p[int0 & 0xFF] & 0xFF;
    }

    private double sampleAndLerp(int int0, int int1, int int2, double double3, double double4, double double5, double double6) {
        int $$7 = this.p(int0);
        int $$8 = this.p(int0 + 1);
        int $$9 = this.p($$7 + int1);
        int $$10 = this.p($$7 + int1 + 1);
        int $$11 = this.p($$8 + int1);
        int $$12 = this.p($$8 + int1 + 1);
        double $$13 = gradDot(this.p($$9 + int2), double3, double4, double5);
        double $$14 = gradDot(this.p($$11 + int2), double3 - 1.0, double4, double5);
        double $$15 = gradDot(this.p($$10 + int2), double3, double4 - 1.0, double5);
        double $$16 = gradDot(this.p($$12 + int2), double3 - 1.0, double4 - 1.0, double5);
        double $$17 = gradDot(this.p($$9 + int2 + 1), double3, double4, double5 - 1.0);
        double $$18 = gradDot(this.p($$11 + int2 + 1), double3 - 1.0, double4, double5 - 1.0);
        double $$19 = gradDot(this.p($$10 + int2 + 1), double3, double4 - 1.0, double5 - 1.0);
        double $$20 = gradDot(this.p($$12 + int2 + 1), double3 - 1.0, double4 - 1.0, double5 - 1.0);
        double $$21 = Mth.smoothstep(double3);
        double $$22 = Mth.smoothstep(double6);
        double $$23 = Mth.smoothstep(double5);
        return Mth.lerp3($$21, $$22, $$23, $$13, $$14, $$15, $$16, $$17, $$18, $$19, $$20);
    }

    private double sampleWithDerivative(int int0, int int1, int int2, double double3, double double4, double double5, double[] double6) {
        int $$7 = this.p(int0);
        int $$8 = this.p(int0 + 1);
        int $$9 = this.p($$7 + int1);
        int $$10 = this.p($$7 + int1 + 1);
        int $$11 = this.p($$8 + int1);
        int $$12 = this.p($$8 + int1 + 1);
        int $$13 = this.p($$9 + int2);
        int $$14 = this.p($$11 + int2);
        int $$15 = this.p($$10 + int2);
        int $$16 = this.p($$12 + int2);
        int $$17 = this.p($$9 + int2 + 1);
        int $$18 = this.p($$11 + int2 + 1);
        int $$19 = this.p($$10 + int2 + 1);
        int $$20 = this.p($$12 + int2 + 1);
        int[] $$21 = SimplexNoise.GRADIENT[$$13 & 15];
        int[] $$22 = SimplexNoise.GRADIENT[$$14 & 15];
        int[] $$23 = SimplexNoise.GRADIENT[$$15 & 15];
        int[] $$24 = SimplexNoise.GRADIENT[$$16 & 15];
        int[] $$25 = SimplexNoise.GRADIENT[$$17 & 15];
        int[] $$26 = SimplexNoise.GRADIENT[$$18 & 15];
        int[] $$27 = SimplexNoise.GRADIENT[$$19 & 15];
        int[] $$28 = SimplexNoise.GRADIENT[$$20 & 15];
        double $$29 = SimplexNoise.dot($$21, double3, double4, double5);
        double $$30 = SimplexNoise.dot($$22, double3 - 1.0, double4, double5);
        double $$31 = SimplexNoise.dot($$23, double3, double4 - 1.0, double5);
        double $$32 = SimplexNoise.dot($$24, double3 - 1.0, double4 - 1.0, double5);
        double $$33 = SimplexNoise.dot($$25, double3, double4, double5 - 1.0);
        double $$34 = SimplexNoise.dot($$26, double3 - 1.0, double4, double5 - 1.0);
        double $$35 = SimplexNoise.dot($$27, double3, double4 - 1.0, double5 - 1.0);
        double $$36 = SimplexNoise.dot($$28, double3 - 1.0, double4 - 1.0, double5 - 1.0);
        double $$37 = Mth.smoothstep(double3);
        double $$38 = Mth.smoothstep(double4);
        double $$39 = Mth.smoothstep(double5);
        double $$40 = Mth.lerp3($$37, $$38, $$39, (double) $$21[0], (double) $$22[0], (double) $$23[0], (double) $$24[0], (double) $$25[0], (double) $$26[0], (double) $$27[0], (double) $$28[0]);
        double $$41 = Mth.lerp3($$37, $$38, $$39, (double) $$21[1], (double) $$22[1], (double) $$23[1], (double) $$24[1], (double) $$25[1], (double) $$26[1], (double) $$27[1], (double) $$28[1]);
        double $$42 = Mth.lerp3($$37, $$38, $$39, (double) $$21[2], (double) $$22[2], (double) $$23[2], (double) $$24[2], (double) $$25[2], (double) $$26[2], (double) $$27[2], (double) $$28[2]);
        double $$43 = Mth.lerp2($$38, $$39, $$30 - $$29, $$32 - $$31, $$34 - $$33, $$36 - $$35);
        double $$44 = Mth.lerp2($$39, $$37, $$31 - $$29, $$35 - $$33, $$32 - $$30, $$36 - $$34);
        double $$45 = Mth.lerp2($$37, $$38, $$33 - $$29, $$34 - $$30, $$35 - $$31, $$36 - $$32);
        double $$46 = Mth.smoothstepDerivative(double3);
        double $$47 = Mth.smoothstepDerivative(double4);
        double $$48 = Mth.smoothstepDerivative(double5);
        double $$49 = $$40 + $$46 * $$43;
        double $$50 = $$41 + $$47 * $$44;
        double $$51 = $$42 + $$48 * $$45;
        double6[0] += $$49;
        double6[1] += $$50;
        double6[2] += $$51;
        return Mth.lerp3($$37, $$38, $$39, $$29, $$30, $$31, $$32, $$33, $$34, $$35, $$36);
    }

    @VisibleForTesting
    public void parityConfigString(StringBuilder stringBuilder0) {
        NoiseUtils.parityNoiseOctaveConfigString(stringBuilder0, this.xo, this.yo, this.zo, this.p);
    }
}