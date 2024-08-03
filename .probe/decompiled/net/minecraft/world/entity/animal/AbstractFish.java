package net.minecraft.world.entity.animal;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractFish extends WaterAnimal implements Bucketable {

    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(AbstractFish.class, EntityDataSerializers.BOOLEAN);

    public AbstractFish(EntityType<? extends AbstractFish> entityTypeExtendsAbstractFish0, Level level1) {
        super(entityTypeExtendsAbstractFish0, level1);
        this.f_21342_ = new AbstractFish.FishMoveControl(this);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return entityDimensions1.height * 0.65F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 3.0);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.m_8023_() || this.fromBucket();
    }

    @Override
    public boolean removeWhenFarAway(double double0) {
        return !this.fromBucket() && !this.m_8077_();
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 8;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(FROM_BUCKET, false);
    }

    @Override
    public boolean fromBucket() {
        return this.f_19804_.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean boolean0) {
        this.f_19804_.set(FROM_BUCKET, boolean0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7380_(compoundTag0);
        compoundTag0.putBoolean("FromBucket", this.fromBucket());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7378_(compoundTag0);
        this.setFromBucket(compoundTag0.getBoolean("FromBucket"));
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(0, new PanicGoal(this, 1.25));
        this.f_21345_.addGoal(2, new AvoidEntityGoal(this, Player.class, 8.0F, 1.6, 1.4, EntitySelector.NO_SPECTATORS::test));
        this.f_21345_.addGoal(4, new AbstractFish.FishSwimGoal(this));
    }

    @Override
    protected PathNavigation createNavigation(Level level0) {
        return new WaterBoundPathNavigation(this, level0);
    }

    @Override
    public void travel(Vec3 vec0) {
        if (this.m_21515_() && this.m_20069_()) {
            this.m_19920_(0.01F, vec0);
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
    public void aiStep() {
        if (!this.m_20069_() && this.m_20096_() && this.f_19863_) {
            this.m_20256_(this.m_20184_().add((double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.05F), 0.4F, (double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.05F)));
            this.m_6853_(false);
            this.f_19812_ = true;
            this.m_5496_(this.getFlopSound(), this.m_6121_(), this.m_6100_());
        }
        super.m_8107_();
    }

    @Override
    protected InteractionResult mobInteract(Player player0, InteractionHand interactionHand1) {
        return (InteractionResult) Bucketable.bucketMobPickup(player0, interactionHand1, this).orElse(super.m_6071_(player0, interactionHand1));
    }

    @Override
    public void saveToBucketTag(ItemStack itemStack0) {
        Bucketable.saveDefaultDataToBucketTag(this, itemStack0);
    }

    @Override
    public void loadFromBucketTag(CompoundTag compoundTag0) {
        Bucketable.loadDefaultDataFromBucketTag(this, compoundTag0);
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    protected boolean canRandomSwim() {
        return true;
    }

    protected abstract SoundEvent getFlopSound();

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.FISH_SWIM;
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
    }

    static class FishMoveControl extends MoveControl {

        private final AbstractFish fish;

        FishMoveControl(AbstractFish abstractFish0) {
            super(abstractFish0);
            this.fish = abstractFish0;
        }

        @Override
        public void tick() {
            if (this.fish.m_204029_(FluidTags.WATER)) {
                this.fish.m_20256_(this.fish.m_20184_().add(0.0, 0.005, 0.0));
            }
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO && !this.fish.m_21573_().isDone()) {
                float $$0 = (float) (this.f_24978_ * this.fish.m_21133_(Attributes.MOVEMENT_SPEED));
                this.fish.m_7910_(Mth.lerp(0.125F, this.fish.m_6113_(), $$0));
                double $$1 = this.f_24975_ - this.fish.m_20185_();
                double $$2 = this.f_24976_ - this.fish.m_20186_();
                double $$3 = this.f_24977_ - this.fish.m_20189_();
                if ($$2 != 0.0) {
                    double $$4 = Math.sqrt($$1 * $$1 + $$2 * $$2 + $$3 * $$3);
                    this.fish.m_20256_(this.fish.m_20184_().add(0.0, (double) this.fish.m_6113_() * ($$2 / $$4) * 0.1, 0.0));
                }
                if ($$1 != 0.0 || $$3 != 0.0) {
                    float $$5 = (float) (Mth.atan2($$3, $$1) * 180.0F / (float) Math.PI) - 90.0F;
                    this.fish.m_146922_(this.m_24991_(this.fish.m_146908_(), $$5, 90.0F));
                    this.fish.f_20883_ = this.fish.m_146908_();
                }
            } else {
                this.fish.m_7910_(0.0F);
            }
        }
    }

    static class FishSwimGoal extends RandomSwimmingGoal {

        private final AbstractFish fish;

        public FishSwimGoal(AbstractFish abstractFish0) {
            super(abstractFish0, 1.0, 40);
            this.fish = abstractFish0;
        }

        @Override
        public boolean canUse() {
            return this.fish.canRandomSwim() && super.m_8036_();
        }
    }
}