package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AquaticMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.PartEntity;

public class EntityGiantSquid extends WaterAnimal {

    private static final EntityDataAccessor<Float> SQUID_PITCH = SynchedEntityData.defineId(EntityGiantSquid.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> DEPRESSURIZATION = SynchedEntityData.defineId(EntityGiantSquid.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Boolean> OVERRIDE_BODYROT = SynchedEntityData.defineId(EntityGiantSquid.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> GRABBING = SynchedEntityData.defineId(EntityGiantSquid.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> CAPTURED = SynchedEntityData.defineId(EntityGiantSquid.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> BLUE = SynchedEntityData.defineId(EntityGiantSquid.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> GRAB_ENTITY = SynchedEntityData.defineId(EntityGiantSquid.class, EntityDataSerializers.INT);

    public final EntityGiantSquidPart mantlePart1;

    public final EntityGiantSquidPart mantlePart2;

    public final EntityGiantSquidPart mantlePart3;

    public final EntityGiantSquidPart tentaclesPart1;

    public final EntityGiantSquidPart tentaclesPart2;

    public final EntityGiantSquidPart tentaclesPart3;

    public final EntityGiantSquidPart tentaclesPart4;

    public final EntityGiantSquidPart tentaclesPart5;

    public final EntityGiantSquidPart tentaclesPart6;

    public final EntityGiantSquidPart mantleCollisionPart;

    public final EntityGiantSquidPart[] allParts;

    public final float[][] ringBuffer = new float[64][2];

    public int ringBufferIndex = -1;

    public float prevSquidPitch;

    public float prevDepressurization;

    public float grabProgress;

    public float prevGrabProgress;

    public float dryProgress;

    public float prevDryProgress;

    public float capturedProgress;

    public float prevCapturedProgress;

    public int humTick = 0;

    private int holdTime;

    private int resetCapturedStateIn;

    protected EntityGiantSquid(EntityType type, Level level) {
        super(type, level);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.mantlePart1 = new EntityGiantSquidPart(this, 0.9F, 0.9F);
        this.mantlePart2 = new EntityGiantSquidPart(this, 1.2F, 1.2F);
        this.mantlePart3 = new EntityGiantSquidPart(this, 0.45F, 0.45F);
        this.tentaclesPart1 = new EntityGiantSquidPart(this, 0.9F, 0.9F);
        this.tentaclesPart2 = new EntityGiantSquidPart(this, 1.0F, 1.0F);
        this.tentaclesPart3 = new EntityGiantSquidPart(this, 1.2F, 1.2F);
        this.tentaclesPart4 = new EntityGiantSquidPart(this, 1.2F, 1.2F);
        this.tentaclesPart5 = new EntityGiantSquidPart(this, 1.2F, 1.2F);
        this.tentaclesPart6 = new EntityGiantSquidPart(this, 1.2F, 1.2F);
        this.mantleCollisionPart = new EntityGiantSquidPart(this, 2.9F, 2.9F, true);
        this.allParts = new EntityGiantSquidPart[] { this.mantlePart1, this.mantlePart2, this.mantlePart3, this.mantleCollisionPart, this.tentaclesPart1, this.tentaclesPart2, this.tentaclesPart3, this.tentaclesPart4, this.tentaclesPart5, this.tentaclesPart6 };
        this.f_21365_ = new SmoothSwimmingLookControl(this, 4);
        this.f_21342_ = new AquaticMoveController(this, 1.2F, 5.0F);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.giantSquidSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static boolean canGiantSquidSpawn(EntityType<EntityGiantSquid> entityType, ServerLevelAccessor iServerWorld, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return reason == MobSpawnType.SPAWNER || iServerWorld.m_46801_(pos) && iServerWorld.m_46801_(pos.above());
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (reason == MobSpawnType.NATURAL) {
            this.doInitialPosing(worldIn);
        }
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    private void doInitialPosing(LevelAccessor world) {
        BlockPos down = this.m_20183_();
        while (!world.m_6425_(down).isEmpty() && down.m_123342_() > 1) {
            down = down.below();
        }
        this.m_6034_((double) ((float) down.m_123341_() + 0.5F), (double) (down.m_123342_() + 3 + this.f_19796_.nextInt(3)), (double) ((float) down.m_123343_() + 0.5F));
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.GIANT_SQUID_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.GIANT_SQUID_HURT.get();
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 38.0).add(Attributes.ATTACK_DAMAGE, 8.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(SQUID_PITCH, 0.0F);
        this.f_19804_.define(OVERRIDE_BODYROT, false);
        this.f_19804_.define(DEPRESSURIZATION, 0.0F);
        this.f_19804_.define(GRABBING, false);
        this.f_19804_.define(CAPTURED, false);
        this.f_19804_.define(BLUE, false);
        this.f_19804_.define(GRAB_ENTITY, -1);
    }

    @Nullable
    public Entity getGrabbedEntity() {
        return (Entity) (this.m_9236_().isClientSide && this.f_19804_.get(GRAB_ENTITY) != -1 ? this.m_9236_().getEntity(this.f_19804_.get(GRAB_ENTITY)) : this.m_5448_());
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.getItem();
        return super.m_6071_(player, hand);
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new WaterBoundPathNavigation(this, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new TryFindWaterGoal(this));
        this.f_21345_.addGoal(1, new EntityGiantSquid.AIAvoidWhales());
        this.f_21345_.addGoal(2, new EntityGiantSquid.AIMelee());
        this.f_21345_.addGoal(3, new EntityGiantSquid.AIDeepwaterSwimming());
        this.f_21345_.addGoal(5, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this, EntityCachalotWhale.class));
        this.f_21346_.addGoal(2, new EntityAINearestTarget3D(this, Guardian.class, 20, true, true, null) {

            @Override
            public boolean canUse() {
                return super.m_8036_();
            }
        });
        this.f_21346_.addGoal(3, new EntityAINearestTarget3D(this, LivingEntity.class, 70, false, true, AMEntityRegistry.buildPredicateFromTag(AMTagRegistry.GIANT_SQUID_TARGETS)) {

            @Override
            public boolean canUse() {
                return !EntityGiantSquid.this.m_20072_() && !EntityGiantSquid.this.isCaptured() && super.m_8036_();
            }
        });
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        if (!this.m_21525_()) {
            if (this.ringBufferIndex < 0) {
                for (int i = 0; i < this.ringBuffer.length; i++) {
                    this.ringBuffer[i][0] = 180.0F + this.m_146908_();
                    this.ringBuffer[i][1] = this.getSquidPitch();
                }
            }
            this.ringBufferIndex++;
            if (this.ringBufferIndex == this.ringBuffer.length) {
                this.ringBufferIndex = 0;
            }
            this.ringBuffer[this.ringBufferIndex][0] = this.f_20883_;
            this.ringBuffer[this.ringBufferIndex][1] = this.getSquidPitch();
        }
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.f_19797_ % 100 == 0) {
            this.m_5634_(2.0F);
        }
        float f = Mth.wrapDegrees(180.0F + this.m_146908_());
        this.f_20883_ = this.rotlerp(this.f_20883_, f, 180.0F);
        this.prevSquidPitch = this.getSquidPitch();
        this.prevDepressurization = this.getDepressurization();
        this.prevDryProgress = this.dryProgress;
        this.prevGrabProgress = this.grabProgress;
        this.prevCapturedProgress = this.capturedProgress;
        if (!this.m_20069_() && this.dryProgress < 5.0F) {
            this.dryProgress++;
        }
        if (this.m_20069_() && this.dryProgress > 0.0F) {
            this.dryProgress--;
        }
        if (this.isGrabbing()) {
            if (this.grabProgress < 5.0F) {
                this.grabProgress += 0.25F;
            }
        } else if (this.grabProgress > 0.0F) {
            this.grabProgress -= 0.25F;
        }
        if (this.isCaptured()) {
            if (this.capturedProgress < 5.0F) {
                this.capturedProgress += 0.5F;
            }
        } else if (this.capturedProgress > 0.0F) {
            this.capturedProgress -= 0.5F;
        }
        if (this.isGrabbing()) {
            Entity target = this.getGrabbedEntity();
            if (!this.m_9236_().isClientSide && target != null) {
                this.f_19804_.set(GRAB_ENTITY, target.getId());
                if (this.holdTime % 20 == 0 && this.holdTime > 30) {
                    target.hurt(this.m_269291_().mobAttack(this), (float) (3 + this.f_19796_.nextInt(5)));
                }
            }
            if (target != null && target.isAlive()) {
                this.m_146926_(0.0F);
                float invert = 1.0F - this.grabProgress * 0.2F;
                Vec3 extraVec = new Vec3(0.0, 0.0, (double) (2.0F + invert * 7.0F)).xRot(-this.getXRot() * (float) (Math.PI / 180.0)).yRot(-this.f_20883_ * (float) (Math.PI / 180.0));
                Vec3 minus = new Vec3(this.m_20185_() + extraVec.x - target.getX(), this.m_20186_() + extraVec.y - target.getY(), this.m_20189_() + extraVec.z - target.getZ());
                target.setDeltaMovement(minus);
            }
            this.holdTime++;
            if (this.holdTime > 1000) {
                this.holdTime = 0;
                this.setGrabbing(false);
            }
        } else {
            this.holdTime = 0;
        }
        if (!this.m_21525_()) {
            Vec3[] avector3d = new Vec3[this.allParts.length];
            for (int j = 0; j < this.allParts.length; j++) {
                this.allParts[j].collideWithNearbyEntities();
                avector3d[j] = new Vec3(this.allParts[j].m_20185_(), this.allParts[j].m_20186_(), this.allParts[j].m_20189_());
            }
            float pitch = this.getXRot() * (float) (Math.PI / 180.0) * 0.8F;
            this.mantleCollisionPart.m_6034_(this.m_20185_(), this.m_20186_() - (double) ((this.mantleCollisionPart.m_20206_() - this.m_20192_()) * 0.5F * (1.0F - this.dryProgress * 0.2F)), this.m_20189_());
            this.setPartPositionFromBuffer(this.mantlePart1, pitch, 0.9F, 0);
            this.setPartPositionFromBuffer(this.mantlePart2, pitch, 1.6F, 0);
            this.setPartPositionFromBuffer(this.mantlePart3, pitch, 2.45F, 0);
            this.setPartPositionFromBuffer(this.tentaclesPart1, pitch, -0.8F, 0);
            this.setPartPositionFromBuffer(this.tentaclesPart2, pitch, -1.5F, 0);
            this.setPartPositionFromBuffer(this.tentaclesPart3, pitch, -2.3F, 5);
            this.setPartPositionFromBuffer(this.tentaclesPart4, pitch, -3.4F, 10);
            this.setPartPositionFromBuffer(this.tentaclesPart5, pitch, -5.4F, 15);
            this.setPartPositionFromBuffer(this.tentaclesPart6, pitch, -7.4F, 20);
            if (this.m_20072_()) {
                if (this.mantleCollisionPart.scale != 1.0F) {
                    this.mantleCollisionPart.scale = 1.0F;
                    this.mantleCollisionPart.m_6210_();
                }
            } else if (this.mantleCollisionPart.scale != 0.25F) {
                this.mantleCollisionPart.scale = 0.25F;
                this.mantleCollisionPart.m_6210_();
            }
            for (int l = 0; l < this.allParts.length; l++) {
                this.allParts[l].f_19854_ = avector3d[l].x;
                this.allParts[l].f_19855_ = avector3d[l].y;
                this.allParts[l].f_19856_ = avector3d[l].z;
                this.allParts[l].f_19790_ = avector3d[l].x;
                this.allParts[l].f_19791_ = avector3d[l].y;
                this.allParts[l].f_19792_ = avector3d[l].z;
            }
            this.m_20242_(this.m_20069_());
        }
        if (!this.m_9236_().isClientSide) {
            if (this.getSquidPitch() > 0.0F) {
                float decrease = Math.min(2.0F, this.getSquidPitch());
                this.decrementSquidPitch(decrease);
            }
            if (this.getSquidPitch() < 0.0F) {
                float decrease = Math.min(2.0F, -this.getSquidPitch());
                this.incrementSquidPitch(decrease);
            }
            if (this.m_20072_()) {
                float dist = (float) this.m_20184_().y() * 45.0F;
                if (this.f_19804_.get(OVERRIDE_BODYROT)) {
                    this.decrementSquidPitch(dist);
                } else {
                    this.incrementSquidPitch(dist);
                }
            }
            if (!this.m_20096_() && this.m_204036_(FluidTags.WATER) < (double) this.m_20206_()) {
                this.m_20256_(this.m_20184_().add(0.0, -0.1F, 0.0));
            }
            float pressure = this.getDepressureLevel();
            if (this.getDepressurization() < pressure) {
                this.setDepressurization(this.getDepressurization() + 0.1F);
            }
            if (this.getDepressurization() > pressure) {
                this.setDepressurization(this.getDepressurization() - 0.1F);
            }
        }
        if (this.isHumming()) {
            if (this.humTick % 20 == 0) {
                this.m_5496_(AMSoundRegistry.GIANT_SQUID_GAMES.get(), this.m_6121_(), 1.0F);
                this.humTick = 0;
            }
            this.humTick++;
        }
        if (!this.m_9236_().isClientSide) {
            if (this.resetCapturedStateIn > 0) {
                this.resetCapturedStateIn--;
            } else {
                this.setCaptured(false);
            }
        }
    }

    private boolean isHumming() {
        String s = ChatFormatting.stripFormatting(this.m_7755_().getString());
        return s != null && s.toLowerCase().contains("squid games!!") || AlexsMobs.isAprilFools();
    }

    public float getRingBuffer(int bufferOffset, float partialTicks, boolean pitch) {
        int i = this.ringBufferIndex - bufferOffset & 63;
        int j = this.ringBufferIndex - bufferOffset - 1 & 63;
        int k = pitch ? 1 : 0;
        float prevBuffer = this.ringBuffer[j][k];
        float buffer = this.ringBuffer[i][k];
        float end = prevBuffer + (buffer - prevBuffer) * partialTicks;
        return this.rotlerp(prevBuffer, end, 10.0F);
    }

    private void setPartPosition(EntityGiantSquidPart part, double offsetX, double offsetY, double offsetZ, float offsetScale) {
        part.m_6034_(this.m_20185_() + offsetX * (double) offsetScale * (double) part.scale, this.m_20186_() + offsetY * (double) offsetScale * (double) part.scale, this.m_20189_() + offsetZ * (double) offsetScale * (double) part.scale);
    }

    private void setPartPositionFromBuffer(EntityGiantSquidPart part, float pitch, float offsetScale, int ringBufferOffset) {
        float f2 = Mth.sin(this.getRingBuffer(ringBufferOffset, 1.0F, false) * (float) (Math.PI / 180.0)) * (1.0F - Math.abs(this.getXRot() / 90.0F));
        float f3 = Mth.cos(this.getRingBuffer(ringBufferOffset, 1.0F, false) * (float) (Math.PI / 180.0)) * (1.0F - Math.abs(this.getXRot() / 90.0F));
        this.setPartPosition(part, (double) f2, (double) pitch, (double) (-f3), offsetScale);
    }

    @Override
    public int getMaxHeadXRot() {
        return 1;
    }

    @Override
    public int getMaxHeadYRot() {
        return 3;
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.m_21515_() && this.m_20069_()) {
            if (this.f_19804_.get(OVERRIDE_BODYROT)) {
                travelVector = new Vec3(travelVector.x, travelVector.y, -travelVector.z);
            }
            this.m_19920_(this.m_6113_(), travelVector);
            double d = this.m_5448_() == null ? 0.6 : 0.9;
            this.m_20256_(this.m_20184_().multiply(0.9, d, 0.9));
            this.m_6478_(MoverType.SELF, this.m_20184_());
        } else {
            super.m_7023_(travelVector);
        }
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
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
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.setBlue(compound.getBoolean("Blue"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        compound.putBoolean("Blue", this.isBlue());
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        return worldIn.m_45784_(this);
    }

    public float getDepressurization() {
        return Mth.clamp(this.f_19804_.get(DEPRESSURIZATION), 0.0F, 1.0F);
    }

    public void setDepressurization(float depressurization) {
        this.f_19804_.set(DEPRESSURIZATION, depressurization);
    }

    public float getSquidPitch() {
        return Mth.clamp(this.f_19804_.get(SQUID_PITCH), -90.0F, 90.0F);
    }

    public void setSquidPitch(float pitch) {
        this.f_19804_.set(SQUID_PITCH, pitch);
    }

    public void incrementSquidPitch(float pitch) {
        this.f_19804_.set(SQUID_PITCH, this.getSquidPitch() + pitch);
    }

    public void decrementSquidPitch(float pitch) {
        this.f_19804_.set(SQUID_PITCH, this.getSquidPitch() - pitch);
    }

    public boolean isGrabbing() {
        return this.f_19804_.get(GRABBING);
    }

    public void setGrabbing(boolean running) {
        this.f_19804_.set(GRABBING, running);
    }

    public boolean isCaptured() {
        return this.f_19804_.get(CAPTURED);
    }

    public void setCaptured(boolean running) {
        this.f_19804_.set(CAPTURED, running);
    }

    public boolean isBlue() {
        return this.f_19804_.get(BLUE);
    }

    public void setBlue(boolean t) {
        this.f_19804_.set(BLUE, t);
    }

    @Override
    public void push(Entity entity) {
        if (!this.isCaptured()) {
            super.m_7334_(entity);
        }
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, this.m_20186_() - this.f_19855_, this.m_20189_() - this.f_19856_);
        float f2 = Math.min(f1 * 8.0F, 1.0F);
        this.f_267362_.update(f2, 0.4F);
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.m_6084_();
    }

    @Override
    public Vec3 collide(Vec3 movement) {
        if (!this.m_146899_() && this.m_20072_()) {
            AABB aabb = this.mantleCollisionPart.m_20191_();
            List<VoxelShape> list = this.m_9236_().m_183134_(this, aabb.expandTowards(movement));
            Vec3 vec3 = movement.lengthSqr() == 0.0 ? movement : m_198894_(this, movement, aabb, this.m_9236_(), list);
            boolean flag = movement.x != vec3.x;
            boolean flag1 = movement.y != vec3.y;
            boolean flag2 = movement.z != vec3.z;
            boolean flag3 = this.m_20096_() || flag1 && movement.y < 0.0;
            if (this.getStepHeight() > 0.0F && flag3 && (flag || flag2)) {
                Vec3 vec31 = m_198894_(this, new Vec3(movement.x, (double) this.getStepHeight(), movement.z), aabb, this.m_9236_(), list);
                Vec3 vec32 = m_198894_(this, new Vec3(0.0, (double) this.getStepHeight(), 0.0), aabb.expandTowards(movement.x, 0.0, movement.z), this.m_9236_(), list);
                if (vec32.y < (double) this.getStepHeight()) {
                    Vec3 vec33 = m_198894_(this, new Vec3(movement.x, 0.0, movement.z), aabb.move(vec32), this.m_9236_(), list).add(vec32);
                    if (vec33.horizontalDistanceSqr() > vec31.horizontalDistanceSqr()) {
                        vec31 = vec33;
                    }
                }
                if (vec31.horizontalDistanceSqr() > vec3.horizontalDistanceSqr()) {
                    return vec31.add(m_198894_(this, new Vec3(0.0, -vec31.y + movement.y, 0.0), aabb.move(vec31), this.m_9236_(), list));
                }
            }
            return vec3;
        } else {
            return super.m_20272_(movement);
        }
    }

    @Override
    public float getXRot() {
        return this.getSquidPitch();
    }

    public boolean isMultipartEntity() {
        return true;
    }

    public PartEntity<?>[] getParts() {
        return this.allParts;
    }

    public boolean attackEntityPartFrom(EntityGiantSquidPart part, DamageSource source, float amount) {
        return this.hurt(source, amount);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.IN_WALL) || super.m_6673_(source);
    }

    public void directPitch(double d0, double d1, double d2, double d3) {
        boolean shift = this.f_19804_.get(OVERRIDE_BODYROT);
        float add = shift ? 90.0F : -90.0F;
        float f = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) + add;
        this.m_146922_(this.rotlerp(this.m_146908_(), f, shift ? 10.0F : 5.0F));
    }

    @Override
    public float getViewXRot(float partialTick) {
        return this.prevSquidPitch + (this.getSquidPitch() - this.prevSquidPitch) * partialTick;
    }

    @Override
    public float getViewYRot(float partialTick) {
        return partialTick == 1.0F ? this.f_20883_ : Mth.lerp(partialTick, this.f_20884_, this.f_20883_);
    }

    @Override
    protected float rotlerp(float in, float target, float maxShift) {
        float f = Mth.wrapDegrees(target - in);
        if (f > maxShift) {
            f = maxShift;
        }
        if (f < -maxShift) {
            f = -maxShift;
        }
        float f1 = in + f;
        if (f1 < 0.0F) {
            f1 += 360.0F;
        } else if (f1 > 360.0F) {
            f1 -= 360.0F;
        }
        return f1;
    }

    private float getDepressureLevel() {
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
        int waterLevelAbove;
        for (waterLevelAbove = 0; waterLevelAbove < 10; waterLevelAbove++) {
            BlockState blockstate = this.m_9236_().getBlockState(blockpos$mutable.set(this.m_20185_(), this.m_20186_() + (double) waterLevelAbove, this.m_20189_()));
            if (!blockstate.m_60819_().is(FluidTags.WATER) && !blockstate.m_280296_()) {
                break;
            }
        }
        return 1.0F - (float) waterLevelAbove / 10.0F;
    }

    private boolean canFitAt(BlockPos pos) {
        return true;
    }

    public boolean tickCaptured(EntityCachalotWhale whale) {
        this.resetCapturedStateIn = 25;
        if (this.f_19796_.nextInt(13) == 0) {
            this.spawnInk();
            whale.m_6469_(this.m_269291_().mobAttack(this), (float) (4 + this.f_19796_.nextInt(4)));
            if (this.f_19796_.nextFloat() <= 0.3F) {
                this.setCaptured(false);
                if (this.f_19796_.nextFloat() < 0.2F) {
                    this.m_19998_(AMItemRegistry.LOST_TENTACLE.get());
                }
                return true;
            }
        }
        this.setCaptured(true);
        this.setSquidPitch(0.0F);
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte id) {
        super.m_7822_(id);
    }

    @Override
    public boolean hurt(DamageSource src, float f) {
        if (super.m_6469_(src, f) && this.m_21188_() != null && !this.isCaptured() && this.f_19796_.nextBoolean()) {
            this.spawnInk();
            return true;
        } else {
            return false;
        }
    }

    private void spawnInk() {
        this.m_146850_(GameEvent.ENTITY_INTERACT);
        this.m_5496_(SoundEvents.SQUID_SQUIRT, this.m_6121_(), 0.5F * this.m_6100_());
        if (!this.m_9236_().isClientSide) {
            Vec3 inkDirection = new Vec3(0.0, 0.0, 1.2F).xRot(-this.getXRot() * (float) (Math.PI / 180.0)).yRot(-this.f_20883_ * (float) (Math.PI / 180.0));
            Vec3 vec3 = this.m_20182_().add(inkDirection);
            for (int i = 0; i < 30; i++) {
                Vec3 vec32 = inkDirection.add((double) (this.f_19796_.nextFloat() - 0.5F), (double) (this.f_19796_.nextFloat() - 0.5F), (double) (this.f_19796_.nextFloat() - 0.5F)).scale(0.8 + (double) (this.f_19796_.nextFloat() * 2.0F));
                ((ServerLevel) this.m_9236_()).sendParticles(ParticleTypes.SQUID_INK, vec3.x, vec3.y + 0.5, vec3.z, 0, vec32.x, vec32.y, vec32.z, 0.1F);
            }
        }
    }

    private class AIAvoidWhales extends Goal {

        private EntityCachalotWhale whale;

        private Vec3 moveTo;

        private int runDelay;

        public AIAvoidWhales() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (EntityGiantSquid.this.m_20072_() && !EntityGiantSquid.this.f_19862_ && !EntityGiantSquid.this.isCaptured() && this.runDelay-- <= 0) {
                EntityCachalotWhale closest = null;
                float dist = 50.0F;
                for (EntityCachalotWhale dude : EntityGiantSquid.this.m_9236_().m_45976_(EntityCachalotWhale.class, EntityGiantSquid.this.m_20191_().inflate((double) dist))) {
                    if (closest == null || dude.m_20270_(EntityGiantSquid.this) < closest.m_20270_(EntityGiantSquid.this)) {
                        closest = dude;
                    }
                }
                if (closest != null) {
                    this.whale = closest;
                    return true;
                }
                this.runDelay = 50 + EntityGiantSquid.this.f_19796_.nextInt(50);
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return this.whale != null && this.whale.m_6084_() && !EntityGiantSquid.this.f_19862_ && EntityGiantSquid.this.m_20270_(this.whale) < 60.0F;
        }

        @Override
        public void tick() {
            if (this.whale != null && this.whale.m_6084_()) {
                double dist = (double) EntityGiantSquid.this.m_20270_(this.whale);
                Vec3 vec = EntityGiantSquid.this.m_20182_().subtract(this.whale.m_20182_()).normalize();
                Vec3 vec2 = EntityGiantSquid.this.m_20182_().add(vec.scale((double) (12 + EntityGiantSquid.this.f_19796_.nextInt(5))));
                EntityGiantSquid.this.m_21573_().moveTo(vec2.x, vec2.y, vec2.z, dist < 20.0 ? 1.9F : 1.3F);
            }
        }

        @Override
        public void stop() {
            this.whale = null;
            this.moveTo = null;
        }
    }

    private class AIDeepwaterSwimming extends Goal {

        private BlockPos moveTo;

        public AIDeepwaterSwimming() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (!EntityGiantSquid.this.m_20160_() && (EntityGiantSquid.this.m_5448_() == null || EntityGiantSquid.this.isGrabbing()) && (EntityGiantSquid.this.m_20069_() || EntityGiantSquid.this.m_20077_())) {
                if (EntityGiantSquid.this.m_21573_().isDone() || EntityGiantSquid.this.m_217043_().nextInt(30) == 0) {
                    BlockPos found = this.findTargetPos();
                    if (found != null) {
                        this.moveTo = found;
                        return true;
                    }
                }
                return false;
            } else {
                return false;
            }
        }

        private BlockPos findTargetPos() {
            RandomSource r = EntityGiantSquid.this.m_217043_();
            for (int i = 0; i < 15; i++) {
                BlockPos pos = EntityGiantSquid.this.m_20183_().offset(r.nextInt(16) - 8, r.nextInt(32) - 16, r.nextInt(16) - 8);
                if (EntityGiantSquid.this.m_9236_().m_46801_(pos) && EntityGiantSquid.this.canFitAt(pos)) {
                    return this.getDeeperTarget(pos);
                }
            }
            return null;
        }

        private BlockPos getDeeperTarget(BlockPos waterAtPos) {
            BlockPos surface = new BlockPos(waterAtPos);
            BlockPos seafloor = new BlockPos(waterAtPos);
            while (EntityGiantSquid.this.m_9236_().m_46801_(surface) && surface.m_123342_() < 320) {
                surface = surface.above();
            }
            while (EntityGiantSquid.this.m_9236_().m_46801_(seafloor) && seafloor.m_123342_() > -64) {
                seafloor = seafloor.below();
            }
            int distance = surface.m_123342_() - seafloor.m_123342_();
            if (distance < 10) {
                return waterAtPos;
            } else {
                int i = (int) ((double) distance * 0.4);
                return seafloor.above(1 + EntityGiantSquid.this.m_217043_().nextInt(i));
            }
        }

        @Override
        public void start() {
            EntityGiantSquid.this.m_21573_().moveTo((double) ((float) this.moveTo.m_123341_() + 0.5F), (double) ((float) this.moveTo.m_123342_() + 0.5F), (double) ((float) this.moveTo.m_123343_() + 0.5F), 1.0);
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }
    }

    private class AIMelee extends Goal {

        @Override
        public boolean canUse() {
            return EntityGiantSquid.this.m_20072_() && EntityGiantSquid.this.m_5448_() != null && EntityGiantSquid.this.m_5448_().isAlive();
        }

        @Override
        public void tick() {
            EntityGiantSquid squid = EntityGiantSquid.this;
            LivingEntity target = EntityGiantSquid.this.m_5448_();
            double dist = (double) squid.m_20270_(target);
            if (squid.m_142582_(target) && dist < 7.0) {
                squid.setGrabbing(true);
            } else {
                Vec3 moveBodyTo = target.m_20182_();
                squid.m_21573_().moveTo(moveBodyTo.x, moveBodyTo.y, moveBodyTo.z, 1.0);
            }
            if (dist < 14.0) {
                squid.f_19804_.set(EntityGiantSquid.OVERRIDE_BODYROT, true);
            } else {
                squid.f_19804_.set(EntityGiantSquid.OVERRIDE_BODYROT, false);
            }
        }

        @Override
        public void stop() {
            EntityGiantSquid.this.f_19804_.set(EntityGiantSquid.OVERRIDE_BODYROT, false);
            EntityGiantSquid.this.setGrabbing(false);
        }
    }
}