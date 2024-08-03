package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIFindWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAILeaveWater;
import com.github.alexthe666.alexsmobs.entity.ai.BottomFeederAIWander;
import com.github.alexthe666.alexsmobs.entity.ai.SemiAquaticPathNavigator;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
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
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class EntityLobster extends WaterAnimal implements ISemiAquatic, Bucketable {

    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(EntityLobster.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData.defineId(EntityLobster.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityLobster.class, EntityDataSerializers.INT);

    public float attackProgress;

    public float prevAttackProgress;

    private int attackCooldown = 0;

    protected EntityLobster(EntityType type, Level p_i48565_2_) {
        super(type, p_i48565_2_);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.m_21441_(BlockPathTypes.WATER_BORDER, 0.0F);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 7;
    }

    @Override
    public boolean isMaxGroupSizeReached(int sizeIn) {
        return false;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 5.0).add(Attributes.ARMOR, 2.0).add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.MOVEMENT_SPEED, 0.15F);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.lobsterSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.LOBSTER_HURT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.LOBSTER_HURT.get();
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        return worldIn.m_45784_(this);
    }

    public static String getVariantName(int variant) {
        return switch(variant) {
            case 1 ->
                "blue";
            case 2 ->
                "yellow";
            case 3 ->
                "redblue";
            case 4 ->
                "black";
            case 5 ->
                "white";
            default ->
                "red";
        };
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new AnimalAIFindWater(this));
        this.f_21345_.addGoal(1, new AnimalAILeaveWater(this));
        this.f_21345_.addGoal(3, new BottomFeederAIWander(this, 1.0, 10, 50));
        this.f_21345_.addGoal(4, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.m_21515_() && this.m_20069_()) {
            this.m_19920_(this.m_6113_(), travelVector);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            if (this.f_20899_) {
                this.m_20256_(this.m_20184_().scale(1.4));
                this.m_20256_(this.m_20184_().add(0.0, 0.72, 0.0));
            } else {
                this.m_20256_(this.m_20184_().scale(0.4));
                this.m_20256_(this.m_20184_().add(0.0, -0.08, 0.0));
            }
        } else {
            super.m_7023_(travelVector);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(VARIANT, 0);
        this.f_19804_.define(ATTACK_TICK, 0);
        this.f_19804_.define(FROM_BUCKET, false);
    }

    @Nonnull
    @Override
    public ItemStack getBucketItemStack() {
        ItemStack stack = new ItemStack(AMItemRegistry.LOBSTER_BUCKET.get());
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
        Bucketable.saveDefaultDataToBucketTag(this, bucket);
        CompoundTag compoundnbt = bucket.getOrCreateTag();
        compoundnbt.putInt("BucketVariantTag", this.getVariant());
    }

    @Override
    public void loadFromBucketTag(@Nonnull CompoundTag compound) {
        Bucketable.loadDefaultDataFromBucketTag(this, compound);
        if (compound.contains("BucketVariantTag", 3)) {
            this.setVariant(compound.getInt("BucketVariantTag"));
        }
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.m_8023_() || this.fromBucket();
    }

    @Override
    public boolean removeWhenFarAway(double double0) {
        return !this.fromBucket() && !this.m_8077_();
    }

    @Nonnull
    @Override
    protected InteractionResult mobInteract(@Nonnull Player player, @Nonnull InteractionHand hand) {
        return (InteractionResult) Bucketable.bucketMobPickup(player, hand, this).orElse(super.m_6071_(player, hand));
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
        return worldIn.m_6425_(pos.below()).isEmpty() && worldIn.m_6425_(pos).is(FluidTags.WATER) ? 10.0F : super.m_5610_(pos, worldIn);
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        this.f_19804_.set(ATTACK_TICK, 5);
        return super.m_7327_(entityIn);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevAttackProgress = this.attackProgress;
        if (this.f_19804_.get(ATTACK_TICK) > 0) {
            if (this.attackProgress == 3.0F) {
                this.m_5496_(AMSoundRegistry.LOBSTER_ATTACK.get(), this.m_6121_(), this.m_6100_());
            }
            if (this.f_19804_.get(ATTACK_TICK) == 2 && this.m_5448_() != null && (double) this.m_20270_(this.m_5448_()) < 1.3) {
                this.m_5448_().hurt(this.m_269291_().mobAttack(this), 2.0F);
            }
            this.f_19804_.set(ATTACK_TICK, this.f_19804_.get(ATTACK_TICK) - 1);
            if (this.attackProgress < 5.0F) {
                this.attackProgress++;
            }
        } else if (this.attackProgress > 0.0F) {
            this.attackProgress--;
        }
        if (this.attackCooldown > 0) {
            this.attackCooldown--;
        }
        if (this.m_5448_() != null && this.m_20270_(this.m_5448_()) <= 1.0F && this.attackCooldown == 0) {
            this.m_21391_(this.m_5448_(), 180.0F, 20.0F);
            this.doHurtTarget(this.m_5448_());
            this.attackCooldown = 20;
        }
    }

    @Override
    protected void handleAirSupply(int air) {
    }

    public int getVariant() {
        return this.f_19804_.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.set(VARIANT, variant);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putBoolean("FromBucket", this.fromBucket());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setFromBucket(compound.getBoolean("FromBucket"));
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

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        float variantChange = this.m_217043_().nextFloat();
        if ((double) variantChange <= 1.0E-5) {
            this.setVariant(5);
        } else if ((double) variantChange <= 2.0E-5) {
            this.setVariant(4);
        } else if (variantChange <= 0.05F) {
            this.setVariant(3);
        } else if (variantChange <= 0.1F) {
            this.setVariant(2);
        } else if (variantChange <= 0.25F) {
            this.setVariant(1);
        } else {
            this.setVariant(0);
        }
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new SemiAquaticPathNavigator(this, worldIn) {

            @Override
            public boolean isStableDestination(BlockPos pos) {
                return this.f_26495_.getBlockState(pos).m_60819_().isEmpty();
            }
        };
    }

    @Override
    public boolean shouldEnterWater() {
        return true;
    }

    @Override
    public boolean shouldLeaveWater() {
        return false;
    }

    @Override
    public boolean shouldStopMoving() {
        return false;
    }

    @Override
    public int getWaterSearchRange() {
        return 5;
    }

    public static <T extends Mob> boolean canLobsterSpawn(EntityType type, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        boolean spawnBlock = worldIn.m_8055_(pos.below()).m_204336_(AMTagRegistry.LOBSTER_SPAWNS);
        return spawnBlock || worldIn.m_6425_(pos).is(FluidTags.WATER);
    }
}