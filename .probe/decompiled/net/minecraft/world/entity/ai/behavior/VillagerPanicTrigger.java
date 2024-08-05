package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.schedule.Activity;

public class VillagerPanicTrigger extends Behavior<Villager> {

    public VillagerPanicTrigger() {
        super(ImmutableMap.of());
    }

    protected boolean canStillUse(ServerLevel serverLevel0, Villager villager1, long long2) {
        return isHurt(villager1) || hasHostile(villager1);
    }

    protected void start(ServerLevel serverLevel0, Villager villager1, long long2) {
        if (isHurt(villager1) || hasHostile(villager1)) {
            Brain<?> $$3 = villager1.getBrain();
            if (!$$3.isActive(Activity.PANIC)) {
                $$3.eraseMemory(MemoryModuleType.PATH);
                $$3.eraseMemory(MemoryModuleType.WALK_TARGET);
                $$3.eraseMemory(MemoryModuleType.LOOK_TARGET);
                $$3.eraseMemory(MemoryModuleType.BREED_TARGET);
                $$3.eraseMemory(MemoryModuleType.INTERACTION_TARGET);
            }
            $$3.setActiveActivityIfPossible(Activity.PANIC);
        }
    }

    protected void tick(ServerLevel serverLevel0, Villager villager1, long long2) {
        if (long2 % 100L == 0L) {
            villager1.spawnGolemIfNeeded(serverLevel0, long2, 3);
        }
    }

    public static boolean hasHostile(LivingEntity livingEntity0) {
        return livingEntity0.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_HOSTILE);
    }

    public static boolean isHurt(LivingEntity livingEntity0) {
        return livingEntity0.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY);
    }
}