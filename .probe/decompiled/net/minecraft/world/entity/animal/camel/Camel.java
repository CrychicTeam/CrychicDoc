package net.minecraft.world.entity.animal.camel;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.serialization.Dynamic;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.RiderShieldingMount;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class Camel extends AbstractHorse implements PlayerRideableJumping, RiderShieldingMount, Saddleable {

    public static final Ingredient TEMPTATION_ITEM = Ingredient.of(Items.CACTUS);

    public static final int DASH_COOLDOWN_TICKS = 55;

    public static final int MAX_HEAD_Y_ROT = 30;

    private static final float RUNNING_SPEED_BONUS = 0.1F;

    private static final float DASH_VERTICAL_MOMENTUM = 1.4285F;

    private static final float DASH_HORIZONTAL_MOMENTUM = 22.2222F;

    private static final int DASH_MINIMUM_DURATION_TICKS = 5;

    private static final int SITDOWN_DURATION_TICKS = 40;

    private static final int STANDUP_DURATION_TICKS = 52;

    private static final int IDLE_MINIMAL_DURATION_TICKS = 80;

    private static final float SITTING_HEIGHT_DIFFERENCE = 1.43F;

    public static final EntityDataAccessor<Boolean> DASH = SynchedEntityData.defineId(Camel.class, EntityDataSerializers.BOOLEAN);

    public static final EntityDataAccessor<Long> LAST_POSE_CHANGE_TICK = SynchedEntityData.defineId(Camel.class, EntityDataSerializers.LONG);

    public final AnimationState sitAnimationState = new AnimationState();

    public final AnimationState sitPoseAnimationState = new AnimationState();

    public final AnimationState sitUpAnimationState = new AnimationState();

    public final AnimationState idleAnimationState = new AnimationState();

    public final AnimationState dashAnimationState = new AnimationState();

    private static final EntityDimensions SITTING_DIMENSIONS = EntityDimensions.scalable(EntityType.CAMEL.getWidth(), EntityType.CAMEL.getHeight() - 1.43F);

    private int dashCooldown = 0;

    private int idleAnimationTimeout = 0;

    public Camel(EntityType<? extends Camel> entityTypeExtendsCamel0, Level level1) {
        super(entityTypeExtendsCamel0, level1);
        this.m_274367_(1.5F);
        this.f_21342_ = new Camel.CamelMoveControl();
        GroundPathNavigation $$2 = (GroundPathNavigation) this.m_21573_();
        $$2.m_7008_(true);
        $$2.setCanWalkOverFences(true);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putLong("LastPoseTick", this.f_19804_.get(LAST_POSE_CHANGE_TICK));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        long $$1 = compoundTag0.getLong("LastPoseTick");
        if ($$1 < 0L) {
            this.m_20124_(Pose.SITTING);
        }
        this.resetLastPoseChangeTick($$1);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return m_30627_().add(Attributes.MAX_HEALTH, 32.0).add(Attributes.MOVEMENT_SPEED, 0.09F).add(Attributes.JUMP_STRENGTH, 0.42F);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(DASH, false);
        this.f_19804_.define(LAST_POSE_CHANGE_TICK, 0L);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        CamelAi.initMemories(this, serverLevelAccessor0.m_213780_());
        this.resetLastPoseChangeTickToFullStand(serverLevelAccessor0.getLevel().m_46467_());
        return super.finalizeSpawn(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
    }

    @Override
    protected Brain.Provider<Camel> brainProvider() {
        return CamelAi.brainProvider();
    }

    @Override
    protected void registerGoals() {
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic0) {
        return CamelAi.makeBrain(this.brainProvider().makeBrain(dynamic0));
    }

    @Override
    public EntityDimensions getDimensions(Pose pose0) {
        return pose0 == Pose.SITTING ? SITTING_DIMENSIONS.scale(this.m_6134_()) : super.m_6972_(pose0);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return entityDimensions1.height - 0.1F;
    }

    @Override
    public double getRiderShieldingHeight() {
        return 0.5;
    }

    @Override
    protected void customServerAiStep() {
        this.m_9236_().getProfiler().push("camelBrain");
        Brain<?> $$0 = this.m_6274_();
        ((Brain<Camel>) $$0).tick((ServerLevel) this.m_9236_(), this);
        this.m_9236_().getProfiler().pop();
        this.m_9236_().getProfiler().push("camelActivityUpdate");
        CamelAi.updateActivity(this);
        this.m_9236_().getProfiler().pop();
        super.m_8024_();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isDashing() && this.dashCooldown < 50 && (this.m_20096_() || this.m_20069_() || this.m_20159_())) {
            this.setDashing(false);
        }
        if (this.dashCooldown > 0) {
            this.dashCooldown--;
            if (this.dashCooldown == 0) {
                this.m_9236_().playSound(null, this.m_20183_(), SoundEvents.CAMEL_DASH_READY, SoundSource.NEUTRAL, 1.0F, 1.0F);
            }
        }
        if (this.m_9236_().isClientSide()) {
            this.setupAnimationStates();
        }
        if (this.refuseToMove()) {
            this.clampHeadRotationToBody(this, 30.0F);
        }
        if (this.isCamelSitting() && this.m_20069_()) {
            this.standUpInstantly();
        }
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.f_19796_.nextInt(40) + 80;
            this.idleAnimationState.start(this.f_19797_);
        } else {
            this.idleAnimationTimeout--;
        }
        if (this.isCamelVisuallySitting()) {
            this.sitUpAnimationState.stop();
            this.dashAnimationState.stop();
            if (this.isVisuallySittingDown()) {
                this.sitAnimationState.startIfStopped(this.f_19797_);
                this.sitPoseAnimationState.stop();
            } else {
                this.sitAnimationState.stop();
                this.sitPoseAnimationState.startIfStopped(this.f_19797_);
            }
        } else {
            this.sitAnimationState.stop();
            this.sitPoseAnimationState.stop();
            this.dashAnimationState.animateWhen(this.isDashing(), this.f_19797_);
            this.sitUpAnimationState.animateWhen(this.isInPoseTransition() && this.getPoseTime() >= 0L, this.f_19797_);
        }
    }

    @Override
    protected void updateWalkAnimation(float float0) {
        float $$1;
        if (this.m_20089_() == Pose.STANDING && !this.dashAnimationState.isStarted()) {
            $$1 = Math.min(float0 * 6.0F, 1.0F);
        } else {
            $$1 = 0.0F;
        }
        this.f_267362_.update($$1, 0.2F);
    }

    @Override
    public void travel(Vec3 vec0) {
        if (this.refuseToMove() && this.m_20096_()) {
            this.m_20256_(this.m_20184_().multiply(0.0, 1.0, 0.0));
            vec0 = vec0.multiply(0.0, 1.0, 0.0);
        }
        super.m_7023_(vec0);
    }

    @Override
    protected void tickRidden(Player player0, Vec3 vec1) {
        super.tickRidden(player0, vec1);
        if (player0.f_20902_ > 0.0F && this.isCamelSitting() && !this.isInPoseTransition()) {
            this.standUp();
        }
    }

    public boolean refuseToMove() {
        return this.isCamelSitting() || this.isInPoseTransition();
    }

    @Override
    protected float getRiddenSpeed(Player player0) {
        float $$1 = player0.m_20142_() && this.getJumpCooldown() == 0 ? 0.1F : 0.0F;
        return (float) this.m_21133_(Attributes.MOVEMENT_SPEED) + $$1;
    }

    @Override
    protected Vec2 getRiddenRotation(LivingEntity livingEntity0) {
        return this.refuseToMove() ? new Vec2(this.m_146909_(), this.m_146908_()) : super.getRiddenRotation(livingEntity0);
    }

    @Override
    protected Vec3 getRiddenInput(Player player0, Vec3 vec1) {
        return this.refuseToMove() ? Vec3.ZERO : super.getRiddenInput(player0, vec1);
    }

    @Override
    public boolean canJump() {
        return !this.refuseToMove() && super.canJump();
    }

    @Override
    public void onPlayerJump(int int0) {
        if (this.m_6254_() && this.dashCooldown <= 0 && this.m_20096_()) {
            super.onPlayerJump(int0);
        }
    }

    @Override
    public boolean canSprint() {
        return true;
    }

    @Override
    protected void executeRidersJump(float float0, Vec3 vec1) {
        double $$2 = this.m_21133_(Attributes.JUMP_STRENGTH) * (double) this.m_20098_() + (double) this.m_285755_();
        this.m_246865_(this.m_20154_().multiply(1.0, 0.0, 1.0).normalize().scale((double) (22.2222F * float0) * this.m_21133_(Attributes.MOVEMENT_SPEED) * (double) this.m_6041_()).add(0.0, (double) (1.4285F * float0) * $$2, 0.0));
        this.dashCooldown = 55;
        this.setDashing(true);
        this.f_19812_ = true;
    }

    public boolean isDashing() {
        return this.f_19804_.get(DASH);
    }

    public void setDashing(boolean boolean0) {
        this.f_19804_.set(DASH, boolean0);
    }

    public boolean isPanicking() {
        return this.m_6274_().checkMemory(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_PRESENT);
    }

    @Override
    public void handleStartJump(int int0) {
        this.m_5496_(SoundEvents.CAMEL_DASH, 1.0F, 1.0F);
        this.setDashing(true);
    }

    @Override
    public void handleStopJump() {
    }

    @Override
    public int getJumpCooldown() {
        return this.dashCooldown;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.CAMEL_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CAMEL_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.CAMEL_HURT;
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
        if (blockState1.m_60827_() == SoundType.SAND) {
            this.m_5496_(SoundEvents.CAMEL_STEP_SAND, 1.0F, 1.0F);
        } else {
            this.m_5496_(SoundEvents.CAMEL_STEP, 1.0F, 1.0F);
        }
    }

    @Override
    public boolean isFood(ItemStack itemStack0) {
        return TEMPTATION_ITEM.test(itemStack0);
    }

    @Override
    public InteractionResult mobInteract(Player player0, InteractionHand interactionHand1) {
        ItemStack $$2 = player0.m_21120_(interactionHand1);
        if (player0.isSecondaryUseActive() && !this.m_6162_()) {
            this.openCustomInventoryScreen(player0);
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else {
            InteractionResult $$3 = $$2.interactLivingEntity(player0, this, interactionHand1);
            if ($$3.consumesAction()) {
                return $$3;
            } else if (this.isFood($$2)) {
                return this.m_30580_(player0, $$2);
            } else {
                if (this.m_20197_().size() < 2 && !this.m_6162_()) {
                    this.m_6835_(player0);
                }
                return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
            }
        }
    }

    @Override
    protected void onLeashDistance(float float0) {
        if (float0 > 6.0F && this.isCamelSitting() && !this.isInPoseTransition()) {
            this.standUp();
        }
    }

    @Override
    protected boolean handleEating(Player player0, ItemStack itemStack1) {
        if (!this.isFood(itemStack1)) {
            return false;
        } else {
            boolean $$2 = this.m_21223_() < this.m_21233_();
            if ($$2) {
                this.m_5634_(2.0F);
            }
            boolean $$3 = this.isTamed() && this.m_146764_() == 0 && this.m_5957_();
            if ($$3) {
                this.m_27595_(player0);
            }
            boolean $$4 = this.m_6162_();
            if ($$4) {
                this.m_9236_().addParticle(ParticleTypes.HAPPY_VILLAGER, this.m_20208_(1.0), this.m_20187_() + 0.5, this.m_20262_(1.0), 0.0, 0.0, 0.0);
                if (!this.m_9236_().isClientSide) {
                    this.m_146758_(10);
                }
            }
            if (!$$2 && !$$3 && !$$4) {
                return false;
            } else {
                if (!this.m_20067_()) {
                    SoundEvent $$5 = this.getEatingSound();
                    if ($$5 != null) {
                        this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), $$5, this.m_5720_(), 1.0F, 1.0F + (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F);
                    }
                }
                return true;
            }
        }
    }

    @Override
    protected boolean canPerformRearing() {
        return false;
    }

    @Override
    public boolean canMate(Animal animal0) {
        if (animal0 != this && animal0 instanceof Camel $$1 && this.m_30628_() && $$1.m_30628_()) {
            return true;
        }
        return false;
    }

    @Nullable
    public Camel getBreedOffspring(ServerLevel serverLevel0, AgeableMob ageableMob1) {
        return EntityType.CAMEL.create(serverLevel0);
    }

    @Nullable
    @Override
    protected SoundEvent getEatingSound() {
        return SoundEvents.CAMEL_EAT;
    }

    @Override
    protected void actuallyHurt(DamageSource damageSource0, float float1) {
        this.standUpInstantly();
        super.m_6475_(damageSource0, float1);
    }

    @Override
    protected void positionRider(Entity entity0, Entity.MoveFunction entityMoveFunction1) {
        int $$2 = this.m_20197_().indexOf(entity0);
        if ($$2 >= 0) {
            boolean $$3 = $$2 == 0;
            float $$4 = 0.5F;
            float $$5 = (float) (this.m_213877_() ? 0.01F : this.getBodyAnchorAnimationYOffset($$3, 0.0F) + entity0.getMyRidingOffset());
            if (this.m_20197_().size() > 1) {
                if (!$$3) {
                    $$4 = -0.7F;
                }
                if (entity0 instanceof Animal) {
                    $$4 += 0.2F;
                }
            }
            Vec3 $$6 = new Vec3(0.0, 0.0, (double) $$4).yRot(-this.f_20883_ * (float) (Math.PI / 180.0));
            entityMoveFunction1.accept(entity0, this.m_20185_() + $$6.x, this.m_20186_() + (double) $$5, this.m_20189_() + $$6.z);
            this.clampRotation(entity0);
        }
    }

    private double getBodyAnchorAnimationYOffset(boolean boolean0, float float1) {
        double $$2 = this.getPassengersRidingOffset();
        float $$3 = this.m_6134_() * 1.43F;
        float $$4 = $$3 - this.m_6134_() * 0.2F;
        float $$5 = $$3 - $$4;
        boolean $$6 = this.isInPoseTransition();
        boolean $$7 = this.isCamelSitting();
        if ($$6) {
            int $$8 = $$7 ? 40 : 52;
            int $$9;
            float $$10;
            if ($$7) {
                $$9 = 28;
                $$10 = boolean0 ? 0.5F : 0.1F;
            } else {
                $$9 = boolean0 ? 24 : 32;
                $$10 = boolean0 ? 0.6F : 0.35F;
            }
            float $$13 = Mth.clamp((float) this.getPoseTime() + float1, 0.0F, (float) $$8);
            boolean $$14 = $$13 < (float) $$9;
            float $$15 = $$14 ? $$13 / (float) $$9 : ($$13 - (float) $$9) / (float) ($$8 - $$9);
            float $$16 = $$3 - $$10 * $$4;
            $$2 += $$7 ? (double) Mth.lerp($$15, $$14 ? $$3 : $$16, $$14 ? $$16 : $$5) : (double) Mth.lerp($$15, $$14 ? $$5 - $$3 : $$5 - $$16, $$14 ? $$5 - $$16 : 0.0F);
        }
        if ($$7 && !$$6) {
            $$2 += (double) $$5;
        }
        return $$2;
    }

    @Override
    public Vec3 getLeashOffset(float float0) {
        return new Vec3(0.0, this.getBodyAnchorAnimationYOffset(true, float0) - (double) (0.2F * this.m_6134_()), (double) (this.m_20205_() * 0.56F));
    }

    @Override
    public double getPassengersRidingOffset() {
        return (double) (this.getDimensions(this.isCamelSitting() ? Pose.SITTING : Pose.STANDING).height - (this.m_6162_() ? 0.35F : 0.6F));
    }

    @Override
    public void onPassengerTurned(Entity entity0) {
        if (this.getControllingPassenger() != entity0) {
            this.clampRotation(entity0);
        }
    }

    private void clampRotation(Entity entity0) {
        entity0.setYBodyRot(this.m_146908_());
        float $$1 = entity0.getYRot();
        float $$2 = Mth.wrapDegrees($$1 - this.m_146908_());
        float $$3 = Mth.clamp($$2, -160.0F, 160.0F);
        entity0.yRotO += $$3 - $$2;
        float $$4 = $$1 + $$3 - $$2;
        entity0.setYRot($$4);
        entity0.setYHeadRot($$4);
    }

    private void clampHeadRotationToBody(Entity entity0, float float1) {
        float $$2 = entity0.getYHeadRot();
        float $$3 = Mth.wrapDegrees(this.f_20883_ - $$2);
        float $$4 = Mth.clamp(Mth.wrapDegrees(this.f_20883_ - $$2), -float1, float1);
        float $$5 = $$2 + $$3 - $$4;
        entity0.setYHeadRot($$5);
    }

    @Override
    public int getMaxHeadYRot() {
        return 30;
    }

    @Override
    protected boolean canAddPassenger(Entity entity0) {
        return this.m_20197_().size() <= 2;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        if (!this.m_20197_().isEmpty() && this.m_6254_()) {
            Entity $$0 = (Entity) this.m_20197_().get(0);
            if ($$0 instanceof LivingEntity) {
                return (LivingEntity) $$0;
            }
        }
        return null;
    }

    @Override
    protected void sendDebugPackets() {
        super.m_8025_();
        DebugPackets.sendEntityBrain(this);
    }

    public boolean isCamelSitting() {
        return this.f_19804_.get(LAST_POSE_CHANGE_TICK) < 0L;
    }

    public boolean isCamelVisuallySitting() {
        return this.getPoseTime() < 0L != this.isCamelSitting();
    }

    public boolean isInPoseTransition() {
        long $$0 = this.getPoseTime();
        return $$0 < (long) (this.isCamelSitting() ? 40 : 52);
    }

    private boolean isVisuallySittingDown() {
        return this.isCamelSitting() && this.getPoseTime() < 40L && this.getPoseTime() >= 0L;
    }

    public void sitDown() {
        if (!this.isCamelSitting()) {
            this.m_5496_(SoundEvents.CAMEL_SIT, 1.0F, 1.0F);
            this.m_20124_(Pose.SITTING);
            this.resetLastPoseChangeTick(-this.m_9236_().getGameTime());
        }
    }

    public void standUp() {
        if (this.isCamelSitting()) {
            this.m_5496_(SoundEvents.CAMEL_STAND, 1.0F, 1.0F);
            this.m_20124_(Pose.STANDING);
            this.resetLastPoseChangeTick(this.m_9236_().getGameTime());
        }
    }

    public void standUpInstantly() {
        this.m_20124_(Pose.STANDING);
        this.resetLastPoseChangeTickToFullStand(this.m_9236_().getGameTime());
    }

    @VisibleForTesting
    public void resetLastPoseChangeTick(long long0) {
        this.f_19804_.set(LAST_POSE_CHANGE_TICK, long0);
    }

    private void resetLastPoseChangeTickToFullStand(long long0) {
        this.resetLastPoseChangeTick(Math.max(0L, long0 - 52L - 1L));
    }

    public long getPoseTime() {
        return this.m_9236_().getGameTime() - Math.abs(this.f_19804_.get(LAST_POSE_CHANGE_TICK));
    }

    @Override
    public SoundEvent getSaddleSoundEvent() {
        return SoundEvents.CAMEL_SADDLE;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor0) {
        if (!this.f_19803_ && DASH.equals(entityDataAccessor0)) {
            this.dashCooldown = this.dashCooldown == 0 ? 55 : this.dashCooldown;
        }
        super.m_7350_(entityDataAccessor0);
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new Camel.CamelBodyRotationControl(this);
    }

    @Override
    public boolean isTamed() {
        return true;
    }

    @Override
    public void openCustomInventoryScreen(Player player0) {
        if (!this.m_9236_().isClientSide) {
            player0.openHorseInventory(this, this.f_30520_);
        }
    }

    class CamelBodyRotationControl extends BodyRotationControl {

        public CamelBodyRotationControl(Camel camel0) {
            super(camel0);
        }

        @Override
        public void clientTick() {
            if (!Camel.this.refuseToMove()) {
                super.clientTick();
            }
        }
    }

    class CamelMoveControl extends MoveControl {

        public CamelMoveControl() {
            super(Camel.this);
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO && !Camel.this.m_21523_() && Camel.this.isCamelSitting() && !Camel.this.isInPoseTransition()) {
                Camel.this.standUp();
            }
            super.tick();
        }
    }
}