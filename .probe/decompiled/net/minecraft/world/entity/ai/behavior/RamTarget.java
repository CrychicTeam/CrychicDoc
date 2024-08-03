package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.phys.Vec3;

public class RamTarget extends Behavior<Goat> {

    public static final int TIME_OUT_DURATION = 200;

    public static final float RAM_SPEED_FORCE_FACTOR = 1.65F;

    private final Function<Goat, UniformInt> getTimeBetweenRams;

    private final TargetingConditions ramTargeting;

    private final float speed;

    private final ToDoubleFunction<Goat> getKnockbackForce;

    private Vec3 ramDirection;

    private final Function<Goat, SoundEvent> getImpactSound;

    private final Function<Goat, SoundEvent> getHornBreakSound;

    public RamTarget(Function<Goat, UniformInt> functionGoatUniformInt0, TargetingConditions targetingConditions1, float float2, ToDoubleFunction<Goat> toDoubleFunctionGoat3, Function<Goat, SoundEvent> functionGoatSoundEvent4, Function<Goat, SoundEvent> functionGoatSoundEvent5) {
        super(ImmutableMap.of(MemoryModuleType.RAM_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT, MemoryModuleType.RAM_TARGET, MemoryStatus.VALUE_PRESENT), 200);
        this.getTimeBetweenRams = functionGoatUniformInt0;
        this.ramTargeting = targetingConditions1;
        this.speed = float2;
        this.getKnockbackForce = toDoubleFunctionGoat3;
        this.getImpactSound = functionGoatSoundEvent4;
        this.getHornBreakSound = functionGoatSoundEvent5;
        this.ramDirection = Vec3.ZERO;
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel0, Goat goat1) {
        return goat1.getBrain().hasMemoryValue(MemoryModuleType.RAM_TARGET);
    }

    protected boolean canStillUse(ServerLevel serverLevel0, Goat goat1, long long2) {
        return goat1.getBrain().hasMemoryValue(MemoryModuleType.RAM_TARGET);
    }

    protected void start(ServerLevel serverLevel0, Goat goat1, long long2) {
        BlockPos $$3 = goat1.m_20183_();
        Brain<?> $$4 = goat1.getBrain();
        Vec3 $$5 = (Vec3) $$4.getMemory(MemoryModuleType.RAM_TARGET).get();
        this.ramDirection = new Vec3((double) $$3.m_123341_() - $$5.x(), 0.0, (double) $$3.m_123343_() - $$5.z()).normalize();
        $$4.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget($$5, this.speed, 0));
    }

    protected void tick(ServerLevel serverLevel0, Goat goat1, long long2) {
        List<LivingEntity> $$3 = serverLevel0.m_45971_(LivingEntity.class, this.ramTargeting, goat1, goat1.m_20191_());
        Brain<?> $$4 = goat1.getBrain();
        if (!$$3.isEmpty()) {
            LivingEntity $$5 = (LivingEntity) $$3.get(0);
            $$5.hurt(serverLevel0.m_269111_().noAggroMobAttack(goat1), (float) goat1.m_21133_(Attributes.ATTACK_DAMAGE));
            int $$6 = goat1.m_21023_(MobEffects.MOVEMENT_SPEED) ? goat1.m_21124_(MobEffects.MOVEMENT_SPEED).getAmplifier() + 1 : 0;
            int $$7 = goat1.m_21023_(MobEffects.MOVEMENT_SLOWDOWN) ? goat1.m_21124_(MobEffects.MOVEMENT_SLOWDOWN).getAmplifier() + 1 : 0;
            float $$8 = 0.25F * (float) ($$6 - $$7);
            float $$9 = Mth.clamp(goat1.m_6113_() * 1.65F, 0.2F, 3.0F) + $$8;
            float $$10 = $$5.isDamageSourceBlocked(serverLevel0.m_269111_().mobAttack(goat1)) ? 0.5F : 1.0F;
            $$5.knockback((double) ($$10 * $$9) * this.getKnockbackForce.applyAsDouble(goat1), this.ramDirection.x(), this.ramDirection.z());
            this.finishRam(serverLevel0, goat1);
            serverLevel0.m_6269_(null, goat1, (SoundEvent) this.getImpactSound.apply(goat1), SoundSource.NEUTRAL, 1.0F, 1.0F);
        } else if (this.hasRammedHornBreakingBlock(serverLevel0, goat1)) {
            serverLevel0.m_6269_(null, goat1, (SoundEvent) this.getImpactSound.apply(goat1), SoundSource.NEUTRAL, 1.0F, 1.0F);
            boolean $$11 = goat1.dropHorn();
            if ($$11) {
                serverLevel0.m_6269_(null, goat1, (SoundEvent) this.getHornBreakSound.apply(goat1), SoundSource.NEUTRAL, 1.0F, 1.0F);
            }
            this.finishRam(serverLevel0, goat1);
        } else {
            Optional<WalkTarget> $$12 = $$4.getMemory(MemoryModuleType.WALK_TARGET);
            Optional<Vec3> $$13 = $$4.getMemory(MemoryModuleType.RAM_TARGET);
            boolean $$14 = $$12.isEmpty() || $$13.isEmpty() || ((WalkTarget) $$12.get()).getTarget().currentPosition().closerThan((Position) $$13.get(), 0.25);
            if ($$14) {
                this.finishRam(serverLevel0, goat1);
            }
        }
    }

    private boolean hasRammedHornBreakingBlock(ServerLevel serverLevel0, Goat goat1) {
        Vec3 $$2 = goat1.m_20184_().multiply(1.0, 0.0, 1.0).normalize();
        BlockPos $$3 = BlockPos.containing(goat1.m_20182_().add($$2));
        return serverLevel0.m_8055_($$3).m_204336_(BlockTags.SNAPS_GOAT_HORN) || serverLevel0.m_8055_($$3.above()).m_204336_(BlockTags.SNAPS_GOAT_HORN);
    }

    protected void finishRam(ServerLevel serverLevel0, Goat goat1) {
        serverLevel0.broadcastEntityEvent(goat1, (byte) 59);
        goat1.getBrain().setMemory(MemoryModuleType.RAM_COOLDOWN_TICKS, ((UniformInt) this.getTimeBetweenRams.apply(goat1)).sample(serverLevel0.f_46441_));
        goat1.getBrain().eraseMemory(MemoryModuleType.RAM_TARGET);
    }
}