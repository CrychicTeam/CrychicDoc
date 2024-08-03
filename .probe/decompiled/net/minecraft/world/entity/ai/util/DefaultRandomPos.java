package net.minecraft.world.entity.ai.util;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.Vec3;

public class DefaultRandomPos {

    @Nullable
    public static Vec3 getPos(PathfinderMob pathfinderMob0, int int1, int int2) {
        boolean $$3 = GoalUtils.mobRestricted(pathfinderMob0, int1);
        return RandomPos.generateRandomPos(pathfinderMob0, () -> {
            BlockPos $$4 = RandomPos.generateRandomDirection(pathfinderMob0.m_217043_(), int1, int2);
            return generateRandomPosTowardDirection(pathfinderMob0, int1, $$3, $$4);
        });
    }

    @Nullable
    public static Vec3 getPosTowards(PathfinderMob pathfinderMob0, int int1, int int2, Vec3 vec3, double double4) {
        Vec3 $$5 = vec3.subtract(pathfinderMob0.m_20185_(), pathfinderMob0.m_20186_(), pathfinderMob0.m_20189_());
        boolean $$6 = GoalUtils.mobRestricted(pathfinderMob0, int1);
        return RandomPos.generateRandomPos(pathfinderMob0, () -> {
            BlockPos $$6x = RandomPos.generateRandomDirectionWithinRadians(pathfinderMob0.m_217043_(), int1, int2, 0, $$5.x, $$5.z, double4);
            return $$6x == null ? null : generateRandomPosTowardDirection(pathfinderMob0, int1, $$6, $$6x);
        });
    }

    @Nullable
    public static Vec3 getPosAway(PathfinderMob pathfinderMob0, int int1, int int2, Vec3 vec3) {
        Vec3 $$4 = pathfinderMob0.m_20182_().subtract(vec3);
        boolean $$5 = GoalUtils.mobRestricted(pathfinderMob0, int1);
        return RandomPos.generateRandomPos(pathfinderMob0, () -> {
            BlockPos $$5x = RandomPos.generateRandomDirectionWithinRadians(pathfinderMob0.m_217043_(), int1, int2, 0, $$4.x, $$4.z, (float) (Math.PI / 2));
            return $$5x == null ? null : generateRandomPosTowardDirection(pathfinderMob0, int1, $$5, $$5x);
        });
    }

    @Nullable
    private static BlockPos generateRandomPosTowardDirection(PathfinderMob pathfinderMob0, int int1, boolean boolean2, BlockPos blockPos3) {
        BlockPos $$4 = RandomPos.generateRandomPosTowardDirection(pathfinderMob0, int1, pathfinderMob0.m_217043_(), blockPos3);
        return !GoalUtils.isOutsideLimits($$4, pathfinderMob0) && !GoalUtils.isRestricted(boolean2, pathfinderMob0, $$4) && !GoalUtils.isNotStable(pathfinderMob0.m_21573_(), $$4) && !GoalUtils.hasMalus(pathfinderMob0, $$4) ? $$4 : null;
    }
}