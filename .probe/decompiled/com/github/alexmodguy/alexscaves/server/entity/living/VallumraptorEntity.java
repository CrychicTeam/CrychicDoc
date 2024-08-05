package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.MultipleDinosaurEggsBlock;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalBreedEggsGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalFollowOwnerGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalJoinPackGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalLayEggGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalLootChestsGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalPackTargetGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.GroundPathNavigatorNoSpin;
import com.github.alexmodguy.alexscaves.server.entity.ai.MobTargetClosePlayers;
import com.github.alexmodguy.alexscaves.server.entity.ai.MobTargetItemGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.MobTargetUntamedGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.VallumraptorMeleeGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.VallumraptorOpenDoorGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.VallumraptorWanderGoal;
import com.github.alexmodguy.alexscaves.server.entity.util.ChestThief;
import com.github.alexmodguy.alexscaves.server.entity.util.PackAnimal;
import com.github.alexmodguy.alexscaves.server.entity.util.TargetsDroppedItems;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.server.entity.collision.ICustomCollisions;
import java.util.EnumSet;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class VallumraptorEntity extends DinosaurEntity implements IAnimatedEntity, ICustomCollisions, PackAnimal, ChestThief, TargetsDroppedItems {

    public static final Animation ANIMATION_CALL_1 = Animation.create(15);

    public static final Animation ANIMATION_CALL_2 = Animation.create(25);

    public static final Animation ANIMATION_SCRATCH_1 = Animation.create(20);

    public static final Animation ANIMATION_SCRATCH_2 = Animation.create(20);

    public static final Animation ANIMATION_SHAKE = Animation.create(40);

    public static final Animation ANIMATION_STARTLEAP = Animation.create(20);

    public static final Animation ANIMATION_MELEE_BITE = Animation.create(15);

    public static final Animation ANIMATION_MELEE_SLASH_1 = Animation.create(15);

    public static final Animation ANIMATION_MELEE_SLASH_2 = Animation.create(15);

    public static final Animation ANIMATION_GRAB = Animation.create(40);

    private static final EntityDataAccessor<Boolean> RUNNING = SynchedEntityData.defineId(VallumraptorEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> LEAPING = SynchedEntityData.defineId(VallumraptorEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> ELDER = SynchedEntityData.defineId(VallumraptorEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Float> PUZZLED_HEAD_ROT = SynchedEntityData.defineId(VallumraptorEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> RELAXED_FOR = SynchedEntityData.defineId(VallumraptorEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> HIDING_FOR = SynchedEntityData.defineId(VallumraptorEntity.class, EntityDataSerializers.INT);

    private static final Predicate<LivingEntity> VALLUMRAPTOR_TARGETS = living -> living.m_6095_().is(ACTagRegistry.VALLUMRAPTOR_TARGETS);

    private Animation currentAnimation;

    private int animationTick;

    private float leapProgress;

    private float prevLeapProgress;

    private float runProgress;

    private float prevRunProgress;

    private float prevPuzzleHeadRot;

    public float prevRelaxedProgress;

    public float relaxedProgress;

    private float hideProgress;

    private float prevHideProgress;

    private boolean hasRunningAttributes = false;

    private boolean hasElderAttributes = false;

    private float targetPuzzleRot;

    private VallumraptorEntity priorPackMember;

    private VallumraptorEntity afterPackMember;

    private boolean justLootedChest;

    private int fleeTicks = 0;

    private Vec3 fleeFromPosition;

    private float tailYaw = this.m_146908_();

    private float prevTailYaw = this.m_146908_();

    private int eatHeldItemIn;

    public VallumraptorEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        GroundPathNavigation navigation = new GroundPathNavigatorNoSpin(this, level);
        navigation.setCanOpenDoors(true);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(2, new AnimalFollowOwnerGoal(this, 1.0, 5.0F, 2.0F, false) {

            @Override
            public boolean shouldFollow() {
                return VallumraptorEntity.this.getCommand() == 2;
            }

            @Override
            public void tickDistance(float distanceTo) {
                VallumraptorEntity.this.setRunning(distanceTo > 5.0F);
            }
        });
        this.f_21345_.addGoal(3, new AnimalBreedEggsGoal(this, 1.0));
        this.f_21345_.addGoal(4, new AnimalLayEggGoal(this, 100, 1.0));
        this.f_21345_.addGoal(5, new AnimalJoinPackGoal(this, 60, 8));
        this.f_21345_.addGoal(6, new VallumraptorEntity.FleeGoal());
        this.f_21345_.addGoal(7, new TemptGoal(this, 1.1, Ingredient.of(ACItemRegistry.DINOSAUR_NUGGET.get()), false));
        this.f_21345_.addGoal(8, new VallumraptorMeleeGoal(this));
        this.f_21345_.addGoal(9, new VallumraptorWanderGoal(this, 1.0, 25));
        this.f_21345_.addGoal(10, new VallumraptorOpenDoorGoal(this));
        this.f_21345_.addGoal(11, new AnimalLootChestsGoal(this, 20));
        this.f_21345_.addGoal(12, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(13, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new MobTargetItemGoal(this, true) {

            @Override
            public void start() {
                super.start();
                VallumraptorEntity.this.setRunning(true);
            }

            @Override
            public void stop() {
                super.stop();
                VallumraptorEntity.this.setRunning(false);
            }
        });
        this.f_21346_.addGoal(2, new OwnerHurtByTargetGoal(this));
        this.f_21346_.addGoal(3, new OwnerHurtTargetGoal(this));
        this.f_21346_.addGoal(4, new HurtByTargetGoal(this, VallumraptorEntity.class).setAlertOthers());
        this.f_21346_.addGoal(5, new AnimalPackTargetGoal(this, GrottoceratopsEntity.class, 30, false, 5));
        this.f_21346_.addGoal(6, new MobTargetUntamedGoal(this, Mob.class, 100, true, false, VALLUMRAPTOR_TARGETS));
        this.f_21346_.addGoal(8, new MobTargetClosePlayers(this, 120, 12.0F));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return ACEntityRegistry.VALLUMRAPTOR.get().create(level);
    }

    @Override
    public int getMaxFallDistance() {
        return super.m_6056_() + 10;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(PUZZLED_HEAD_ROT, 0.0F);
        this.f_19804_.define(LEAPING, false);
        this.f_19804_.define(ELDER, false);
        this.f_19804_.define(RUNNING, false);
        this.f_19804_.define(RELAXED_FOR, 0);
        this.f_19804_.define(HIDING_FOR, 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.MOVEMENT_SPEED, 0.2).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MAX_HEALTH, 28.0);
    }

    @Override
    public void tick() {
        super.tick();
        this.prevRunProgress = this.runProgress;
        this.prevLeapProgress = this.leapProgress;
        this.prevRelaxedProgress = this.relaxedProgress;
        this.prevHideProgress = this.hideProgress;
        this.prevTailYaw = this.tailYaw;
        float headPuzzleRot = this.getPuzzledHeadRot();
        if (this.isRunning() && this.runProgress < 5.0F) {
            this.runProgress++;
        }
        if (!this.isRunning() && this.runProgress > 0.0F) {
            this.runProgress--;
        }
        if (this.isLeaping() && this.leapProgress < 5.0F) {
            this.leapProgress++;
        }
        if (!this.isLeaping() && this.leapProgress > 0.0F) {
            this.leapProgress--;
        }
        if (this.getRelaxedFor() > 0 && this.relaxedProgress < 20.0F) {
            this.relaxedProgress++;
        }
        if (this.getRelaxedFor() <= 0 && this.relaxedProgress > 0.0F) {
            this.relaxedProgress--;
        }
        if (this.getHideFor() > 0 && this.hideProgress < 20.0F) {
            this.hideProgress++;
        }
        if (this.getHideFor() <= 0 && this.hideProgress > 0.0F) {
            this.hideProgress--;
        }
        if (this.isRunning() && !this.hasRunningAttributes) {
            this.hasRunningAttributes = true;
            this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.35);
        }
        if (!this.isRunning() && this.hasRunningAttributes) {
            this.hasRunningAttributes = false;
            this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.2);
        }
        if (this.isElder() && !this.hasElderAttributes) {
            this.hasElderAttributes = true;
            this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(32.0);
            this.m_21051_(Attributes.ARMOR).setBaseValue(5.0);
            this.m_5634_(36.0F);
        }
        if (!this.isElder() && this.hasElderAttributes) {
            this.hasElderAttributes = false;
            this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(24.0);
            this.m_21051_(Attributes.ARMOR).setBaseValue(0.0);
            this.m_5634_(28.0F);
        }
        if (this.f_19797_ % (this.getHideFor() > 0 ? 15 : 100) == 0 && this.m_21223_() < this.m_21233_()) {
            this.m_5634_(2.0F);
        }
        if (!this.m_9236_().isClientSide) {
            this.puzzledTick(headPuzzleRot);
            if (this.isStillEnough() && this.f_19796_.nextInt(100) == 0 && this.getAnimation() == NO_ANIMATION && this.getRelaxedFor() <= 0 && !this.isDancing()) {
                float rand = this.f_19796_.nextFloat();
                Animation idle;
                if (rand < 0.45F) {
                    idle = ANIMATION_SCRATCH_1;
                } else if (rand < 0.9F) {
                    idle = ANIMATION_SCRATCH_2;
                } else {
                    idle = ANIMATION_SHAKE;
                }
                this.setAnimation(idle);
            }
            if (this.fleeTicks > 0) {
                this.fleeTicks--;
            }
            if (this.isLeaping()) {
                this.m_20256_(this.m_20184_().multiply(1.1F, 1.0, 1.1F));
            }
        }
        if (this.getAnimation() == ANIMATION_CALL_1 && this.getAnimationTick() == 5 || this.getAnimation() == ANIMATION_CALL_2 && this.getAnimationTick() == 4) {
            this.actuallyPlayAmbientSound();
        }
        if (!this.m_9236_().isClientSide) {
            if (this.eatHeldItemIn > 0) {
                this.eatHeldItemIn--;
            } else if (this.canTargetItem(this.m_21205_())) {
                ItemStack stack = this.m_21205_();
                this.m_9236_().broadcastEntityEvent(this, (byte) 45);
                this.m_5634_(5.0F);
                if (stack.is(ACItemRegistry.DINOSAUR_NUGGET.get()) && this.justLootedChest) {
                    this.setRelaxedForTime(200 + this.f_19796_.nextInt(200));
                }
                if (!this.m_9236_().isClientSide) {
                    stack.shrink(1);
                }
                this.justLootedChest = false;
            }
            if (this.getRelaxedFor() > 0) {
                this.setRelaxedForTime(this.getRelaxedFor() - 1);
            }
            if (this.getHideFor() > 0) {
                this.setHideFor(this.getHideFor() - 1);
            }
        }
        LivingEntity target = this.m_5448_();
        label169: if (target != null && target.isAlive()) {
            if (target instanceof Player player && player.isCreative()) {
                break label169;
            }
            if (this.isElder()) {
                PackAnimal leader = this;
                while (leader.getAfterPackMember() != null) {
                    leader = leader.getAfterPackMember();
                    if (!((VallumraptorEntity) leader).m_7307_(target)) {
                        ((VallumraptorEntity) leader).m_6710_(target);
                    }
                }
            }
            if (this.m_21223_() < this.m_21233_() * 0.45F && this.m_21824_() && this.getHideFor() <= 0) {
                int i = 80 + this.f_19796_.nextInt(40);
                this.setHideFor(i);
                this.fleeFromPosition = target.m_20182_();
                this.fleeTicks = i;
                if (target instanceof Mob mob) {
                    mob.setTarget(null);
                    mob.m_6703_(null);
                    mob.m_21335_(null);
                }
            }
            if (target instanceof GrottoceratopsEntity && (this.f_19797_ + this.m_19879_()) % 20 == 0 && this.getPackSize() < 4 && !this.m_21824_()) {
                this.fleeFromPosition = target.m_20182_();
                this.fleeTicks = 100 + this.f_19796_.nextInt(100);
                this.m_6710_(null);
                this.m_6703_(null);
            }
        }
        this.tailYaw = Mth.approachDegrees(this.tailYaw, this.f_20883_, 8.0F);
        this.prevPuzzleHeadRot = headPuzzleRot;
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 45) {
            ItemStack itemstack = this.m_6844_(EquipmentSlot.MAINHAND);
            if (!itemstack.isEmpty()) {
                for (int i = 0; i < 8; i++) {
                    Vec3 headPos = new Vec3(0.0, 0.1, 0.7).xRot(-this.m_146909_() * (float) (Math.PI / 180.0)).yRot(-this.f_20883_ * (float) (Math.PI / 180.0));
                    this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, itemstack), this.m_20185_() + headPos.x, this.m_20227_(0.5) + headPos.y, this.m_20189_() + headPos.z, (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.1F), (double) (this.f_19796_.nextFloat() * 0.15F), (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.1F));
                }
            }
        } else {
            super.handleEntityEvent(b);
        }
    }

    @Override
    public float maxSitTicks() {
        return 5.0F;
    }

    private void puzzledTick(float current) {
        float dist = Math.abs(this.targetPuzzleRot - current);
        LivingEntity target = this.m_5448_();
        if ((target == null || !target.isAlive()) && this.getAnimation() == NO_ANIMATION && this.getRelaxedFor() <= 0) {
            if (this.f_19796_.nextInt(10) == 0 && dist <= 0.1F) {
                if (this.f_19796_.nextFloat() < 0.25F) {
                    this.targetPuzzleRot = 0.0F;
                } else {
                    float invSignum = this.f_19796_.nextFloat() < 0.1F ? Math.signum(this.f_19796_.nextFloat() - 0.5F) : -Math.signum(this.targetPuzzleRot);
                    this.targetPuzzleRot = this.f_19796_.nextFloat() * 50.0F * invSignum;
                }
            }
        } else {
            this.targetPuzzleRot = 0.0F;
        }
        if (current < this.targetPuzzleRot && dist > 0.1F) {
            this.setPuzzledHeadRot(current + Math.min(dist, 6.0F));
        }
        if (current > this.targetPuzzleRot && dist > 0.1F) {
            this.setPuzzledHeadRot(current - Math.min(dist, 6.0F));
        }
    }

    public float getTailYaw(float partialTick) {
        return this.prevTailYaw + (this.tailYaw - this.prevTailYaw) * partialTick;
    }

    private boolean isStillEnough() {
        return this.m_20184_().horizontalDistance() < 0.05;
    }

    private float getPuzzledHeadRot() {
        return this.f_19804_.get(PUZZLED_HEAD_ROT);
    }

    public float getPuzzledHeadRot(float f) {
        return this.prevPuzzleHeadRot + (this.getPuzzledHeadRot() - this.prevPuzzleHeadRot) * f;
    }

    public void setPuzzledHeadRot(float rot) {
        this.f_19804_.set(PUZZLED_HEAD_ROT, rot);
    }

    public float getLeapProgress(float partialTick) {
        return (this.prevLeapProgress + (this.leapProgress - this.prevLeapProgress) * partialTick) * 0.2F;
    }

    public float getRunProgress(float partialTick) {
        return (this.prevRunProgress + (this.runProgress - this.prevRunProgress) * partialTick) * 0.2F;
    }

    public float getRelaxedProgress(float partialTick) {
        return (this.prevRelaxedProgress + (this.relaxedProgress - this.prevRelaxedProgress) * partialTick) * 0.05F;
    }

    public float getHideProgress(float partialTick) {
        return (this.prevHideProgress + (this.hideProgress - this.prevHideProgress) * partialTick) * 0.05F;
    }

    public boolean isRunning() {
        return this.f_19804_.get(RUNNING);
    }

    public void setRunning(boolean bool) {
        this.f_19804_.set(RUNNING, bool);
    }

    public boolean isLeaping() {
        return this.f_19804_.get(LEAPING);
    }

    public void setLeaping(boolean bool) {
        this.f_19804_.set(LEAPING, bool);
    }

    public boolean isElder() {
        return this.f_19804_.get(ELDER);
    }

    public void setElder(boolean bool) {
        this.f_19804_.set(ELDER, bool);
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
        return new Animation[] { ANIMATION_CALL_1, ANIMATION_CALL_2, ANIMATION_SCRATCH_1, ANIMATION_SCRATCH_2, ANIMATION_SHAKE, ANIMATION_STARTLEAP, ANIMATION_MELEE_BITE, ANIMATION_MELEE_SLASH_1, ANIMATION_MELEE_SLASH_2, ANIMATION_GRAB };
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setElder(compound.getBoolean("Elder"));
        this.setRelaxedForTime(compound.getInt("RelaxedTime"));
        this.justLootedChest = compound.getBoolean("JustLootedChest");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Elder", this.isElder());
        compound.putInt("RelaxedTime", this.getRelaxedFor());
        compound.putBoolean("JustLootedChest", this.justLootedChest);
    }

    @javax.annotation.Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficultyIn, MobSpawnType reason, @javax.annotation.Nullable SpawnGroupData spawnDataIn, @javax.annotation.Nullable CompoundTag dataTag) {
        if (spawnDataIn instanceof AgeableMob.AgeableMobGroupData data) {
            if (data.getGroupSize() == 0) {
                this.setElder(true);
            }
        } else {
            this.setElder(this.m_217043_().nextInt(2) == 0);
        }
        return super.m_6518_(level, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, this.m_20186_() - this.f_19855_, this.m_20189_() - this.f_19856_);
        float f2 = Math.min(f1 * 6.0F, 1.0F);
        this.f_267362_.update(f2, 0.4F);
    }

    @Override
    public void playAmbientSound() {
        if (this.getRelaxedFor() > 0) {
            super.m_8032_();
        } else if (this.getAnimation() == NO_ANIMATION && !this.m_9236_().isClientSide) {
            this.setAnimation(this.f_19796_.nextBoolean() && !this.m_21825_() ? ANIMATION_CALL_2 : ANIMATION_CALL_1);
        }
    }

    public void actuallyPlayAmbientSound() {
        float volume = this.m_6121_();
        SoundEvent soundevent = this.getAmbientSound();
        if (this.getAnimation() == ANIMATION_CALL_2) {
            soundevent = ACSoundRegistry.VALLUMRAPTOR_CALL.get();
            volume++;
        }
        if (soundevent != null) {
            this.m_5496_(soundevent, volume, this.m_6100_());
        }
    }

    @Override
    public void travel(Vec3 vec3d) {
        if (this.getAnimation() == ANIMATION_GRAB || this.getRelaxedFor() > 0) {
            vec3d = Vec3.ZERO;
        }
        super.travel(vec3d);
    }

    @Override
    public PackAnimal getPriorPackMember() {
        return this.priorPackMember;
    }

    @Override
    public PackAnimal getAfterPackMember() {
        return this.afterPackMember;
    }

    @Override
    public void setPriorPackMember(PackAnimal animal) {
        this.priorPackMember = (VallumraptorEntity) animal;
    }

    @Override
    public void setAfterPackMember(PackAnimal animal) {
        this.afterPackMember = (VallumraptorEntity) animal;
    }

    @Override
    public void afterSteal(BlockPos stealPos) {
        this.fleeFromPosition = Vec3.atCenterOf(stealPos);
        this.fleeTicks = 300 + this.f_19796_.nextInt(80);
        this.justLootedChest = true;
        if (this.m_21120_(InteractionHand.MAIN_HAND).is(ACItemRegistry.DINOSAUR_NUGGET.get())) {
            this.eatHeldItemIn = 40 + this.f_19796_.nextInt(20);
        } else {
            this.eatHeldItemIn = 100 + this.f_19796_.nextInt(80);
        }
    }

    @Override
    public boolean isValidLeader(PackAnimal packLeader) {
        return packLeader instanceof VallumraptorEntity && ((VallumraptorEntity) packLeader).m_6084_() && ((VallumraptorEntity) packLeader).isElder();
    }

    @Override
    public boolean shouldLootItem(ItemStack stack) {
        return this.canTargetItem(stack);
    }

    @Override
    public void startOpeningChest() {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_GRAB);
        }
    }

    @Override
    public boolean didOpeningChest() {
        return this.getAnimation() == ANIMATION_GRAB && this.getAnimationTick() > 15;
    }

    @Override
    public boolean canPassThrough(BlockPos blockPos, BlockState blockState, VoxelShape voxelShape) {
        return blockState.m_60734_() instanceof DoorBlock && (Boolean) blockState.m_61143_(DoorBlock.OPEN);
    }

    @Override
    public boolean isColliding(BlockPos pos, BlockState blockState) {
        return (!(blockState.m_60734_() instanceof DoorBlock) || !(Boolean) blockState.m_61143_(DoorBlock.OPEN)) && super.m_20039_(pos, blockState);
    }

    @Override
    public Vec3 collide(Vec3 vec3) {
        return ICustomCollisions.getAllowedMovementForEntity(this, vec3);
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return (stack.is(ACTagRegistry.VALLUMRAPTOR_STEALS) || stack.getItem().isEdible() && stack.getItem().getFoodProperties(stack, this).isMeat()) && !stack.is(ACBlockRegistry.VALLUMRAPTOR_EGG.get().asItem());
    }

    @Override
    public double getMaxDistToItem() {
        return 2.0;
    }

    @Override
    public void onGetItem(ItemEntity e) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_GRAB);
        }
        if (this.getAnimation() == ANIMATION_GRAB && this.getAnimationTick() > 15) {
            if (!this.m_21120_(InteractionHand.MAIN_HAND).isEmpty() && !this.m_9236_().isClientSide) {
                this.m_5552_(this.m_21120_(InteractionHand.MAIN_HAND), 0.0F);
            }
            this.m_7938_(e, 1);
            ItemStack duplicate = e.getItem().copy();
            duplicate.setCount(1);
            this.m_21008_(InteractionHand.MAIN_HAND, duplicate);
            e.getItem().shrink(1);
            this.eatHeldItemIn = this.m_21824_() ? 50 : 200;
        }
    }

    @Override
    public BlockState createEggBlockState() {
        return (BlockState) ACBlockRegistry.VALLUMRAPTOR_EGG.get().defaultBlockState().m_61124_(MultipleDinosaurEggsBlock.EGGS, 1 + this.f_19796_.nextInt(3));
    }

    public float getStepHeight() {
        return this.hasRunningAttributes ? 1.1F : 0.6F;
    }

    @Override
    public boolean tamesFromHatching() {
        return true;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return this.m_21824_() && stack.is(ACItemRegistry.DINOSAUR_NUGGET.get());
    }

    public int getRelaxedFor() {
        return this.f_19804_.get(RELAXED_FOR);
    }

    public void setRelaxedForTime(int ticks) {
        this.f_19804_.set(RELAXED_FOR, ticks);
    }

    public int getHideFor() {
        return this.f_19804_.get(HIDING_FOR);
    }

    public void setHideFor(int ticks) {
        this.f_19804_.set(HIDING_FOR, ticks);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.getRelaxedFor() > 0 ? ACSoundRegistry.VALLUMRAPTOR_SLEEP.get() : ACSoundRegistry.VALLUMRAPTOR_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.VALLUMRAPTOR_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.VALLUMRAPTOR_DEATH.get();
    }

    @Override
    public boolean onFeedMixture(ItemStack itemStack, Player player) {
        if (itemStack.is(ACItemRegistry.SERENE_SALAD.get()) && this.getRelaxedFor() > 0 && !this.m_21824_()) {
            this.m_5634_(5.0F);
            this.setRelaxedForTime(0);
            this.m_21828_(player);
            this.setCommand(1);
            this.m_21839_(true);
            this.m_9236_().broadcastEntityEvent(this, (byte) 7);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canOwnerCommand(Player ownerPlayer) {
        return true;
    }

    private class FleeGoal extends Goal {

        public FleeGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return VallumraptorEntity.this.fleeTicks > 0 && VallumraptorEntity.this.fleeFromPosition != null;
        }

        @Override
        public void stop() {
            VallumraptorEntity.this.fleeFromPosition = null;
            VallumraptorEntity.this.setRunning(false);
        }

        @Override
        public void tick() {
            VallumraptorEntity.this.setRunning(true);
            if (VallumraptorEntity.this.m_21573_().isDone()) {
                int dist = VallumraptorEntity.this.getHideFor() > 0 ? 4 : 8;
                Vec3 vec3 = LandRandomPos.getPosAway(VallumraptorEntity.this, dist, dist, VallumraptorEntity.this.fleeFromPosition);
                if (vec3 != null) {
                    VallumraptorEntity.this.m_21573_().moveTo(vec3.x, vec3.y, vec3.z, 1.0);
                }
            }
        }
    }
}