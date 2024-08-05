package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class LookAtTargetSink extends Behavior<Mob> {

    public LookAtTargetSink(int int0, int int1) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_PRESENT), int0, int1);
    }

    protected boolean canStillUse(ServerLevel serverLevel0, Mob mob1, long long2) {
        return mob1.m_6274_().getMemory(MemoryModuleType.LOOK_TARGET).filter(p_23497_ -> p_23497_.isVisibleBy(mob1)).isPresent();
    }

    protected void stop(ServerLevel serverLevel0, Mob mob1, long long2) {
        mob1.m_6274_().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }

    protected void tick(ServerLevel serverLevel0, Mob mob1, long long2) {
        mob1.m_6274_().getMemory(MemoryModuleType.LOOK_TARGET).ifPresent(p_23486_ -> mob1.getLookControl().setLookAt(p_23486_.currentPosition()));
    }
}