package math.fast;

final class CommonsAccurateMath {

    private static final long HEX_40000000 = 1073741824L;

    private static final boolean RECOMPUTE_TABLES_AT_RUNTIME = false;

    static final int EXP_INT_TABLE_MAX_INDEX = 750;

    static final int EXP_INT_TABLE_LEN = 1500;

    static final int LN_MANT_LEN = 1024;

    static final int EXP_FRAC_TABLE_LEN = 1025;

    private static final double LOG_MAX_VALUE = StrictMath.log(Double.MAX_VALUE);

    private static double exp_(double x, double extra, double[] hiPrec) {
        double intPartA;
        double intPartB;
        int intVal;
        if (x < 0.0) {
            intVal = (int) (-x);
            if (intVal > 746) {
                if (hiPrec != null) {
                    hiPrec[0] = 0.0;
                    hiPrec[1] = 0.0;
                }
                return 0.0;
            }
            if (intVal > 709) {
                double result = exp_(x + 40.191406F, extra, hiPrec) / 2.8504009514401178E17;
                if (hiPrec != null) {
                    hiPrec[0] /= 2.8504009514401178E17;
                    hiPrec[1] /= 2.8504009514401178E17;
                }
                return result;
            }
            if (intVal == 709) {
                double result = exp_(x + 1.4941406F, extra, hiPrec) / 4.455505956692757;
                if (hiPrec != null) {
                    hiPrec[0] /= 4.455505956692757;
                    hiPrec[1] /= 4.455505956692757;
                }
                return result;
            }
            intVal++;
            intPartA = CommonsAccurateMath.ExpIntTable.EXP_INT_TABLE_A[750 - intVal];
            intPartB = CommonsAccurateMath.ExpIntTable.EXP_INT_TABLE_B[750 - intVal];
            intVal = -intVal;
        } else {
            intVal = (int) x;
            if (intVal > 709) {
                if (hiPrec != null) {
                    hiPrec[0] = Double.POSITIVE_INFINITY;
                    hiPrec[1] = 0.0;
                }
                return Double.POSITIVE_INFINITY;
            }
            intPartA = CommonsAccurateMath.ExpIntTable.EXP_INT_TABLE_A[750 + intVal];
            intPartB = CommonsAccurateMath.ExpIntTable.EXP_INT_TABLE_B[750 + intVal];
        }
        int intFrac = (int) ((x - (double) intVal) * 1024.0);
        double fracPartA = CommonsAccurateMath.ExpFracTable.EXP_FRAC_TABLE_A[intFrac];
        double fracPartB = CommonsAccurateMath.ExpFracTable.EXP_FRAC_TABLE_B[intFrac];
        double epsilon = x - ((double) intVal + (double) intFrac / 1024.0);
        double z = 0.04168701738764507;
        z = z * epsilon + 0.1666666505023083;
        z = z * epsilon + 0.5000000000042687;
        z = z * epsilon + 1.0;
        z = z * epsilon + -3.940510424527919E-20;
        double tempA = intPartA * fracPartA;
        double tempB = intPartA * fracPartB + intPartB * fracPartA + intPartB * fracPartB;
        double tempC = tempB + tempA;
        double result;
        if (extra != 0.0) {
            result = tempC * extra * z + tempC * extra + tempC * z + tempB + tempA;
        } else {
            result = tempC * z + tempB + tempA;
        }
        if (hiPrec != null) {
            hiPrec[0] = tempA;
            hiPrec[1] = tempC * extra * z + tempC * extra + tempC * z + tempB;
        }
        return result;
    }

    static double expm1(double x) {
        return expm1_(x, null);
    }

    private static double expm1_(double x, double[] hiPrecOut) {
        if (Double.isNaN(x) || x == 0.0) {
            return x;
        } else if (!(x <= -1.0) && !(x >= 1.0)) {
            boolean negative = false;
            if (x < 0.0) {
                x = -x;
                negative = true;
            }
            int intFrac = (int) (x * 1024.0);
            double tempA = CommonsAccurateMath.ExpFracTable.EXP_FRAC_TABLE_A[intFrac] - 1.0;
            double tempB = CommonsAccurateMath.ExpFracTable.EXP_FRAC_TABLE_B[intFrac];
            double temp = tempA + tempB;
            tempB = -(temp - tempA - tempB);
            double var58 = temp * 1.0737418E9F;
            double baseA = temp + var58 - var58;
            double baseB = tempB + (temp - baseA);
            double epsilon = x - (double) intFrac / 1024.0;
            double zb = 0.008336750013465571;
            double var35 = zb * epsilon + 0.041666663879186654;
            double var36 = var35 * epsilon + 0.16666666666745392;
            double var37 = var36 * epsilon + 0.49999999999999994;
            double var38 = var37 * epsilon;
            double var39 = var38 * epsilon;
            double tempx = epsilon + var39;
            double var40 = -(tempx - epsilon - var39);
            double var47 = tempx * 1.0737418E9F;
            double var48 = tempx + var47 - var47;
            double var41 = var40 + (tempx - var48);
            double ya = var48 * baseA;
            tempx = ya + var48 * baseB;
            double yb = -(tempx - ya - var48 * baseB);
            double var50 = tempx + var41 * baseA;
            yb += -(var50 - tempx - var41 * baseA);
            tempx = var50 + var41 * baseB;
            yb += -(tempx - var50 - var41 * baseB);
            double var52 = tempx + baseA;
            yb += -(var52 - baseA - tempx);
            tempx = var52 + var48;
            yb += -(tempx - var52 - var48);
            double var54 = tempx + baseB;
            yb += -(var54 - tempx - baseB);
            tempx = var54 + var41;
            yb += -(tempx - var54 - var41);
            ya = tempx;
            if (negative) {
                double denom = 1.0 + tempx;
                double denomr = 1.0 / denom;
                double denomb = -(denom - 1.0 - tempx) + yb;
                double ratio = tempx * denomr;
                double var56 = ratio * 1.0737418E9F;
                double ra = ratio + var56 - var56;
                double rb = ratio - ra;
                double var57 = denom * 1.0737418E9F;
                double var45 = denom + var57 - var57;
                double var42 = denom - var45;
                rb += (tempx - var45 * ra - var45 * rb - var42 * ra - var42 * rb) * denomr;
                rb += yb * denomr;
                rb += -tempx * denomb * denomr * denomr;
                ya = -ra;
                yb = -rb;
            }
            if (hiPrecOut != null) {
                hiPrecOut[0] = ya;
                hiPrecOut[1] = yb;
            }
            return ya + yb;
        } else {
            double[] hiPrec = new double[2];
            exp_(x, 0.0, hiPrec);
            if (x > 0.0) {
                return -1.0 + hiPrec[0] + hiPrec[1];
            } else {
                double ra = -1.0 + hiPrec[0];
                double rb = -(ra + 1.0 - hiPrec[0]);
                rb += hiPrec[1];
                return ra + rb;
            }
        }
    }

    static double cosh(double x) {
        if (Double.isNaN(x)) {
            return x;
        } else if (x > 20.0) {
            if (x >= LOG_MAX_VALUE) {
                double t = Math.exp(0.5 * x);
                return 0.5 * t * t;
            } else {
                return 0.5 * Math.exp(x);
            }
        } else if (x < -20.0) {
            if (x <= -LOG_MAX_VALUE) {
                double t = Math.exp(-0.5 * x);
                return 0.5 * t * t;
            } else {
                return 0.5 * Math.exp(-x);
            }
        } else {
            double[] hiPrec = new double[2];
            if (x < 0.0) {
                x = -x;
            }
            exp_(x, 0.0, hiPrec);
            double ya = hiPrec[0] + hiPrec[1];
            double yb = -(ya - hiPrec[0] - hiPrec[1]);
            double temp = ya * 1.0737418E9F;
            double yaa = ya + temp - temp;
            double yab = ya - yaa;
            double recip = 1.0 / ya;
            temp = recip * 1.0737418E9F;
            double recipa = recip + temp - temp;
            double recipb = recip - recipa;
            recipb += (1.0 - yaa * recipa - yaa * recipb - yab * recipa - yab * recipb) * recip;
            recipb += -yb * recip * recip;
            temp = ya + recipa;
            yb += -(temp - ya - recipa);
            double var28 = temp + recipb;
            yb += -(var28 - temp - recipb);
            double result = var28 + yb;
            return result * 0.5;
        }
    }

    static double sinh(double x) {
        boolean negate = false;
        if (Double.isNaN(x)) {
            return x;
        } else if (x > 20.0) {
            if (x >= LOG_MAX_VALUE) {
                double t = Math.exp(0.5 * x);
                return 0.5 * t * t;
            } else {
                return 0.5 * Math.exp(x);
            }
        } else if (x < -20.0) {
            if (x <= -LOG_MAX_VALUE) {
                double t = Math.exp(-0.5 * x);
                return -0.5 * t * t;
            } else {
                return -0.5 * Math.exp(-x);
            }
        } else if (x == 0.0) {
            return x;
        } else {
            if (x < 0.0) {
                x = -x;
                negate = true;
            }
            double result;
            if (x > 0.25) {
                double[] hiPrec = new double[2];
                exp_(x, 0.0, hiPrec);
                double ya = hiPrec[0] + hiPrec[1];
                double yb = -(ya - hiPrec[0] - hiPrec[1]);
                double temp = ya * 1.0737418E9F;
                double yaa = ya + temp - temp;
                double yab = ya - yaa;
                double recip = 1.0 / ya;
                temp = recip * 1.0737418E9F;
                double recipa = recip + temp - temp;
                double recipb = recip - recipa;
                recipb += (1.0 - yaa * recipa - yaa * recipb - yab * recipa - yab * recipb) * recip;
                recipb += -yb * recip * recip;
                recipa = -recipa;
                recipb = -recipb;
                temp = ya + recipa;
                yb += -(temp - ya - recipa);
                double var43 = temp + recipb;
                yb += -(var43 - temp - recipb);
                result = var43 + yb;
                result *= 0.5;
            } else {
                double[] hiPrec = new double[2];
                expm1_(x, hiPrec);
                double ya = hiPrec[0] + hiPrec[1];
                double yb = -(ya - hiPrec[0] - hiPrec[1]);
                double denom = 1.0 + ya;
                double denomr = 1.0 / denom;
                double denomb = -(denom - 1.0 - ya) + yb;
                double ratio = ya * denomr;
                double temp = ratio * 1.0737418E9F;
                double ra = ratio + temp - temp;
                double rb = ratio - ra;
                temp = denom * 1.0737418E9F;
                double za = denom + temp - temp;
                double zb = denom - za;
                rb += (ya - za * ra - za * rb - zb * ra - zb * rb) * denomr;
                rb += yb * denomr;
                rb += -ya * denomb * denomr * denomr;
                temp = ya + ra;
                yb += -(temp - ya - ra);
                double var52 = temp + rb;
                yb += -(var52 - temp - rb);
                result = var52 + yb;
                result *= 0.5;
            }
            if (negate) {
                result = -result;
            }
            return result;
        }
    }

    static double tanh(double x) {
        boolean negate = false;
        if (Double.isNaN(x)) {
            return x;
        } else if (x > 20.0) {
            return 1.0;
        } else if (x < -20.0) {
            return -1.0;
        } else if (x == 0.0) {
            return x;
        } else {
            if (x < 0.0) {
                x = -x;
                negate = true;
            }
            double result;
            if (x >= 0.5) {
                double[] hiPrec = new double[2];
                exp_(x * 2.0, 0.0, hiPrec);
                double ya = hiPrec[0] + hiPrec[1];
                double yb = -(ya - hiPrec[0] - hiPrec[1]);
                double na = -1.0 + ya;
                double nb = -(na + 1.0 - ya);
                double temp = na + yb;
                nb += -(temp - na - yb);
                double da = 1.0 + ya;
                double db = -(da - 1.0 - ya);
                double var35 = da + yb;
                db += -(var35 - da - yb);
                double var36 = var35 * 1.0737418E9F;
                double daa = var35 + var36 - var36;
                double dab = var35 - daa;
                double ratio = temp / var35;
                double var37 = ratio * 1.0737418E9F;
                double ratioa = ratio + var37 - var37;
                double ratiob = ratio - ratioa;
                ratiob += (temp - daa * ratioa - daa * ratiob - dab * ratioa - dab * ratiob) / var35;
                ratiob += nb / var35;
                ratiob += -db * temp / var35 / var35;
                result = ratioa + ratiob;
            } else {
                double[] hiPrec = new double[2];
                expm1_(x * 2.0, hiPrec);
                double ya = hiPrec[0] + hiPrec[1];
                double yb = -(ya - hiPrec[0] - hiPrec[1]);
                double da = 2.0 + ya;
                double db = -(da - 2.0 - ya);
                double temp = da + yb;
                db += -(temp - da - yb);
                double var45 = temp * 1.0737418E9F;
                double daa = temp + var45 - var45;
                double dab = temp - daa;
                double ratio = ya / temp;
                double var46 = ratio * 1.0737418E9F;
                double ratioa = ratio + var46 - var46;
                double ratiob = ratio - ratioa;
                ratiob += (ya - daa * ratioa - daa * ratiob - dab * ratioa - dab * ratiob) / temp;
                ratiob += yb / temp;
                ratiob += -db * ya / temp / temp;
                result = ratioa + ratiob;
            }
            if (negate) {
                result = -result;
            }
            return result;
        }
    }

    private static final class ExpFracTable {

        static final double[] EXP_FRAC_TABLE_A = CommonsMathLiterals.loadExpFracA();

        static final double[] EXP_FRAC_TABLE_B = CommonsMathLiterals.loadExpFracB();
    }

    private static final class ExpIntTable {

        static final double[] EXP_INT_TABLE_A = CommonsMathLiterals.loadExpIntA();

        static final double[] EXP_INT_TABLE_B = CommonsMathLiterals.loadExpIntB();
    }
}