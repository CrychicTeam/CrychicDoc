package net.minecraft.world.entity.animal;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.StructureTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreathAirGoal;
import net.minecraft.world.entity.ai.goal.DolphinJumpGoal;
import net.minecraft.world.entity.ai.goal.FollowBoatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;

public class Dolphin extends WaterAnimal {

    private static final EntityDataAccessor<BlockPos> TREASURE_POS = SynchedEntityData.defineId(Dolphin.class, EntityDataSerializers.BLOCK_POS);

    private static final EntityDataAccessor<Boolean> GOT_FISH = SynchedEntityData.defineId(Dolphin.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> MOISTNESS_LEVEL = SynchedEntityData.defineId(Dolphin.class, EntityDataSerializers.INT);

    static final TargetingConditions SWIM_WITH_PLAYER_TARGETING = TargetingConditions.forNonCombat().range(10.0).ignoreLineOfSight();

    public static final int TOTAL_AIR_SUPPLY = 4800;

    private static final int TOTAL_MOISTNESS_LEVEL = 2400;

    public static final Predicate<ItemEntity> ALLOWED_ITEMS = p_289436_ -> !p_289436_.hasPickUpDelay() && p_289436_.m_6084_() && p_289436_.m_20069_();

    public Dolphin(EntityType<? extends Dolphin> entityTypeExtendsDolphin0, Level level1) {
        super(entityTypeExtendsDolphin0, level1);
        this.f_21342_ = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.f_21365_ = new SmoothSwimmingLookControl(this, 10);
        this.m_21553_(true);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        this.m_20301_(this.getMaxAirSupply());
        this.m_146926_(0.0F);
        return super.m_6518_(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return false;
    }

    @Override
    protected void handleAirSupply(int int0) {
    }

    public void setTreasurePos(BlockPos blockPos0) {
        this.f_19804_.set(TREASURE_POS, blockPos0);
    }

    public BlockPos getTreasurePos() {
        return this.f_19804_.get(TREASURE_POS);
    }

    public boolean gotFish() {
        return this.f_19804_.get(GOT_FISH);
    }

    public void setGotFish(boolean boolean0) {
        this.f_19804_.set(GOT_FISH, boolean0);
    }

    public int getMoistnessLevel() {
        return this.f_19804_.get(MOISTNESS_LEVEL);
    }

    public void setMoisntessLevel(int int0) {
        this.f_19804_.set(MOISTNESS_LEVEL, int0);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(TREASURE_POS, BlockPos.ZERO);
        this.f_19804_.define(GOT_FISH, false);
        this.f_19804_.define(MOISTNESS_LEVEL, 2400);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7380_(compoundTag0);
        compoundTag0.putInt("TreasurePosX", this.getTreasurePos().m_123341_());
        compoundTag0.putInt("TreasurePosY", this.getTreasurePos().m_123342_());
        compoundTag0.putInt("TreasurePosZ", this.getTreasurePos().m_123343_());
        compoundTag0.putBoolean("GotFish", this.gotFish());
        compoundTag0.putInt("Moistness", this.getMoistnessLevel());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        int $$1 = compoundTag0.getInt("TreasurePosX");
        int $$2 = compoundTag0.getInt("TreasurePosY");
        int $$3 = compoundTag0.getInt("TreasurePosZ");
        this.setTreasurePos(new BlockPos($$1, $$2, $$3));
        super.m_7378_(compoundTag0);
        this.setGotFish(compoundTag0.getBoolean("GotFish"));
        this.setMoisntessLevel(compoundTag0.getInt("Moistness"));
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new BreathAirGoal(this));
        this.f_21345_.addGoal(0, new TryFindWaterGoal(this));
        this.f_21345_.addGoal(1, new Dolphin.DolphinSwimToTreasureGoal(this));
        this.f_21345_.addGoal(2, new Dolphin.DolphinSwimWithPlayerGoal(this, 4.0));
        this.f_21345_.addGoal(4, new RandomSwimmingGoal(this, 1.0, 10));
        this.f_21345_.addGoal(4, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(5, new DolphinJumpGoal(this, 10));
        this.f_21345_.addGoal(6, new MeleeAttackGoal(this, 1.2F, true));
        this.f_21345_.addGoal(8, new Dolphin.PlayWithItemsGoal());
        this.f_21345_.addGoal(8, new FollowBoatGoal(this));
        this.f_21345_.addGoal(9, new AvoidEntityGoal(this, Guardian.class, 8.0F, 1.0, 1.0));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this, Guardian.class).setAlertOthers());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 1.2F).add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    @Override
    protected PathNavigation createNavigation(Level level0) {
        return new WaterBoundPathNavigation(this, level0);
    }

    @Override
    public boolean doHurtTarget(Entity entity0) {
        boolean $$1 = entity0.hurt(this.m_269291_().mobAttack(this), (float) ((int) this.m_21133_(Attributes.ATTACK_DAMAGE)));
        if ($$1) {
            this.m_19970_(this, entity0);
            this.m_5496_(SoundEvents.DOLPHIN_ATTACK, 1.0F, 1.0F);
        }
        return $$1;
    }

    @Override
    public int getMaxAirSupply() {
        return 4800;
    }

    @Override
    protected int increaseAirSupply(int int0) {
        return this.getMaxAirSupply();
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return 0.3F;
    }

    @Override
    public int getMaxHeadXRot() {
        return 1;
    }

    @Override
    public int getMaxHeadYRot() {
        return 1;
    }

    @Override
    protected boolean canRide(Entity entity0) {
        return true;
    }

    @Override
    public boolean canTakeItem(ItemStack itemStack0) {
        EquipmentSlot $$1 = Mob.m_147233_(itemStack0);
        return !this.m_6844_($$1).isEmpty() ? false : $$1 == EquipmentSlot.MAINHAND && super.m_7066_(itemStack0);
    }

    @Override
    protected void pickUpItem(ItemEntity itemEntity0) {
        if (this.m_6844_(EquipmentSlot.MAINHAND).isEmpty()) {
            ItemStack $$1 = itemEntity0.getItem();
            if (this.m_7252_($$1)) {
                this.m_21053_(itemEntity0);
                this.m_8061_(EquipmentSlot.MAINHAND, $$1);
                this.m_21508_(EquipmentSlot.MAINHAND);
                this.m_7938_(itemEntity0, $$1.getCount());
                itemEntity0.m_146870_();
            }
        }
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.m_21525_()) {
            this.m_20301_(this.getMaxAirSupply());
        } else {
            if (this.m_20071_()) {
                this.setMoisntessLevel(2400);
            } else {
                this.setMoisntessLevel(this.getMoistnessLevel() - 1);
                if (this.getMoistnessLevel() <= 0) {
                    this.m_6469_(this.m_269291_().dryOut(), 1.0F);
                }
                if (this.m_20096_()) {
                    this.m_20256_(this.m_20184_().add((double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.2F), 0.5, (double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.2F)));
                    this.m_146922_(this.f_19796_.nextFloat() * 360.0F);
                    this.m_6853_(false);
                    this.f_19812_ = true;
                }
            }
            if (this.m_9236_().isClientSide && this.m_20069_() && this.m_20184_().lengthSqr() > 0.03) {
                Vec3 $$0 = this.m_20252_(0.0F);
                float $$1 = Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0)) * 0.3F;
                float $$2 = Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0)) * 0.3F;
                float $$3 = 1.2F - this.f_19796_.nextFloat() * 0.7F;
                for (int $$4 = 0; $$4 < 2; $$4++) {
                    this.m_9236_().addParticle(ParticleTypes.DOLPHIN, this.m_20185_() - $$0.x * (double) $$3 + (double) $$1, this.m_20186_() - $$0.y, this.m_20189_() - $$0.z * (double) $$3 + (double) $$2, 0.0, 0.0, 0.0);
                    this.m_9236_().addParticle(ParticleTypes.DOLPHIN, this.m_20185_() - $$0.x * (double) $$3 - (double) $$1, this.m_20186_() - $$0.y, this.m_20189_() - $$0.z * (double) $$3 - (double) $$2, 0.0, 0.0, 0.0);
                }
            }
        }
    }

    @Override
    public void handleEntityEvent(byte byte0) {
        if (byte0 == 38) {
            this.addParticlesAroundSelf(ParticleTypes.HAPPY_VILLAGER);
        } else {
            super.m_7822_(byte0);
        }
    }

    private void addParticlesAroundSelf(ParticleOptions particleOptions0) {
        for (int $$1 = 0; $$1 < 7; $$1++) {
            double $$2 = this.f_19796_.nextGaussian() * 0.01;
            double $$3 = this.f_19796_.nextGaussian() * 0.01;
            double $$4 = this.f_19796_.nextGaussian() * 0.01;
            this.m_9236_().addParticle(particleOptions0, this.m_20208_(1.0), this.m_20187_() + 0.2, this.m_20262_(1.0), $$2, $$3, $$4);
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player0, InteractionHand interactionHand1) {
        ItemStack $$2 = player0.m_21120_(interactionHand1);
        if (!$$2.isEmpty() && $$2.is(ItemTags.FISHES)) {
            if (!this.m_9236_().isClientSide) {
                this.m_5496_(SoundEvents.DOLPHIN_EAT, 1.0F, 1.0F);
            }
            this.setGotFish(true);
            if (!player0.getAbilities().instabuild) {
                $$2.shrink(1);
            }
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else {
            return super.m_6071_(player0, interactionHand1);
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.DOLPHIN_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.DOLPHIN_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.m_20069_() ? SoundEvents.DOLPHIN_AMBIENT_WATER : SoundEvents.DOLPHIN_AMBIENT;
    }

    @Override
    protected SoundEvent getSwimSplashSound() {
        return SoundEvents.DOLPHIN_SPLASH;
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.DOLPHIN_SWIM;
    }

    protected boolean closeToNextPos() {
        BlockPos $$0 = this.m_21573_().getTargetPos();
        return $$0 != null ? $$0.m_203195_(this.m_20182_(), 12.0) : false;
    }

    @Override
    public void travel(Vec3 vec0) {
        if (this.m_21515_() && this.m_20069_()) {
            this.m_19920_(this.m_6113_(), vec0);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().scale(0.9));
            if (this.m_5448_() == null) {
                this.m_20256_(this.m_20184_().add(0.0, -0.005, 0.0));
            }
        } else {
            super.m_7023_(vec0);
        }
    }

    @Override
    public boolean canBeLeashed(Player player0) {
        return true;
    }

    static class DolphinSwimToTreasureGoal extends Goal {

        private final Dolphin dolphin;

        private boolean stuck;

        DolphinSwimToTreasureGoal(Dolphin dolphin0) {
            this.dolphin = dolphin0;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        @Override
        public boolean canUse() {
            return this.dolphin.gotFish() && this.dolphin.m_20146_() >= 100;
        }

        @Override
        public boolean canContinueToUse() {
            BlockPos $$0 = this.dolphin.getTreasurePos();
            return !BlockPos.containing((double) $$0.m_123341_(), this.dolphin.m_20186_(), (double) $$0.m_123343_()).m_203195_(this.dolphin.m_20182_(), 4.0) && !this.stuck && this.dolphin.m_20146_() >= 100;
        }

        @Override
        public void start() {
            if (this.dolphin.m_9236_() instanceof ServerLevel) {
                ServerLevel $$0 = (ServerLevel) this.dolphin.m_9236_();
                this.stuck = false;
                this.dolphin.m_21573_().stop();
                BlockPos $$1 = this.dolphin.m_20183_();
                BlockPos $$2 = $$0.findNearestMapStructure(StructureTags.DOLPHIN_LOCATED, $$1, 50, false);
                if ($$2 != null) {
                    this.dolphin.setTreasurePos($$2);
                    $$0.broadcastEntityEvent(this.dolphin, (byte) 38);
                } else {
                    this.stuck = true;
                }
            }
        }

        @Override
        public void stop() {
            BlockPos $$0 = this.dolphin.getTreasurePos();
            if (BlockPos.containing((double) $$0.m_123341_(), this.dolphin.m_20186_(), (double) $$0.m_123343_()).m_203195_(this.dolphin.m_20182_(), 4.0) || this.stuck) {
                this.dolphin.setGotFish(false);
            }
        }

        @Override
        public void tick() {
            Level $$0 = this.dolphin.m_9236_();
            if (this.dolphin.closeToNextPos() || this.dolphin.m_21573_().isDone()) {
                Vec3 $$1 = Vec3.atCenterOf(this.dolphin.getTreasurePos());
                Vec3 $$2 = DefaultRandomPos.getPosTowards(this.dolphin, 16, 1, $$1, (float) (Math.PI / 8));
                if ($$2 == null) {
                    $$2 = DefaultRandomPos.getPosTowards(this.dolphin, 8, 4, $$1, (float) (Math.PI / 2));
                }
                if ($$2 != null) {
                    BlockPos $$3 = BlockPos.containing($$2);
                    if (!$$0.getFluidState($$3).is(FluidTags.WATER) || !$$0.getBlockState($$3).m_60647_($$0, $$3, PathComputationType.WATER)) {
                        $$2 = DefaultRandomPos.getPosTowards(this.dolphin, 8, 5, $$1, (float) (Math.PI / 2));
                    }
                }
                if ($$2 == null) {
                    this.stuck = true;
                    return;
                }
                this.dolphin.m_21563_().setLookAt($$2.x, $$2.y, $$2.z, (float) (this.dolphin.getMaxHeadYRot() + 20), (float) this.dolphin.getMaxHeadXRot());
                this.dolphin.m_21573_().moveTo($$2.x, $$2.y, $$2.z, 1.3);
                if ($$0.random.nextInt(this.m_183277_(80)) == 0) {
                    $$0.broadcastEntityEvent(this.dolphin, (byte) 38);
                }
            }
        }
    }

    static class DolphinSwimWithPlayerGoal extends Goal {

        private final Dolphin dolphin;

        private final double speedModifier;

        @Nullable
        private Player player;

        DolphinSwimWithPlayerGoal(Dolphin dolphin0, double double1) {
            this.dolphin = dolphin0;
            this.speedModifier = double1;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            this.player = this.dolphin.m_9236_().m_45946_(Dolphin.SWIM_WITH_PLAYER_TARGETING, this.dolphin);
            return this.player == null ? false : this.player.isSwimming() && this.dolphin.m_5448_() != this.player;
        }

        @Override
        public boolean canContinueToUse() {
            return this.player != null && this.player.isSwimming() && this.dolphin.m_20280_(this.player) < 256.0;
        }

        @Override
        public void start() {
            this.player.m_147207_(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 100), this.dolphin);
        }

        @Override
        public void stop() {
            this.player = null;
            this.dolphin.m_21573_().stop();
        }

        @Override
        public void tick() {
            this.dolphin.m_21563_().setLookAt(this.player, (float) (this.dolphin.getMaxHeadYRot() + 20), (float) this.dolphin.getMaxHeadXRot());
            if (this.dolphin.m_20280_(this.player) < 6.25) {
                this.dolphin.m_21573_().stop();
            } else {
                this.dolphin.m_21573_().moveTo(this.player, this.speedModifier);
            }
            if (this.player.isSwimming() && this.player.m_9236_().random.nextInt(6) == 0) {
                this.player.m_147207_(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 100), this.dolphin);
            }
        }
    }

    class PlayWithItemsGoal extends Goal {

        private int cooldown;

        @Override
        public boolean canUse() {
            if (this.cooldown > Dolphin.this.f_19797_) {
                return false;
            } else {
                List<ItemEntity> $$0 = Dolphin.this.m_9236_().m_6443_(ItemEntity.class, Dolphin.this.m_20191_().inflate(8.0, 8.0, 8.0), Dolphin.ALLOWED_ITEMS);
                return !$$0.isEmpty() || !Dolphin.this.m_6844_(EquipmentSlot.MAINHAND).isEmpty();
            }
        }

        @Override
        public void start() {
            List<ItemEntity> $$0 = Dolphin.this.m_9236_().m_6443_(ItemEntity.class, Dolphin.this.m_20191_().inflate(8.0, 8.0, 8.0), Dolphin.ALLOWED_ITEMS);
            if (!$$0.isEmpty()) {
                Dolphin.this.m_21573_().moveTo((Entity) $$0.get(0), 1.2F);
                Dolphin.this.m_5496_(SoundEvents.DOLPHIN_PLAY, 1.0F, 1.0F);
            }
            this.cooldown = 0;
        }

        @Override
        public void stop() {
            ItemStack $$0 = Dolphin.this.m_6844_(EquipmentSlot.MAINHAND);
            if (!$$0.isEmpty()) {
                this.drop($$0);
                Dolphin.this.m_8061_(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                this.cooldown = Dolphin.this.f_19797_ + Dolphin.this.f_19796_.nextInt(100);
            }
        }

        @Override
        public void tick() {
            List<ItemEntity> $$0 = Dolphin.this.m_9236_().m_6443_(ItemEntity.class, Dolphin.this.m_20191_().inflate(8.0, 8.0, 8.0), Dolphin.ALLOWED_ITEMS);
            ItemStack $$1 = Dolphin.this.m_6844_(EquipmentSlot.MAINHAND);
            if (!$$1.isEmpty()) {
                this.drop($$1);
                Dolphin.this.m_8061_(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            } else if (!$$0.isEmpty()) {
                Dolphin.this.m_21573_().moveTo((Entity) $$0.get(0), 1.2F);
            }
        }

        private void drop(ItemStack itemStack0) {
            if (!itemStack0.isEmpty()) {
                double $$1 = Dolphin.this.m_20188_() - 0.3F;
                ItemEntity $$2 = new ItemEntity(Dolphin.this.m_9236_(), Dolphin.this.m_20185_(), $$1, Dolphin.this.m_20189_(), itemStack0);
                $$2.setPickUpDelay(40);
                $$2.setThrower(Dolphin.this.m_20148_());
                float $$3 = 0.3F;
                float $$4 = Dolphin.this.f_19796_.nextFloat() * (float) (Math.PI * 2);
                float $$5 = 0.02F * Dolphin.this.f_19796_.nextFloat();
                $$2.m_20334_((double) (0.3F * -Mth.sin(Dolphin.this.m_146908_() * (float) (Math.PI / 180.0)) * Mth.cos(Dolphin.this.m_146909_() * (float) (Math.PI / 180.0)) + Mth.cos($$4) * $$5), (double) (0.3F * Mth.sin(Dolphin.this.m_146909_() * (float) (Math.PI / 180.0)) * 1.5F), (double) (0.3F * Mth.cos(Dolphin.this.m_146908_() * (float) (Math.PI / 180.0)) * Mth.cos(Dolphin.this.m_146909_() * (float) (Math.PI / 180.0)) + Mth.sin($$4) * $$5));
                Dolphin.this.m_9236_().m_7967_($$2);
            }
        }
    }
}