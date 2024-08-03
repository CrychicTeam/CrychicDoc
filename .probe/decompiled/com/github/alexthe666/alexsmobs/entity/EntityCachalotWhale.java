package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIFollowParentRanged;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIHurtByTargetNotBaby;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIRandomSwimming;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalSwimMoveControllerSink;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowBoatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.ForgeEventFactory;

public class EntityCachalotWhale extends Animal {

    private static final TargetingConditions REWARD_PLAYER_PREDICATE = TargetingConditions.forNonCombat().range(50.0).ignoreLineOfSight();

    private static final EntityDataAccessor<Boolean> CHARGING = SynchedEntityData.defineId(EntityCachalotWhale.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(EntityCachalotWhale.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> BEACHED = SynchedEntityData.defineId(EntityCachalotWhale.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> ALBINO = SynchedEntityData.defineId(EntityCachalotWhale.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DESPAWN_BEACH = SynchedEntityData.defineId(EntityCachalotWhale.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> GRABBING = SynchedEntityData.defineId(EntityCachalotWhale.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> HOLDING_SQUID_LEFT = SynchedEntityData.defineId(EntityCachalotWhale.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> CAUGHT_ID = SynchedEntityData.defineId(EntityCachalotWhale.class, EntityDataSerializers.INT);

    public final double[][] ringBuffer = new double[64][3];

    public final EntityCachalotPart headPart;

    public final EntityCachalotPart bodyFrontPart;

    public final EntityCachalotPart bodyPart;

    public final EntityCachalotPart tail1Part;

    public final EntityCachalotPart tail2Part;

    public final EntityCachalotPart tail3Part;

    public final EntityCachalotPart[] whaleParts;

    private final boolean hasAlbinoAttribute = false;

    public int ringBufferIndex = -1;

    public float prevChargingProgress;

    public float chargeProgress;

    public float prevSleepProgress;

    public float sleepProgress;

    public float prevBeachedProgress;

    public float beachedProgress;

    public float prevGrabProgress;

    public float grabProgress;

    public int grabTime = 0;

    private boolean receivedEcho = false;

    private boolean waitForEchoFlag = true;

    private int echoTimer = 0;

    private boolean prevEyesInWater = false;

    private int spoutTimer = 0;

    private int chargeCooldown = 0;

    private float whaleSpeedMod = 1.0F;

    private int rewardTime = 0;

    private Player rewardPlayer;

    private int blockBreakCounter;

    private int despawnDelay = 47999;

    private int echoSoundCooldown = 0;

    private boolean hasRewardedPlayer = false;

    public EntityCachalotWhale(EntityType type, Level world) {
        super(type, world);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.f_21342_ = new AnimalSwimMoveControllerSink(this, 1.0F, 1.0F, 6.0F);
        this.f_21365_ = new SmoothSwimmingLookControl(this, 4);
        this.headPart = new EntityCachalotPart(this, 3.0F, 3.5F);
        this.bodyFrontPart = new EntityCachalotPart(this, 4.0F, 4.0F);
        this.bodyPart = new EntityCachalotPart(this, 5.0F, 4.0F);
        this.tail1Part = new EntityCachalotPart(this, 4.0F, 3.0F);
        this.tail2Part = new EntityCachalotPart(this, 3.0F, 2.0F);
        this.tail3Part = new EntityCachalotPart(this, 3.0F, 0.7F);
        this.whaleParts = new EntityCachalotPart[] { this.headPart, this.bodyFrontPart, this.bodyPart, this.tail1Part, this.tail2Part, this.tail3Part };
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 160.0).add(Attributes.KNOCKBACK_RESISTANCE, 1.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MOVEMENT_SPEED, 1.2F).add(Attributes.ATTACK_DAMAGE, 30.0);
    }

    public static <T extends Mob> boolean canCachalotWhaleSpawn(EntityType<T> entityType, ServerLevelAccessor iServerWorld, MobSpawnType reason, BlockPos pos, RandomSource random) {
        BlockPos up = pos;
        while (up.m_123342_() < iServerWorld.m_151558_() && iServerWorld.m_6425_(up).is(FluidTags.WATER)) {
            up = up.above();
        }
        return iServerWorld.m_6425_(up.below()).is(FluidTags.WATER) && up.m_123342_() < iServerWorld.m_5736_() + 15 && iServerWorld.m_45527_(up);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isSleeping() && !this.isCharging() && !this.isDespawnBeach() && !this.isAlbino();
    }

    private boolean canDespawn() {
        return this.isDespawnBeach();
    }

    private void tryDespawn() {
        if (this.canDespawn()) {
            this.despawnDelay--;
            if (this.despawnDelay <= 0) {
                this.m_21455_(true, false);
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.CACHALOT_WHALE_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.CACHALOT_WHALE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.CACHALOT_WHALE_HURT.get();
    }

    public void scaleParts() {
        for (EntityCachalotPart parts : this.whaleParts) {
            float prev = parts.scale;
            parts.scale = this.m_6162_() ? 0.5F : 1.0F;
            if (prev != parts.scale) {
                parts.m_6210_();
            }
        }
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public void pushEntities() {
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        return super.mobInteract(player, hand);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Albino", this.isAlbino());
        compound.putBoolean("Beached", this.isBeached());
        compound.putBoolean("BeachedDespawnFlag", this.isDespawnBeach());
        compound.putBoolean("GivenReward", this.hasRewardedPlayer);
        compound.putInt("DespawnDelay", this.despawnDelay);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setAlbino(compound.getBoolean("Albino"));
        this.setBeached(compound.getBoolean("Beached"));
        this.setDespawnBeach(compound.getBoolean("BeachedDespawnFlag"));
        if (compound.contains("DespawnDelay", 99)) {
            this.despawnDelay = compound.getInt("DespawnDelay");
        }
        this.hasRewardedPlayer = compound.getBoolean("GivenReward");
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(CHARGING, false);
        this.f_19804_.define(SLEEPING, false);
        this.f_19804_.define(BEACHED, false);
        this.f_19804_.define(ALBINO, false);
        this.f_19804_.define(GRABBING, false);
        this.f_19804_.define(HOLDING_SQUID_LEFT, false);
        this.f_19804_.define(DESPAWN_BEACH, false);
        this.f_19804_.define(CAUGHT_ID, -1);
    }

    public boolean hasCaughtSquid() {
        return this.f_19804_.get(CAUGHT_ID) != -1;
    }

    private void setCaughtSquidId(int i) {
        this.f_19804_.set(CAUGHT_ID, i);
    }

    @Nullable
    public Entity getCaughtSquid() {
        return !this.hasCaughtSquid() ? null : this.m_9236_().getEntity(this.f_19804_.get(CAUGHT_ID));
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new EntityCachalotWhale.AIBreathe());
        this.f_21345_.addGoal(1, new TryFindWaterGoal(this));
        this.f_21345_.addGoal(2, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(3, new AnimalAIFollowParentRanged(this, 1.1F, 32.0F, 10.0F));
        this.f_21345_.addGoal(4, new AnimalAIRandomSwimming(this, 0.6, 10, 24, true) {

            @Override
            public boolean canUse() {
                return !EntityCachalotWhale.this.isSleeping() && !EntityCachalotWhale.this.isBeached() && super.canUse();
            }
        });
        this.f_21345_.addGoal(5, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 20.0F));
        this.f_21345_.addGoal(7, new FollowBoatGoal(this));
        this.f_21346_.addGoal(1, new AnimalAIHurtByTargetNotBaby(this).m_26044_(new Class[0]));
        this.f_21346_.addGoal(2, new EntityAINearestTarget3D(this, LivingEntity.class, 30, false, true, AMEntityRegistry.buildPredicateFromTag(AMTagRegistry.CACHALOT_WHALE_TARGETS)) {

            @Override
            public boolean canUse() {
                return !EntityCachalotWhale.this.isSleeping() && !EntityCachalotWhale.this.isBeached() && super.m_8036_();
            }
        });
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new WaterBoundPathNavigation(this, worldIn);
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        this.breakBlock();
    }

    public void breakBlock() {
        if (this.blockBreakCounter > 0) {
            this.blockBreakCounter--;
        } else {
            boolean flag = false;
            if (!this.m_9236_().isClientSide && this.blockBreakCounter == 0 && ForgeEventFactory.getMobGriefingEvent(this.m_9236_(), this)) {
                TagKey<Block> breakables = this.isCharging() && this.m_5448_() != null && AMConfig.cachalotDestruction ? AMTagRegistry.CACHALOT_WHALE_BREAKABLES : AMTagRegistry.ORCA_BREAKABLES;
                for (int a = (int) Math.round(this.m_20191_().minX); a <= (int) Math.round(this.m_20191_().maxX); a++) {
                    for (int b = (int) Math.round(this.m_20191_().minY) - 1; b <= (int) Math.round(this.m_20191_().maxY) + 1 && b <= 127; b++) {
                        for (int c = (int) Math.round(this.m_20191_().minZ); c <= (int) Math.round(this.m_20191_().maxZ); c++) {
                            BlockPos pos = new BlockPos(a, b, c);
                            BlockState state = this.m_9236_().getBlockState(pos);
                            FluidState fluidState = this.m_9236_().getFluidState(pos);
                            if (!state.m_60795_() && !state.m_60808_(this.m_9236_(), pos).isEmpty() && state.m_204336_(breakables) && fluidState.isEmpty()) {
                                Block block = state.m_60734_();
                                if (block != Blocks.AIR) {
                                    this.m_20256_(this.m_20184_().multiply(0.6F, 1.0, 0.6F));
                                    flag = true;
                                    this.m_9236_().m_46961_(pos, true);
                                    if (state.m_204336_(BlockTags.ICE)) {
                                        this.m_9236_().setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (flag) {
                this.blockBreakCounter = this.isCharging() && this.m_5448_() != null ? 2 : 20;
            }
        }
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.m_21515_() && this.m_20069_()) {
            this.m_19920_(this.m_6113_(), travelVector);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().scale(0.9));
        } else {
            super.m_7023_(travelVector);
        }
    }

    private void spawnSpoutParticles() {
        if (this.m_6084_()) {
            float radius = this.headPart.m_20205_() * 0.5F;
            for (int j = 0; j < 5 + this.f_19796_.nextInt(4); j++) {
                float angle = (float) (Math.PI / 180.0) * this.f_20883_;
                double extraX = (double) (radius * (1.0F + this.f_19796_.nextFloat() * 0.13F) * Mth.sin((float) Math.PI + angle) + (this.f_19796_.nextFloat() - 0.5F)) + this.m_20184_().x * 2.0;
                double extraZ = (double) (radius * (1.0F + this.f_19796_.nextFloat() * 0.13F) * Mth.cos(angle) + (this.f_19796_.nextFloat() - 0.5F)) + this.m_20184_().z * 2.0;
                double motX = this.f_19796_.nextGaussian();
                double motZ = this.f_19796_.nextGaussian();
                this.m_9236_().addParticle(AMParticleRegistry.WHALE_SPLASH.get(), this.headPart.m_20185_() + extraX, this.headPart.m_20186_() + (double) this.headPart.m_20206_(), this.headPart.m_20189_() + extraZ, motX * 0.1F + this.m_20184_().x, 2.0, motZ * 0.1F + this.m_20184_().z);
            }
        }
    }

    public boolean isCharging() {
        return this.f_19804_.get(CHARGING);
    }

    public void setCharging(boolean charging) {
        this.f_19804_.set(CHARGING, charging);
    }

    @Override
    public boolean isSleeping() {
        return this.f_19804_.get(SLEEPING);
    }

    public void setSleeping(boolean charging) {
        this.f_19804_.set(SLEEPING, charging);
    }

    public boolean isBeached() {
        return this.f_19804_.get(BEACHED);
    }

    public void setBeached(boolean charging) {
        this.f_19804_.set(BEACHED, charging);
    }

    public boolean isGrabbing() {
        return this.f_19804_.get(GRABBING);
    }

    public void setGrabbing(boolean charging) {
        this.f_19804_.set(GRABBING, charging);
    }

    public boolean isHoldingSquidLeft() {
        return this.f_19804_.get(HOLDING_SQUID_LEFT);
    }

    public void setHoldingSquidLeft(boolean charging) {
        this.f_19804_.set(HOLDING_SQUID_LEFT, charging);
    }

    public boolean isAlbino() {
        return this.f_19804_.get(ALBINO);
    }

    public void setAlbino(boolean albino) {
        boolean prev = this.isAlbino();
        if (!prev && albino) {
            this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(230.0);
            this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue(45.0);
            this.m_21153_(230.0F);
        } else {
            this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(160.0);
            this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue(30.0);
        }
        this.f_19804_.set(ALBINO, albino);
    }

    public boolean isDespawnBeach() {
        return this.f_19804_.get(DESPAWN_BEACH);
    }

    public void setDespawnBeach(boolean despawn) {
        this.f_19804_.set(DESPAWN_BEACH, despawn);
    }

    @Override
    protected float getSoundVolume() {
        return this.m_20067_() ? 0.0F : (float) AMConfig.cachalotVolume;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.scaleParts();
        if (this.echoSoundCooldown > 0) {
            this.echoSoundCooldown--;
        }
        if (this.isSleeping()) {
            this.m_21573_().stop();
            this.m_146926_(-90.0F);
            this.whaleSpeedMod = 0.0F;
            if (this.m_204029_(FluidTags.WATER) && this.m_20146_() < 200) {
                this.m_20256_(this.m_20184_().add(0.0, 0.06, 0.0));
            } else {
                BlockPos waterPos = this.m_20183_();
                while (this.m_9236_().getFluidState(waterPos).is(FluidTags.WATER) && waterPos.m_123342_() < 255) {
                    waterPos = waterPos.above();
                }
                if ((double) waterPos.m_123342_() - this.m_20186_() < (double) (this.m_6162_() ? 7 : 12)) {
                    this.m_20256_(this.m_20184_().add(0.0, -0.06, 0.0));
                }
                if (this.f_19796_.nextInt(100) == 0) {
                    this.m_20256_(this.m_20184_().add(0.0, this.f_19796_.nextGaussian() * 0.06, 0.0));
                }
            }
        } else if (this.whaleSpeedMod == 0.0F) {
            this.whaleSpeedMod = 1.0F;
        }
        float rPitch = -((float) this.m_20184_().y * (180.0F / (float) Math.PI));
        if (this.isGrabbing()) {
            this.m_146926_(0.0F);
        } else {
            this.m_146926_(Mth.clamp(rPitch, -90.0F, 90.0F));
        }
        if (this.m_20096_() && !this.m_20072_()) {
            this.setBeached(true);
            this.m_146926_(0.0F);
            this.setSleeping(false);
        }
        if (this.isBeached()) {
            this.whaleSpeedMod = 0.0F;
            this.m_20256_(this.m_20184_().multiply(0.5, 1.0, 0.5));
            if (this.m_204029_(FluidTags.WATER)) {
                Player entity = this.m_9236_().m_45946_(REWARD_PLAYER_PREDICATE, this);
                if (this.m_21188_() != entity) {
                    this.rewardPlayer = entity;
                }
                this.despawnDelay = 47999;
                this.setBeached(false);
            }
        }
        if (this.rewardPlayer != null && !this.hasRewardedPlayer && this.m_20072_()) {
            double d0 = this.rewardPlayer.m_20185_() - this.m_20185_();
            double d1 = this.rewardPlayer.m_20188_() - this.m_20188_();
            double d2 = this.rewardPlayer.m_20189_() - this.m_20189_();
            double d3 = (double) Mth.sqrt((float) (d0 * d0 + d2 * d2));
            float targetYaw = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F;
            float targetPitch = (float) (-(Mth.atan2(d1, d3) * 180.0F / (float) Math.PI));
            this.m_146922_(this.m_146908_() + Mth.clamp(targetYaw - this.m_146908_(), -2.0F, 2.0F));
            this.m_146926_(this.m_146909_() + Mth.clamp(targetPitch - this.m_146909_(), -2.0F, 2.0F));
            this.f_20883_ = this.m_146908_();
            this.whaleSpeedMod = 0.1F;
            this.m_21566_().setWantedPosition(this.rewardPlayer.m_20185_(), this.rewardPlayer.m_20186_(), this.rewardPlayer.m_20189_(), 0.5);
            if (this.m_20270_(this.rewardPlayer) < 10.0F) {
                if (!this.m_9236_().isClientSide) {
                    Vec3 vec = this.getMouthVec();
                    ItemEntity itementity = new ItemEntity(this.m_9236_(), vec.x, vec.y, vec.z, new ItemStack(AMItemRegistry.AMBERGRIS.get(), 2 + this.f_19796_.nextInt(2)));
                    itementity.setDefaultPickUpDelay();
                    this.m_9236_().m_7967_(itementity);
                }
                this.hasRewardedPlayer = true;
                this.rewardPlayer = null;
            }
        }
        this.prevChargingProgress = this.chargeProgress;
        this.prevSleepProgress = this.sleepProgress;
        this.prevBeachedProgress = this.beachedProgress;
        this.prevGrabProgress = this.grabProgress;
        if (this.f_19797_ % 200 == 0) {
            this.m_5634_(2.0F);
        }
        if (this.isCharging()) {
            if (this.chargeProgress < 10.0F) {
                this.chargeProgress++;
            }
        } else if (this.chargeProgress > 0.0F) {
            this.chargeProgress--;
        }
        if (this.isSleeping()) {
            if (this.sleepProgress < 10.0F) {
                this.sleepProgress++;
            }
        } else if (this.sleepProgress > 0.0F) {
            this.sleepProgress--;
        }
        if (this.isBeached()) {
            if (this.beachedProgress < 10.0F) {
                this.beachedProgress++;
            }
        } else if (this.beachedProgress > 0.0F) {
            this.beachedProgress--;
        }
        if (this.isGrabbing()) {
            if (this.grabProgress < 10.0F) {
                this.grabProgress++;
            }
            this.grabTime++;
        } else {
            if (this.grabProgress > 0.0F) {
                this.grabProgress--;
            }
            this.grabTime = 0;
        }
        this.f_20885_ = this.m_146908_();
        this.f_20883_ = this.m_146908_();
        if (!this.m_21525_()) {
            if (this.ringBufferIndex < 0) {
                for (int i = 0; i < this.ringBuffer.length; i++) {
                    this.ringBuffer[i][0] = (double) this.m_146908_();
                    this.ringBuffer[i][1] = this.m_20186_();
                }
            }
            this.ringBufferIndex++;
            if (this.ringBufferIndex == this.ringBuffer.length) {
                this.ringBufferIndex = 0;
            }
            this.ringBuffer[this.ringBufferIndex][0] = (double) this.m_146908_();
            this.ringBuffer[this.ringBufferIndex][1] = this.m_20186_();
            Vec3[] avector3d = new Vec3[this.whaleParts.length];
            for (int j = 0; j < this.whaleParts.length; j++) {
                this.whaleParts[j].collideWithNearbyEntities();
                avector3d[j] = new Vec3(this.whaleParts[j].m_20185_(), this.whaleParts[j].m_20186_(), this.whaleParts[j].m_20189_());
            }
            float f15 = (float) (this.getMovementOffsets(5, 1.0F)[1] - this.getMovementOffsets(10, 1.0F)[1]) * 10.0F * (float) (Math.PI / 180.0);
            float f16 = Mth.cos(f15);
            float f17 = this.m_146908_() * (float) (Math.PI / 180.0);
            float pitch = this.m_146909_() * (float) (Math.PI / 180.0);
            float xRotDiv90 = Math.abs(this.m_146909_() / 90.0F);
            float f3 = Mth.sin(f17) * (1.0F - xRotDiv90);
            float f18 = Mth.cos(f17) * (1.0F - xRotDiv90);
            this.setPartPosition(this.bodyPart, (double) (f3 * 0.5F), (double) (-pitch * 0.5F), (double) (-f18 * 0.5F));
            this.setPartPosition(this.bodyFrontPart, (double) (f3 * -3.5F), (double) (-pitch * 3.0F), (double) (f18 * 3.5F));
            this.setPartPosition(this.headPart, (double) (f3 * -7.0F), (double) (-pitch * 5.0F), (double) (-f18 * -7.0F));
            double[] adouble = this.getMovementOffsets(5, 1.0F);
            for (int k = 0; k < 3; k++) {
                EntityCachalotPart enderdragonpartentity;
                if (k == 0) {
                    enderdragonpartentity = this.tail1Part;
                } else if (k == 1) {
                    enderdragonpartentity = this.tail2Part;
                } else {
                    enderdragonpartentity = this.tail3Part;
                }
                double[] adouble1 = this.getMovementOffsets(15 + k * 5, 1.0F);
                float f7 = this.m_146908_() * (float) (Math.PI / 180.0) + (float) Mth.wrapDegrees(adouble1[0] - adouble[0]) * (float) (Math.PI / 180.0);
                float f19 = 1.0F - Math.abs(this.m_146909_() / 90.0F);
                float f20 = Mth.sin(f7) * f19;
                float f21 = Mth.cos(f7) * f19;
                float f22 = -3.6F;
                float f23 = (float) (k + 1) * -3.6F - 2.0F;
                this.setPartPosition(enderdragonpartentity, (double) (-(f3 * 0.5F + f20 * f23) * f16), (double) (pitch * 1.5F * (float) (k + 1)), (double) ((f18 * 0.5F + f21 * f23) * f16));
            }
            for (int l = 0; l < this.whaleParts.length; l++) {
                this.whaleParts[l].f_19854_ = avector3d[l].x;
                this.whaleParts[l].f_19855_ = avector3d[l].y;
                this.whaleParts[l].f_19856_ = avector3d[l].z;
                this.whaleParts[l].f_19790_ = avector3d[l].x;
                this.whaleParts[l].f_19791_ = avector3d[l].y;
                this.whaleParts[l].f_19792_ = avector3d[l].z;
            }
        }
        if (!this.m_9236_().isClientSide) {
            LivingEntity target = this.m_5448_();
            if (target != null && target.isAlive()) {
                if (!this.isBeached() && !this.isSleeping() && this.rewardPlayer == null) {
                    if (this.isGrabbing() && this.m_5448_().isAlive()) {
                        this.setCaughtSquidId(this.m_5448_().m_19879_());
                        this.whaleSpeedMod = 0.1F;
                        float scale = this.m_6162_() ? 0.5F : 1.0F;
                        float offsetAngle = -((float) Math.cos((double) ((float) this.grabTime * 0.3F))) * 0.1F * this.grabProgress;
                        float renderYaw = (float) this.getMovementOffsets(0, 1.0F)[0];
                        Vec3 extraVec = new Vec3(0.0, 0.0, -3.0).xRot(-this.m_146909_() * (float) (Math.PI / 180.0)).yRot(-renderYaw * (float) (Math.PI / 180.0));
                        Vec3 backOfHead = this.headPart.m_20182_().add(extraVec);
                        Vec3 swingVec = new Vec3(this.isHoldingSquidLeft() ? 1.4F : -1.4F, -0.1, 3.0).xRot(-this.m_146909_() * (float) (Math.PI / 180.0)).yRot(-renderYaw * (float) (Math.PI / 180.0)).yRot(offsetAngle);
                        Vec3 mouth = backOfHead.add(swingVec).scale((double) scale);
                        this.m_5448_().m_6034_(mouth.x, mouth.y, mouth.z);
                        if (this.isHoldingSquidLeft()) {
                            this.m_5448_().m_146922_(this.f_20883_ + 90.0F - (float) Math.toDegrees((double) offsetAngle));
                        } else {
                            this.m_5448_().m_146922_(this.f_20883_ - 90.0F - (float) Math.toDegrees((double) offsetAngle));
                        }
                        if (this.m_5448_() instanceof EntityGiantSquid && ((EntityGiantSquid) this.m_5448_()).tickCaptured(this)) {
                            this.setGrabbing(false);
                            this.m_5448_().m_146884_(this.getDismountLocationForPassenger(this.m_5448_()));
                        }
                        if (this.grabTime % 20 == 0 && this.grabTime > 30) {
                            this.m_5448_().hurt(this.m_269291_().mobAttack(this), (float) (4 + this.f_19796_.nextInt(4)));
                        }
                        if (this.grabTime > 300) {
                            this.setGrabbing(false);
                            this.m_5448_().m_146884_(this.getDismountLocationForPassenger(this.m_5448_()));
                        }
                    } else {
                        this.setCaughtSquidId(-1);
                        this.m_21391_(target, 360.0F, 360.0F);
                        this.waitForEchoFlag = this.m_21188_() == null || !this.m_21188_().m_7306_(target);
                        if (target instanceof Player || !target.m_20072_()) {
                            this.waitForEchoFlag = false;
                        }
                        if (this.waitForEchoFlag && !this.receivedEcho) {
                            this.setCharging(false);
                            this.whaleSpeedMod = 0.25F;
                            if (this.echoTimer % 10 == 0) {
                                if (this.echoTimer % 40 == 0) {
                                    this.m_5496_(AMSoundRegistry.CACHALOT_WHALE_CLICK.get(), this.getSoundVolume(), this.m_6100_());
                                    this.m_146850_(GameEvent.ENTITY_ROAR);
                                }
                                EntityCachalotEcho echo = new EntityCachalotEcho(this.m_9236_(), this);
                                float radius = this.headPart.m_20205_() * 0.5F;
                                float angle = (float) (Math.PI / 180.0) * this.f_20883_;
                                double extraX = (double) (radius * (1.0F + this.f_19796_.nextFloat() * 0.13F) * Mth.sin((float) Math.PI + angle) + (this.f_19796_.nextFloat() - 0.5F)) + this.m_20184_().x * 2.0;
                                double extraZ = (double) (radius * (1.0F + this.f_19796_.nextFloat() * 0.13F) * Mth.cos(angle) + (this.f_19796_.nextFloat() - 0.5F)) + this.m_20184_().z * 2.0;
                                double x = this.headPart.m_20185_() + extraX;
                                double y = this.headPart.m_20186_() + (double) this.headPart.m_20206_() * 0.5;
                                double z = this.headPart.m_20189_() + extraZ;
                                echo.m_6034_(x, y, z);
                                double d0 = target.m_20185_() - x;
                                double d1 = target.m_20227_(0.1) - y;
                                double d2 = target.m_20189_() - z;
                                echo.shoot(d0, d1, d2, 1.0F, 0.0F);
                                this.m_9236_().m_7967_(echo);
                            }
                            this.echoTimer++;
                        }
                        if (!this.waitForEchoFlag || this.receivedEcho) {
                            double d0 = target.m_20185_() - this.m_20185_();
                            double d1 = target.m_20188_() - this.m_20188_();
                            double d2 = target.m_20189_() - this.m_20189_();
                            double d3 = (double) Mth.sqrt((float) (d0 * d0 + d2 * d2));
                            float targetYaw = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F;
                            float targetPitch = (float) (-(Mth.atan2(d1, d3) * 180.0F / (float) Math.PI));
                            this.m_146926_(this.m_146909_() + Mth.clamp(targetPitch - this.m_146909_(), -2.0F, 2.0F));
                            if (d0 * d0 + d2 * d2 >= 4.0) {
                                this.m_146922_(this.m_146908_() + Mth.clamp(targetYaw - this.m_146908_(), -2.0F, 2.0F));
                                this.f_20883_ = this.m_146908_();
                            }
                            if (this.chargeCooldown <= 0 && Math.abs(Mth.wrapDegrees(targetYaw) - Mth.wrapDegrees(this.m_146908_())) < 4.0F) {
                                this.setCharging(true);
                                this.whaleSpeedMod = 1.2F;
                                double distSq = d0 * d0 + d2 * d2;
                                if (distSq < 4.0) {
                                    this.m_146922_(this.f_19859_);
                                    this.f_20883_ = this.f_19859_;
                                    this.m_20256_(this.m_20184_().multiply(0.8, 1.0, 0.8));
                                } else {
                                    if (this.m_20069_() && target.m_20069_()) {
                                        Vec3 vector3d = this.m_20184_();
                                        Vec3 vector3d1 = new Vec3(target.m_20185_() - this.m_20185_(), target.m_20186_() - this.m_20186_(), target.m_20189_() - this.m_20189_());
                                        if (vector3d1.lengthSqr() > 1.0E-7) {
                                            vector3d1 = vector3d1.normalize().scale(0.5).add(vector3d.scale(0.8));
                                        }
                                        this.m_20334_(vector3d1.x, vector3d1.y, vector3d1.z);
                                    }
                                    this.m_21566_().setWantedPosition(target.m_20185_(), target.m_20186_(), target.m_20189_(), 1.0);
                                }
                                if (this.isCharging() && this.m_20270_(target) < this.m_20205_() && this.chargeProgress > 4.0F) {
                                    if (target instanceof EntityGiantSquid && !this.m_6162_()) {
                                        this.setGrabbing(true);
                                        this.setHoldingSquidLeft(this.f_19796_.nextBoolean());
                                    } else {
                                        target.hurt(this.m_269291_().mobAttack(this), (float) this.m_21133_(Attributes.ATTACK_DAMAGE));
                                    }
                                    this.setCharging(false);
                                    if (target.m_20202_() instanceof Boat boat) {
                                        for (int i = 0; i < 3; i++) {
                                            this.m_19998_(boat.getVariant().getPlanks());
                                        }
                                        for (int j = 0; j < 2; j++) {
                                            this.m_19998_(Items.STICK);
                                        }
                                        target.m_6038_();
                                        boat.hurt(this.m_269291_().mobAttack(this), 1000.0F);
                                        boat.m_142687_(Entity.RemovalReason.DISCARDED);
                                    }
                                    this.chargeCooldown = target instanceof Player ? 30 : 100;
                                    if (this.f_19796_.nextInt(10) == 0) {
                                        Vec3 vec = this.getMouthVec();
                                        ItemEntity itementity = new ItemEntity(this.m_9236_(), vec.x, vec.y, vec.z, new ItemStack(AMItemRegistry.CACHALOT_WHALE_TOOTH.get()));
                                        itementity.setDefaultPickUpDelay();
                                        this.m_9236_().m_7967_(itementity);
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                this.setGrabbing(false);
                this.whaleSpeedMod = this.isSleeping() ? 0.0F : 1.0F;
                this.setCharging(false);
                this.setCaughtSquidId(-1);
            }
            if (this.chargeCooldown > 0) {
                this.chargeCooldown--;
            }
            if (this.spoutTimer > 0) {
                this.m_9236_().broadcastEntityEvent(this, (byte) 67);
                this.spoutTimer--;
                this.m_146926_(0.0F);
                this.m_20256_(this.m_20184_().multiply(0.0, 0.0, 0.0));
            }
            if (this.isSleepTime() && !this.isSleeping() && this.m_20072_() && this.m_5448_() == null) {
                this.setSleeping(true);
            }
            if (this.isSleeping() && (!this.isSleepTime() || this.m_5448_() != null)) {
                this.setSleeping(false);
            }
            if (target instanceof Player && ((Player) target).isCreative()) {
                this.setTarget(null);
            }
        }
        if (this.m_6084_() && this.isCharging()) {
            for (Entity entity : this.m_9236_().m_45976_(LivingEntity.class, this.headPart.m_20191_().inflate(1.0))) {
                if (!this.m_7307_(entity) && !(entity instanceof EntityCachalotPart) && entity != this) {
                    this.launch(entity, true);
                }
            }
        }
        if (this.m_20069_() && !this.m_204029_(FluidTags.WATER) && this.m_20146_() > 140) {
            this.m_20256_(this.m_20184_().add(0.0, -0.06, 0.0));
        }
        if (!this.m_9236_().isClientSide) {
            this.tryDespawn();
        }
        this.prevEyesInWater = this.m_204029_(FluidTags.WATER);
    }

    private void launch(Entity e, boolean huge) {
        if ((e.onGround() || e.isInWater()) && !(e instanceof EntityCachalotWhale)) {
            double d0 = e.getX() - this.m_20185_();
            double d1 = e.getZ() - this.m_20189_();
            double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
            float f = huge ? 2.0F : 0.5F;
            e.push(d0 / d2 * (double) f, huge ? 0.5 : 0.2F, d1 / d2 * (double) f);
        }
    }

    private boolean isSleepTime() {
        long time = this.m_9236_().getDayTime();
        return time > 18000L && time < 22812L && this.m_20072_();
    }

    public Vec3 getReturnEchoVector() {
        return this.getVec(0.5);
    }

    public Vec3 getMouthVec() {
        return this.getVec(0.25);
    }

    private Vec3 getVec(double yShift) {
        float radius = this.headPart.m_20205_() * 0.5F;
        float angle = (float) (Math.PI / 180.0) * this.f_20883_;
        double extraX = (double) (radius * (1.0F + this.f_19796_.nextFloat() * 0.13F) * Mth.sin((float) Math.PI + angle) + (this.f_19796_.nextFloat() - 0.5F)) + this.m_20184_().x * 2.0;
        double extraZ = (double) (radius * (1.0F + this.f_19796_.nextFloat() * 0.13F) * Mth.cos(angle) + (this.f_19796_.nextFloat() - 0.5F)) + this.m_20184_().z * 2.0;
        double x = this.headPart.m_20185_() + extraX;
        double y = this.headPart.m_20186_() + yShift;
        double z = this.headPart.m_20189_() + extraZ;
        return new Vec3(x, y, z);
    }

    @Override
    public void setTarget(@Nullable LivingEntity entitylivingbaseIn) {
        LivingEntity prev = this.m_5448_();
        if (prev != entitylivingbaseIn && entitylivingbaseIn != null) {
            this.receivedEcho = false;
        }
        super.m_6710_(entitylivingbaseIn);
    }

    public double[] getMovementOffsets(int p_70974_1_, float partialTicks) {
        if (this.m_21224_()) {
            partialTicks = 0.0F;
        }
        partialTicks = 1.0F - partialTicks;
        int i = this.ringBufferIndex - p_70974_1_ & 63;
        int j = this.ringBufferIndex - p_70974_1_ - 1 & 63;
        double[] adouble = new double[3];
        double d0 = this.ringBuffer[i][0];
        double d1 = this.ringBuffer[j][0] - d0;
        adouble[0] = d0 + d1 * (double) partialTicks;
        d0 = this.ringBuffer[i][1];
        d1 = this.ringBuffer[j][1] - d0;
        adouble[1] = d0 + d1 * (double) partialTicks;
        adouble[2] = Mth.lerp((double) partialTicks, this.ringBuffer[i][2], this.ringBuffer[j][2]);
        return adouble;
    }

    @Override
    public void push(Entity entityIn) {
    }

    private void setPartPosition(EntityCachalotPart part, double offsetX, double offsetY, double offsetZ) {
        part.m_6034_(this.m_20185_() + offsetX * (double) part.scale, this.m_20186_() + offsetY * (double) part.scale, this.m_20189_() + offsetZ * (double) part.scale);
    }

    public boolean isMultipartEntity() {
        return true;
    }

    public PartEntity<?>[] getParts() {
        return this.whaleParts;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        EntityCachalotWhale whale = AMEntityRegistry.CACHALOT_WHALE.get().create(serverWorld);
        whale.setAlbino(this.isAlbino());
        return whale;
    }

    public boolean attackEntityPartFrom(EntityCachalotPart entityCachalotPart, DamageSource source, float amount) {
        return this.m_6469_(source, amount);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.m_20301_(this.getMaxAirSupply());
        this.m_146926_(0.0F);
        if (spawnDataIn == null) {
            spawnDataIn = new AgeableMob.AgeableMobGroupData(0.75F);
        }
        this.setAlbino(this.f_19796_.nextInt(100) == 0);
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
        return this.isBeached();
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
    public int getMaxAirSupply() {
        return 4000;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 67) {
            this.spawnSpoutParticles();
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected int increaseAirSupply(int currentAir) {
        if (!this.m_9236_().isClientSide && this.prevEyesInWater && this.spoutTimer <= 0 && !this.m_204029_(FluidTags.WATER) && currentAir < this.getMaxAirSupply() / 2) {
            this.spoutTimer = 20 + this.f_19796_.nextInt(10);
        }
        return this.getMaxAirSupply();
    }

    @Override
    public int getMaxHeadXRot() {
        return 1;
    }

    @Override
    public int getMaxHeadYRot() {
        return 3;
    }

    public void recieveEcho() {
        this.receivedEcho = true;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.cachalotWhaleSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity dismount) {
        Vec3 mouth = this.getMouthVec();
        BlockPos pos = AMBlockPos.fromVec3(mouth);
        while (!this.m_9236_().m_46859_(pos) && !this.m_9236_().m_46801_(pos) && pos.m_123342_() < this.m_9236_().m_151558_()) {
            pos = pos.above();
        }
        return new Vec3(mouth.x, (double) ((float) pos.m_123342_() + 0.5F), mouth.z);
    }

    class AIBreathe extends Goal {

        public AIBreathe() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return EntityCachalotWhale.this.m_20146_() < 140;
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        @Override
        public void start() {
            this.navigate();
        }

        private void navigate() {
            Iterable<BlockPos> lvt_1_1_ = BlockPos.betweenClosed(Mth.floor(EntityCachalotWhale.this.m_20185_() - 1.0), Mth.floor(EntityCachalotWhale.this.m_20186_()), Mth.floor(EntityCachalotWhale.this.m_20189_() - 1.0), Mth.floor(EntityCachalotWhale.this.m_20185_() + 1.0), Mth.floor(EntityCachalotWhale.this.m_20186_() + 8.0), Mth.floor(EntityCachalotWhale.this.m_20189_() + 1.0));
            BlockPos lvt_2_1_ = null;
            for (BlockPos lvt_4_1_ : lvt_1_1_) {
                if (this.canBreatheAt(EntityCachalotWhale.this.m_9236_(), lvt_4_1_)) {
                    lvt_2_1_ = lvt_4_1_.below((int) ((double) EntityCachalotWhale.this.m_20206_() * 0.25));
                    break;
                }
            }
            if (lvt_2_1_ == null) {
                lvt_2_1_ = AMBlockPos.fromCoords(EntityCachalotWhale.this.m_20185_(), EntityCachalotWhale.this.m_20186_() + 4.0, EntityCachalotWhale.this.m_20189_());
            }
            if (EntityCachalotWhale.this.m_204029_(FluidTags.WATER)) {
                EntityCachalotWhale.this.m_20256_(EntityCachalotWhale.this.m_20184_().add(0.0, 0.05F, 0.0));
            }
            EntityCachalotWhale.this.m_21573_().moveTo((double) lvt_2_1_.m_123341_(), (double) lvt_2_1_.m_123342_(), (double) lvt_2_1_.m_123343_(), 0.7);
        }

        @Override
        public void tick() {
            this.navigate();
        }

        private boolean canBreatheAt(LevelReader p_205140_1_, BlockPos p_205140_2_) {
            BlockState lvt_3_1_ = p_205140_1_.m_8055_(p_205140_2_);
            return (p_205140_1_.m_6425_(p_205140_2_).isEmpty() || lvt_3_1_.m_60713_(Blocks.BUBBLE_COLUMN)) && lvt_3_1_.m_60647_(p_205140_1_, p_205140_2_, PathComputationType.LAND);
        }
    }
}