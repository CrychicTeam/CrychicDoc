package net.mehvahdjukaar.supplementaries.client.cannon;

import com.mojang.datafixers.util.Pair;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public record CannonTrajectory(Vec2 point, float pitch, double finalTime, boolean miss, float gravity, float drag, float v0x, float v0y) {

    public static CannonTrajectory of(Vec2 point, float pitch, double finalTime, boolean miss, float gravity, float drag, float pow) {
        return new CannonTrajectory(point, pitch, finalTime, miss, gravity, drag, (float) (Math.cos((double) pitch) * (double) pow), (float) (Math.sin((double) pitch) * (double) pow));
    }

    @Nullable
    public static CannonTrajectory findBest(Vec2 targetPoint, float gravity, float drag, float initialPow, boolean preferShootingDown, float minPitch, float maxPitch) {
        double targetAngle = Math.atan2((double) targetPoint.y, (double) targetPoint.x);
        if (gravity == 0.0F) {
            float v0x = Mth.cos((float) targetAngle) * initialPow;
            float v0y = Mth.sin((float) targetAngle) * initialPow;
            if (drag == 0.0F) {
                return new CannonTrajectory(targetPoint, (float) targetAngle, 20.0, false, gravity, drag, v0x, v0y);
            } else {
                float finalDist = targetPoint.length();
                double ld = Math.log((double) drag);
                double arg = 1.0 + (double) finalDist * ld / (double) initialPow;
                boolean miss = false;
                double t;
                if (arg < 0.0) {
                    miss = true;
                    t = Math.log(0.4 / (double) initialPow) / Math.log((double) drag);
                } else {
                    t = Math.log(arg) / ld;
                }
                float arcx = (float) arcX(t, gravity, drag, v0x);
                float arcy = (float) arcY(t, gravity, drag, v0y);
                Vec2 pointHit = new Vec2(arcx, arcy);
                return new CannonTrajectory(pointHit, (float) targetAngle, t, miss, gravity, drag, v0x, v0y);
            }
        } else if (initialPow == 0.0F) {
            return null;
        } else {
            float tolerance = 0.001F;
            float start = (float) targetAngle + 0.01F;
            float end = (float) (Math.PI / 2);
            Vec2 farAway = targetPoint.scale(1000.0F);
            CannonTrajectory furthestTrajectory = findBestTrajectoryGoldenSection(farAway, gravity, drag, initialPow, 0.01F, tolerance, start, end);
            float peakAngle = furthestTrajectory.pitch();
            CannonTrajectory solution;
            if (preferShootingDown && minPitch < peakAngle) {
                solution = findBestTrajectoryGoldenSection(targetPoint, gravity, drag, initialPow, 0.001F, tolerance, Math.max(start, minPitch), Math.min(peakAngle, maxPitch));
            } else {
                solution = findBestTrajectoryGoldenSection(targetPoint, gravity, drag, initialPow, 0.001F, tolerance, Math.max(peakAngle, minPitch), Math.min(end, maxPitch));
            }
            return solution;
        }
    }

    private static CannonTrajectory findBestTrajectoryBruteForce(float step, Vec2 targetPoint, float gravity, float drag, float initialPow) {
        boolean exitEarly = true;
        float stopDistance = 0.01F;
        float targetSlope = targetPoint.y / targetPoint.x;
        float start = (float) (180.0F / (float) Math.PI * Mth.atan2((double) targetPoint.y, (double) targetPoint.x)) + 0.01F;
        float end = 90.0F;
        Vec2 bestPoint = new Vec2(0.0F, 0.0F);
        float bestAngle = start;
        double bestPointTime = 0.0;
        float bestV0x = 0.0F;
        float bestV0y = 0.0F;
        boolean miss = true;
        float angle = start;
        while (angle < end) {
            float rad = angle * (float) (Math.PI / 180.0);
            float v0x = Mth.cos(rad) * initialPow;
            float v0y = Mth.sin(rad) * initialPow;
            Pair<Vec2, Double> r = findLineIntersection(targetSlope, gravity, drag, v0x, v0y, stopDistance);
            if (r != null) {
                Vec2 landPoint = (Vec2) r.getFirst();
                float landDist = targetPoint.distanceToSqr(landPoint);
                float lastBestDist = targetPoint.distanceToSqr(bestPoint);
                if (landDist < lastBestDist) {
                    bestPoint = landPoint;
                    bestAngle = rad;
                    bestPointTime = (Double) r.getSecond();
                    bestV0x = v0x;
                    bestV0y = v0y;
                    if (landDist < stopDistance) {
                        miss = false;
                        bestPoint = targetPoint;
                        if (exitEarly) {
                            break;
                        }
                    }
                }
            } else {
                Supplementaries.error();
            }
            angle += step;
        }
        return new CannonTrajectory(bestPoint, bestAngle, bestPointTime, miss, gravity, drag, bestV0x, bestV0y);
    }

    private static CannonTrajectory findBestTrajectorySecant(Vec2 targetPoint, float gravity, float drag, float initialPow, float tolerance, int maxIterations) {
        float targetSlope = targetPoint.y / targetPoint.x;
        float startAngle = (float) Math.atan2((double) targetPoint.y, (double) targetPoint.x) + 0.01F;
        float endAngle = (float) (Math.PI / 2);
        float angle1 = startAngle;
        float angle2 = endAngle;
        Vec2 bestPoint = new Vec2(0.0F, 0.0F);
        float bestAngle = startAngle;
        double bestPointTime = 0.0;
        float bestV0x = 0.0F;
        float bestV0y = 0.0F;
        boolean miss = true;
        float bestDistance = Float.MAX_VALUE;
        for (int i = 0; i < maxIterations; i++) {
            float v0x1 = (float) (Math.cos((double) angle1) * (double) initialPow);
            float v0y1 = (float) (Math.sin((double) angle1) * (double) initialPow);
            float v0x2 = (float) (Math.cos((double) angle2) * (double) initialPow);
            float v0y2 = (float) (Math.sin((double) angle2) * (double) initialPow);
            Pair<Vec2, Double> r1 = findLineIntersection(targetSlope, gravity, drag, v0x1, v0y1, tolerance);
            Pair<Vec2, Double> r2 = findLineIntersection(targetSlope, gravity, drag, v0x2, v0y2, tolerance);
            float distance1 = r1 != null ? targetPoint.distanceToSqr((Vec2) r1.getFirst()) : Float.MAX_VALUE;
            float distance2 = r2 != null ? targetPoint.distanceToSqr((Vec2) r2.getFirst()) : Float.MAX_VALUE;
            float distToTarget;
            if (distance1 < distance2) {
                bestPoint = (Vec2) r1.getFirst();
                bestAngle = angle1;
                bestPointTime = (Double) r1.getSecond();
                bestV0x = v0x1;
                bestV0y = v0y1;
                angle2 = angle1;
                angle1 -= tolerance;
                distToTarget = distance1;
            } else {
                bestPoint = (Vec2) r2.getFirst();
                bestAngle = angle2;
                bestPointTime = (Double) r2.getSecond();
                bestV0x = v0x2;
                bestV0y = v0y2;
                angle1 = angle2;
                angle2 += tolerance;
                distToTarget = distance2;
            }
            float distanceIncrease = Math.abs(distance1 - distance2);
            if (distanceIncrease < tolerance) {
                break;
            }
            if (distToTarget < tolerance) {
                miss = false;
                bestPoint = targetPoint;
                break;
            }
        }
        return new CannonTrajectory(bestPoint, bestAngle, bestPointTime, miss, gravity, drag, bestV0x, bestV0y);
    }

    private static CannonTrajectory findBestTrajectoryGoldenSection(Vec2 targetPoint, float gravity, float drag, float initialPow, float angleTolerance, float tolerance, float start, float end) {
        float targetSlope = targetPoint.y / targetPoint.x;
        float goldenRatio = MthUtils.PHI - 1.0F;
        Vec2 bestPoint = new Vec2(0.0F, 0.0F);
        float bestAngle = start;
        double bestPointTime = 0.0;
        float bestV0x = 0.0F;
        float bestV0y = 0.0F;
        boolean miss = true;
        float startAngle = start;
        float endAngle = end;
        float midAngle1 = start + goldenRatio * (end - start);
        float midAngle2 = end - goldenRatio * (end - start);
        int iterNumber = 0;
        while (Math.abs(endAngle - startAngle) > angleTolerance) {
            iterNumber++;
            float v0x1 = (float) (Math.cos((double) midAngle1) * (double) initialPow);
            float v0y1 = (float) (Math.sin((double) midAngle1) * (double) initialPow);
            float v0x2 = (float) (Math.cos((double) midAngle2) * (double) initialPow);
            float v0y2 = (float) (Math.sin((double) midAngle2) * (double) initialPow);
            Pair<Vec2, Double> r1 = findLineIntersection(targetSlope, gravity, drag, v0x1, v0y1, tolerance);
            Pair<Vec2, Double> r2 = findLineIntersection(targetSlope, gravity, drag, v0x2, v0y2, tolerance);
            float distance1 = r1 != null ? targetPoint.distanceToSqr((Vec2) r1.getFirst()) : Float.MAX_VALUE;
            float distance2 = r2 != null ? targetPoint.distanceToSqr((Vec2) r2.getFirst()) : Float.MAX_VALUE;
            if (midAngle1 < midAngle2) {
                Supplementaries.error();
            }
            float lastBestDist = targetPoint.distanceToSqr(bestPoint);
            if (distance1 < distance2) {
                bestPoint = (Vec2) r1.getFirst();
                bestAngle = midAngle1;
                bestPointTime = (Double) r1.getSecond();
                bestV0x = v0x1;
                bestV0y = v0y1;
                if (distance1 > lastBestDist && iterNumber != 1) {
                    Supplementaries.error();
                }
                startAngle = midAngle2;
                midAngle2 = midAngle1;
                midAngle1 = startAngle + goldenRatio * (endAngle - startAngle);
            } else {
                bestPoint = (Vec2) r2.getFirst();
                bestAngle = midAngle2;
                bestPointTime = (Double) r2.getSecond();
                bestV0x = v0x2;
                bestV0y = v0y2;
                if (distance2 > lastBestDist && iterNumber != 1) {
                    Supplementaries.error();
                }
                endAngle = midAngle1;
                midAngle1 = midAngle2;
                midAngle2 = endAngle - goldenRatio * (endAngle - startAngle);
            }
            if (endAngle < startAngle) {
                Supplementaries.error();
            }
            if (lastBestDist < tolerance * 10.0F) {
                bestPoint = targetPoint;
                miss = false;
                break;
            }
        }
        return new CannonTrajectory(bestPoint, bestAngle, bestPointTime, miss, gravity, drag, bestV0x, bestV0y);
    }

    private static Pair<Vec2, Double> findLineIntersection(float m, float g, float d, float V0x, float V0y, float precision) {
        return findLineIntersectionBisection(m, g, d, V0x, V0y, precision);
    }

    private static Pair<Vec2, Double> findLineIntersectionSecant(float m, float g, float d, float V0x, float V0y) {
        float slopeAt0 = V0y / V0x;
        if (slopeAt0 < m) {
            return null;
        } else {
            float tolerance = 0.01F;
            int maxIterations = 20;
            double t1 = 20.0;
            double t2 = 50000.0;
            double x1 = arcX(t1, g, d, V0x);
            double x2 = arcX(t2, g, d, V0x);
            double y1 = arcY(t1, g, d, V0y);
            double y2 = arcY(t2, g, d, V0y);
            double xNew = 0.0;
            double yNew = 0.0;
            double tNew = 0.0;
            for (int iter = 0; iter < maxIterations && t1 != t2; iter++) {
                tNew = t2 - (y2 - (double) m * x2) * (t2 - t1) / (y2 - y1 - (double) m * (x2 - x1));
                if (!Double.isFinite(tNew)) {
                    break;
                }
                xNew = arcX(tNew, g, d, V0x);
                yNew = arcY(tNew, g, d, V0y);
                double error = yNew - (double) m * xNew;
                if (Math.abs(error) < (double) tolerance) {
                    break;
                }
                t1 = t2;
                t2 = tNew;
                x1 = x2;
                x2 = xNew;
                y1 = y2;
                y2 = yNew;
            }
            if (tNew < 0.0) {
                boolean var29 = false;
            }
            return Pair.of(new Vec2((float) xNew, (float) yNew), tNew);
        }
    }

    private static Pair<Vec2, Double> findLineIntersectionBisection(float m, float g, float d, float V0x, float V0y, float precision) {
        float slopeAt0 = V0y / V0x;
        if (slopeAt0 < m) {
            Supplementaries.error();
            return null;
        } else {
            double low = 0.0;
            double high = 1000.0;
            int iter = 0;
            int maxIter = 50;
            while (iter++ < maxIter) {
                double midTime = (low + high) / 2.0;
                double yNew = arcY(midTime, g, d, V0y);
                double xNew = arcX(midTime, g, d, V0x);
                double yLine = (double) m * xNew;
                if (Math.abs(yNew - yLine) < (double) precision) {
                    return Pair.of(new Vec2((float) xNew, (float) yNew), midTime);
                }
                if (yNew > yLine) {
                    low = midTime;
                } else {
                    high = midTime;
                }
            }
            Supplementaries.error();
            return null;
        }
    }

    private static void projectileEquation() {
    }

    public static double arcY(double t, float g, float d, float V0y) {
        float k = g / (d - 1.0F);
        double inLog = 1.0 / Math.log((double) d);
        return (double) (V0y - k) * inLog * (Math.pow((double) d, t) - 1.0) + (double) k * t;
    }

    public static double arcX(double t, float g, float d, float V0x) {
        double inLog = 1.0 / Math.log((double) d);
        return (double) V0x * inLog * (Math.pow((double) d, t) - 1.0);
    }

    public double getX(double t) {
        return arcX(t, this.gravity, this.drag, this.v0x);
    }

    public double getY(double t) {
        return arcY(t, this.gravity, this.drag, this.v0y);
    }

    public BlockPos getHitPos(BlockPos cannonPos, float yaw) {
        Vec2 v = this.point;
        Vec3 localPos = new Vec3(0.0, (double) (v.y - 1.0F), (double) (-v.x)).yRot(-yaw);
        return BlockPos.containing(cannonPos.getCenter().add(localPos));
    }
}