package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.DirectPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.entity.ai.FlightMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.GroundPathNavigatorWide;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class EntityWarpedMosco extends Monster implements IAnimatedEntity {

    public static final Animation ANIMATION_PUNCH_R = Animation.create(25);

    public static final Animation ANIMATION_PUNCH_L = Animation.create(25);

    public static final Animation ANIMATION_SLAM = Animation.create(35);

    public static final Animation ANIMATION_SUCK = Animation.create(60);

    public static final Animation ANIMATION_SPIT = Animation.create(60);

    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(EntityWarpedMosco.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> HAND_SIDE = SynchedEntityData.defineId(EntityWarpedMosco.class, EntityDataSerializers.BOOLEAN);

    public float flyLeftProgress;

    public float prevLeftFlyProgress;

    public float flyRightProgress;

    public float prevFlyRightProgress;

    private int animationTick;

    private Animation currentAnimation;

    private boolean isLandNavigator;

    private int timeFlying;

    private int loopSoundTick = 0;

    protected EntityWarpedMosco(EntityType entityType, Level world) {
        super(entityType, world);
        this.f_21364_ = 30;
        this.switchNavigator(false);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 100.0).add(Attributes.FOLLOW_RANGE, 128.0).add(Attributes.ATTACK_DAMAGE, 10.0).add(Attributes.ARMOR, 10.0).add(Attributes.KNOCKBACK_RESISTANCE, 1.0).add(Attributes.ARMOR_TOUGHNESS, 2.0).add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    private static Animation getRandomAttack(RandomSource rand) {
        return switch(rand.nextInt(4)) {
            case 0 ->
                ANIMATION_PUNCH_L;
            case 1 ->
                ANIMATION_PUNCH_R;
            case 2 ->
                ANIMATION_SLAM;
            case 3 ->
                ANIMATION_SUCK;
            default ->
                ANIMATION_SUCK;
        };
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.WARPED_MOSCO_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.WARPED_MOSCO_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.WARPED_MOSCO_HURT.get();
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(0, new EntityWarpedMosco.AttackGoal());
        this.f_21345_.addGoal(4, new EntityWarpedMosco.AIWalkIdle());
        this.f_21345_.addGoal(4, new LookAtPlayerGoal(this, Player.class, 32.0F));
        this.f_21345_.addGoal(5, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this, EntityCrimsonMosquito.class, EntityWarpedMosco.class));
        this.f_21346_.addGoal(2, new EntityAINearestTarget3D(this, Player.class, true));
        this.f_21346_.addGoal(2, new EntityAINearestTarget3D(this, LivingEntity.class, 50, false, true, AMEntityRegistry.buildPredicateFromTag(AMTagRegistry.CRIMSON_MOSQUITO_TARGETS)));
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new GroundPathNavigatorWide(this, this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new FlightMoveController(this, 0.7F, false);
            this.f_21344_ = new DirectPathNavigator(this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(FLYING, false);
        this.f_19804_.define(HAND_SIDE, true);
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public boolean isFlying() {
        return this.f_19804_.get(FLYING);
    }

    public void setFlying(boolean flying) {
        this.setDashRight(flying != this.isFlying() ? this.f_19796_.nextBoolean() : this.isDashRight());
        this.f_19804_.set(FLYING, flying);
    }

    public boolean isDashRight() {
        return this.f_19804_.get(HAND_SIDE);
    }

    public void setDashRight(boolean right) {
        this.f_19804_.set(HAND_SIDE, right);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevFlyRightProgress = this.flyRightProgress;
        this.prevLeftFlyProgress = this.flyLeftProgress;
        boolean dashRight = this.isDashRight();
        boolean flying = this.isFlying();
        if (flying && dashRight && this.flyRightProgress < 5.0F) {
            this.flyRightProgress++;
        }
        if ((!flying || !dashRight) && this.flyRightProgress > 0.0F) {
            this.flyRightProgress--;
        }
        if (flying && !dashRight && this.flyLeftProgress < 5.0F) {
            this.flyLeftProgress++;
        }
        if ((!flying || dashRight) && this.flyLeftProgress > 0.0F) {
            this.flyLeftProgress--;
        }
        if (!this.m_9236_().isClientSide) {
            if (flying) {
                if (this.isLandNavigator) {
                    this.switchNavigator(false);
                }
            } else if (!this.isLandNavigator) {
                this.switchNavigator(true);
            }
        }
        if (flying) {
            if (this.loopSoundTick == 0) {
                this.m_5496_(AMSoundRegistry.MOSQUITO_LOOP.get(), this.m_6121_(), this.m_6100_() * 0.3F);
            }
            this.loopSoundTick++;
            if (this.loopSoundTick > 100) {
                this.loopSoundTick = 0;
            }
            this.timeFlying++;
            this.m_20242_(true);
            if (this.m_20159_() || this.m_20160_()) {
                this.setFlying(false);
            }
        } else {
            this.timeFlying = 0;
            this.m_20242_(false);
        }
        if (this.f_19862_ && ForgeEventFactory.getMobGriefingEvent(this.m_9236_(), this)) {
            boolean flag = false;
            AABB axisalignedbb = this.m_20191_().inflate(0.2);
            for (BlockPos blockpos : BlockPos.betweenClosed(Mth.floor(axisalignedbb.minX), Mth.floor(axisalignedbb.minY), Mth.floor(axisalignedbb.minZ), Mth.floor(axisalignedbb.maxX), Mth.floor(axisalignedbb.maxY), Mth.floor(axisalignedbb.maxZ))) {
                BlockState blockstate = this.m_9236_().getBlockState(blockpos);
                if (blockstate.m_204336_(AMTagRegistry.WARPED_MOSCO_BREAKABLES)) {
                    flag = this.m_9236_().m_46953_(blockpos, true, this) || flag;
                }
            }
            if (!flag && this.m_20096_()) {
                this.m_6135_();
            }
        }
        LivingEntity target = this.m_5448_();
        if (target != null && this.m_6084_()) {
            if (this.getAnimation() == ANIMATION_SUCK && this.getAnimationTick() == 3 && this.m_20270_(target) < 4.7F) {
                target.m_7998_(this, true);
            }
            if (this.getAnimation() == ANIMATION_SLAM && this.getAnimationTick() == 19) {
                for (Entity entity : this.m_9236_().m_45976_(LivingEntity.class, this.m_20191_().inflate(5.0))) {
                    if (!this.m_7307_(entity) && !(entity instanceof EntityWarpedMosco) && entity != this) {
                        entity.hurt(this.m_269291_().mobAttack(this), 10.0F + this.f_19796_.nextFloat() * 8.0F);
                        this.launch(entity, true);
                    }
                }
            }
            if ((this.getAnimation() == ANIMATION_PUNCH_R || this.getAnimation() == ANIMATION_PUNCH_L) && this.getAnimationTick() == 13 && this.m_20270_(target) < 4.7F) {
                target.hurt(this.m_269291_().mobAttack(this), (float) this.m_21133_(Attributes.ATTACK_DAMAGE));
                this.knockbackRidiculous(target, 0.9F);
            }
        }
        if (this.getAnimation() == ANIMATION_SLAM && this.getAnimationTick() == 19) {
            this.spawnGroundEffects();
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    public void spawnGroundEffects() {
        float radius = 2.3F;
        double extraY = 0.8F;
        for (int i = 0; i < 4; i++) {
            for (int i1 = 0; i1 < 20 + this.f_19796_.nextInt(12); i1++) {
                double motionX = this.m_217043_().nextGaussian() * 0.07;
                double motionY = this.m_217043_().nextGaussian() * 0.07;
                double motionZ = this.m_217043_().nextGaussian() * 0.07;
                float angle = (float) (Math.PI / 180.0) * this.f_20883_ + (float) i1;
                double extraX = (double) (2.3F * Mth.sin((float) Math.PI + angle));
                double extraZ = (double) (2.3F * Mth.cos(angle));
                BlockPos ground = this.getMoscoGround(new BlockPos(Mth.floor(this.m_20185_() + extraX), Mth.floor(this.m_20186_() + 0.8F) - 1, Mth.floor(this.m_20189_() + extraZ)));
                BlockState state = this.m_9236_().getBlockState(ground);
                if (state.m_280296_() && this.m_9236_().isClientSide) {
                    this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), true, this.m_20185_() + extraX, (double) ground.m_123342_() + 0.8F, this.m_20189_() + extraZ, motionX, motionY, motionZ);
                }
            }
        }
    }

    private void launch(Entity e, boolean huge) {
        if (e.onGround()) {
            double d0 = e.getX() - this.m_20185_();
            double d1 = e.getZ() - this.m_20189_();
            double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
            float f = huge ? 2.0F : 0.5F;
            e.push(d0 / d2 * (double) f, huge ? 0.5 : 0.2F, d1 / d2 * (double) f);
        }
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
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int i) {
        this.animationTick = i;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_PUNCH_L, ANIMATION_PUNCH_R, ANIMATION_SLAM, ANIMATION_SUCK, ANIMATION_SPIT };
    }

    private BlockPos getMoscoGround(BlockPos in) {
        BlockPos position = new BlockPos(in.m_123341_(), (int) this.m_20186_(), in.m_123343_());
        while (position.m_123342_() > -62 && !this.m_9236_().getBlockState(position).m_280296_() && this.m_9236_().getFluidState(position).isEmpty()) {
            position = position.below();
        }
        return position;
    }

    public Vec3 getBlockGrounding(Vec3 fleePos) {
        float radius = -9.45F - (float) this.m_217043_().nextInt(24);
        float neg = this.m_217043_().nextBoolean() ? 1.0F : -1.0F;
        float renderYawOffset = this.f_20883_;
        float angle = (float) (Math.PI / 180.0) * renderYawOffset + 3.15F + this.m_217043_().nextFloat() * neg;
        double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = AMBlockPos.fromCoords(fleePos.x() + extraX, this.m_20186_(), fleePos.z() + extraZ);
        BlockPos ground = this.getMoscoGround(radialPos);
        if (ground.m_123342_() == -62) {
            return this.m_20182_();
        } else {
            ground = this.m_20183_();
            while (ground.m_123342_() > -62 && !this.m_9236_().getBlockState(ground).m_280296_()) {
                ground = ground.below();
            }
            return !this.isTargetBlocked(Vec3.atCenterOf(ground.above())) ? Vec3.atCenterOf(ground) : null;
        }
    }

    public Vec3 getBlockInViewAway(Vec3 fleePos, float radiusAdd) {
        float radius = -9.45F - (float) this.m_217043_().nextInt(24) - radiusAdd;
        float neg = this.m_217043_().nextBoolean() ? 1.0F : -1.0F;
        float renderYawOffset = this.f_20883_;
        float angle = (float) (Math.PI / 180.0) * renderYawOffset + 3.15F + this.m_217043_().nextFloat() * neg;
        double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = new BlockPos((int) (fleePos.x() + extraX), 0, (int) (fleePos.z() + extraZ));
        BlockPos ground = this.getMoscoGround(radialPos);
        int distFromGround = (int) this.m_20186_() - ground.m_123342_();
        int flightHeight = 4 + this.m_217043_().nextInt(10);
        BlockPos newPos = ground.above(distFromGround > 8 ? flightHeight : this.m_217043_().nextInt(6) + 1);
        return !this.isTargetBlocked(Vec3.atCenterOf(newPos)) && this.m_20238_(Vec3.atCenterOf(newPos)) > 1.0 ? Vec3.atCenterOf(newPos) : null;
    }

    public void knockbackRidiculous(LivingEntity target, float power) {
        target.knockback((double) power, this.m_20185_() - target.m_20185_(), this.m_20189_() - target.m_20189_());
        float knockbackResist = (float) Mth.clamp(1.0 - this.m_21133_(Attributes.KNOCKBACK_RESISTANCE), 0.0, 1.0);
        target.m_20256_(target.m_20184_().add(0.0, (double) (knockbackResist * power * 0.45F), 0.0));
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        return this.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.MISS;
    }

    private boolean isOverLiquid() {
        BlockPos position = this.m_20183_();
        while (position.m_123342_() > 2 && this.m_9236_().m_46859_(position)) {
            position = position.below();
        }
        return !this.m_9236_().getFluidState(position).isEmpty();
    }

    @Override
    public void travel(Vec3 travelVector) {
        if ((this.getAnimation() == ANIMATION_SUCK || this.getAnimation() == ANIMATION_SLAM) && this.getAnimationTick() > 8) {
            if (this.m_21573_().getPath() != null) {
                this.m_21573_().stop();
            }
            travelVector = Vec3.ZERO;
            super.m_7023_(travelVector);
        } else {
            super.m_7023_(travelVector);
        }
    }

    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction moveFunc) {
        super.m_19956_(passenger, moveFunc);
        if (this.m_20363_(passenger)) {
            int tick = 5;
            if (this.getAnimation() == ANIMATION_SUCK) {
                tick = this.getAnimationTick();
            } else {
                passenger.stopRiding();
            }
            float radius = 2.0F;
            float angle = (float) (Math.PI / 180.0) * this.f_20883_;
            double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
            double extraZ = (double) (radius * Mth.cos(angle));
            double extraY = tick < 10 ? 0.0 : (double) (0.15F * (float) Mth.clamp(tick - 10, 0, 15));
            passenger.setPos(this.m_20185_() + extraX, this.m_20186_() + extraY + 0.1F, this.m_20189_() + extraZ);
            if ((tick - 10) % 4 == 0) {
                this.m_7292_(new MobEffectInstance(MobEffects.REGENERATION, 100, 1));
                passenger.hurt(this.m_269291_().mobAttack(this), (float) this.m_21133_(Attributes.ATTACK_DAMAGE));
            }
        }
    }

    public boolean canRiderInteract() {
        return true;
    }

    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.warpedMoscoSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    private void spit(LivingEntity target) {
        if (this.getAnimation() == ANIMATION_SPIT) {
            this.m_21391_(target, 100.0F, 100.0F);
            this.f_20883_ = this.f_20885_;
            for (int i = 0; i < 2 + this.f_19796_.nextInt(2); i++) {
                EntityHemolymph llamaspitentity = new EntityHemolymph(this.m_9236_(), this);
                double d0 = target.m_20185_() - this.m_20185_();
                double d1 = target.m_20227_(0.3333333333333333) - llamaspitentity.m_20186_();
                double d2 = target.m_20189_() - this.m_20189_();
                float f = Mth.sqrt((float) (d0 * d0 + d2 * d2)) * 0.2F;
                llamaspitentity.shoot(d0, d1 + (double) f, d2, 1.5F, 5.0F);
                if (!this.m_20067_()) {
                    this.m_146850_(GameEvent.PROJECTILE_SHOOT);
                    this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.LLAMA_SPIT, this.m_5720_(), 1.0F, 1.0F + (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F);
                }
                this.m_9236_().m_7967_(llamaspitentity);
            }
        }
    }

    private boolean shouldRangeAttack(LivingEntity target) {
        return (double) this.m_21223_() < Math.floor((double) (this.m_21233_() * 0.25F)) ? true : this.m_21223_() < this.m_21223_() * 0.5F && this.m_20270_(target) > 10.0F;
    }

    private class AIWalkIdle extends Goal {

        protected final EntityWarpedMosco mosco;

        protected double x;

        protected double y;

        protected double z;

        private boolean flightTarget = false;

        public AIWalkIdle() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
            this.mosco = EntityWarpedMosco.this;
        }

        @Override
        public boolean canUse() {
            if (this.mosco.m_20160_() || this.mosco.m_5448_() != null && this.mosco.m_5448_().isAlive() || this.mosco.m_20159_()) {
                return false;
            } else if (this.mosco.m_217043_().nextInt(30) != 0 && !this.mosco.isFlying()) {
                return false;
            } else {
                if (this.mosco.m_20096_()) {
                    this.flightTarget = EntityWarpedMosco.this.f_19796_.nextInt(8) == 0;
                } else {
                    this.flightTarget = EntityWarpedMosco.this.f_19796_.nextInt(5) > 0 && this.mosco.timeFlying < 200;
                }
                Vec3 lvt_1_1_ = this.getPosition();
                if (lvt_1_1_ == null) {
                    return false;
                } else {
                    this.x = lvt_1_1_.x;
                    this.y = lvt_1_1_.y;
                    this.z = lvt_1_1_.z;
                    return true;
                }
            }
        }

        @Override
        public void tick() {
            if (this.flightTarget) {
                this.mosco.m_21566_().setWantedPosition(this.x, this.y, this.z, 1.0);
            } else {
                this.mosco.m_21573_().moveTo(this.x, this.y, this.z, 1.0);
            }
            if (!this.flightTarget && EntityWarpedMosco.this.isFlying() && this.mosco.m_20096_()) {
                this.mosco.setFlying(false);
            }
            if (EntityWarpedMosco.this.isFlying() && this.mosco.m_20096_() && this.mosco.timeFlying > 10) {
                this.mosco.setFlying(false);
            }
        }

        @Nullable
        protected Vec3 getPosition() {
            Vec3 vector3d = this.mosco.m_20182_();
            if (this.mosco.isOverLiquid()) {
                this.flightTarget = true;
            }
            if (this.flightTarget) {
                return this.mosco.timeFlying >= 50 && !this.mosco.isOverLiquid() ? this.mosco.getBlockGrounding(vector3d) : this.mosco.getBlockInViewAway(vector3d, 0.0F);
            } else {
                return LandRandomPos.getPos(this.mosco, 20, 7);
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.flightTarget ? this.mosco.isFlying() && this.mosco.m_20275_(this.x, this.y, this.z) > 20.0 && !this.mosco.f_19862_ : !this.mosco.m_21573_().isDone() && !this.mosco.m_20160_();
        }

        @Override
        public void start() {
            if (this.flightTarget) {
                this.mosco.setFlying(true);
                this.mosco.m_21566_().setWantedPosition(this.x, this.y, this.z, 1.0);
            } else {
                this.mosco.m_21573_().moveTo(this.x, this.y, this.z, 1.0);
            }
        }

        @Override
        public void stop() {
            this.mosco.m_21573_().stop();
            super.stop();
        }
    }

    private class AttackGoal extends Goal {

        private int upTicks = 0;

        private int dashCooldown = 0;

        private boolean ranged = false;

        private BlockPos farTarget = null;

        public AttackGoal() {
        }

        @Override
        public boolean canUse() {
            return EntityWarpedMosco.this.m_5448_() != null;
        }

        @Override
        public void tick() {
            if (this.dashCooldown > 0) {
                this.dashCooldown--;
            }
            if (EntityWarpedMosco.this.m_5448_() != null) {
                LivingEntity target = EntityWarpedMosco.this.m_5448_();
                this.ranged = EntityWarpedMosco.this.shouldRangeAttack(target);
                if (EntityWarpedMosco.this.isFlying() || this.ranged || EntityWarpedMosco.this.m_20270_(target) > 12.0F && !EntityWarpedMosco.this.isTargetBlocked(target.m_20182_().add(0.0, (double) (target.m_20206_() * 0.6F), 0.0))) {
                    float speedRush = 5.0F;
                    this.upTicks++;
                    EntityWarpedMosco.this.setFlying(true);
                    if (!this.ranged) {
                        if (this.upTicks <= 20 && !(EntityWarpedMosco.this.m_20270_(target) < 6.0F)) {
                            EntityWarpedMosco.this.m_21566_().setWantedPosition(EntityWarpedMosco.this.m_20185_(), EntityWarpedMosco.this.m_20186_() + 3.0, EntityWarpedMosco.this.m_20189_(), 0.5);
                        } else {
                            EntityWarpedMosco.this.m_21566_().setWantedPosition(target.m_20185_(), target.m_20186_() + (double) (target.m_20192_() * 0.6F), target.m_20189_(), (double) speedRush);
                        }
                    } else {
                        if (this.farTarget == null || EntityWarpedMosco.this.m_20238_(Vec3.atCenterOf(this.farTarget)) < 9.0) {
                            this.farTarget = this.getAvoidTarget(target);
                        }
                        if (this.farTarget != null) {
                            EntityWarpedMosco.this.m_21566_().setWantedPosition((double) this.farTarget.m_123341_(), (double) ((float) this.farTarget.m_123342_() + target.m_20192_() * 0.6F), (double) this.farTarget.m_123343_(), 3.0);
                        }
                        EntityWarpedMosco.this.setAnimation(EntityWarpedMosco.ANIMATION_SPIT);
                        if (this.upTicks % 30 == 0) {
                            EntityWarpedMosco.this.m_5634_(1.0F);
                        }
                        int tick = EntityWarpedMosco.this.getAnimationTick();
                        switch(tick) {
                            case 10:
                            case 20:
                            case 30:
                            case 40:
                                EntityWarpedMosco.this.spit(target);
                        }
                    }
                } else {
                    EntityWarpedMosco.this.m_21573_().moveTo(EntityWarpedMosco.this.m_5448_(), 1.25);
                }
                if (EntityWarpedMosco.this.isFlying()) {
                    if (EntityWarpedMosco.this.m_20270_(target) < 4.3F) {
                        if (this.dashCooldown == 0 || target.m_20096_() || target.m_20077_() || target.m_20069_()) {
                            target.hurt(EntityWarpedMosco.this.m_269291_().mobAttack(EntityWarpedMosco.this), 5.0F);
                            EntityWarpedMosco.this.knockbackRidiculous(target, 1.0F);
                            this.dashCooldown = 30;
                        }
                        float groundHeight = (float) EntityWarpedMosco.this.getMoscoGround(EntityWarpedMosco.this.m_20183_()).m_123342_();
                        if (Math.abs(EntityWarpedMosco.this.m_20186_() - (double) groundHeight) < 3.0 && !EntityWarpedMosco.this.isOverLiquid()) {
                            EntityWarpedMosco.this.timeFlying += 300;
                            EntityWarpedMosco.this.setFlying(false);
                        }
                    }
                } else if (EntityWarpedMosco.this.m_20270_(target) < 4.0F && EntityWarpedMosco.this.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                    Animation animation = EntityWarpedMosco.getRandomAttack(EntityWarpedMosco.this.f_19796_);
                    if (animation == EntityWarpedMosco.ANIMATION_SUCK && target.m_20159_()) {
                        animation = EntityWarpedMosco.ANIMATION_SLAM;
                    }
                    EntityWarpedMosco.this.setAnimation(animation);
                }
            }
        }

        public BlockPos getAvoidTarget(LivingEntity target) {
            float radius = (float) (10 + EntityWarpedMosco.this.m_217043_().nextInt(8));
            float angle = (float) (Math.PI / 180.0) * (target.yHeadRot + 90.0F + (float) EntityWarpedMosco.this.m_217043_().nextInt(180));
            double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
            double extraZ = (double) (radius * Mth.cos(angle));
            BlockPos radialPos = AMBlockPos.fromCoords(target.m_20185_() + extraX, target.m_20186_() + 1.0, target.m_20189_() + extraZ);
            return EntityWarpedMosco.this.m_20238_(Vec3.atCenterOf(radialPos)) > 30.0 && !EntityWarpedMosco.this.isTargetBlocked(Vec3.atCenterOf(radialPos)) && EntityWarpedMosco.this.m_20238_(Vec3.atCenterOf(radialPos)) > 6.0 ? radialPos : EntityWarpedMosco.this.m_20183_();
        }

        @Override
        public void stop() {
            this.upTicks = 0;
            this.dashCooldown = 0;
            this.ranged = false;
        }
    }
}