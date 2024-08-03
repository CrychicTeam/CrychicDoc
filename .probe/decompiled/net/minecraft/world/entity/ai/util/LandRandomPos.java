package net.minecraft.world.entity.ai.util;

import java.util.function.ToDoubleFunction;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.Vec3;

public class LandRandomPos {

    @Nullable
    public static Vec3 getPos(PathfinderMob pathfinderMob0, int int1, int int2) {
        return getPos(pathfinderMob0, int1, int2, pathfinderMob0::m_21692_);
    }

    @Nullable
    public static Vec3 getPos(PathfinderMob pathfinderMob0, int int1, int int2, ToDoubleFunction<BlockPos> toDoubleFunctionBlockPos3) {
        boolean $$4 = GoalUtils.mobRestricted(pathfinderMob0, int1);
        return RandomPos.generateRandomPos(() -> {
            BlockPos $$4x = RandomPos.generateRandomDirection(pathfinderMob0.m_217043_(), int1, int2);
            BlockPos $$5 = generateRandomPosTowardDirection(pathfinderMob0, int1, $$4, $$4x);
            return $$5 == null ? null : movePosUpOutOfSolid(pathfinderMob0, $$5);
        }, toDoubleFunctionBlockPos3);
    }

    @Nullable
    public static Vec3 getPosTowards(PathfinderMob pathfinderMob0, int int1, int int2, Vec3 vec3) {
        Vec3 $$4 = vec3.subtract(pathfinderMob0.m_20185_(), pathfinderMob0.m_20186_(), pathfinderMob0.m_20189_());
        boolean $$5 = GoalUtils.mobRestricted(pathfinderMob0, int1);
        return getPosInDirection(pathfinderMob0, int1, int2, $$4, $$5);
    }

    @Nullable
    public static Vec3 getPosAway(PathfinderMob pathfinderMob0, int int1, int int2, Vec3 vec3) {
        Vec3 $$4 = pathfinderMob0.m_20182_().subtract(vec3);
        boolean $$5 = GoalUtils.mobRestricted(pathfinderMob0, int1);
        return getPosInDirection(pathfinderMob0, int1, int2, $$4, $$5);
    }

    @Nullable
    private static Vec3 getPosInDirection(PathfinderMob pathfinderMob0, int int1, int int2, Vec3 vec3, boolean boolean4) {
        return RandomPos.generateRandomPos(pathfinderMob0, () -> {
            BlockPos $$5 = RandomPos.generateRandomDirectionWithinRadians(pathfinderMob0.m_217043_(), int1, int2, 0, vec3.x, vec3.z, (float) (Math.PI / 2));
            if ($$5 == null) {
                return null;
            } else {
                BlockPos $$6 = generateRandomPosTowardDirection(pathfinderMob0, int1, boolean4, $$5);
                return $$6 == null ? null : movePosUpOutOfSolid(pathfinderMob0, $$6);
            }
        });
    }

    @Nullable
    public static BlockPos movePosUpOutOfSolid(PathfinderMob pathfinderMob0, BlockPos blockPos1) {
        blockPos1 = RandomPos.moveUpOutOfSolid(blockPos1, pathfinderMob0.m_9236_().m_151558_(), p_148534_ -> GoalUtils.isSolid(pathfinderMob0, p_148534_));
        return !GoalUtils.isWater(pathfinderMob0, blockPos1) && !GoalUtils.hasMalus(pathfinderMob0, blockPos1) ? blockPos1 : null;
    }

    @Nullable
    public static BlockPos generateRandomPosTowardDirection(PathfinderMob pathfinderMob0, int int1, boolean boolean2, BlockPos blockPos3) {
        BlockPos $$4 = RandomPos.generateRandomPosTowardDirection(pathfinderMob0, int1, pathfinderMob0.m_217043_(), blockPos3);
        return !GoalUtils.isOutsideLimits($$4, pathfinderMob0) && !GoalUtils.isRestricted(boolean2, pathfinderMob0, $$4) && !GoalUtils.isNotStable(pathfinderMob0.m_21573_(), $$4) ? $$4 : null;
    }
}