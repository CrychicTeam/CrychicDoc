package dev.xkmc.modulargolems.content.entity.ranged;

import java.util.function.Function;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class Estimator {

    private static final double ERR = 1.0E-5;

    private final double gk;

    private final double k;

    private final double vk;

    private final double g;

    private final double v;

    private final double x;

    private final int max;

    private final Vec3 dp;

    private final Vec3 ev;

    public Estimator(double G, double K, Vec3 pos, double V, int maxt, Vec3 ep, Vec3 eV) {
        this.gk = G / K;
        this.g = G;
        this.k = K;
        this.vk = V / K;
        this.v = V;
        this.max = maxt;
        this.ev = eV;
        Vec3 vdp = ep.subtract(pos);
        this.dp = vdp.add(Math.abs(vdp.x) < 1.0E-5 ? 1.0E-5 : 0.0, 0.0, Math.abs(vdp.z) < 1.0E-5 ? 1.0E-5 : 0.0);
        this.x = dis(this.dp.x, this.dp.z);
    }

    public static Estimator.SolResult solve(Function<Double, Double> f, double v, double min, double max, double err) {
        double x0 = min;
        double x1 = max;
        double v0 = (Double) f.apply(min);
        double v1 = (Double) f.apply(max);
        if (Math.abs(v0 - v) < err) {
            return new Estimator.SucSolRes(min);
        } else if (Math.abs(v1 - v) < err) {
            return new Estimator.SucSolRes(max);
        } else if (v0 > v && v1 > v) {
            return Estimator.SolType.OVER;
        } else if (v0 < v && v1 < v) {
            return Estimator.SolType.BELOW;
        } else {
            boolean inc = v0 < v1;
            while (Math.abs(x1 - x0) > err) {
                double x = (x0 + x1) / 2.0;
                double vm = (Double) f.apply(x);
                if (Math.abs(vm - v) < err) {
                    return new Estimator.SucSolRes(x);
                }
                if ((!(v < vm) || !inc) && (!(v > vm) || inc)) {
                    x0 = x;
                } else {
                    x1 = x;
                }
            }
            return new Estimator.SucSolRes((x0 + x1) / 2.0);
        }
    }

    public static Estimator.SolResult solve(Function<Double, Double> f2, Function<Double, Double> f1, double v, double x0, double x1, double err) {
        Estimator.SolResult sr = solve(f1, 0.0, x0, x1, err);
        if (sr.getType() == Estimator.SolType.ZERO) {
            double tip = sr.getVal();
            double max = (Double) f2.apply(tip);
            double v0 = (Double) f2.apply(x0);
            double v1 = (Double) f2.apply(x1);
            if (v > max && max > v0 && max > v1) {
                return Estimator.SolType.OVER;
            } else if (v < max && max < v0 && max < v1) {
                return Estimator.SolType.BELOW;
            } else {
                return (v - v0) * (v - max) < 0.0 ? solve(f2, v, x0, tip, err) : solve(f2, v, tip, x1, err);
            }
        } else {
            return solve(f2, v, x0, x1, err);
        }
    }

    private static double dis(double a, double b) {
        return Math.sqrt(a * a + b * b);
    }

    public Estimator.EstiResult getAnswer() {
        if (this.v * this.v - 2.0 * this.g * this.dp.y < 0.0) {
            return Estimator.EstiType.CLOSE;
        } else {
            Estimator.EstiResult ans = this.getIdeal();
            double[] data = new double[] { ans.getA(), ans.getT() };
            if (this.estimate(data, 0.1, 0.2, 3.0)) {
                return Estimator.EstiType.FAIL;
            } else {
                return (Estimator.EstiResult) (this.estimate(data, 0.01, 0.2, 0.3) ? Estimator.EstiType.FAIL : new Estimator.SucEstiRes(data[0], data[1], this));
            }
        }
    }

    public Estimator.EstiResult getIdeal() {
        double xt0 = Math.abs((Math.sqrt(this.v * this.v - 2.0 * this.g * this.dp.y) + this.v) / this.g);
        double xt1 = Math.abs((-Math.sqrt(this.v * this.v - 2.0 * this.g * this.dp.y) + this.v) / this.g);
        double mint = Math.max(0.0, Math.min(xt0, xt1) + 1.0E-5);
        double maxt = Math.min((double) this.max, Math.max(xt0, xt1) - 1.0E-5);
        Estimator.SolResult r0 = solve(this::get_0, 0.0, mint, maxt, 1.0E-5);
        Estimator.SolResult r1;
        if (r0.getType() == Estimator.SolType.ZERO) {
            double bp = r0.getVal();
            Estimator.SolResult dr0 = solve(this::get_1, 0.0, mint, bp, 1.0E-5);
            Estimator.SolResult dr1 = solve(this::get_1, 0.0, bp, maxt, 1.0E-5);
            if (dr0.getType() == Estimator.SolType.ZERO) {
                r1 = dr0;
            } else {
                r1 = dr1;
            }
        } else {
            r1 = solve(this::get_1, 0.0, mint, maxt, 1.0E-5);
        }
        if (r1.getType() != Estimator.SolType.ZERO) {
            return Estimator.EstiType.FAIL;
        } else {
            double t0 = r1.getVal();
            double a0 = Math.asin(this.dp.y / this.v / t0 + this.g * t0 / 2.0 / this.v);
            return new Estimator.SucEstiRes(a0, t0, this);
        }
    }

    public double getX0(double a, double t) {
        double xt = this.dp.x + this.ev.x * t;
        double zt = this.dp.z + this.ev.z * t;
        return this.vk * Math.cos(a) * (1.0 - Math.exp(-this.k * t)) - Math.sqrt(xt * xt + zt * zt);
    }

    public double getY0(double a, double t) {
        return -this.gk * t + (this.vk * Math.sin(a) + this.gk / this.k) * (1.0 - Math.exp(-this.k * t)) - this.dp.y - this.ev.y * t;
    }

    private boolean estimate(double[] data, double DA, double DT, double ER) {
        double a0 = data[0];
        double t0 = data[1];
        double x0 = this.getX0(a0, t0);
        double y0 = this.getY0(a0, t0);
        int count = 0;
        boolean out = false;
        double len;
        while ((len = dis(x0, y0)) > ER) {
            count++;
            double da = x0 * this.getXA(a0, t0) + y0 * this.getYA(a0, t0);
            double dt = x0 * this.getXT(a0, t0) + y0 * this.getYT(a0, t0);
            if (da > 0.0) {
                a0 -= DA;
            }
            if (da < 0.0) {
                a0 += DA;
            }
            if (dt > 0.0) {
                t0 -= DT;
            }
            if (dt < 0.0) {
                t0 += DT;
            }
            if ((out |= a0 < -Math.PI / 2 || a0 > Math.PI / 2) || (out |= t0 < 0.0 || t0 > (double) this.max)) {
                break;
            }
            x0 = this.getX0(a0, t0);
            y0 = this.getY0(a0, t0);
            if (Math.abs(dis(x0, y0) - len) < 1.0E-5 || count > 100) {
                break;
            }
        }
        data[0] = Mth.clamp(a0, -Math.PI / 2, Math.PI / 2);
        data[1] = Mth.clamp(t0, 0.0, (double) this.max);
        return out;
    }

    private double get_0(double t) {
        double mul = this.dp.y / this.v / t + this.g * t / 2.0 / this.v;
        double m = Math.sqrt(1.0 - mul * mul);
        return this.v * m - this.v * t * mul / m * (this.g / 2.0 / this.v - this.dp.y / this.v / t / t);
    }

    private double get_1(double t) {
        double mul = this.dp.y / this.v / t + this.g * t / 2.0 / this.v;
        return this.v * t * Math.sqrt(1.0 - mul * mul) - this.x;
    }

    private double getXA(double a, double t) {
        return -this.vk * Math.sin(a) * (1.0 - Math.exp(-this.k * t));
    }

    private double getXT(double a, double t) {
        double xt = this.dp.x + this.ev.x * t;
        double zt = this.dp.z + this.ev.z * t;
        double ext = ((this.ev.x * this.ev.x + this.ev.z * this.ev.z) * t + this.dp.x * this.ev.x + this.dp.z * this.ev.z) / Math.sqrt(xt * xt + zt * zt);
        return Math.cos(a) * this.v * Math.exp(-this.k * t) - ext;
    }

    private double getYA(double a, double t) {
        return this.vk * Math.cos(a) * (1.0 - Math.exp(-this.k * t));
    }

    private double getYT(double a, double t) {
        return (Math.sin(a) * this.v + this.gk) * Math.exp(-this.k * t) - this.gk - this.ev.y;
    }

    public interface EstiResult {

        double getA();

        double getT();

        Estimator.EstiType getType();

        Vec3 getVec();
    }

    public static enum EstiType implements Estimator.EstiResult {

        ZERO, FAIL, CLOSE;

        @Override
        public double getA() {
            return 0.0;
        }

        @Override
        public double getT() {
            return 0.0;
        }

        @Override
        public Estimator.EstiType getType() {
            return this;
        }

        @Override
        public Vec3 getVec() {
            return Vec3.ZERO;
        }
    }

    public interface SolResult {

        Estimator.SolType getType();

        double getVal();
    }

    public static enum SolType implements Estimator.SolResult {

        OVER, BELOW, ZERO;

        @Override
        public Estimator.SolType getType() {
            return this;
        }

        @Override
        public double getVal() {
            return 0.0;
        }
    }

    private static class SucEstiRes implements Estimator.EstiResult {

        private final double a;

        private final double t;

        private final Estimator mov;

        private SucEstiRes(double A, double T, Estimator m) {
            this.a = A;
            this.t = T;
            this.mov = m;
        }

        @Override
        public double getA() {
            return this.a;
        }

        @Override
        public double getT() {
            return this.t;
        }

        @Override
        public Estimator.EstiType getType() {
            return Estimator.EstiType.ZERO;
        }

        @Override
        public Vec3 getVec() {
            Vec3 fin = this.mov.dp.add(this.mov.ev.scale(this.t)).multiply(1.0, 0.0, 1.0);
            double l = fin.length();
            double c = Math.cos(this.a);
            return new Vec3(this.mov.v * c * fin.x / l, this.mov.v * Math.sin(this.a), this.mov.v * c * fin.z / l);
        }
    }

    private static class SucSolRes implements Estimator.SolResult {

        private final double val;

        private SucSolRes(double value) {
            this.val = value;
        }

        @Override
        public Estimator.SolType getType() {
            return Estimator.SolType.ZERO;
        }

        @Override
        public double getVal() {
            return this.val;
        }
    }
}