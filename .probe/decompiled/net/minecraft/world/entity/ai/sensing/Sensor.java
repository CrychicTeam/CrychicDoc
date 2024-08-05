package net.minecraft.world.entity.ai.sensing;

import java.util.Set;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public abstract class Sensor<E extends LivingEntity> {

    private static final RandomSource RANDOM = RandomSource.createThreadSafe();

    private static final int DEFAULT_SCAN_RATE = 20;

    protected static final int TARGETING_RANGE = 16;

    private static final TargetingConditions TARGET_CONDITIONS = TargetingConditions.forNonCombat().range(16.0);

    private static final TargetingConditions TARGET_CONDITIONS_IGNORE_INVISIBILITY_TESTING = TargetingConditions.forNonCombat().range(16.0).ignoreInvisibilityTesting();

    private static final TargetingConditions ATTACK_TARGET_CONDITIONS = TargetingConditions.forCombat().range(16.0);

    private static final TargetingConditions ATTACK_TARGET_CONDITIONS_IGNORE_INVISIBILITY_TESTING = TargetingConditions.forCombat().range(16.0).ignoreInvisibilityTesting();

    private static final TargetingConditions ATTACK_TARGET_CONDITIONS_IGNORE_LINE_OF_SIGHT = TargetingConditions.forCombat().range(16.0).ignoreLineOfSight();

    private static final TargetingConditions ATTACK_TARGET_CONDITIONS_IGNORE_INVISIBILITY_AND_LINE_OF_SIGHT = TargetingConditions.forCombat().range(16.0).ignoreLineOfSight().ignoreInvisibilityTesting();

    private final int scanRate;

    private long timeToTick;

    public Sensor(int int0) {
        this.scanRate = int0;
        this.timeToTick = (long) RANDOM.nextInt(int0);
    }

    public Sensor() {
        this(20);
    }

    public final void tick(ServerLevel serverLevel0, E e1) {
        if (--this.timeToTick <= 0L) {
            this.timeToTick = (long) this.scanRate;
            this.doTick(serverLevel0, e1);
        }
    }

    protected abstract void doTick(ServerLevel var1, E var2);

    public abstract Set<MemoryModuleType<?>> requires();

    public static boolean isEntityTargetable(LivingEntity livingEntity0, LivingEntity livingEntity1) {
        return livingEntity0.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, livingEntity1) ? TARGET_CONDITIONS_IGNORE_INVISIBILITY_TESTING.test(livingEntity0, livingEntity1) : TARGET_CONDITIONS.test(livingEntity0, livingEntity1);
    }

    public static boolean isEntityAttackable(LivingEntity livingEntity0, LivingEntity livingEntity1) {
        return livingEntity0.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, livingEntity1) ? ATTACK_TARGET_CONDITIONS_IGNORE_INVISIBILITY_TESTING.test(livingEntity0, livingEntity1) : ATTACK_TARGET_CONDITIONS.test(livingEntity0, livingEntity1);
    }

    public static boolean isEntityAttackableIgnoringLineOfSight(LivingEntity livingEntity0, LivingEntity livingEntity1) {
        return livingEntity0.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, livingEntity1) ? ATTACK_TARGET_CONDITIONS_IGNORE_INVISIBILITY_AND_LINE_OF_SIGHT.test(livingEntity0, livingEntity1) : ATTACK_TARGET_CONDITIONS_IGNORE_LINE_OF_SIGHT.test(livingEntity0, livingEntity1);
    }
}