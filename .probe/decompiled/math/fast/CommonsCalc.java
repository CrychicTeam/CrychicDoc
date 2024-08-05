package math.fast;

class CommonsCalc {

    private static final long HEX_40000000 = 1073741824L;

    private static final double[] FACT = new double[] { 1.0, 1.0, 2.0, 6.0, 24.0, 120.0, 720.0, 5040.0, 40320.0, 362880.0, 3628800.0, 3.99168E7, 4.790016E8, 6.227021E9F, 8.71782912E10, 1.307674368E12, 2.0922789888E13, 3.55687428096E14, 6.402373705728E15, 1.21645100408832E17 };

    private CommonsCalc() {
    }

    static double slowexp(double x, double[] result) {
        double[] xs = new double[2];
        double[] ys = new double[2];
        double[] facts = new double[2];
        double[] as = new double[2];
        split(x, xs);
        ys[0] = ys[1] = 0.0;
        for (int i = FACT.length - 1; i >= 0; i--) {
            splitMult(xs, ys, as);
            ys[0] = as[0];
            ys[1] = as[1];
            split(FACT[i], as);
            splitReciprocal(as, facts);
            splitAdd(ys, facts, as);
            ys[0] = as[0];
            ys[1] = as[1];
        }
        if (result != null) {
            result[0] = ys[0];
            result[1] = ys[1];
        }
        return ys[0] + ys[1];
    }

    private static void split(double d, double[] split) {
        if (d < 8.0E298 && d > -8.0E298) {
            double a = d * 1.0737418E9F;
            split[0] = d + a - a;
            split[1] = d - split[0];
        } else {
            double a = d * 9.313226E-10F;
            split[0] = (d + a - d) * 1.0737418E9F;
            split[1] = d - split[0];
        }
    }

    private static void resplit(double[] a) {
        double c = a[0] + a[1];
        double d = -(c - a[0] - a[1]);
        if (c < 8.0E298 && c > -8.0E298) {
            double z = c * 1.0737418E9F;
            a[0] = c + z - z;
            a[1] = c - a[0] + d;
        } else {
            double z = c * 9.313226E-10F;
            a[0] = (c + z - c) * 1.0737418E9F;
            a[1] = c - a[0] + d;
        }
    }

    private static void splitMult(double[] a, double[] b, double[] ans) {
        ans[0] = a[0] * b[0];
        ans[1] = a[0] * b[1] + a[1] * b[0] + a[1] * b[1];
        resplit(ans);
    }

    private static void splitAdd(double[] a, double[] b, double[] ans) {
        ans[0] = a[0] + b[0];
        ans[1] = a[1] + b[1];
        resplit(ans);
    }

    static void splitReciprocal(double[] in, double[] result) {
        double b = 2.3841858E-7F;
        double a = 0.99999976F;
        if (in[0] == 0.0) {
            in[0] = in[1];
            in[1] = 0.0;
        }
        result[0] = 0.99999976F / in[0];
        result[1] = (2.3841858E-7F * in[0] - 0.99999976F * in[1]) / (in[0] * in[0] + in[0] * in[1]);
        if (result[1] != result[1]) {
            result[1] = 0.0;
        }
        resplit(result);
        for (int i = 0; i < 2; i++) {
            double err = 1.0 - result[0] * in[0] - result[0] * in[1] - result[1] * in[0] - result[1] * in[1];
            err *= result[0] + result[1];
            result[1] += err;
        }
    }

    private static void quadMult(double[] a, double[] b, double[] result) {
        double[] xs = new double[2];
        double[] ys = new double[2];
        double[] zs = new double[2];
        split(a[0], xs);
        split(b[0], ys);
        splitMult(xs, ys, zs);
        result[0] = zs[0];
        result[1] = zs[1];
        split(b[1], ys);
        splitMult(xs, ys, zs);
        double tmp = result[0] + zs[0];
        result[1] -= tmp - result[0] - zs[0];
        result[0] = tmp;
        tmp = result[0] + zs[1];
        result[1] -= tmp - result[0] - zs[1];
        result[0] = tmp;
        split(a[1], xs);
        split(b[0], ys);
        splitMult(xs, ys, zs);
        tmp = result[0] + zs[0];
        result[1] -= tmp - result[0] - zs[0];
        result[0] = tmp;
        tmp = result[0] + zs[1];
        result[1] -= tmp - result[0] - zs[1];
        result[0] = tmp;
        split(a[1], xs);
        split(b[1], ys);
        splitMult(xs, ys, zs);
        tmp = result[0] + zs[0];
        result[1] -= tmp - result[0] - zs[0];
        result[0] = tmp;
        tmp = result[0] + zs[1];
        result[1] -= tmp - result[0] - zs[1];
        result[0] = tmp;
    }

    static double expint(int p, double[] result) {
        double[] xs = new double[2];
        double[] as = new double[2];
        double[] ys = new double[2];
        xs[0] = Math.E;
        xs[1] = 1.4456468917292502E-16;
        split(1.0, ys);
        while (p > 0) {
            if ((p & 1) != 0) {
                quadMult(ys, xs, as);
                ys[0] = as[0];
                ys[1] = as[1];
            }
            quadMult(xs, xs, as);
            xs[0] = as[0];
            xs[1] = as[1];
            p >>= 1;
        }
        if (result != null) {
            result[0] = ys[0];
            result[1] = ys[1];
            resplit(result);
        }
        return ys[0] + ys[1];
    }
}