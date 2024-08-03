package net.minecraft.world.entity.ai.util;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.Vec3;

public class AirRandomPos {

    @Nullable
    public static Vec3 getPosTowards(PathfinderMob pathfinderMob0, int int1, int int2, int int3, Vec3 vec4, double double5) {
        Vec3 $$6 = vec4.subtract(pathfinderMob0.m_20185_(), pathfinderMob0.m_20186_(), pathfinderMob0.m_20189_());
        boolean $$7 = GoalUtils.mobRestricted(pathfinderMob0, int1);
        return RandomPos.generateRandomPos(pathfinderMob0, () -> {
            BlockPos $$7x = AirAndWaterRandomPos.generateRandomPos(pathfinderMob0, int1, int2, int3, $$6.x, $$6.z, double5, $$7);
            return $$7x != null && !GoalUtils.isWater(pathfinderMob0, $$7x) ? $$7x : null;
        });
    }
}