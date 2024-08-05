package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.FlightPathNavigatorNoSpin;
import com.github.alexmodguy.alexscaves.server.entity.ai.VesperAttackGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.VesperFlyAndHangGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.VesperTargetUnderneathEntities;
import com.github.alexmodguy.alexscaves.server.entity.util.UnderzealotSacrifice;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class VesperEntity extends Monster implements IAnimatedEntity, UnderzealotSacrifice {

    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(VesperEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> HANGING = SynchedEntityData.defineId(VesperEntity.class, EntityDataSerializers.BOOLEAN);

    public static final Animation ANIMATION_BITE = Animation.create(15);

    private float flyProgress;

    private float prevFlyProgress;

    private float sleepProgress;

    private float prevSleepProgress;

    private float capturedProgress;

    private float prevCapturedProgress;

    private float groundProgress = 5.0F;

    private float prevGroundProgress = 5.0F;

    private boolean validHangingPos = false;

    private int checkHangingTime;

    private BlockPos prevHangPos;

    public int timeHanging = 0;

    public int timeFlying = 0;

    private float flightPitch = 0.0F;

    private float prevFlightPitch = 0.0F;

    private float flightRoll = 0.0F;

    private float prevFlightRoll = 0.0F;

    private Animation currentAnimation;

    private int animationTick;

    public int groundedFor = 0;

    private boolean isLandNavigator;

    private boolean isBeingSacrificed = false;

    private int sacrificeTime = 0;

    private int lastTargetId = -1;

    public VesperEntity(EntityType entityType, Level level) {
        super(entityType, level);
        this.switchNavigator(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.MAX_HEALTH, 16.0).add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.FOLLOW_RANGE, 52.0);
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new GroundPathNavigation(this, this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new VesperEntity.MoveController();
            this.f_21344_ = new FlightPathNavigatorNoSpin(this, this.m_9236_(), 1.0F);
            this.isLandNavigator = false;
        }
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new VesperAttackGoal(this));
        this.f_21345_.addGoal(2, new RandomStrollGoal(this, 1.0, 15, false) {

            @Override
            public boolean canUse() {
                return !VesperEntity.this.isFlying() && !VesperEntity.this.isHanging() && super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return !VesperEntity.this.isFlying() && !VesperEntity.this.isHanging() && super.canContinueToUse();
            }
        });
        this.f_21345_.addGoal(3, new VesperFlyAndHangGoal(this));
        this.f_21345_.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(4, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new VesperTargetUnderneathEntities(this, 20.0F, Player.class));
        this.f_21346_.addGoal(3, new VesperTargetUnderneathEntities(this, 32.0F, GloomothEntity.class));
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevFlyProgress = this.flyProgress;
        this.prevSleepProgress = this.sleepProgress;
        this.prevGroundProgress = this.groundProgress;
        this.prevCapturedProgress = this.capturedProgress;
        this.prevFlightPitch = this.flightPitch;
        this.prevFlightRoll = this.flightRoll;
        if (this.isFlying() && this.flyProgress < 5.0F) {
            this.flyProgress++;
        }
        if (!this.isFlying() && this.flyProgress > 0.0F) {
            this.flyProgress--;
        }
        if (this.m_20096_() && this.groundProgress < 5.0F) {
            this.groundProgress++;
        }
        if (!this.m_20096_() && this.groundProgress > 0.0F) {
            this.groundProgress--;
        }
        if (this.isHanging() && this.sleepProgress < 5.0F) {
            this.sleepProgress++;
        }
        if (!this.isHanging() && this.sleepProgress > 0.0F) {
            this.sleepProgress--;
        }
        boolean captured = this.m_20159_();
        if (captured && this.capturedProgress < 5.0F) {
            this.capturedProgress++;
        }
        if (!captured && this.capturedProgress > 0.0F) {
            this.capturedProgress--;
        }
        if (!this.m_9236_().isClientSide) {
            if (captured) {
                this.setFlying(false);
                this.setHanging(false);
            }
            if (!this.isHanging()) {
                this.timeHanging = 0;
                this.validHangingPos = false;
                this.prevHangPos = null;
            } else {
                BlockPos above = this.posAbove();
                if (this.checkHangingTime-- < 0 || this.f_19796_.nextFloat() < 0.1F || this.prevHangPos != above) {
                    this.validHangingPos = this.canHangFrom(above, this.m_9236_().getBlockState(above));
                    this.checkHangingTime = 5 + this.f_19796_.nextInt(5);
                    this.prevHangPos = above;
                }
                if (this.validHangingPos) {
                    this.m_20256_(this.m_20184_().multiply(0.1F, 0.3F, 0.1F).add(0.0, 0.08, 0.0));
                } else {
                    this.setHanging(false);
                    this.setFlying(true);
                }
                this.timeHanging++;
            }
            if (this.isFlying()) {
                if (this.timeFlying % 10 == 0) {
                    this.m_216990_(ACSoundRegistry.VESPER_FLAP.get());
                }
                this.timeFlying++;
                this.m_20242_(true);
                if (this.isLandNavigator) {
                    this.switchNavigator(false);
                }
                if (this.groundedFor > 0) {
                    this.setFlying(false);
                }
            } else {
                this.timeFlying = 0;
                this.m_20242_(false);
                if (!this.isLandNavigator) {
                    this.switchNavigator(true);
                }
            }
            LivingEntity target = this.m_5448_();
            if (target != null && target.isAlive()) {
                if (target.m_19879_() != this.lastTargetId) {
                    this.lastTargetId = target.m_19879_();
                    this.m_5496_(ACSoundRegistry.VESPER_SCREAM.get(), 3.0F, 1.0F);
                }
            } else {
                this.lastTargetId = -1;
            }
        }
        if (this.groundedFor > 0) {
            this.groundedFor--;
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
        this.tickRotation((float) this.m_20184_().y * 2.0F * (-180.0F / (float) Math.PI));
        if (this.isBeingSacrificed && this.m_20159_() && !this.m_9236_().isClientSide) {
            this.sacrificeTime--;
            if (this.sacrificeTime < 10) {
                this.m_9236_().broadcastEntityEvent(this, (byte) 61);
            }
            if (this.sacrificeTime < 0) {
                if (this.m_20159_() && this.m_20202_() instanceof UnderzealotEntity underzealot) {
                    underzealot.postSacrifice(this);
                    underzealot.triggerIdleDigging();
                }
                this.m_8127_();
                ForsakenEntity forsakenEntity = (ForsakenEntity) this.m_21406_(ACEntityRegistry.FORSAKEN.get(), true);
                if (forsakenEntity != null) {
                    this.m_5496_(ACSoundRegistry.FORSAKEN_SPAWN.get(), 8.0F, 1.0F);
                    forsakenEntity.setAnimation(ForsakenEntity.ANIMATION_SUMMON);
                    ForgeEventFactory.onLivingConvert(this, forsakenEntity);
                    forsakenEntity.m_8127_();
                }
            }
        }
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 61) {
            for (int i = 0; i < 1 + this.f_19796_.nextInt(4); i++) {
                this.m_9236_().addParticle(ACParticleRegistry.UNDERZEALOT_EXPLOSION.get(), this.m_20208_(1.0), this.m_20187_(), this.m_20262_(1.0), 0.0, 0.0, 0.0);
            }
        } else {
            super.m_7822_(b);
        }
    }

    private void tickRotation(float yMov) {
        this.flightPitch = yMov;
        float threshold = 1.0F;
        boolean flag = false;
        if (this.isFlying() && this.f_19859_ - this.m_146908_() > threshold) {
            this.flightRoll += 10.0F;
            flag = true;
        }
        if (this.isFlying() && this.f_19859_ - this.m_146908_() < -threshold) {
            this.flightRoll -= 10.0F;
            flag = true;
        }
        if (!flag) {
            if (this.flightRoll > 0.0F) {
                this.flightRoll = Math.max(this.flightRoll - 5.0F, 0.0F);
            }
            if (this.flightRoll < 0.0F) {
                this.flightRoll = Math.min(this.flightRoll + 5.0F, 0.0F);
            }
        }
        this.flightRoll = Mth.clamp(this.flightRoll, -60.0F, 60.0F);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(FLYING, false);
        this.f_19804_.define(HANGING, false);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public boolean isFlying() {
        return this.f_19804_.get(FLYING);
    }

    public void setFlying(boolean flying) {
        this.f_19804_.set(FLYING, flying);
    }

    public boolean isHanging() {
        return this.f_19804_.get(HANGING);
    }

    public void setHanging(boolean hanging) {
        this.f_19804_.set(HANGING, hanging);
    }

    public float getFlightPitch(float partialTick) {
        return this.prevFlightPitch + (this.flightPitch - this.prevFlightPitch) * partialTick;
    }

    public float getFlightRoll(float partialTick) {
        return this.prevFlightRoll + (this.flightRoll - this.prevFlightRoll) * partialTick;
    }

    public float getCapturedProgress(float partialTick) {
        return (this.prevCapturedProgress + (this.capturedProgress - this.prevCapturedProgress) * partialTick) * 0.2F;
    }

    public float getSleepProgress(float partialTick) {
        return (this.prevSleepProgress + (this.sleepProgress - this.prevSleepProgress) * partialTick) * 0.2F;
    }

    public float getFlyProgress(float partialTick) {
        return (this.prevFlyProgress + (this.flyProgress - this.prevFlyProgress) * partialTick) * 0.2F;
    }

    public float getGroundProgress(float partialTick) {
        return (this.prevGroundProgress + (this.groundProgress - this.prevGroundProgress) * partialTick) * 0.2F;
    }

    public boolean canHangFrom(BlockPos pos, BlockState state) {
        return state.m_60783_(this.m_9236_(), pos, Direction.DOWN) && this.m_9236_().m_46859_(pos.below()) && this.m_9236_().m_46859_(pos.below(2));
    }

    public BlockPos posAbove() {
        return BlockPos.containing(this.m_20185_(), this.m_20191_().maxY + 0.1F, this.m_20189_());
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
        return new Animation[] { ANIMATION_BITE };
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return this.m_20191_().inflate(3.0, 3.0, 3.0);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return Math.sqrt(distance) < 1024.0;
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, this.m_20186_() - this.f_19855_, this.m_20189_() - this.f_19856_);
        float f2 = Math.min(f1 * 4.0F, 1.0F);
        this.f_267362_.update(f2, 0.4F);
    }

    private void doInitialPosing(LevelAccessor world) {
        BlockPos above = this.m_20183_();
        int upBy = 100;
        for (int k = 0; world.m_46859_(above) && above.m_123342_() < this.m_9236_().m_151558_() && k < upBy; k++) {
            above = above.above();
        }
        if (world.m_46859_(above)) {
            this.setFlying(true);
        } else {
            this.setHanging(true);
        }
        this.m_6034_((double) ((float) above.m_123341_() + 0.5F), (double) above.m_123342_() - this.m_20191_().getYsize(), (double) ((float) above.m_123343_() + 0.5F));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (reason == MobSpawnType.NATURAL) {
            this.doInitialPosing(worldIn);
        }
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public static boolean checkVesperSpawnRules(EntityType<? extends Monster> entityType, ServerLevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        if (m_219013_(entityType, levelAccessor, mobSpawnType, blockPos, randomSource)) {
            BlockPos.MutableBlockPos above = new BlockPos.MutableBlockPos();
            above.set(blockPos);
            int k = 0;
            while (levelAccessor.m_46859_(above) && above.m_123342_() < levelAccessor.m_151558_()) {
                above.move(0, 1, 0);
                if (++k > 5) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void triggerSacrificeIn(int time) {
        this.isBeingSacrificed = true;
        this.sacrificeTime = time;
    }

    @Override
    public boolean isValidSacrifice(int distanceFromGround) {
        return distanceFromGround < (this.isHanging() ? 3 : 9);
    }

    @Override
    public int getAmbientSoundInterval() {
        return this.isHanging() ? 80 : 140;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isHanging() ? ACSoundRegistry.VESPER_QUIET_IDLE.get() : ACSoundRegistry.VESPER_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.VESPER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.VESPER_DEATH.get();
    }

    class MoveController extends MoveControl {

        private final Mob parentEntity = VesperEntity.this;

        public MoveController() {
            super(VesperEntity.this);
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
                double d0 = vector3d.length();
                double width = this.parentEntity.m_20191_().getSize();
                Vec3 vector3d1 = vector3d.scale(this.f_24978_ * 0.05 / d0);
                this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(vector3d1).scale(0.95).add(0.0, -0.01, 0.0));
                if (d0 < width) {
                    this.f_24981_ = MoveControl.Operation.WAIT;
                } else if (d0 >= width) {
                    float yaw = -((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180.0F / (float) Math.PI);
                    this.parentEntity.m_146922_(Mth.approachDegrees(this.parentEntity.m_146908_(), yaw, 8.0F));
                }
            }
        }
    }
}