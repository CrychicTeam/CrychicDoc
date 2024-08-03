package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.entity.ai.OrcaAIJump;
import com.github.alexthe666.alexsmobs.entity.ai.OrcaAIMeleeJump;
import com.github.alexthe666.alexsmobs.entity.ai.SwimmerJumpPathNavigator;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.BreathAirGoal;
import net.minecraft.world.entity.ai.goal.FollowBoatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class EntityOrca extends TamableAnimal implements IAnimatedEntity {

    public static final Animation ANIMATION_BITE = Animation.create(8);

    public static final Animation ANIMATION_TAILSWING = Animation.create(20);

    private static final EntityDataAccessor<Integer> MOISTNESS = SynchedEntityData.defineId(EntityOrca.class, EntityDataSerializers.INT);

    private static final TargetingConditions PLAYER_PREDICATE = TargetingConditions.forNonCombat().range(24.0).ignoreLineOfSight();

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityOrca.class, EntityDataSerializers.INT);

    public int jumpCooldown;

    private int animationTick;

    private Animation currentAnimation;

    private int blockBreakCounter;

    public static final Predicate<LivingEntity> TARGET_BABY = animal -> animal.isBaby();

    protected EntityOrca(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.f_21342_ = new EntityOrca.MoveHelperController(this);
        this.f_21365_ = new SmoothSwimmingLookControl(this, 10);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.m_21824_();
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.orcaSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 60.0).add(Attributes.FOLLOW_RANGE, 64.0).add(Attributes.ARMOR, 0.0).add(Attributes.ATTACK_DAMAGE, 10.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.7F).add(Attributes.MOVEMENT_SPEED, 1.35F);
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new SwimmerJumpPathNavigator(this, worldIn);
    }

    public int getMoistness() {
        return this.f_19804_.get(MOISTNESS);
    }

    public void setMoistness(int p_211137_1_) {
        this.f_19804_.set(MOISTNESS, p_211137_1_);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(MOISTNESS, 2400);
        this.f_19804_.define(VARIANT, 0);
    }

    public int getVariant() {
        return this.f_19804_.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.set(VARIANT, variant);
    }

    public int determineVariant(BlockPos coords) {
        if (coords == null) {
            return 0;
        } else if (coords.m_123343_() < 0) {
            return coords.m_123341_() < 0 ? 1 : 0;
        } else {
            return coords.m_123341_() < 0 ? 3 : 2;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.ORCA_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.ORCA_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.ORCA_DIE.get();
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new BreathAirGoal(this));
        this.f_21345_.addGoal(1, new TryFindWaterGoal(this));
        this.f_21345_.addGoal(2, new EntityOrca.SwimWithPlayerGoal(this, 4.0));
        this.f_21345_.addGoal(4, new RandomSwimmingGoal(this, 1.0, 10));
        this.f_21345_.addGoal(4, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(5, new OrcaAIJump(this, 10));
        this.f_21345_.addGoal(6, new OrcaAIMeleeJump(this));
        this.f_21345_.addGoal(6, new OrcaAIMelee(this, 1.2F, true));
        this.f_21345_.addGoal(8, new FollowBoatGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.f_21346_.addGoal(2, new EntityAINearestTarget3D(this, EntityCachalotWhale.class, 25, false, false, TARGET_BABY));
        this.f_21346_.addGoal(3, new EntityAINearestTarget3D(this, LivingEntity.class, 200, false, true, AMEntityRegistry.buildPredicateFromTag(AMTagRegistry.ORCA_TARGETS)));
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
        return new Animation[] { ANIMATION_BITE, ANIMATION_TAILSWING };
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
    public void customServerAiStep() {
        super.m_8024_();
        this.breakBlock();
    }

    public void breakBlock() {
        if (this.blockBreakCounter > 0) {
            this.blockBreakCounter--;
        } else {
            boolean flag = false;
            if (!this.m_9236_().isClientSide && this.blockBreakCounter == 0) {
                for (int a = (int) Math.round(this.m_20191_().minX); a <= (int) Math.round(this.m_20191_().maxX); a++) {
                    for (int b = (int) Math.round(this.m_20191_().minY) - 1; b <= (int) Math.round(this.m_20191_().maxY) + 1 && b <= 127; b++) {
                        for (int c = (int) Math.round(this.m_20191_().minZ); c <= (int) Math.round(this.m_20191_().maxZ); c++) {
                            BlockPos pos = new BlockPos(a, b, c);
                            BlockState state = this.m_9236_().getBlockState(pos);
                            FluidState fluidState = this.m_9236_().getFluidState(pos);
                            Block block = state.m_60734_();
                            if (!state.m_60795_() && !state.m_60808_(this.m_9236_(), pos).isEmpty() && state.m_204336_(AMTagRegistry.ORCA_BREAKABLES) && fluidState.isEmpty() && block != Blocks.AIR) {
                                this.m_20256_(this.m_20184_().multiply(0.6F, 1.0, 0.6F));
                                flag = true;
                                if (state.m_204336_(BlockTags.ICE)) {
                                    this.m_9236_().m_46961_(pos, false);
                                    this.m_9236_().setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
                                } else {
                                    this.m_9236_().m_46961_(pos, true);
                                }
                            }
                        }
                    }
                }
            }
            if (flag) {
                this.blockBreakCounter = 20;
            }
        }
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.jumpCooldown > 0) {
            this.jumpCooldown--;
            float f2 = (float) (-((double) ((float) this.m_20184_().y) * 180.0F / (float) Math.PI));
            this.m_146926_(f2);
        }
        if (this.m_21525_()) {
            this.m_20301_(this.getMaxAirSupply());
        } else {
            if (this.m_20071_()) {
                this.setMoistness(2400);
            } else {
                this.setMoistness(this.getMoistness() - 1);
                if (this.getMoistness() <= 0) {
                    this.m_6469_(this.m_269291_().dryOut(), 1.0F);
                }
                if (this.m_20096_()) {
                    this.m_20256_(this.m_20184_().add((double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.2F), 0.5, (double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.2F)));
                    this.m_146922_(this.f_19796_.nextFloat() * 360.0F);
                    this.m_6853_(false);
                    this.f_19812_ = true;
                }
            }
            if (this.m_9236_().isClientSide && this.m_20069_() && this.m_20184_().lengthSqr() > 0.03) {
                Vec3 vector3d = this.m_20252_(0.0F);
                float yRotRad = this.m_146908_() * (float) (Math.PI / 180.0);
                float f = Mth.cos(yRotRad) * 0.9F;
                float f1 = Mth.sin(yRotRad) * 0.9F;
                float f2 = 1.2F - this.f_19796_.nextFloat() * 0.7F;
                for (int i = 0; i < 2; i++) {
                    this.m_9236_().addParticle(ParticleTypes.DOLPHIN, this.m_20185_() - vector3d.x * (double) f2 + (double) f, this.m_20186_() - vector3d.y, this.m_20189_() - vector3d.z * (double) f2 + (double) f1, 0.0, 0.0, 0.0);
                    this.m_9236_().addParticle(ParticleTypes.DOLPHIN, this.m_20185_() - vector3d.x * (double) f2 - (double) f, this.m_20186_() - vector3d.y, this.m_20189_() - vector3d.z * (double) f2 - (double) f1, 0.0, 0.0, 0.0);
                }
            }
        }
        LivingEntity attackTarget = this.m_5448_();
        if (attackTarget != null && this.m_20270_(attackTarget) < attackTarget.m_20205_() + this.m_20205_() + 2.0F) {
            if (this.getAnimation() == ANIMATION_BITE && this.getAnimationTick() == 4) {
                float damage = (float) ((int) this.m_21133_(Attributes.ATTACK_DAMAGE));
                if (attackTarget instanceof Drowned || attackTarget instanceof Guardian) {
                    damage *= 2.0F;
                }
                boolean flag = attackTarget.hurt(this.m_269291_().mobAttack(this), damage);
                if (flag) {
                    this.m_19970_(this, attackTarget);
                    this.m_5496_(SoundEvents.DOLPHIN_ATTACK, 1.0F, 1.0F);
                }
            }
            if (this.getAnimation() == ANIMATION_TAILSWING && this.getAnimationTick() == 6) {
                float damagex = (float) ((int) this.m_21133_(Attributes.ATTACK_DAMAGE));
                if (attackTarget instanceof Drowned || attackTarget instanceof Guardian) {
                    damagex *= 2.0F;
                }
                boolean flag = attackTarget.hurt(this.m_269291_().mobAttack(this), damagex);
                if (flag) {
                    this.m_19970_(this, attackTarget);
                    this.m_5496_(SoundEvents.DOLPHIN_ATTACK, 1.0F, 1.0F);
                }
                float yRotRad = this.m_146908_() * (float) (Math.PI / 180.0);
                attackTarget.knockback(1.0, (double) Mth.sin(yRotRad), (double) (-Mth.cos(yRotRad)));
                float knockbackResist = (float) Mth.clamp(1.0 - this.m_21133_(Attributes.KNOCKBACK_RESISTANCE), 0.0, 1.0);
                this.m_5448_().m_20256_(this.m_5448_().m_20184_().add(0.0, (double) (knockbackResist * 0.4F), 0.0));
            }
        }
        if (attackTarget != null && attackTarget instanceof Player && attackTarget.hasEffect(AMEffectRegistry.ORCAS_MIGHT.get())) {
            attackTarget.removeEffect(AMEffectRegistry.ORCAS_MIGHT.get());
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
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
    public boolean doHurtTarget(Entity entityIn) {
        if (this.m_20072_() && this.f_19796_.nextBoolean()) {
            this.setAnimation(ANIMATION_TAILSWING);
        } else {
            this.setAnimation(ANIMATION_BITE);
        }
        return true;
    }

    @Override
    public int getMaxAirSupply() {
        return 4800;
    }

    @Override
    protected int increaseAirSupply(int currentAir) {
        return this.getMaxAirSupply();
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return 1.0F;
    }

    @Override
    public int getMaxHeadXRot() {
        return 1;
    }

    @Override
    public int getMaxHeadYRot() {
        return 1;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == Items.SALMON;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob p_241840_2_) {
        return AMEntityRegistry.ORCA.get().create(serverWorld);
    }

    public boolean shouldUseJumpAttack(LivingEntity attackTarget) {
        if (!attackTarget.m_20069_()) {
            return this.jumpCooldown == 0;
        } else {
            BlockPos up = attackTarget.m_20183_().above();
            return this.m_9236_().getFluidState(up.above()).isEmpty() && this.m_9236_().getFluidState(up.above(2)).isEmpty() && this.jumpCooldown == 0;
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.m_20301_(this.getMaxAirSupply());
        this.setVariant(this.determineVariant(this.m_20183_()));
        this.m_146926_(0.0F);
        this.setMoistness(2400);
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return false;
    }

    @Override
    public void baseTick() {
        int i = this.m_20146_();
        super.m_6075_();
        this.updateAir(i);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public MobType getMobType() {
        return MobType.WATER;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        return worldIn.m_45784_(this);
    }

    protected void updateAir(int p_209207_1_) {
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Moistness", this.getMoistness());
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setMoistness(compound.getInt("Moistness"));
        this.setVariant(compound.getInt("Variant"));
    }

    public void onJumpHit(LivingEntity entityIn) {
        boolean flag = entityIn.hurt(this.m_269291_().mobAttack(this), (float) ((int) this.m_21133_(Attributes.ATTACK_DAMAGE)));
        if (flag) {
            this.m_19970_(this, entityIn);
            this.m_5496_(SoundEvents.DOLPHIN_ATTACK, 1.0F, 1.0F);
        }
    }

    public static boolean canOrcaSpawn(EntityType<EntityOrca> p_223364_0_, LevelAccessor p_223364_1_, MobSpawnType reason, BlockPos p_223364_3_, RandomSource p_223364_4_) {
        return p_223364_3_.m_123342_() > 45 && p_223364_3_.m_123342_() < p_223364_1_.m_5736_() ? p_223364_1_.m_6425_(p_223364_3_).is(FluidTags.WATER) : false;
    }

    static class MoveHelperController extends MoveControl {

        private final EntityOrca dolphin;

        public MoveHelperController(EntityOrca dolphinIn) {
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
                    float f1 = (float) (this.f_24978_ * this.dolphin.m_21133_(Attributes.MOVEMENT_SPEED));
                    if (this.dolphin.m_20069_()) {
                        this.dolphin.m_7910_(f1 * 0.02F);
                        float f2 = -((float) (Mth.atan2(d1, (double) Mth.sqrt((float) (d0 * d0 + d2 * d2))) * 180.0F / (float) Math.PI));
                        f2 = Mth.clamp(Mth.wrapDegrees(f2), -85.0F, 85.0F);
                        this.dolphin.m_146926_(this.m_24991_(this.dolphin.m_146909_(), f2, 5.0F));
                        float xRotRad = this.dolphin.m_146909_() * (float) (Math.PI / 180.0);
                        float f3 = Mth.cos(xRotRad);
                        float f4 = Mth.sin(xRotRad);
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

    static class SwimWithPlayerGoal extends Goal {

        private final EntityOrca dolphin;

        private final double speed;

        private Player targetPlayer;

        SwimWithPlayerGoal(EntityOrca dolphinIn, double speedIn) {
            this.dolphin = dolphinIn;
            this.speed = speedIn;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            this.targetPlayer = this.dolphin.m_9236_().m_45946_(EntityOrca.PLAYER_PREDICATE, this.dolphin);
            return this.targetPlayer == null ? false : this.targetPlayer.isSwimming() && this.dolphin.m_5448_() != this.targetPlayer;
        }

        @Override
        public boolean canContinueToUse() {
            return this.targetPlayer != null && this.dolphin.m_5448_() != this.targetPlayer && this.targetPlayer.isSwimming() && this.dolphin.m_20280_(this.targetPlayer) < 256.0;
        }

        @Override
        public void start() {
        }

        @Override
        public void stop() {
            this.targetPlayer = null;
            this.dolphin.m_21573_().stop();
        }

        @Override
        public void tick() {
            this.dolphin.m_21563_().setLookAt(this.targetPlayer, (float) (this.dolphin.getMaxHeadYRot() + 20), (float) this.dolphin.getMaxHeadXRot());
            if (this.dolphin.m_20280_(this.targetPlayer) < 10.0) {
                this.dolphin.m_21573_().stop();
            } else {
                this.dolphin.m_21573_().moveTo(this.targetPlayer, this.speed);
            }
            if (this.targetPlayer.isSwimming() && this.targetPlayer.m_9236_().random.nextInt(6) == 0) {
                this.targetPlayer.m_7292_(new MobEffectInstance(AMEffectRegistry.ORCAS_MIGHT.get(), 1000));
            }
        }
    }
}