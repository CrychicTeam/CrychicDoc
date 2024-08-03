package math.fast;

final class JafamaFastMath {

    private static final boolean USE_TWO_POW_TAB = false;

    public static final double PI_SUP = Double.longBitsToDouble(Double.doubleToRawLongBits(Math.PI) + 1L);

    private static final double ONE_DIV_F2 = 0.5;

    private static final double ONE_DIV_F3 = 0.16666666666666666;

    private static final double ONE_DIV_F4 = 0.041666666666666664;

    private static final double TWO_POW_24 = twoPow(24);

    private static final double TWO_POW_N24 = twoPow(-24);

    private static final double TWO_POW_66 = twoPow(66);

    private static final double TWO_POW_450 = twoPow(450);

    private static final double TWO_POW_N450 = twoPow(-450);

    private static final double TWO_POW_750 = twoPow(750);

    private static final double TWO_POW_N750 = twoPow(-750);

    private static final int MIN_DOUBLE_EXPONENT = -1074;

    private static final int MAX_DOUBLE_EXPONENT = 1023;

    private static final double[] ONE_OVER_TWOPI_TAB = new double[] { 2670176.0, 1.4390161E7, 346751.0, 644596.0, 8211767.0, 7354072.0, 1.0839631E7, 1106960.0, 8361048.0, 1.539883E7, 1.5816813E7, 1.317979E7, 9474932.0, 1.2059026E7, 4962946.0, 7627911.0, 4163450.0, 1.3053002E7, 6934458.0, 2133373.0, 4959953.0, 2177639.0, 1837485.0, 1564560.0, 5137525.0, 9330900.0, 1.3532455E7, 2168802.0, 1.5695434E7, 968702.0, 2490359.0, 8480259.0, 1.65017E7, 6477442.0, 1.0176475E7, 5087155.0, 1.3234882E7, 7197649.0, 9427367.0, 9960075.0, 6113774.0, 1.1664121E7, 8150735.0, 4312701.0, 1.4849188E7, 1.2229374E7, 1.4150727E7 };

    private static final double TWOPI_TAB0 = Double.longBitsToDouble(4618760255839404032L);

    private static final double TWOPI_TAB1 = Double.longBitsToDouble(4509304086968926208L);

    private static final double TWOPI_TAB2 = Double.longBitsToDouble(4402346256551116800L);

    private static final double TWOPI_TAB3 = Double.longBitsToDouble(4294406894572797952L);

    private static final double TWOPI_TAB4 = Double.longBitsToDouble(4183874305429340160L);

    private static final double INVPIO2 = Double.longBitsToDouble(4603909380684499075L);

    private static final double PIO2_HI = Double.longBitsToDouble(4609753056924401664L);

    private static final double PIO2_LO = Double.longBitsToDouble(4454258360616903473L);

    private static final double INVTWOPI = INVPIO2 / 4.0;

    private static final double TWOPI_HI = 4.0 * PIO2_HI;

    private static final double TWOPI_LO = 4.0 * PIO2_LO;

    private static final double NORMALIZE_ANGLE_MAX_MEDIUM_DOUBLE = StrictMath.pow(2.0, 20.0) * (Math.PI * 2);

    private static final int SIN_COS_TABS_SIZE = (1 << getTabSizePower(11)) + 1;

    private static final double SIN_COS_DELTA_HI = TWOPI_HI / (double) (SIN_COS_TABS_SIZE - 1);

    private static final double SIN_COS_DELTA_LO = TWOPI_LO / (double) (SIN_COS_TABS_SIZE - 1);

    private static final double SIN_COS_INDEXER = 1.0 / (SIN_COS_DELTA_HI + SIN_COS_DELTA_LO);

    private static final double[] sinTab = new double[SIN_COS_TABS_SIZE];

    private static final double[] cosTab = new double[SIN_COS_TABS_SIZE];

    private static final double SIN_COS_MAX_VALUE_FOR_INT_MODULO = 4194303.0 / SIN_COS_INDEXER * 0.99;

    private static final double ASIN_MAX_VALUE_FOR_TABS = StrictMath.sin(Math.toRadians(73.0));

    private static final int ASIN_TABS_SIZE = (1 << getTabSizePower(13)) + 1;

    private static final double ASIN_DELTA = ASIN_MAX_VALUE_FOR_TABS / (double) (ASIN_TABS_SIZE - 1);

    private static final double ASIN_INDEXER = 1.0 / ASIN_DELTA;

    private static final double[] asinTab = new double[ASIN_TABS_SIZE];

    private static final double[] asinDer1DivF1Tab = new double[ASIN_TABS_SIZE];

    private static final double[] asinDer2DivF2Tab = new double[ASIN_TABS_SIZE];

    private static final double[] asinDer3DivF3Tab = new double[ASIN_TABS_SIZE];

    private static final double[] asinDer4DivF4Tab = new double[ASIN_TABS_SIZE];

    private static final double ASIN_PIO2_HI = Double.longBitsToDouble(4609753056924675352L);

    private static final double ASIN_PIO2_LO = Double.longBitsToDouble(4364452196894661639L);

    private static final double ASIN_PS0 = Double.longBitsToDouble(4595172819793696085L);

    private static final double ASIN_PS1 = Double.longBitsToDouble(-4623835544539140227L);

    private static final double ASIN_PS2 = Double.longBitsToDouble(4596417465768494165L);

    private static final double ASIN_PS3 = Double.longBitsToDouble(-4637438604930937029L);

    private static final double ASIN_PS4 = Double.longBitsToDouble(4560439845004096136L);

    private static final double ASIN_PS5 = Double.longBitsToDouble(4540259411154564873L);

    private static final double ASIN_QS1 = Double.longBitsToDouble(-4610777653840302773L);

    private static final double ASIN_QS2 = Double.longBitsToDouble(4611733184086379208L);

    private static final double ASIN_QS3 = Double.longBitsToDouble(-4618997306433404583L);

    private static final double ASIN_QS4 = Double.longBitsToDouble(4590215604441354882L);

    private static final double ATAN_MAX_VALUE_FOR_TABS = StrictMath.tan(Math.toRadians(74.0));

    private static final int ATAN_TABS_SIZE = (1 << getTabSizePower(12)) + 1;

    private static final double ATAN_DELTA = ATAN_MAX_VALUE_FOR_TABS / (double) (ATAN_TABS_SIZE - 1);

    private static final double ATAN_INDEXER = 1.0 / ATAN_DELTA;

    private static final double[] atanTab = new double[ATAN_TABS_SIZE];

    private static final double[] atanDer1DivF1Tab = new double[ATAN_TABS_SIZE];

    private static final double[] atanDer2DivF2Tab = new double[ATAN_TABS_SIZE];

    private static final double[] atanDer3DivF3Tab = new double[ATAN_TABS_SIZE];

    private static final double[] atanDer4DivF4Tab = new double[ATAN_TABS_SIZE];

    private static final double ATAN_HI3 = Double.longBitsToDouble(4609753056924675352L);

    private static final double ATAN_LO3 = Double.longBitsToDouble(4364452196894661639L);

    private static final double ATAN_AT0 = Double.longBitsToDouble(4599676419421066509L);

    private static final double ATAN_AT1 = Double.longBitsToDouble(-4626998257160492092L);

    private static final double ATAN_AT2 = Double.longBitsToDouble(4594314991288484863L);

    private static final double ATAN_AT3 = Double.longBitsToDouble(-4630701217362536847L);

    private static final double ATAN_AT4 = Double.longBitsToDouble(4591215095208222830L);

    private static final double ATAN_AT5 = Double.longBitsToDouble(-4633165035261879699L);

    private static final double ATAN_AT6 = Double.longBitsToDouble(4589464229703073105L);

    private static final double ATAN_AT7 = Double.longBitsToDouble(-4634804155249132134L);

    private static final double ATAN_AT8 = Double.longBitsToDouble(4587333258118041067L);

    private static final double ATAN_AT9 = Double.longBitsToDouble(-4637946461342241745L);

    private static final double ATAN_AT10 = Double.longBitsToDouble(4580351289466214929L);

    private static final double[] twoPowTab = null;

    static double cos(double angle) {
        angle = Math.abs(angle);
        if (angle > SIN_COS_MAX_VALUE_FOR_INT_MODULO) {
            angle = remainderTwoPi(angle);
            if (angle < 0.0) {
                angle += Math.PI * 2;
            }
        }
        int index = (int) (angle * SIN_COS_INDEXER + 0.5);
        double delta = angle - (double) index * SIN_COS_DELTA_HI - (double) index * SIN_COS_DELTA_LO;
        index &= SIN_COS_TABS_SIZE - 2;
        double indexCos = cosTab[index];
        double indexSin = sinTab[index];
        return indexCos + delta * (-indexSin + delta * (-indexCos * 0.5 + delta * (indexSin * 0.16666666666666666 + delta * indexCos * 0.041666666666666664)));
    }

    static double asin(double value) {
        boolean negateResult;
        if (value < 0.0) {
            value = -value;
            negateResult = true;
        } else {
            negateResult = false;
        }
        if (value <= ASIN_MAX_VALUE_FOR_TABS) {
            int index = (int) (value * ASIN_INDEXER + 0.5);
            double delta = value - (double) index * ASIN_DELTA;
            double result = asinTab[index] + delta * (asinDer1DivF1Tab[index] + delta * (asinDer2DivF2Tab[index] + delta * (asinDer3DivF3Tab[index] + delta * asinDer4DivF4Tab[index])));
            return negateResult ? -result : result;
        } else if (value < 1.0) {
            double t = (1.0 - value) * 0.5;
            double p = t * (ASIN_PS0 + t * (ASIN_PS1 + t * (ASIN_PS2 + t * (ASIN_PS3 + t * (ASIN_PS4 + t * ASIN_PS5)))));
            double q = 1.0 + t * (ASIN_QS1 + t * (ASIN_QS2 + t * (ASIN_QS3 + t * ASIN_QS4)));
            double s = Math.sqrt(t);
            double z = s + s * (p / q);
            double result = ASIN_PIO2_HI - (z + z - ASIN_PIO2_LO);
            return negateResult ? -result : result;
        } else if (value == 1.0) {
            return negateResult ? -Math.PI / 2 : Math.PI / 2;
        } else {
            return Double.NaN;
        }
    }

    static double acos(double value) {
        return (Math.PI / 2) - asin(value);
    }

    static double atan(double value) {
        boolean negateResult;
        if (value < 0.0) {
            value = -value;
            negateResult = true;
        } else {
            negateResult = false;
        }
        if (value == 1.0) {
            return negateResult ? -Math.PI / 4 : Math.PI / 4;
        } else if (value <= ATAN_MAX_VALUE_FOR_TABS) {
            int index = (int) (value * ATAN_INDEXER + 0.5);
            double delta = value - (double) index * ATAN_DELTA;
            double result = atanTab[index] + delta * (atanDer1DivF1Tab[index] + delta * (atanDer2DivF2Tab[index] + delta * (atanDer3DivF3Tab[index] + delta * atanDer4DivF4Tab[index])));
            return negateResult ? -result : result;
        } else if (value < TWO_POW_66) {
            double x = -1.0 / value;
            double x2 = x * x;
            double x4 = x2 * x2;
            double s1 = x2 * (ATAN_AT0 + x4 * (ATAN_AT2 + x4 * (ATAN_AT4 + x4 * (ATAN_AT6 + x4 * (ATAN_AT8 + x4 * ATAN_AT10)))));
            double s2 = x4 * (ATAN_AT1 + x4 * (ATAN_AT3 + x4 * (ATAN_AT5 + x4 * (ATAN_AT7 + x4 * ATAN_AT9))));
            double result = ATAN_HI3 - (x * (s1 + s2) - ATAN_LO3 - x);
            return negateResult ? -result : result;
        } else if (Double.isNaN(value)) {
            return Double.NaN;
        } else {
            return negateResult ? -Math.PI / 2 : Math.PI / 2;
        }
    }

    static double atan2(double y, double x) {
        if (x > 0.0) {
            if (y == 0.0) {
                return y;
            } else {
                return x == Double.POSITIVE_INFINITY ? atan2_pinf_yyy(y) : atan(y / x);
            }
        } else if (x < 0.0) {
            if (y == 0.0) {
                return (double) signFromBit(y) * Math.PI;
            } else if (x == Double.NEGATIVE_INFINITY) {
                return atan2_ninf_yyy(y);
            } else if (y > 0.0) {
                return (Math.PI / 2) + atan(-x / y);
            } else {
                return y < 0.0 ? (-Math.PI / 2) - atan(x / y) : Double.NaN;
            }
        } else {
            return atan2_zeroOrNaN_yyy(x, y);
        }
    }

    static double hypot(double x, double y) {
        x = Math.abs(x);
        y = Math.abs(y);
        if (y < x) {
            double a = x;
            x = y;
            y = a;
        } else if (!(y >= x)) {
            if (x != Double.POSITIVE_INFINITY && y != Double.POSITIVE_INFINITY) {
                return Double.NaN;
            }
            return Double.POSITIVE_INFINITY;
        }
        if (y - x == y) {
            return y;
        } else {
            double factor;
            if (x > TWO_POW_450) {
                x *= TWO_POW_N750;
                y *= TWO_POW_N750;
                factor = TWO_POW_750;
            } else if (y < TWO_POW_N450) {
                x *= TWO_POW_750;
                y *= TWO_POW_750;
                factor = TWO_POW_N750;
            } else {
                factor = 1.0;
            }
            return factor * Math.sqrt(x * x + y * y);
        }
    }

    private JafamaFastMath() {
    }

    private static int getTabSizePower(int tabSizePower) {
        return tabSizePower;
    }

    private static long signFromBit(double value) {
        return Double.doubleToRawLongBits(value) >> 62 | 1L;
    }

    private static double twoPow(int power) {
        if (power <= -1023) {
            return power >= -1074 ? Double.longBitsToDouble(2251799813685248L >> -(power + 1023)) : 0.0;
        } else {
            return power > 1023 ? Double.POSITIVE_INFINITY : Double.longBitsToDouble((long) (power + 1023) << 52);
        }
    }

    private static double twoPowNormal(int power) {
        return Double.longBitsToDouble((long) (power + 1023) << 52);
    }

    private static double atan2_pinf_yyy(double y) {
        if (y == Double.POSITIVE_INFINITY) {
            return Math.PI / 4;
        } else if (y == Double.NEGATIVE_INFINITY) {
            return -Math.PI / 4;
        } else if (y > 0.0) {
            return 0.0;
        } else {
            return y < 0.0 ? -0.0 : Double.NaN;
        }
    }

    private static double atan2_ninf_yyy(double y) {
        if (y == Double.POSITIVE_INFINITY) {
            return Math.PI * 3.0 / 4.0;
        } else if (y == Double.NEGATIVE_INFINITY) {
            return -Math.PI * 3.0 / 4.0;
        } else if (y > 0.0) {
            return Math.PI;
        } else {
            return y < 0.0 ? -Math.PI : Double.NaN;
        }
    }

    private static double atan2_zeroOrNaN_yyy(double x, double y) {
        if (x == 0.0) {
            if (y == 0.0) {
                return signFromBit(x) < 0L ? (double) signFromBit(y) * Math.PI : y;
            } else if (y > 0.0) {
                return Math.PI / 2;
            } else {
                return y < 0.0 ? -Math.PI / 2 : Double.NaN;
            }
        } else {
            return Double.NaN;
        }
    }

    private static double remainderTwoPi(double angle) {
        boolean negateResult;
        if (angle < 0.0) {
            negateResult = true;
            angle = -angle;
        } else {
            negateResult = false;
        }
        if (angle <= NORMALIZE_ANGLE_MAX_MEDIUM_DOUBLE) {
            double fn = (double) ((int) (angle * INVTWOPI + 0.5));
            double result = angle - fn * TWOPI_HI - fn * TWOPI_LO;
            return negateResult ? -result : result;
        } else if (angle < Double.POSITIVE_INFINITY) {
            long lx = Double.doubleToRawLongBits(angle);
            long exp = (lx >> 52 & 2047L) - 1046L;
            double z = Double.longBitsToDouble(lx - (exp << 52));
            double x0 = (double) ((int) z);
            z = (z - x0) * TWO_POW_24;
            double x1 = (double) ((int) z);
            double x2 = (z - x1) * TWO_POW_24;
            double result = subRemainderTwoPi(x0, x1, x2, (int) exp, x2 == 0.0 ? 2 : 3);
            return negateResult ? -result : result;
        } else {
            return Double.NaN;
        }
    }

    private static double subRemainderTwoPi(double x0, double x1, double x2, int e0, int nx) {
        double f6 = 0.0;
        int jx = nx - 1;
        int jv = (e0 - 3) / 24;
        int q = e0 - ((jv << 4) + (jv << 3)) - 24;
        int j = jv + 4;
        double f5;
        double q0;
        double q1;
        double q2;
        double q3;
        double q4;
        if (jx == 1) {
            f5 = j >= 0 ? ONE_OVER_TWOPI_TAB[j] : 0.0;
            double f4 = j >= 1 ? ONE_OVER_TWOPI_TAB[j - 1] : 0.0;
            double f3 = j >= 2 ? ONE_OVER_TWOPI_TAB[j - 2] : 0.0;
            double f2 = j >= 3 ? ONE_OVER_TWOPI_TAB[j - 3] : 0.0;
            double f1 = j >= 4 ? ONE_OVER_TWOPI_TAB[j - 4] : 0.0;
            double f0 = j >= 5 ? ONE_OVER_TWOPI_TAB[j - 5] : 0.0;
            q0 = x0 * f1 + x1 * f0;
            q1 = x0 * f2 + x1 * f1;
            q2 = x0 * f3 + x1 * f2;
            q3 = x0 * f4 + x1 * f3;
            q4 = x0 * f5 + x1 * f4;
        } else {
            f6 = j >= 0 ? ONE_OVER_TWOPI_TAB[j] : 0.0;
            f5 = j >= 1 ? ONE_OVER_TWOPI_TAB[j - 1] : 0.0;
            double f4 = j >= 2 ? ONE_OVER_TWOPI_TAB[j - 2] : 0.0;
            double f3 = j >= 3 ? ONE_OVER_TWOPI_TAB[j - 3] : 0.0;
            double f2 = j >= 4 ? ONE_OVER_TWOPI_TAB[j - 4] : 0.0;
            double f1 = j >= 5 ? ONE_OVER_TWOPI_TAB[j - 5] : 0.0;
            double f0 = j >= 6 ? ONE_OVER_TWOPI_TAB[j - 6] : 0.0;
            q0 = x0 * f2 + x1 * f1 + x2 * f0;
            q1 = x0 * f3 + x1 * f2 + x2 * f1;
            q2 = x0 * f4 + x1 * f3 + x2 * f2;
            q3 = x0 * f5 + x1 * f4 + x2 * f3;
            q4 = x0 * f6 + x1 * f5 + x2 * f4;
        }
        double fw = (double) ((int) (TWO_POW_N24 * q4));
        int iq0 = (int) (q4 - TWO_POW_24 * fw);
        double z = q3 + fw;
        fw = (double) ((int) (TWO_POW_N24 * z));
        int iq1 = (int) (z - TWO_POW_24 * fw);
        z = q2 + fw;
        fw = (double) ((int) (TWO_POW_N24 * z));
        int iq2 = (int) (z - TWO_POW_24 * fw);
        z = q1 + fw;
        fw = (double) ((int) (TWO_POW_N24 * z));
        int iq3 = (int) (z - TWO_POW_24 * fw);
        z = q0 + fw;
        double twoPowQ = twoPowNormal(q);
        z = z * twoPowQ % 8.0;
        z -= (double) ((int) z);
        int ih;
        if (q > 0) {
            iq3 &= 16777215 >> q;
            ih = iq3 >> 23 - q;
        } else if (q == 0) {
            ih = iq3 >> 23;
        } else if (z >= 0.5) {
            ih = 2;
        } else {
            ih = 0;
        }
        if (ih > 0) {
            int carry;
            if (iq0 != 0) {
                carry = 1;
                iq0 = 16777216 - iq0;
                iq1 = 16777215 - iq1;
                iq2 = 16777215 - iq2;
                iq3 = 16777215 - iq3;
            } else if (iq1 != 0) {
                carry = 1;
                iq1 = 16777216 - iq1;
                iq2 = 16777215 - iq2;
                iq3 = 16777215 - iq3;
            } else if (iq2 != 0) {
                carry = 1;
                iq2 = 16777216 - iq2;
                iq3 = 16777215 - iq3;
            } else if (iq3 != 0) {
                carry = 1;
                iq3 = 16777216 - iq3;
            } else {
                carry = 0;
            }
            if (q > 0) {
                switch(q) {
                    case 1:
                        iq3 &= 8388607;
                        break;
                    case 2:
                        iq3 &= 4194303;
                }
            }
            if (ih == 2) {
                z = 1.0 - z;
                if (carry != 0) {
                    z -= twoPowQ;
                }
            }
        }
        int iq4;
        if (z == 0.0) {
            double q5;
            if (jx == 1) {
                f6 = ONE_OVER_TWOPI_TAB[jv + 5];
                q5 = x0 * f6 + x1 * f5;
            } else {
                double f7 = ONE_OVER_TWOPI_TAB[jv + 5];
                q5 = x0 * f7 + x1 * f6 + x2 * f5;
            }
            fw = (double) ((int) (TWO_POW_N24 * q5));
            iq0 = (int) (q5 - TWO_POW_24 * fw);
            z = q4 + fw;
            fw = (double) ((int) (TWO_POW_N24 * z));
            iq1 = (int) (z - TWO_POW_24 * fw);
            z = q3 + fw;
            fw = (double) ((int) (TWO_POW_N24 * z));
            iq2 = (int) (z - TWO_POW_24 * fw);
            z = q2 + fw;
            fw = (double) ((int) (TWO_POW_N24 * z));
            iq3 = (int) (z - TWO_POW_24 * fw);
            z = q1 + fw;
            fw = (double) ((int) (TWO_POW_N24 * z));
            iq4 = (int) (z - TWO_POW_24 * fw);
            z = q0 + fw;
            z = z * twoPowQ % 8.0;
            z -= (double) ((int) z);
            if (q > 0) {
                iq4 &= 16777215 >> q;
                ih = iq4 >> 23 - q;
            } else if (q == 0) {
                ih = iq4 >> 23;
            } else if (z >= 0.5) {
                ih = 2;
            } else {
                ih = 0;
            }
            if (ih > 0) {
                if (iq0 != 0) {
                    iq0 = 16777216 - iq0;
                    iq1 = 16777215 - iq1;
                    iq2 = 16777215 - iq2;
                    iq3 = 16777215 - iq3;
                    iq4 = 16777215 - iq4;
                } else if (iq1 != 0) {
                    iq1 = 16777216 - iq1;
                    iq2 = 16777215 - iq2;
                    iq3 = 16777215 - iq3;
                    iq4 = 16777215 - iq4;
                } else if (iq2 != 0) {
                    iq2 = 16777216 - iq2;
                    iq3 = 16777215 - iq3;
                    iq4 = 16777215 - iq4;
                } else if (iq3 != 0) {
                    iq3 = 16777216 - iq3;
                    iq4 = 16777215 - iq4;
                } else if (iq4 != 0) {
                    iq4 = 16777216 - iq4;
                }
                if (q > 0) {
                    switch(q) {
                        case 1:
                            iq4 &= 8388607;
                            break;
                        case 2:
                            iq4 &= 4194303;
                    }
                }
            }
            fw = twoPowQ * TWO_POW_N24;
        } else {
            iq4 = (int) (z / twoPowQ);
            fw = twoPowQ;
        }
        q4 = fw * (double) iq4;
        fw *= TWO_POW_N24;
        q3 = fw * (double) iq3;
        fw *= TWO_POW_N24;
        q2 = fw * (double) iq2;
        fw *= TWO_POW_N24;
        q1 = fw * (double) iq1;
        fw *= TWO_POW_N24;
        q0 = fw * (double) iq0;
        fw *= TWO_POW_N24;
        fw = TWOPI_TAB0 * q4;
        fw += TWOPI_TAB0 * q3 + TWOPI_TAB1 * q4;
        fw += TWOPI_TAB0 * q2 + TWOPI_TAB1 * q3 + TWOPI_TAB2 * q4;
        fw += TWOPI_TAB0 * q1 + TWOPI_TAB1 * q2 + TWOPI_TAB2 * q3 + TWOPI_TAB3 * q4;
        fw += TWOPI_TAB0 * q0 + TWOPI_TAB1 * q1 + TWOPI_TAB2 * q2 + TWOPI_TAB3 * q3 + TWOPI_TAB4 * q4;
        return ih == 0 ? fw : -fw;
    }

    private static void init() {
        int SIN_COS_PI_INDEX = (SIN_COS_TABS_SIZE - 1) / 2;
        int SIN_COS_PI_MUL_2_INDEX = 2 * SIN_COS_PI_INDEX;
        int SIN_COS_PI_MUL_0_5_INDEX = SIN_COS_PI_INDEX / 2;
        int SIN_COS_PI_MUL_1_5_INDEX = 3 * SIN_COS_PI_INDEX / 2;
        for (int i = 0; i < SIN_COS_TABS_SIZE; i++) {
            double angle = (double) i * SIN_COS_DELTA_HI + (double) i * SIN_COS_DELTA_LO;
            double sinAngle = Math.sin(angle);
            double cosAngle = Math.cos(angle);
            if (i == SIN_COS_PI_INDEX) {
                sinAngle = 0.0;
            } else if (i == SIN_COS_PI_MUL_2_INDEX) {
                sinAngle = 0.0;
            } else if (i == SIN_COS_PI_MUL_0_5_INDEX) {
                cosAngle = 0.0;
            } else if (i == SIN_COS_PI_MUL_1_5_INDEX) {
                cosAngle = 0.0;
            }
            sinTab[i] = sinAngle;
            cosTab[i] = cosAngle;
        }
        for (int i = 0; i < ASIN_TABS_SIZE; i++) {
            double x = (double) i * ASIN_DELTA;
            double oneMinusXSqInv = 1.0 / (1.0 - x * x);
            double oneMinusXSqInv0_5 = Math.sqrt(oneMinusXSqInv);
            double oneMinusXSqInv1_5 = oneMinusXSqInv0_5 * oneMinusXSqInv;
            double oneMinusXSqInv2_5 = oneMinusXSqInv1_5 * oneMinusXSqInv;
            double oneMinusXSqInv3_5 = oneMinusXSqInv2_5 * oneMinusXSqInv;
            asinTab[i] = Math.asin(x);
            asinDer1DivF1Tab[i] = oneMinusXSqInv0_5;
            asinDer2DivF2Tab[i] = x * oneMinusXSqInv1_5 * 0.5;
            asinDer3DivF3Tab[i] = (1.0 + 2.0 * x * x) * oneMinusXSqInv2_5 * 0.16666666666666666;
            asinDer4DivF4Tab[i] = (5.0 + 2.0 * x * (2.0 + x * (5.0 - 2.0 * x))) * oneMinusXSqInv3_5 * 0.041666666666666664;
        }
        for (int i = 0; i < ATAN_TABS_SIZE; i++) {
            double x = (double) i * ATAN_DELTA;
            double onePlusXSqInv = 1.0 / (1.0 + x * x);
            double onePlusXSqInv2 = onePlusXSqInv * onePlusXSqInv;
            double onePlusXSqInv3 = onePlusXSqInv2 * onePlusXSqInv;
            double onePlusXSqInv4 = onePlusXSqInv2 * onePlusXSqInv2;
            atanTab[i] = Math.atan(x);
            atanDer1DivF1Tab[i] = onePlusXSqInv;
            atanDer2DivF2Tab[i] = -2.0 * x * onePlusXSqInv2 * 0.5;
            atanDer3DivF3Tab[i] = (-2.0 + 6.0 * x * x) * onePlusXSqInv3 * 0.16666666666666666;
            atanDer4DivF4Tab[i] = 24.0 * x * (1.0 - x * x) * onePlusXSqInv4 * 0.041666666666666664;
        }
    }

    static {
        init();
    }
}