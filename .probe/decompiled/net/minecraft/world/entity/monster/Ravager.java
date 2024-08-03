package net.minecraft.world.entity.monster;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class Ravager extends Raider {

    private static final Predicate<Entity> NO_RAVAGER_AND_ALIVE = p_33346_ -> p_33346_.isAlive() && !(p_33346_ instanceof Ravager);

    private static final double BASE_MOVEMENT_SPEED = 0.3;

    private static final double ATTACK_MOVEMENT_SPEED = 0.35;

    private static final int STUNNED_COLOR = 8356754;

    private static final double STUNNED_COLOR_BLUE = 0.5725490196078431;

    private static final double STUNNED_COLOR_GREEN = 0.5137254901960784;

    private static final double STUNNED_COLOR_RED = 0.4980392156862745;

    private static final int ATTACK_DURATION = 10;

    public static final int STUN_DURATION = 40;

    private int attackTick;

    private int stunnedTick;

    private int roarTick;

    public Ravager(EntityType<? extends Ravager> entityTypeExtendsRavager0, Level level1) {
        super(entityTypeExtendsRavager0, level1);
        this.m_274367_(1.0F);
        this.f_21364_ = 20;
        this.m_21441_(BlockPathTypes.LEAVES, 0.0F);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(4, new Ravager.RavagerMeleeAttackGoal());
        this.f_21345_.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.4));
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.f_21346_.addGoal(2, new HurtByTargetGoal(this, Raider.class).setAlertOthers());
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Player.class, true));
        this.f_21346_.addGoal(4, new NearestAttackableTargetGoal(this, AbstractVillager.class, true, p_199899_ -> !p_199899_.isBaby()));
        this.f_21346_.addGoal(4, new NearestAttackableTargetGoal(this, IronGolem.class, true));
    }

    @Override
    protected void updateControlFlags() {
        boolean $$0 = !(this.getControllingPassenger() instanceof Mob) || this.getControllingPassenger().m_6095_().is(EntityTypeTags.RAIDERS);
        boolean $$1 = !(this.m_20202_() instanceof Boat);
        this.f_21345_.setControlFlag(Goal.Flag.MOVE, $$0);
        this.f_21345_.setControlFlag(Goal.Flag.JUMP, $$0 && $$1);
        this.f_21345_.setControlFlag(Goal.Flag.LOOK, $$0);
        this.f_21345_.setControlFlag(Goal.Flag.TARGET, $$0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 100.0).add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.KNOCKBACK_RESISTANCE, 0.75).add(Attributes.ATTACK_DAMAGE, 12.0).add(Attributes.ATTACK_KNOCKBACK, 1.5).add(Attributes.FOLLOW_RANGE, 32.0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putInt("AttackTick", this.attackTick);
        compoundTag0.putInt("StunTick", this.stunnedTick);
        compoundTag0.putInt("RoarTick", this.roarTick);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        this.attackTick = compoundTag0.getInt("AttackTick");
        this.stunnedTick = compoundTag0.getInt("StunTick");
        this.roarTick = compoundTag0.getInt("RoarTick");
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.RAVAGER_CELEBRATE;
    }

    @Override
    public int getMaxHeadYRot() {
        return 45;
    }

    @Override
    public double getPassengersRidingOffset() {
        return 2.1;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return !this.m_21525_() && this.m_146895_() instanceof LivingEntity $$0 ? $$0 : null;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.m_6084_()) {
            if (this.isImmobile()) {
                this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.0);
            } else {
                double $$0 = this.m_5448_() != null ? 0.35 : 0.3;
                double $$1 = this.m_21051_(Attributes.MOVEMENT_SPEED).getBaseValue();
                this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(Mth.lerp(0.1, $$1, $$0));
            }
            if (this.f_19862_ && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                boolean $$2 = false;
                AABB $$3 = this.m_20191_().inflate(0.2);
                for (BlockPos $$4 : BlockPos.betweenClosed(Mth.floor($$3.minX), Mth.floor($$3.minY), Mth.floor($$3.minZ), Mth.floor($$3.maxX), Mth.floor($$3.maxY), Mth.floor($$3.maxZ))) {
                    BlockState $$5 = this.m_9236_().getBlockState($$4);
                    Block $$6 = $$5.m_60734_();
                    if ($$6 instanceof LeavesBlock) {
                        $$2 = this.m_9236_().m_46953_($$4, true, this) || $$2;
                    }
                }
                if (!$$2 && this.m_20096_()) {
                    this.m_6135_();
                }
            }
            if (this.roarTick > 0) {
                this.roarTick--;
                if (this.roarTick == 10) {
                    this.roar();
                }
            }
            if (this.attackTick > 0) {
                this.attackTick--;
            }
            if (this.stunnedTick > 0) {
                this.stunnedTick--;
                this.stunEffect();
                if (this.stunnedTick == 0) {
                    this.m_5496_(SoundEvents.RAVAGER_ROAR, 1.0F, 1.0F);
                    this.roarTick = 20;
                }
            }
        }
    }

    private void stunEffect() {
        if (this.f_19796_.nextInt(6) == 0) {
            double $$0 = this.m_20185_() - (double) this.m_20205_() * Math.sin((double) (this.f_20883_ * (float) (Math.PI / 180.0))) + (this.f_19796_.nextDouble() * 0.6 - 0.3);
            double $$1 = this.m_20186_() + (double) this.m_20206_() - 0.3;
            double $$2 = this.m_20189_() + (double) this.m_20205_() * Math.cos((double) (this.f_20883_ * (float) (Math.PI / 180.0))) + (this.f_19796_.nextDouble() * 0.6 - 0.3);
            this.m_9236_().addParticle(ParticleTypes.ENTITY_EFFECT, $$0, $$1, $$2, 0.4980392156862745, 0.5137254901960784, 0.5725490196078431);
        }
    }

    @Override
    protected boolean isImmobile() {
        return super.m_6107_() || this.attackTick > 0 || this.stunnedTick > 0 || this.roarTick > 0;
    }

    @Override
    public boolean hasLineOfSight(Entity entity0) {
        return this.stunnedTick <= 0 && this.roarTick <= 0 ? super.m_142582_(entity0) : false;
    }

    @Override
    protected void blockedByShield(LivingEntity livingEntity0) {
        if (this.roarTick == 0) {
            if (this.f_19796_.nextDouble() < 0.5) {
                this.stunnedTick = 40;
                this.m_5496_(SoundEvents.RAVAGER_STUNNED, 1.0F, 1.0F);
                this.m_9236_().broadcastEntityEvent(this, (byte) 39);
                livingEntity0.push(this);
            } else {
                this.strongKnockback(livingEntity0);
            }
            livingEntity0.f_19864_ = true;
        }
    }

    private void roar() {
        if (this.m_6084_()) {
            for (LivingEntity $$1 : this.m_9236_().m_6443_(LivingEntity.class, this.m_20191_().inflate(4.0), NO_RAVAGER_AND_ALIVE)) {
                if (!($$1 instanceof AbstractIllager)) {
                    $$1.hurt(this.m_269291_().mobAttack(this), 6.0F);
                }
                this.strongKnockback($$1);
            }
            Vec3 $$2 = this.m_20191_().getCenter();
            for (int $$3 = 0; $$3 < 40; $$3++) {
                double $$4 = this.f_19796_.nextGaussian() * 0.2;
                double $$5 = this.f_19796_.nextGaussian() * 0.2;
                double $$6 = this.f_19796_.nextGaussian() * 0.2;
                this.m_9236_().addParticle(ParticleTypes.POOF, $$2.x, $$2.y, $$2.z, $$4, $$5, $$6);
            }
            this.m_146850_(GameEvent.ENTITY_ROAR);
        }
    }

    private void strongKnockback(Entity entity0) {
        double $$1 = entity0.getX() - this.m_20185_();
        double $$2 = entity0.getZ() - this.m_20189_();
        double $$3 = Math.max($$1 * $$1 + $$2 * $$2, 0.001);
        entity0.push($$1 / $$3 * 4.0, 0.2, $$2 / $$3 * 4.0);
    }

    @Override
    public void handleEntityEvent(byte byte0) {
        if (byte0 == 4) {
            this.attackTick = 10;
            this.m_5496_(SoundEvents.RAVAGER_ATTACK, 1.0F, 1.0F);
        } else if (byte0 == 39) {
            this.stunnedTick = 40;
        }
        super.m_7822_(byte0);
    }

    public int getAttackTick() {
        return this.attackTick;
    }

    public int getStunnedTick() {
        return this.stunnedTick;
    }

    public int getRoarTick() {
        return this.roarTick;
    }

    @Override
    public boolean doHurtTarget(Entity entity0) {
        this.attackTick = 10;
        this.m_9236_().broadcastEntityEvent(this, (byte) 4);
        this.m_5496_(SoundEvents.RAVAGER_ATTACK, 1.0F, 1.0F);
        return super.m_7327_(entity0);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.RAVAGER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.RAVAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.RAVAGER_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
        this.m_5496_(SoundEvents.RAVAGER_STEP, 0.15F, 1.0F);
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader levelReader0) {
        return !levelReader0.containsAnyLiquid(this.m_20191_());
    }

    @Override
    public void applyRaidBuffs(int int0, boolean boolean1) {
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    class RavagerMeleeAttackGoal extends MeleeAttackGoal {

        public RavagerMeleeAttackGoal() {
            super(Ravager.this, 1.0, true);
        }

        @Override
        protected double getAttackReachSqr(LivingEntity livingEntity0) {
            float $$1 = Ravager.this.m_20205_() - 0.1F;
            return (double) ($$1 * 2.0F * $$1 * 2.0F + livingEntity0.m_20205_());
        }
    }
}