package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SleepInBed extends Behavior<LivingEntity> {

    public static final int COOLDOWN_AFTER_BEING_WOKEN = 100;

    private long nextOkStartTime;

    public SleepInBed() {
        super(ImmutableMap.of(MemoryModuleType.HOME, MemoryStatus.VALUE_PRESENT, MemoryModuleType.LAST_WOKEN, MemoryStatus.REGISTERED));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel0, LivingEntity livingEntity1) {
        if (livingEntity1.m_20159_()) {
            return false;
        } else {
            Brain<?> $$2 = livingEntity1.getBrain();
            GlobalPos $$3 = (GlobalPos) $$2.getMemory(MemoryModuleType.HOME).get();
            if (serverLevel0.m_46472_() != $$3.dimension()) {
                return false;
            } else {
                Optional<Long> $$4 = $$2.getMemory(MemoryModuleType.LAST_WOKEN);
                if ($$4.isPresent()) {
                    long $$5 = serverLevel0.m_46467_() - (Long) $$4.get();
                    if ($$5 > 0L && $$5 < 100L) {
                        return false;
                    }
                }
                BlockState $$6 = serverLevel0.m_8055_($$3.pos());
                return $$3.pos().m_203195_(livingEntity1.m_20182_(), 2.0) && $$6.m_204336_(BlockTags.BEDS) && !(Boolean) $$6.m_61143_(BedBlock.OCCUPIED);
            }
        }
    }

    @Override
    protected boolean canStillUse(ServerLevel serverLevel0, LivingEntity livingEntity1, long long2) {
        Optional<GlobalPos> $$3 = livingEntity1.getBrain().getMemory(MemoryModuleType.HOME);
        if (!$$3.isPresent()) {
            return false;
        } else {
            BlockPos $$4 = ((GlobalPos) $$3.get()).pos();
            return livingEntity1.getBrain().isActive(Activity.REST) && livingEntity1.m_20186_() > (double) $$4.m_123342_() + 0.4 && $$4.m_203195_(livingEntity1.m_20182_(), 1.14);
        }
    }

    @Override
    protected void start(ServerLevel serverLevel0, LivingEntity livingEntity1, long long2) {
        if (long2 > this.nextOkStartTime) {
            Brain<?> $$3 = livingEntity1.getBrain();
            if ($$3.hasMemoryValue(MemoryModuleType.DOORS_TO_CLOSE)) {
                Set<GlobalPos> $$4 = (Set<GlobalPos>) $$3.getMemory(MemoryModuleType.DOORS_TO_CLOSE).get();
                Optional<List<LivingEntity>> $$5;
                if ($$3.hasMemoryValue(MemoryModuleType.NEAREST_LIVING_ENTITIES)) {
                    $$5 = $$3.getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES);
                } else {
                    $$5 = Optional.empty();
                }
                InteractWithDoor.closeDoorsThatIHaveOpenedOrPassedThrough(serverLevel0, livingEntity1, null, null, $$4, $$5);
            }
            livingEntity1.startSleeping(((GlobalPos) livingEntity1.getBrain().getMemory(MemoryModuleType.HOME).get()).pos());
        }
    }

    @Override
    protected boolean timedOut(long long0) {
        return false;
    }

    @Override
    protected void stop(ServerLevel serverLevel0, LivingEntity livingEntity1, long long2) {
        if (livingEntity1.isSleeping()) {
            livingEntity1.stopSleeping();
            this.nextOkStartTime = long2 + 40L;
        }
    }
}