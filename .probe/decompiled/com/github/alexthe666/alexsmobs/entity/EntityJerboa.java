package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWanderRanged;
import com.github.alexthe666.alexsmobs.entity.ai.JerboaAIBeg;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;

public class EntityJerboa extends Animal {

    private static final EntityDataAccessor<Boolean> JUMP_ACTIVE = SynchedEntityData.defineId(EntityJerboa.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> BEGGING = SynchedEntityData.defineId(EntityJerboa.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(EntityJerboa.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> BEFRIENDED = SynchedEntityData.defineId(EntityJerboa.class, EntityDataSerializers.BOOLEAN);

    public float jumpProgress;

    public float prevJumpProgress;

    public float reboundProgress;

    public float prevReboundProgress;

    public float begProgress;

    public float prevBegProgress;

    public float sleepProgress;

    public float prevSleepProgress;

    private int jumpTicks;

    private int jumpDuration;

    private boolean wasOnGround;

    private int currentMoveTypeDuration;

    protected EntityJerboa(EntityType<? extends Animal> jerboa, Level lvl) {
        super(jerboa, lvl);
        this.f_21342_ = new EntityJerboa.MoveHelperController(this);
        this.f_21343_ = new EntityJerboa.JumpHelperController(this);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 4.0).add(Attributes.MOVEMENT_SPEED, 0.45F);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(JUMP_ACTIVE, false);
        this.f_19804_.define(BEGGING, false);
        this.f_19804_.define(SLEEPING, false);
        this.f_19804_.define(BEFRIENDED, false);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new JerboaAIBeg(this, 1.0));
        this.f_21345_.addGoal(2, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(3, new AvoidEntityGoal(this, Player.class, 5.0F, 1.3, 1.0) {

            @Override
            public boolean canUse() {
                return !EntityJerboa.this.isBefriended() && super.canUse();
            }
        });
        this.f_21345_.addGoal(4, new AvoidEntityGoal(this, Cat.class, 9.0F, 1.3, 1.0));
        this.f_21345_.addGoal(5, new AvoidEntityGoal(this, Ocelot.class, 9.0F, 1.3, 1.0));
        this.f_21345_.addGoal(6, new AvoidEntityGoal(this, EntityRattlesnake.class, 9.0F, 1.3, 1.0));
        this.f_21345_.addGoal(7, new PanicGoal(this, 1.1));
        this.f_21345_.addGoal(8, new AnimalAIWanderRanged(this, 20, 1.0, 10, 7));
        this.f_21345_.addGoal(9, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.f_21345_.addGoal(10, new RandomLookAroundGoal(this));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setBefriended(compound.getBoolean("Befriended"));
        this.setSleeping(compound.getBoolean("Sleeping"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Befriended", this.isBefriended());
        compound.putBoolean("Sleeping", this.isSleeping());
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.requiresCustomPersistence();
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.m_8023_() || this.isBefriended();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.JERBOA_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.JERBOA_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.JERBOA_HURT.get();
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevJumpProgress = this.jumpProgress;
        this.prevReboundProgress = this.reboundProgress;
        this.prevSleepProgress = this.sleepProgress;
        this.prevBegProgress = this.begProgress;
        if (!this.m_9236_().isClientSide) {
            this.f_19804_.set(JUMP_ACTIVE, !this.m_20096_());
        }
        if (this.f_19804_.get(JUMP_ACTIVE)) {
            if (this.jumpProgress < 5.0F) {
                this.jumpProgress++;
                if (this.reboundProgress > 0.0F) {
                    this.reboundProgress--;
                }
            }
            if (this.jumpProgress >= 5.0F && this.reboundProgress < 5.0F) {
                this.reboundProgress++;
            }
        } else {
            if (this.reboundProgress > 0.0F) {
                this.reboundProgress = Math.max(this.reboundProgress - 1.0F, 0.0F);
            }
            if (this.jumpProgress > 0.0F) {
                this.jumpProgress = Math.max(this.jumpProgress - 1.0F, 0.0F);
            }
        }
        if (this.isBegging()) {
            if (this.begProgress < 5.0F) {
                this.begProgress++;
            }
        } else if (this.begProgress > 0.0F) {
            this.begProgress--;
        }
        if (this.isSleeping()) {
            if (this.sleepProgress < 5.0F) {
                this.sleepProgress++;
            }
        } else if (this.sleepProgress > 0.0F) {
            this.sleepProgress--;
        }
        if (!this.m_9236_().isClientSide) {
            if (this.m_9236_().isDay() && this.m_21188_() == null && !this.isBegging()) {
                if (this.f_19797_ % 10 == 0 && this.m_217043_().nextInt(750) == 0) {
                    this.setSleeping(true);
                }
            } else if (this.isSleeping()) {
                this.setSleeping(false);
            }
        }
    }

    public boolean isBegging() {
        return this.f_19804_.get(BEGGING);
    }

    public void setBegging(boolean begging) {
        this.f_19804_.set(BEGGING, begging);
    }

    @Override
    public boolean isSleeping() {
        return this.f_19804_.get(SLEEPING);
    }

    public void setSleeping(boolean sleeping) {
        this.f_19804_.set(SLEEPING, sleeping);
    }

    public boolean isBefriended() {
        return this.f_19804_.get(BEFRIENDED);
    }

    public void setBefriended(boolean befriended) {
        this.f_19804_.set(BEFRIENDED, befriended);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.getItem();
        if (!itemstack.is(Tags.Items.SEEDS) && !this.isFood(itemstack) || !(this.m_21223_() < this.m_21233_()) && this.isBefriended()) {
            InteractionResult type = super.mobInteract(player, hand);
            if (type != InteractionResult.SUCCESS && !this.isFood(itemstack) && itemstack.is(Tags.Items.SEEDS)) {
                this.setSleeping(false);
                this.m_146850_(GameEvent.ENTITY_INTERACT);
                this.m_5496_(SoundEvents.PARROT_EAT, this.m_6100_(), this.m_6121_());
                for (int i = 0; i < 6 + this.f_19796_.nextInt(3); i++) {
                    double d2 = this.f_19796_.nextGaussian() * 0.02;
                    double d0 = this.f_19796_.nextGaussian() * 0.02;
                    double d1 = this.f_19796_.nextGaussian() * 0.02;
                    this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, itemstack), this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_()) - (double) this.m_20205_() * 0.5, this.m_20186_() + (double) (this.m_20206_() * 0.5F) + (double) (this.f_19796_.nextFloat() * this.m_20206_() * 0.5F), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_()) - (double) this.m_20205_() * 0.5, d0, d1, d2);
                }
                if (this.f_19796_.nextFloat() <= 0.3F) {
                    player.m_7292_(new MobEffectInstance(AMEffectRegistry.FLEET_FOOTED.get(), 12000));
                }
                return InteractionResult.SUCCESS;
            } else {
                return type;
            }
        } else {
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            this.setBefriended(true);
            this.m_5634_(4.0F);
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean prev = super.hurt(source, amount);
        if (prev) {
            this.setSleeping(false);
            if (source.getEntity() != null && source.getEntity() instanceof LivingEntity) {
                LivingEntity hurter = (LivingEntity) source.getEntity();
                if (hurter.hasEffect(AMEffectRegistry.FLEET_FOOTED.get())) {
                    hurter.removeEffect(AMEffectRegistry.FLEET_FOOTED.get());
                }
            }
            return prev;
        } else {
            return prev;
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(AMTagRegistry.INSECT_ITEMS);
    }

    public boolean shouldMove() {
        return !this.isSleeping();
    }

    public float getJumpCompletion(float partialTicks) {
        return this.jumpDuration == 0 ? 0.0F : ((float) this.jumpTicks + partialTicks) / (float) this.jumpDuration;
    }

    @Override
    protected float getJumpPower() {
        return this.f_19862_ ? super.m_6118_() + 0.2F : 0.25F + this.f_19796_.nextFloat() * 0.15F;
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public static boolean isValidLightLevel(ServerLevelAccessor p_223323_0_, BlockPos p_223323_1_, RandomSource p_223323_2_) {
        int light = p_223323_0_.m_46803_(p_223323_1_);
        return light <= 4;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.jerboaSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static boolean canMonsterSpawnInLight(EntityType<? extends EntityJerboa> p_223325_0_, ServerLevelAccessor p_223325_1_, MobSpawnType p_223325_2_, BlockPos p_223325_3_, RandomSource p_223325_4_) {
        return isValidLightLevel(p_223325_1_, p_223325_3_, p_223325_4_) && m_217057_(p_223325_0_, p_223325_1_, p_223325_2_, p_223325_3_, p_223325_4_);
    }

    public static <T extends Mob> boolean canJerboaSpawn(EntityType<EntityJerboa> entityType, ServerLevelAccessor iServerWorld, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return reason == MobSpawnType.SPAWNER || iServerWorld.m_45527_(pos.above()) && canMonsterSpawnInLight(entityType, iServerWorld, reason, pos, random);
    }

    @Override
    protected void jumpFromGround() {
        super.m_6135_();
        double d0 = this.f_21342_.getSpeedModifier();
        if (d0 > 0.0) {
            double d1 = this.m_20184_().horizontalDistance();
            if (d1 < 0.01) {
            }
        }
        if (!this.m_9236_().isClientSide) {
            this.m_9236_().broadcastEntityEvent(this, (byte) 1);
        }
    }

    public void setMovementSpeed(double newSpeed) {
        this.m_21573_().setSpeedModifier(newSpeed);
        this.f_21342_.setWantedPosition(this.f_21342_.getWantedX(), this.f_21342_.getWantedY(), this.f_21342_.getWantedZ(), newSpeed);
    }

    public void startJumping() {
        this.m_6862_(true);
        this.jumpDuration = 10;
        this.jumpTicks = 0;
    }

    private void checkLandingDelay() {
        this.updateMoveTypeDuration();
        this.disableJumpControl();
    }

    private void calculateRotationYaw(double x, double z) {
        this.m_146922_((float) (Mth.atan2(z - this.m_20189_(), x - this.m_20185_()) * 180.0F / (float) Math.PI) - 90.0F);
    }

    private void enableJumpControl() {
        if (this.f_21343_ instanceof EntityJerboa.JumpHelperController) {
            ((EntityJerboa.JumpHelperController) this.f_21343_).setCanJump(true);
        }
    }

    private void disableJumpControl() {
        if (this.f_21343_ instanceof EntityJerboa.JumpHelperController) {
            ((EntityJerboa.JumpHelperController) this.f_21343_).setCanJump(false);
        }
    }

    private void updateMoveTypeDuration() {
        if (this.f_21342_.getSpeedModifier() < 2.2) {
            this.currentMoveTypeDuration = 2;
        } else {
            this.currentMoveTypeDuration = 1;
        }
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        if (this.currentMoveTypeDuration > 0) {
            this.currentMoveTypeDuration--;
        }
        if (this.m_20096_() && this.shouldMove()) {
            if (!this.wasOnGround) {
                this.m_6862_(false);
                this.checkLandingDelay();
            }
            if (this.currentMoveTypeDuration == 0) {
                LivingEntity livingentity = this.m_5448_();
                if (livingentity != null && this.m_20280_(livingentity) < 16.0) {
                    this.calculateRotationYaw(livingentity.m_20185_(), livingentity.m_20189_());
                    this.f_21342_.setWantedPosition(livingentity.m_20185_(), livingentity.m_20186_(), livingentity.m_20189_(), this.f_21342_.getSpeedModifier());
                    this.startJumping();
                    this.wasOnGround = true;
                }
            }
            if (this.f_21343_ instanceof EntityJerboa.JumpHelperController rabbitController) {
                if (!rabbitController.getIsJumping()) {
                    if (this.f_21342_.hasWanted() && this.currentMoveTypeDuration == 0) {
                        Path path = this.f_21344_.getPath();
                        Vec3 vector3d = new Vec3(this.f_21342_.getWantedX(), this.f_21342_.getWantedY(), this.f_21342_.getWantedZ());
                        if (path != null && !path.isDone()) {
                            vector3d = path.getNextEntityPos(this);
                        }
                        this.calculateRotationYaw(vector3d.x, vector3d.z);
                        this.startJumping();
                    }
                } else if (!rabbitController.canJump()) {
                    this.enableJumpControl();
                }
            }
        } else if (!this.shouldMove()) {
            this.m_6862_(false);
            this.checkLandingDelay();
        }
        this.wasOnGround = this.m_20096_();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.jumpTicks != this.jumpDuration) {
            this.jumpTicks++;
        } else if (this.jumpDuration != 0) {
            this.jumpTicks = 0;
            this.jumpDuration = 0;
            this.m_6862_(false);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 1) {
            this.m_20076_();
            this.jumpDuration = 10;
            this.jumpTicks = 0;
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel0, AgeableMob ageableMob1) {
        EntityJerboa boa = AMEntityRegistry.JERBOA.get().create(serverLevel0);
        boa.setBefriended(true);
        return boa;
    }

    public boolean hasJumper() {
        return this.f_21343_ instanceof EntityJerboa.JumpHelperController;
    }

    public static class JumpHelperController extends JumpControl {

        private final EntityJerboa jerboa;

        private boolean canJump;

        public JumpHelperController(EntityJerboa jerboa) {
            super(jerboa);
            this.jerboa = jerboa;
        }

        public boolean getIsJumping() {
            return this.f_24897_;
        }

        public boolean canJump() {
            return this.canJump;
        }

        public void setCanJump(boolean canJumpIn) {
            this.canJump = canJumpIn;
        }

        @Override
        public void tick() {
            if (this.f_24897_) {
                this.jerboa.startJumping();
                this.f_24897_ = false;
            }
        }
    }

    static class MoveHelperController extends MoveControl {

        private final EntityJerboa jerboa;

        private double nextJumpSpeed;

        public MoveHelperController(EntityJerboa jerboa) {
            super(jerboa);
            this.jerboa = jerboa;
        }

        @Override
        public void tick() {
            if (this.jerboa.hasJumper() && this.jerboa.m_20096_() && !this.jerboa.f_20899_ && !((EntityJerboa.JumpHelperController) this.jerboa.f_21343_).getIsJumping()) {
                this.jerboa.setMovementSpeed(0.0);
            } else if (this.m_24995_()) {
                this.jerboa.setMovementSpeed(this.nextJumpSpeed);
            }
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                this.f_24981_ = MoveControl.Operation.WAIT;
                Vec3 vector3d = new Vec3(this.f_24975_ - this.jerboa.m_20185_(), this.f_24976_ - this.jerboa.m_20186_(), this.f_24977_ - this.jerboa.m_20189_());
                double d0 = vector3d.length();
                this.jerboa.m_20256_(this.jerboa.m_20184_().add(vector3d.scale(this.f_24978_ * 1.0 * 0.05 / d0)));
            }
            super.tick();
        }

        @Override
        public void setWantedPosition(double x, double y, double z, double speedIn) {
            if (this.jerboa.m_20069_()) {
                speedIn = 1.5;
            }
            super.setWantedPosition(x, y, z, speedIn);
            if (speedIn > 0.0) {
                this.nextJumpSpeed = speedIn;
            }
        }
    }
}