package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class BoundroidWinchEntity extends Monster {

    private static final EntityDataAccessor<Optional<UUID>> HEAD_UUID = SynchedEntityData.defineId(BoundroidWinchEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Integer> HEAD_ID = SynchedEntityData.defineId(BoundroidWinchEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> LATCHED = SynchedEntityData.defineId(BoundroidWinchEntity.class, EntityDataSerializers.BOOLEAN);

    private static final float MAX_DIST_TO_CEILING = 2.9F;

    private float latchProgress;

    private float prevLatchProgress;

    private float distanceToCeiling;

    private boolean goingUp;

    private int lastStepTimestamp = -1;

    private boolean isUpsideDownNavigator = false;

    private int noLatchCooldown = 0;

    private int changeLatchStateTime = 0;

    public BoundroidWinchEntity(EntityType entityType, Level level) {
        super(entityType, level);
        this.switchNavigator(false);
    }

    public BoundroidWinchEntity(BoundroidEntity parent) {
        this(ACEntityRegistry.BOUNDROID_WINCH.get(), parent.m_9236_());
        this.setHeadUUID(parent.m_20148_());
        this.m_146884_(parent.m_20182_().add(0.0, 0.5, 0.0));
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new BoundroidWinchEntity.MeleeGoal());
        this.f_21345_.addGoal(2, new BoundroidWinchEntity.FindShelterGoal());
        this.f_21345_.addGoal(3, new BoundroidWinchEntity.WanderUpsideDownGoal());
        this.f_21345_.addGoal(4, new RandomStrollGoal(this, 1.0, 45));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.2).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MAX_HEALTH, 20.0);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return this.getHead() != null;
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        Entity body = this.getHead();
        if (this.m_6673_(source)) {
            return false;
        } else if (body != null && !body.isInvulnerableTo(source)) {
            boolean flag = body.hurt(source, damage);
            if (flag) {
                this.noLatchCooldown = 60 + this.f_19796_.nextInt(60);
            }
            return flag;
        } else {
            return super.m_6469_(source, damage);
        }
    }

    public void linkWithHead(Entity head) {
        this.setHeadUUID(head.getUUID());
        this.f_19804_.set(HEAD_ID, head.getId());
    }

    public boolean isLatched() {
        return this.f_19804_.get(LATCHED);
    }

    public void setLatched(boolean latched) {
        this.f_19804_.set(LATCHED, latched);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        if (compound.hasUUID("HeadUUID")) {
            this.setHeadUUID(compound.getUUID("HeadUUID"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        if (this.getHeadUUID() != null) {
            compound.putUUID("BodyUUID", this.getHeadUUID());
        }
    }

    private void switchNavigator(boolean clinging) {
        if (clinging) {
            this.f_21342_ = new BoundroidWinchEntity.CeilingMoveControl();
            this.f_21344_ = this.createCeilingNavigator(this.m_9236_());
            this.isUpsideDownNavigator = true;
        } else {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new GroundPathNavigation(this, this.m_9236_());
            this.isUpsideDownNavigator = false;
        }
    }

    protected PathNavigation createCeilingNavigator(Level level) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level) {

            @Override
            public boolean isStableDestination(BlockPos pos) {
                int airAbove;
                for (airAbove = 0; BoundroidWinchEntity.this.m_9236_().getBlockState(pos).m_60795_() && (float) airAbove < 3.9F; airAbove++) {
                    pos = pos.above();
                }
                return (float) airAbove < Math.min(2.9F, (float) BoundroidWinchEntity.this.f_19796_.nextInt(2));
            }
        };
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.m_7008_(false);
        return flyingpathnavigation;
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevLatchProgress = this.latchProgress;
        if (this.isLatched() && this.latchProgress < 5.0F) {
            this.latchProgress++;
        }
        if (!this.isLatched() && this.latchProgress > 0.0F) {
            this.latchProgress--;
        }
        if (this.noLatchCooldown > 0) {
            this.noLatchCooldown--;
        }
        double d1 = this.m_20185_() - this.f_19854_;
        double d2 = this.m_20189_() - this.f_19856_;
        double d3 = Math.sqrt(d1 * d1 + d2 * d2);
        if (this.isLatched() && this.f_19863_ && d3 > 0.1F && this.f_19797_ - this.lastStepTimestamp > 6) {
            this.lastStepTimestamp = this.f_19797_;
            BlockState state = this.m_9236_().getBlockState(BlockPos.containing(this.m_20185_(), this.m_20191_().maxY + 0.5, this.m_20189_()));
            this.m_5496_(state.m_60827_().getStepSound(), 1.0F, 0.5F);
        }
        if (!this.m_9236_().isClientSide) {
            if (this.isLatched() && !this.isUpsideDownNavigator) {
                this.switchNavigator(true);
            }
            if (!this.isLatched() && this.isUpsideDownNavigator) {
                this.switchNavigator(false);
            }
        }
        Entity head = this.getHead();
        if (head instanceof BoundroidEntity boundroid) {
            if (!this.m_9236_().isClientSide) {
                this.f_19804_.set(HEAD_ID, head.getId());
                double distance = (double) this.m_20270_(boundroid);
                double distanceGoal = this.isLatched() ? 1.25 + Math.sin((double) ((float) this.f_19797_ * 0.1F)) * 0.25 : 3.5;
                boolean headNoClip = false;
                float pullSpeed = boundroid.m_5448_() != null ? 0.3F : 0.1F;
                if (!boundroid.m_142582_(this) && !boundroid.stopPullingUp() && distance > 7.0) {
                    headNoClip = true;
                }
                if (distance > distanceGoal && !boundroid.stopPullingUp()) {
                    double disRem = Math.min(distance - distanceGoal, 1.0);
                    Vec3 moveTo = this.getChainFrom(1.0F).subtract(boundroid.m_20182_());
                    if (moveTo.length() > 1.0) {
                        moveTo = moveTo.normalize();
                    }
                    boundroid.draggedClimable = true;
                    boundroid.m_20256_(boundroid.m_20184_().multiply(0.95F, 0.7F, 0.95F).add(moveTo.scale(disRem * (double) pullSpeed)));
                } else {
                    boundroid.draggedClimable = false;
                }
                this.distanceToCeiling = this.calculateDistanceToCeiling();
                if (this.isLatched()) {
                    this.m_20242_(true);
                    if (!(this.distanceToCeiling > 2.9F) && this.m_6084_() && this.noLatchCooldown <= 0) {
                        this.changeLatchStateTime = 0;
                    } else {
                        this.changeLatchStateTime++;
                    }
                    if (this.changeLatchStateTime > 5) {
                        this.setLatched(false);
                        if (this.noLatchCooldown > 0) {
                            this.m_5496_(ACSoundRegistry.BOUNDROID_DAZED.get(), 2.0F, 1.0F);
                        }
                        this.changeLatchStateTime = 0;
                    }
                    this.m_20256_(this.m_20184_().add(0.0, 0.14, 0.0).scale(0.85F));
                    this.goingUp = false;
                    boundroid.stopGravity = true;
                } else {
                    this.m_20242_(false);
                    boundroid.stopGravity = false;
                    if ((this.distanceToCeiling < 2.9F || this.f_19863_ && !this.f_201939_) && this.noLatchCooldown <= 0) {
                        this.changeLatchStateTime++;
                    } else {
                        this.changeLatchStateTime = 0;
                    }
                    if (this.changeLatchStateTime > 5) {
                        this.setLatched(true);
                        this.changeLatchStateTime = 0;
                    }
                    if (this.goingUp && !this.hasBlockAbove()) {
                        this.goingUp = false;
                    }
                    if (this.goingUp) {
                        this.m_20256_(new Vec3(this.m_20184_().x, 1.5, this.m_20184_().z));
                    } else if (this.m_20096_() && this.noLatchCooldown == 0 && this.m_6084_() && this.f_19796_.nextInt(30) == 0 && this.distanceToCeiling > 2.9F && this.hasBlockAbove()) {
                        this.goingUp = true;
                    }
                }
                boundroid.f_19794_ = headNoClip;
            }
            if (boundroid.f_20916_ > 0 || boundroid.f_20919_ > 0) {
                this.f_20916_ = boundroid.f_20916_;
                this.f_20919_ = boundroid.f_20919_;
            }
        } else if (!this.m_9236_().isClientSide) {
            this.m_142687_(Entity.RemovalReason.KILLED);
        }
    }

    private boolean hasBlockAbove() {
        if (!this.m_9236_().m_45527_(this.m_20183_())) {
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(this.m_20185_(), this.m_20188_(), this.m_20189_());
            while (mutableBlockPos.m_123342_() < this.m_9236_().m_151558_()) {
                if (this.m_9236_().getBlockState(mutableBlockPos).m_280296_()) {
                    return true;
                }
                mutableBlockPos.move(0, 1, 0);
            }
        }
        return false;
    }

    public float getLatchProgress(float partialTicks) {
        return (this.prevLatchProgress + (this.latchProgress - this.prevLatchProgress) * partialTicks) * 0.2F;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public float getChainLength(float partialTick) {
        return (float) this.getChainTo(partialTick).subtract(this.getChainFrom(partialTick)).length();
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(HEAD_UUID, Optional.empty());
        this.f_19804_.define(HEAD_ID, -1);
        this.f_19804_.define(LATCHED, false);
    }

    @Nullable
    public UUID getHeadUUID() {
        return (UUID) this.f_19804_.get(HEAD_UUID).orElse(null);
    }

    public void setHeadUUID(@Nullable UUID uniqueId) {
        this.f_19804_.set(HEAD_UUID, Optional.ofNullable(uniqueId));
    }

    public Vec3 getChainFrom(float partialTicks) {
        return this.m_20318_(partialTicks).add(0.0, 0.0, 0.0);
    }

    public Vec3 getChainTo(float partialTicks) {
        return this.getHead() instanceof BoundroidEntity boundroid ? boundroid.m_20318_(partialTicks).add(0.0, (double) boundroid.m_20206_(), 0.0) : this.m_20318_(partialTicks).add(0.0, 0.3, 0.0);
    }

    public Entity getHead() {
        if (!this.m_9236_().isClientSide) {
            UUID id = this.getHeadUUID();
            return id == null ? null : ((ServerLevel) this.m_9236_()).getEntity(id);
        } else {
            int id = this.f_19804_.get(HEAD_ID);
            return id == -1 ? null : this.m_9236_().getEntity(id);
        }
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.m_21515_() && this.isLatched()) {
            this.m_19920_(this.m_6113_(), travelVector);
            this.m_6478_(MoverType.SELF, this.m_20184_());
        } else {
            super.m_7023_(travelVector);
        }
    }

    private float calculateDistanceToCeiling() {
        BlockPos ceiling = this.getCeilingOf(this.m_20183_());
        return (float) ((double) ceiling.m_123342_() - this.m_20191_().maxY);
    }

    public BlockPos getCeilingOf(BlockPos usPos) {
        while (!this.m_9236_().getBlockState(usPos).m_60783_(this.m_9236_(), usPos, Direction.DOWN) && usPos.m_123342_() < this.m_9236_().m_151558_()) {
            usPos = usPos.above();
        }
        return usPos;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effectInstance) {
        return super.m_7301_(effectInstance) && effectInstance.getEffect() != ACEffectRegistry.MAGNETIZING.get();
    }

    class CeilingMoveControl extends MoveControl {

        private final Mob parentEntity = BoundroidWinchEntity.this;

        public CeilingMoveControl() {
            super(BoundroidWinchEntity.this);
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
                double d0 = vector3d.length();
                double width = this.parentEntity.m_20191_().getSize();
                Vec3 vector3d1 = vector3d.scale(this.f_24978_ * 0.035 / d0);
                float verticalSpeed = 0.15F;
                float y = this.parentEntity.f_19862_ ? -0.2F : (this.parentEntity.f_19863_ ? 0.2F : 1.2F);
                this.parentEntity.m_20256_(this.parentEntity.m_20184_().scale(0.95F).add(0.0, (double) y, 0.0).add(vector3d1.multiply(1.0, (double) verticalSpeed, 1.0)));
                if (this.parentEntity.getTarget() != null) {
                    double d1 = this.parentEntity.getTarget().m_20189_() - this.parentEntity.m_20189_();
                    double d3 = this.parentEntity.getTarget().m_20186_() - this.parentEntity.m_20186_();
                    double d2 = this.parentEntity.getTarget().m_20185_() - this.parentEntity.m_20185_();
                    float f = Mth.sqrt((float) (d2 * d2 + d1 * d1));
                    this.parentEntity.m_146922_(-((float) Mth.atan2(d2, d1)) * (180.0F / (float) Math.PI));
                    this.parentEntity.m_146926_((float) (Mth.atan2(d3, (double) f) * 180.0F / (float) Math.PI));
                    this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
                } else if (d0 >= width) {
                    this.parentEntity.m_146922_(-((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180.0F / (float) Math.PI));
                }
            }
        }
    }

    class FindShelterGoal extends Goal {

        private double wantedX;

        private double wantedY;

        private double wantedZ;

        public FindShelterGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return !BoundroidWinchEntity.this.m_9236_().m_45527_(BoundroidWinchEntity.this.m_20183_()) && BoundroidWinchEntity.this.m_217043_().nextInt(20) != 0 ? false : this.setWantedPos();
        }

        protected boolean setWantedPos() {
            Vec3 vec3 = this.getHidePos();
            if (vec3 == null) {
                return false;
            } else {
                this.wantedX = vec3.x;
                this.wantedY = vec3.y;
                this.wantedZ = vec3.z;
                return true;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return !BoundroidWinchEntity.this.m_21573_().isDone();
        }

        @Override
        public void start() {
            BoundroidWinchEntity.this.m_21573_().moveTo(this.wantedX, this.wantedY, this.wantedZ, 1.0);
        }

        @Nullable
        protected Vec3 getHidePos() {
            RandomSource randomsource = BoundroidWinchEntity.this.m_217043_();
            BlockPos blockpos = BoundroidWinchEntity.this.m_20183_();
            for (int i = 0; i < 10; i++) {
                BlockPos blockpos1 = blockpos.offset(randomsource.nextInt(20) - 10, randomsource.nextInt(6) - 3, randomsource.nextInt(20) - 10);
                if (!BoundroidWinchEntity.this.m_9236_().m_45527_(blockpos1) && BoundroidWinchEntity.this.m_21692_(blockpos1) < 0.0F) {
                    return Vec3.atBottomCenterOf(blockpos1);
                }
            }
            return null;
        }
    }

    private class MeleeGoal extends Goal {

        public MeleeGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (!(BoundroidWinchEntity.this.getHead() instanceof BoundroidEntity boundroid)) {
                return false;
            } else {
                Entity target = boundroid.m_5448_();
                return target != null && target.isAlive();
            }
        }

        @Override
        public void stop() {
            if (BoundroidWinchEntity.this.getHead() instanceof BoundroidEntity boundroid) {
                boundroid.setScared(false);
            }
        }

        @Override
        public void tick() {
            if (BoundroidWinchEntity.this.getHead() instanceof BoundroidEntity boundroid) {
                Entity target = boundroid.m_5448_();
                if (target != null && target.isAlive()) {
                    if (BoundroidWinchEntity.this.isLatched()) {
                        boundroid.setScared(false);
                        BlockPos lowestPos = BoundroidWinchEntity.this.getCeilingOf(target.blockPosition());
                        BoundroidWinchEntity.this.m_21573_().moveTo((double) lowestPos.m_123341_(), (double) lowestPos.m_123342_(), (double) lowestPos.m_123343_(), 1.0);
                    } else {
                        if (BoundroidWinchEntity.this.m_21573_().isDone()) {
                            Vec3 vec = LandRandomPos.getPosAway(BoundroidWinchEntity.this, 15, 7, target.position());
                            if (vec != null) {
                                BoundroidWinchEntity.this.m_21573_().moveTo(vec.x, vec.y, vec.z, 1.3);
                            }
                        }
                        boundroid.setScared(true);
                    }
                }
            }
        }
    }

    class WanderUpsideDownGoal extends RandomStrollGoal {

        private int stillTicks = 0;

        public WanderUpsideDownGoal() {
            super(BoundroidWinchEntity.this, 1.0, 10);
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Nullable
        @Override
        protected Vec3 getPosition() {
            if (BoundroidWinchEntity.this.isLatched()) {
                int distance = 16;
                int i = 0;
                if (i < 15) {
                    Random rand = new Random();
                    BlockPos randPos = BoundroidWinchEntity.this.m_20183_().offset(rand.nextInt(distance * 2) - distance, -5, rand.nextInt(distance * 2) - distance);
                    BlockPos lowestPos = BoundroidWinchEntity.this.getCeilingOf(randPos).below();
                    return Vec3.atCenterOf(lowestPos);
                } else {
                    return null;
                }
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
            return super.canContinueToUse() && BoundroidWinchEntity.this.f_19796_.nextInt(100) != 0;
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
            this.stillTicks = 0;
            this.f_25725_.m_21573_().moveTo(this.f_25726_, this.f_25727_, this.f_25728_, this.f_25729_);
        }
    }
}