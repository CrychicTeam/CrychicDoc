package net.minecraft.world.entity.ai.behavior.warden;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.monster.warden.WardenAi;

public class Roar extends Behavior<Warden> {

    private static final int TICKS_BEFORE_PLAYING_ROAR_SOUND = 25;

    private static final int ROAR_ANGER_INCREASE = 20;

    public Roar() {
        super(ImmutableMap.of(MemoryModuleType.ROAR_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.ROAR_SOUND_COOLDOWN, MemoryStatus.REGISTERED, MemoryModuleType.ROAR_SOUND_DELAY, MemoryStatus.REGISTERED), WardenAi.ROAR_DURATION);
    }

    protected void start(ServerLevel serverLevel0, Warden warden1, long long2) {
        Brain<Warden> $$3 = warden1.getBrain();
        $$3.setMemoryWithExpiry(MemoryModuleType.ROAR_SOUND_DELAY, Unit.INSTANCE, 25L);
        $$3.eraseMemory(MemoryModuleType.WALK_TARGET);
        LivingEntity $$4 = (LivingEntity) warden1.getBrain().getMemory(MemoryModuleType.ROAR_TARGET).get();
        BehaviorUtils.lookAtEntity(warden1, $$4);
        warden1.m_20124_(Pose.ROARING);
        warden1.increaseAngerAt($$4, 20, false);
    }

    protected boolean canStillUse(ServerLevel serverLevel0, Warden warden1, long long2) {
        return true;
    }

    protected void tick(ServerLevel serverLevel0, Warden warden1, long long2) {
        if (!warden1.getBrain().hasMemoryValue(MemoryModuleType.ROAR_SOUND_DELAY) && !warden1.getBrain().hasMemoryValue(MemoryModuleType.ROAR_SOUND_COOLDOWN)) {
            warden1.getBrain().setMemoryWithExpiry(MemoryModuleType.ROAR_SOUND_COOLDOWN, Unit.INSTANCE, (long) (WardenAi.ROAR_DURATION - 25));
            warden1.m_5496_(SoundEvents.WARDEN_ROAR, 3.0F, 1.0F);
        }
    }

    protected void stop(ServerLevel serverLevel0, Warden warden1, long long2) {
        if (warden1.m_217003_(Pose.ROARING)) {
            warden1.m_20124_(Pose.STANDING);
        }
        warden1.getBrain().getMemory(MemoryModuleType.ROAR_TARGET).ifPresent(warden1::m_219459_);
        warden1.getBrain().eraseMemory(MemoryModuleType.ROAR_TARGET);
    }
}