package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import java.util.Optional;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.npc.Villager;

public class WorkAtPoi extends Behavior<Villager> {

    private static final int CHECK_COOLDOWN = 300;

    private static final double DISTANCE = 1.73;

    private long lastCheck;

    public WorkAtPoi() {
        super(ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED));
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel0, Villager villager1) {
        if (serverLevel0.m_46467_() - this.lastCheck < 300L) {
            return false;
        } else if (serverLevel0.f_46441_.nextInt(2) != 0) {
            return false;
        } else {
            this.lastCheck = serverLevel0.m_46467_();
            GlobalPos $$2 = (GlobalPos) villager1.getBrain().getMemory(MemoryModuleType.JOB_SITE).get();
            return $$2.dimension() == serverLevel0.m_46472_() && $$2.pos().m_203195_(villager1.m_20182_(), 1.73);
        }
    }

    protected void start(ServerLevel serverLevel0, Villager villager1, long long2) {
        Brain<Villager> $$3 = villager1.getBrain();
        $$3.setMemory(MemoryModuleType.LAST_WORKED_AT_POI, long2);
        $$3.getMemory(MemoryModuleType.JOB_SITE).ifPresent(p_24821_ -> $$3.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(p_24821_.pos())));
        villager1.playWorkSound();
        this.useWorkstation(serverLevel0, villager1);
        if (villager1.shouldRestock()) {
            villager1.restock();
        }
    }

    protected void useWorkstation(ServerLevel serverLevel0, Villager villager1) {
    }

    protected boolean canStillUse(ServerLevel serverLevel0, Villager villager1, long long2) {
        Optional<GlobalPos> $$3 = villager1.getBrain().getMemory(MemoryModuleType.JOB_SITE);
        if (!$$3.isPresent()) {
            return false;
        } else {
            GlobalPos $$4 = (GlobalPos) $$3.get();
            return $$4.dimension() == serverLevel0.m_46472_() && $$4.pos().m_203195_(villager1.m_20182_(), 1.73);
        }
    }
}