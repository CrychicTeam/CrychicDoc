package net.minecraft.world.entity.animal;

import com.google.common.collect.ImmutableList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.GolemRandomStrollInVillageGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveBackToVillageGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.OfferFlowerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.DefendVillageTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;

public class IronGolem extends AbstractGolem implements NeutralMob {

    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(IronGolem.class, EntityDataSerializers.BYTE);

    private static final int IRON_INGOT_HEAL_AMOUNT = 25;

    private int attackAnimationTick;

    private int offerFlowerTick;

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);

    private int remainingPersistentAngerTime;

    @Nullable
    private UUID persistentAngerTarget;

    public IronGolem(EntityType<? extends IronGolem> entityTypeExtendsIronGolem0, Level level1) {
        super(entityTypeExtendsIronGolem0, level1);
        this.m_274367_(1.0F);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new MeleeAttackGoal(this, 1.0, true));
        this.f_21345_.addGoal(2, new MoveTowardsTargetGoal(this, 0.9, 32.0F));
        this.f_21345_.addGoal(2, new MoveBackToVillageGoal(this, 0.6, false));
        this.f_21345_.addGoal(4, new GolemRandomStrollInVillageGoal(this, 0.6));
        this.f_21345_.addGoal(5, new OfferFlowerGoal(this));
        this.f_21345_.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new DefendVillageTargetGoal(this));
        this.f_21346_.addGoal(2, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, this::m_21674_));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Mob.class, 5, false, false, p_28879_ -> p_28879_ instanceof Enemy && !(p_28879_ instanceof Creeper)));
        this.f_21346_.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_FLAGS_ID, (byte) 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 100.0).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.KNOCKBACK_RESISTANCE, 1.0).add(Attributes.ATTACK_DAMAGE, 15.0);
    }

    @Override
    protected int decreaseAirSupply(int int0) {
        return int0;
    }

    @Override
    protected void doPush(Entity entity0) {
        if (entity0 instanceof Enemy && !(entity0 instanceof Creeper) && this.m_217043_().nextInt(20) == 0) {
            this.m_6710_((LivingEntity) entity0);
        }
        super.m_7324_(entity0);
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        if (this.attackAnimationTick > 0) {
            this.attackAnimationTick--;
        }
        if (this.offerFlowerTick > 0) {
            this.offerFlowerTick--;
        }
        if (!this.m_9236_().isClientSide) {
            this.m_21666_((ServerLevel) this.m_9236_(), true);
        }
    }

    @Override
    public boolean canSpawnSprintParticle() {
        return this.m_20184_().horizontalDistanceSqr() > 2.5000003E-7F && this.f_19796_.nextInt(5) == 0;
    }

    @Override
    public boolean canAttackType(EntityType<?> entityType0) {
        if (this.isPlayerCreated() && entityType0 == EntityType.PLAYER) {
            return false;
        } else {
            return entityType0 == EntityType.CREEPER ? false : super.m_6549_(entityType0);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7380_(compoundTag0);
        compoundTag0.putBoolean("PlayerCreated", this.isPlayerCreated());
        this.m_21678_(compoundTag0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7378_(compoundTag0);
        this.setPlayerCreated(compoundTag0.getBoolean("PlayerCreated"));
        this.m_147285_(this.m_9236_(), compoundTag0);
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.f_19796_));
    }

    @Override
    public void setRemainingPersistentAngerTime(int int0) {
        this.remainingPersistentAngerTime = int0;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID uUID0) {
        this.persistentAngerTarget = uUID0;
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    private float getAttackDamage() {
        return (float) this.m_21133_(Attributes.ATTACK_DAMAGE);
    }

    @Override
    public boolean doHurtTarget(Entity entity0) {
        this.attackAnimationTick = 10;
        this.m_9236_().broadcastEntityEvent(this, (byte) 4);
        float $$1 = this.getAttackDamage();
        float $$2 = (int) $$1 > 0 ? $$1 / 2.0F + (float) this.f_19796_.nextInt((int) $$1) : $$1;
        boolean $$3 = entity0.hurt(this.m_269291_().mobAttack(this), $$2);
        if ($$3) {
            double $$5 = entity0 instanceof LivingEntity $$4 ? $$4.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) : 0.0;
            double $$6 = Math.max(0.0, 1.0 - $$5);
            entity0.setDeltaMovement(entity0.getDeltaMovement().add(0.0, 0.4F * $$6, 0.0));
            this.m_19970_(this, entity0);
        }
        this.m_5496_(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        return $$3;
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        IronGolem.Crackiness $$2 = this.getCrackiness();
        boolean $$3 = super.m_6469_(damageSource0, float1);
        if ($$3 && this.getCrackiness() != $$2) {
            this.m_5496_(SoundEvents.IRON_GOLEM_DAMAGE, 1.0F, 1.0F);
        }
        return $$3;
    }

    public IronGolem.Crackiness getCrackiness() {
        return IronGolem.Crackiness.byFraction(this.m_21223_() / this.m_21233_());
    }

    @Override
    public void handleEntityEvent(byte byte0) {
        if (byte0 == 4) {
            this.attackAnimationTick = 10;
            this.m_5496_(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        } else if (byte0 == 11) {
            this.offerFlowerTick = 400;
        } else if (byte0 == 34) {
            this.offerFlowerTick = 0;
        } else {
            super.m_7822_(byte0);
        }
    }

    public int getAttackAnimationTick() {
        return this.attackAnimationTick;
    }

    public void offerFlower(boolean boolean0) {
        if (boolean0) {
            this.offerFlowerTick = 400;
            this.m_9236_().broadcastEntityEvent(this, (byte) 11);
        } else {
            this.offerFlowerTick = 0;
            this.m_9236_().broadcastEntityEvent(this, (byte) 34);
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }

    @Override
    protected InteractionResult mobInteract(Player player0, InteractionHand interactionHand1) {
        ItemStack $$2 = player0.m_21120_(interactionHand1);
        if (!$$2.is(Items.IRON_INGOT)) {
            return InteractionResult.PASS;
        } else {
            float $$3 = this.m_21223_();
            this.m_5634_(25.0F);
            if (this.m_21223_() == $$3) {
                return InteractionResult.PASS;
            } else {
                float $$4 = 1.0F + (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F;
                this.m_5496_(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, $$4);
                if (!player0.getAbilities().instabuild) {
                    $$2.shrink(1);
                }
                return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
            }
        }
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
        this.m_5496_(SoundEvents.IRON_GOLEM_STEP, 1.0F, 1.0F);
    }

    public int getOfferFlowerTick() {
        return this.offerFlowerTick;
    }

    public boolean isPlayerCreated() {
        return (this.f_19804_.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setPlayerCreated(boolean boolean0) {
        byte $$1 = this.f_19804_.get(DATA_FLAGS_ID);
        if (boolean0) {
            this.f_19804_.set(DATA_FLAGS_ID, (byte) ($$1 | 1));
        } else {
            this.f_19804_.set(DATA_FLAGS_ID, (byte) ($$1 & -2));
        }
    }

    @Override
    public void die(DamageSource damageSource0) {
        super.m_6667_(damageSource0);
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader levelReader0) {
        BlockPos $$1 = this.m_20183_();
        BlockPos $$2 = $$1.below();
        BlockState $$3 = levelReader0.m_8055_($$2);
        if (!$$3.m_60634_(levelReader0, $$2, this)) {
            return false;
        } else {
            for (int $$4 = 1; $$4 < 3; $$4++) {
                BlockPos $$5 = $$1.above($$4);
                BlockState $$6 = levelReader0.m_8055_($$5);
                if (!NaturalSpawner.isValidEmptySpawnBlock(levelReader0, $$5, $$6, $$6.m_60819_(), EntityType.IRON_GOLEM)) {
                    return false;
                }
            }
            return NaturalSpawner.isValidEmptySpawnBlock(levelReader0, $$1, levelReader0.m_8055_($$1), Fluids.EMPTY.defaultFluidState(), EntityType.IRON_GOLEM) && levelReader0.m_45784_(this);
        }
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, (double) (0.875F * this.m_20192_()), (double) (this.m_20205_() * 0.4F));
    }

    public static enum Crackiness {

        NONE(1.0F), LOW(0.75F), MEDIUM(0.5F), HIGH(0.25F);

        private static final List<IronGolem.Crackiness> BY_DAMAGE = (List<IronGolem.Crackiness>) Stream.of(values()).sorted(Comparator.comparingDouble(p_28904_ -> (double) p_28904_.fraction)).collect(ImmutableList.toImmutableList());

        private final float fraction;

        private Crackiness(float p_28900_) {
            this.fraction = p_28900_;
        }

        public static IronGolem.Crackiness byFraction(float p_28902_) {
            for (IronGolem.Crackiness $$1 : BY_DAMAGE) {
                if (p_28902_ < $$1.fraction) {
                    return $$1;
                }
            }
            return NONE;
        }
    }
}