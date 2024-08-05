package net.minecraft.world.entity.animal;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class Chicken extends Animal {

    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS, Items.PITCHER_POD);

    public float flap;

    public float flapSpeed;

    public float oFlapSpeed;

    public float oFlap;

    public float flapping = 1.0F;

    private float nextFlap = 1.0F;

    public int eggTime = this.f_19796_.nextInt(6000) + 6000;

    public boolean isChickenJockey;

    public Chicken(EntityType<? extends Chicken> entityTypeExtendsChicken0, Level level1) {
        super(entityTypeExtendsChicken0, level1);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new PanicGoal(this, 1.4));
        this.f_21345_.addGoal(2, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(3, new TemptGoal(this, 1.0, FOOD_ITEMS, false));
        this.f_21345_.addGoal(4, new FollowParentGoal(this, 1.1));
        this.f_21345_.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(7, new RandomLookAroundGoal(this));
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return this.m_6162_() ? entityDimensions1.height * 0.85F : entityDimensions1.height * 0.92F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed = this.flapSpeed + (this.m_20096_() ? -1.0F : 4.0F) * 0.3F;
        this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
        if (!this.m_20096_() && this.flapping < 1.0F) {
            this.flapping = 1.0F;
        }
        this.flapping *= 0.9F;
        Vec3 $$0 = this.m_20184_();
        if (!this.m_20096_() && $$0.y < 0.0) {
            this.m_20256_($$0.multiply(1.0, 0.6, 1.0));
        }
        this.flap = this.flap + this.flapping * 2.0F;
        if (!this.m_9236_().isClientSide && this.m_6084_() && !this.m_6162_() && !this.isChickenJockey() && --this.eggTime <= 0) {
            this.m_5496_(SoundEvents.CHICKEN_EGG, 1.0F, (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F + 1.0F);
            this.m_19998_(Items.EGG);
            this.m_146850_(GameEvent.ENTITY_PLACE);
            this.eggTime = this.f_19796_.nextInt(6000) + 6000;
        }
    }

    @Override
    protected boolean isFlapping() {
        return this.f_146794_ > this.nextFlap;
    }

    @Override
    protected void onFlap() {
        this.nextFlap = this.f_146794_ + this.flapSpeed / 2.0F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.CHICKEN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.CHICKEN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CHICKEN_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
        this.m_5496_(SoundEvents.CHICKEN_STEP, 0.15F, 1.0F);
    }

    @Nullable
    public Chicken getBreedOffspring(ServerLevel serverLevel0, AgeableMob ageableMob1) {
        return EntityType.CHICKEN.create(serverLevel0);
    }

    @Override
    public boolean isFood(ItemStack itemStack0) {
        return FOOD_ITEMS.test(itemStack0);
    }

    @Override
    public int getExperienceReward() {
        return this.isChickenJockey() ? 10 : super.getExperienceReward();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        this.isChickenJockey = compoundTag0.getBoolean("IsChickenJockey");
        if (compoundTag0.contains("EggLayTime")) {
            this.eggTime = compoundTag0.getInt("EggLayTime");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putBoolean("IsChickenJockey", this.isChickenJockey);
        compoundTag0.putInt("EggLayTime", this.eggTime);
    }

    @Override
    public boolean removeWhenFarAway(double double0) {
        return this.isChickenJockey();
    }

    @Override
    protected void positionRider(Entity entity0, Entity.MoveFunction entityMoveFunction1) {
        super.m_19956_(entity0, entityMoveFunction1);
        float $$2 = Mth.sin(this.f_20883_ * (float) (Math.PI / 180.0));
        float $$3 = Mth.cos(this.f_20883_ * (float) (Math.PI / 180.0));
        float $$4 = 0.1F;
        float $$5 = 0.0F;
        entityMoveFunction1.accept(entity0, this.m_20185_() + (double) (0.1F * $$2), this.m_20227_(0.5) + entity0.getMyRidingOffset() + 0.0, this.m_20189_() - (double) (0.1F * $$3));
        if (entity0 instanceof LivingEntity) {
            ((LivingEntity) entity0).yBodyRot = this.f_20883_;
        }
    }

    public boolean isChickenJockey() {
        return this.isChickenJockey;
    }

    public void setChickenJockey(boolean boolean0) {
        this.isChickenJockey = boolean0;
    }
}