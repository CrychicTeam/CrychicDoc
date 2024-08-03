package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIRandomSwimming;
import com.github.alexthe666.alexsmobs.entity.ai.AquaticMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.entity.ai.SemiAquaticPathNavigator;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;

public class EntitySkelewag extends Monster implements IAnimatedEntity {

    public static final Animation ANIMATION_STAB = Animation.create(10);

    public static final Animation ANIMATION_SLASH = Animation.create(25);

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntitySkelewag.class, EntityDataSerializers.INT);

    private int animationTick;

    private Animation currentAnimation;

    public float prevOnLandProgress;

    public float onLandProgress;

    protected EntitySkelewag(EntityType<? extends Monster> monster, Level level) {
        super(monster, level);
        this.f_21364_ = 10;
        this.f_21342_ = new AquaticMoveController(this, 1.0F, 15.0F);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.m_21441_(BlockPathTypes.WATER_BORDER, 0.0F);
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new SemiAquaticPathNavigator(this, worldIn);
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.skelewagSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static boolean canSkelewagSpawn(EntityType<EntitySkelewag> type, ServerLevelAccessor levelAccessor, MobSpawnType mobSpawnType0, BlockPos below, RandomSource random) {
        return !levelAccessor.m_6425_(below.below()).is(FluidTags.WATER) ? false : levelAccessor.m_46791_() != Difficulty.PEACEFUL && m_219009_(levelAccessor, below, random) && (mobSpawnType0 == MobSpawnType.SPAWNER || random.nextInt(40) == 0 && levelAccessor.m_6425_(below).is(FluidTags.WATER));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.SKELEWAG_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.SKELEWAG_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.SKELEWAG_HURT.get();
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return this.m_9236_().getFluidState(pos).is(FluidTags.WATER) ? 10.0F + level.getLightLevelDependentMagicValue(pos) - 0.5F : super.getWalkTargetValue(pos, this.m_9236_());
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new TryFindWaterGoal(this));
        this.f_21345_.addGoal(2, new EntitySkelewag.AttackGoal(this));
        this.f_21345_.addGoal(3, new AnimalAIRandomSwimming(this, 1.0, 12, 5));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this, Drowned.class, EntitySkelewag.class));
        this.f_21346_.addGoal(2, new EntityAINearestTarget3D(this, Player.class, true));
        this.f_21346_.addGoal(3, new EntityAINearestTarget3D(this, Dolphin.class, true));
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(VARIANT, 0);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.MOVEMENT_SPEED, 0.45).add(Attributes.MAX_HEALTH, 20.0);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevOnLandProgress = this.onLandProgress;
        boolean onLand = !this.m_20072_() && this.m_20096_();
        if (onLand && this.onLandProgress < 5.0F) {
            this.onLandProgress++;
        }
        if (!onLand && this.onLandProgress > 0.0F) {
            this.onLandProgress--;
        }
        float targetXRot = 0.0F;
        if (this.m_20184_().length() > 0.09) {
            targetXRot = -((float) (Mth.atan2(this.m_20184_().y, this.m_20184_().horizontalDistance()) * 180.0F / (float) Math.PI));
        }
        if (targetXRot < this.m_146909_() - 5.0F) {
            targetXRot = this.m_146909_() - 5.0F;
        }
        if (targetXRot > this.m_146909_() + 5.0F) {
            targetXRot = this.m_146909_() + 5.0F;
        }
        this.m_146926_(targetXRot);
        if (!this.m_9236_().isClientSide && this.m_5448_() != null && this.m_20270_(this.m_5448_()) < 2.0F + this.m_5448_().m_20205_()) {
            this.m_21391_(this.m_5448_(), 350.0F, 200.0F);
            if (this.getAnimation() == ANIMATION_STAB && this.getAnimationTick() == 7 && this.m_142582_(this.m_5448_())) {
                float f1 = this.m_146908_() * (float) (Math.PI / 180.0);
                this.m_20256_(this.m_20184_().add((double) (-Mth.sin(f1) * 0.02F), 0.0, (double) (Mth.cos(f1) * 0.02F)));
                this.m_5448_().knockback(1.0, this.m_5448_().m_20185_() - this.m_20185_(), this.m_5448_().m_20189_() - this.m_20189_());
                this.m_5448_().hurt(this.m_269291_().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getBaseValue());
            }
            if (this.getAnimation() == ANIMATION_SLASH && this.getAnimationTick() % 5 == 0 && this.getAnimationTick() > 0 && this.getAnimationTick() < 25 && this.m_142582_(this.m_5448_())) {
                for (LivingEntity entity : this.m_9236_().m_45976_(LivingEntity.class, this.m_5448_().m_20191_().inflate(2.0))) {
                    if (!entity.m_20365_(this) && entity != this && !entity.m_7307_(this)) {
                        entity.hurt(this.m_269291_().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getBaseValue() * 0.5F);
                    }
                }
            }
        }
        if (this.onLandProgress >= 5.0F && this.m_20160_()) {
            this.m_20153_();
        }
        if (!this.m_20072_() && this.m_20096_() && this.f_19796_.nextFloat() < 0.2F) {
            this.m_20256_(this.m_20184_().add((double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.2F), 0.5, (double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.2F)));
            this.m_146922_(this.f_19796_.nextFloat() * 360.0F);
            this.m_5496_(AMSoundRegistry.SKELEWAG_HURT.get(), this.m_6121_(), this.m_6100_());
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    public int getVariant() {
        return this.f_19804_.get(VARIANT);
    }

    public void setVariant(int command) {
        this.f_19804_.set(VARIANT, command);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.setVariant(compound.getInt("Variant"));
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        return worldIn.m_45784_(this);
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.m_21515_() && this.m_20069_()) {
            this.m_19920_(this.m_6113_(), travelVector);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().scale(0.9));
            if (this.m_5448_() == null) {
                this.m_20256_(this.m_20184_().add(0.0, -0.005, 0.0));
            }
        } else {
            super.m_7023_(travelVector);
        }
    }

    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction moveFunc) {
        if (this.m_20363_(passenger)) {
            passenger.setYBodyRot(this.f_20883_);
            Vec3 vec = new Vec3(0.0, (double) (this.m_20206_() * 0.4F), (double) (this.m_20205_() * -0.2F)).xRot(-this.m_146909_() * (float) (Math.PI / 180.0)).yRot(-this.m_146908_() * (float) (Math.PI / 180.0));
            passenger.setPos(this.m_20185_() + vec.x, this.m_20186_() + vec.y + passenger.getMyRidingOffset(), this.m_20189_() + vec.z);
        }
    }

    public boolean canBeRiddenUnderFluidType(FluidType type, Entity rider) {
        return true;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.setVariant(this.m_217043_().nextFloat() < 0.3F ? 1 : 0);
        if (this.f_19796_.nextFloat() < 0.2F) {
            Drowned drowned = EntityType.DROWNED.create(this.m_9236_());
            drowned.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
            drowned.m_20359_(this);
            drowned.m_20329_(this);
            worldIn.addFreshEntityWithPassengers(drowned);
        }
        if (reason == MobSpawnType.STRUCTURE) {
            this.m_21446_(this.m_20183_(), 15);
        }
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int i) {
        this.animationTick = i;
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
        return new Animation[] { ANIMATION_SLASH, ANIMATION_STAB };
    }

    private class AttackGoal extends Goal {

        private final EntitySkelewag fish;

        private boolean isCharging = false;

        public AttackGoal(EntitySkelewag skelewag) {
            this.fish = skelewag;
        }

        @Override
        public boolean canUse() {
            return this.fish.m_5448_() != null;
        }

        @Override
        public void tick() {
            LivingEntity target = this.fish.m_5448_();
            if (target != null) {
                double dist = (double) this.fish.m_20270_(target);
                if (dist > 5.0) {
                    this.isCharging = true;
                }
                this.fish.m_21573_().moveTo(target, this.isCharging ? 1.3F : 0.8F);
                if (dist < (double) (5.0F + target.m_20205_() / 2.0F)) {
                    this.fish.setAnimation(this.isCharging ? EntitySkelewag.ANIMATION_STAB : (EntitySkelewag.this.f_19796_.nextBoolean() ? EntitySkelewag.ANIMATION_SLASH : EntitySkelewag.ANIMATION_STAB));
                    this.isCharging = false;
                }
            }
        }

        @Override
        public void stop() {
            this.isCharging = false;
        }
    }
}