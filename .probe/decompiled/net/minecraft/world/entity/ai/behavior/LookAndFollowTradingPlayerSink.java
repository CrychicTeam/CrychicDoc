package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;

public class LookAndFollowTradingPlayerSink extends Behavior<Villager> {

    private final float speedModifier;

    public LookAndFollowTradingPlayerSink(float float0) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED), Integer.MAX_VALUE);
        this.speedModifier = float0;
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel0, Villager villager1) {
        Player $$2 = villager1.m_7962_();
        return villager1.m_6084_() && $$2 != null && !villager1.m_20069_() && !villager1.f_19864_ && villager1.m_20280_($$2) <= 16.0 && $$2.containerMenu != null;
    }

    protected boolean canStillUse(ServerLevel serverLevel0, Villager villager1, long long2) {
        return this.checkExtraStartConditions(serverLevel0, villager1);
    }

    protected void start(ServerLevel serverLevel0, Villager villager1, long long2) {
        this.followPlayer(villager1);
    }

    protected void stop(ServerLevel serverLevel0, Villager villager1, long long2) {
        Brain<?> $$3 = villager1.getBrain();
        $$3.eraseMemory(MemoryModuleType.WALK_TARGET);
        $$3.eraseMemory(MemoryModuleType.LOOK_TARGET);
    }

    protected void tick(ServerLevel serverLevel0, Villager villager1, long long2) {
        this.followPlayer(villager1);
    }

    @Override
    protected boolean timedOut(long long0) {
        return false;
    }

    private void followPlayer(Villager villager0) {
        Brain<?> $$1 = villager0.getBrain();
        $$1.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker(villager0.m_7962_(), false), this.speedModifier, 2));
        $$1.setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(villager0.m_7962_(), true));
    }
}