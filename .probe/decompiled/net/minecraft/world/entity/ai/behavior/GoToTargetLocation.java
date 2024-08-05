package net.minecraft.world.entity.ai.behavior;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class GoToTargetLocation {

    private static BlockPos getNearbyPos(Mob mob0, BlockPos blockPos1) {
        RandomSource $$2 = mob0.m_9236_().random;
        return blockPos1.offset(getRandomOffset($$2), 0, getRandomOffset($$2));
    }

    private static int getRandomOffset(RandomSource randomSource0) {
        return randomSource0.nextInt(3) - 1;
    }

    public static <E extends Mob> OneShot<E> create(MemoryModuleType<BlockPos> memoryModuleTypeBlockPos0, int int1, float float2) {
        return BehaviorBuilder.create(p_259997_ -> p_259997_.group(p_259997_.present(memoryModuleTypeBlockPos0), p_259997_.absent(MemoryModuleType.ATTACK_TARGET), p_259997_.absent(MemoryModuleType.WALK_TARGET), p_259997_.registered(MemoryModuleType.LOOK_TARGET)).apply(p_259997_, (p_259831_, p_259115_, p_259521_, p_259223_) -> (p_289322_, p_289323_, p_289324_) -> {
            BlockPos $$7 = p_259997_.get(p_259831_);
            boolean $$8 = $$7.m_123314_(p_289323_.m_20183_(), (double) int1);
            if (!$$8) {
                BehaviorUtils.setWalkAndLookTargetMemories(p_289323_, getNearbyPos(p_289323_, $$7), float2, int1);
            }
            return true;
        }));
    }
}