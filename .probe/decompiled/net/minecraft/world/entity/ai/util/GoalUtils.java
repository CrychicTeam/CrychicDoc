package net.minecraft.world.entity.ai.util;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

public class GoalUtils {

    public static boolean hasGroundPathNavigation(Mob mob0) {
        return mob0.getNavigation() instanceof GroundPathNavigation;
    }

    public static boolean mobRestricted(PathfinderMob pathfinderMob0, int int1) {
        return pathfinderMob0.m_21536_() && pathfinderMob0.m_21534_().m_203195_(pathfinderMob0.m_20182_(), (double) (pathfinderMob0.m_21535_() + (float) int1) + 1.0);
    }

    public static boolean isOutsideLimits(BlockPos blockPos0, PathfinderMob pathfinderMob1) {
        return blockPos0.m_123342_() < pathfinderMob1.m_9236_().m_141937_() || blockPos0.m_123342_() > pathfinderMob1.m_9236_().m_151558_();
    }

    public static boolean isRestricted(boolean boolean0, PathfinderMob pathfinderMob1, BlockPos blockPos2) {
        return boolean0 && !pathfinderMob1.m_21444_(blockPos2);
    }

    public static boolean isNotStable(PathNavigation pathNavigation0, BlockPos blockPos1) {
        return !pathNavigation0.isStableDestination(blockPos1);
    }

    public static boolean isWater(PathfinderMob pathfinderMob0, BlockPos blockPos1) {
        return pathfinderMob0.m_9236_().getFluidState(blockPos1).is(FluidTags.WATER);
    }

    public static boolean hasMalus(PathfinderMob pathfinderMob0, BlockPos blockPos1) {
        return pathfinderMob0.m_21439_(WalkNodeEvaluator.getBlockPathTypeStatic(pathfinderMob0.m_9236_(), blockPos1.mutable())) != 0.0F;
    }

    public static boolean isSolid(PathfinderMob pathfinderMob0, BlockPos blockPos1) {
        return pathfinderMob0.m_9236_().getBlockState(blockPos1).m_280296_();
    }
}