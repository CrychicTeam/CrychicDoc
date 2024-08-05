package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.DirectPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.entity.ai.FlightMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.GroundPathNavigatorWide;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
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
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityDropBear extends Monster implements IAnimatedEntity {

    public static final Animation ANIMATION_BITE = Animation.create(9);

    public static final Animation ANIMATION_SWIPE_R = Animation.create(15);

    public static final Animation ANIMATION_SWIPE_L = Animation.create(15);

    public static final Animation ANIMATION_JUMPUP = Animation.create(20);

    private static final EntityDataAccessor<Boolean> UPSIDE_DOWN = SynchedEntityData.defineId(EntityDropBear.class, EntityDataSerializers.BOOLEAN);

    public float prevUpsideDownProgress;

    public float upsideDownProgress;

    public boolean fallRotation = this.f_19796_.nextBoolean();

    private int animationTick;

    private boolean jumpingUp = false;

    private Animation currentAnimation;

    private int upwardsFallingTicks = 0;

    private boolean isUpsideDownNavigator;

    private boolean prevOnGround = false;

    protected EntityDropBear(EntityType type, Level world) {
        super(type, world);
        this.switchNavigator(true);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 22.0).add(Attributes.FOLLOW_RANGE, 20.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.7F).add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    public static BlockPos getLowestPos(LevelAccessor world, BlockPos pos) {
        while (!world.m_8055_(pos).m_60783_(world, pos, Direction.DOWN) && pos.m_123342_() < 320) {
            pos = pos.above();
        }
        return pos;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.dropbearSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.DROPBEAR_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.DROPBEAR_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.DROPBEAR_HURT.get();
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(this.f_19796_.nextBoolean() ? ANIMATION_BITE : (this.f_19796_.nextBoolean() ? ANIMATION_SWIPE_L : ANIMATION_SWIPE_R));
        }
        return true;
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new EntityDropBear.AIDropMelee());
        this.f_21345_.addGoal(2, new EntityDropBear.AIUpsideDownWander());
        this.f_21345_.addGoal(6, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(7, new LookAtPlayerGoal(this, LivingEntity.class, 30.0F));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this, EntityDropBear.class));
        this.f_21346_.addGoal(2, new EntityAINearestTarget3D(this, Player.class, true) {

            @Override
            protected AABB getTargetSearchArea(double targetDistance) {
                AABB bb = this.f_26135_.m_20191_().inflate(targetDistance, targetDistance, targetDistance);
                return new AABB(bb.minX, 0.0, bb.minZ, bb.maxX, 256.0, bb.maxZ);
            }
        });
        this.f_21346_.addGoal(2, new EntityAINearestTarget3D(this, AbstractVillager.class, true) {

            @Override
            protected AABB getTargetSearchArea(double targetDistance) {
                AABB bb = this.f_26135_.m_20191_().inflate(targetDistance, targetDistance, targetDistance);
                return new AABB(bb.minX, 0.0, bb.minZ, bb.maxX, 256.0, bb.maxZ);
            }
        });
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return super.m_6673_(source) || source.is(DamageTypeTags.IS_FALL) || source.is(DamageTypes.IN_WALL);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
        super.m_7840_(y, onGroundIn, state, pos);
    }

    @Override
    protected void playBlockFallSound() {
        this.onLand();
        super.m_21229_();
    }

    private void switchNavigator(boolean rightsideUp) {
        if (rightsideUp) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new GroundPathNavigatorWide(this, this.m_9236_());
            this.isUpsideDownNavigator = false;
        } else {
            this.f_21342_ = new FlightMoveController(this, 1.1F, false);
            this.f_21344_ = new DirectPathNavigator(this, this.m_9236_());
            this.isUpsideDownNavigator = true;
        }
    }

    @Override
    public void tick() {
        super.m_8119_();
        AnimationHandler.INSTANCE.updateAnimations(this);
        this.prevUpsideDownProgress = this.upsideDownProgress;
        if (this.isUpsideDown() && this.upsideDownProgress < 5.0F) {
            this.upsideDownProgress++;
        }
        if (!this.isUpsideDown() && this.upsideDownProgress > 0.0F) {
            this.upsideDownProgress--;
        }
        if (!this.m_9236_().isClientSide) {
            BlockPos abovePos = this.getPositionAbove();
            BlockState aboveState = this.m_9236_().getBlockState(abovePos);
            BlockState belowState = this.m_9236_().getBlockState(this.m_20099_());
            BlockPos worldHeight = this.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING, this.m_20183_());
            boolean validAboveState = aboveState.m_60783_(this.m_9236_(), abovePos, Direction.DOWN);
            boolean validBelowState = belowState.m_60783_(this.m_9236_(), this.m_20099_(), Direction.UP);
            LivingEntity attackTarget = this.m_5448_();
            if (attackTarget != null && this.m_20270_(attackTarget) < attackTarget.m_20205_() + this.m_20205_() + 1.0F && this.m_142582_(attackTarget)) {
                if (this.getAnimationTick() == 6) {
                    if (this.getAnimation() == ANIMATION_BITE) {
                        float yRotRad = this.m_146908_() * (float) (Math.PI / 180.0);
                        attackTarget.knockback(0.5, (double) Mth.sin(yRotRad), (double) (-Mth.cos(yRotRad)));
                        this.m_5448_().hurt(this.m_269291_().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getBaseValue());
                    }
                } else if (this.getAnimationTick() == 9) {
                    if (this.getAnimation() == ANIMATION_SWIPE_L) {
                        float rot = this.m_146908_() + 90.0F;
                        float rotRad = rot * (float) (Math.PI / 180.0);
                        attackTarget.knockback(0.5, (double) Mth.sin(rotRad), (double) (-Mth.cos(rotRad)));
                        this.m_5448_().hurt(this.m_269291_().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getBaseValue());
                    } else if (this.getAnimation() == ANIMATION_SWIPE_R) {
                        float rot = this.m_146908_() - 90.0F;
                        float rotRad = rot * (float) (Math.PI / 180.0);
                        attackTarget.knockback(0.5, (double) Mth.sin(rotRad), (double) (-Mth.cos(rotRad)));
                        this.m_5448_().hurt(this.m_269291_().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getBaseValue());
                    }
                }
            }
            if ((attackTarget == null || attackTarget != null && !attackTarget.isAlive()) && this.f_19796_.nextInt(300) == 0 && this.m_20096_() && !this.isUpsideDown() && this.m_20186_() + 2.0 < (double) worldHeight.m_123342_() && this.getAnimation() == NO_ANIMATION) {
                this.setAnimation(ANIMATION_JUMPUP);
            }
            if (this.jumpingUp && this.m_20186_() > (double) worldHeight.m_123342_()) {
                this.jumpingUp = false;
            }
            if (this.m_20096_() && this.getAnimation() == ANIMATION_JUMPUP && this.getAnimationTick() > 10 || this.jumpingUp && this.getAnimation() == NO_ANIMATION) {
                this.m_20256_(this.m_20184_().add(0.0, 2.0, 0.0));
                this.jumpingUp = true;
            }
            if (this.isUpsideDown()) {
                this.jumpingUp = false;
                this.m_20242_(!this.m_20096_());
                float f = 0.91F;
                this.m_20256_(this.m_20184_().multiply(0.91F, 1.0, 0.91F));
                if (!this.f_19863_) {
                    if (!this.m_20096_() && !validBelowState && this.upwardsFallingTicks <= 5) {
                        if (!validAboveState) {
                            this.upwardsFallingTicks++;
                        }
                        this.m_20256_(this.m_20184_().add(0.0, 0.2F, 0.0));
                    } else {
                        this.setUpsideDown(false);
                        this.upwardsFallingTicks = 0;
                    }
                } else {
                    this.upwardsFallingTicks = 0;
                }
                if (this.f_19862_) {
                    this.upwardsFallingTicks = 0;
                    this.m_20256_(this.m_20184_().add(0.0, -0.3F, 0.0));
                }
                if (this.m_5830_() && this.m_9236_().m_46859_(this.m_20099_())) {
                    this.m_6034_(this.m_20185_(), this.m_20186_() - 1.0, this.m_20189_());
                }
            } else {
                this.m_20242_(false);
                if (validAboveState) {
                    this.setUpsideDown(true);
                }
            }
            if (this.isUpsideDown()) {
                if (!this.isUpsideDownNavigator) {
                    this.switchNavigator(false);
                }
            } else if (this.isUpsideDownNavigator) {
                this.switchNavigator(true);
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(UPSIDE_DOWN, false);
    }

    public boolean isUpsideDown() {
        return this.f_19804_.get(UPSIDE_DOWN);
    }

    public void setUpsideDown(boolean upsideDown) {
        this.f_19804_.set(UPSIDE_DOWN, upsideDown);
    }

    protected BlockPos getPositionAbove() {
        return new BlockPos((int) this.m_20182_().x, (int) (this.m_20191_().maxY + 0.5000001), (int) this.m_20182_().z);
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
        return new Animation[] { ANIMATION_BITE, ANIMATION_SWIPE_L, ANIMATION_SWIPE_R, ANIMATION_JUMPUP };
    }

    private boolean hasLineOfSightBlock(BlockPos destinationBlock) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        Vec3 blockVec = Vec3.atCenterOf(destinationBlock);
        BlockHitResult result = this.m_9236_().m_45547_(new ClipContext(Vector3d, blockVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        return result.getBlockPos().equals(destinationBlock);
    }

    private void doInitialPosing(LevelAccessor world) {
        BlockPos upperPos = this.getPositionAbove().above();
        BlockPos highest = getLowestPos(world, upperPos);
        this.m_6034_((double) ((float) highest.m_123341_() + 0.5F), (double) highest.m_123342_(), (double) ((float) highest.m_123343_() + 0.5F));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (reason == MobSpawnType.NATURAL) {
            this.doInitialPosing(worldIn);
        }
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    private void onLand() {
        if (!this.m_9236_().isClientSide) {
            this.m_9236_().broadcastEntityEvent(this, (byte) 39);
            for (Entity entity : this.m_9236_().m_45976_(LivingEntity.class, this.m_20191_().inflate(2.5))) {
                if (!this.m_7307_(entity) && !(entity instanceof EntityDropBear) && entity != this) {
                    entity.hurt(this.m_269291_().mobAttack(this), 2.0F + this.f_19796_.nextFloat() * 5.0F);
                    this.launch(entity, true);
                }
            }
        }
    }

    private void launch(Entity e, boolean huge) {
        if (e.onGround()) {
            double d0 = e.getX() - this.m_20185_();
            double d1 = e.getZ() - this.m_20189_();
            double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
            float f = 0.5F;
            e.push(d0 / d2 * 0.5, huge ? 0.5 : 0.2F, d1 / d2 * 0.5);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 39) {
            this.spawnGroundEffects();
        } else {
            super.m_7822_(id);
        }
    }

    public void spawnGroundEffects() {
        float radius = 2.3F;
        if (this.m_9236_().isClientSide) {
            for (int i1 = 0; i1 < 20 + this.f_19796_.nextInt(12); i1++) {
                double motionX = this.m_217043_().nextGaussian() * 0.07;
                double motionY = this.m_217043_().nextGaussian() * 0.07;
                double motionZ = this.m_217043_().nextGaussian() * 0.07;
                float angle = (float) (Math.PI / 180.0) * this.f_20883_ + (float) i1;
                double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
                double extraY = 0.8F;
                double extraZ = (double) (radius * Mth.cos(angle));
                BlockPos ground = this.getGroundPosition(new BlockPos(Mth.floor(this.m_20185_() + extraX), (int) this.m_20186_(), Mth.floor(this.m_20189_() + extraZ)));
                BlockState state = this.m_9236_().getBlockState(ground);
                if (!state.m_60795_()) {
                    this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), true, this.m_20185_() + extraX, (double) ground.m_123342_() + extraY, this.m_20189_() + extraZ, motionX, motionY, motionZ);
                }
            }
        }
    }

    private BlockPos getGroundPosition(BlockPos in) {
        BlockPos position = new BlockPos(in.m_123341_(), (int) this.m_20186_(), in.m_123343_());
        while (position.m_123342_() > 2 && this.m_9236_().m_46859_(position) && this.m_9236_().getFluidState(position).isEmpty()) {
            position = position.below();
        }
        return position;
    }

    private class AIDropMelee extends Goal {

        private boolean prevOnGround = false;

        public AIDropMelee() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return EntityDropBear.this.m_5448_() != null;
        }

        @Override
        public void tick() {
            LivingEntity target = EntityDropBear.this.m_5448_();
            if (target != null) {
                double dist = (double) EntityDropBear.this.m_20270_(target);
                if (EntityDropBear.this.isUpsideDown()) {
                    double d0 = EntityDropBear.this.m_20185_() - target.m_20185_();
                    double d2 = EntityDropBear.this.m_20189_() - target.m_20189_();
                    double xzDistSqr = d0 * d0 + d2 * d2;
                    BlockPos ceilingPos = new BlockPos((int) target.m_20185_(), (int) (EntityDropBear.this.m_20186_() - 3.0 - (double) EntityDropBear.this.f_19796_.nextInt(3)), (int) target.m_20189_());
                    BlockPos lowestPos = EntityDropBear.getLowestPos(EntityDropBear.this.m_9236_(), ceilingPos);
                    EntityDropBear.this.m_21566_().setWantedPosition((double) ((float) lowestPos.m_123341_() + 0.5F), (double) ceilingPos.m_123342_(), (double) ((float) lowestPos.m_123343_() + 0.5F), 1.1);
                    if (xzDistSqr < 2.5) {
                        EntityDropBear.this.setUpsideDown(false);
                    }
                } else if (EntityDropBear.this.m_20096_()) {
                    EntityDropBear.this.m_21573_().moveTo(target, 1.2);
                }
                if (dist < 3.0) {
                    EntityDropBear.this.doHurtTarget(target);
                }
            }
        }
    }

    class AIUpsideDownWander extends RandomStrollGoal {

        public AIUpsideDownWander() {
            super(EntityDropBear.this, 1.0, 50);
        }

        @Nullable
        @Override
        protected Vec3 getPosition() {
            if (EntityDropBear.this.isUpsideDown()) {
                for (int i = 0; i < 15; i++) {
                    Random rand = new Random();
                    BlockPos randPos = EntityDropBear.this.m_20183_().offset(rand.nextInt(16) - 8, -2, rand.nextInt(16) - 8);
                    BlockPos lowestPos = EntityDropBear.getLowestPos(EntityDropBear.this.m_9236_(), randPos);
                    if (EntityDropBear.this.m_9236_().getBlockState(lowestPos).m_60783_(EntityDropBear.this.m_9236_(), lowestPos, Direction.DOWN)) {
                        return Vec3.atCenterOf(lowestPos);
                    }
                }
                return null;
            } else {
                return super.getPosition();
            }
        }

        @Override
        public boolean canUse() {
            return super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            if (EntityDropBear.this.isUpsideDown()) {
                double d0 = EntityDropBear.this.m_20185_() - this.f_25726_;
                double d2 = EntityDropBear.this.m_20189_() - this.f_25728_;
                double d4 = d0 * d0 + d2 * d2;
                return d4 > 4.0;
            } else {
                return super.canContinueToUse();
            }
        }

        @Override
        public void stop() {
            super.stop();
            this.f_25726_ = 0.0;
            this.f_25727_ = 0.0;
            this.f_25728_ = 0.0;
        }

        @Override
        public void start() {
            if (EntityDropBear.this.isUpsideDown()) {
                this.f_25725_.m_21566_().setWantedPosition(this.f_25726_, this.f_25727_, this.f_25728_, this.f_25729_ * 0.7F);
            } else {
                this.f_25725_.m_21573_().moveTo(this.f_25726_, this.f_25727_, this.f_25728_, this.f_25729_);
            }
        }
    }
}