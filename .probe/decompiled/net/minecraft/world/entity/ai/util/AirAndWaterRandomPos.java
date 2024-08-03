package net.minecraft.world.entity.ai.util;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.Vec3;

public class AirAndWaterRandomPos {

    @Nullable
    public static Vec3 getPos(PathfinderMob pathfinderMob0, int int1, int int2, int int3, double double4, double double5, double double6) {
        boolean $$7 = GoalUtils.mobRestricted(pathfinderMob0, int1);
        return RandomPos.generateRandomPos(pathfinderMob0, () -> generateRandomPos(pathfinderMob0, int1, int2, int3, double4, double5, double6, $$7));
    }

    @Nullable
    public static BlockPos generateRandomPos(PathfinderMob pathfinderMob0, int int1, int int2, int int3, double double4, double double5, double double6, boolean boolean7) {
        BlockPos $$8 = RandomPos.generateRandomDirectionWithinRadians(pathfinderMob0.m_217043_(), int1, int2, int3, double4, double5, double6);
        if ($$8 == null) {
            return null;
        } else {
            BlockPos $$9 = RandomPos.generateRandomPosTowardDirection(pathfinderMob0, int1, pathfinderMob0.m_217043_(), $$8);
            if (!GoalUtils.isOutsideLimits($$9, pathfinderMob0) && !GoalUtils.isRestricted(boolean7, pathfinderMob0, $$9)) {
                $$9 = RandomPos.moveUpOutOfSolid($$9, pathfinderMob0.m_9236_().m_151558_(), p_148376_ -> GoalUtils.isSolid(pathfinderMob0, p_148376_));
                return GoalUtils.hasMalus(pathfinderMob0, $$9) ? null : $$9;
            } else {
                return null;
            }
        }
    }
}