package net.minecraft.world.entity.ai.behavior.warden;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.phys.Vec3;

public class SonicBoom extends Behavior<Warden> {

    private static final int DISTANCE_XZ = 15;

    private static final int DISTANCE_Y = 20;

    private static final double KNOCKBACK_VERTICAL = 0.5;

    private static final double KNOCKBACK_HORIZONTAL = 2.5;

    public static final int COOLDOWN = 40;

    private static final int TICKS_BEFORE_PLAYING_SOUND = Mth.ceil(34.0);

    private static final int DURATION = Mth.ceil(60.0F);

    public SonicBoom() {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.SONIC_BOOM_COOLDOWN, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SONIC_BOOM_SOUND_COOLDOWN, MemoryStatus.REGISTERED, MemoryModuleType.SONIC_BOOM_SOUND_DELAY, MemoryStatus.REGISTERED), DURATION);
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel0, Warden warden1) {
        return warden1.m_216992_((Entity) warden1.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get(), 15.0, 20.0);
    }

    protected boolean canStillUse(ServerLevel serverLevel0, Warden warden1, long long2) {
        return true;
    }

    protected void start(ServerLevel serverLevel0, Warden warden1, long long2) {
        warden1.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, (long) DURATION);
        warden1.getBrain().setMemoryWithExpiry(MemoryModuleType.SONIC_BOOM_SOUND_DELAY, Unit.INSTANCE, (long) TICKS_BEFORE_PLAYING_SOUND);
        serverLevel0.broadcastEntityEvent(warden1, (byte) 62);
        warden1.m_5496_(SoundEvents.WARDEN_SONIC_CHARGE, 3.0F, 1.0F);
    }

    protected void tick(ServerLevel serverLevel0, Warden warden1, long long2) {
        warden1.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent(p_289393_ -> warden1.m_21563_().setLookAt(p_289393_.m_20182_()));
        if (!warden1.getBrain().hasMemoryValue(MemoryModuleType.SONIC_BOOM_SOUND_DELAY) && !warden1.getBrain().hasMemoryValue(MemoryModuleType.SONIC_BOOM_SOUND_COOLDOWN)) {
            warden1.getBrain().setMemoryWithExpiry(MemoryModuleType.SONIC_BOOM_SOUND_COOLDOWN, Unit.INSTANCE, (long) (DURATION - TICKS_BEFORE_PLAYING_SOUND));
            warden1.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).filter(warden1::m_219385_).filter(p_217707_ -> warden1.m_216992_(p_217707_, 15.0, 20.0)).ifPresent(p_217704_ -> {
                Vec3 $$3 = warden1.m_20182_().add(0.0, 1.6F, 0.0);
                Vec3 $$4 = p_217704_.m_146892_().subtract($$3);
                Vec3 $$5 = $$4.normalize();
                for (int $$6 = 1; $$6 < Mth.floor($$4.length()) + 7; $$6++) {
                    Vec3 $$7 = $$3.add($$5.scale((double) $$6));
                    serverLevel0.sendParticles(ParticleTypes.SONIC_BOOM, $$7.x, $$7.y, $$7.z, 1, 0.0, 0.0, 0.0, 0.0);
                }
                warden1.m_5496_(SoundEvents.WARDEN_SONIC_BOOM, 3.0F, 1.0F);
                p_217704_.hurt(serverLevel0.m_269111_().sonicBoom(warden1), 10.0F);
                double $$8 = 0.5 * (1.0 - p_217704_.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                double $$9 = 2.5 * (1.0 - p_217704_.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                p_217704_.m_5997_($$5.x() * $$9, $$5.y() * $$8, $$5.z() * $$9);
            });
        }
    }

    protected void stop(ServerLevel serverLevel0, Warden warden1, long long2) {
        setCooldown(warden1, 40);
    }

    public static void setCooldown(LivingEntity livingEntity0, int int1) {
        livingEntity0.getBrain().setMemoryWithExpiry(MemoryModuleType.SONIC_BOOM_COOLDOWN, Unit.INSTANCE, (long) int1);
    }
}