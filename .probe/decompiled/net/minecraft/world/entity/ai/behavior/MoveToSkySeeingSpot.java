package net.minecraft.world.entity.ai.behavior;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

public class MoveToSkySeeingSpot {

    public static OneShot<LivingEntity> create(float float0) {
        return BehaviorBuilder.create(p_258543_ -> p_258543_.group(p_258543_.absent(MemoryModuleType.WALK_TARGET)).apply(p_258543_, p_258545_ -> (p_289365_, p_289366_, p_289367_) -> {
            if (p_289365_.m_45527_(p_289366_.m_20183_())) {
                return false;
            } else {
                Optional<Vec3> $$5 = Optional.ofNullable(getOutdoorPosition(p_289365_, p_289366_));
                $$5.ifPresent(p_258548_ -> p_258545_.set(new WalkTarget(p_258548_, float0, 0)));
                return true;
            }
        }));
    }

    @Nullable
    private static Vec3 getOutdoorPosition(ServerLevel serverLevel0, LivingEntity livingEntity1) {
        RandomSource $$2 = livingEntity1.getRandom();
        BlockPos $$3 = livingEntity1.m_20183_();
        for (int $$4 = 0; $$4 < 10; $$4++) {
            BlockPos $$5 = $$3.offset($$2.nextInt(20) - 10, $$2.nextInt(6) - 3, $$2.nextInt(20) - 10);
            if (hasNoBlocksAbove(serverLevel0, livingEntity1, $$5)) {
                return Vec3.atBottomCenterOf($$5);
            }
        }
        return null;
    }

    public static boolean hasNoBlocksAbove(ServerLevel serverLevel0, LivingEntity livingEntity1, BlockPos blockPos2) {
        return serverLevel0.m_45527_(blockPos2) && (double) serverLevel0.m_5452_(Heightmap.Types.MOTION_BLOCKING, blockPos2).m_123342_() <= livingEntity1.m_20186_();
    }
}