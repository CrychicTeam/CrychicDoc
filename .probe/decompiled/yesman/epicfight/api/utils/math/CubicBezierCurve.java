package yesman.epicfight.api.utils.math;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.world.phys.Vec3;

public class CubicBezierCurve {

    private static final List<Double> MATRIX_CONSTANTS = Lists.newArrayList();

    private static void getBezierEquationCoefficients(List<Double> points, List<Double> aList, List<Double> bList) {
        List<Double> results = Lists.newArrayList();
        int size = points.size();
        results.add((Double) points.get(0) + (Double) points.get(1) * 2.0);
        for (int idx = 1; idx < size - 2; idx++) {
            results.add((Double) points.get(idx) * 4.0 + (Double) points.get(idx + 1) * 2.0);
        }
        results.add((Double) points.get(size - 2) * 8.0 + (Double) points.get(size - 1));
        int storedConstsSize = MATRIX_CONSTANTS.size();
        int coordSize = results.size();
        if (storedConstsSize < coordSize - 1) {
            for (int i = 0; i < coordSize - 1 - storedConstsSize; i++) {
                double lastConst = (Double) MATRIX_CONSTANTS.get(storedConstsSize - 1);
                MATRIX_CONSTANTS.add(1.0 / (4.0 - lastConst));
            }
        }
        List<Double> convertedResults = Lists.newArrayList();
        for (int idx = 0; idx < coordSize; idx++) {
            if (idx == 0) {
                convertedResults.add((Double) results.get(idx) * 0.5);
            } else if (idx == coordSize - 1) {
                convertedResults.add(((Double) results.get(idx) - 2.0 * (Double) convertedResults.get(idx - 1)) * (1.0 / (7.0 - (Double) MATRIX_CONSTANTS.get(idx - 1) * 2.0)));
            } else {
                convertedResults.add(((Double) results.get(idx) - (Double) convertedResults.get(idx - 1)) * (Double) MATRIX_CONSTANTS.get(idx));
            }
        }
        for (int idxx = coordSize - 1; idxx >= 0; idxx--) {
            if (idxx == coordSize - 1) {
                aList.add(0, (Double) convertedResults.get(idxx));
            } else {
                aList.add(0, (Double) convertedResults.get(idxx) - (Double) convertedResults.get(idxx + 1) * (Double) MATRIX_CONSTANTS.get(idxx));
            }
        }
        for (int i = 0; i < coordSize; i++) {
            if (i == coordSize - 1) {
                bList.add(((Double) aList.get(i) + (Double) points.get(i + 1)) * 0.5);
            } else {
                bList.add(2.0 * (Double) points.get(i + 1) - (Double) aList.get(i + 1));
            }
        }
    }

    private static double cubicBezier(double start, double end, double a, double b, double t) {
        return Math.pow(1.0 - t, 3.0) * start + 3.0 * t * Math.pow(1.0 - t, 2.0) * a + 3.0 * t * t * (1.0 - t) * b + t * t * t * end;
    }

    public static List<Vec3> getBezierInterpolatedPoints(List<Vec3> points, int interpolatedResults) {
        return getBezierInterpolatedPoints(points, 0, points.size() - 1, interpolatedResults);
    }

    public static List<Vec3> getBezierInterpolatedPoints(List<Vec3> points, int sliceBegin, int sliceEnd, int interpolatedResults) {
        if (points.size() < 3) {
            return null;
        } else {
            sliceBegin = Math.max(sliceBegin, 0);
            sliceEnd = Math.min(sliceEnd, points.size() - 1);
            int size = points.size();
            List<Vec3> interpolatedPoints = Lists.newArrayList();
            List<Double> x = Lists.newArrayList();
            List<Double> y = Lists.newArrayList();
            List<Double> z = Lists.newArrayList();
            for (int idx = 0; idx < size; idx++) {
                x.add(((Vec3) points.get(idx)).x);
                y.add(((Vec3) points.get(idx)).y);
                z.add(((Vec3) points.get(idx)).z);
            }
            List<Double> x_a = Lists.newArrayList();
            List<Double> x_b = Lists.newArrayList();
            List<Double> y_a = Lists.newArrayList();
            List<Double> y_b = Lists.newArrayList();
            List<Double> z_a = Lists.newArrayList();
            List<Double> z_b = Lists.newArrayList();
            getBezierEquationCoefficients(x, x_a, x_b);
            getBezierEquationCoefficients(y, y_a, y_b);
            getBezierEquationCoefficients(z, z_a, z_b);
            for (int i = sliceBegin; i < sliceEnd; i++) {
                if (interpolatedPoints.size() > 0) {
                    interpolatedPoints.remove(interpolatedPoints.size() - 1);
                }
                Vec3 start = (Vec3) points.get(i);
                Vec3 end = (Vec3) points.get(i + 1);
                double x_av = (Double) x_a.get(i);
                double x_bv = (Double) x_b.get(i);
                double y_av = (Double) y_a.get(i);
                double y_bv = (Double) y_b.get(i);
                double z_av = (Double) z_a.get(i);
                double z_bv = (Double) z_b.get(i);
                for (int j = 0; j < interpolatedResults + 1; j++) {
                    double t = (double) j / (double) interpolatedResults;
                    interpolatedPoints.add(new Vec3(cubicBezier(start.x, end.x, x_av, x_bv, t), cubicBezier(start.y, end.y, y_av, y_bv, t), cubicBezier(start.z, end.z, z_av, z_bv, t)));
                }
            }
            return interpolatedPoints;
        }
    }

    static {
        MATRIX_CONSTANTS.add(0.5);
    }
}