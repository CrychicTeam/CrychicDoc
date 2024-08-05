package com.corosus.coroutil.util;

import java.util.List;
import net.minecraft.world.phys.Vec3;

public class CoroUtilPhysics {

    public static boolean isInConvexShape(Vec3 test, List<Vec3> nodes) {
        boolean result = false;
        int i = 0;
        for (int j = nodes.size() - 1; i < nodes.size(); j = i++) {
            Vec3 vecI = (Vec3) nodes.get(i);
            Vec3 vecJ = (Vec3) nodes.get(j);
            if (vecI.z > test.z != vecJ.z > test.z && test.x < (vecJ.x - vecI.x) * (test.z - vecI.z) / (vecJ.z - vecI.z) + vecI.x) {
                result = !result;
            }
        }
        return result;
    }

    public static double getDistanceToShape(Vec3 point, List<Vec3> nodes) {
        float closestDist1 = 9999.0F;
        float closestDist2 = 9999.0F;
        Vec3 closestPoint1 = null;
        Vec3 closestPoint2 = null;
        for (int i = 0; i < 2; i++) {
            for (Vec3 pointTest : nodes) {
                double dist = pointTest.distanceTo(point);
                if (dist < (double) closestDist1) {
                    closestDist1 = (float) dist;
                    closestPoint1 = pointTest;
                } else if (dist < (double) closestDist2 && pointTest != closestPoint1) {
                    closestDist2 = (float) dist;
                    closestPoint2 = pointTest;
                }
            }
        }
        return closestPoint1 != null && closestPoint2 != null ? distBetweenPointAndLine(point.x, point.z, closestPoint1.x, closestPoint1.z, closestPoint2.x, closestPoint2.z) : -1.0;
    }

    public static double distBetweenPointAndLine(double x, double y, double x1, double y1, double x2, double y2) {
        double AB = distBetween(x, y, x1, y1);
        double BC = distBetween(x1, y1, x2, y2);
        double AC = distBetween(x, y, x2, y2);
        double s = (AB + BC + AC) / 2.0;
        double area = Math.sqrt(s * (s - AB) * (s - BC) * (s - AC));
        return 2.0 * area / BC;
    }

    public static double distBetween(double x, double y, double x1, double y1) {
        double xx = x1 - x;
        double yy = y1 - y;
        return Math.sqrt(xx * xx + yy * yy);
    }
}