package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.AcidSwimNodeEvaluator;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import java.util.EnumSet;
import javax.annotation.Nonnull;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;

public class RadgillEntity extends WaterAnimal implements Bucketable {

    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(RadgillEntity.class, EntityDataSerializers.BOOLEAN);

    private float landProgress;

    private float prevLandProgress;

    private float fishPitch = 0.0F;

    private float prevFishPitch = 0.0F;

    private boolean wasJustInAcid = false;

    public RadgillEntity(EntityType<? extends WaterAnimal> type, Level level) {
        super(type, level);
        this.f_21342_ = new RadgillEntity.AcidMoveControl();
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(FROM_BUCKET, false);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new RadgillEntity.WanderGoal());
        this.f_21345_.addGoal(2, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.MAX_HEALTH, 8.0);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level) {

            @Override
            protected PathFinder createPathFinder(int p_26598_) {
                this.f_26508_ = new AcidSwimNodeEvaluator(true);
                return new PathFinder(this.f_26508_, p_26598_);
            }

            @Override
            public boolean isInLiquid() {
                return RadgillEntity.this.isInLiquid();
            }
        };
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 2;
    }

    @Override
    public boolean isMaxGroupSizeReached(int i) {
        return false;
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
    protected int calculateFallDamage(float f, float f1) {
        return super.m_5639_(f, f1) - 5;
    }

    public static boolean checkRadgillSpawnRules(EntityType<? extends LivingEntity> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource randomSource) {
        return spawnType == MobSpawnType.SPAWNER || !level.m_6425_(pos).isEmpty() && level.m_6425_(pos).getFluidType() == ACFluidRegistry.ACID_FLUID_TYPE.get();
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevLandProgress = this.landProgress;
        this.prevFishPitch = this.fishPitch;
        boolean grounded = this.m_20096_() && !this.isInLiquid();
        if (grounded && this.landProgress < 5.0F) {
            this.landProgress++;
        }
        if (!grounded && this.landProgress > 0.0F) {
            this.landProgress--;
        }
        this.fishPitch = Mth.clamp((float) this.m_20184_().y * 1.8F, -1.0F, 1.0F) * (-180.0F / (float) Math.PI);
        boolean inAcid = this.isInAcid();
        if (inAcid != this.wasJustInAcid) {
            for (int i = 0; i < 5 + this.f_19796_.nextInt(5); i++) {
                this.m_9236_().addParticle(ACParticleRegistry.RADGILL_SPLASH.get(), this.m_20208_(0.8F), this.m_20191_().minY + 0.1F, this.m_20262_(0.8F), (this.f_19796_.nextDouble() - 0.5) * 0.3F, (double) (0.1F + this.f_19796_.nextFloat() * 0.3F), (this.f_19796_.nextDouble() - 0.5) * 0.3F);
            }
            this.wasJustInAcid = inAcid;
        }
        if (!this.isInLiquid() && this.m_6084_() && this.m_20096_() && this.f_19796_.nextFloat() < 0.1F) {
            this.m_20256_(this.m_20184_().add((double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.2F), 0.5, (double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.2F)));
            this.m_146922_(this.f_19796_.nextFloat() * 360.0F);
            this.m_5496_(ACSoundRegistry.RADGILL_FLOP.get(), this.m_6121_(), this.m_6100_());
        }
    }

    public float getFishPitch(float partialTick) {
        return this.prevFishPitch + (this.fishPitch - this.prevFishPitch) * partialTick;
    }

    private boolean isInLiquid() {
        return this.m_20072_() || this.isInAcid();
    }

    @Override
    protected void handleAirSupply(int prevAir) {
        if (this.m_6084_() && !this.isInLiquid()) {
            this.m_20301_(prevAir - 1);
            if (this.m_20146_() == -20) {
                this.m_20301_(0);
                this.m_6469_(this.m_269291_().dryOut(), 2.0F);
            }
        } else {
            this.m_20301_(500);
        }
    }

    private boolean isInAcid() {
        return this.getFluidTypeHeight(ACFluidRegistry.ACID_FLUID_TYPE.get()) > 0.0;
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
        ItemStack itemstack = player.m_21120_(hand);
        InteractionResult type = super.m_6071_(player, hand);
        if (!type.consumesAction() && itemstack.getItem() == ACItemRegistry.ACID_BUCKET.get() && this.m_6084_()) {
            this.m_5496_(this.getPickupSound(), 1.0F, 1.0F);
            ItemStack itemstack1 = this.getBucketItemStack();
            this.saveToBucketTag(itemstack1);
            ItemStack itemstack2 = ItemUtils.createFilledResult(itemstack, player, itemstack1, false);
            player.m_21008_(hand, itemstack2);
            if (!this.m_9236_().isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, itemstack1);
            }
            this.m_146870_();
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else {
            return type;
        }
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
        ItemStack stack = new ItemStack(ACItemRegistry.RADGILL_BUCKET.get());
        if (this.m_8077_()) {
            stack.setHoverName(this.m_7770_());
        }
        return stack;
    }

    @Nonnull
    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.RADGILL_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.RADGILL_HURT.get();
    }

    class AcidMoveControl extends MoveControl {

        public AcidMoveControl() {
            super(RadgillEntity.this);
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO && RadgillEntity.this.isInLiquid()) {
                Vec3 vector3d = new Vec3(this.f_24975_ - RadgillEntity.this.m_20185_(), this.f_24976_ - RadgillEntity.this.m_20186_(), this.f_24977_ - RadgillEntity.this.m_20189_());
                double d5 = vector3d.length();
                if (d5 < RadgillEntity.this.m_20191_().getSize()) {
                    this.f_24981_ = MoveControl.Operation.WAIT;
                    RadgillEntity.this.m_20256_(RadgillEntity.this.m_20184_().scale(0.5));
                } else {
                    RadgillEntity.this.m_20256_(RadgillEntity.this.m_20184_().add(vector3d.scale(this.f_24978_ * 0.06 / d5)));
                    Vec3 vector3d1 = RadgillEntity.this.m_20184_();
                    float f = -((float) Mth.atan2(vector3d1.x, vector3d1.z)) * 180.0F / (float) Math.PI;
                    RadgillEntity.this.m_146922_(Mth.approachDegrees(RadgillEntity.this.m_146908_(), f, 20.0F));
                    RadgillEntity.this.f_20883_ = RadgillEntity.this.m_146908_();
                }
            }
        }
    }

    private class WanderGoal extends Goal {

        private BlockPos target;

        private boolean isJump;

        private boolean hasJumped;

        private int timeout = 0;

        private WanderGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        private boolean isLiquidAt(BlockPos pos) {
            FluidState state = RadgillEntity.this.m_9236_().getFluidState(pos);
            return state.is(FluidTags.WATER) || state.getFluidType() == ACFluidRegistry.ACID_FLUID_TYPE.get();
        }

        private BlockPos findMoveToPos(boolean jump) {
            BlockPos fishPos = RadgillEntity.this.m_20183_();
            for (int i = 0; i < 15; i++) {
                BlockPos offset = fishPos.offset(RadgillEntity.this.f_19796_.nextInt(16) - 8, 0, RadgillEntity.this.f_19796_.nextInt(16) - 8);
                while (this.isLiquidAt(offset) && offset.m_123342_() < RadgillEntity.this.m_9236_().m_151558_()) {
                    offset = offset.above();
                }
                if (!this.isLiquidAt(offset) && this.isLiquidAt(offset.below())) {
                    BlockPos surface = offset.below();
                    if (jump) {
                        return surface.above(2 + RadgillEntity.this.f_19796_.nextInt(2));
                    }
                    surface = surface.below(1 + RadgillEntity.this.f_19796_.nextInt(4));
                    return this.isLiquidAt(surface) ? surface : null;
                }
            }
            return null;
        }

        @Override
        public boolean canUse() {
            if (!RadgillEntity.this.isInLiquid()) {
                return false;
            } else {
                if (RadgillEntity.this.m_217043_().nextInt(10) == 0) {
                    boolean jump = RadgillEntity.this.f_19796_.nextFloat() <= 0.4F;
                    BlockPos found = this.findMoveToPos(jump);
                    if (found != null) {
                        this.isJump = jump;
                        this.target = found;
                        return true;
                    }
                }
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return (RadgillEntity.this.isInLiquid() && !this.hasJumped || this.isJump) && RadgillEntity.this.m_20238_(Vec3.atCenterOf(this.target)) < 3.0 && this.timeout < 200;
        }

        @Override
        public void stop() {
            this.hasJumped = false;
            this.timeout = 0;
        }

        @Override
        public void tick() {
            this.timeout++;
            RadgillEntity.this.m_21573_().moveTo((double) ((float) this.target.m_123341_() + 0.5F), (double) ((float) this.target.m_123342_() + 0.25F), (double) ((float) this.target.m_123343_() + 0.5F), 1.0);
            double horizDistance = RadgillEntity.this.m_20275_((double) ((float) this.target.m_123341_() + 0.5F), RadgillEntity.this.m_20186_(), (double) ((float) this.target.m_123343_() + 0.5F));
            if (horizDistance < 16.0 && this.isJump && !this.hasJumped) {
                Vec3 targetVec = Vec3.atCenterOf(this.target);
                Vec3 vec3 = targetVec.subtract(RadgillEntity.this.m_20182_());
                if (vec3.length() < 1.0) {
                    vec3 = Vec3.ZERO;
                } else {
                    vec3 = vec3.normalize();
                }
                Vec3 vec31 = new Vec3(vec3.x * 0.8F, (double) (0.75F + RadgillEntity.this.f_19796_.nextFloat() * 0.3F), vec3.y * 0.8F);
                RadgillEntity.this.m_20256_(vec31);
                if (RadgillEntity.this.m_20186_() > (double) this.target.m_123342_()) {
                    this.hasJumped = true;
                } else {
                    RadgillEntity.this.m_7618_(EntityAnchorArgument.Anchor.EYES, targetVec);
                }
            }
        }
    }
}