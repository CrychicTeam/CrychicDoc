package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.ai.FlyingAITarget;
import com.github.alexthe666.iceandfire.entity.ai.SeaSerpentAIAttackMelee;
import com.github.alexthe666.iceandfire.entity.ai.SeaSerpentAIGetInWater;
import com.github.alexthe666.iceandfire.entity.ai.SeaSerpentAIJump;
import com.github.alexthe666.iceandfire.entity.ai.SeaSerpentAIMeleeJump;
import com.github.alexthe666.iceandfire.entity.ai.SeaSerpentAIRandomSwimming;
import com.github.alexthe666.iceandfire.entity.ai.SeaSerpentPathNavigator;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.EntityUtil;
import com.github.alexthe666.iceandfire.entity.util.IAnimalFear;
import com.github.alexthe666.iceandfire.entity.util.IHasCustomizableAttributes;
import com.github.alexthe666.iceandfire.entity.util.IMultipartEntity;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.enums.EnumSeaSerpent;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.IPlantable;
import org.jetbrains.annotations.NotNull;

public class EntitySeaSerpent extends Animal implements IAnimatedEntity, IMultipartEntity, IVillagerFear, IAnimalFear, IHasCustomizableAttributes {

    public static final Animation ANIMATION_BITE = Animation.create(15);

    public static final Animation ANIMATION_SPEAK = Animation.create(15);

    public static final Animation ANIMATION_ROAR = Animation.create(40);

    public static final int TIME_BETWEEN_ROARS = 300;

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntitySeaSerpent.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(EntitySeaSerpent.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Boolean> JUMPING = SynchedEntityData.defineId(EntitySeaSerpent.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> BREATHING = SynchedEntityData.defineId(EntitySeaSerpent.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> ANCIENT = SynchedEntityData.defineId(EntitySeaSerpent.class, EntityDataSerializers.BOOLEAN);

    private static final Predicate<Entity> NOT_SEA_SERPENT = new Predicate<Entity>() {

        public boolean apply(@Nullable Entity entity) {
            return entity instanceof LivingEntity && !(entity instanceof EntitySeaSerpent) && DragonUtils.isAlive((LivingEntity) entity);
        }
    };

    private static final Predicate<Entity> NOT_SEA_SERPENT_IN_WATER = new Predicate<Entity>() {

        public boolean apply(@Nullable Entity entity) {
            return entity instanceof LivingEntity && !(entity instanceof EntitySeaSerpent) && DragonUtils.isAlive((LivingEntity) entity) && entity.isInWaterOrBubble();
        }
    };

    public int swimCycle;

    public float jumpProgress = 0.0F;

    public float wantJumpProgress = 0.0F;

    public float jumpRot = 0.0F;

    public float prevJumpRot = 0.0F;

    public float breathProgress = 0.0F;

    public boolean attackDecision = false;

    private int animationTick;

    private Animation currentAnimation;

    private EntityMutlipartPart[] segments = new EntityMutlipartPart[9];

    private float lastScale;

    private boolean isLandNavigator;

    private boolean changedSwimBehavior = false;

    public int jumpCooldown = 0;

    private int ticksSinceRoar = 0;

    private boolean isBreathing;

    private final float[] tailYaw = new float[5];

    private final float[] prevTailYaw = new float[5];

    private final float[] tailPitch = new float[5];

    private final float[] prevTailPitch = new float[5];

    public EntitySeaSerpent(EntityType<EntitySeaSerpent> t, Level worldIn) {
        super(t, worldIn);
        this.switchNavigator(false);
        this.f_19811_ = true;
        this.resetParts(1.0F);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
    }

    private static BlockPos clampBlockPosToWater(Entity entity, Level world, BlockPos pos) {
        BlockPos topY = new BlockPos(pos.m_123341_(), entity.getBlockY(), pos.m_123343_());
        BlockPos bottomY = new BlockPos(pos.m_123341_(), entity.getBlockY(), pos.m_123343_());
        while (isWaterBlock(world, topY) && topY.m_123342_() < world.m_151558_()) {
            topY = topY.above();
        }
        while (isWaterBlock(world, bottomY) && bottomY.m_123342_() > 0) {
            bottomY = bottomY.below();
        }
        return new BlockPos(pos.m_123341_(), Mth.clamp(pos.m_123342_(), bottomY.m_123342_() + 1, topY.m_123342_() - 1), pos.m_123343_());
    }

    public static boolean isWaterBlock(Level world, BlockPos pos) {
        return world.getFluidState(pos).is(FluidTags.WATER);
    }

    @NotNull
    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new SeaSerpentAIGetInWater(this));
        this.f_21345_.addGoal(1, new SeaSerpentAIMeleeJump(this));
        this.f_21345_.addGoal(1, new SeaSerpentAIAttackMelee(this, 1.0, true));
        this.f_21345_.addGoal(2, new SeaSerpentAIRandomSwimming(this, 1.0, 2));
        this.f_21345_.addGoal(3, new SeaSerpentAIJump(this, 4));
        this.f_21345_.addGoal(4, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this, EntityMutlipartPart.class).setAlertOthers());
        this.f_21346_.addGoal(2, new FlyingAITarget(this, LivingEntity.class, 150, false, false, NOT_SEA_SERPENT_IN_WATER));
        this.f_21346_.addGoal(3, new FlyingAITarget(this, Player.class, 0, false, false, NOT_SEA_SERPENT));
    }

    @Override
    public int getExperienceReward() {
        return this.isAncient() ? 30 : 15;
    }

    @Override
    public void pushEntities() {
        List<Entity> entities = this.m_9236_().m_45933_(this, this.m_20191_().expandTowards(0.2F, 0.0, 0.2F));
        entities.stream().filter(entity -> !(entity instanceof EntityMutlipartPart) && entity.isPushable()).forEach(entity -> entity.push(this));
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new GroundPathNavigation(this, this.m_9236_());
            this.f_21344_.setCanFloat(true);
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new EntitySeaSerpent.SwimmingMoveHelper(this);
            this.f_21344_ = new SeaSerpentPathNavigator(this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    public boolean isDirectPathBetweenPoints(BlockPos pos) {
        Vec3 vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        Vec3 bector3d1 = new Vec3((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5);
        return this.m_9236_().m_45547_(new ClipContext(vector3d, bector3d1, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
    }

    @NotNull
    @Override
    public MobType getMobType() {
        return MobType.WATER;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, IafConfig.seaSerpentBaseHealth).add(Attributes.MOVEMENT_SPEED, 0.15).add(Attributes.ATTACK_DAMAGE, 1.0).add(Attributes.FOLLOW_RANGE, (double) Math.min(2048, IafConfig.dragonTargetSearchLength)).add(Attributes.ARMOR, 3.0);
    }

    @Override
    public void setConfigurableAttributes() {
        this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(IafConfig.seaSerpentBaseHealth);
        this.m_21051_(Attributes.FOLLOW_RANGE).setBaseValue((double) Math.min(2048, IafConfig.dragonTargetSearchLength));
        this.updateAttributes();
    }

    public void resetParts(float scale) {
        this.clearParts();
        this.segments = new EntityMutlipartPart[9];
        for (int i = 0; i < this.segments.length; i++) {
            if (i > 3) {
                Entity parentToSet = (Entity) (i <= 4 ? this : this.segments[i - 1]);
                this.segments[i] = new EntitySlowPart(parentToSet, 0.5F * scale, 180.0F, 0.0F, 0.5F * scale, 0.5F * scale, 1.0F);
            } else {
                Entity parentToSet = (Entity) (i == 0 ? this : this.segments[i - 1]);
                this.segments[i] = new EntitySlowPart(parentToSet, -0.4F * scale, 180.0F, 0.0F, 0.45F * scale, 0.4F * scale, 1.0F);
            }
            this.segments[i].m_20359_(this);
        }
    }

    public void onUpdateParts() {
        for (EntityMutlipartPart entity : this.segments) {
            EntityUtil.updatePart(entity, this);
        }
    }

    private void clearParts() {
        for (EntityMutlipartPart entity : this.segments) {
            if (entity != null) {
                entity.remove(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    public void remove(@NotNull Entity.RemovalReason reason) {
        this.clearParts();
        super.m_142687_(reason);
    }

    @NotNull
    @Override
    public EntityDimensions getDimensions(@NotNull Pose poseIn) {
        return this.m_6095_().getDimensions().scale(this.getScale());
    }

    @Override
    public float getScale() {
        return this.getSeaSerpentScale();
    }

    @Override
    public void refreshDimensions() {
        super.m_6210_();
        float scale = this.getSeaSerpentScale();
        if (scale != this.lastScale) {
            this.resetParts(this.getSeaSerpentScale());
        }
        this.lastScale = scale;
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        if (this.getAnimation() != ANIMATION_BITE) {
            this.setAnimation(ANIMATION_BITE);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.jumpCooldown > 0) {
            this.jumpCooldown--;
        }
        this.refreshDimensions();
        this.onUpdateParts();
        if (this.m_20069_()) {
            this.spawnParticlesAroundEntity(ParticleTypes.BUBBLE, this, (int) this.getSeaSerpentScale());
        }
        if (!this.m_9236_().isClientSide && this.m_9236_().m_46791_() == Difficulty.PEACEFUL) {
            this.remove(Entity.RemovalReason.DISCARDED);
        }
        if (this.m_5448_() != null && !this.m_5448_().isAlive()) {
            this.m_6710_(null);
        }
        for (int i = 0; i < this.tailYaw.length; i++) {
            this.prevTailYaw[i] = this.tailYaw[i];
        }
        for (int i = 0; i < this.tailPitch.length; i++) {
            this.prevTailPitch[i] = this.tailPitch[i];
        }
        this.tailYaw[0] = this.f_20883_;
        this.tailPitch[0] = this.m_146909_();
        for (int i = 1; i < this.tailYaw.length; i++) {
            this.tailYaw[i] = this.prevTailYaw[i - 1];
        }
        for (int i = 1; i < this.tailPitch.length; i++) {
            this.tailPitch[i] = this.prevTailPitch[i - 1];
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    public float getPieceYaw(int index, float partialTicks) {
        return index < this.segments.length && index >= 0 ? this.prevTailYaw[index] + (this.tailYaw[index] - this.prevTailYaw[index]) * partialTicks : 0.0F;
    }

    public float getPiecePitch(int index, float partialTicks) {
        return index < this.segments.length && index >= 0 ? this.prevTailPitch[index] + (this.tailPitch[index] - this.prevTailPitch[index]) * partialTicks : 0.0F;
    }

    private void spawnParticlesAroundEntity(ParticleOptions type, Entity entity, int count) {
        for (int i = 0; i < count; i++) {
            int x = (int) Math.round(entity.getX() + (double) (this.f_19796_.nextFloat() * entity.getBbWidth() * 2.0F) - (double) entity.getBbWidth());
            int y = (int) Math.round(entity.getY() + 0.5 + (double) (this.f_19796_.nextFloat() * entity.getBbHeight()));
            int z = (int) Math.round(entity.getZ() + (double) (this.f_19796_.nextFloat() * entity.getBbWidth() * 2.0F) - (double) entity.getBbWidth());
            if (this.m_9236_().getBlockState(new BlockPos(x, y, z)).m_60713_(Blocks.WATER)) {
                this.m_9236_().addParticle(type, (double) x, (double) y, (double) z, 0.0, 0.0, 0.0);
            }
        }
    }

    private void spawnSlamParticles(ParticleOptions type) {
        for (int i = 0; (float) i < this.getSeaSerpentScale() * 3.0F; i++) {
            for (int i1 = 0; i1 < 5; i1++) {
                double motionX = this.m_217043_().nextGaussian() * 0.07;
                double motionY = this.m_217043_().nextGaussian() * 0.07;
                double motionZ = this.m_217043_().nextGaussian() * 0.07;
                float radius = 1.25F * this.getSeaSerpentScale();
                float angle = (float) (Math.PI / 180.0) * this.f_20883_ + (float) i1 * 1.0F;
                double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
                double extraY = 0.8F;
                double extraZ = (double) (radius * Mth.cos(angle));
                if (this.m_9236_().isClientSide) {
                    this.m_9236_().addParticle(type, true, this.m_20185_() + extraX, this.m_20186_() + extraY, this.m_20189_() + extraZ, motionX, motionY, motionZ);
                }
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(VARIANT, 0);
        this.f_19804_.define(SCALE, 0.0F);
        this.f_19804_.define(JUMPING, false);
        this.f_19804_.define(BREATHING, false);
        this.f_19804_.define(ANCIENT, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putInt("TicksSinceRoar", this.ticksSinceRoar);
        compound.putInt("JumpCooldown", this.jumpCooldown);
        compound.putFloat("Scale", this.getSeaSerpentScale());
        compound.putBoolean("JumpingOutOfWater", this.isJumpingOutOfWater());
        compound.putBoolean("AttackDecision", this.attackDecision);
        compound.putBoolean("Breathing", this.isBreathing());
        compound.putBoolean("Ancient", this.isAncient());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.ticksSinceRoar = compound.getInt("TicksSinceRoar");
        this.jumpCooldown = compound.getInt("JumpCooldown");
        this.setSeaSerpentScale(compound.getFloat("Scale"));
        this.setJumpingOutOfWater(compound.getBoolean("JumpingOutOfWater"));
        this.attackDecision = compound.getBoolean("AttackDecision");
        this.setBreathing(compound.getBoolean("Breathing"));
        this.setAncient(compound.getBoolean("Ancient"));
        this.setConfigurableAttributes();
    }

    private void updateAttributes() {
        this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(Math.min(0.25, 0.15 * (double) this.getSeaSerpentScale() * (double) this.getAncientModifier()));
        this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue(Math.max(4.0, IafConfig.seaSerpentAttackStrength * (double) this.getSeaSerpentScale() * (double) this.getAncientModifier()));
        this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(Math.max(10.0, IafConfig.seaSerpentBaseHealth * (double) this.getSeaSerpentScale() * (double) this.getAncientModifier()));
        this.m_21051_(Attributes.FOLLOW_RANGE).setBaseValue((double) Math.min(2048, IafConfig.dragonTargetSearchLength));
        this.m_5634_(30.0F * this.getSeaSerpentScale());
    }

    private float getAncientModifier() {
        return this.isAncient() ? 1.5F : 1.0F;
    }

    public float getSeaSerpentScale() {
        return this.f_19804_.get(SCALE);
    }

    private void setSeaSerpentScale(float scale) {
        this.f_19804_.set(SCALE, scale);
    }

    public int getVariant() {
        return this.f_19804_.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.set(VARIANT, variant);
    }

    public boolean isJumpingOutOfWater() {
        return this.f_19804_.get(JUMPING);
    }

    public void setJumpingOutOfWater(boolean jump) {
        this.f_19804_.set(JUMPING, jump);
    }

    public boolean isAncient() {
        return this.f_19804_.get(ANCIENT);
    }

    public void setAncient(boolean ancient) {
        this.f_19804_.set(ANCIENT, ancient);
    }

    public boolean isBreathing() {
        if (this.m_9236_().isClientSide) {
            boolean breathing = this.f_19804_.get(BREATHING);
            this.isBreathing = breathing;
            return breathing;
        } else {
            return this.isBreathing;
        }
    }

    public void setBreathing(boolean breathing) {
        this.f_19804_.set(BREATHING, breathing);
        if (!this.m_9236_().isClientSide) {
            this.isBreathing = breathing;
        }
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.m_9236_().isClientSide && this.m_9236_().m_46791_() == Difficulty.PEACEFUL && this.m_5448_() instanceof Player) {
            this.m_6710_(null);
        }
        boolean breathing = this.isBreathing() && this.getAnimation() != ANIMATION_BITE && this.getAnimation() != ANIMATION_ROAR;
        boolean jumping = !this.m_20069_() && !this.m_20096_() && this.m_20184_().y >= 0.0;
        boolean wantJumping = false;
        boolean ground = !this.m_20069_() && this.m_20096_();
        boolean prevJumping = this.isJumpingOutOfWater();
        this.ticksSinceRoar++;
        this.jumpCooldown++;
        this.prevJumpRot = this.jumpRot;
        if (this.ticksSinceRoar > 300 && this.isAtSurface() && this.getAnimation() != ANIMATION_BITE && this.jumpProgress == 0.0F && !this.isJumpingOutOfWater()) {
            this.setAnimation(ANIMATION_ROAR);
            this.ticksSinceRoar = 0;
        }
        if (this.getAnimation() == ANIMATION_ROAR && this.getAnimationTick() == 1) {
            this.m_5496_(IafSoundRegistry.SEA_SERPENT_ROAR, this.m_6121_() + 1.0F, 1.0F);
        }
        if (this.getAnimation() == ANIMATION_BITE && this.getAnimationTick() == 5) {
            this.m_5496_(IafSoundRegistry.SEA_SERPENT_BITE, this.m_6121_(), 1.0F);
        }
        if (this.isJumpingOutOfWater() && isWaterBlock(this.m_9236_(), this.m_20183_().above(2))) {
            this.setJumpingOutOfWater(false);
        }
        if (this.swimCycle < 38) {
            this.swimCycle += 2;
        } else {
            this.swimCycle = 0;
        }
        if (breathing && this.breathProgress < 20.0F) {
            this.breathProgress += 0.5F;
        } else if (!breathing && this.breathProgress > 0.0F) {
            this.breathProgress -= 0.5F;
        }
        if (jumping && this.jumpProgress < 10.0F) {
            this.jumpProgress += 0.5F;
        } else if (!jumping && this.jumpProgress > 0.0F) {
            this.jumpProgress -= 0.5F;
        }
        if (wantJumping && this.wantJumpProgress < 10.0F) {
            this.wantJumpProgress += 2.0F;
        } else if (!wantJumping && this.wantJumpProgress > 0.0F) {
            this.wantJumpProgress -= 2.0F;
        }
        if (this.isJumpingOutOfWater() && this.jumpRot < 1.0F) {
            this.jumpRot += 0.1F;
        } else if (!this.isJumpingOutOfWater() && this.jumpRot > 0.0F) {
            this.jumpRot -= 0.1F;
        }
        if (prevJumping && !this.isJumpingOutOfWater()) {
            this.m_5496_(IafSoundRegistry.SEA_SERPENT_SPLASH, 5.0F, 0.75F);
            this.spawnSlamParticles(ParticleTypes.BUBBLE);
            this.doSplashDamage();
        }
        if (!ground && this.isLandNavigator) {
            this.switchNavigator(false);
        }
        if (ground && !this.isLandNavigator) {
            this.switchNavigator(true);
        }
        this.m_146926_(Mth.clamp((float) this.m_20184_().y * 20.0F, -90.0F, 90.0F));
        if (this.changedSwimBehavior) {
            this.changedSwimBehavior = false;
        }
        if (!this.m_9236_().isClientSide) {
            if (this.attackDecision) {
                this.setBreathing(false);
            }
            if (this.m_5448_() == null || this.getAnimation() == ANIMATION_ROAR) {
                this.setBreathing(false);
            } else if (!this.attackDecision) {
                if (!this.m_5448_().m_20069_() || !this.m_142582_(this.m_5448_()) || this.m_20270_(this.m_5448_()) < 30.0F * this.getSeaSerpentScale()) {
                    this.attackDecision = true;
                }
                if (!this.attackDecision) {
                    this.shoot(this.m_5448_());
                }
            } else if (this.m_20280_(this.m_5448_()) > (double) (200.0F * this.getSeaSerpentScale())) {
                this.attackDecision = false;
            }
        }
        if (this.getAnimation() == ANIMATION_BITE && this.m_5448_() != null && (this.isTouchingMob(this.m_5448_()) || this.m_20280_(this.m_5448_()) < 50.0)) {
            this.hurtMob(this.m_5448_());
        }
        this.breakBlock();
        if (!this.m_9236_().isClientSide && this.m_20159_() && this.m_20201_() instanceof Boat) {
            Boat boat = (Boat) this.m_20201_();
            boat.m_142687_(Entity.RemovalReason.KILLED);
            this.m_8127_();
        }
    }

    private boolean isAtSurface() {
        BlockPos pos = this.m_20183_();
        return isWaterBlock(this.m_9236_(), pos.below()) && !isWaterBlock(this.m_9236_(), pos.above());
    }

    private void doSplashDamage() {
        double getWidth = 2.0 * (double) this.getSeaSerpentScale();
        for (Entity entity : this.m_9236_().getEntities(this, this.m_20191_().inflate(getWidth, getWidth * 0.5, getWidth), NOT_SEA_SERPENT)) {
            if (entity instanceof LivingEntity && DragonUtils.isAlive((LivingEntity) entity)) {
                entity.hurt(this.m_9236_().damageSources().mobAttack(this), (float) ((int) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue()));
                this.destroyBoat(entity);
                double xRatio = this.m_20185_() - entity.getX();
                double zRatio = this.m_20189_() - entity.getZ();
                float f = Mth.sqrt((float) (xRatio * xRatio + zRatio * zRatio));
                float strength = 0.3F * this.getSeaSerpentScale();
                entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.5, 1.0, 0.5));
                entity.setDeltaMovement(entity.getDeltaMovement().add(xRatio / (double) f * (double) strength, (double) strength, zRatio / (double) f * (double) strength));
            }
        }
    }

    public void destroyBoat(Entity sailor) {
        if (sailor.getVehicle() != null && sailor.getVehicle() instanceof Boat && !this.m_9236_().isClientSide) {
            Boat boat = (Boat) sailor.getVehicle();
            boat.m_142687_(Entity.RemovalReason.KILLED);
            if (this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                for (int i = 0; i < 3; i++) {
                    boat.m_5552_(new ItemStack(boat.getVariant().getPlanks().asItem()), 0.0F);
                }
                for (int j = 0; j < 2; j++) {
                    boat.m_19983_(new ItemStack(Items.STICK));
                }
            }
        }
    }

    private boolean isPreyAtSurface() {
        if (this.m_5448_() != null) {
            BlockPos pos = this.m_5448_().m_20183_();
            return !isWaterBlock(this.m_9236_(), pos.above((int) Math.ceil((double) this.m_5448_().m_20206_())));
        } else {
            return false;
        }
    }

    private void hurtMob(LivingEntity entity) {
        if (this.getAnimation() == ANIMATION_BITE && entity != null && this.getAnimationTick() == 6) {
            this.m_5448_().hurt(this.m_9236_().damageSources().mobAttack(this), (float) ((int) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue()));
            this.attackDecision = this.m_217043_().nextBoolean();
        }
    }

    public void moveJumping() {
        float velocity = 0.5F;
        double x = (double) (-Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0)) * Mth.cos(this.m_146909_() * (float) (Math.PI / 180.0)));
        double z = (double) (Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0)) * Mth.cos(this.m_146909_() * (float) (Math.PI / 180.0)));
        float f = Mth.sqrt((float) (x * x + z * z));
        x /= (double) f;
        z /= (double) f;
        x *= (double) velocity;
        z *= (double) velocity;
        this.m_20334_(x, this.m_20184_().y, z);
    }

    public boolean isTouchingMob(Entity entity) {
        if (this.m_20191_().expandTowards(1.0, 1.0, 1.0).intersects(entity.getBoundingBox())) {
            return true;
        } else {
            for (Entity segment : this.segments) {
                if (segment.getBoundingBox().expandTowards(1.0, 1.0, 1.0).intersects(entity.getBoundingBox())) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    public void breakBlock() {
        if (IafConfig.seaSerpentGriefing) {
            for (int a = (int) Math.round(this.m_20191_().minX) - 2; a <= (int) Math.round(this.m_20191_().maxX) + 2; a++) {
                for (int b = (int) Math.round(this.m_20191_().minY) - 1; b <= (int) Math.round(this.m_20191_().maxY) + 2 && b <= 127; b++) {
                    for (int c = (int) Math.round(this.m_20191_().minZ) - 2; c <= (int) Math.round(this.m_20191_().maxZ) + 2; c++) {
                        BlockPos pos = new BlockPos(a, b, c);
                        BlockState state = this.m_9236_().getBlockState(pos);
                        FluidState fluidState = this.m_9236_().getFluidState(pos);
                        Block block = state.m_60734_();
                        if (!state.m_60795_() && !state.m_60808_(this.m_9236_(), pos).isEmpty() && (state.m_60734_() instanceof IPlantable || state.m_60734_() instanceof LeavesBlock) && fluidState.isEmpty() && block != Blocks.AIR && !this.m_9236_().isClientSide) {
                            this.m_9236_().m_46961_(pos, true);
                        }
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        spawnDataIn = super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setVariant(this.m_217043_().nextInt(7));
        boolean ancient = this.m_217043_().nextInt(16) == 1;
        if (ancient) {
            this.setAncient(true);
            this.setSeaSerpentScale(6.0F + this.m_217043_().nextFloat() * 3.0F);
        } else {
            this.setSeaSerpentScale(1.5F + this.m_217043_().nextFloat() * 4.0F);
        }
        this.updateAttributes();
        return spawnDataIn;
    }

    public void onWorldSpawn(RandomSource random) {
        this.setVariant(random.nextInt(7));
        boolean ancient = random.nextInt(15) == 1;
        if (ancient) {
            this.setAncient(true);
            this.setSeaSerpentScale(6.0F + random.nextFloat() * 3.0F);
        } else {
            this.setSeaSerpentScale(1.5F + random.nextFloat() * 4.0F);
        }
        this.updateAttributes();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverWorld, @NotNull AgeableMob ageable) {
        return null;
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
        return new Animation[] { ANIMATION_BITE, ANIMATION_ROAR, ANIMATION_SPEAK };
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.SEA_SERPENT_IDLE;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return IafSoundRegistry.SEA_SERPENT_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.SEA_SERPENT_DIE;
    }

    @Override
    public void playAmbientSound() {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.m_8032_();
    }

    @Override
    protected void playHurtSound(@NotNull DamageSource source) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.m_6677_(source);
    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return true;
    }

    public boolean isBlinking() {
        return this.f_19797_ % 50 > 43;
    }

    private void shoot(LivingEntity entity) {
        if (!this.attackDecision) {
            if (!this.m_20069_()) {
                this.setBreathing(false);
                this.attackDecision = true;
            }
            if (this.isBreathing()) {
                if (this.f_19797_ % 40 == 0) {
                    this.m_5496_(IafSoundRegistry.SEA_SERPENT_BREATH, 4.0F, 1.0F);
                }
                if (this.f_19797_ % 10 == 0) {
                    this.m_146922_(this.f_20883_);
                    float f1 = 0.0F;
                    float f2 = 0.0F;
                    float f3 = 0.0F;
                    float headPosX = f1 + (float) (this.segments[0].m_20185_() + (double) (1.3F * this.getSeaSerpentScale() * Mth.cos((float) ((double) (this.m_146908_() + 90.0F) * Math.PI / 180.0))));
                    float headPosZ = f2 + (float) (this.segments[0].m_20189_() + (double) (1.3F * this.getSeaSerpentScale() * Mth.sin((float) ((double) (this.m_146908_() + 90.0F) * Math.PI / 180.0))));
                    float headPosY = f3 + (float) (this.segments[0].m_20186_() + (double) (0.2F * this.getSeaSerpentScale()));
                    double d2 = entity.m_20185_() - (double) headPosX;
                    double d3 = entity.m_20186_() - (double) headPosY;
                    double d4 = entity.m_20189_() - (double) headPosZ;
                    float inaccuracy = 1.0F;
                    d2 += this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy;
                    d3 += this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy;
                    d4 += this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy;
                    EntitySeaSerpentBubbles entitylargefireball = new EntitySeaSerpentBubbles(IafEntityRegistry.SEA_SERPENT_BUBBLES.get(), this.m_9236_(), this, d2, d3, d4);
                    entitylargefireball.m_6034_((double) headPosX, (double) headPosY, (double) headPosZ);
                    if (!this.m_9236_().isClientSide) {
                        this.m_9236_().m_7967_(entitylargefireball);
                    }
                    if (!entity.isAlive() || entity == null) {
                        this.setBreathing(false);
                        this.attackDecision = this.m_217043_().nextBoolean();
                    }
                }
            } else {
                this.setBreathing(true);
            }
        }
        this.m_21391_(entity, 360.0F, 360.0F);
    }

    public EnumSeaSerpent getEnum() {
        switch(this.getVariant()) {
            case 1:
                return EnumSeaSerpent.BRONZE;
            case 2:
                return EnumSeaSerpent.DEEPBLUE;
            case 3:
                return EnumSeaSerpent.GREEN;
            case 4:
                return EnumSeaSerpent.PURPLE;
            case 5:
                return EnumSeaSerpent.RED;
            case 6:
                return EnumSeaSerpent.TEAL;
            default:
                return EnumSeaSerpent.BLUE;
        }
    }

    @Override
    public void travel(@NotNull Vec3 vec) {
        if (this.m_21515_() && this.m_20069_()) {
            this.m_19920_(this.m_6113_(), vec);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().scale(0.9));
            if (this.m_5448_() == null) {
                this.m_20256_(this.m_20184_().add(0.0, -0.005, 0.0));
            }
        } else {
            super.m_7023_(vec);
        }
    }

    @Override
    public boolean killedEntity(@NotNull ServerLevel world, @NotNull LivingEntity entity) {
        this.attackDecision = this.m_217043_().nextBoolean();
        return this.attackDecision;
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
    public int getMaxFallDistance() {
        return 1000;
    }

    public void onJumpHit(LivingEntity target) {
    }

    public boolean shouldUseJumpAttack(LivingEntity attackTarget) {
        return !attackTarget.m_20069_() || this.isPreyAtSurface();
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource source) {
        DamageSources damageSources = this.m_9236_().damageSources();
        return source == damageSources.fall() || source == damageSources.drown() || source == damageSources.inWall() || source.getEntity() != null && source == damageSources.fallingBlock(source.getEntity()) || source == damageSources.lava() || source.is(DamageTypes.IN_FIRE) || super.m_6673_(source);
    }

    public class SwimmingMoveHelper extends MoveControl {

        private final EntitySeaSerpent dolphin;

        public SwimmingMoveHelper(EntitySeaSerpent dolphinIn) {
            super(dolphinIn);
            this.dolphin = dolphinIn;
        }

        @Override
        public void tick() {
            if (this.dolphin.m_20069_()) {
                this.dolphin.m_20256_(this.dolphin.m_20184_().add(0.0, 0.005, 0.0));
            }
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO && !this.dolphin.m_21573_().isDone()) {
                double d0 = this.f_24975_ - this.dolphin.m_20185_();
                double d1 = this.f_24976_ - this.dolphin.m_20186_();
                double d2 = this.f_24977_ - this.dolphin.m_20189_();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                if (d3 < 2.5000003E-7F) {
                    this.f_24974_.setZza(0.0F);
                } else {
                    float f = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F;
                    this.dolphin.m_146922_(this.m_24991_(this.dolphin.m_146908_(), f, 10.0F));
                    this.dolphin.f_20883_ = this.dolphin.m_146908_();
                    this.dolphin.f_20885_ = this.dolphin.m_146908_();
                    float f1 = (float) (this.f_24978_ * 3.0);
                    if (this.dolphin.m_20069_()) {
                        this.dolphin.m_7910_(f1 * 0.02F);
                        float f2 = -((float) (Mth.atan2(d1, (double) Mth.sqrt((float) (d0 * d0 + d2 * d2))) * 180.0F / (float) Math.PI));
                        f2 = Mth.clamp(Mth.wrapDegrees(f2), -85.0F, 85.0F);
                        this.dolphin.m_20256_(this.dolphin.m_20184_().add(0.0, (double) this.dolphin.m_6113_() * d1 * 0.6, 0.0));
                        this.dolphin.m_146926_(this.m_24991_(this.dolphin.m_146909_(), f2, 1.0F));
                        float f3 = Mth.cos(this.dolphin.m_146909_() * (float) (Math.PI / 180.0));
                        float f4 = Mth.sin(this.dolphin.m_146909_() * (float) (Math.PI / 180.0));
                        this.dolphin.f_20902_ = f3 * f1;
                        this.dolphin.f_20901_ = -f4 * f1;
                    } else {
                        this.dolphin.m_7910_(f1 * 0.1F);
                    }
                }
            } else {
                this.dolphin.m_7910_(0.0F);
                this.dolphin.m_21570_(0.0F);
                this.dolphin.m_21567_(0.0F);
                this.dolphin.m_21564_(0.0F);
            }
        }
    }
}