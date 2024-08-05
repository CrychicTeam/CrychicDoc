package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalJoinPackGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.GroundPathNavigatorNoSpin;
import com.github.alexmodguy.alexscaves.server.entity.ai.MobTargetClosePlayers;
import com.github.alexmodguy.alexscaves.server.entity.ai.UnderzealotBreakLightGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.UnderzealotCaptureSacrificeGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.UnderzealotHurtByTargetGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.UnderzealotMeleeGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.UnderzealotOpenDoorGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.UnderzealotProcessionGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.UnderzealotSacrificeGoal;
import com.github.alexmodguy.alexscaves.server.entity.util.PackAnimal;
import com.github.alexmodguy.alexscaves.server.entity.util.UnderzealotSacrifice;
import com.github.alexmodguy.alexscaves.server.misc.ACAdvancementTriggerRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class UnderzealotEntity extends Monster implements PackAnimal, IAnimatedEntity {

    private static final EntityDataAccessor<Boolean> BURIED = SynchedEntityData.defineId(UnderzealotEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> CARRYING = SynchedEntityData.defineId(UnderzealotEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> PRAYING = SynchedEntityData.defineId(UnderzealotEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Optional<BlockPos>> SACRIFICE_POS = SynchedEntityData.defineId(UnderzealotEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

    private static final EntityDataAccessor<Optional<BlockPos>> PARTICLE_POS = SynchedEntityData.defineId(UnderzealotEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

    private static final EntityDataAccessor<Integer> WORSHIP_TIME = SynchedEntityData.defineId(UnderzealotEntity.class, EntityDataSerializers.INT);

    public static final Animation ANIMATION_ATTACK_0 = Animation.create(15);

    public static final Animation ANIMATION_ATTACK_1 = Animation.create(15);

    public static final Animation ANIMATION_BREAKTORCH = Animation.create(15);

    public static final int MAX_WORSHIP_TIME = 500;

    public int sacrificeCooldown;

    public int cloudCooldown;

    private Animation currentAnimation;

    private int animationTick;

    private float buriedProgress;

    private float prevBuriedProgress;

    private float carryingProgress;

    private float prevCarryingProgress;

    private float prayingProgress;

    private float prevPrayingProgress;

    private UnderzealotEntity priorPackMember;

    private UnderzealotEntity afterPackMember;

    private int idleBuryIn = 400 + this.f_19796_.nextInt(800);

    private int reemergeTime = 0;

    private BlockPos remergePos = null;

    public UnderzealotEntity(EntityType type, Level level) {
        super(type, level);
        this.buriedProgress = 20.0F;
        this.prevBuriedProgress = 20.0F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 4.0).add(Attributes.FOLLOW_RANGE, 20.0);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new AnimalJoinPackGoal(this, 60, 11));
        this.f_21345_.addGoal(2, new UnderzealotOpenDoorGoal(this));
        this.f_21345_.addGoal(3, new UnderzealotSacrificeGoal(this));
        this.f_21345_.addGoal(4, new UnderzealotMeleeGoal(this));
        this.f_21345_.addGoal(5, new UnderzealotCaptureSacrificeGoal(this));
        this.f_21345_.addGoal(6, new UnderzealotProcessionGoal(this, 1.0));
        this.f_21345_.addGoal(7, new UnderzealotBreakLightGoal(this, 32));
        this.f_21345_.addGoal(9, new RandomStrollGoal(this, 1.0, 100));
        this.f_21345_.addGoal(9, new LookAtPlayerGoal(this, Player.class, 15.0F));
        this.f_21345_.addGoal(10, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new UnderzealotHurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new MobTargetClosePlayers(this, 40, 12.0F) {

            @Override
            public boolean canUse() {
                return !UnderzealotEntity.this.isTargetingBlocked() && super.canUse();
            }
        });
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(BURIED, false);
        this.f_19804_.define(CARRYING, false);
        this.f_19804_.define(PRAYING, false);
        this.f_19804_.define(SACRIFICE_POS, Optional.empty());
        this.f_19804_.define(PARTICLE_POS, Optional.empty());
        this.f_19804_.define(WORSHIP_TIME, 0);
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, this.m_20186_() - this.f_19855_, this.m_20189_() - this.f_19856_);
        float f2 = Math.min(f1 * 8.0F, 1.0F);
        this.f_267362_.update(f2, 0.4F);
    }

    @Override
    public void travel(Vec3 vec3d) {
        if (this.isBuried() || this.isDiggingInProgress()) {
            vec3d = Vec3.ZERO;
        }
        super.m_7023_(vec3d);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new GroundPathNavigatorNoSpin(this, level);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevBuriedProgress = this.buriedProgress;
        this.prevCarryingProgress = this.carryingProgress;
        this.prevPrayingProgress = this.prayingProgress;
        if (!this.m_21525_() && (this.buriedProgress == 0.0F && this.isBuried() || this.buriedProgress == 20.0F && !this.isBuried())) {
            this.m_216990_(ACSoundRegistry.UNDERZEALOT_DIG.get());
        }
        if (this.isBuried() && this.buriedProgress < 20.0F) {
            this.buriedProgress = Math.min(20.0F, this.buriedProgress + 1.5F);
        }
        if (!this.isBuried() && this.buriedProgress > 0.0F) {
            this.buriedProgress = Math.max(0.0F, this.buriedProgress - 1.5F);
        }
        if (this.isCarrying() && this.carryingProgress < 5.0F) {
            this.carryingProgress++;
        }
        if (!this.isCarrying() && this.carryingProgress > 0.0F) {
            this.carryingProgress--;
        }
        if (this.isPraying() && this.prayingProgress < 5.0F) {
            this.prayingProgress++;
        }
        if (!this.isPraying() && this.prayingProgress > 0.0F) {
            this.prayingProgress--;
        }
        if (!this.m_9236_().isClientSide) {
            if (this.isCarrying() && this.isPackFollower()) {
                for (Entity passenger : this.m_20197_()) {
                    passenger.stopRiding();
                }
            }
            this.setCarrying(!this.m_20197_().isEmpty());
            if (this.isBuried() && this.buriedProgress >= 20.0F) {
                if (this.reemergeTime-- < 0) {
                    this.m_6027_((double) ((float) this.remergePos.m_123341_() + 0.5F), (double) this.remergePos.m_123342_(), (double) ((float) this.remergePos.m_123343_() + 0.5F));
                    this.idleBuryIn = 400 + this.f_19796_.nextInt(800);
                    this.reemergeTime = 0;
                    this.setBuried(false);
                }
                Vec3 centerOf = Vec3.atBottomCenterOf(this.m_20183_()).subtract(this.m_20182_());
                this.m_20184_().add(centerOf.x * 0.1F, 0.0, centerOf.z * 0.1F);
            } else if (this.digsIdle() && this.idleBuryIn-- < 0 && this.m_20096_()) {
                this.setBuried(true);
                this.idleBuryIn = 0;
                this.reemergeAt(this.findReemergePos(this.m_20183_(), 10), 40 + this.f_19796_.nextInt(60));
            }
            if (this.isDiggingInProgress()) {
                this.m_20256_(this.m_20184_().multiply(0.0, 1.0, 0.0));
            }
        } else if (this.isDiggingInProgress()) {
            BlockState stateOn = this.m_20075_();
            if (stateOn.m_280296_()) {
                for (int i = 0; i < 3; i++) {
                    this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, stateOn), true, this.m_20208_(0.8F), this.m_20186_(), this.m_20262_(0.8F), (double) (this.f_19796_.nextFloat() - 0.5F), (double) (this.f_19796_.nextFloat() + 0.5F), (double) (this.f_19796_.nextFloat() - 0.5F));
                }
            }
            if (this.isBuried()) {
                this.m_146867_();
            }
        }
        if (this.cloudCooldown > 0) {
            this.cloudCooldown--;
        }
        if (this.sacrificeCooldown > 0) {
            this.sacrificeCooldown--;
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 61) {
            Vec3 eye = this.m_146892_();
            Vec3 leftHand = eye.add(new Vec3(0.8F, 0.0, 0.0).yRot(-this.f_20883_ * (float) (Math.PI / 180.0)));
            Vec3 rightHand = eye.add(new Vec3(-0.8F, 0.0, 0.0).yRot(-this.f_20883_ * (float) (Math.PI / 180.0)));
            Vec3 target = this.getParticlePos() == null ? this.m_20182_().add(0.0, 4.0, 0.0) : Vec3.atCenterOf(this.getParticlePos());
            this.m_9236_().addParticle(ACParticleRegistry.UNDERZEALOT_MAGIC.get(), leftHand.x, leftHand.y, leftHand.z, target.x, target.y, target.z);
            this.m_9236_().addParticle(ACParticleRegistry.UNDERZEALOT_MAGIC.get(), rightHand.x, rightHand.y, rightHand.z, target.x, target.y, target.z);
        } else if (b == 62) {
            Vec3 particleAt = this.getParticlePos() == null ? this.m_20182_().add(0.0, 4.0, 0.0) : Vec3.atCenterOf(this.getParticlePos());
            int carryingId = -1;
            if (this.m_20160_()) {
                carryingId = this.m_146895_() == null ? -1 : this.m_146895_().getId();
            }
            this.m_9236_().addParticle(ACParticleRegistry.VOID_BEING_CLOUD.get(), particleAt.x, particleAt.y, particleAt.z, 1.0, (double) carryingId, (double) (5 + this.f_19796_.nextInt(4)));
        } else if (b == 77) {
            if (this.m_6084_()) {
                AlexsCaves.PROXY.playWorldSound(this, (byte) 5);
            }
        } else {
            super.m_7822_(b);
        }
    }

    @Override
    public void remove(Entity.RemovalReason removalReason) {
        AlexsCaves.PROXY.clearSoundCacheFor(this);
        super.m_142687_(removalReason);
    }

    public BlockPos findReemergePos(BlockPos origin, int range) {
        for (int i = 0; i < 15; i++) {
            BlockPos blockPos = origin.offset(this.m_217043_().nextInt(range) - range / 2, this.m_217043_().nextInt(range) - range / 2, this.m_217043_().nextInt(range) - range / 2);
            while (!this.m_9236_().m_46859_(blockPos) && blockPos.m_123342_() < this.m_9236_().m_151558_()) {
                blockPos = blockPos.above();
            }
            while (this.m_9236_().m_46859_(blockPos.below()) && blockPos.m_123342_() > this.m_9236_().m_141937_()) {
                blockPos = blockPos.below();
            }
            if (!this.m_9236_().m_46859_(blockPos.below()) && blockPos.m_123331_(origin) < (double) (range * range + 10)) {
                return blockPos;
            }
        }
        return origin;
    }

    public void triggerIdleDigging() {
        this.idleBuryIn = 0;
    }

    public boolean digsIdle() {
        LivingEntity target = this.m_5448_();
        return !this.isCarrying() && !this.isPackFollower() && !this.isPraying() && this.getAnimation() == NO_ANIMATION && (target == null || !target.isAlive());
    }

    public void reemergeAt(BlockPos pos, int time) {
        this.remergePos = pos;
        this.reemergeTime = time;
    }

    @Override
    public void positionRider(Entity entity, Entity.MoveFunction moveFunction) {
        super.m_19956_(entity, moveFunction);
        entity.setYRot(this.f_20883_);
        entity.setYHeadRot(this.f_20883_);
        entity.setYBodyRot(this.f_20883_);
    }

    public boolean isBuried() {
        return this.f_19804_.get(BURIED);
    }

    public void setBuried(boolean bool) {
        this.f_19804_.set(BURIED, bool);
    }

    public boolean isCarrying() {
        return this.f_19804_.get(CARRYING);
    }

    public BlockPos getLastSacrificePos() {
        return (BlockPos) this.f_19804_.get(SACRIFICE_POS).orElse(null);
    }

    public void setLastSacrificePos(BlockPos lastAltarPos) {
        this.f_19804_.set(SACRIFICE_POS, Optional.ofNullable(lastAltarPos));
    }

    public BlockPos getParticlePos() {
        return (BlockPos) this.f_19804_.get(PARTICLE_POS).orElse(null);
    }

    public void setParticlePos(BlockPos lastAltarPos) {
        this.f_19804_.set(PARTICLE_POS, Optional.ofNullable(lastAltarPos));
    }

    public void setCarrying(boolean bool) {
        this.f_19804_.set(CARRYING, bool);
    }

    public boolean isPraying() {
        return this.f_19804_.get(PRAYING);
    }

    public void setPraying(boolean bool) {
        this.f_19804_.set(PRAYING, bool);
    }

    public int getWorshipTime() {
        return this.f_19804_.get(WORSHIP_TIME);
    }

    public void setWorshipTime(int worship) {
        this.f_19804_.set(WORSHIP_TIME, worship);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return null;
    }

    public float getBuriedProgress(float partialTick) {
        return this.m_21525_() ? 0.0F : (this.prevBuriedProgress + (this.buriedProgress - this.prevBuriedProgress) * partialTick) * 0.05F;
    }

    public float getCarryingProgress(float partialTick) {
        return (this.prevCarryingProgress + (this.carryingProgress - this.prevCarryingProgress) * partialTick) * 0.2F;
    }

    public float getPrayingProgress(float partialTick) {
        return (this.prevPrayingProgress + (this.prayingProgress - this.prevPrayingProgress) * partialTick) * 0.2F;
    }

    @Override
    public PackAnimal getPriorPackMember() {
        return this.priorPackMember;
    }

    @Override
    public PackAnimal getAfterPackMember() {
        return this.afterPackMember;
    }

    @Override
    public void setPriorPackMember(PackAnimal animal) {
        this.priorPackMember = (UnderzealotEntity) animal;
    }

    @Override
    public void setAfterPackMember(PackAnimal animal) {
        this.afterPackMember = (UnderzealotEntity) animal;
    }

    @Override
    public boolean isValidLeader(PackAnimal packLeader) {
        return ((UnderzealotEntity) packLeader).isCarrying() && !packLeader.isPackFollower() && ((LivingEntity) packLeader).isAlive();
    }

    public boolean isTargetingBlocked() {
        if (this.isPackFollower() && this.getPackLeader() instanceof UnderzealotEntity underzealotLeader) {
            return underzealotLeader.isCarrying();
        } else {
            return this.isCarrying() ? true : this.isPraying();
        }
    }

    public boolean isDiggingInProgress() {
        return this.buriedProgress > 0.0F && this.buriedProgress < 20.0F;
    }

    @Override
    public void push(Entity entity) {
        if ((!(entity instanceof UnderzealotEntity underzealot) || !underzealot.isPraying() && !this.isPraying()) && !this.isBuried() && !this.isDiggingInProgress()) {
            super.m_7334_(entity);
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return (double) this.m_20206_();
    }

    @Override
    public boolean isPickable() {
        return !this.isBuried();
    }

    @Override
    public boolean isPushable() {
        return !this.isBuried();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return damageSource.is(DamageTypes.IN_WALL) || super.m_6673_(damageSource);
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_ATTACK_0, ANIMATION_ATTACK_1, ANIMATION_BREAKTORCH };
    }

    public static boolean checkUnderzealotSpawnRules(EntityType<? extends Monster> entityType, ServerLevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        return m_219013_(entityType, levelAccessor, mobSpawnType, blockPos, randomSource);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (reason == MobSpawnType.NATURAL) {
            this.spawnReinforcements(worldIn);
        }
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    private void spawnReinforcements(ServerLevelAccessor worldIn) {
        for (int i = 0; i < 3 + this.f_19796_.nextInt(2); i++) {
            UnderzealotEntity friend = ACEntityRegistry.UNDERZEALOT.get().create(worldIn.getLevel());
            friend.m_20359_(this);
            worldIn.m_7967_(friend);
        }
    }

    public boolean isSurroundedByPrayers() {
        PackAnimal leader = this.getPackLeader();
        int prayers = 0;
        while (leader.getAfterPackMember() != null) {
            leader = leader.getAfterPackMember();
            if (leader instanceof UnderzealotEntity) {
                UnderzealotEntity underzealot = (UnderzealotEntity) leader;
                if (underzealot.isPraying()) {
                    prayers++;
                }
            }
        }
        return prayers >= 3;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        compound.putInt("SacrificeCooldown", this.sacrificeCooldown);
        compound.putBoolean("Buried", this.isBuried());
        if (this.remergePos != null) {
            compound.putInt("RX", this.remergePos.m_123341_());
            compound.putInt("RY", this.remergePos.m_123342_());
            compound.putInt("RZ", this.remergePos.m_123343_());
        }
        BlockPos sacrificePos = this.getLastSacrificePos();
        if (sacrificePos != null) {
            compound.putInt("SX", sacrificePos.m_123341_());
            compound.putInt("SY", sacrificePos.m_123342_());
            compound.putInt("SZ", sacrificePos.m_123343_());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.sacrificeCooldown = compound.getInt("SacrificeCooldown");
        this.setBuried(compound.getBoolean("Buried"));
        if (compound.contains("RX") && compound.contains("RY") && compound.contains("RZ")) {
            this.remergePos = new BlockPos(compound.getInt("RX"), compound.getInt("RY"), compound.getInt("RZ"));
        }
        if (compound.contains("SX") && compound.contains("SY") && compound.contains("SZ")) {
            this.setLastSacrificePos(new BlockPos(compound.getInt("SX"), compound.getInt("SY"), compound.getInt("SZ")));
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        boolean prev = super.m_6469_(damageSource, f);
        if (prev && this.m_20160_() && this.m_217043_().nextFloat() < 0.65F) {
            this.m_20153_();
            return true;
        } else {
            return prev;
        }
    }

    @Override
    public boolean isAttackable() {
        return super.m_6097_() && !this.isBuried();
    }

    public void postSacrifice(UnderzealotSacrifice sacrifice) {
        this.m_5496_(ACSoundRegistry.UNDERZEALOT_TRANSFORMATION.get(), 8.0F, 1.0F);
        this.sacrificeCooldown = 6000 + this.f_19796_.nextInt(6000);
        float advancementRange = 64.0F;
        for (Player player : this.m_9236_().m_45976_(Player.class, this.m_20191_().inflate((double) advancementRange))) {
            if (player.m_20270_(this) < advancementRange) {
                ACAdvancementTriggerRegistry.UNDERZEALOT_SACRIFICE.triggerForEntity(player);
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return !this.isCarrying() && !this.isPraying() && !this.isBuried() ? ACSoundRegistry.UNDERZEALOT_IDLE.get() : super.m_7515_();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.UNDERZEALOT_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.UNDERZEALOT_DEATH.get();
    }

    @Override
    public void jumpFromGround() {
        super.m_6135_();
    }
}