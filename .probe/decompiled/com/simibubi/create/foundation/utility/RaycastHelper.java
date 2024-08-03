package com.simibubi.create.foundation.utility;

import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class RaycastHelper {

    public static BlockHitResult rayTraceRange(Level worldIn, Player playerIn, double range) {
        Vec3 origin = getTraceOrigin(playerIn);
        Vec3 target = getTraceTarget(playerIn, range, origin);
        ClipContext context = new ClipContext(origin, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, playerIn);
        return worldIn.m_45547_(context);
    }

    public static RaycastHelper.PredicateTraceResult rayTraceUntil(Player playerIn, double range, Predicate<BlockPos> predicate) {
        Vec3 origin = getTraceOrigin(playerIn);
        Vec3 target = getTraceTarget(playerIn, range, origin);
        return rayTraceUntil(origin, target, predicate);
    }

    public static Vec3 getTraceTarget(Player playerIn, double range, Vec3 origin) {
        float f = playerIn.m_146909_();
        float f1 = playerIn.m_146908_();
        float f2 = Mth.cos(-f1 * (float) (Math.PI / 180.0) - (float) Math.PI);
        float f3 = Mth.sin(-f1 * (float) (Math.PI / 180.0) - (float) Math.PI);
        float f4 = -Mth.cos(-f * (float) (Math.PI / 180.0));
        float f5 = Mth.sin(-f * (float) (Math.PI / 180.0));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        return origin.add((double) f6 * range, (double) f5 * range, (double) f7 * range);
    }

    public static Vec3 getTraceOrigin(Player playerIn) {
        double d0 = playerIn.m_20185_();
        double d1 = playerIn.m_20186_() + (double) playerIn.m_20192_();
        double d2 = playerIn.m_20189_();
        return new Vec3(d0, d1, d2);
    }

    public static RaycastHelper.PredicateTraceResult rayTraceUntil(Vec3 start, Vec3 end, Predicate<BlockPos> predicate) {
        if (Double.isNaN(start.x) || Double.isNaN(start.y) || Double.isNaN(start.z)) {
            return null;
        } else if (!Double.isNaN(end.x) && !Double.isNaN(end.y) && !Double.isNaN(end.z)) {
            int dx = Mth.floor(end.x);
            int dy = Mth.floor(end.y);
            int dz = Mth.floor(end.z);
            int x = Mth.floor(start.x);
            int y = Mth.floor(start.y);
            int z = Mth.floor(start.z);
            BlockPos.MutableBlockPos currentPos = new BlockPos(x, y, z).mutable();
            if (predicate.test(currentPos)) {
                return new RaycastHelper.PredicateTraceResult(currentPos.immutable(), Direction.getNearest((float) (dx - x), (float) (dy - y), (float) (dz - z)));
            } else {
                int remainingDistance = 200;
                while (remainingDistance-- >= 0) {
                    if (Double.isNaN(start.x) || Double.isNaN(start.y) || Double.isNaN(start.z)) {
                        return null;
                    }
                    if (x == dx && y == dy && z == dz) {
                        return new RaycastHelper.PredicateTraceResult();
                    }
                    boolean flag2 = true;
                    boolean flag = true;
                    boolean flag1 = true;
                    double d0 = 999.0;
                    double d1 = 999.0;
                    double d2 = 999.0;
                    if (dx > x) {
                        d0 = (double) x + 1.0;
                    } else if (dx < x) {
                        d0 = (double) x + 0.0;
                    } else {
                        flag2 = false;
                    }
                    if (dy > y) {
                        d1 = (double) y + 1.0;
                    } else if (dy < y) {
                        d1 = (double) y + 0.0;
                    } else {
                        flag = false;
                    }
                    if (dz > z) {
                        d2 = (double) z + 1.0;
                    } else if (dz < z) {
                        d2 = (double) z + 0.0;
                    } else {
                        flag1 = false;
                    }
                    double d3 = 999.0;
                    double d4 = 999.0;
                    double d5 = 999.0;
                    double d6 = end.x - start.x;
                    double d7 = end.y - start.y;
                    double d8 = end.z - start.z;
                    if (flag2) {
                        d3 = (d0 - start.x) / d6;
                    }
                    if (flag) {
                        d4 = (d1 - start.y) / d7;
                    }
                    if (flag1) {
                        d5 = (d2 - start.z) / d8;
                    }
                    if (d3 == -0.0) {
                        d3 = -1.0E-4;
                    }
                    if (d4 == -0.0) {
                        d4 = -1.0E-4;
                    }
                    if (d5 == -0.0) {
                        d5 = -1.0E-4;
                    }
                    Direction enumfacing;
                    if (d3 < d4 && d3 < d5) {
                        enumfacing = dx > x ? Direction.WEST : Direction.EAST;
                        start = new Vec3(d0, start.y + d7 * d3, start.z + d8 * d3);
                    } else if (d4 < d5) {
                        enumfacing = dy > y ? Direction.DOWN : Direction.UP;
                        start = new Vec3(start.x + d6 * d4, d1, start.z + d8 * d4);
                    } else {
                        enumfacing = dz > z ? Direction.NORTH : Direction.SOUTH;
                        start = new Vec3(start.x + d6 * d5, start.y + d7 * d5, d2);
                    }
                    x = Mth.floor(start.x) - (enumfacing == Direction.EAST ? 1 : 0);
                    y = Mth.floor(start.y) - (enumfacing == Direction.UP ? 1 : 0);
                    z = Mth.floor(start.z) - (enumfacing == Direction.SOUTH ? 1 : 0);
                    currentPos.set(x, y, z);
                    if (predicate.test(currentPos)) {
                        return new RaycastHelper.PredicateTraceResult(currentPos.immutable(), enumfacing);
                    }
                }
                return new RaycastHelper.PredicateTraceResult();
            }
        } else {
            return null;
        }
    }

    public static class PredicateTraceResult {

        private BlockPos pos;

        private Direction facing;

        public PredicateTraceResult(BlockPos pos, Direction facing) {
            this.pos = pos;
            this.facing = facing;
        }

        public PredicateTraceResult() {
        }

        public Direction getFacing() {
            return this.facing;
        }

        public BlockPos getPos() {
            return this.pos;
        }

        public boolean missed() {
            return this.pos == null;
        }
    }
}