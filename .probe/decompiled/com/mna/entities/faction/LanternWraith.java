package com.mna.entities.faction;

import com.mna.api.entities.DamageHelper;
import com.mna.api.faction.IFaction;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedEvent;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.effects.EffectInit;
import com.mna.entities.faction.base.BaseFlyingFactionMob;
import com.mna.factions.Factions;
import com.mna.network.ServerMessageDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class LanternWraith extends BaseFlyingFactionMob<LanternWraith> implements RangedAttackMob {

    private static final String INSTRUCTION_BOLT = "bolt";

    private static final String INSTRUCTION_AFFECT = "affect";

    private boolean isAttacking = false;

    public LanternWraith(EntityType<LanternWraith> type, Level worldIn) {
        super(type, worldIn);
    }

    public static boolean canSpawnPredicate(EntityType<LanternWraith> type, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        if (worldIn.getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        } else if (randomIn.nextBoolean()) {
            return false;
        } else if (randomIn.nextInt(15) > 7) {
            return false;
        } else {
            BlockPos.MutableBlockPos blockpos$mutableblockpos = pos.mutable();
            do {
                blockpos$mutableblockpos.move(Direction.UP);
            } while (worldIn.m_6425_(blockpos$mutableblockpos).is(FluidTags.LAVA));
            return worldIn.m_8055_(blockpos$mutableblockpos).m_60795_();
        }
    }

    @Override
    protected PlayState handleAnimState(AnimationState<? extends BaseFlyingFactionMob<?>> state) {
        if (!this.isAttacking) {
            return this.m_20184_().add(0.0, -this.m_20184_().y, 0.0).length() > 0.02F ? state.setAndContinue(RawAnimation.begin().thenLoop("animation.ModelLanternWraith.move")) : state.setAndContinue(RawAnimation.begin().thenLoop("animation.ModelLanternWraith.idle"));
        } else {
            return state.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.ModelLanternWraith.attack"));
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(2, new RangedAttackGoal(this, this.m_21133_(Attributes.MOVEMENT_SPEED), 40, 80, 12.0F));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return source.is(DamageTypeTags.IS_FIRE) ? false : super.hurt(source, amount);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SFX.Entity.LanternWraith.HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SFX.Entity.LanternWraith.DEATH;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SFX.Entity.LanternWraith.IDLE;
    }

    @Override
    public IFaction getFaction() {
        return Factions.DEMONS;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        if (!this.isAttacking) {
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("bolt", 20, target, this::handleDelayCallback));
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("resetattack", 40, "", this::handleDelayCallback));
            this.isAttacking = true;
            this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.BELL_BLOCK, SoundSource.HOSTILE, 1.0F, (float) (0.9 + Math.random() * 0.2));
            ServerMessageDispatcher.sendEntityStateMessage(this);
        }
    }

    public void spawnSoulFireBolt(Entity target) {
        if (target.isAlive()) {
            ServerMessageDispatcher.sendParticleSpawn(this.m_20185_(), this.m_20186_() + 1.1F, this.m_20189_(), target.getX(), target.getY() + (double) target.getEyeHeight(), target.getZ(), 0, 64.0F, this.m_9236_().dimension(), ParticleInit.WRAITH_BOLT.get());
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("affect", 20, target.position(), this::tryAffectTarget));
        }
    }

    public void tryAffectTarget(String identifier, Vec3 position) {
        LivingEntity entity = this.m_5448_();
        if (entity != null) {
            MobEffect goalEffect = MobEffects.MOVEMENT_SLOWDOWN;
            int duration = 60;
            boolean increaseMagnitude = true;
            if (this.tier >= 2 && entity.getEffect(EffectInit.LIFE_TAP.get()) == null) {
                goalEffect = EffectInit.LIFE_TAP.get();
                duration = 200;
                increaseMagnitude = false;
            } else if (this.tier >= 3 && !entity.m_20096_()) {
                goalEffect = EffectInit.GRAVITY_WELL.get();
                duration = 100;
                increaseMagnitude = false;
            }
            double dist = entity.m_20238_(position);
            if (entity.isAlive() && !(dist > 4.0)) {
                if (entity instanceof Player) {
                    entity.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.getCastingResource().consume(entity, 100.0F));
                }
                int magnitude = 0;
                int damage = 5;
                MobEffectInstance inst = entity.getEffect(goalEffect);
                if (inst != null) {
                    if (increaseMagnitude) {
                        damage += inst.getAmplifier() * 2;
                        magnitude += inst.getAmplifier() + 1;
                        if (magnitude > 2) {
                            magnitude = 2;
                        }
                    } else {
                        damage += 5;
                    }
                }
                entity.addEffect(new MobEffectInstance(goalEffect, duration, magnitude));
                entity.hurt(DamageHelper.createSourcedType(DamageTypes.MAGIC, this.m_9236_().registryAccess(), this), (float) damage);
            }
        }
    }

    public static AttributeSupplier.Builder getGlobalAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 5.0).add(Attributes.MOVEMENT_SPEED, 0.55F).add(Attributes.ATTACK_DAMAGE, 6.0).add(Attributes.ATTACK_SPEED, 40.0).add(Attributes.ATTACK_KNOCKBACK, 1.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.0);
    }

    private void handleDelayCallback(String identifier, LivingEntity entity) {
        if (!this.m_9236_().isClientSide() && this.m_6084_()) {
            byte var4 = -1;
            switch(identifier.hashCode()) {
                case 3029653:
                    if (identifier.equals("bolt")) {
                        var4 = 0;
                    }
                default:
                    switch(var4) {
                        case 0:
                            this.spawnSoulFireBolt(entity);
                    }
            }
        }
    }

    private void handleDelayCallback(String identifier, String data) {
        if (!this.m_9236_().isClientSide()) {
            this.isAttacking = false;
            ServerMessageDispatcher.sendEntityStateMessage(this);
        }
    }

    @Override
    public CompoundTag getPacketData() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("attacking", this.isAttacking);
        return nbt;
    }

    @Override
    public void handlePacketData(CompoundTag nbt) {
        this.isAttacking = nbt.getBoolean("attacking");
    }

    @Override
    protected float attackTargetRunawayDistance() {
        return 10.0F;
    }

    @Override
    protected float attackTargetRunawayThreshold() {
        return 4.0F;
    }
}