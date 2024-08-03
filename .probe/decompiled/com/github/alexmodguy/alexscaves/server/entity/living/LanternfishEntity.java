package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.mojang.datafixers.DataFixUtils;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class LanternfishEntity extends WaterAnimal implements Bucketable {

    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(LanternfishEntity.class, EntityDataSerializers.BOOLEAN);

    private float landProgress;

    private float prevLandProgress;

    private float circleSpeed = 1.0F;

    private float fishPitch = 0.0F;

    private float prevFishPitch = 0.0F;

    private int baitballCooldown = 100 + this.f_19796_.nextInt(100);

    private int circleTime = 0;

    private int maxCircleTime = 300;

    private BlockPos circlePos;

    private LanternfishEntity groupLeader;

    private int groupSize = 1;

    public LanternfishEntity(EntityType<? extends WaterAnimal> type, Level level) {
        super(type, level);
        this.f_21342_ = new LanternfishEntity.LanternfishMoveControl();
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(FROM_BUCKET, false);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new LanternfishEntity.SwimInSchoolGoal(this));
        this.f_21345_.addGoal(1, new LanternfishEntity.JoinSchoolGoal(this));
        this.f_21345_.addGoal(2, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.15).add(Attributes.MAX_HEALTH, 2.0);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.m_21515_() && this.m_20072_()) {
            this.m_19920_(this.m_6113_(), travelVector);
            Vec3 delta = this.m_20184_();
            this.m_6478_(MoverType.SELF, delta);
            this.m_20256_(delta.scale(0.9));
        } else {
            super.m_7023_(travelVector);
        }
    }

    @Override
    protected void playSwimSound(float f) {
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.FISH_SWIM;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.m_8023_() || this.fromBucket();
    }

    @Override
    public boolean removeWhenFarAway(double dist) {
        return !this.fromBucket() && !this.m_8077_();
    }

    @Override
    public boolean fromBucket() {
        return this.f_19804_.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean sit) {
        this.f_19804_.set(FROM_BUCKET, sit);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        compound.putBoolean("FromBucket", this.fromBucket());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.setFromBucket(compound.getBoolean("FromBucket"));
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevLandProgress = this.landProgress;
        this.prevFishPitch = this.fishPitch;
        boolean grounded = this.m_20096_() && !this.m_20072_();
        if (grounded && this.landProgress < 5.0F) {
            this.landProgress++;
        }
        if (!grounded && this.landProgress > 0.0F) {
            this.landProgress--;
        }
        this.fishPitch = Mth.clamp((float) this.m_20184_().y * 3.0F, -1.4F, 1.4F) * (-180.0F / (float) Math.PI);
        if (!this.m_20072_() && this.m_6084_() && this.m_20096_()) {
            this.m_20256_(this.m_20184_().add((double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.2F), 0.5, (double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.2F)));
            this.m_146922_(this.f_19796_.nextFloat() * 360.0F);
            this.m_5496_(ACSoundRegistry.LANTERNFISH_FLOP.get(), this.m_6121_(), this.m_6100_());
        }
        if (this.baitballCooldown > 0) {
            this.baitballCooldown--;
        }
    }

    public float getFishPitch(float partialTick) {
        return this.prevFishPitch + (this.fishPitch - this.prevFishPitch) * partialTick;
    }

    @Override
    protected void handleAirSupply(int prevAir) {
        if (this.m_6084_() && !this.m_20072_()) {
            this.m_20301_(prevAir - 1);
            if (this.m_20146_() == -20) {
                this.m_20301_(0);
                this.m_6469_(this.m_269291_().dryOut(), 2.0F);
            }
        } else {
            this.m_20301_(200);
        }
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, this.m_20186_() - this.f_19855_, this.m_20189_() - this.f_19856_);
        float f2 = Math.min(f1 * 10.0F, 1.0F);
        this.f_267362_.update(f2, 0.4F);
    }

    public float getLandProgress(float partialTicks) {
        return (this.prevLandProgress + (this.landProgress - this.prevLandProgress) * partialTicks) * 0.2F;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        return (InteractionResult) Bucketable.bucketMobPickup(player, hand, this).orElse(super.m_6071_(player, hand));
    }

    @Override
    public void saveToBucketTag(@Nonnull ItemStack bucket) {
        if (this.m_8077_()) {
            bucket.setHoverName(this.m_7770_());
        }
        CompoundTag platTag = new CompoundTag();
        this.addAdditionalSaveData(platTag);
        CompoundTag compound = bucket.getOrCreateTag();
        compound.put("FishBucketTag", platTag);
    }

    @Override
    public void loadFromBucketTag(@Nonnull CompoundTag compound) {
        if (compound.contains("FishBucketTag")) {
            this.readAdditionalSaveData(compound.getCompound("FishBucketTag"));
        }
        this.m_20301_(2000);
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(ACItemRegistry.LANTERNFISH_BUCKET.get());
    }

    @Nonnull
    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    public void leaveGroup() {
        if (this.groupLeader != null) {
            this.groupLeader.decreaseGroupSize();
        }
        this.groupLeader = null;
    }

    protected boolean hasNoLeader() {
        return !this.hasGroupLeader();
    }

    public boolean hasGroupLeader() {
        return this.groupLeader != null && this.groupLeader.m_6084_();
    }

    private void increaseGroupSize() {
        this.groupSize++;
    }

    private void decreaseGroupSize() {
        this.groupSize--;
    }

    public boolean canGroupGrow() {
        return this.isGroupLeader() && this.groupSize < this.getMaxGroupSize();
    }

    private int getMaxGroupSize() {
        return 20;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return this.getMaxGroupSize();
    }

    @Override
    public boolean isMaxGroupSizeReached(int sizeIn) {
        return false;
    }

    public boolean isGroupLeader() {
        return this.groupSize > 1;
    }

    public boolean inRangeOfGroupLeader() {
        return this.m_20280_(this.groupLeader) <= 121.0;
    }

    public static boolean checkLanternfishSpawnRules(EntityType<? extends LivingEntity> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource randomSource) {
        return level.m_6425_(pos).is(FluidTags.WATER) && pos.m_123342_() < level.m_5736_() - 30;
    }

    public void moveToGroupLeader() {
        if (this.hasGroupLeader()) {
            this.m_21573_().moveTo(this.groupLeader.m_20185_(), this.groupLeader.m_20186_(), this.groupLeader.m_20189_(), 1.0);
        }
    }

    private void doInitialPosing(LevelAccessor world) {
        BlockPos down = this.m_20183_();
        while (!world.m_6425_(down).isEmpty() && down.m_123342_() > world.m_141937_()) {
            down = down.below();
        }
        this.m_6034_((double) ((float) down.m_123341_() + 0.5F), (double) (down.m_123342_() + 2), (double) ((float) down.m_123343_() + 0.5F));
    }

    public LanternfishEntity createAndSetLeader(LanternfishEntity leader) {
        this.groupLeader = leader;
        leader.increaseGroupSize();
        return leader;
    }

    public void createFromStream(Stream<LanternfishEntity> stream) {
        stream.limit((long) (this.getMaxGroupSize() - this.groupSize)).filter(fishe -> fishe != this).forEach(fishe -> fishe.createAndSetLeader(this));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (spawnDataIn == null) {
            spawnDataIn = new LanternfishEntity.GroupData(this);
        } else {
            this.createAndSetLeader(((LanternfishEntity.GroupData) spawnDataIn).groupLeader);
        }
        if (reason == MobSpawnType.NATURAL) {
            this.doInitialPosing(worldIn);
        }
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public boolean isCircling() {
        return this.circlePos != null && this.circleTime < this.maxCircleTime;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.LANTERNFISH_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.LANTERNFISH_HURT.get();
    }

    public static class GroupData extends AgeableMob.AgeableMobGroupData {

        public final LanternfishEntity groupLeader;

        public GroupData(LanternfishEntity groupLeaderIn) {
            super(0.05F);
            this.groupLeader = groupLeaderIn;
        }
    }

    class JoinSchoolGoal extends Goal {

        private static final int INTERVAL_TICKS = 200;

        private final LanternfishEntity mob;

        private int timeToRecalcPath;

        private int nextStartTick;

        public JoinSchoolGoal(LanternfishEntity fish) {
            this.mob = fish;
            this.nextStartTick = this.nextStartTick(fish);
        }

        protected int nextStartTick(LanternfishEntity fish) {
            return m_186073_(100 + fish.m_217043_().nextInt(100) % 20);
        }

        @Override
        public boolean canUse() {
            if (this.mob.isGroupLeader() || this.mob.isCircling()) {
                return false;
            } else if (this.mob.hasGroupLeader()) {
                return true;
            } else if (this.nextStartTick > 0) {
                this.nextStartTick--;
                return false;
            } else {
                this.nextStartTick = this.nextStartTick(this.mob);
                Predicate<LanternfishEntity> predicate = p_25258_ -> p_25258_.canGroupGrow() || !p_25258_.hasGroupLeader();
                List<LanternfishEntity> list = this.mob.m_9236_().m_6443_(LanternfishEntity.class, this.mob.m_20191_().inflate(8.0, 8.0, 8.0), predicate);
                LanternfishEntity cc = (LanternfishEntity) DataFixUtils.orElse(list.stream().filter(LanternfishEntity::canGroupGrow).findAny(), this.mob);
                cc.createFromStream(list.stream().filter(p_25255_ -> !p_25255_.hasGroupLeader()));
                return this.mob.hasGroupLeader();
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.mob.hasGroupLeader() && this.mob.inRangeOfGroupLeader() && !this.mob.isCircling();
        }

        @Override
        public void start() {
            this.timeToRecalcPath = 0;
        }

        @Override
        public void stop() {
            this.mob.leaveGroup();
        }

        @Override
        public void tick() {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = this.m_183277_(10);
                this.mob.moveToGroupLeader();
            }
        }
    }

    private class LanternfishMoveControl extends MoveControl {

        public LanternfishMoveControl() {
            super(LanternfishEntity.this);
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO && this.f_24974_.m_20072_()) {
                Vec3 vector3d = new Vec3(this.f_24975_ - this.f_24974_.m_20185_(), this.f_24976_ - this.f_24974_.m_20186_(), this.f_24977_ - this.f_24974_.m_20189_());
                double d5 = vector3d.length();
                double maxDist = this.f_24974_.m_20191_().getSize() > 1.0 ? 1.0 : this.f_24974_.m_20191_().getSize();
                if (d5 < maxDist) {
                    this.f_24981_ = MoveControl.Operation.WAIT;
                    this.f_24974_.m_20256_(this.f_24974_.m_20184_().scale(0.85));
                } else {
                    this.f_24974_.m_20256_(this.f_24974_.m_20184_().add(vector3d.scale(this.f_24978_ * 0.02F / d5)));
                    Vec3 vector3d1 = this.f_24974_.m_20184_();
                    float f = -((float) Mth.atan2(vector3d1.x, vector3d1.z)) * 180.0F / (float) Math.PI;
                    this.f_24974_.m_146922_(Mth.approachDegrees(this.f_24974_.m_146908_(), f, 10.0F));
                    this.f_24974_.f_20883_ = this.f_24974_.m_146908_();
                }
            }
        }
    }

    private class SwimInSchoolGoal extends Goal {

        private final LanternfishEntity fish;

        float circleDistance = 3.0F;

        boolean clockwise = false;

        public SwimInSchoolGoal(LanternfishEntity fish) {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
            this.fish = fish;
        }

        @Override
        public boolean canUse() {
            return this.fish.isGroupLeader() || this.fish.hasNoLeader() || this.fish.hasGroupLeader() && this.fish.groupLeader.circlePos != null;
        }

        @Override
        public void tick() {
            if (this.fish.circleTime > this.fish.maxCircleTime) {
                this.fish.circleTime = 0;
                this.fish.circlePos = null;
            }
            if (this.fish.circlePos != null && this.fish.circleTime <= this.fish.maxCircleTime) {
                this.fish.circleTime++;
                Vec3 movePos = this.orbitAroundPos(this.fish.circlePos);
                this.fish.m_21573_().moveTo(movePos.x(), movePos.y(), movePos.z(), (double) this.fish.circleSpeed);
            } else if (this.fish.isGroupLeader()) {
                if (this.fish.baitballCooldown == 0) {
                    this.fish.baitballCooldown = 100 + this.fish.f_19796_.nextInt(150);
                    if (this.fish.circlePos == null || this.fish.circleTime >= this.fish.maxCircleTime) {
                        this.fish.circleTime = 0;
                        this.fish.maxCircleTime = 100 + this.fish.f_19796_.nextInt(200);
                        this.circleDistance = 1.0F + this.fish.f_19796_.nextFloat() * 1.5F;
                        this.fish.circleSpeed = 0.75F + this.fish.f_19796_.nextFloat() * 0.5F;
                        this.clockwise = this.fish.f_19796_.nextBoolean();
                        this.fish.circlePos = this.findSwimToPos(3);
                    }
                }
            } else if (this.fish.f_19796_.nextInt(40) == 0 || this.fish.hasNoLeader()) {
                Vec3 result = Vec3.atCenterOf(this.findSwimToPos(6));
                this.fish.m_21573_().moveTo(result.x, result.y, result.z, 1.0);
            } else if (this.fish.hasGroupLeader() && this.fish.groupLeader.circlePos != null && this.fish.circlePos == null) {
                this.fish.circlePos = this.fish.groupLeader.circlePos;
                this.fish.circleTime = this.fish.groupLeader.circleTime;
                this.fish.maxCircleTime = this.fish.groupLeader.maxCircleTime;
                this.circleDistance = 1.0F + this.fish.f_19796_.nextFloat() * 1.5F;
                this.clockwise = this.fish.f_19796_.nextBoolean();
                this.fish.circleSpeed = 0.75F + this.fish.f_19796_.nextFloat() * 0.5F;
            }
        }

        public BlockPos findSwimToPos(int range) {
            int fishY = this.fish.m_146904_();
            BlockPos.MutableBlockPos move = new BlockPos.MutableBlockPos();
            move.set(this.fish.m_20185_(), this.fish.m_20186_() + 1.0, this.fish.m_20189_());
            while (move.m_123342_() < LanternfishEntity.this.m_9236_().m_151558_() && LanternfishEntity.this.m_9236_().getFluidState(move).is(FluidTags.WATER)) {
                move.move(0, 5, 0);
            }
            int surfaceY = move.m_123342_();
            move.set(this.fish.m_20185_(), this.fish.m_20186_() - 1.0, this.fish.m_20189_());
            while (move.m_123342_() > LanternfishEntity.this.m_9236_().m_141937_() && LanternfishEntity.this.m_9236_().getFluidState(move).is(FluidTags.WATER)) {
                move.move(0, -5, 0);
            }
            int floorY = move.m_123342_();
            int oceanHeight = surfaceY - floorY;
            BlockPos base;
            if (LanternfishEntity.this.m_9236_().isNight()) {
                base = this.fish.m_20183_().atY(Mth.clamp(fishY, floorY + (int) ((float) oceanHeight * 0.85F), surfaceY));
            } else {
                base = this.fish.m_20183_().atY(Mth.clamp(fishY, floorY + (int) ((float) oceanHeight * 0.25F), surfaceY - (int) ((float) oceanHeight * 0.85F)));
            }
            for (int i = 0; i < 15; i++) {
                BlockPos blockPos = base.offset(LanternfishEntity.this.f_19796_.nextInt(range) - range / 2, LanternfishEntity.this.f_19796_.nextInt(range) - range / 2, LanternfishEntity.this.f_19796_.nextInt(range) - range / 2);
                if (this.fish.m_9236_().getFluidState(blockPos).is(FluidTags.WATER) && blockPos.m_123342_() > LanternfishEntity.this.m_9236_().m_141937_() + 1) {
                    return blockPos;
                }
            }
            return base;
        }

        public Vec3 orbitAroundPos(BlockPos target) {
            float prog = 1.0F - (float) this.fish.circleTime / (float) this.fish.maxCircleTime;
            float angle = (float) (Math.PI / 18) * this.fish.circleSpeed * (float) (this.clockwise ? -this.fish.circleTime : this.fish.circleTime);
            double extraX = (double) ((this.circleDistance * prog + 1.75F) * Mth.sin(angle));
            double extraY = Math.sin((double) (1.0F + (float) this.fish.m_19879_() * 0.2F + (float) this.fish.circleTime * 0.2F));
            double extraZ = (double) ((this.circleDistance * prog + 1.75F) * prog * Mth.cos(angle));
            return new Vec3((double) ((float) target.m_123341_() + 0.5F) + extraX, Math.max((double) ((float) target.m_123342_() + 0.5F) + extraY, -62.0), (double) ((float) target.m_123343_() + 0.5F) + extraZ);
        }
    }
}