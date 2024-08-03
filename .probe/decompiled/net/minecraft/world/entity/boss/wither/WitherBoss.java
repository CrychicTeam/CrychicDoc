package net.minecraft.world.entity.boss.wither;

import com.google.common.collect.ImmutableList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class WitherBoss extends Monster implements PowerableMob, RangedAttackMob {

    private static final EntityDataAccessor<Integer> DATA_TARGET_A = SynchedEntityData.defineId(WitherBoss.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DATA_TARGET_B = SynchedEntityData.defineId(WitherBoss.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DATA_TARGET_C = SynchedEntityData.defineId(WitherBoss.class, EntityDataSerializers.INT);

    private static final List<EntityDataAccessor<Integer>> DATA_TARGETS = ImmutableList.of(DATA_TARGET_A, DATA_TARGET_B, DATA_TARGET_C);

    private static final EntityDataAccessor<Integer> DATA_ID_INV = SynchedEntityData.defineId(WitherBoss.class, EntityDataSerializers.INT);

    private static final int INVULNERABLE_TICKS = 220;

    private final float[] xRotHeads = new float[2];

    private final float[] yRotHeads = new float[2];

    private final float[] xRotOHeads = new float[2];

    private final float[] yRotOHeads = new float[2];

    private final int[] nextHeadUpdate = new int[2];

    private final int[] idleHeadUpdates = new int[2];

    private int destroyBlocksTick;

    private final ServerBossEvent bossEvent = (ServerBossEvent) new ServerBossEvent(this.m_5446_(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS).setDarkenScreen(true);

    private static final Predicate<LivingEntity> LIVING_ENTITY_SELECTOR = p_31504_ -> p_31504_.getMobType() != MobType.UNDEAD && p_31504_.attackable();

    private static final TargetingConditions TARGETING_CONDITIONS = TargetingConditions.forCombat().range(20.0).selector(LIVING_ENTITY_SELECTOR);

    public WitherBoss(EntityType<? extends WitherBoss> entityTypeExtendsWitherBoss0, Level level1) {
        super(entityTypeExtendsWitherBoss0, level1);
        this.f_21342_ = new FlyingMoveControl(this, 10, false);
        this.m_21153_(this.m_21233_());
        this.f_21364_ = 50;
    }

    @Override
    protected PathNavigation createNavigation(Level level0) {
        FlyingPathNavigation $$1 = new FlyingPathNavigation(this, level0);
        $$1.setCanOpenDoors(false);
        $$1.m_7008_(true);
        $$1.setCanPassDoors(true);
        return $$1;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new WitherBoss.WitherDoNothingGoal());
        this.f_21345_.addGoal(2, new RangedAttackGoal(this, 1.0, 40, 20.0F));
        this.f_21345_.addGoal(5, new WaterAvoidingRandomFlyingGoal(this, 1.0));
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(7, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, LivingEntity.class, 0, false, false, LIVING_ENTITY_SELECTOR));
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_TARGET_A, 0);
        this.f_19804_.define(DATA_TARGET_B, 0);
        this.f_19804_.define(DATA_TARGET_C, 0);
        this.f_19804_.define(DATA_ID_INV, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7380_(compoundTag0);
        compoundTag0.putInt("Invul", this.getInvulnerableTicks());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7378_(compoundTag0);
        this.setInvulnerableTicks(compoundTag0.getInt("Invul"));
        if (this.m_8077_()) {
            this.bossEvent.setName(this.m_5446_());
        }
    }

    @Override
    public void setCustomName(@Nullable Component component0) {
        super.m_6593_(component0);
        this.bossEvent.setName(this.m_5446_());
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WITHER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.WITHER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_DEATH;
    }

    @Override
    public void aiStep() {
        Vec3 $$0 = this.m_20184_().multiply(1.0, 0.6, 1.0);
        if (!this.m_9236_().isClientSide && this.getAlternativeTarget(0) > 0) {
            Entity $$1 = this.m_9236_().getEntity(this.getAlternativeTarget(0));
            if ($$1 != null) {
                double $$2 = $$0.y;
                if (this.m_20186_() < $$1.getY() || !this.isPowered() && this.m_20186_() < $$1.getY() + 5.0) {
                    $$2 = Math.max(0.0, $$2);
                    $$2 += 0.3 - $$2 * 0.6F;
                }
                $$0 = new Vec3($$0.x, $$2, $$0.z);
                Vec3 $$3 = new Vec3($$1.getX() - this.m_20185_(), 0.0, $$1.getZ() - this.m_20189_());
                if ($$3.horizontalDistanceSqr() > 9.0) {
                    Vec3 $$4 = $$3.normalize();
                    $$0 = $$0.add($$4.x * 0.3 - $$0.x * 0.6, 0.0, $$4.z * 0.3 - $$0.z * 0.6);
                }
            }
        }
        this.m_20256_($$0);
        if ($$0.horizontalDistanceSqr() > 0.05) {
            this.m_146922_((float) Mth.atan2($$0.z, $$0.x) * (180.0F / (float) Math.PI) - 90.0F);
        }
        super.aiStep();
        for (int $$5 = 0; $$5 < 2; $$5++) {
            this.yRotOHeads[$$5] = this.yRotHeads[$$5];
            this.xRotOHeads[$$5] = this.xRotHeads[$$5];
        }
        for (int $$6 = 0; $$6 < 2; $$6++) {
            int $$7 = this.getAlternativeTarget($$6 + 1);
            Entity $$8 = null;
            if ($$7 > 0) {
                $$8 = this.m_9236_().getEntity($$7);
            }
            if ($$8 != null) {
                double $$9 = this.getHeadX($$6 + 1);
                double $$10 = this.getHeadY($$6 + 1);
                double $$11 = this.getHeadZ($$6 + 1);
                double $$12 = $$8.getX() - $$9;
                double $$13 = $$8.getEyeY() - $$10;
                double $$14 = $$8.getZ() - $$11;
                double $$15 = Math.sqrt($$12 * $$12 + $$14 * $$14);
                float $$16 = (float) (Mth.atan2($$14, $$12) * 180.0F / (float) Math.PI) - 90.0F;
                float $$17 = (float) (-(Mth.atan2($$13, $$15) * 180.0F / (float) Math.PI));
                this.xRotHeads[$$6] = this.rotlerp(this.xRotHeads[$$6], $$17, 40.0F);
                this.yRotHeads[$$6] = this.rotlerp(this.yRotHeads[$$6], $$16, 10.0F);
            } else {
                this.yRotHeads[$$6] = this.rotlerp(this.yRotHeads[$$6], this.f_20883_, 10.0F);
            }
        }
        boolean $$18 = this.isPowered();
        for (int $$19 = 0; $$19 < 3; $$19++) {
            double $$20 = this.getHeadX($$19);
            double $$21 = this.getHeadY($$19);
            double $$22 = this.getHeadZ($$19);
            this.m_9236_().addParticle(ParticleTypes.SMOKE, $$20 + this.f_19796_.nextGaussian() * 0.3F, $$21 + this.f_19796_.nextGaussian() * 0.3F, $$22 + this.f_19796_.nextGaussian() * 0.3F, 0.0, 0.0, 0.0);
            if ($$18 && this.m_9236_().random.nextInt(4) == 0) {
                this.m_9236_().addParticle(ParticleTypes.ENTITY_EFFECT, $$20 + this.f_19796_.nextGaussian() * 0.3F, $$21 + this.f_19796_.nextGaussian() * 0.3F, $$22 + this.f_19796_.nextGaussian() * 0.3F, 0.7F, 0.7F, 0.5);
            }
        }
        if (this.getInvulnerableTicks() > 0) {
            for (int $$23 = 0; $$23 < 3; $$23++) {
                this.m_9236_().addParticle(ParticleTypes.ENTITY_EFFECT, this.m_20185_() + this.f_19796_.nextGaussian(), this.m_20186_() + (double) (this.f_19796_.nextFloat() * 3.3F), this.m_20189_() + this.f_19796_.nextGaussian(), 0.7F, 0.7F, 0.9F);
            }
        }
    }

    @Override
    protected void customServerAiStep() {
        if (this.getInvulnerableTicks() > 0) {
            int $$0 = this.getInvulnerableTicks() - 1;
            this.bossEvent.setProgress(1.0F - (float) $$0 / 220.0F);
            if ($$0 <= 0) {
                this.m_9236_().explode(this, this.m_20185_(), this.m_20188_(), this.m_20189_(), 7.0F, false, Level.ExplosionInteraction.MOB);
                if (!this.m_20067_()) {
                    this.m_9236_().globalLevelEvent(1023, this.m_20183_(), 0);
                }
            }
            this.setInvulnerableTicks($$0);
            if (this.f_19797_ % 10 == 0) {
                this.m_5634_(10.0F);
            }
        } else {
            super.m_8024_();
            for (int $$1 = 1; $$1 < 3; $$1++) {
                if (this.f_19797_ >= this.nextHeadUpdate[$$1 - 1]) {
                    this.nextHeadUpdate[$$1 - 1] = this.f_19797_ + 10 + this.f_19796_.nextInt(10);
                    if ((this.m_9236_().m_46791_() == Difficulty.NORMAL || this.m_9236_().m_46791_() == Difficulty.HARD) && this.idleHeadUpdates[$$1 - 1]++ > 15) {
                        float $$2 = 10.0F;
                        float $$3 = 5.0F;
                        double $$4 = Mth.nextDouble(this.f_19796_, this.m_20185_() - 10.0, this.m_20185_() + 10.0);
                        double $$5 = Mth.nextDouble(this.f_19796_, this.m_20186_() - 5.0, this.m_20186_() + 5.0);
                        double $$6 = Mth.nextDouble(this.f_19796_, this.m_20189_() - 10.0, this.m_20189_() + 10.0);
                        this.performRangedAttack($$1 + 1, $$4, $$5, $$6, true);
                        this.idleHeadUpdates[$$1 - 1] = 0;
                    }
                    int $$7 = this.getAlternativeTarget($$1);
                    if ($$7 > 0) {
                        LivingEntity $$8 = (LivingEntity) this.m_9236_().getEntity($$7);
                        if ($$8 != null && this.m_6779_($$8) && !(this.m_20280_($$8) > 900.0) && this.m_142582_($$8)) {
                            this.performRangedAttack($$1 + 1, $$8);
                            this.nextHeadUpdate[$$1 - 1] = this.f_19797_ + 40 + this.f_19796_.nextInt(20);
                            this.idleHeadUpdates[$$1 - 1] = 0;
                        } else {
                            this.setAlternativeTarget($$1, 0);
                        }
                    } else {
                        List<LivingEntity> $$9 = this.m_9236_().m_45971_(LivingEntity.class, TARGETING_CONDITIONS, this, this.m_20191_().inflate(20.0, 8.0, 20.0));
                        if (!$$9.isEmpty()) {
                            LivingEntity $$10 = (LivingEntity) $$9.get(this.f_19796_.nextInt($$9.size()));
                            this.setAlternativeTarget($$1, $$10.m_19879_());
                        }
                    }
                }
            }
            if (this.m_5448_() != null) {
                this.setAlternativeTarget(0, this.m_5448_().m_19879_());
            } else {
                this.setAlternativeTarget(0, 0);
            }
            if (this.destroyBlocksTick > 0) {
                this.destroyBlocksTick--;
                if (this.destroyBlocksTick == 0 && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                    int $$11 = Mth.floor(this.m_20186_());
                    int $$12 = Mth.floor(this.m_20185_());
                    int $$13 = Mth.floor(this.m_20189_());
                    boolean $$14 = false;
                    for (int $$15 = -1; $$15 <= 1; $$15++) {
                        for (int $$16 = -1; $$16 <= 1; $$16++) {
                            for (int $$17 = 0; $$17 <= 3; $$17++) {
                                int $$18 = $$12 + $$15;
                                int $$19 = $$11 + $$17;
                                int $$20 = $$13 + $$16;
                                BlockPos $$21 = new BlockPos($$18, $$19, $$20);
                                BlockState $$22 = this.m_9236_().getBlockState($$21);
                                if (canDestroy($$22)) {
                                    $$14 = this.m_9236_().m_46953_($$21, true, this) || $$14;
                                }
                            }
                        }
                    }
                    if ($$14) {
                        this.m_9236_().m_5898_(null, 1022, this.m_20183_(), 0);
                    }
                }
            }
            if (this.f_19797_ % 20 == 0) {
                this.m_5634_(1.0F);
            }
            this.bossEvent.setProgress(this.m_21223_() / this.m_21233_());
        }
    }

    public static boolean canDestroy(BlockState blockState0) {
        return !blockState0.m_60795_() && !blockState0.m_204336_(BlockTags.WITHER_IMMUNE);
    }

    public void makeInvulnerable() {
        this.setInvulnerableTicks(220);
        this.bossEvent.setProgress(0.0F);
        this.m_21153_(this.m_21233_() / 3.0F);
    }

    @Override
    public void makeStuckInBlock(BlockState blockState0, Vec3 vec1) {
    }

    @Override
    public void startSeenByPlayer(ServerPlayer serverPlayer0) {
        super.m_6457_(serverPlayer0);
        this.bossEvent.addPlayer(serverPlayer0);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer serverPlayer0) {
        super.m_6452_(serverPlayer0);
        this.bossEvent.removePlayer(serverPlayer0);
    }

    private double getHeadX(int int0) {
        if (int0 <= 0) {
            return this.m_20185_();
        } else {
            float $$1 = (this.f_20883_ + (float) (180 * (int0 - 1))) * (float) (Math.PI / 180.0);
            float $$2 = Mth.cos($$1);
            return this.m_20185_() + (double) $$2 * 1.3;
        }
    }

    private double getHeadY(int int0) {
        return int0 <= 0 ? this.m_20186_() + 3.0 : this.m_20186_() + 2.2;
    }

    private double getHeadZ(int int0) {
        if (int0 <= 0) {
            return this.m_20189_();
        } else {
            float $$1 = (this.f_20883_ + (float) (180 * (int0 - 1))) * (float) (Math.PI / 180.0);
            float $$2 = Mth.sin($$1);
            return this.m_20189_() + (double) $$2 * 1.3;
        }
    }

    private float rotlerp(float float0, float float1, float float2) {
        float $$3 = Mth.wrapDegrees(float1 - float0);
        if ($$3 > float2) {
            $$3 = float2;
        }
        if ($$3 < -float2) {
            $$3 = -float2;
        }
        return float0 + $$3;
    }

    private void performRangedAttack(int int0, LivingEntity livingEntity1) {
        this.performRangedAttack(int0, livingEntity1.m_20185_(), livingEntity1.m_20186_() + (double) livingEntity1.m_20192_() * 0.5, livingEntity1.m_20189_(), int0 == 0 && this.f_19796_.nextFloat() < 0.001F);
    }

    private void performRangedAttack(int int0, double double1, double double2, double double3, boolean boolean4) {
        if (!this.m_20067_()) {
            this.m_9236_().m_5898_(null, 1024, this.m_20183_(), 0);
        }
        double $$5 = this.getHeadX(int0);
        double $$6 = this.getHeadY(int0);
        double $$7 = this.getHeadZ(int0);
        double $$8 = double1 - $$5;
        double $$9 = double2 - $$6;
        double $$10 = double3 - $$7;
        WitherSkull $$11 = new WitherSkull(this.m_9236_(), this, $$8, $$9, $$10);
        $$11.m_5602_(this);
        if (boolean4) {
            $$11.setDangerous(true);
        }
        $$11.m_20343_($$5, $$6, $$7);
        this.m_9236_().m_7967_($$11);
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity0, float float1) {
        this.performRangedAttack(0, livingEntity0);
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        if (this.m_6673_(damageSource0)) {
            return false;
        } else if (damageSource0.is(DamageTypeTags.WITHER_IMMUNE_TO) || damageSource0.getEntity() instanceof WitherBoss) {
            return false;
        } else if (this.getInvulnerableTicks() > 0 && !damageSource0.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        } else {
            if (this.isPowered()) {
                Entity $$2 = damageSource0.getDirectEntity();
                if ($$2 instanceof AbstractArrow) {
                    return false;
                }
            }
            Entity $$3 = damageSource0.getEntity();
            if ($$3 != null && !($$3 instanceof Player) && $$3 instanceof LivingEntity && ((LivingEntity) $$3).getMobType() == this.getMobType()) {
                return false;
            } else {
                if (this.destroyBlocksTick <= 0) {
                    this.destroyBlocksTick = 20;
                }
                for (int $$4 = 0; $$4 < this.idleHeadUpdates.length; $$4++) {
                    this.idleHeadUpdates[$$4] = this.idleHeadUpdates[$$4] + 3;
                }
                return super.m_6469_(damageSource0, float1);
            }
        }
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource0, int int1, boolean boolean2) {
        super.m_7472_(damageSource0, int1, boolean2);
        ItemEntity $$3 = this.m_19998_(Items.NETHER_STAR);
        if ($$3 != null) {
            $$3.setExtendedLifetime();
        }
    }

    @Override
    public void checkDespawn() {
        if (this.m_9236_().m_46791_() == Difficulty.PEACEFUL && this.m_8028_()) {
            this.m_146870_();
        } else {
            this.f_20891_ = 0;
        }
    }

    @Override
    public boolean addEffect(MobEffectInstance mobEffectInstance0, @Nullable Entity entity1) {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 300.0).add(Attributes.MOVEMENT_SPEED, 0.6F).add(Attributes.FLYING_SPEED, 0.6F).add(Attributes.FOLLOW_RANGE, 40.0).add(Attributes.ARMOR, 4.0);
    }

    public float getHeadYRot(int int0) {
        return this.yRotHeads[int0];
    }

    public float getHeadXRot(int int0) {
        return this.xRotHeads[int0];
    }

    public int getInvulnerableTicks() {
        return this.f_19804_.get(DATA_ID_INV);
    }

    public void setInvulnerableTicks(int int0) {
        this.f_19804_.set(DATA_ID_INV, int0);
    }

    public int getAlternativeTarget(int int0) {
        return this.f_19804_.<Integer>get((EntityDataAccessor<Integer>) DATA_TARGETS.get(int0));
    }

    public void setAlternativeTarget(int int0, int int1) {
        this.f_19804_.set((EntityDataAccessor<Integer>) DATA_TARGETS.get(int0), int1);
    }

    @Override
    public boolean isPowered() {
        return this.m_21223_() <= this.m_21233_() / 2.0F;
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    protected boolean canRide(Entity entity0) {
        return false;
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance mobEffectInstance0) {
        return mobEffectInstance0.getEffect() == MobEffects.WITHER ? false : super.m_7301_(mobEffectInstance0);
    }

    class WitherDoNothingGoal extends Goal {

        public WitherDoNothingGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return WitherBoss.this.getInvulnerableTicks() > 0;
        }
    }
}