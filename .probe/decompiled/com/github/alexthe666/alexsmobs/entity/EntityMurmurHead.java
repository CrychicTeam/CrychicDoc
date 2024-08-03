package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class EntityMurmurHead extends Monster implements FlyingAnimal {

    private static final EntityDataAccessor<Optional<UUID>> BODY_UUID = SynchedEntityData.defineId(EntityMurmurHead.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Integer> BODY_ID = SynchedEntityData.defineId(EntityMurmurHead.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> PULLED_IN = SynchedEntityData.defineId(EntityMurmurHead.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> ANGRY = SynchedEntityData.defineId(EntityMurmurHead.class, EntityDataSerializers.BOOLEAN);

    public double prevXHair;

    public double prevYHair;

    public double prevZHair;

    public double xHair;

    public double yHair;

    public double zHair;

    public float angerProgress;

    public float prevAngerProgress;

    private boolean prevLaunched = false;

    protected EntityMurmurHead(EntityType type, Level level) {
        super(type, level);
        this.f_21342_ = new EntityMurmurHead.MoveController();
    }

    protected EntityMurmurHead(EntityMurmur parent) {
        this(AMEntityRegistry.MURMUR_HEAD.get(), parent.m_9236_());
        this.setBodyId(parent.m_20148_());
        this.doSpawnPositioning(parent);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, this.m_9236_());
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.m_7008_(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    public int getExperienceReward() {
        return 0;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new EntityMurmurHead.AttackGoal());
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(9, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new EntityAINearestTarget3D(this, Player.class, 10, false, true, null));
        this.f_21346_.addGoal(3, new EntityAINearestTarget3D(this, AbstractVillager.class, 30, false, true, null));
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(BODY_UUID, Optional.empty());
        this.f_19804_.define(BODY_ID, -1);
        this.f_19804_.define(PULLED_IN, true);
        this.f_19804_.define(ANGRY, false);
    }

    private void doSpawnPositioning(EntityMurmur parent) {
        this.m_146884_(parent.getNeckBottom(1.0F).add(0.0, 0.5, 0.0));
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.FOLLOW_RANGE, 48.0).add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public boolean isPulledIn() {
        return this.f_19804_.get(PULLED_IN);
    }

    public void setPulledIn(boolean pulledIn) {
        this.f_19804_.set(PULLED_IN, pulledIn);
    }

    public boolean isAngry() {
        return this.f_19804_.get(ANGRY) || !this.m_6084_();
    }

    public void setAngry(boolean angry) {
        this.f_19804_.set(ANGRY, angry);
    }

    public Vec3 getNeckTop(float partialTick) {
        double d0 = Mth.lerp((double) partialTick, this.f_19854_, this.m_20185_());
        double d1 = Mth.lerp((double) partialTick, this.f_19855_, this.m_20186_());
        double d2 = Mth.lerp((double) partialTick, this.f_19856_, this.m_20189_());
        double bounce = 0.0;
        Entity body = this.getBody();
        if (body instanceof EntityMurmur) {
            bounce = ((EntityMurmur) body).calculateWalkBounce(partialTick);
        }
        return new Vec3(d0, d1 + bounce, d2);
    }

    public Vec3 getNeckBottom(float partialTick) {
        Entity body = this.getBody();
        Vec3 top = this.getNeckTop(partialTick);
        if (body instanceof EntityMurmur murmur) {
            Vec3 bodyBase = murmur.getNeckBottom(partialTick);
            double sub = top.subtract(bodyBase).horizontalDistance();
            return sub <= 0.06 ? new Vec3(top.x, bodyBase.y, top.z) : bodyBase;
        } else {
            return top.add(0.0, -0.5, 0.0);
        }
    }

    public boolean hasNeckBottom() {
        return true;
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Nullable
    public UUID getBodyId() {
        return (UUID) this.f_19804_.get(BODY_UUID).orElse(null);
    }

    public void setBodyId(@Nullable UUID uniqueId) {
        this.f_19804_.set(BODY_UUID, Optional.ofNullable(uniqueId));
    }

    public Entity getBody() {
        if (!this.m_9236_().isClientSide) {
            UUID id = this.getBodyId();
            return id == null ? null : ((ServerLevel) this.m_9236_()).getEntity(id);
        } else {
            int id = this.f_19804_.get(BODY_ID);
            return id == -1 ? null : this.m_9236_().getEntity(id);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        if (compound.hasUUID("BodyUUID")) {
            this.setBodyId(compound.getUUID("BodyUUID"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        if (this.getBodyId() != null) {
            compound.putUUID("BodyUUID", this.getBodyId());
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.35F;
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.f_20885_ = Mth.clamp(this.f_20885_, this.f_20883_ - 70.0F, this.f_20883_ + 70.0F);
        this.prevAngerProgress = this.angerProgress;
        if (this.isAngry() && this.angerProgress < 5.0F) {
            this.angerProgress++;
        }
        if (!this.isAngry() && this.angerProgress > 0.0F) {
            this.angerProgress--;
        }
        this.moveHair();
        Entity body = this.getBody();
        if (!this.m_9236_().isClientSide) {
            if (body instanceof EntityMurmur murmur) {
                this.f_19804_.set(BODY_ID, body.getId());
                if (this.isPulledIn() && murmur.m_6084_()) {
                    Vec3 base = murmur.getNeckBottom(1.0F).add(0.0, 0.55F, 0.0);
                    Vec3 vec3 = base.subtract(this.m_20182_());
                    if (vec3.length() < 1.0) {
                        this.m_6034_(base.x, base.y, base.z);
                        this.f_19794_ = false;
                    } else {
                        this.f_19794_ = true;
                        vec3 = base.subtract(this.m_20182_()).normalize();
                        float f = this.m_5448_() != null && this.m_5448_().isAlive() ? 0.3F : 0.15F;
                        this.m_20256_(vec3.scale((double) f));
                    }
                    this.m_146922_(murmur.m_146908_());
                    this.f_20883_ = murmur.m_146908_();
                } else {
                    this.f_19794_ = false;
                }
                LivingEntity headTarget = this.m_5448_();
                LivingEntity bodyTarget = murmur.m_5448_();
                if (headTarget != null && headTarget.isAlive()) {
                    if (murmur.m_6779_(headTarget)) {
                        murmur.m_6710_(headTarget);
                    } else {
                        this.m_6710_(null);
                        murmur.m_6710_(null);
                    }
                } else if (bodyTarget != null && bodyTarget.isAlive() && this.m_6779_(bodyTarget)) {
                    this.m_6710_(bodyTarget);
                }
                if (body.isRemoved()) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                }
            }
            if (body == null && this.f_19797_ > 20) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        } else if (body instanceof EntityMurmur murmur && (murmur.f_20916_ > 0 || murmur.f_20919_ > 0)) {
            this.f_20916_ = murmur.f_20916_;
            this.f_20919_ = murmur.f_20919_;
        }
        if (this.prevLaunched && !this.isPulledIn()) {
            this.m_5496_(AMSoundRegistry.MURMUR_NECK.get(), 3.0F * this.m_6121_(), this.m_6100_());
        }
        this.prevLaunched = this.isPulledIn();
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        Entity body = this.getBody();
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            return body != null && body.hurt(source, 0.5F * damage) ? true : super.m_6469_(source, damage);
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return super.m_6673_(damageSource) || damageSource.is(DamageTypes.IN_WALL);
    }

    private void moveHair() {
        this.prevXHair = this.xHair;
        this.prevYHair = this.yHair;
        this.prevZHair = this.zHair;
        double d0 = this.m_20185_() - this.xHair;
        double d1 = this.m_20186_() - this.yHair;
        double d2 = this.m_20189_() - this.zHair;
        if (d0 > 10.0) {
            this.xHair = this.m_20185_();
            this.prevXHair = this.xHair;
        }
        if (d2 > 10.0) {
            this.zHair = this.m_20189_();
            this.prevZHair = this.zHair;
        }
        if (d1 > 10.0) {
            this.yHair = this.m_20186_();
            this.prevYHair = this.yHair;
        }
        if (d0 < -10.0) {
            this.xHair = this.m_20185_();
            this.prevXHair = this.xHair;
        }
        if (d2 < -10.0) {
            this.zHair = this.m_20189_();
            this.prevZHair = this.zHair;
        }
        if (d1 < -10.0) {
            this.yHair = this.m_20186_();
            this.prevYHair = this.yHair;
        }
        this.xHair += d0 * 0.25;
        this.zHair += d2 * 0.25;
        this.yHair += d1 * 0.25;
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        return this.getBodyId() != null && entity.getUUID().equals(this.getBodyId()) || super.m_7307_(entity);
    }

    @Override
    public void playAmbientSound() {
        if (this.isPulledIn() && !this.isAngry()) {
            super.m_8032_();
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.MURMUR_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return this.getBody() == null ? AMSoundRegistry.MURMUR_HURT.get() : null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.getBody() == null ? AMSoundRegistry.MURMUR_HURT.get() : null;
    }

    @Override
    public boolean isFlying() {
        return true;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    private class AttackGoal extends Goal {

        private int time;

        private int biteCooldown = 0;

        private Vec3 emergeFrom = Vec3.ZERO;

        private float emergeAngle = 0.0F;

        public AttackGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return EntityMurmurHead.this.m_5448_() != null && EntityMurmurHead.this.m_5448_().isAlive();
        }

        @Override
        public void start() {
            this.time = 0;
            this.biteCooldown = 0;
            EntityMurmurHead.this.setPulledIn(false);
        }

        @Override
        public void stop() {
            this.time = 0;
            EntityMurmurHead.this.setPulledIn(true);
            EntityMurmurHead.this.setAngry(false);
        }

        @Override
        public void tick() {
            LivingEntity target = EntityMurmurHead.this.m_5448_();
            Entity body = EntityMurmurHead.this.getBody();
            if (target != null) {
                double dist = Math.sqrt(EntityMurmurHead.this.m_20238_(target.m_146892_()));
                double bodyDist = body != null ? (double) body.distanceTo(target) : 0.0;
                if (bodyDist > 16.0 && this.time > 30 && body instanceof EntityMurmur murmur) {
                    murmur.m_6710_(target);
                    murmur.m_21573_().moveTo(target, 1.35);
                }
                if (bodyDist > 64.0) {
                    EntityMurmurHead.this.setPulledIn(true);
                } else if (this.biteCooldown == 0) {
                    EntityMurmurHead.this.setPulledIn(false);
                    Vec3 moveTo = target.m_146892_();
                    if (this.time > 30) {
                        if (!EntityMurmurHead.this.isAngry()) {
                            EntityMurmurHead.this.m_5496_(AMSoundRegistry.MURMUR_ANGER.get(), 1.5F * EntityMurmurHead.this.m_6121_(), EntityMurmurHead.this.m_6100_());
                            EntityMurmurHead.this.m_146850_(GameEvent.ENTITY_ROAR);
                        }
                        EntityMurmurHead.this.setAngry(true);
                        EntityMurmurHead.this.m_21573_().moveTo(moveTo.x, moveTo.y, moveTo.z, 1.3);
                    } else {
                        if (this.time == 0) {
                            this.emergeFrom = EntityMurmurHead.this.getNeckTop(1.0F).add(0.0, 0.5, 0.0);
                            Vec3 clockwise = moveTo.subtract(this.emergeFrom);
                        }
                        boolean clockwise = false;
                        float circleDistance = 2.5F;
                        float circlingTime = (float) (30 * this.time);
                        float angle = (float) (Math.PI / 180.0) * (clockwise ? -circlingTime : circlingTime);
                        double extraX = (double) (circleDistance * Mth.sin((float) Math.PI + angle));
                        double extraZ = (double) (circleDistance * Mth.cos(angle));
                        double y = Math.max(this.emergeFrom.y + 2.0, target.m_20188_());
                        Vec3 vec3 = new Vec3(this.emergeFrom.x + extraX, y, this.emergeFrom.z + extraZ);
                        EntityMurmurHead.this.m_21573_().moveTo(vec3.x, vec3.y, vec3.z, 0.7);
                    }
                    EntityMurmurHead.this.m_7618_(EntityAnchorArgument.Anchor.EYES, moveTo);
                    if (dist < 1.5 && EntityMurmurHead.this.m_142582_(target)) {
                        EntityMurmurHead.this.m_5496_(AMSoundRegistry.MURMUR_ATTACK.get(), EntityMurmurHead.this.m_6121_(), EntityMurmurHead.this.m_6100_());
                        this.biteCooldown = 5 + EntityMurmurHead.this.m_217043_().nextInt(15);
                        target.hurt(EntityMurmurHead.this.m_269291_().mobAttack(EntityMurmurHead.this), 5.0F);
                    }
                } else {
                    EntityMurmurHead.this.setPulledIn(true);
                    EntityMurmurHead.this.m_7618_(EntityAnchorArgument.Anchor.EYES, target.m_146892_());
                    EntityMurmurHead.this.setAngry(false);
                }
                this.time++;
            }
            if (this.biteCooldown > 0) {
                this.biteCooldown--;
            }
        }
    }

    class MoveController extends MoveControl {

        private final Mob parentEntity = EntityMurmurHead.this;

        public MoveController() {
            super(EntityMurmurHead.this);
        }

        @Override
        public void tick() {
            if (!EntityMurmurHead.this.isPulledIn()) {
                float angle = (float) (Math.PI / 180.0) * (this.parentEntity.f_20883_ + 90.0F);
                float radius = (float) Math.sin((double) ((float) this.parentEntity.f_19797_ * 0.2F)) * 2.0F;
                double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
                double extraY = (double) radius * -Math.cos((double) angle - (Math.PI / 2));
                double extraZ = (double) (radius * Mth.cos(angle));
                Vec3 strafPlus = new Vec3(extraX, extraY, extraZ);
                if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                    Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
                    double d0 = vector3d.length();
                    double width = this.parentEntity.m_20191_().getSize();
                    Vec3 shimmy = Vec3.ZERO;
                    LivingEntity attackTarget = this.parentEntity.getTarget();
                    if (attackTarget != null && this.parentEntity.f_19862_) {
                        shimmy = new Vec3(0.0, 0.005, 0.0);
                    }
                    Vec3 vector3d1 = vector3d.scale(this.f_24978_ * 0.05 / d0);
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(vector3d1.add(strafPlus.scale(0.003 * Math.min(d0, 100.0)).add(shimmy))));
                    if (attackTarget == null) {
                        if (d0 >= width) {
                            Vec3 deltaMovement = this.parentEntity.m_20184_();
                            this.parentEntity.m_146922_(-((float) Mth.atan2(deltaMovement.x, deltaMovement.z)) * (180.0F / (float) Math.PI));
                            this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
                        }
                    } else {
                        double d2 = attackTarget.m_20185_() - this.parentEntity.m_20185_();
                        double d1 = attackTarget.m_20189_() - this.parentEntity.m_20189_();
                        this.parentEntity.m_146922_(-((float) Mth.atan2(d2, d1)) * (180.0F / (float) Math.PI));
                        this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
                    }
                } else if (this.f_24981_ == MoveControl.Operation.WAIT) {
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(strafPlus.scale(0.003)));
                }
            }
        }
    }
}