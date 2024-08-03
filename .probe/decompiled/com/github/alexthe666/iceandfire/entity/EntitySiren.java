package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ai.AquaticAIGetInWater;
import com.github.alexthe666.iceandfire.entity.ai.AquaticAIGetOutOfWater;
import com.github.alexthe666.iceandfire.entity.ai.SirenAIFindWaterTarget;
import com.github.alexthe666.iceandfire.entity.ai.SirenAIWander;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.github.alexthe666.iceandfire.entity.util.ChainBuffer;
import com.github.alexthe666.iceandfire.entity.util.IHasCustomizableAttributes;
import com.github.alexthe666.iceandfire.entity.util.IHearsSiren;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.message.MessageSirenSong;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class EntitySiren extends Monster implements IAnimatedEntity, IVillagerFear, IHasCustomizableAttributes {

    public static final int SEARCH_RANGE = 32;

    public static final Predicate<Entity> SIREN_PREY = new Predicate<Entity>() {

        public boolean apply(@Nullable Entity p_apply_1_) {
            return p_apply_1_ instanceof Player && !((Player) p_apply_1_).isCreative() && !p_apply_1_.isSpectator() || p_apply_1_ instanceof AbstractVillager || p_apply_1_ instanceof IHearsSiren;
        }
    };

    private static final EntityDataAccessor<Integer> HAIR_COLOR = SynchedEntityData.defineId(EntitySiren.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> AGGRESSIVE = SynchedEntityData.defineId(EntitySiren.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> SING_POSE = SynchedEntityData.defineId(EntitySiren.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> SINGING = SynchedEntityData.defineId(EntitySiren.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SWIMMING = SynchedEntityData.defineId(EntitySiren.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> CHARMED = SynchedEntityData.defineId(EntitySiren.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(EntitySiren.class, EntityDataSerializers.BYTE);

    public static Animation ANIMATION_BITE = Animation.create(20);

    public static Animation ANIMATION_PULL = Animation.create(20);

    public ChainBuffer tail_buffer;

    public float singProgress;

    public float swimProgress;

    public int singCooldown;

    private int animationTick;

    private Animation currentAnimation;

    private boolean isSinging;

    private boolean isSwimming;

    private boolean isLandNavigator;

    private int ticksAgressive;

    public EntitySiren(EntityType<EntitySiren> t, Level worldIn) {
        super(t, worldIn);
        this.switchNavigator(true);
        if (worldIn.isClientSide) {
            this.tail_buffer = new ChainBuffer();
        }
        this.m_274367_(1.0F);
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(0, new SirenAIFindWaterTarget(this));
        this.f_21345_.addGoal(1, new AquaticAIGetInWater(this, 1.0));
        this.f_21345_.addGoal(1, new AquaticAIGetOutOfWater(this, 1.0));
        this.f_21345_.addGoal(2, new SirenAIWander(this, 1.0));
        this.f_21345_.addGoal(3, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(3, new MeleeAttackGoal(this, 1.0, false));
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F, 1.0F));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(4, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, new Predicate<Player>() {

            public boolean apply(@Nullable Player entity) {
                return EntitySiren.this.isAgressive() && !entity.isCreative() && !entity.isSpectator();
            }
        }));
        this.f_21346_.addGoal(4, new NearestAttackableTargetGoal(this, AbstractVillager.class, 10, true, false, new Predicate<AbstractVillager>() {

            public boolean apply(@Nullable AbstractVillager entity) {
                return EntitySiren.this.isAgressive();
            }
        }));
    }

    public static boolean isWearingEarplugs(LivingEntity entity) {
        ItemStack helmet = entity.getItemBySlot(EquipmentSlot.HEAD);
        return helmet.getItem() == IafItemRegistry.EARPLUGS.get() || helmet != ItemStack.EMPTY && helmet.getItem().getDescriptionId().contains("earmuff");
    }

    @Override
    public int getExperienceReward() {
        return 8;
    }

    @Override
    public float getWalkTargetValue(@NotNull BlockPos pos) {
        return this.m_9236_().getBlockState(pos).m_60713_(Blocks.WATER) ? 10.0F : super.m_21692_(pos);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        if (this.m_217043_().nextInt(2) == 0) {
            if (this.getAnimation() != ANIMATION_PULL) {
                this.setAnimation(ANIMATION_PULL);
                this.m_5496_(IafSoundRegistry.NAGA_ATTACK, 1.0F, 1.0F);
            }
        } else if (this.getAnimation() != ANIMATION_BITE) {
            this.setAnimation(ANIMATION_BITE);
            this.m_5496_(IafSoundRegistry.NAGA_ATTACK, 1.0F, 1.0F);
        }
        return true;
    }

    public boolean isDirectPathBetweenPoints(Vec3 vec1, Vec3 pos) {
        Vec3 Vector3d1 = new Vec3(pos.x() + 0.5, pos.y() + 0.5, pos.z() + 0.5);
        return this.m_9236_().m_45547_(new ClipContext(vec1, Vector3d1, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
    }

    @Override
    public float getPathfindingMalus(@NotNull BlockPathTypes nodeType) {
        return nodeType == BlockPathTypes.WATER ? 0.0F : super.m_21439_(nodeType);
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new AmphibiousPathNavigation(this, this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new EntitySiren.SwimmingMoveHelper();
            this.f_21344_ = new WaterBoundPathNavigation(this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    private boolean isPathOnHighGround() {
        if (this.f_21344_ != null && this.f_21344_.getPath() != null && this.f_21344_.getPath().getEndNode() != null) {
            BlockPos target = new BlockPos(this.f_21344_.getPath().getEndNode().x, this.f_21344_.getPath().getEndNode().y, this.f_21344_.getPath().getEndNode().z);
            BlockPos siren = this.m_20183_();
            return this.m_9236_().m_46859_(siren.above()) && this.m_9236_().m_46859_(target.above()) && target.m_123342_() >= siren.m_123342_();
        } else {
            return false;
        }
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.f_20883_ = this.m_146908_();
        LivingEntity attackTarget = this.m_5448_();
        if (this.singCooldown > 0) {
            this.singCooldown--;
            this.setSinging(false);
        }
        if (!this.m_9236_().isClientSide && attackTarget == null && !this.isAgressive()) {
            this.setSinging(true);
        }
        if (this.getAnimation() == ANIMATION_BITE && attackTarget != null && this.m_20280_(attackTarget) < 7.0 && this.getAnimationTick() == 5) {
            attackTarget.hurt(this.m_9236_().damageSources().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue());
        }
        if (this.getAnimation() == ANIMATION_PULL && attackTarget != null && this.m_20280_(attackTarget) < 16.0 && this.getAnimationTick() == 5) {
            attackTarget.hurt(this.m_9236_().damageSources().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue());
            double attackmotionX = (Math.signum(this.m_20185_() - attackTarget.m_20185_()) * 0.5 - attackTarget.m_20184_().z) * 0.100000000372529 * 5.0;
            double attackmotionY = (Math.signum(this.m_20186_() - attackTarget.m_20186_() + 1.0) * 0.5 - attackTarget.m_20184_().y) * 0.100000000372529 * 5.0;
            double attackmotionZ = (Math.signum(this.m_20189_() - attackTarget.m_20189_()) * 0.5 - attackTarget.m_20184_().z) * 0.100000000372529 * 5.0;
            attackTarget.m_20256_(attackTarget.m_20184_().add(attackmotionX, attackmotionY, attackmotionZ));
            double d0 = this.m_20185_() - attackTarget.m_20185_();
            double d2 = this.m_20189_() - attackTarget.m_20189_();
            double d1 = this.m_20186_() - 1.0 - attackTarget.m_20186_();
            double d3 = Math.sqrt((double) ((float) (d0 * d0 + d2 * d2)));
            float f = (float) (Mth.atan2(d2, d0) * (180.0 / Math.PI)) - 90.0F;
            float f1 = (float) (-(Mth.atan2(d1, d3) * (180.0 / Math.PI)));
            attackTarget.m_146926_(updateRotation(attackTarget.m_146909_(), f1, 30.0F));
            attackTarget.m_146922_(updateRotation(attackTarget.m_146908_(), f, 30.0F));
        }
        if (this.m_9236_().isClientSide) {
            this.tail_buffer.calculateChainSwingBuffer(40.0F, 10, 2.5F, this);
        }
        if (this.isAgressive()) {
            this.ticksAgressive++;
        } else {
            this.ticksAgressive = 0;
        }
        if (this.ticksAgressive > 300 && this.isAgressive() && attackTarget == null && !this.m_9236_().isClientSide) {
            this.setAggressive(false);
            this.ticksAgressive = 0;
            this.setSinging(false);
        }
        if (this.m_20069_() && !this.isSwimming()) {
            this.setSwimming(true);
        }
        if (!this.m_20069_() && this.isSwimming()) {
            this.setSwimming(false);
        }
        LivingEntity target = this.m_5448_();
        boolean pathOnHighGround = this.isPathOnHighGround() || !this.m_9236_().isClientSide && target != null && !target.m_20069_();
        if ((target == null || !target.m_20069_() && !target.m_20069_()) && pathOnHighGround && this.m_20069_()) {
            this.m_6135_();
            this.m_5841_();
        }
        if (this.m_20069_() && !pathOnHighGround && this.isLandNavigator) {
            this.switchNavigator(false);
        }
        if ((!this.m_20069_() || pathOnHighGround) && !this.isLandNavigator) {
            this.switchNavigator(true);
        }
        if (target instanceof Player && ((Player) target).isCreative()) {
            this.m_6710_(null);
            this.setAggressive(false);
        }
        if (target != null && !this.isAgressive()) {
            this.setAggressive(true);
        }
        boolean singing = this.isActuallySinging() && !this.isAgressive() && !this.m_20069_() && this.m_20096_();
        if (singing && this.singProgress < 20.0F) {
            this.singProgress++;
        } else if (!singing && this.singProgress > 0.0F) {
            this.singProgress--;
        }
        boolean swimming = this.isSwimming();
        if (swimming && this.swimProgress < 20.0F) {
            this.swimProgress++;
        } else if (!swimming && this.swimProgress > 0.0F) {
            this.swimProgress -= 0.5F;
        }
        if (!this.m_9236_().isClientSide && !EntityGorgon.isStoneMob(this) && this.isActuallySinging()) {
            this.updateLure();
            this.checkForPrey();
        }
        if (!this.m_9236_().isClientSide && EntityGorgon.isStoneMob(this) && this.isSinging()) {
            this.setSinging(false);
        }
        if (this.isActuallySinging() && !this.m_20069_() && this.m_217043_().nextInt(3) == 0) {
            this.f_20883_ = this.m_146908_();
            if (this.m_9236_().isClientSide) {
                float radius = -0.9F;
                float angle = (float) (Math.PI / 180.0) * this.f_20883_ - 3.0F;
                double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
                double extraY = 1.2F;
                double extraZ = (double) (radius * Mth.cos(angle));
                IceAndFire.PROXY.spawnParticle(EnumParticles.Siren_Music, this.m_20185_() + extraX + (double) this.f_19796_.nextFloat() - 0.5, this.m_20186_() + extraY + (double) this.f_19796_.nextFloat() - 0.5, this.m_20189_() + extraZ + (double) this.f_19796_.nextFloat() - 0.5, 0.0, 0.0, 0.0);
            }
        }
        if (this.isActuallySinging() && !this.m_20069_() && this.f_19797_ % 200 == 0) {
            this.m_5496_(IafSoundRegistry.SIREN_SONG, 2.0F, 1.0F);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    private void checkForPrey() {
        this.setSinging(true);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() != null && source.getEntity() instanceof LivingEntity) {
            this.triggerOtherSirens((LivingEntity) source.getEntity());
        }
        return super.m_6469_(source, amount);
    }

    public void triggerOtherSirens(LivingEntity aggressor) {
        for (Entity entity : this.m_9236_().m_45933_(this, this.m_20191_().inflate(12.0, 12.0, 12.0))) {
            if (entity instanceof EntitySiren) {
                ((EntitySiren) entity).m_6710_(aggressor);
                ((EntitySiren) entity).setAggressive(true);
                ((EntitySiren) entity).setSinging(false);
            }
        }
    }

    public void updateLure() {
        if (this.f_19797_ % 20 == 0) {
            for (LivingEntity entity : this.m_9236_().m_6443_(LivingEntity.class, this.m_20191_().inflate(50.0, 12.0, 50.0), SIREN_PREY)) {
                if (!isWearingEarplugs(entity)) {
                    EntityDataProvider.getCapability(entity).ifPresent(data -> {
                        if (data.sirenData.isCharmed || data.sirenData.charmedBy == null) {
                            data.sirenData.setCharmed(this);
                        }
                    });
                }
            }
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.m_7380_(tag);
        tag.putInt("HairColor", this.getHairColor());
        tag.putBoolean("Aggressive", this.isAgressive());
        tag.putInt("SingingPose", this.getSingingPose());
        tag.putBoolean("Singing", this.isSinging());
        tag.putBoolean("Swimming", this.isSwimming());
        tag.putBoolean("Passive", this.isCharmed());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.m_7378_(tag);
        this.setHairColor(tag.getInt("HairColor"));
        this.setAggressive(tag.getBoolean("Aggressive"));
        this.setSingingPose(tag.getInt("SingingPose"));
        this.setSinging(tag.getBoolean("Singing"));
        this.setSwimming(tag.getBoolean("Swimming"));
        this.setCharmed(tag.getBoolean("Passive"));
        this.setConfigurableAttributes();
    }

    public boolean isSinging() {
        return this.m_9236_().isClientSide ? (this.isSinging = this.f_19804_.get(SINGING)) : this.isSinging;
    }

    public void setSinging(boolean singing) {
        if (this.singCooldown > 0) {
            singing = false;
        }
        this.f_19804_.set(SINGING, singing);
        if (!this.m_9236_().isClientSide) {
            this.isSinging = singing;
            IceAndFire.sendMSGToAll(new MessageSirenSong(this.m_19879_(), singing));
        }
    }

    public boolean wantsToSing() {
        return this.isSinging() && this.m_20069_() && !this.isAgressive();
    }

    public boolean isActuallySinging() {
        return this.isSinging() && !this.wantsToSing();
    }

    @Override
    public boolean isSwimming() {
        return this.m_9236_().isClientSide ? (this.isSwimming = this.f_19804_.get(SWIMMING)) : this.isSwimming;
    }

    @Override
    public void setSwimming(boolean swimming) {
        this.f_19804_.set(SWIMMING, swimming);
        if (!this.m_9236_().isClientSide) {
            this.isSwimming = swimming;
        }
    }

    @Override
    public void setAggressive(boolean aggressive) {
        this.f_19804_.set(AGGRESSIVE, aggressive);
    }

    public boolean isAgressive() {
        return this.f_19804_.get(AGGRESSIVE);
    }

    public boolean isCharmed() {
        return this.f_19804_.get(CHARMED);
    }

    public void setCharmed(boolean aggressive) {
        this.f_19804_.set(CHARMED, aggressive);
    }

    public int getHairColor() {
        return this.f_19804_.get(HAIR_COLOR);
    }

    public void setHairColor(int hairColor) {
        this.f_19804_.set(HAIR_COLOR, hairColor);
    }

    public int getSingingPose() {
        return this.f_19804_.get(SING_POSE);
    }

    public void setSingingPose(int pose) {
        this.f_19804_.set(SING_POSE, Mth.clamp(pose, 0, 2));
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, IafConfig.sirenMaxHealth).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ATTACK_DAMAGE, 6.0);
    }

    @Override
    public void setConfigurableAttributes() {
        this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(IafConfig.sirenMaxHealth);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(HAIR_COLOR, 0);
        this.f_19804_.define(SING_POSE, 0);
        this.f_19804_.define(AGGRESSIVE, Boolean.FALSE);
        this.f_19804_.define(SINGING, Boolean.FALSE);
        this.f_19804_.define(SWIMMING, Boolean.FALSE);
        this.f_19804_.define(CHARMED, Boolean.FALSE);
        this.f_19804_.define(CLIMBING, (byte) 0);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        spawnDataIn = super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setHairColor(this.m_217043_().nextInt(3));
        this.setSingingPose(this.m_217043_().nextInt(3));
        return spawnDataIn;
    }

    public static float updateRotation(float angle, float targetAngle, float maxIncrease) {
        float f = Mth.wrapDegrees(targetAngle - angle);
        if (f > maxIncrease) {
            f = maxIncrease;
        }
        if (f < -maxIncrease) {
            f = -maxIncrease;
        }
        return angle + f;
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
        return new Animation[] { NO_ANIMATION, ANIMATION_BITE, ANIMATION_PULL };
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isAgressive() ? IafSoundRegistry.NAGA_IDLE : IafSoundRegistry.MERMAID_IDLE;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return this.isAgressive() ? IafSoundRegistry.NAGA_HURT : IafSoundRegistry.MERMAID_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return this.isAgressive() ? IafSoundRegistry.NAGA_DIE : IafSoundRegistry.MERMAID_DIE;
    }

    @Override
    public void travel(@NotNull Vec3 motion) {
        super.m_7023_(motion);
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean shouldFear() {
        return this.isAgressive();
    }

    class SwimmingMoveHelper extends MoveControl {

        private final EntitySiren siren = EntitySiren.this;

        public SwimmingMoveHelper() {
            super(EntitySiren.this);
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                double distanceX = this.f_24975_ - this.siren.m_20185_();
                double distanceY = this.f_24976_ - this.siren.m_20186_();
                double distanceZ = this.f_24977_ - this.siren.m_20189_();
                double distance = Math.abs(distanceX * distanceX + distanceZ * distanceZ);
                double distanceWithY = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
                distanceY /= distanceWithY;
                float angle = (float) (Math.atan2(distanceZ, distanceX) * 180.0 / Math.PI) - 90.0F;
                this.siren.m_146922_(this.m_24991_(this.siren.m_146908_(), angle, 30.0F));
                this.siren.m_7910_(1.0F);
                float f1 = 0.0F;
                float f2 = 0.0F;
                if (distance < (double) Math.max(1.0F, this.siren.m_20205_())) {
                    float f = this.siren.m_146908_() * (float) (Math.PI / 180.0);
                    f1 = (float) ((double) f1 - (double) (Mth.sin(f) * 0.35F));
                    f2 = (float) ((double) f2 + (double) (Mth.cos(f) * 0.35F));
                }
                this.siren.m_20256_(this.siren.m_20184_().add((double) f1, (double) this.siren.m_6113_() * distanceY * 0.1, (double) f2));
            } else if (this.f_24981_ == MoveControl.Operation.JUMPING) {
                this.siren.m_7910_((float) (this.f_24978_ * this.siren.m_21051_(Attributes.MOVEMENT_SPEED).getValue()));
                if (this.siren.m_20096_()) {
                    this.f_24981_ = MoveControl.Operation.WAIT;
                }
            } else {
                this.siren.m_7910_(0.0F);
            }
        }
    }
}