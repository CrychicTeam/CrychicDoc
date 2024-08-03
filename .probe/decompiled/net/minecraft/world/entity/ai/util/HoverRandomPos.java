package net.minecraft.world.entity.ai.util;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.Vec3;

public class HoverRandomPos {

    @Nullable
    public static Vec3 getPos(PathfinderMob pathfinderMob0, int int1, int int2, double double3, double double4, float float5, int int6, int int7) {
        boolean $$8 = GoalUtils.mobRestricted(pathfinderMob0, int1);
        return RandomPos.generateRandomPos(pathfinderMob0, () -> {
            BlockPos $$9 = RandomPos.generateRandomDirectionWithinRadians(pathfinderMob0.m_217043_(), int1, int2, 0, double3, double4, (double) float5);
            if ($$9 == null) {
                return null;
            } else {
                BlockPos $$10 = LandRandomPos.generateRandomPosTowardDirection(pathfinderMob0, int1, $$8, $$9);
                if ($$10 == null) {
                    return null;
                } else {
                    $$10 = RandomPos.moveUpToAboveSolid($$10, pathfinderMob0.m_217043_().nextInt(int6 - int7 + 1) + int7, pathfinderMob0.m_9236_().m_151558_(), p_148486_ -> GoalUtils.isSolid(pathfinderMob0, p_148486_));
                    return !GoalUtils.isWater(pathfinderMob0, $$10) && !GoalUtils.hasMalus(pathfinderMob0, $$10) ? $$10 : null;
                }
            }
        });
    }
}