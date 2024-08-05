package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIFindWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIHerdPanic;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAILeaveWater;
import com.github.alexthe666.alexsmobs.entity.ai.AquaticMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.entity.ai.GroundPathNavigatorWide;
import com.github.alexthe666.alexsmobs.entity.ai.SealAIBask;
import com.github.alexthe666.alexsmobs.entity.ai.SealAIDiveForItems;
import com.github.alexthe666.alexsmobs.entity.ai.SemiAquaticPathNavigator;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreathAirGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class EntitySeal extends Animal implements ISemiAquatic, IHerdPanic, ITargetsDroppedItems {

    private static final EntityDataAccessor<Float> SWIM_ANGLE = SynchedEntityData.defineId(EntitySeal.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Boolean> BASKING = SynchedEntityData.defineId(EntitySeal.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DIGGING = SynchedEntityData.defineId(EntitySeal.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> ARCTIC = SynchedEntityData.defineId(EntitySeal.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntitySeal.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> BOB_TICKS = SynchedEntityData.defineId(EntitySeal.class, EntityDataSerializers.INT);

    public float prevSwimAngle;

    public float prevBaskProgress;

    public float baskProgress;

    public float prevDigProgress;

    public float digProgress;

    public float prevBobbingProgress;

    public float bobbingProgress;

    public int revengeCooldown = 0;

    public UUID feederUUID = null;

    private int baskingTimer = 0;

    private int swimTimer = -1000;

    private int ticksSinceInWater = 0;

    private boolean isLandNavigator;

    public int fishFeedings = 0;

    protected EntitySeal(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.m_21441_(BlockPathTypes.WATER_BORDER, 0.0F);
        this.switchNavigator(false);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.SEAL_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.SEAL_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.SEAL_HURT.get();
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.MOVEMENT_SPEED, 0.18F);
    }

    public static boolean canSealSpawn(EntityType<? extends Animal> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        Holder<Biome> holder = worldIn.m_204166_(pos);
        if (!holder.is(Biomes.FROZEN_OCEAN) && !holder.is(Biomes.DEEP_FROZEN_OCEAN)) {
            boolean spawnBlock = worldIn.m_8055_(pos.below()).m_204336_(AMTagRegistry.SEAL_SPAWNS);
            return spawnBlock && worldIn.m_45524_(pos, 0) > 8;
        } else {
            return worldIn.m_45524_(pos, 0) > 8 && worldIn.m_8055_(pos.below()).m_60713_(Blocks.ICE);
        }
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new SealAIBask(this));
        this.f_21345_.addGoal(1, new BreathAirGoal(this));
        this.f_21345_.addGoal(2, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(3, new AnimalAIFindWater(this));
        this.f_21345_.addGoal(3, new AnimalAILeaveWater(this));
        this.f_21345_.addGoal(4, new AnimalAIHerdPanic(this, 1.6));
        this.f_21345_.addGoal(5, new MeleeAttackGoal(this, 1.0, true));
        this.f_21345_.addGoal(6, new SealAIDiveForItems(this));
        this.f_21345_.addGoal(7, new RandomSwimmingGoal(this, 1.0, 7));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(9, new AvoidEntityGoal(this, EntityOrca.class, 20.0F, 1.3, 1.0));
        this.f_21345_.addGoal(10, new TemptGoal(this, 1.1, Ingredient.of(AMTagRegistry.SEAL_FOODSTUFFS), false));
        this.f_21346_.addGoal(1, new NearestAttackableTargetGoal(this, EntityFlyingFish.class, 55, true, true, null));
        this.f_21346_.addGoal(2, new CreatureAITargetItems(this, false));
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new GroundPathNavigatorWide(this, this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new AquaticMoveController(this, 1.5F);
            this.f_21344_ = new SemiAquaticPathNavigator(this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean prev = super.hurt(source, amount);
        if (prev) {
            double range = 15.0;
            int fleeTime = 100 + this.m_217043_().nextInt(150);
            this.revengeCooldown = fleeTime;
            for (EntitySeal gaz : this.m_9236_().m_45976_(this.getClass(), this.m_20191_().inflate(15.0, 7.5, 15.0))) {
                gaz.revengeCooldown = fleeTime;
                gaz.setBasking(false);
            }
            this.setBasking(false);
        }
        return prev;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(SWIM_ANGLE, 0.0F);
        this.f_19804_.define(BASKING, false);
        this.f_19804_.define(DIGGING, false);
        this.f_19804_.define(ARCTIC, false);
        this.f_19804_.define(VARIANT, 0);
        this.f_19804_.define(BOB_TICKS, 0);
    }

    public boolean isTearsEasterEgg() {
        String s = ChatFormatting.stripFormatting(this.m_7755_().getString());
        return s != null && s.toLowerCase().contains("he was");
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, 0.0, this.m_20189_() - this.f_19856_);
        float f2 = Math.min(f1 * (this.m_20069_() ? 4.0F : 48.0F), 1.0F);
        this.f_267362_.update(f2, 0.4F);
    }

    public float getSwimAngle() {
        return this.f_19804_.get(SWIM_ANGLE);
    }

    public void setSwimAngle(float progress) {
        this.f_19804_.set(SWIM_ANGLE, progress);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevBaskProgress = this.baskProgress;
        this.prevDigProgress = this.digProgress;
        this.prevBobbingProgress = this.bobbingProgress;
        this.prevSwimAngle = this.getSwimAngle();
        boolean dig = this.isDigging() && this.m_20072_();
        float f2 = (float) (-((double) ((float) this.m_20184_().y) * 180.0F / (float) Math.PI));
        if (this.m_20069_()) {
            this.m_146926_(f2 * 2.5F);
            if (this.isLandNavigator) {
                this.switchNavigator(false);
            }
        } else if (!this.isLandNavigator) {
            this.switchNavigator(true);
        }
        if (this.isBasking()) {
            if (this.baskProgress < 5.0F) {
                this.baskProgress++;
            }
        } else if (this.baskProgress > 0.0F) {
            this.baskProgress--;
        }
        if (dig) {
            if (this.digProgress < 5.0F) {
                this.digProgress++;
            }
        } else if (this.digProgress > 0.0F) {
            this.digProgress--;
        }
        if (dig && this.m_9236_().getBlockState(this.m_20099_()).m_60815_()) {
            BlockPos posit = this.m_20099_();
            BlockState understate = this.m_9236_().getBlockState(posit);
            for (int i = 0; i < 4 + this.f_19796_.nextInt(2); i++) {
                double particleX = (double) ((float) posit.m_123341_() + this.f_19796_.nextFloat());
                double particleY = (double) ((float) posit.m_123342_() + 1.0F);
                double particleZ = (double) ((float) posit.m_123343_() + this.f_19796_.nextFloat());
                double motX = this.f_19796_.nextGaussian() * 0.02;
                double motY = (double) (0.1F + this.f_19796_.nextFloat() * 0.2F);
                double motZ = this.f_19796_.nextGaussian() * 0.02;
                this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, understate), particleX, particleY, particleZ, motX, motY, motZ);
            }
        }
        if (!this.m_9236_().isClientSide) {
            if (this.isBasking()) {
                if (this.m_21188_() != null || this.m_27593_() || this.revengeCooldown > 0 || this.m_20072_() || this.m_5448_() != null || this.baskingTimer > 1000 && this.m_217043_().nextInt(100) == 0) {
                    this.setBasking(false);
                }
            } else if (this.m_5448_() == null && !this.m_27593_() && this.m_21188_() == null && this.revengeCooldown == 0 && !this.isBasking() && this.baskingTimer == 0 && this.m_217043_().nextInt(15) == 0 && !this.m_20072_()) {
                this.setBasking(true);
            }
            if (this.revengeCooldown > 0) {
                this.revengeCooldown--;
            }
            if (this.revengeCooldown == 0 && this.m_21188_() != null) {
                this.m_6703_(null);
            }
            float threshold = 0.05F;
            if (this.m_20069_() && this.f_19859_ - this.m_146908_() > threshold) {
                this.setSwimAngle(this.getSwimAngle() + 2.0F);
            } else if (this.m_20069_() && this.f_19859_ - this.m_146908_() < -threshold) {
                this.setSwimAngle(this.getSwimAngle() - 2.0F);
            } else if (this.getSwimAngle() > 0.0F) {
                this.setSwimAngle(Math.max(this.getSwimAngle() - 10.0F, 0.0F));
            } else if (this.getSwimAngle() < 0.0F) {
                this.setSwimAngle(Math.min(this.getSwimAngle() + 10.0F, 0.0F));
            }
            this.setSwimAngle(Mth.clamp(this.getSwimAngle(), -70.0F, 70.0F));
            if (this.isBasking()) {
                this.baskingTimer++;
            } else {
                this.baskingTimer = 0;
            }
            if (this.m_20069_()) {
                this.swimTimer++;
                this.ticksSinceInWater = 0;
            } else {
                this.ticksSinceInWater++;
                this.swimTimer--;
            }
        }
        int bob = this.f_19804_.get(BOB_TICKS);
        if (bob > 0) {
            bob--;
            if (this.bobbingProgress < 5.0F) {
                this.bobbingProgress++;
            }
            this.f_19804_.set(BOB_TICKS, bob);
        } else {
            if (this.bobbingProgress > 0.0F) {
                this.bobbingProgress--;
            }
            if (!this.m_9236_().isClientSide && this.f_19796_.nextInt(300) == 0 && !this.m_20069_() && this.revengeCooldown == 0) {
                bob = 20 + this.f_19796_.nextInt(20);
                this.f_19804_.set(BOB_TICKS, bob);
            }
        }
    }

    public int getVariant() {
        return this.f_19804_.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.set(VARIANT, variant);
    }

    public boolean isBasking() {
        return this.f_19804_.get(BASKING);
    }

    public void setBasking(boolean basking) {
        this.f_19804_.set(BASKING, basking);
    }

    public boolean isDigging() {
        return this.f_19804_.get(DIGGING);
    }

    public void setDigging(boolean digging) {
        this.f_19804_.set(DIGGING, digging);
    }

    public boolean isArctic() {
        return this.f_19804_.get(ARCTIC);
    }

    public void setArctic(boolean arctic) {
        this.f_19804_.set(ARCTIC, arctic);
    }

    @Override
    public int getMaxAirSupply() {
        return 4800;
    }

    @Override
    protected int increaseAirSupply(int currentAir) {
        return this.getMaxAirSupply();
    }

    @Override
    public int getMaxHeadXRot() {
        return 1;
    }

    @Override
    public int getMaxHeadYRot() {
        return 1;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag dataTag) {
        this.setArctic(this.isBiomeArctic(worldIn, this.m_20183_()));
        int i;
        if (data instanceof EntitySeal.SealGroupData) {
            i = ((EntitySeal.SealGroupData) data).variant;
        } else {
            i = this.f_19796_.nextInt(2);
            data = new EntitySeal.SealGroupData(i);
        }
        this.setVariant(i);
        this.m_20301_(this.getMaxAirSupply());
        this.m_146926_(0.0F);
        return super.m_6518_(worldIn, difficultyIn, reason, data, dataTag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Arctic", this.isArctic());
        compound.putBoolean("Basking", this.isBasking());
        compound.putInt("BaskingTimer", this.baskingTimer);
        compound.putInt("SwimTimer", this.swimTimer);
        compound.putInt("FishFeedings", this.fishFeedings);
        compound.putInt("Variant", this.getVariant());
        if (this.feederUUID != null) {
            compound.putUUID("FeederUUID", this.feederUUID);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setArctic(compound.getBoolean("Arctic"));
        this.setBasking(compound.getBoolean("Basking"));
        this.baskingTimer = compound.getInt("BaskingTimer");
        this.swimTimer = compound.getInt("SwimTimer");
        this.fishFeedings = compound.getInt("FishFeedings");
        if (compound.hasUUID("FeederUUID")) {
            this.feederUUID = compound.getUUID("FeederUUID");
        }
        this.setVariant(compound.getInt("Variant"));
    }

    private boolean isBiomeArctic(LevelAccessor worldIn, BlockPos position) {
        return worldIn.m_204166_(position).is(AMTagRegistry.SPAWNS_WHITE_SEALS);
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.m_21515_() && this.m_20069_()) {
            this.m_19920_(this.m_6113_(), travelVector);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().scale(0.9));
            if (this.m_5448_() == null) {
                this.m_20256_(this.m_20184_().add(0.0, -0.005, 0.0));
            }
            if (this.isDigging()) {
                this.m_20256_(this.m_20184_().add(0.0, -0.02, 0.0));
            }
        } else {
            super.m_7023_(travelVector);
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == AMItemRegistry.LOBSTER_TAIL.get();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        EntitySeal seal = AMEntityRegistry.SEAL.get().create(serverWorld);
        seal.setArctic(this.isBiomeArctic(serverWorld, this.m_20183_()));
        return seal;
    }

    @Override
    public boolean shouldEnterWater() {
        return !this.shouldLeaveWater() && this.swimTimer <= -1000;
    }

    @Override
    public boolean shouldLeaveWater() {
        if (!this.m_20197_().isEmpty()) {
            return false;
        } else {
            return this.m_5448_() != null && !this.m_5448_().m_20069_() ? true : this.swimTimer > 600;
        }
    }

    @Override
    public boolean shouldStopMoving() {
        return this.isBasking();
    }

    @Override
    public int getWaterSearchRange() {
        return 32;
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return stack.is(AMTagRegistry.SEAL_FOODSTUFFS);
    }

    @Override
    public void onGetItem(ItemEntity e) {
        if (e.getItem().is(ItemTags.FISHES)) {
            this.fishFeedings++;
            this.m_146850_(GameEvent.EAT);
            this.m_5496_(SoundEvents.CAT_EAT, this.m_6121_(), this.m_6100_());
            Entity itemThrower = e.getOwner();
            if (this.fishFeedings >= 3) {
                if (itemThrower != null) {
                    this.feederUUID = itemThrower.getUUID();
                }
                this.fishFeedings = 0;
            }
        } else {
            this.feederUUID = null;
        }
        this.m_5634_(10.0F);
    }

    @Override
    public void onPanic() {
    }

    @Override
    public boolean canPanic() {
        return !this.isBasking();
    }

    public static class SealGroupData extends AgeableMob.AgeableMobGroupData {

        public final int variant;

        SealGroupData(int variant) {
            super(true);
            this.variant = variant;
        }
    }
}