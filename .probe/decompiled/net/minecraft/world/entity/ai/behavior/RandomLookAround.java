package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;

public class RandomLookAround extends Behavior<Mob> {

    private final IntProvider interval;

    private final float maxYaw;

    private final float minPitch;

    private final float pitchRange;

    public RandomLookAround(IntProvider intProvider0, float float1, float float2, float float3) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.GAZE_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT));
        if (float2 > float3) {
            throw new IllegalArgumentException("Minimum pitch is larger than maximum pitch! " + float2 + " > " + float3);
        } else {
            this.interval = intProvider0;
            this.maxYaw = float1;
            this.minPitch = float2;
            this.pitchRange = float3 - float2;
        }
    }

    protected void start(ServerLevel serverLevel0, Mob mob1, long long2) {
        RandomSource $$3 = mob1.m_217043_();
        float $$4 = Mth.clamp($$3.nextFloat() * this.pitchRange + this.minPitch, -90.0F, 90.0F);
        float $$5 = Mth.wrapDegrees(mob1.m_146908_() + 2.0F * $$3.nextFloat() * this.maxYaw - this.maxYaw);
        Vec3 $$6 = Vec3.directionFromRotation($$4, $$5);
        mob1.m_6274_().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(mob1.m_146892_().add($$6)));
        mob1.m_6274_().setMemory(MemoryModuleType.GAZE_COOLDOWN_TICKS, this.interval.sample($$3));
    }
}