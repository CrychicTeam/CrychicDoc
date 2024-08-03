package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAISwimBottom;
import com.github.alexthe666.alexsmobs.entity.ai.AquaticMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FollowBoatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityFrilledShark extends WaterAnimal implements IAnimatedEntity, Bucketable {

    public static final Animation ANIMATION_ATTACK = Animation.create(17);

    private static final EntityDataAccessor<Boolean> DEPRESSURIZED = SynchedEntityData.defineId(EntityFrilledShark.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(EntityFrilledShark.class, EntityDataSerializers.BOOLEAN);

    public float prevOnLandProgress;

    public float onLandProgress;

    private int animationTick;

    private Animation currentAnimation;

    protected EntityFrilledShark(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.f_21342_ = new AquaticMoveController(this, 1.0F);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ARMOR, 0.0).add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DEPRESSURIZED, false);
        this.f_19804_.define(FROM_BUCKET, false);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new TryFindWaterGoal(this));
        this.f_21345_.addGoal(2, new EntityFrilledShark.AIMelee());
        this.f_21345_.addGoal(3, new AnimalAISwimBottom(this, 0.8F, 7));
        this.f_21345_.addGoal(4, new RandomSwimmingGoal(this, 0.8F, 3));
        this.f_21345_.addGoal(5, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(6, new FollowBoatGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new EntityAINearestTarget3D(this, Squid.class, 40, false, true, null));
        this.f_21346_.addGoal(2, new EntityAINearestTarget3D(this, EntityMimicOctopus.class, 70, false, true, null));
        this.f_21346_.addGoal(3, new EntityAINearestTarget3D(this, AbstractSchoolingFish.class, 100, false, true, null));
        this.f_21346_.addGoal(4, new EntityAINearestTarget3D(this, EntityBlobfish.class, 70, false, true, null));
        this.f_21346_.addGoal(5, new EntityAINearestTarget3D(this, Drowned.class, 4, false, true, null));
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.frilledSharkSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static boolean canFrilledSharkSpawn(EntityType<EntityFrilledShark> entityType, ServerLevelAccessor iServerWorld, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return reason == MobSpawnType.SPAWNER || iServerWorld.m_46801_(pos) && iServerWorld.m_46801_(pos.above());
    }

    @Override
    public boolean fromBucket() {
        return this.f_19804_.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean p_203706_1_) {
        this.f_19804_.set(FROM_BUCKET, p_203706_1_);
    }

    @Nonnull
    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        compound.putBoolean("FromBucket", this.fromBucket());
        compound.putBoolean("Depressurized", this.isDepressurized());
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.m_8023_() || this.fromBucket();
    }

    @Override
    public boolean removeWhenFarAway(double p_213397_1_) {
        return !this.fromBucket() && !this.m_8077_();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.setFromBucket(compound.getBoolean("FromBucket"));
        this.setDepressurized(compound.getBoolean("Depressurized"));
    }

    private void doInitialPosing(LevelAccessor world) {
        BlockPos down = this.m_20183_();
        while (!world.m_6425_(down).isEmpty() && down.m_123342_() > 1) {
            down = down.below();
        }
        this.m_6034_((double) ((float) down.m_123341_() + 0.5F), (double) (down.m_123342_() + 1), (double) ((float) down.m_123343_() + 0.5F));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (reason == MobSpawnType.NATURAL) {
            this.doInitialPosing(worldIn);
        }
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        return worldIn.m_45784_(this);
    }

    public boolean isDepressurized() {
        return this.f_19804_.get(DEPRESSURIZED);
    }

    public void setDepressurized(boolean depressurized) {
        this.f_19804_.set(DEPRESSURIZED, depressurized);
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new WaterBoundPathNavigation(this, worldIn);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.COD_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.COD_HURT;
    }

    @Nonnull
    @Override
    public ItemStack getBucketItemStack() {
        ItemStack stack = new ItemStack(AMItemRegistry.FRILLED_SHARK_BUCKET.get());
        if (this.m_8077_()) {
            stack.setHoverName(this.m_7770_());
        }
        return stack;
    }

    @Override
    public void saveToBucketTag(@Nonnull ItemStack bucket) {
        if (this.m_8077_()) {
            bucket.setHoverName(this.m_7770_());
        }
        CompoundTag platTag = new CompoundTag();
        this.addAdditionalSaveData(platTag);
        CompoundTag compound = bucket.getOrCreateTag();
        compound.put("FrilledSharkData", platTag);
    }

    @Override
    public void loadFromBucketTag(@Nonnull CompoundTag compound) {
        if (compound.contains("FrilledSharkData")) {
            this.readAdditionalSaveData(compound.getCompound("FrilledSharkData"));
        }
    }

    @Nonnull
    @Override
    protected InteractionResult mobInteract(@Nonnull Player player, @Nonnull InteractionHand hand) {
        return (InteractionResult) Bucketable.bucketMobPickup(player, hand, this).orElse(super.m_6071_(player, hand));
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.m_21515_() && this.m_20069_()) {
            this.m_19920_(this.m_6113_(), travelVector);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().multiply(0.9, 0.6, 0.9));
            if (this.m_5448_() == null) {
                this.m_20256_(this.m_20184_().add(0.0, -0.005, 0.0));
            }
        } else {
            super.m_7023_(travelVector);
        }
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, this.m_20186_() - this.f_19855_, this.m_20189_() - this.f_19856_);
        float f2 = Math.min(f1 * 8.0F, 1.0F);
        this.f_267362_.update(f2, 0.4F);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevOnLandProgress = this.onLandProgress;
        if (!this.m_20069_() && this.onLandProgress < 5.0F) {
            this.onLandProgress++;
        }
        if (this.m_20069_() && this.onLandProgress > 0.0F) {
            this.onLandProgress--;
        }
        if (this.m_20069_()) {
            this.m_20256_(this.m_20184_().multiply(1.0, 0.8, 1.0));
        }
        boolean clear = this.hasClearance();
        if (this.isDepressurized() && clear) {
            this.setDepressurized(false);
        }
        if (!this.isDepressurized() && !clear) {
            this.setDepressurized(true);
        }
        if (!this.m_9236_().isClientSide && this.m_5448_() != null && this.getAnimation() == ANIMATION_ATTACK && this.getAnimationTick() == 12) {
            float f1 = this.m_146908_() * (float) (Math.PI / 180.0);
            this.m_20256_(this.m_20184_().add((double) (-Mth.sin(f1) * 0.06F), 0.0, (double) (Mth.cos(f1) * 0.06F)));
            if (this.m_5448_().hurt(this.m_269291_().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getBaseValue())) {
                this.m_5448_().addEffect(new MobEffectInstance(AMEffectRegistry.EXSANGUINATION.get(), 60, 2));
                if (this.f_19796_.nextInt(15) == 0 && this.m_5448_() instanceof Squid) {
                    this.m_19998_(AMItemRegistry.SERRATED_SHARK_TOOTH.get());
                }
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof Drowned) {
            amount *= 0.5F;
        }
        return super.m_6469_(source, amount);
    }

    private boolean hasClearance() {
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
        for (int l1 = 0; l1 < 10; l1++) {
            BlockState blockstate = this.m_9236_().getBlockState(blockpos$mutable.set(this.m_20185_(), this.m_20186_() + (double) l1, this.m_20189_()));
            if (!blockstate.m_60819_().is(FluidTags.WATER)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Animation getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    public boolean isKaiju() {
        String s = ChatFormatting.stripFormatting(this.m_7755_().getString());
        return s != null && (s.toLowerCase().contains("kamata kun") || s.toLowerCase().contains("kamata-kun"));
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_ATTACK };
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
    public boolean doHurtTarget(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_ATTACK);
        }
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 68) {
            double d2 = this.f_19796_.nextGaussian() * 0.1;
            double d0 = this.f_19796_.nextGaussian() * 0.1;
            double d1 = this.f_19796_.nextGaussian() * 0.1;
            float radius = this.m_20205_() * 0.8F;
            float angle = (float) (Math.PI / 180.0) * this.f_20883_;
            double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
            double extraZ = (double) (radius * Mth.cos(angle));
            double x = this.m_20185_() + extraX + d0;
            double y = this.m_20186_() + (double) (this.m_20206_() * 0.15F) + d1;
            double z = this.m_20189_() + extraZ + d2;
            this.m_9236_().addParticle(AMParticleRegistry.TEETH_GLINT.get(), x, y, z, this.m_20184_().x, this.m_20184_().y, this.m_20184_().z);
        } else {
            super.m_7822_(id);
        }
    }

    private class AIMelee extends Goal {

        public AIMelee() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return EntityFrilledShark.this.m_5448_() != null && EntityFrilledShark.this.m_5448_().isAlive();
        }

        @Override
        public void tick() {
            LivingEntity target = EntityFrilledShark.this.m_5448_();
            double speed = 1.0;
            if (EntityFrilledShark.this.m_20270_(target) < 10.0F) {
                if ((double) EntityFrilledShark.this.m_20270_(target) < 1.9) {
                    EntityFrilledShark.this.doHurtTarget(target);
                    speed = 0.8F;
                } else {
                    speed = 0.6F;
                    EntityFrilledShark.this.m_21391_(target, 70.0F, 70.0F);
                    if (target instanceof Squid) {
                        Vec3 mouth = EntityFrilledShark.this.m_20182_();
                        float squidSpeed = 0.07F;
                        ((Squid) target).setMovementVector((float) (mouth.x - target.m_20185_()) * squidSpeed, (float) (mouth.y - target.m_20188_()) * squidSpeed, (float) (mouth.z - target.m_20189_()) * squidSpeed);
                        EntityFrilledShark.this.m_9236_().broadcastEntityEvent(EntityFrilledShark.this, (byte) 68);
                    }
                }
            }
            if (target instanceof Drowned || target instanceof Player) {
                speed = 1.0;
            }
            EntityFrilledShark.this.m_21573_().moveTo(target, speed);
        }
    }
}